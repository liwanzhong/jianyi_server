package com.lanyuan.controller.system;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.ResFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.mapper.ResourcesMapper;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/resource/")
public class ResourcesController extends BaseController {
	@Inject
	private ResourcesMapper resourcesMapper;




	@RequestMapping("list")
	public String list(Model model) {
		return Common.BACKGROUND_PATH + "/system/resources/list";
	}

	@RequestMapping("addPage")
	public String addPage(Model model) {
		return Common.BACKGROUND_PATH + "/system/resources/add";
	}

	@RequestMapping("editPage")
	public String editPage(Model model,String id)throws Exception {
		ResFormMap resFormMap = resourcesMapper.findbyFrist("id",id,ResFormMap.class);
		if(null == resFormMap){
			throw new Exception("没有对象");
		}
		model.addAttribute("resFormMap",resFormMap);
		return Common.BACKGROUND_PATH + "/system/resources/edit";
	}


	@ResponseBody
	@RequestMapping("tree")
	public List<Tree> treelists(Model model, HttpServletRequest request) {
		List<Tree> list = new ArrayList<Tree>();
		try{
			UserFormMap userFormMap = (UserFormMap)Common.findUserSession(request);

			ResFormMap resFormMap = getFormMap(ResFormMap.class);
			resFormMap.put("userid",userFormMap.getInt("id"));
			List<ResFormMap> mps = resourcesMapper.findResByUserID(resFormMap);

			if(CollectionUtils.isNotEmpty(mps)){
				for (ResFormMap map:mps) {
					if(map!=null){
						if(StringUtils.equalsIgnoreCase(map.getStr("type"),"0")||StringUtils.equalsIgnoreCase(map.getStr("type"),"1")){
							Tree tree = new Tree();
							tree.setId(map.get("id").toString());
							if (map.get("parentId") != null && !StringUtils.equalsIgnoreCase(map.get("parentId").toString(),"0")) {
								tree.setPid(map.get("parentId").toString());
							} else {
								tree.setState("closed");
							}
							tree.setText(map.get("name").toString());
							if(null!=map.get("icon")){
								tree.setIconCls(map.get("icon").toString());
							}
							if(StringUtils.equalsIgnoreCase(map.get("type").toString(),"1")){
								Map<String, Object> attr = new HashMap<String, Object>();
								attr.put("url", map.get("resUrl")!=null?map.get("resUrl").toString():null);
								tree.setAttributes(attr);
							}
							list.add(tree);
						}

					}

				}
			}


		}catch (Exception ex){
			ex.printStackTrace();
		}

		return list;
	}



	@ResponseBody
	@RequestMapping("treeGrid")
	@SystemLog(module="组织管理",methods="加载组织树形列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public List<ResFormMap> treeGrid()throws Exception {
		ResFormMap resFormMap = getFormMap(ResFormMap.class);
		List<ResFormMap> resFormMapList = resourcesMapper.findRes(resFormMap);
		return resFormMapList;
	}


	@ResponseBody
	@RequestMapping("add")
	@SystemLog(module="组织管理",methods="新增组织")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object>  add(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			ResFormMap resFormMap = getFormMap(ResFormMap.class);
			resourcesMapper.addEntity(resFormMap);
			retMap.put("msg","添加成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}

	@ResponseBody
	@RequestMapping("update")
	@SystemLog(module="资源管理",methods="更新资源")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object>  update(Model model) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			ResFormMap resFormMap = getFormMap(ResFormMap.class);
			resourcesMapper.editEntity(resFormMap);
			retMap.put("msg","修改成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}


	@ResponseBody
	@RequestMapping("delete")
	@SystemLog(module="资源管理",methods="删除资源")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> delete(Model model,String id) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			resourcesMapper.deleteByAttribute("parentId",id,ResFormMap.class);
			resourcesMapper.deleteByAttribute("id",id,ResFormMap.class);
			retMap.put("msg","删除成功");
			retMap.put("status",1);
		}catch (Exception ex){
			retMap.put("msg",ex.getMessage());
		}
		return retMap;
	}


}