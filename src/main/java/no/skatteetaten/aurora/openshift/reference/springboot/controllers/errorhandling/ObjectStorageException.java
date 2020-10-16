package no.skatteetaten.aurora.openshift.reference.springboot.controllers.errorhandling;

public class ObjectStorageException extends RuntimeException {
    public ObjectStorageException(String msg, Exception e) {
        super(msg, e);
    }
}
