package com.lanyuan.mapper;

import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.CustomZhiwenFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CustomZhiwenMapper extends BaseMapper{

	public List<CustomZhiwenFormMap> findList(CustomZhiwenFormMap customZhiwenFormMap);



	
}
