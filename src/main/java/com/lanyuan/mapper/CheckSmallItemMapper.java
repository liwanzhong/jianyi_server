package com.lanyuan.mapper;

import com.lanyuan.entity.CheckBigItemFormMap;
import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CheckSmallItemMapper extends BaseMapper{

	public List<CheckSmallItemFormMap> findEnterprisePage(CheckSmallItemFormMap checkSmallItemFormMap);

	public Integer findLasyOrderItemCout();
}
