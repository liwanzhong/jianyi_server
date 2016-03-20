package com.lanyuan.mapper;

import java.util.List;

import com.lanyuan.entity.OrganizationFormMap;
import com.lanyuan.entity.RoleFormMap;
import com.lanyuan.mapper.base.BaseMapper;

public interface RoleMapper extends BaseMapper{
	public List<RoleFormMap> seletUserRole(RoleFormMap map);
	
	public void deleteById(RoleFormMap map);

	List<RoleFormMap> findEnterprisePage(RoleFormMap roleFormMap)throws Exception;
}
