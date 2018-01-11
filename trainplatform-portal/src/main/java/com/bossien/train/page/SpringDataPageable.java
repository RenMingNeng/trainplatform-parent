package com.bossien.train.page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/18.
 */
public class SpringDataPageable implements Serializable, Pageable {

    // 当前页
    private Integer pageNumber = 1;

    // 当前页面条数
    private Integer pageSize = 10;

    // 排序条件
    private Sort sort;

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getOffset() {
        return (getPageNumber() - 1) * getPageSize();
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }



    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
