package com.lanyuan.service;

import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.entity.CustomBelonetoEntFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface ICheckService {

    PhysicalExaminationRecordFormMap saveCheckRecord(PhysicalExaminationRecordFormMap recordFormMap, CustomBelonetoEntFormMap customBelonetoEntFormMap, String instrumentId) throws Exception;


    List<CheckSmallItemFormMap> getCustomerCheckSmallItemsList(String customid) throws Exception;



    void recordCheckResult(CustomBelonetoEntFormMap customBelonetoEntFormMap,PhysicalExaminationRecordFormMap recordFormMap) throws Exception;


    void genCheckResult(PhysicalExaminationRecordFormMap recordFormMap) throws Exception;


    void genSickRiskResult(PhysicalExaminationRecordFormMap recordFormMap)throws Exception;



}
