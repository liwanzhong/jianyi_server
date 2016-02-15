package com.lanyuan.mapper;

import com.lanyuan.entity.CustomBelonetoEntFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CustomBelonetoEntMapper extends BaseMapper{

	public List<CustomBelonetoEntFormMap> findEnterprisePage(CustomBelonetoEntFormMap cutItemFormMap);

}
