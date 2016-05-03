package com.lanyuan.mapper;

import com.lanyuan.entity.BmiNormalConfigFormMap;
import com.lanyuan.entity.BmiSickRiskRoutFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface BmiNormalConfigMapper extends BaseMapper{

	 BmiNormalConfigFormMap findSexNormalConfig(BmiNormalConfigFormMap bmiNormalConfigFormMap);


}
