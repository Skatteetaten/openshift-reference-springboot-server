package no.skatteetaten.aurora.openshift.reference.springboot;

import java.net.URI;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import no.skatteetaten.aurora.openshift.reference.springboot.service.dto.S3Properties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableConfigurationProperties({ S3Properties.class })
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public S3Client s3Client(S3Properties s3Properties) {
        S3Properties.S3Bucket defaultS3Bucket = s3Properties.getBuckets().get("default");
        return S3Client.builder()
            .region(Region.of(defaultS3Bucket.getBucketRegion()))
            .credentialsProvider(
                StaticCredentialsProvider
                    .create(AwsBasicCredentials.create(defaultS3Bucket.getAccessKey(), defaultS3Bucket.getSecretKey()))
            ).endpointOverride(URI.create(defaultS3Bucket.getServiceEndpoint()))
            .build();
    }
}
