package com.lanyuan.mapper;

import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.EnterpriseFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CustomInfoMapper extends BaseMapper{

	public List<CustomInfoFormMap> findEnterprisePage(CustomInfoFormMap customInfoFormMap);
	
}
