package com.lanyuan.mapper;

import com.lanyuan.entity.CheckValueScoreInFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CheckValueScoreInMapper extends BaseMapper{

	public List<CheckValueScoreInFormMap> findEnterprisePage(CheckValueScoreInFormMap cfPingfenRoutFormMap);

}
