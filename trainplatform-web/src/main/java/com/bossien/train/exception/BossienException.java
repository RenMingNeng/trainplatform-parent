package com.bossien.train.exception;

/**
 * bossien的异常基类
 * Created by luocc on 2017/07/21.
 */
public abstract class BossienException extends RuntimeException {
    public BossienException(String message) {
        super(message);
    }
}
