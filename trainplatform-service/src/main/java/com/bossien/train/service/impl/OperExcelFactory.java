package com.bossien.train.service.impl;

import com.bossien.train.service.IImportExcelUserService;
import com.bossien.train.service.IOperExcelFactory;

/**
 * Created by Administrator on 2017/8/21.
 */
public class OperExcelFactory implements IOperExcelFactory {
    @Override
    public IImportExcelUserService impoertUser() {

        return new IImportExcelUserServiceImpl();
    }
}
