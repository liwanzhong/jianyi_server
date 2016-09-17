package com.lanyuan.mapper;

import com.lanyuan.entity.CheckItemDegrLeveZhanbiFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface CheckItemDegrLeveZhanbiMapper extends BaseMapper{

    List<CheckItemDegrLeveZhanbiFormMap> findPage(CheckItemDegrLeveZhanbiFormMap bmiCheckItemConfigFormMap);

    List<CheckItemDegrLeveZhanbiFormMap> findFZhanbiList(CheckItemDegrLeveZhanbiFormMap smallZhanbiCondition);
}
