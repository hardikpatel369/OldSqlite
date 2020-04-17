package com.example.sqlite.Images;

public class ImageModelClass {

    String id;

    private String filePath;
    private String fileName;
    private String serviceID;
    private String displayOrder;

    public ImageModelClass(String id, String filePath, String fileName, String serviceID, String displayOrder) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.serviceID = serviceID;
        this.displayOrder = displayOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }
}
