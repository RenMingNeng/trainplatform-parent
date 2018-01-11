package com.bossien.framework.context;

/**
 * Created by Administrator on 2017/9/6.
 */
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContextHolder.context = context;
    }

    public static ApplicationContext getApplicationContext(){
        return context;
    }

    public static <T> T getBean(String name){
        return (T) context.getBean(name);
    }
}
