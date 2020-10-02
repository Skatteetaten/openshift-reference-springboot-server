package no.skatteetaten.aurora.openshift.reference.springboot.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "s3")
public class S3Properties {

    private String serviceEndpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String objectPrefix;
    private String bucketRegion;

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() { return bucketName; }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectPrefix() { return objectPrefix; }

    public void setObjectPrefix(String objectPrefix) {
        this.objectPrefix = objectPrefix;
    }

    public String getBucketRegion() { return bucketRegion; }

    public void setBucketRegion(String bucketRegion) { this.bucketRegion = bucketRegion; }
}
