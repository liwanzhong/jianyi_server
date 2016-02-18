package com.lanyuan.mapper;

import com.lanyuan.entity.CheckBigItemFormMap;
import com.lanyuan.entity.CutItemFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CheckBigItemMapper extends BaseMapper{

	public List<CheckBigItemFormMap> findEnterprisePage(CheckBigItemFormMap checkBigItemFormMap);

	public Integer findLasyOrderItemCout();
}
