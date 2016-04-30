package com.lanyuan.mapper;

import com.lanyuan.entity.AgeSickRiskRoutFormMap;
import com.lanyuan.entity.SickRiskFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface AgeSickRiskRoutMapper extends BaseMapper{

	 List<AgeSickRiskRoutFormMap> findEnterprisePage(AgeSickRiskRoutFormMap ageSickRiskRoutFormMap);


	List<AgeSickRiskRoutFormMap> findFixConfig(AgeSickRiskRoutFormMap ageSickRiskRoutFormMap);
}
