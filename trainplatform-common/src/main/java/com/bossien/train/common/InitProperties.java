package com.bossien.train.common;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * 内部类，加载配置文件信息
 *
 * @author gengxiefeng
 */
class InitProperties {

    public static Logger logger = Logger.getLogger(InitProperties.class);

    private static Properties prop = null;

    static {
        prop = new Properties();
        try {
            //prop.load(new BufferedInputStream (new FileInputStream("app-config.properties")));
            prop.load(InitProperties.class.getClassLoader().getResourceAsStream("app-config.properties"));
        } catch (IOException e) {
            logger.error("bdc-config.properties init is error:" + e.getMessage());
        }
    }

    public static String getValue(String key) {
        return prop.getProperty(key);
    }

}
