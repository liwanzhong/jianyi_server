package com.lanyuan.controller.instrument;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CutItemFormMap;
import com.lanyuan.entity.UserEntrelationFormMap;
import com.lanyuan.entity.UserGroupsFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.CutItemMapper;
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
@RequestMapping("/instrument/smallitem/")
public class CheckSmallItemController extends BaseController {
	@Inject
	private CutItemMapper cutItemMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/custom/info/list";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		CutItemFormMap cutItemFormMap = getFormMap(CutItemFormMap.class);
		cutItemFormMap=toFormMap(cutItemFormMap, pageNow, pageSize,cutItemFormMap.getStr("orderby"));
		cutItemFormMap.put("column", column);
		cutItemFormMap.put("sort", sort);
		//todo 获取当前登录用户的企业和检测点
		Session session = SecurityUtils.getSubject().getSession();
		UserEntrelationFormMap userEntrelationFormMap = (UserEntrelationFormMap)session.getAttribute(CommonConstants.ENERPRISE_RELATION_INSESSION);

		if(userEntrelationFormMap!=null){//为空表示是系统管理用户,不为空表示是企业的用户
			Integer ent_id = userEntrelationFormMap.getInt("ent_id");
			Integer sub_point_id = userEntrelationFormMap.getInt("sub_point_id");

			cutItemFormMap.put("ent_id",ent_id);
			cutItemFormMap.put("sub_point_id",sub_point_id);
		}

		pageView.setRecords(cutItemMapper.findEnterprisePage(cutItemFormMap));//不调用默认分页,调用自已的mapper中findUserPage
		return pageView;
	}




	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/custom/info/add";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module="系统管理",methods="企业管理，新增企业信息")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String addEntity(String txtGroupsSelect){
		try {
			SimpleDateFormat datetimeformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CutItemFormMap CutItemFormMap = getFormMap(CutItemFormMap.class);
			CutItemFormMap.put("txtGroupsSelect", txtGroupsSelect);
			CutItemFormMap.put("insert_time",datetimeformat.format(new Date()));
			CutItemFormMap.put("update_time",datetimeformat.format(new Date()));

			cutItemMapper.addEntity(CutItemFormMap);//新增后返回新增信息

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
			cutItemMapper.deleteByAttribute("id", id, CutItemFormMap.class);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("enterprise", cutItemMapper.findbyFrist("id", id, CutItemFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/custom/info/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String txtGroupsSelect) throws Exception {
		CutItemFormMap CutItemFormMap = getFormMap(CutItemFormMap.class);
		CutItemFormMap.put("txtGroupsSelect", txtGroupsSelect);
		cutItemMapper.editEntity(CutItemFormMap);
		cutItemMapper.deleteByAttribute("userId", CutItemFormMap.get("id")+"", UserGroupsFormMap.class);
		if(!Common.isEmpty(txtGroupsSelect)){
			String[] txt = txtGroupsSelect.split(",");
			for (String roleId : txt) {
				UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
				userGroupsFormMap.put("userId", CutItemFormMap.get("id"));
				userGroupsFormMap.put("roleId", roleId);
				cutItemMapper.addEntity(userGroupsFormMap);
			}
		}
		return "success";
	}




	@ResponseBody
	@RequestMapping("loadCutItems")
	public List<CutItemFormMap> loadCutItems(Model model) throws Exception {
		CutItemFormMap cutItemFormMap = getFormMap(CutItemFormMap.class);
		return cutItemMapper.findByWhere(cutItemFormMap);
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
		CutItemFormMap account = cutItemMapper.findbyFrist("accountName", name, CutItemFormMap.class);
		if (account == null) {
			return true;
		} else {
			return false;
		}
	}
	

}