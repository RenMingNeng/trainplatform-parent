package com.bossien.train.kit;

import com.bossien.train.domain.OnlineUser;
import com.bossien.train.domain.User;
import com.bossien.train.util.PropertiesUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

public class BSTools {

    private static final String session_user = PropertiesUtils.getValue("session_user");

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static BrowserClient parseClient(HttpServletRequest request){
		BrowserClient client = new BrowserClient() ;
		String  browserDetails  =   request.getHeader("User-Agent");
	    String  userAgent       =   browserDetails;
	    String  user   =   userAgent.toLowerCase();
		String os = "";
        String browser = "" , version = "";


        //=================OS=======================
         if (userAgent.toLowerCase().indexOf("windows") >= 0 )
         {
             os = "windows";
         } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
         {
             os = "mac";
         } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
         {
             os = "unix";
         } else if(userAgent.toLowerCase().indexOf("android") >= 0)
         {
             os = "android";
         } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
         {
             os = "iphone";
         }else{
             os = "UnKnown";
         }
         //===============Browser===========================
        if (user.contains("msie") || user.indexOf("rv:11") > -1)
        {
        	if(user.indexOf("rv:11") >= 0){
        		browser = "IE11" ;
        	}else{
	            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
	            browser=substring.split(" ")[0].replace("MSIE", "IE")+substring.split(" ")[1];
        	}
        }else if (user.contains("trident"))
        {
            browser= "IE 11" ;
        }else if (user.contains("edge"))
        {
            browser= "Edge" ;
        }  else if (user.contains("safari") && user.contains("version"))
        {
            browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0];
            version = (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1] ;
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera")) {
                browser =
                    (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]
                        + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0])
                        .split("/")[1];
            } else if(user.contains("opr")) {
                browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0])
                    .replace("/", "-")).replace("OPR", "Opera");
            }
        } else if (user.contains("chrome"))
        {
            browser = "Chrome";
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        }else if ((user.indexOf("mozilla") > -1))
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
        	if(browserDetails.indexOf(" ") > 0){
        		browser = browserDetails.substring(0 , browserDetails.indexOf(" "));
        	}else{
        		browser = "Mozilla" ;
        	}

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            browser="ie";
        } else
        {
            browser = "UnKnown";
        }
        client.setUseragent(browserDetails);
        client.setOs(os);
        client.setBrowser(browser);
        client.setVersion(version);

        return client ;
	}

    public static OnlineUser parseOnlineUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(session_user);
        if(null == user) {
            return null;
        }

        OnlineUser onlineUser = new OnlineUser();

        String ip = BSTools.getIpAddr(request);
        onlineUser.setUserId(user.getId());
        onlineUser.setUserName(user.getUserName());
        onlineUser.setLoginTime(new Date().getTime());
        onlineUser.setIp(ip);

        IP ipdata = IPTools.getInstance().findGeography(ip);

        onlineUser.setCountry(ipdata.getCountry());
        onlineUser.setProvince(ipdata.getCountry());
        onlineUser.setCity(ipdata.getCity());
        onlineUser.setHostname(ip);
        onlineUser.setSessionId(request.getSession().getId());

        BrowserClient client = BSTools.parseClient(request);
        onlineUser.setOpersystem(client.getOs());
        onlineUser.setBrowser(client.getBrowser());
        onlineUser.setUseragent(client.getUseragent());

        return onlineUser;
    }
}
