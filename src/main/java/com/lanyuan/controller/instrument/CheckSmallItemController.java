package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CheckSmallItemFormMap;
import com.lanyuan.entity.UserGroupsFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.CheckSmallItemMapper;
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
@RequestMapping("/instrument/smallitem/")
public class CheckSmallItemController extends BaseController {


	@Inject
	private CheckSmallItemMapper checkSmallItemMapper;

	@RequestMapping("list")
	public String listUI(Model model,Long big_item_id) throws Exception {
//		model.addAttribute("res", findByRes());
		model.addAttribute("big_item_id",big_item_id);
		return Common.BACKGROUND_PATH + "/instrument/checksmallitem/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		CheckSmallItemFormMap checkSmallItemFormMap = getFormMap(CheckSmallItemFormMap.class);
		checkSmallItemFormMap=toFormMap(checkSmallItemFormMap, pageNow, pageSize,checkSmallItemFormMap.getStr("orderby"));
		checkSmallItemFormMap.put("column", column);
		checkSmallItemFormMap.put("sort", sort);
		pageView.setRecords(checkSmallItemMapper.findEnterprisePage(checkSmallItemFormMap));//不调用默认分页,调用自已的mapper中findUserPage
		return pageView;
	}




	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/instrument/checksmallitem/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module="系统管理",methods="企业管理，新增企业信息")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String addEntity(String txtGroupsSelect){
		try {
			SimpleDateFormat datetimeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CheckSmallItemFormMap CheckSmallItemFormMap = getFormMap(CheckSmallItemFormMap.class);
			CheckSmallItemFormMap.put("txtGroupsSelect", txtGroupsSelect);
			CheckSmallItemFormMap.put("insert_time",datetimeformat.format(new Date()));
			CheckSmallItemFormMap.put("update_time",datetimeformat.format(new Date()));
			CheckSmallItemFormMap.put("is_show",1);

			// 查询当前排序
			Integer lastcheckbigitem_orderby = checkSmallItemMapper.findLasyOrderItemCout();
			CheckSmallItemFormMap.put("order_by",(lastcheckbigitem_orderby==null?0:lastcheckbigitem_orderby)+1);

			checkSmallItemMapper.addEntity(CheckSmallItemFormMap);//新增后返回新增信息

		} catch (Exception e) {
			throw new SystemException("添加账号异常");
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
			checkSmallItemMapper.deleteByAttribute("id", id, CheckSmallItemFormMap.class);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("enterprise", checkSmallItemMapper.findbyFrist("id", id, CheckSmallItemFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/instrument/checksmallitem/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String txtGroupsSelect) throws Exception {
		CheckSmallItemFormMap CheckSmallItemFormMap = getFormMap(CheckSmallItemFormMap.class);
		CheckSmallItemFormMap.put("txtGroupsSelect", txtGroupsSelect);
		checkSmallItemMapper.editEntity(CheckSmallItemFormMap);
		checkSmallItemMapper.deleteByAttribute("userId", CheckSmallItemFormMap.get("id")+"", UserGroupsFormMap.class);
		if(!Common.isEmpty(txtGroupsSelect)){
			String[] txt = txtGroupsSelect.split(",");
			for (String roleId : txt) {
				UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
				userGroupsFormMap.put("userId", CheckSmallItemFormMap.get("id"));
				userGroupsFormMap.put("roleId", roleId);
				checkSmallItemMapper.addEntity(userGroupsFormMap);
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
		CheckSmallItemFormMap account = checkSmallItemMapper.findbyFrist("accountName", name, CheckSmallItemFormMap.class);
		if (account == null) {
			return true;
		} else {
			return false;
		}
	}


}