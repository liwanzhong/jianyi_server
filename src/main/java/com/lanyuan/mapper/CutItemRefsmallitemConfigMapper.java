package com.lanyuan.mapper;

import com.lanyuan.entity.CutItemFormMap;
import com.lanyuan.entity.CutItemRefsmallitemConfigFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CutItemRefsmallitemConfigMapper extends BaseMapper{

	public List<CutItemRefsmallitemConfigFormMap> findEnterprisePage(CutItemRefsmallitemConfigFormMap cutItemRefsmallitemConfigFormMap);
	
}
