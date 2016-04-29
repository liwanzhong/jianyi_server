package com.lanyuan.mapper;

import com.lanyuan.entity.BmiSickRiskRoutFormMap;
import com.lanyuan.entity.SickRiskFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface BmiSickRiskRoutMapper extends BaseMapper{

	 List<BmiSickRiskRoutFormMap> findEnterprisePage(BmiSickRiskRoutFormMap bmiSickRiskRoutFormMap);


}
