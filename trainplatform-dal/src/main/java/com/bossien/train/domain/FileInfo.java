package com.bossien.train.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by A on 2017/8/3.
 */
@XStreamAlias("fileInfo")
public class FileInfo implements Serializable {
    @XStreamAlias("fileId")
    String fileId;
    @XStreamAlias("fileName")
    String fileName;
    @XStreamAlias("filePath")
    String filePath;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

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
}
