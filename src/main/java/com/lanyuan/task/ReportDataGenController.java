package com.lanyuan.task;

import br.eti.mertz.wkhtmltopdf.wrapper.Pdf;
import br.eti.mertz.wkhtmltopdf.wrapper.page.PageType;
import br.eti.mertz.wkhtmltopdf.wrapper.params.Param;
import com.lanyuan.entity.PhysicalExaminationBigResultFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;
import com.lanyuan.mapper.PhysicalExaminationBigResultMapper;
import com.lanyuan.mapper.PhysicalExaminationRecordMapper;
import com.lanyuan.service.ICheckService;
import com.lanyuan.util.MergePDF;
import com.lanyuan.util.PropertiesUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
@Lazy(false)
public class ReportDataGenController {

	@Inject
	private PhysicalExaminationRecordMapper physicalExaminationRecordMapper;

	@Autowired
	private ICheckService checkService;



	private static Logger logger = Logger.getLogger(ReportDataGenController.class);

	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	@Scheduled(cron = "0 0/5 * * * ? ")
	public void task() throws Exception {
		logger.info("======================================生成报告数据================start==============================================================");
		// 获取没有生成的列表
		PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = new PhysicalExaminationRecordFormMap();
		physicalExaminationRecordFormMap.put("status",1);
		physicalExaminationRecordFormMap.put("before_minute",-10);
		//查询条件
		List<PhysicalExaminationRecordFormMap> physicalExaminationRecordFormMapList =physicalExaminationRecordMapper.findNeed2GenData(physicalExaminationRecordFormMap);

		if(CollectionUtils.isNotEmpty(physicalExaminationRecordFormMapList)){
			logger.info("======================================有【"+physicalExaminationRecordFormMapList.size()+"】条检测记录需要生成报告数据==============================================================");
			// 迭代生成pdf报告
			for(PhysicalExaminationRecordFormMap item:physicalExaminationRecordFormMapList){
				try{
					//todo 生成检测数据
					checkService.deleteGenedData(item);
					checkService.genCheckResult(item);
					checkService.genSickRiskResult(item);
					item.put("status",2);
					item.put("update_time",dateTimeFormat.format(new Date()));
					physicalExaminationRecordMapper.editEntity(item);
				}catch (Exception ex){
					logger.error(ex.getMessage());
					ex.printStackTrace();
				}

			}

		}
		logger.info("======================================生成报告数据================end==============================================================");
	}





}