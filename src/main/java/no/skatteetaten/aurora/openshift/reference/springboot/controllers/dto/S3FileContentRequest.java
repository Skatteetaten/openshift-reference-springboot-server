package no.skatteetaten.aurora.openshift.reference.springboot.controllers.dto;

public class S3FileContentRequest {

    private String fileName;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
