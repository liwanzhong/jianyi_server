package com.lanyuan.mapper;

import com.lanyuan.entity.CfPingfenRoutFormMap;
import com.lanyuan.entity.SickRiskFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface SickRiskMapper extends BaseMapper{

	 List<SickRiskFormMap> findEnterprisePage(SickRiskFormMap sickRiskFormMap);


}
