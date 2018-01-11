package com.bossien.train.service.impl;

import com.bossien.train.service.ISequenceService;
import com.bossien.train.util.ShortUuid;
import com.bossien.train.util.UUIDGenerator;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2017/7/27.
 */
@Service
public class SequenceServiceImpl implements ISequenceService {

    @Override
    public String  generator() {
//        ShortUuid.Builder builder = new ShortUuid.Builder();
//        return builder.build(UUID.randomUUID()).toString();
        return UUIDGenerator.genID()+UUIDGenerator.genID();
    }
}
