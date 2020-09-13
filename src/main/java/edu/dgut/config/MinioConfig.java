package edu.dgut.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;



@Configuration
@Component
@PropertySource("classpath:minio.properties")
public class MinioConfig {

    @Value("${minio.endPoint}")
    private String endpoint;

    @Value("${minio.port}")
    private int port;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = null;
        try {
            minioClient = MinioClient.builder()
                    .endpoint(endpoint, port, false)
                    .credentials(accessKey, secretKey)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return minioClient;
        }

    }

}
