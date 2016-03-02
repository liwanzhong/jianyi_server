package com.lanyuan.util.sign;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by liwanzhong on 2016/3/2.
 */
public class MapWithStringConvertTest {

    @org.junit.Test
    public void testCoverMap2String() throws Exception {
        System.out.println("testCoverMap2String");
        Map<String,String> mapdata= new HashMap<String,String>();
        mapdata.put("key333","33333");
        mapdata.put("key444","44444");
        mapdata.put("key111","1111111");
        mapdata.put("key222","222222");
        mapdata.put("signature","sssssssss");
        System.out.println(MapWithStringConvert.coverMap2String(mapdata));
    }

    @org.junit.Test
    public void testConvertResultStringToMap() throws Exception {
        System.out.println("testConvertResultStringToMap");
        Map<String,String> mapdata= MapWithStringConvert.convertResultStringToMap("key111=1111111&key222=222222&key333=33333&key444=44444&signature=wwwwwwwwiwiwiwiiw");
        System.out.println(mapdata.toString());
    }
}