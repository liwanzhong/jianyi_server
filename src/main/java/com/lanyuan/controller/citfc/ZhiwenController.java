package com.lanyuan.controller.citfc;

import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.CustomInfoFormMap;
import com.lanyuan.service.ICheckService;
import com.lanyuan.service.IZhiwenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *客户端操作接口类
 */
@Controller
@RequestMapping("/citfc/client_call/zhiwen")
public class ZhiwenController extends BaseController {


	@Autowired
	private IZhiwenService zhiwenService;

	private static Logger logger = LoggerFactory.getLogger(ZhiwenController.class);



	/**
	 * 记录指纹信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "record",  produces = "text/json; charset=utf-8")
	public Map<String,Object> record(@RequestParam(value = "customerId",required = true) Long customerId,
									@RequestParam(value = "zhiwenCode",required = true) String zhiwenCode,
									 @RequestParam(value = "pic",required = false)String zhiwenPIC,
									 @RequestParam(value = "position",required = false)Integer position,
									 @RequestParam(value = "instrument",required = true)String instrument) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			//todo 如果有图片，保存图片，获取图片保存的服务器目录
			String filePath = "";
			zhiwenService.record(customerId,zhiwenCode,filePath,position,instrument);
			retMap.put("status",1);
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error",e.getMessage());
		}
		return retMap;
	}


	/**
	 * 通过用户id获取用户指纹列表
	 * @param customId 用户id
	 * @return
     */
	@ResponseBody
	@RequestMapping(value = "queryZhiwenByCustomid",  produces = "text/json; charset=utf-8")
	public Map<String,Object> queryZhiwenByCustomid(@RequestParam(value = "customId",required = true) Long customId) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			List<String> zhiwenList = zhiwenService.queryZhiwenByCustomId(customId);
			retMap.put("zhiwenList",zhiwenList);
			retMap.put("customid",customId);
			retMap.put("status",1);
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error",e.getMessage());
			logger.error(e.getMessage());
		}
		return retMap;
	}








}
