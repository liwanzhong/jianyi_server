package com.lanyuan.controller.custom;


import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CustomBelonetoEntFormMap;
import com.lanyuan.entity.CustomCutItemFormMap;
import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.CustomBelonetoEntMapper;
import com.lanyuan.mapper.CustomCutItemMapper;
import com.lanyuan.mapper.CustomInfoMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/custom/info/")
public class CustomInfoController extends BaseController {
	@Inject
	private CustomInfoMapper customInfoMapper;


	@Inject
	private CustomCutItemMapper customCutItemMapper;


	@Inject
	private CustomBelonetoEntMapper customBelonetoEntMapper;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/custom/info/list";
	}


	@RequestMapping("list_client")
	public String list_client(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/front/custom/list";
	}


	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
		CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
		customInfoFormMap=toFormMap(customInfoFormMap, pageNow, pageSize,customInfoFormMap.getStr("orderby"));
		customInfoFormMap.put("column", column);
		customInfoFormMap.put("sort", sort);
		// 获取当前登录用户所在企业的会员列表
		Session session = SecurityUtils.getSubject().getSession();
		UserFormMap userFormMap = (UserFormMap)session.getAttribute("userSession");
		if(userFormMap == null){
			throw  new Exception("用户未登陆!");
		}
		customInfoFormMap.put("organization_id",userFormMap.getLong("organization_id"));
		pageView.setRecords(customInfoMapper.findEnterprisePage_front(customInfoFormMap));
		return pageView;
	}



	@RequestMapping("addPage")
	public String addPage(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/custom/info/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id) throws Exception {
		CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id",id,CustomInfoFormMap.class);
		model.addAttribute("customInfoFormMap",customInfoFormMap);
		return Common.BACKGROUND_PATH + "/custom/info/edit";
	}


	@RequestMapping("addUI")
	public String addUI(Model model,String customid,String idcard) throws Exception {
		if(StringUtils.isNotBlank(customid)){
			CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id",customid,CustomInfoFormMap.class);
			model.addAttribute("customInfoFormMap",customInfoFormMap);
		}
		model.addAttribute("idcard",idcard);
		return Common.BACKGROUND_PATH + "/front/custom/add";
	}

	@RequestMapping("front_edit")
	public String front_edit(Model model,@RequestParam(value = "id",required = true) String id) throws Exception {
		model.addAttribute("customInfoFormMap", customInfoMapper.findbyFrist("id", id, CustomInfoFormMap.class));
		return Common.BACKGROUND_PATH + "/front/custom/edit";
	}




	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
		customInfoFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
		customInfoFormMap=toFormMap(customInfoFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),customInfoFormMap.getStr("orderby"));
		List<CustomInfoFormMap> userFormMapList =customInfoMapper.findEnterprisePage(customInfoFormMap);
		if(CollectionUtils.isNotEmpty(userFormMapList)){
			grid.setRows(userFormMapList);
		}
		PageView pageView = (PageView) customInfoFormMap.get("paging");
		grid.setTotal(pageView.getRowCount());
		return grid;

	}




	@ResponseBody
	@RequestMapping("add")
	@SystemLog(module="用户管理",methods="新增用户")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> add(HttpServletRequest request){
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {

			Session session = SecurityUtils.getSubject().getSession();
			UserFormMap userFormMap = (UserFormMap)session.getAttribute("userSession");
			if(userFormMap == null){
				throw  new Exception("用户未登陆!");
			}


			CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
			customInfoFormMap.put("insert_time",dateFormat.format(new Date()));
			customInfoFormMap.put("update_time",dateFormat.format(new Date()));
			customInfoFormMap.put("isvalid",1);
			customInfoFormMap.put("organization_id",userFormMap.getLong("organization_id"));

			boolean isNewCustom =false;
			if(StringUtils.isBlank(customInfoFormMap.get("id")==null?null:customInfoFormMap.get("id").toString())){
				//todo 检测用户身份证
				CustomInfoFormMap customInfoFormMapCheck  = customInfoMapper.findbyFrist("idcard",customInfoFormMap.get("idcard").toString(),CustomInfoFormMap.class);
				if(customInfoFormMapCheck != null){
					throw new Exception("系统中已经存在相同身份证的用户!");
				}

				customInfoMapper.addEntity(customInfoFormMap);
				isNewCustom = true;
			}else{
				customInfoMapper.editEntity(customInfoFormMap);
			}


			//todo 保存切割项
			//删除原来的切割项
			if(!isNewCustom){
				customCutItemMapper.deleteByAttribute("custom_id",customInfoFormMap.get("id").toString(), CustomCutItemFormMap.class);
			}
			String [] cutItemsArray =  request.getParameterValues("cut_item");
			List<CustomCutItemFormMap> customCutItemFormMapList = new ArrayList<CustomCutItemFormMap>();
			for(String item:cutItemsArray){
				CustomCutItemFormMap customCutItemFormMap = getFormMap(CustomCutItemFormMap.class);
				customCutItemFormMap.put("cut_item_id",item);
				customCutItemFormMap.put("custom_id",customInfoFormMap.get("id").toString());

				customCutItemFormMapList.add(customCutItemFormMap);
			}

			if(CollectionUtils.isNotEmpty(customCutItemFormMapList)){
				customCutItemMapper.batchSave(customCutItemFormMapList);
			}





			//todo 保存完用户以后，绑定关系
			CustomBelonetoEntFormMap customBelonetoEntFormMap = getFormMap(CustomBelonetoEntFormMap.class);
			customBelonetoEntFormMap.put("custom_id",customInfoFormMap.get("id").toString());
			customBelonetoEntFormMap.put("organization_id",userFormMap.getLong("organization_id"));
			customBelonetoEntFormMap.put("insert_time",dateFormat.format(new Date()));
			customBelonetoEntFormMap.put("isdelete",0);
			customBelonetoEntMapper.addEntity(customBelonetoEntFormMap);

			retMap.put("msg","绑定用户成功");
			retMap.put("status",1);
		}catch (Exception ex){
			ex.printStackTrace();
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}



	@ResponseBody
	@RequestMapping("delete")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-删除用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public Map<String,Object>  delete() throws Exception {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			CustomBelonetoEntFormMap customBelonetoEntFormMap = getFormMap(CustomBelonetoEntFormMap.class);
			customBelonetoEntFormMap.put("isdelete",1);
			customBelonetoEntMapper.editEntity(customBelonetoEntFormMap);
			retMap.put("msg","删除成功");
			retMap.put("status",1);
		}catch (Exception ex){
			ex.printStackTrace();
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}





	@ResponseBody
	@RequestMapping("update")
	@SystemLog(module="权限组管理",methods="修改权限组")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object>  update(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
			customInfoMapper.editEntity(customInfoFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
			ex.printStackTrace();
		}
		return retMap;
	}



	@RequestMapping("toVerify")
	public String toVerify(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/front/custom/check";
	}



	@ResponseBody
	@RequestMapping("verify")
	@SystemLog(module="会员管理",methods="验证会员信息")//凡需要处理业务逻辑的.都需要记录操作日志
	public Map<String,Object> verify(@RequestParam(value = "customInfoFormMap.idCard") String idCard){
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("cardid",idCard);
		try {
			if(StringUtils.isBlank(idCard)){
				throw new SystemException("请输入完整的身份证号！");
			}
			Session session = SecurityUtils.getSubject().getSession();
			UserFormMap userFormMap = (UserFormMap)session.getAttribute("userSession");
			if(userFormMap == null){
				throw  new Exception("用户未登陆!");
			}


			//todo 验证系统是否存在这样的一个用户
			CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("idcard",idCard,CustomInfoFormMap.class);
			if(customInfoFormMap == null){
				retMap.put("custom_status",0);
				retMap.put("msg","当前验证会员为系统新会员！");
				return retMap;
			}else{
				// 验证会员是否已经跟当前企业绑定了关系

				CustomBelonetoEntFormMap customBelonetoEntFormMap = getFormMap(CustomBelonetoEntFormMap.class);
				customBelonetoEntFormMap.put("organization_id",userFormMap.getLong("organization_id"));
				customBelonetoEntFormMap.put("custom_id",customInfoFormMap.getLong("id"));
				customBelonetoEntFormMap.put("isdelete",0);

				List<CustomBelonetoEntFormMap> customBelonetoEntFormMapList = customBelonetoEntMapper.findByNames(customBelonetoEntFormMap);

				if(CollectionUtils.isNotEmpty(customBelonetoEntFormMapList)){
					retMap.put("custom_status",2);
					retMap.put("msg","当前检测点已经绑定当前会员，无需创建！");
				}else{
					retMap.put("custom_status",1);
					retMap.put("msg","系统已经存在当前会员，是否绑定当前会员！");
				}
				retMap.put("data",customInfoFormMap);

			}
		} catch (Exception e) {
//			throw new SystemException("验证会员信息异常["+e.getMessage()+"]");
			retMap.put("custom_status",-1);
			retMap.put("msg",e.getMessage());
			e.printStackTrace();
		}
		return retMap;
	}



}