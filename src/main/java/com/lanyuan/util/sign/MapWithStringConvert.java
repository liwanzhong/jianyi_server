package com.lanyuan.util.sign;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/3/2.
 */
public class MapWithStringConvert {


    public static String coverMap2String(Map<String, String> data) throws Exception{
        if(data==null || data.size() == 0){
            throw new Exception("数据异常！");
        }
        TreeMap tree = new TreeMap();
        Iterator it = data.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry sf = (Map.Entry)it.next();
            if(!"signature".equals(((String)sf.getKey()).trim())) {
                tree.put(sf.getKey(), sf.getValue());
            }
        }

        it = tree.entrySet().iterator();
        StringBuffer sf1 = new StringBuffer();

        while(it.hasNext()) {
            Map.Entry en = (Map.Entry)it.next();
            sf1.append((String)en.getKey() + "=" + (String)en.getValue() + "&");
        }

        return sf1.substring(0, sf1.length() - 1);
    }

    private static Map<String, String> convertResultString2Map(String res) {
        HashMap map = null;
        if(res != null && !"".equals(res.trim())) {
            String[] resArray = res.split("&");
            if(resArray.length != 0) {
                map = new HashMap(resArray.length);
                String[] var6 = resArray;
                int var5 = resArray.length;

                for(int var4 = 0; var4 < var5; ++var4) {
                    String arrayStr = var6[var4];
                    if(arrayStr != null && !"".equals(arrayStr.trim())) {
                        int index = arrayStr.indexOf("=");
                        if(-1 != index) {
                            map.put(arrayStr.substring(0, index), arrayStr.substring(index + 1));
                        }
                    }
                }
            }
        }

        return map;
    }

    private static void convertResultStringJoinMap(String res, Map<String, String> map) {
        if(res != null && !"".equals(res.trim())) {
            String[] resArray = res.split("&");
            if(resArray.length != 0) {
                String[] var6 = resArray;
                int var5 = resArray.length;

                for(int var4 = 0; var4 < var5; ++var4) {
                    String arrayStr = var6[var4];
                    if(arrayStr != null && !"".equals(arrayStr.trim())) {
                        int index = arrayStr.indexOf("=");
                        if(-1 != index) {
                            map.put(arrayStr.substring(0, index), arrayStr.substring(index + 1));
                        }
                    }
                }
            }
        }

    }



    public static Map<String, String> convertResultStringToMap(String result)throws Exception {
        if(StringUtils.isBlank(result)){
            throw new Exception("数据异常");
        }
        if(!result.contains("{")) {
            return convertResultString2Map(result);
        } else {
            String separator = "\\{";
            String[] res = result.split(separator);
            HashMap map = new HashMap();
            convertResultStringJoinMap(res[0], map);

            for(int i = 1; i < res.length; ++i) {
                int index = res[i].indexOf("}");
                String specialValue = "{" + res[i].substring(0, index) + "}";
                int indexKey = res[i - 1].lastIndexOf("&");
                String specialKey = res[i - 1].substring(indexKey + 1, res[i - 1].length() - 1);
                map.put(specialKey, specialValue);
                String normalResult = res[i].substring(index + 2, res[i].length());
                convertResultStringJoinMap(normalResult, map);
            }

            return map;
        }
    }
}
