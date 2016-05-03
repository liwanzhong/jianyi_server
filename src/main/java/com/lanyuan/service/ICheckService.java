package com.lanyuan.service;

import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.entity.CustomBelonetoEntFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface ICheckService {

    void recordCheckResult(String instrumentCode,Long customerId) throws Exception;


    void genCheckResult(PhysicalExaminationRecordFormMap recordFormMap) throws Exception;


    void genSickRiskResult(PhysicalExaminationRecordFormMap recordFormMap)throws Exception;


    void deleteGenedData(PhysicalExaminationRecordFormMap recordFormMap)throws Exception;
}
