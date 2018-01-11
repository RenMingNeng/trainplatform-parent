package com.bossien.train.domain;

import java.io.Serializable;

public class CompanyPermission implements Serializable {

    private String companyId;      // 公司id
    private String purview;       // permission值

    public CompanyPermission(){}

    public CompanyPermission(String purview){
        this.purview = purview;
    }

    public CompanyPermission(String companyId, String purview){
        this.companyId = companyId;
        this.purview = purview;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPurview() {
        return purview;
    }

    public void setPurview(String purview) {
        this.purview = purview;
    }
}
