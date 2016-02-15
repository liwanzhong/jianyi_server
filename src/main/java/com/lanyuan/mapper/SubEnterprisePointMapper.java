package com.lanyuan.mapper;

import com.lanyuan.entity.EnterpriseFormMap;
import com.lanyuan.entity.SubEnterprisePointFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface SubEnterprisePointMapper extends BaseMapper{

	public List<SubEnterprisePointFormMap> findEnterprisePage(SubEnterprisePointFormMap enterpriseFormMap);

	List<SubEnterprisePointFormMap> findByEntid(SubEnterprisePointFormMap userFormMap);
}
