package com.lanyuan.mapper;

import com.lanyuan.entity.PhysicalExaminationRecordFormMap;
import com.lanyuan.entity.PhysicalExaminationResultFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface PhysicalExaminationResultMapper extends BaseMapper{

	public List<PhysicalExaminationResultFormMap> findEnterprisePage(PhysicalExaminationResultFormMap physicalExaminationResultFormMap);

	List<PhysicalExaminationResultFormMap> findItemCheckResultList(Long examination_record_id);
}
