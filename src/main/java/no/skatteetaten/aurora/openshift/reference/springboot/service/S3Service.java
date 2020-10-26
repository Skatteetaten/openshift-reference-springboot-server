package no.skatteetaten.aurora.openshift.reference.springboot.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import no.skatteetaten.aurora.openshift.reference.springboot.controllers.errorhandling.ObjectStorageException;
import no.skatteetaten.aurora.openshift.reference.springboot.service.dto.S3Properties;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    private S3Client s3Client;
    private S3Properties.S3Bucket s3Bucket;

    public S3Service(S3Properties s3Properties, S3Client s3Client) {
        this.s3Client = s3Client;
        this.s3Bucket = s3Properties.getBuckets().get("default");
    }

    public void putFileContent(String keyName, String content) {

        withS3(s3 -> {
            var request = PutObjectRequest.builder()
                .bucket(s3Bucket.getBucketName())
                .key(getKeyName(keyName)).build();

            s3.putObject(request, RequestBody.fromString(content));
        });
    }

    public String getFileContent(String keyName) {
        return withS3(s3 -> {
            var request = GetObjectRequest.builder()
                .bucket(s3Bucket.getBucketName())
                .key(getKeyName(keyName)).build();

            return s3.getObjectAsBytes(request).asUtf8String();
        });
    }

    private String getKeyName(String keyName) {
        return String.format("%s/%s", s3Bucket.getObjectPrefix(), keyName);
    }

    private void withS3(Consumer<S3Client> fn) {
        try {
            fn.accept(s3Client);
        } catch (Exception e) {
            throw new ObjectStorageException("An error occurred while communicating with S3 storage", e);
        }
    }

    private <T> T withS3(Function<S3Client, T> fn) {
        try {
            return fn.apply(s3Client);
        } catch (Exception e) {
            throw new ObjectStorageException("An error occurred while communicating with S3 storage", e);
        }
    }
}
