package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CfPingfenRoutFormMap;
import com.lanyuan.entity.UserGroupsFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.CfPingfenRoutMapper;
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
@RequestMapping("/instrument/pingfen_rout/")
public class CfPingfenRoutController extends BaseController {



	@Inject
	private CfPingfenRoutMapper cfPingfenRoutMapper;

	@RequestMapping("list")
	public String listUI(Model model,Long check_small_item_id) throws Exception {
		model.addAttribute("small_id",check_small_item_id);
		return Common.BACKGROUND_PATH + "/instrument/cfpingfenrout/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		CfPingfenRoutFormMap cfPingfenRoutFormMap = getFormMap(CfPingfenRoutFormMap.class);
		cfPingfenRoutFormMap=toFormMap(cfPingfenRoutFormMap, pageNow, pageSize,cfPingfenRoutFormMap.getStr("orderby"));
		cfPingfenRoutFormMap.put("column", column);
		cfPingfenRoutFormMap.put("sort", sort);
		pageView.setRecords(cfPingfenRoutMapper.findEnterprisePage(cfPingfenRoutFormMap));//不调用默认分页,调用自已的mapper中findUserPage
		return pageView;
	}




	@RequestMapping("addUI")
	public String addUI(Model model,Long small_id) throws Exception {
		model.addAttribute("small_id",small_id);
		return Common.BACKGROUND_PATH + "/instrument/cfpingfenrout/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module="系统管理",methods="企业管理，新增企业信息")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String addEntity(){
		try {
			SimpleDateFormat datetimeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CfPingfenRoutFormMap cfPingfenRoutFormMap = getFormMap(CfPingfenRoutFormMap.class);
			cfPingfenRoutFormMap.put("update_time",datetimeformat.format(new Date()));

			Double pingfen_min=null;
			Double pingfen_max=null;
			int pingfen = Integer.parseInt(cfPingfenRoutFormMap.get("pingfen").toString());
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
			int tz_pingfen = Integer.parseInt(cfPingfenRoutFormMap.get("tz_pingfen").toString());
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

			cfPingfenRoutFormMap.put("pingfen_min",pingfen_min);
			cfPingfenRoutFormMap.put("pingfen_max",pingfen_max);
			cfPingfenRoutFormMap.put("tz_pingfen_min",tz_pingfen_min);
			cfPingfenRoutFormMap.put("tz_pingfen_max",tz_pingfen_max);

			cfPingfenRoutMapper.addEntity(cfPingfenRoutFormMap);//新增后返回新增信息

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
			cfPingfenRoutMapper.deleteByAttribute("id", id, CfPingfenRoutFormMap.class);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("enterprise", cfPingfenRoutMapper.findbyFrist("id", id, CfPingfenRoutFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/instrument/cfpingfenrout/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String txtGroupsSelect) throws Exception {
		CfPingfenRoutFormMap CfPingfenRoutFormMap = getFormMap(CfPingfenRoutFormMap.class);
		CfPingfenRoutFormMap.put("txtGroupsSelect", txtGroupsSelect);
		cfPingfenRoutMapper.editEntity(CfPingfenRoutFormMap);
		cfPingfenRoutMapper.deleteByAttribute("userId", CfPingfenRoutFormMap.get("id")+"", UserGroupsFormMap.class);
		if(!Common.isEmpty(txtGroupsSelect)){
			String[] txt = txtGroupsSelect.split(",");
			for (String roleId : txt) {
				UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
				userGroupsFormMap.put("userId", CfPingfenRoutFormMap.get("id"));
				userGroupsFormMap.put("roleId", roleId);
				cfPingfenRoutMapper.addEntity(userGroupsFormMap);
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
		CfPingfenRoutFormMap account = cfPingfenRoutMapper.findbyFrist("accountName", name, CfPingfenRoutFormMap.class);
		if (account == null) {
			return true;
		} else {
			return false;
		}
	}


}