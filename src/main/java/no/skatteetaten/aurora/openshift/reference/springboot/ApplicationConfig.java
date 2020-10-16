package no.skatteetaten.aurora.openshift.reference.springboot;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import no.skatteetaten.aurora.openshift.reference.springboot.service.dto.S3Properties;

@Configuration
@EnableConfigurationProperties({ S3Properties.class })
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public AmazonS3 s3Client(S3Properties s3Properties) {
        S3Properties.S3Bucket defaultS3Bucket = s3Properties.getBuckets().get("default");
        AWSCredentials credentials =
            new BasicAWSCredentials(defaultS3Bucket.getAccessKey(), defaultS3Bucket.getSecretKey());
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(defaultS3Bucket.getServiceEndpoint(),
                    defaultS3Bucket.getBucketRegion()))
            .withPathStyleAccessEnabled(true)
            .withClientConfiguration(clientConfiguration)
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .build();
    }
}
