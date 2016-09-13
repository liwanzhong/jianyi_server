package com.lanyuan.mapper;

import com.lanyuan.entity.CheckItemReleationConfigMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CheckItemReleationConfigMapper extends BaseMapper{

	public List<CheckItemReleationConfigMap> findPage(CheckItemReleationConfigMap customCutItemFormMap);


	List<CheckItemReleationConfigMap> findAll();

	List<CheckItemReleationConfigMap> findSmallReleationByDist(CheckItemReleationConfigMap checkItemReleationConfigMap);
}
