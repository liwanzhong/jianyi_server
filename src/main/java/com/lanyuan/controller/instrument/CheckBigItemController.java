package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CheckBigItemFormMap;
import com.lanyuan.entity.UserEntrelationFormMap;
import com.lanyuan.entity.UserGroupsFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.CheckBigItemMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.util.CommonConstants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/instrument/bigitem/")
public class CheckBigItemController extends BaseController {
	@Inject
	private CheckBigItemMapper checkBigItemMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/instrument/checkbigitem/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		CheckBigItemFormMap CheckBigItemFormMap = getFormMap(CheckBigItemFormMap.class);
		CheckBigItemFormMap=toFormMap(CheckBigItemFormMap, pageNow, pageSize,CheckBigItemFormMap.getStr("orderby"));
		CheckBigItemFormMap.put("column", column);
		CheckBigItemFormMap.put("sort", sort);
		pageView.setRecords(checkBigItemMapper.findEnterprisePage(CheckBigItemFormMap));//不调用默认分页,调用自已的mapper中findUserPage
		return pageView;
	}




	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/instrument/checkbigitem/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module="系统管理",methods="企业管理，新增企业信息")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String addEntity(String txtGroupsSelect){
		try {
			SimpleDateFormat datetimeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CheckBigItemFormMap checkBigItemFormMap = getFormMap(CheckBigItemFormMap.class);
			checkBigItemFormMap.put("txtGroupsSelect", txtGroupsSelect);
			checkBigItemFormMap.put("insert_time",datetimeformat.format(new Date()));
			checkBigItemFormMap.put("update_time",datetimeformat.format(new Date()));
			checkBigItemFormMap.put("is_show",1);

			// 查询当前排序
			Integer lastcheckbigitem_orderby = checkBigItemMapper.findLasyOrderItemCout();
			checkBigItemFormMap.put("order_by",(lastcheckbigitem_orderby==null?0:lastcheckbigitem_orderby)+1);

			checkBigItemMapper.addEntity(checkBigItemFormMap);//新增后返回新增信息

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
			checkBigItemMapper.deleteByAttribute("id", id, CheckBigItemFormMap.class);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("enterprise", checkBigItemMapper.findbyFrist("id", id, CheckBigItemFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/instrument/info/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String txtGroupsSelect) throws Exception {
		CheckBigItemFormMap CheckBigItemFormMap = getFormMap(CheckBigItemFormMap.class);
		CheckBigItemFormMap.put("txtGroupsSelect", txtGroupsSelect);
		checkBigItemMapper.editEntity(CheckBigItemFormMap);
		checkBigItemMapper.deleteByAttribute("userId", CheckBigItemFormMap.get("id")+"", UserGroupsFormMap.class);
		if(!Common.isEmpty(txtGroupsSelect)){
			String[] txt = txtGroupsSelect.split(",");
			for (String roleId : txt) {
				UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
				userGroupsFormMap.put("userId", CheckBigItemFormMap.get("id"));
				userGroupsFormMap.put("roleId", roleId);
				checkBigItemMapper.addEntity(userGroupsFormMap);
			}
		}
		return "success";
	}




	@ResponseBody
	@RequestMapping("loadAll")
	public List<CheckBigItemFormMap> loadAll(Model model) throws Exception {
		CheckBigItemFormMap checkBigItemFormMap = getFormMap(CheckBigItemFormMap.class);
		return checkBigItemMapper.findByWhere(checkBigItemFormMap);
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
		CheckBigItemFormMap account = checkBigItemMapper.findbyFrist("accountName", name, CheckBigItemFormMap.class);
		if (account == null) {
			return true;
		} else {
			return false;
		}
	}


}