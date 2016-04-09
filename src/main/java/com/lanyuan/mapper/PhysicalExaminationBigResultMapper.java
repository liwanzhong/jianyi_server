package com.lanyuan.mapper;

import com.lanyuan.entity.PhysicalExaminationBigResultFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;
import com.lanyuan.mapper.base.BaseMapper;

import java.util.List;


public interface PhysicalExaminationBigResultMapper extends BaseMapper{

	public List<PhysicalExaminationBigResultFormMap> findEnterprisePage(PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap);

	List<PhysicalExaminationBigResultFormMap> findItemCheckResultList(Long examination_record_id);
}
