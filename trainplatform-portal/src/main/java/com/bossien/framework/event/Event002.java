package com.bossien.framework.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/26.
 */
public class Event002 extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> params;

    public Event002(Object source) {
        super(source);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
