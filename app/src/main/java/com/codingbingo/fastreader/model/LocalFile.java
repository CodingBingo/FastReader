package com.codingbingo.fastreader.model;

import java.io.Serializable;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/8.
 */

public class LocalFile implements Serializable{
    private String fileName;
    private String filePath;
    private long fileSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
