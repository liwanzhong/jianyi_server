package com.lanyuan.mapper;

import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.OrganizationFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface OrganizationMapper extends BaseMapper{

	public List<OrganizationFormMap> findEnterprisePage(OrganizationFormMap organizationFormMap);
	
}
