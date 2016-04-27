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


    //todo 记录小项检测记录
    void recordCheckResult()throws Exception;



}
