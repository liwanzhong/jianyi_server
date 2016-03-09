package com.lanyuan.controller.citfc;

import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.entity.UserEntrelationFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.mapper.CustomInfoMapper;
import com.lanyuan.mapper.UserEntrelationMapper;
import com.lanyuan.util.Common;
import com.lanyuan.vo.CustomVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *客户端操作接口类
 */
@Controller
@RequestMapping("/citfc/client_call")
public class ClientCallController extends BaseController {


	/**
	 * 客户端登录接口
	 * @param username
	 * @param password
	 * @param mathcode
	 * @param request
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "login"/*, method = RequestMethod.POST*/, produces = "text/json; charset=utf-8")
	public Map<String,Object> login(String username, String password,String mathcode, HttpServletRequest request) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				throw new Exception("用户名或密码不能为空！");
			}
			// 想要得到 SecurityUtils.getSubject()　的对象．．访问地址必须跟ｓｈｉｒｏ的拦截地址内．不然后会报空指针
			Subject user = SecurityUtils.getSubject();
			// 用户输入的账号和密码,,存到UsernamePasswordToken对象中..然后由shiro内部认证对比,
			// 认证执行者交由ShiroDbRealm中doGetAuthenticationInfo处理
			// 当以上认证成功后会向下执行,认证失败会抛出异常
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			try {
				user.login(token);
			} catch (LockedAccountException lae) {
				token.clear();
				throw new Exception("用户已经被锁定不能登录，请与管理员联系！");
			} catch (ExcessiveAttemptsException e) {
				token.clear();
				throw new Exception("账号：" + username + " 登录失败次数过多,锁定10分钟!");
			} catch (AuthenticationException e) {
				token.clear();
				throw new Exception("用户或密码不正确！");
			}

			UserFormMap userFormMap = (UserFormMap)Common.findUserSession(request);
			retMap.put("status",1);
			retMap.put("error","登录成功!");
			retMap.put("entity",userFormMap);
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error","登录异常["+e.getMessage()+"]，请联系管理员！");
		}
		return retMap;
	}




	/**
	 * 客户端添加客户接口
	 * @param customVO
	 * @param mathcode
	 * @return
     */
	@ResponseBody
	@RequestMapping(value = "addcustom", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public Map<String,Object> addCustom(CustomVO customVO,String mathcode,Long oper) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			if (customVO == null || StringUtils.isBlank(mathcode)){
				throw new Exception("参数异常!");
			}
			//todo 查询登录检测员所属检测点/通过检测码确认当前客户所属检测点

			//todo 查询用户是否存在




			//todo 返回已经保存的对象给前台

			retMap.put("status",1);
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error","登录异常["+e.getMessage()+"]，请联系管理员！");
		}
		return retMap;
	}


	@Autowired
	private CustomInfoMapper customInfoMapper;


	@Autowired
	private UserEntrelationMapper userEntrelationMapper;

	/**
	 * 查询客户信息（通过客户名称）
	 * @param customName 客户名称
	 * @param phone 手机号
	 * @param meathcode 机器码
	 * @param userid 用户id
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "querycustom", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public Map<String,Object> queryCustom(String customName,String phone,String meathcode,Long userid) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			if (StringUtils.isBlank(customName) && StringUtils.isBlank(phone)){
				throw new Exception("参数异常[客户名称或者手机号必须]!");
			}

			//获取企业id和检测点id

			UserEntrelationFormMap userEntrelationFormMap = userEntrelationMapper.findbyFrist("user_id",userid+"", UserEntrelationFormMap.class);
			if(userEntrelationFormMap==null){
				throw new Exception("无此系统用户!");
			}


			//todo 通过客户名称，所属企业和检测点查询所有同名的用户
			CustomInfoFormMap customInfoFormMap = new CustomInfoFormMap();
			if(StringUtils.isNotBlank(phone)){
				customInfoFormMap.set("mobile",phone);
			}
			if(StringUtils.isNotBlank(customName)){
				customInfoFormMap.set("name",customName);
			}
			customInfoFormMap.set("ent_id",userEntrelationFormMap.get("ent_id").toString());
			customInfoFormMap.set("sub_point_id",userEntrelationFormMap.get("sub_point_id").toString());
			List<CustomInfoFormMap> customInfoFormMapList =  customInfoMapper.findEnterprisePage(customInfoFormMap);


			//todo 返回列表给客户端展示
			retMap.put("data",customInfoFormMapList);
			retMap.put("status",1);
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error","登录异常["+e.getMessage()+"]，请联系管理员！");
		}
		return retMap;
	}


	/**
	 * 客户端配置接口(机器码/请求地址(域名)/......)
	 * @param customName
	 * @param mathcode
	 * @param oper
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "configm", method = RequestMethod.POST, produces = "text/json; charset=utf-8")
	public Map<String,Object> configm(String customName,String mathcode,Long oper) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			if (StringUtils.isBlank(customName) || StringUtils.isBlank(mathcode) || oper == null){
				throw new Exception("参数异常!");
			}

			//todo 通过客户名称，所属企业和检测点查询所有同名的用户


			//todo 返回列表给客户端展示

			retMap.put("status",1);
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error","登录异常["+e.getMessage()+"]，请联系管理员！");
		}
		return retMap;
	}



}
