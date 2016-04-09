package com.lanyuan.mapper;

import com.lanyuan.entity.PhysicalExaminationBigResultFormMap;
import com.lanyuan.entity.PhysicalExaminationMainReportFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface PhysicalExaminationMainReportMapper extends BaseMapper{

	public List<PhysicalExaminationMainReportFormMap> findEnterprisePage(PhysicalExaminationMainReportFormMap physicalExaminationMainReportFormMap);
	
}
