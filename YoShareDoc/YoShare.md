# YoShare

# 域名
免费 tk 域名申请——[freenom](https://www.freenom.com/zh/index.html?lang=zh)
帮助——https://www.360zimeiti.com/youhuatuijian/118298.html

# Docker部署组件

## Mysql

```bash
docker run -p 3306:3306 --name mysql \
--restart always \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=P@ssw0rdMarson  \
-e TZ=Asia/Shanghai \
-d mysql:8.0.13 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --default-time_zone='+8:00'
```

* 参数说明:
  * -p 3306:3306：将容器的3306端口映射到主机的3306端口
  * -v /mydata/mysql/log:/var/log/mysql：将日志文件夹挂载到主机
  * -v /mydata/mysql/data:/var/lib/mysql/：将数据文件夹挂载到主机
  * -e MYSQL_ROOT_PASSWORD=root：初始化root用户的密码

进入运行mysql的docker容器：
```bash
docker exec -it mysql /bin/bash
```

使用mysql命令打开客户端：
```bash
mysql -uroot -proot --default-character-set=utf8;
``` 

创建数据库
```bash
create database yoshare character set utf8;
```

修改权限，使得任何ip都能访问：
```bash
alter user 'root'@'%' identified by 'root';
```

执行sql，把sql放到/mydata/mysql/data
```bash
use yoshare;
source /var/lib/mysql/yoshare.sql;
```

## Redis
```bash
docker run -p 6379:6379 --name redis \
-v /mydata/redis/data:/data \
--restart always \
-d redis:3.2 redis-server --appendonly yes
```

## Nginx
```bash
docker run -p 80:80 --name nginx \
-v /mydata/nginx/html:/usr/share/nginx/html \
-v /mydata/nginx/logs:/var/log/nginx  \
-v /mydata/nginx/conf:/etc/nginx \
-d nginx:1.22
```


## Minio
```bash
docker run -p 9000:9000 -p 9100:9100 --name minio \
-v /mydata/minio/data:/data \
-v /mydata/minio/config:/root/.minio \
-e "MINIO_ACCESS_KEY=admin" \
-e "MINIO_SECRET_KEY=123456789" \
--restart always \
-d minio/minio:RELEASE.2020-06-03T22-13-49Z server /data --console-address ":9100" -address ":9000"
```

## RabbitMQ
```bash
docker run -p 5672:5672 -p 15672:15672 --name rabbitmq \
-v /mydata/rabbitmq/data:/var/lib/rabbitmq \
-d rabbitmq:3.9-management
```

进入容器并开启管理功能：
```bash
docker exec -it rabbitmq /bin/bash
rabbitmq-plugins enable rabbitmq_management
```

## Mongo
```bash
docker run -p 27017:27017 --name mongo \
-v /mydata/mongo/db:/data/db \
-d mongo:4
```

## ElasticSearch
```shell
docker run -p 9200:9200 -p 9300:9300 --name elasticsearch \
-e "discovery.type=single-node" \
-e "cluster.name=elasticsearch" \
-e "ES_JAVA_OPTS=-Xms512m -Xmx1024m" \
-v /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
-v /mydata/elasticsearch/data:/usr/share/elasticsearch/data \
-d elasticsearch:7.17.3
```
```bash
chmod 777 /mydata/elasticsearch/data/
```
安装中文分词器IKAnalyzer，注意下载与Elasticsearch对应的版本，下载地址：https://github.com/medcl/elasticsearch-analysis-ik/releases

下载完成后解压到Elasticsearch的/mydata/elasticsearch/plugins目录下；
```bash
docker restart elasticsearch
```
访问会返回版本信息：http://127.0.0.1:9200


## Logstash

创建/mydata/logstash目录，并将Logstash的配置文件logstash.conf拷贝到该目录；

```bash
docker run --name logstash -p 4560:4560 -p 4561:4561 -p 4562:4562 -p 4563:4563 -p 5044:5044 \
--link elasticsearch:es \
-v /mydata/logstash/logstash.conf:/usr/share/logstash/pipeline/logstash.conf \
-d logstash:7.17.3
```

进入容器内部，安装json_lines插件。
```shell
logstash-plugin install logstash-codec-json_lines
```



## Kibana
```bash
docker run --name kibana -p 5601:5601 \
--link elasticsearch:es \
-e "elasticsearch.hosts=http://es:9200" \
-d kibana:7.17.3
```

访问地址进行测试：http://127.0.0.1:5601

## Filebeat
创建/mydata/filebeat目录，并将filebeat的配置文件filebeat.yml拷贝到该目录；
```bash
docker run --name filebeat \
--link logstash:ls \
-v /mydata/filebeat/filebeat.yml:/usr/share/filebeat/filebeat.yml \
-v /mydata/logs:/var/log/filebeat/ \
-d docker.elastic.co/beats/filebeat:7.17.3
```

## Docker-Compose
### 直接运行所有组件
```bash
docker-compose -f docker-compose-all.yml -p yoshare up -d
```
* 参数说明:
  * -f 指定配置文件
  * -p 指定工程名，网络名为"<工程名>_<网络名>"
  * -d detach挂到后台

### 分开运行
```bash
docker-compose -f docker-compose-env.yml -p yoshare up -d
docker-compose -f docker-compose-ext.yml -p yoshare up -d
```


## Kubernetes

```bash
minikube start --mount-string="/Users/yo/mydata:/mydata" --mount
minikube dashboard
bg && disown
```


```bash
kubectl apply -f YoShareDoc/kubernetes/env/elasticsearch-stateful.yaml
kubectl apply -f YoShareDoc/kubernetes/env/minio-stateful.yaml
kubectl apply -f YoShareDoc/kubernetes/env/mongo-stateful.yaml
kubectl apply -f YoShareDoc/kubernetes/env/mysql-stateful.yaml
kubectl apply -f YoShareDoc/kubernetes/env/rabbitmq-stateful.yaml
kubectl apply -f YoShareDoc/kubernetes/env/redis-stateful.yaml
```

```bash
kubectl delete -f YoShareDoc/kubernetes/env/elasticsearch-stateful.yaml
kubectl delete -f YoShareDoc/kubernetes/env/minio-stateful.yaml
kubectl delete -f YoShareDoc/kubernetes/env/mongo-stateful.yaml
kubectl delete -f YoShareDoc/kubernetes/env/mysql-stateful.yaml
kubectl delete -f YoShareDoc/kubernetes/env/rabbitmq-stateful.yaml
kubectl delete -f YoShareDoc/kubernetes/env/redis-stateful.yaml
```

```bash
skaffold dev
```

kubectl create namespace yoshare
