package com.lanyuan.mapper;

import com.lanyuan.entity.SickRiskFormMap;
import com.lanyuan.entity.SickRiskLeveFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface SickRiskLeveMapper extends BaseMapper{

	 List<SickRiskLeveFormMap> findEnterprisePage(SickRiskLeveFormMap sickRiskLeveFormMap);


}
