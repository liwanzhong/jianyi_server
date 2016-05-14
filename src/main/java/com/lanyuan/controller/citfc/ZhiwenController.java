package com.lanyuan.controller.citfc;

import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.service.ICheckService;
import com.lanyuan.service.IZhiwenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *客户端操作接口类
 */
@Controller
@RequestMapping("/citfc/client_call/zhiwen")
public class ZhiwenController extends BaseController {




	@Autowired
	private IZhiwenService zhiwenService;





	/**
	 * 记录指纹信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "record",  produces = "text/json; charset=utf-8")
	public Map<String,Object> login(@RequestParam(value = "customerId",required = true) Long customerId,
									@RequestParam(value = "zhiwenCode",required = true) String zhiwenCode) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			String filePath = "";
			zhiwenService.record(zhiwenCode,filePath);
			retMap.put("status",1);

		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error",e.getMessage());
		}
		return retMap;
	}


	/**
	 * 通过指纹识别码查询用户信息
	 * @param zhiwenCode
	 * @return
     */
	@ResponseBody
	@RequestMapping(value = "queryCustomByZhiwen",  produces = "text/json; charset=utf-8")
	public Map<String,Object> queryCustomByZhiwen(@RequestParam(value = "zhiwenCode",required = true) String zhiwenCode) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			String filePath = "";
			CustomInfoFormMap customInfoFormMap = zhiwenService.queryCustom(zhiwenCode);
			retMap.put("custom",customInfoFormMap);
			retMap.put("status",1);

		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error",e.getMessage());
		}
		return retMap;
	}








}
