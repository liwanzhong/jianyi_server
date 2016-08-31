/*
package com.lanyuan.util.sign;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

*/
/**
 * Created by liwanzhong on 2016/3/2.
 *//*

public class RSASignatureTest {

    @Test
    public void testSign() throws Exception {
        Map<String,String> mapdata= new HashMap<String,String>();
        mapdata.put("key333","33333");
        mapdata.put("key444","44444");
        mapdata.put("key111","1111111");
        mapdata.put("key222","222222");

        mapdata.put("signature",RSASignature.sign(MapWithStringConvert.coverMap2String(mapdata)));
        System.out.println(mapdata.toString());
    }

    @Test
    public void testVerify() throws Exception {

        Map<String,String> resData= MapWithStringConvert.convertResultStringToMap("key111=1111111&key222=222222&key333=33333&key444=44444&signature=p9it2RI1TUES41fyuEeAYXSnFnuQbR3o6spHcon3OV7DNNL5RqSKSUCvcWqhl7R7bk9WLUpw0MBS7WgfGBerG+8rDUvzq2S06ArZLJH/dEPRUDMNr8B5ltZ/29wyL3ZEGEVpi1iB0MZypChudS5kGpf44WtsjF6n6vBPUfCauZ0=");
        System.out.println(resData.toString());
        String stringSign = resData.get("signature");
        System.out.println("返回报文中signature=[" + stringSign + "]");
        String stringData = MapWithStringConvert.coverMap2String(resData);
        System.out.println("返回报文中(不含signature域)的stringData=[" + stringData + "]");

        System.out.println(RSASignature.verify(stringData,stringSign));
    }
}*/
