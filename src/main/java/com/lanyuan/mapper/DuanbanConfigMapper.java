package com.lanyuan.mapper;

import com.lanyuan.entity.DuanbanConfigFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface DuanbanConfigMapper extends BaseMapper{

	List<DuanbanConfigFormMap> findDuanbanConfigByBigItem(DuanbanConfigFormMap duanbanConfigFormMap)throws Exception;

}
