package com.bossien.train.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

public class TokenUtil {


    final static private String secret = "bossien";



    public static String creatToken(String userid, String subject, long oktime) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256 ;
        long nowMillis = System. currentTimeMillis();

        JwtBuilder builder = Jwts. builder()
                .setIssuer(userid)
                .setIssuedAt(new Date(nowMillis))
                .setSubject(subject)
                .signWith(signatureAlgorithm, secret);

        if (oktime >= 0){
            builder.setExpiration(new Date(nowMillis + oktime));
        }
        return builder.compact();
    }


    public static Claims parseToken(String token) throws Exception{

        Claims claims = Jwts. parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody();
        return claims;
    }


    /**
     * ztree组装树
     *
     * @param lm
     * @return
     */
    public static List<Map<String, Object>> assembleTreeNodes(List <Map <String, Object>> lm) {
        Map <String, List <Map <String, Object>>> listToMap_pid = listToMap(lm, "pid");
        Map <String, List <Map <String, Object>>> listToMap_id = listToMap(lm, "id");

        List <Map <String, Object>> tree = new ArrayList<Map <String, Object>>();
        for (Map <String, Object> map : lm) {
            if (null == listToMap_id.get(map.get("pid"))) {
                map.put("children", recursion(listToMap_pid, map.get("id").toString()));
                tree.add(map);
            }
        }
        return tree;
    }

    /**
     * 重组map
     *
     * @param list
     * @param key
     * @return
     */
    public static Map <String, List <Map <String, Object>>> listToMap(List <Map <String, Object>> list, String key) {
        Map <String, List <Map <String, Object>>> mlist = new HashMap<String, List <Map <String, Object>>>();
        for (Map <String, Object> courseType : list) {
            Object pid = courseType.get(key);
            if (null != pid) {
                List <Map <String, Object>> types = new ArrayList <Map <String, Object>>();
                if (null != mlist.get(pid.toString())) {
                    types = mlist.get(pid.toString());
                }
                types.add(courseType);
                mlist.put(pid.toString(), types);
            }
        }
        return mlist;
    }

    /**
     * 递归
     *
     * @param listToMap
     * @param pid
     * @return
     */
    public static List <Map <String, Object>> recursion(Map <String, List <Map <String, Object>>> listToMap, String pid) {
        List <Map <String, Object>> data = listToMap.get(pid);
        if (null == data) {
            return new ArrayList <Map <String, Object>>();
        }

        for (int i = 0; i < data.size(); i++) {
            Map <String, Object> map = data.get(i);
            Object id = map.get("id");
            if (null != id) {
                List <Map <String, Object>> children = listToMap.get(id.toString());
                if (null == children) {
                    children = new ArrayList <Map <String, Object>>();
                } else {
                    children = recursion(listToMap, id.toString());
                    data.get(i).put("children", children);
                }
            }
        }
        return data;
    }


}
