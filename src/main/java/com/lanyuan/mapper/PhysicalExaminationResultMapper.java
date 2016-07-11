package com.lanyuan.mapper;

import com.lanyuan.entity.PhysicalExaminationResultFormMap;
import com.lanyuan.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PhysicalExaminationResultMapper extends BaseMapper{

	public List<PhysicalExaminationResultFormMap> findEnterprisePage(PhysicalExaminationResultFormMap physicalExaminationResultFormMap);

	List<PhysicalExaminationResultFormMap> findItemCheckResultList(@Param(value="examination_record_id")Long examination_record_id,@Param(value="big_item_id") Long big_item_id);

	void insertByHistory(PhysicalExaminationResultFormMap physicalExaminationResultFormMap);

	int resultSizeByRecordid(@Param(value = "examination_record_id") Long examination_record_id);
}
