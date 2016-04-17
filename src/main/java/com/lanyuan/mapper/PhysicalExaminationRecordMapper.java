package com.lanyuan.mapper;

import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface PhysicalExaminationRecordMapper extends BaseMapper{

	public List<PhysicalExaminationRecordFormMap> findEnterprisePage(PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap);

	public List<PhysicalExaminationRecordFormMap> findEnterprisePage_Front(PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap);

	List<PhysicalExaminationRecordFormMap> findExaminationRecordCustomInfo(PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap);

	List<PhysicalExaminationRecordFormMap> findListWill2GenPdfReport(PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap);
}
