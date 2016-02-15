package com.lanyuan.controller.system;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.ResourcesMapper;
import com.lanyuan.mapper.UserEntrelationMapper;
import com.lanyuan.mapper.UserMapper;
import com.lanyuan.util.Common;
import com.lanyuan.util.PasswordHelper;
import com.lanyuan.util.TreeObject;
import com.lanyuan.util.TreeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/build_entrelation/")
public class UserEntRelationController extends BaseController {
	@Inject
	private UserEntrelationMapper userEntrelationMapper;

	@Inject
	private UserMapper userMapper;


	/**
	 * 权限分配页面
	 *
	 * @author lanyuan
	 * @param model
	 * @return
	 */
	@RequestMapping("build")
	public String build(Model model,Long userId) {
		//todo 检查用户是否被分配到企业
		//todo 加载用户相关信息
		UserFormMap userFormMap= userMapper.findbyFrist("id",String.valueOf(userId),UserFormMap.class);
		model.addAttribute("userformmap",userFormMap);
		return Common.BACKGROUND_PATH + "/system/build_entrelation/build";
	}


	@ResponseBody
	@RequestMapping("buildRelation")
	@SystemLog(module="用户企业关系",methods="建立关系")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String buildRelation(Model model) {
		try {
			UserEntrelationFormMap userFormMap = getFormMap(UserEntrelationFormMap.class);
			if(checkis_builded()){//已经被添加到其他企业
				throw new SystemException("添加账号异常");
			}
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			userFormMap.put("insert_time", dateFormat.format(new Date()));
			userFormMap.put("is_enterprise_admin", 1);

			userEntrelationMapper.addEntity(userFormMap);//新增后返回新增信息

		}catch (Exception ex){
			throw new SystemException("添加账号异常");
		}
		return "success";
	}


	/**
	 * 检查是否已经被分配到企业
	 * @return
	 * @throws Exception
	 */
	private boolean checkis_builded()throws Exception{

		return false;
	}


}