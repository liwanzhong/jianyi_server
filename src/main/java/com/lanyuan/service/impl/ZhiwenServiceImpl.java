package com.lanyuan.service.impl;

import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.service.IZhiwenService;
import org.springframework.stereotype.Service;

/**
 * Created by liwanzhong on 2016/5/9.
 */
@Service
public class ZhiwenServiceImpl implements IZhiwenService {


    public void record(String zhiwenCode, String filePath) throws Exception {
        System.out.println("记录指纹信息");
    }

    public CustomInfoFormMap queryCustom(String zhiwenCode) throws Exception {
        System.out.println("通过指纹识别码查询用户!");
        return null;
    }
}
