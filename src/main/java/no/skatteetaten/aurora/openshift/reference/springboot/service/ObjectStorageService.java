package no.skatteetaten.aurora.openshift.reference.springboot.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import no.skatteetaten.aurora.openshift.reference.springboot.controllers.errorhandling.ObjectStorageException;
import no.skatteetaten.aurora.openshift.reference.springboot.service.dto.S3Properties;

@Service
public class ObjectStorageService {
    private S3Properties s3Properties;
    private AmazonS3 s3Client;
    private S3Properties.S3Bucket s3Bucket;

    public ObjectStorageService(S3Properties s3Properties, AmazonS3 s3Client) {
        this.s3Properties = s3Properties;
        this.s3Client = s3Client;
        this.s3Bucket = s3Properties.getBuckets().get("default");
    }

    public void putFileContent(String keyName, String content) {
        File fileWithContent = writeFileContentToFile(keyName, content);
        putFile(keyName, fileWithContent);
    }

    public void putFile(String keyName, File file) {
        withS3(s3 -> {
            var request = new PutObjectRequest(s3Bucket.getBucketName(), getKeyName(keyName), file);
            s3.putObject(request);
        });
    }

    public InputStream getObjectInputStream(String keyName) {
        return withS3(s3 -> {
            var request = new GetObjectRequest(s3Bucket.getBucketName(), getKeyName(keyName));
            var objectPortion = s3.getObject(request);
            return objectPortion.getObjectContent();
        });
    }

    public String getTextObject(String keyName) {
        try {
            return readTextStream(getObjectInputStream(keyName));
        } catch (IOException e) {
            throw new ObjectStorageException("Error while reading text object", e);
        }
    }

    private String getKeyName(String keyName) {
        return String.format("%s/%s", s3Bucket.getObjectPrefix(), keyName);
    }

    private void withS3(Consumer<AmazonS3> fn) {
        try {
            fn.accept(s3Client);
        } catch (Exception e) {
            throw new ObjectStorageException("An error occurred while communicating with S3 storage", e);
        }
    }

    private <T> T withS3(Function<AmazonS3, T> fn) {
        try {
            return fn.apply(s3Client);
        } catch (Exception e) {
            throw new ObjectStorageException("An error occurred while communicating with S3 storage", e);
        }
    }

    private static File writeFileContentToFile(String fileName, String content) {
        try {
            File tmpFile = new File(fileName);
            FileOutputStream foutStream = new FileOutputStream(tmpFile);
            foutStream.write(content.getBytes());
            return tmpFile;
        } catch (IOException e) {
            throw new ObjectStorageException("Unable to write to temporary file when storing in S3", e);
        }
    }

    private static String readTextStream(InputStream input) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(input));
        var text = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            text.append(line).append(lineSeparator);
        }
        return text.toString();
    }
}
