package com.lanyuan.controller.front;

import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.ResFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.entity.UserLoginFormMap;
import com.lanyuan.mapper.ResourcesMapper;
import com.lanyuan.mapper.UserEntrelationMapper;
import com.lanyuan.mapper.UserLoginMapper;
import com.lanyuan.util.Common;
import com.lanyuan.util.constant.GlobalConstant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检测人员登录
 */
@Controller
@RequestMapping("/front")
public class LoginController extends BaseController {

	@Inject
	private ResourcesMapper resourcesMapper;

	@Inject
	private UserLoginMapper userLoginMapper;




	@RequestMapping(value = "login", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public String login() {
		return "/login-index";
	}

	@ResponseBody
	@RequestMapping(value = "login", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	public Map<String,Object> login(String username, String password, HttpServletRequest request) {
		Map<String,Object> retMap =new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			if (!request.getMethod().equals("POST")) {
				throw new Exception("支持POST方法提交");
			}
			if (Common.isEmpty(username) || Common.isEmpty(password)) {
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
			UserLoginFormMap userLogin = new UserLoginFormMap();
			Session session = SecurityUtils.getSubject().getSession();
			userLogin.put("userId", session.getAttribute("userSessionId"));
			userLogin.put("accountName", username);
			userLogin.put("loginIP", session.getHost());
			userLoginMapper.addEntity(userLogin);

			UserFormMap userFormMap = (UserFormMap)Common.findUserSession(request);
			ResFormMap resFormMap = new ResFormMap();
			resFormMap.put("userId", userFormMap.get("id"));
			List<ResFormMap> mps = resourcesMapper.findRes(resFormMap);
			List<String> list = new ArrayList<String>();
			for (ResFormMap map : mps) {
				String resourUrl = map.get("resUrl").toString();
				list.add(resourUrl);
			}
			session.setAttribute(GlobalConstant.RESOURCES_SESSION_KEY, list);


			retMap.put("status",1);
			retMap.put("msg","success");

		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("msg",e.getMessage());
		}
		return retMap;
	}



	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		try{
			// 使用权限管理工具进行用户的退出，注销登录
			SecurityUtils.getSubject().logout(); // session
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return "/login-index";
	}








}
