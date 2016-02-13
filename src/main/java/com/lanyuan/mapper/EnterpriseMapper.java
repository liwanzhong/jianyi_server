package com.lanyuan.mapper;

import com.lanyuan.entity.EnterpriseFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface EnterpriseMapper extends BaseMapper{

	public List<EnterpriseFormMap> findEnterprisePage(EnterpriseFormMap enterpriseFormMap);
	
}
