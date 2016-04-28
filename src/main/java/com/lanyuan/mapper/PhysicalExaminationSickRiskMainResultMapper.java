package com.lanyuan.mapper;

import com.lanyuan.entity.PhysicalExaminationBigResultFormMap;
import com.lanyuan.entity.PhysicalExaminationSickRiskMainResultFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface PhysicalExaminationSickRiskMainResultMapper extends BaseMapper{

	public List<PhysicalExaminationSickRiskMainResultFormMap> findEnterprisePage(PhysicalExaminationSickRiskMainResultFormMap physicalExaminationSickRiskMainResultFormMap);

}
