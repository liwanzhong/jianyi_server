package com.lanyuan.service.impl;

import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.entity.CustomBelonetoEntFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;
import com.lanyuan.mapper.CheckSmallItemMapper;
import com.lanyuan.mapper.PhysicalExaminationRecordMapper;
import com.lanyuan.service.ICheckService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
@Service
public class CheckServiceImpl implements ICheckService {

    @Inject
    private PhysicalExaminationRecordMapper physicalExaminationRecordMapper;


    @Inject
    private CheckSmallItemMapper checkSmallItemMapper;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PhysicalExaminationRecordFormMap saveCheckRecord(PhysicalExaminationRecordFormMap recordFormMap,CustomBelonetoEntFormMap customBelonetoEntFormMap,String instrumentId) throws Exception {
        // 保存检测记录
        recordFormMap.put("custom_id",customBelonetoEntFormMap.getLong("custom_id"));
        recordFormMap.put("organization_id",customBelonetoEntFormMap.get("organization_id").toString());
        recordFormMap.put("instrument_id",instrumentId);
        recordFormMap.put("status",PhysicalExaminationRecordFormMap.STATUS_CHECKED);
        recordFormMap.put("check_time",dateFormat.format(new Date()));
        recordFormMap.put("update_time",dateFormat.format(new Date()));
        physicalExaminationRecordMapper.addEntity(recordFormMap);
        return recordFormMap;
    }

    public List<CheckSmallItemFormMap> getCustomerCheckSmallItemsList(String customid) throws Exception {
        List<CheckSmallItemFormMap> checkSmallItemFormMapList =  checkSmallItemMapper.getAllButCustomCutedItem(customid);
        return checkSmallItemFormMapList;
    }
}
