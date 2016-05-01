package com.lanyuan.mapper;

import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.ZongpingLeveDescConfigFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface ZongpingLeveDescConfigMapper extends BaseMapper{

	public List<ZongpingLeveDescConfigFormMap> findEnterprisePage(ZongpingLeveDescConfigFormMap zongpingLeveDescConfigFormMap);



	 ZongpingLeveDescConfigFormMap findFixedItem(ZongpingLeveDescConfigFormMap zongpingLeveDescConfigFormMap);




	
}
