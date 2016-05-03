package com.lanyuan.mapper;

import com.lanyuan.entity.BmiLeveConfigFormMap;
import com.lanyuan.entity.BmiSickRiskRoutFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface BmiLeveConfigMapper extends BaseMapper{


	List<BmiLeveConfigFormMap> findFixConfig(BmiLeveConfigFormMap bmiLeveConfigFormMap);

	List<BmiLeveConfigFormMap> findPage(BmiLeveConfigFormMap bmileveconfigFormMap);
}
