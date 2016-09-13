package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.mapper.*;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/instrument/checkItemReleationConfig/")
public class CheckItemReleationConfigController extends BaseController {
	@Inject
	private CheckItemReleationConfigMapper checkItemReleationConfigMapper;



	@Inject
	private CheckSmallItemMapper checkSmallItemMapper;

	@Autowired
	private CheckBigItemMapper checkBigItemMapper;

	@Inject
	private CutItemRefsmallitemConfigMapper cutItemRefsmallitemConfigMapper;




	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	


	@RequestMapping("configRefPage")
	public String grantPage(Model model,String checkid,String checkType)throws Exception {

		CheckSmallItemFormMap checkSmallItemFormMap = checkSmallItemMapper.findbyFrist("id",checkid,CheckSmallItemFormMap.class);
		model.addAttribute("checkSmallItemFormMap",checkSmallItemFormMap);

		CheckItemReleationConfigMap checkItemReleationConfigMap = getFormMap(CheckItemReleationConfigMap.class);
		checkItemReleationConfigMap.put("dist_check_id",checkSmallItemFormMap.getLong("id"));
		List<CheckItemReleationConfigMap> checkItemReleationConfigMapList = checkItemReleationConfigMapper.findByNames(checkItemReleationConfigMap);

		model.addAttribute("checkItemReleationConfigMapList",checkItemReleationConfigMapList);
		//todo 加载检测大小项目
		List<CheckBigItemFormMap> checkBigItemFormMapList = checkBigItemMapper.findByNames(getFormMap(CheckBigItemFormMap.class));

		List<CheckSmallItemFormMap> checkSmallItemFormMapList = checkSmallItemMapper.findByNames(getFormMap(CheckSmallItemFormMap.class));

		model.addAttribute("checkBigItemFormMapList",checkBigItemFormMapList);

		model.addAttribute("checkSmallItemFormMapList",checkSmallItemFormMapList);


		return  Common.BACKGROUND_PATH + "/instrument/checkitemreleationconfig/configRef";
	}


	@ResponseBody
	@RequestMapping("config")
	@SystemLog(module="关联项配置",methods="配置关联项")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> get(HttpServletRequest request, Long dist_check_id)throws Exception {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try{
			// 删除原来的配置
			cutItemRefsmallitemConfigMapper.deleteByAttribute("dist_check_id",dist_check_id+"", CheckItemReleationConfigMap.class);
			//新增配置
			List<CheckItemReleationConfigMap> checkItemReleationConfigMapArrayList = new ArrayList<CheckItemReleationConfigMap>();
			for(String item:request.getParameterValues("refItem")){
				CheckItemReleationConfigMap cutItemRefsmallitemConfigFormMap = getFormMap(CheckItemReleationConfigMap.class);
				cutItemRefsmallitemConfigFormMap.put("dist_releation_type",2);
				cutItemRefsmallitemConfigFormMap.put("org_releation_type",2);
				cutItemRefsmallitemConfigFormMap.put("dist_check_id",dist_check_id);
				cutItemRefsmallitemConfigFormMap.put("org_check_id",item);

				checkItemReleationConfigMapArrayList.add(cutItemRefsmallitemConfigFormMap);
			}
			cutItemRefsmallitemConfigMapper.batchSave(checkItemReleationConfigMapArrayList);
			retMap.put("status",1);
			retMap.put("msg","success");
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}

		return retMap;

	}






}