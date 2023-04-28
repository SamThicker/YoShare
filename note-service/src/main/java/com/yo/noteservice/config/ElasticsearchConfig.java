package com.yo.noteservice.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticsearchConfig extends ElasticsearchConfigurationSupport {

    @Value("${es.host}")
    private String host;
    @Value("${es.schema}")
    private String schema;
    @Value("${es.cluster-name}")
    private String clusterName;

    @Bean()
    public TransportClient client() throws UnknownHostException {

        //设置setting
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();

        //创建客户端，如果使用默认，传空
        TransportClient client = new PreBuiltTransportClient(settings);

        //添加节点
        String[] hosts = host.split(",");
        for(String str : hosts){
            String ip = str.split(":")[0];
            int port = Integer.valueOf(str.split(":")[1]);
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(ip),port);
            client.addTransportAddress(transportAddress);
        }
        return client;
    }


    // use the ElasticsearchEntityMapper
    @Bean
    @Override
    public EntityMapper entityMapper() {
        ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(elasticsearchMappingContext(),
                new DefaultConversionService());
        entityMapper.setConversions(elasticsearchCustomConversions());
        return entityMapper;
    }

}
