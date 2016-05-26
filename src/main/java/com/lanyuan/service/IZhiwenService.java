package com.lanyuan.service;

import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface IZhiwenService {


    void record(Long customId,String zhiwenCode, String filePath)throws Exception;

    CustomInfoFormMap queryCustom(String zhiwenCode)throws Exception;
}
