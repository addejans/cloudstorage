package com.udacity.jwdnd.course1.cloudstorage.model;

public class File {

    private Integer fileId;
    private String filename;
    private String contentType;
    private Integer userId;
    private byte[] fileData;
    private Integer fileSize;

    public File(){}

    public File(Integer fileId, String filename, String contentType, Integer userid, byte[] fileData) {
        this.fileId = fileId;
        this.filename = filename;
        this.contentType = contentType;
        this.userId = userid;
        this.fileData = fileData;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public Integer getFileSize(){
        return this.fileSize;
    }

    public Integer getFileId(){
        return this.fileId;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}

