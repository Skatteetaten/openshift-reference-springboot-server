package no.skatteetaten.aurora.openshift.reference.springboot;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import no.skatteetaten.aurora.openshift.reference.springboot.service.ObjectStorageException;
import no.skatteetaten.aurora.openshift.reference.springboot.service.ObjectStorageService;

@Component
public class S3Experiment implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(S3Experiment.class);

    private ObjectStorageService storageService;

    public S3Experiment(ObjectStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void run(ApplicationArguments args) {

        try {
            File file = new File("/etc/hosts");
            String keyName = file.getName();
            storageService.putFile(keyName, file);
            var text = storageService.getTextObject(keyName);
            System.out.println(text);
        } catch (ObjectStorageException e) {
            logger.error("An error occurred while performing operations agains S3", e);
        }
    }

}
