package com.lanyuan.mapper;

import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.CutItemFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CutItemMapper extends BaseMapper{

	public List<CutItemFormMap> findEnterprisePage(CutItemFormMap cutItemFormMap);
	
}
