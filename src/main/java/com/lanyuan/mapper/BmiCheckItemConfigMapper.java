package com.lanyuan.mapper;

import com.lanyuan.entity.BmiCheckItemConfigFormMap;
import com.lanyuan.entity.SickRiskItemFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface BmiCheckItemConfigMapper extends BaseMapper{

	 List<BmiCheckItemConfigFormMap> findPage(BmiCheckItemConfigFormMap bmiCheckItemConfigFormMap);

}
