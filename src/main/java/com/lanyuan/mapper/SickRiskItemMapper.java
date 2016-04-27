package com.lanyuan.mapper;

import com.lanyuan.entity.SickRiskFormMap;
import com.lanyuan.entity.SickRiskItemFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface SickRiskItemMapper extends BaseMapper{

	 List<SickRiskItemFormMap> findEnterprisePage(SickRiskItemFormMap sickRiskItemFormMap);


}
