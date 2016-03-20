package com.lanyuan.controller.index;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanyuan.entity.UserEntrelationFormMap;
import com.lanyuan.mapper.UserEntrelationMapper;
import com.lanyuan.util.CommonConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lanyuan.entity.ResFormMap;
import com.lanyuan.entity.UserFormMap;
import com.lanyuan.entity.UserLoginFormMap;
import com.lanyuan.mapper.ResourcesMapper;
import com.lanyuan.mapper.UserLoginMapper;
import com.lanyuan.util.Common;
import com.lanyuan.util.TreeObject;
import com.lanyuan.util.TreeUtil;
import com.mysql.jdbc.Connection;

/**
 * 进行管理后台框架界面的类
 * 
 * @author lanyuan 2015-04-05
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 * @mod  Ekko 2015-09-07
 */
@Controller
@RequestMapping("/")
public class BackgroundController extends BaseController {

	@Inject
	private ResourcesMapper resourcesMapper;

	@Inject
	private UserLoginMapper userLoginMapper;


	@Inject
	private UserEntrelationMapper userEntrelationMapper;
	
	/**
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public String login(HttpServletRequest request) {
		request.removeAttribute("error");
		return "/login";
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

			// 查询用户所属企业和门店，记录到session中

			UserEntrelationFormMap userEntrelationFormMap=userEntrelationMapper.findbyFrist("user_id",session.getAttribute("userSessionId").toString(),UserEntrelationFormMap.class);
			if(userEntrelationFormMap!=null){
				session.setAttribute(CommonConstants.ENERPRISE_RELATION_INSESSION,userEntrelationFormMap);
			}


			retMap.put("status",1);
			retMap.put("msg","success");



		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("msg",e.getMessage());
		}
		return retMap;
	}

	/**
	 * @mod Ekko 2015-09-07
	 * @throws Exception
	 */
	@RequestMapping("index")
	public String index(Model model) throws Exception {
		return "/index";
	}

	@RequestMapping("menu")
	public String menu(Model model) {
		return "/framework/menu";
	}

	/**
	 * 获取某个用户的权限资源
	 * 
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-3-4
	 * @param request
	 * @return
	 */
	@RequestMapping("findAuthority")
	@ResponseBody
	public List<String> findAuthority(HttpServletRequest request) {
		return null;
	}

	@RequestMapping("download")
	public void download(String fileName, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;

		String ctxPath = request.getSession().getServletContext().getRealPath("/") + "\\"
				+ "filezip\\";
		String downLoadPath = ctxPath + fileName;
		System.out.println(downLoadPath);
		try {
			long fileLength = new File(downLoadPath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}

	@ResponseBody
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public Map<String,Object> logout() {

		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try{
			// 使用权限管理工具进行用户的退出，注销登录
			SecurityUtils.getSubject().logout(); // session
			// 会销毁，在SessionListener监听session销毁，清理权限缓存
			retMap.put("status",1);
		}catch (Exception ex){
			ex.printStackTrace();
			retMap.put("msg",ex.getMessage());
		}

		return retMap;
	}


	@RequestMapping(value = "left",method = RequestMethod.GET)
	public String left()throws Exception{
		return  "/common/left";
	}






}
