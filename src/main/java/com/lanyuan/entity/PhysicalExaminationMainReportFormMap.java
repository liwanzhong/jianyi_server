package com.lanyuan.entity;

import com.lanyuan.annotation.TableSeg;
import com.lanyuan.util.FormMap;


/**
 * user实体表
 */
@TableSeg(tableName = "physical_examination_main_report", id="id")
public class PhysicalExaminationMainReportFormMap extends FormMap<String,Object>{

	/**
	 *@descript
	 *@author lanyuan
	 *@date 2015年3月29日
	 *@version 1.0
	 */
	private static final long serialVersionUID = 1L;

	public static final Integer STATUS_CHECKED = 1;//检测结束
}
