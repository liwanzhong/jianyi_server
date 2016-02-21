package com.lanyuan.mapper;

import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.entity.EquipmentFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface EquipmentMapper extends BaseMapper{

	public List<EquipmentFormMap> findEnterprisePage(EquipmentFormMap equipmentFormMap);

	public Integer findLasyOrderItemCout();
}
