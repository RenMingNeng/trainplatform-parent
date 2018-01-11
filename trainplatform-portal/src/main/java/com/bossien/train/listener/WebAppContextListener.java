package com.bossien.train.listener;

import com.bossien.train.service.IAppService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebAppContextListener implements ServletContextListener {

//	private RedisTemplate redisTemplate;
	private ApplicationContext acx;

	@Override
    public void contextDestroyed(ServletContextEvent event) {

//		this.initService();
		// 应用销毁 自动移除server
//		ServerKit.contextDestroyed(redisTemplate);
	}

	@Override
    public void contextInitialized(ServletContextEvent sce) {

		acx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		this.initData();
//		this.initService();
		// 应用启动 自动注册server
//		ServerKit.contextInitialized(redisTemplate);
	}


	// 数据初始化工作
	private void initData() {
		IAppService appService = (IAppService) acx.getBean("appService");
		appService.initCompany();
	}

//	private void initService() {
//		if(null == redisTemplate)
//        redisTemplate = (RedisTemplate) SystemContextLoaderListener.getBean("redisTemplate");
//    }
}
