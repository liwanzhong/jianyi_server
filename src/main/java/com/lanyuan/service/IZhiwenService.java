package com.lanyuan.service;

import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface IZhiwenService {


    void record(Long customId,String zhiwenCode, String filePath,Integer position,String instrument)throws Exception;

    List<String> queryZhiwenByCustomId(Long customId)throws Exception;
}
