package com.lanyuan.mapper;

import com.lanyuan.entity.PhysicalExaminationSickRiskMainResultFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface PhysicalExaminationSickRiskResultMapper extends BaseMapper{

	public List<PhysicalExaminationSickRiskMainResultFormMap> findEnterprisePage(PhysicalExaminationSickRiskMainResultFormMap physicalExaminationSickRiskMainResultFormMap);

}
