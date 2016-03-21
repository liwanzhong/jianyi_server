package com.lanyuan.mapper;

import com.lanyuan.entity.RoleUserFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface RoleUserMapper extends BaseMapper{

	public List<RoleUserFormMap> findUserPage(RoleUserFormMap roleUserFormMap);
	
}
