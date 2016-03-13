package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CfPingfenLeveFormMap;
import com.lanyuan.entity.UserGroupsFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.CfPingfenLeveMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/instrument/pingfen_leve/")
public class CfPingfenLeveController extends BaseController {



	@Inject
	private CfPingfenLeveMapper cfPingfenLeveMapper;

	@RequestMapping("list")
	public String listUI(Model model,Long check_small_item_id) throws Exception {
		model.addAttribute("small_id",check_small_item_id);
		return Common.BACKGROUND_PATH + "/instrument/cfpingfenleve/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		CfPingfenLeveFormMap CfPingfenLeveFormMap = getFormMap(CfPingfenLeveFormMap.class);
		CfPingfenLeveFormMap=toFormMap(CfPingfenLeveFormMap, pageNow, pageSize,CfPingfenLeveFormMap.getStr("orderby"));
		CfPingfenLeveFormMap.put("column", column);
		CfPingfenLeveFormMap.put("sort", sort);
		pageView.setRecords(cfPingfenLeveMapper.findEnterprisePage(CfPingfenLeveFormMap));//不调用默认分页,调用自已的mapper中findUserPage
		return pageView;
	}




	@RequestMapping("addUI")
	public String addUI(Model model,Long small_id) throws Exception {
		model.addAttribute("small_id",small_id);
		return Common.BACKGROUND_PATH + "/instrument/cfpingfenleve/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module="系统管理",methods="企业管理，新增企业信息")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String addEntity(){
		try {
			SimpleDateFormat datetimeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CfPingfenLeveFormMap CfPingfenLeveFormMap = getFormMap(CfPingfenLeveFormMap.class);
			CfPingfenLeveFormMap.put("update_time",datetimeformat.format(new Date()));

			Double pingfen_min=null;
			Double pingfen_max=null;
			int pingfen = Integer.parseInt(CfPingfenLeveFormMap.get("pingfen").toString());
			switch (pingfen){
				case 1:
					pingfen_min = 0d;
					pingfen_max = 59.99;
					break;
				case 2:
					pingfen_min = 60d;
					pingfen_max = 69.99;
					break;
				case 3:
					pingfen_min = 70d;
					pingfen_max = 79.99;
					break;
				case 4:
					pingfen_min = 80d;
					pingfen_max = 89.99;
					break;
				case 5:
					pingfen_min = 90d;
					pingfen_max = 100d;
					break;

			}

			Double tz_pingfen_min=null;
			Double tz_pingfen_max=null;
			int tz_pingfen = Integer.parseInt(CfPingfenLeveFormMap.get("tz_pingfen").toString());
			switch (tz_pingfen){
				case 1:
					tz_pingfen_min = 0d;
					tz_pingfen_max = 59.99;
					break;
				case 2:
					tz_pingfen_min = 60d;
					tz_pingfen_max = 69.99;
					break;
				case 3:
					tz_pingfen_min = 70d;
					tz_pingfen_max = 79.99;
					break;
				case 4:
					tz_pingfen_min = 80d;
					tz_pingfen_max = 89.99;
					break;
				case 5:
					tz_pingfen_min = 90d;
					tz_pingfen_max = 100d;
					break;
			}

			CfPingfenLeveFormMap.put("pingfen_min",pingfen_min);
			CfPingfenLeveFormMap.put("pingfen_max",pingfen_max);
			CfPingfenLeveFormMap.put("tz_pingfen_min",tz_pingfen_min);
			CfPingfenLeveFormMap.put("tz_pingfen_max",tz_pingfen_max);

			cfPingfenLeveMapper.addEntity(CfPingfenLeveFormMap);//新增后返回新增信息

		} catch (Exception e) {
			throw new SystemException("新增评分概率异常");
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="删除企业")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String[] ids = getParaValues("ids");
		for (String id : ids) {
			cfPingfenLeveMapper.deleteByAttribute("id", id, CfPingfenLeveFormMap.class);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("enterprise", cfPingfenLeveMapper.findbyFrist("id", id, CfPingfenLeveFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/instrument/cfpingfenleve/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String txtGroupsSelect) throws Exception {
		CfPingfenLeveFormMap CfPingfenLeveFormMap = getFormMap(CfPingfenLeveFormMap.class);
		CfPingfenLeveFormMap.put("txtGroupsSelect", txtGroupsSelect);
		cfPingfenLeveMapper.editEntity(CfPingfenLeveFormMap);
		cfPingfenLeveMapper.deleteByAttribute("userId", CfPingfenLeveFormMap.get("id")+"", UserGroupsFormMap.class);
		if(!Common.isEmpty(txtGroupsSelect)){
			String[] txt = txtGroupsSelect.split(",");
			for (String roleId : txt) {
				UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
				userGroupsFormMap.put("userId", CfPingfenLeveFormMap.get("id"));
				userGroupsFormMap.put("roleId", roleId);
				cfPingfenLeveMapper.addEntity(userGroupsFormMap);
			}
		}
		return "success";
	}





	/**
	 * 验证账号是否存在
	 *
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-2-19
	 * @param name
	 * @return
	 */
	@RequestMapping("isExist")
	@ResponseBody
	public boolean isExist(String name) {
		CfPingfenLeveFormMap account = cfPingfenLeveMapper.findbyFrist("accountName", name, CfPingfenLeveFormMap.class);
		if (account == null) {
			return true;
		} else {
			return false;
		}
	}


}