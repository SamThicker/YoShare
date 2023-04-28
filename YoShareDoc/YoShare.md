# YoShare
# 域名
免费 tk 域名申请——[freenom](https://www.freenom.com/zh/index.html?lang=zh)
帮助——https://www.360zimeiti.com/youhuatuijian/118298.html
# Docker部署组件
## Mysql
```bash
docker run -p 3306:3306 --name mysql \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-v /mydata/mysql/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root  \
--restart always \
-d mysql:5.7
```

docker run -p 3306:3306 --name mysql \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=P@ssw0rdMarson  \
--restart always \
-e TZ=Asia/Shanghai \
-d mysql:8.0.13 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --default-time_zone='+8:00'


docker exec -it mysql80 mysql_upgrade -uroot -p

alter user 'root'@'%' identified by 'P@ssw0rdMarson';

* 参数说明:
  * -p 3306:3306：将容器的3306端口映射到主机的3306端口
  * -v /mydata/mysql/conf:/etc/mysql：将配置文件夹挂在到主机
  * -v /mydata/mysql/log:/var/log/mysql：将日志文件夹挂载到主机
  * -v /mydata/mysql/data:/var/lib/mysql/：将数据文件夹挂载到主机
  * -e MYSQL_ROOT_PASSWORD=root：初始化root用户的密码

* 进入运行mysql的docker容器：
```bash
docker exec -it mysql /bin/bash
```

* 使用mysql命令打开客户端：
```bash
mysql -uroot -proot --default-character-set=utf8
``` 

* 创建数据库
```bash
create database mall character set utf8
```

* 创建一个reader帐号并修改权限，使得任何ip都能访问：
```bash
grant all privileges on *.* to 'reader' @'%' identified by '123456';
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

nginx.conf
```bash

user  nginx;
worker_processes  auto;

error_log  /var/log/nginx/error.log notice;
pid        /var/run/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    #gzip  on;

    server{
        listen      80;
        server_name  localhost;

        charset utf-8;

        location /{
            root   /usr/share/nginx/html/dist;
            index  index.html;
        }
        
        location ^~ /api{
            proxy_pass http://localhost:9012;
        }
        
        location ^~ /res{
            proxy_pass http://localhost:9000;
        }
        
        location ^~ /raw{
            proxy_pass http://localhost:80;
        }
        

    }


    include /etc/nginx/conf.d/*.conf;
}


```


## Minio
```bash
docker run -p 9000:9000 --name minio \
-v /mydata/minio/data:/data \
-v /mydata/minio/config:/root/.minio \
-e "MINIO_ACCESS_KEY=admin" \
-e "MINIO_SECRET_KEY=123456789" \
--restart always \
-d minio/minio server /data
```

## RabbitMQ
```bash
docker run -d --name rabbitmq \
--restart always \
--publish 5671:5671 --publish 5672:5672 --publish 4369:4369 \
--publish 25672:25672 --publish 15671:15671 --publish 15672:15672 \
rabbitmq:3.7.15
```

进入容器并开启管理功能：
```bash
docker exec -it rabbitmq /bin/bash
rabbitmq-plugins enable rabbitmq_management
```

## ~~ElasticSearch~~
```bash
docker run -p 9200:9200 -p 9300:9300 --name elasticsearch \
-e "discovery.type=single-node" \
-e "cluster.name=elasticsearch" \
-v /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
-v /mydata/elasticsearch/data:/usr/share/elasticsearch/data \
-v /mydata/elasticsearch/config:/usr/share/elasticsearch/config \
--restart always \
-e http.port=9200 -e http.cors.enabled=true \
-e http.cors.allow-origin=http://localhost:1358,http://127.0.0.1:1358,http://localhost:9800,http://127.0.0.1:9800 \
-e http.cors.allow-headers=X-Requested-With,X-Auth-Token,Content-Type,Content-Length,Authorization \
-e http.cors.allow-credentials=true \
-d elasticsearch:6.7.2
```

## Docker-Compose
docker-cmopose-env.yml:
```yaml
version: '3'
services:
  mysql:
    image: mysql:5.7
    container_name: mysql
    command: mysqld --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root #设置root帐号密码
    ports:
      - 3306:3306
    volumes:
      - /mydata/mysql/data/db:/var/lib/mysql #数据文件挂载
      - /mydata/mysql/data/conf:/etc/mysql/conf.d #配置文件挂载
      - /mydata/mysql/log:/var/log/mysql #日志文件挂载
  redis:
    image: redis:3.2
    container_name: redis
    command: redis-server --appendonly yes
    volumes:
      - /mydata/redis/data:/data #数据文件挂载
    ports:
      - 6379:6379
  rabbitmq:
    image: rabbitmq:3.7.15-management
    container_name: rabbitmq
    volumes:
      - /mydata/rabbitmq/data:/var/lib/rabbitmq #数据文件挂载
      - /mydata/rabbitmq/log:/var/log/rabbitmq #日志文件挂载
    ports:
      - 5672:5672
      - 15672:15672
```

命令:
```bash
docker-compose -f docker-compose-env.yml up -d
```



