package com.lanyuan.controller.stats;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.OrganizationFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;
import com.lanyuan.mapper.PhysicalExaminationRecordMapper;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Organization;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/stats/phystats")
public class PhysicStatisController extends BaseController {


	@Inject
	private PhysicalExaminationRecordMapper physicalExaminationRecordMapper;




	@RequestMapping("list")
	public String list(Model model) {
		return Common.BACKGROUND_PATH + "/stats/phystats/list";
	}



	@ResponseBody
	@RequestMapping("treeGrid")
	@SystemLog(module="检测统计",methods="检测概要列表")//凡需要处理业务逻辑的.都需要记录操作日志
	public List<PhysicalExaminationRecordFormMap> treeGrid()throws Exception {
		PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
		List<PhysicalExaminationRecordFormMap> physicalExaminationRecordFormMapList  = physicalExaminationRecordMapper.statsExamination(physicalExaminationRecordFormMap);
		return physicalExaminationRecordFormMapList;
	}


	@ResponseBody
	@RequestMapping("totalExamination")
	@SystemLog(module="检测统计",methods="总检测量统计")//凡需要处理业务逻辑的.都需要记录操作日志
	public PhysicalExaminationRecordFormMap totalExamination()throws Exception {
		PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap  = physicalExaminationRecordMapper.statsExamination_Total();
		return physicalExaminationRecordFormMap;

	}











}