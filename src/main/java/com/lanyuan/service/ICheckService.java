package com.lanyuan.service;

import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.entity.CustomBelonetoEntFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface ICheckService {








    void recordCheckResult(Long instrumentId,Long customerId,Long customBelongToId) throws Exception;


    void genCheckResult(PhysicalExaminationRecordFormMap recordFormMap) throws Exception;


    void genSickRiskResult(PhysicalExaminationRecordFormMap recordFormMap)throws Exception;



}
