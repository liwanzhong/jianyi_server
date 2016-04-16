package com.lanyuan.mapper;

import com.lanyuan.entity.CfPingfenRoutFormMap;
import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CfPingfenRoutMapper extends BaseMapper{

	public List<CfPingfenRoutFormMap> findEnterprisePage(CfPingfenRoutFormMap cfPingfenRoutFormMap);

	public Integer findLasyOrderItemCout();

	List<CfPingfenRoutFormMap> findSmallItemRout(CfPingfenRoutFormMap cfPingfenRoutFormMap);
}
