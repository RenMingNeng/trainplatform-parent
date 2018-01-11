package com.bossien.train.util;

import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

/**
 * 
 * 生成UUID
 * 
 * @author MoXiaoFeng
 *
 */
public class UUIDGenerator {
	private static final IdGenerator idGenerator = new AlternativeJdkIdGenerator() ;
	 /** 
     * 获得一个UUID 
     * @return String UUID 
     */ 
	public static String getUUID(){ 
        //String s = UUID.randomUUID().toString(); 
        //去掉“-”符号 
        //return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
		String s = idGenerator.generateId().toString() ;
		return s.replaceAll("-", "") ;
    }

	public static String genID(){
		return Base62.encode(getUUID()).toLowerCase() ;
	}

	public static void main(String[] args) {
		System.out.println(UUIDGenerator.getUUID());
		
		System.out.println(idGenerator.generateId().toString().replaceAll("-", ""));
	}
	
}
