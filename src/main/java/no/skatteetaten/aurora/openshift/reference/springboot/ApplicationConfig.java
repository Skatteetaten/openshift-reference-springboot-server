package no.skatteetaten.aurora.openshift.reference.springboot;

import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import no.skatteetaten.aurora.openshift.reference.springboot.service.dto.S3Configuration;
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

    @Primary
    @Bean
    public S3Configuration defaultS3Client(
        S3Properties s3Properties,
        @Value("${s3.objectareas.default:referanse-java}") String defaultKey
    ) {
        S3Properties.S3Bucket defaultS3Bucket = s3Properties.getBuckets().get(defaultKey);
        return new S3Configuration(baseS3Client(defaultS3Bucket), defaultS3Bucket);
    }

    @Qualifier("otherArea")
    @Bean
    public S3Configuration otherS3Client(
        S3Properties s3Properties,
        @Value("${s3.objectareas.anotherArea:referanse-java2}") String defaultKey
    ) {
        S3Properties.S3Bucket otherS3Bucket = s3Properties.getBuckets().get(defaultKey);
        return new S3Configuration(baseS3Client(otherS3Bucket), otherS3Bucket);
    }

    private S3Client baseS3Client(S3Properties.S3Bucket objectArea) {
        return S3Client.builder()
            .region(Region.of(objectArea.getBucketRegion()))
            .credentialsProvider(
                StaticCredentialsProvider
                    .create(AwsBasicCredentials.create(objectArea.getAccessKey(), objectArea.getSecretKey()))
            ).endpointOverride(URI.create(objectArea.getServiceEndpoint()))
            .build();
    }

}
