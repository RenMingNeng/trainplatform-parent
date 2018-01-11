package com.bossien.train.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/15.
 */
public class ExcelResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    // 总条数
    private Integer total;
    // 成功数
    private Integer success;
    // 失败数
    private Integer error;
    // 错误文件
    private String fileName;

    private List<String> idsList;
    // 错误list
    List<Map<String, Object>> errorLMap = new LinkedList<Map<String, Object>>();
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
    public Integer getSuccess() {
        return success;
    }
    public void setSuccess(Integer success) {
        this.success = success;
    }
    public Integer getError() {
        return error;
    }
    public void setError(Integer error) {
        this.error = error;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public List<Map<String, Object>> getErrorLMap() {
        return errorLMap;
    }
    public void setErrorLMap(List<Map<String, Object>> errorLMap) {
        this.errorLMap = errorLMap;
    }
    public ExcelResultEntity() {
        super();
        this.total = 0;
        this.success = 0;
        this.error = 0;
        this.fileName = "";
    }
    public ExcelResultEntity(List<List<String>> lList, LinkedList<Map<String, Object>> lMap, Map<String, Object> headerMap, String fileName,List<String> idsList) {
        this.total = (null!=lList)?lList.size():0;
        this.error = (null!=lMap)?lMap.size():0;
        this.success = (total-error);
        this.fileName = fileName;
        if(null!=lMap) {
            lMap.addFirst(headerMap);
        }
        this.errorLMap = lMap;
        this.idsList=idsList;
    }

    public List<String> getIdsList() {
        return idsList;
    }

    public void setIdsList(List<String> idsList) {
        this.idsList = idsList;
    }
}
