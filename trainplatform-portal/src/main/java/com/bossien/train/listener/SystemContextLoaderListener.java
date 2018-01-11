/**
 *
 */
package com.bossien.train.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * 上下文监听器
 * Created by chenxing on 2017/1/17 18:31.
 */
public class SystemContextLoaderListener extends ContextLoaderListener {

    /**
     * spring上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * servlet上下文环境
     */
    private static ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        servletContext = event.getServletContext();
        super.contextInitialized(event);
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);


    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (applicationContext != null) {

        }
        super.contextDestroyed(event);
    }


    /**
     * @return the applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @return the servletContext
     */
    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }


}
