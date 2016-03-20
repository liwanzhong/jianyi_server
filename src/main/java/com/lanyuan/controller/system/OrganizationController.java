package com.lanyuan.controller.system;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.OrganizationFormMap;
import com.lanyuan.entity.UserEntrelationFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.OrganizationMapper;
import com.lanyuan.mapper.UserEntrelationMapper;
import com.lanyuan.mapper.UserMapper;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Organization;
import com.lanyuan.vo.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/organization/")
public class OrganizationController extends BaseController {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	@Inject
	private OrganizationMapper organizationMapper;




	@RequestMapping("list")
	public String list(Model model) {
		return Common.BACKGROUND_PATH + "/system/organization/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model) {
		return Common.BACKGROUND_PATH + "/system/organization/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id)throws Exception {
		//todo 加载编辑组织
		OrganizationFormMap organizationFormMap = organizationMapper.findbyFrist("id",id,OrganizationFormMap.class);
		if(null == organizationFormMap){
			throw new Exception("没有对象");
		}
		model.addAttribute("organizationFormMap",organizationFormMap);
		return Common.BACKGROUND_PATH + "/system/organization/edit";
	}



	@ResponseBody
	@RequestMapping("tree")
	public List<Tree> tree(Model model)throws Exception{
		List<Tree> lt = new ArrayList<Tree>();
		OrganizationFormMap organizationFormMap = getFormMap(OrganizationFormMap.class);
		organizationFormMap.put("orderby"," order by  pid asc,seq asc");
		List<OrganizationFormMap> organizationFormMapList = organizationMapper.findEnterprisePage(organizationFormMap);

		if (CollectionUtils.isNotEmpty(organizationFormMapList)) {
			for (OrganizationFormMap r : organizationFormMapList) {
				Tree tree = new Tree();
				tree.setId(r.get("id").toString());
				if (r.get("pid") != null && !StringUtils.equalsIgnoreCase(r.get("pid").toString(),"0")) {
					for (OrganizationFormMap pidItem : organizationFormMapList) {
						if(StringUtils.equalsIgnoreCase(pidItem.get("id").toString(),r.get("pid").toString())){
							tree.setPid(pidItem.get("id").toString());
							break;
						}
					}

				}
				tree.setText(r.get("name").toString());
				tree.setIconCls(r.get("icon")==null?null:r.get("icon").toString());
				lt.add(tree);
			}
		}
		return lt;
	}


	@ResponseBody
	@RequestMapping("treeGrid")
	@SystemLog(module="组织管理",methods="加载组织树形列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public List<Organization> treeGrid()throws Exception {
		List<Organization> lr = new ArrayList<Organization>();
		OrganizationFormMap organizationFormMap = getFormMap(OrganizationFormMap.class);
		organizationFormMap.put("orderby"," order by  pid asc,seq asc");
		List<OrganizationFormMap> organizationFormMapList = organizationMapper.findEnterprisePage(organizationFormMap);
		if (CollectionUtils.isNotEmpty(organizationFormMapList)) {
			for (OrganizationFormMap item : organizationFormMapList) {
				Organization r = new Organization();
				r.setAddress(item.get("address").toString());
				r.setCode(item.get("code").toString());
				r.setCreatedatetime(dateFormat.parse(item.get("createdatetime").toString()));
				r.setIcon(item.get("icon")==null?null:item.get("icon").toString());
				r.setId(Long.parseLong(item.get("id").toString()));
				r.setName(item.get("name").toString());
				r.setSeq(Integer.parseInt(item.get("seq").toString()));
				if (item.get("pid") != null && !StringUtils.equalsIgnoreCase(item.get("pid").toString(),"0")) {
					for (OrganizationFormMap pidItem : organizationFormMapList) {
						if(StringUtils.equalsIgnoreCase(pidItem.get("id").toString(),item.get("pid").toString())){
							r.setPid(Long.parseLong(pidItem.get("id").toString()));
							r.setPname(pidItem.get("name").toString());
							break;
						}
					}

				}
				r.setIconCls(item.get("icon")==null?null:item.get("icon").toString());
				lr.add(r);
			}
		}
		return lr;

	}


	@ResponseBody
	@RequestMapping("add")
	@SystemLog(module="组织管理",methods="新增组织")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object>  add(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			OrganizationFormMap organizationFormMap = getFormMap(OrganizationFormMap.class);
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			organizationFormMap.put("createdatetime", dateFormat.format(new Date()));
			organizationMapper.addEntity(organizationFormMap);
			retMap.put("msg","添加组织成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}

	@ResponseBody
	@RequestMapping("update")
	@SystemLog(module="用户企业关系",methods="建立关系")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object>  update(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			OrganizationFormMap organizationFormMap = getFormMap(OrganizationFormMap.class);
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			organizationFormMap.put("createdatetime", dateFormat.format(new Date()));
			organizationMapper.editEntity(organizationFormMap);
			retMap.put("msg","修改组织成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}


	@ResponseBody
	@RequestMapping("delete")
	@SystemLog(module="用户企业关系",methods="建立关系")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String delete(Model model) {
		try {
			UserEntrelationFormMap userFormMap = getFormMap(UserEntrelationFormMap.class);

			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			userFormMap.put("insert_time", dateFormat.format(new Date()));
			userFormMap.put("is_enterprise_admin", 1);

//			userEntrelationMapper.addEntity(userFormMap);//新增后返回新增信息

		}catch (Exception ex){
			throw new SystemException("添加账号异常");
		}
		return "success";
	}


}