package com.lanyuan.mapper;

import com.lanyuan.entity.BigItemResultLeveConfigFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface BigItemResultLeveConfigMapper extends BaseMapper{


	List<BigItemResultLeveConfigFormMap> findPage(BigItemResultLeveConfigFormMap bigItemResultLeveConfigFormMap);

	List<BigItemResultLeveConfigFormMap> findFixedList(BigItemResultLeveConfigFormMap bigItemResultLeveConfigFormMapCondition);
}
