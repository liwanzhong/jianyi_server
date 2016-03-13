package com.lanyuan.mapper;

import com.lanyuan.entity.CfPingfenLeveFormMap;
import com.lanyuan.entity.CfPingfenRoutFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CfPingfenLeveMapper extends BaseMapper{

	public List<CfPingfenLeveFormMap> findEnterprisePage(CfPingfenLeveFormMap cfPingfenLeveFormMap);

}
