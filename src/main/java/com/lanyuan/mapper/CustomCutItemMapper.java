package com.lanyuan.mapper;

import com.lanyuan.entity.CustomCutItemFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CustomCutItemMapper extends BaseMapper{

	public List<CustomCutItemFormMap> findEnterprisePage(CustomCutItemFormMap customCutItemFormMap);

}
