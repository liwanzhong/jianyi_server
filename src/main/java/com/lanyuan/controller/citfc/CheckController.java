package com.lanyuan.controller.citfc;

import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.mapper.*;
import com.lanyuan.service.ICheckService;
import com.lanyuan.util.AgeCal;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *客户端操作接口类
 */
@Controller
@RequestMapping("/citfc/client_call/check")
public class CheckController extends BaseController {




	@Autowired
	private ICheckService checkService;





	/**
	 * 上传用户检测数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "upload",  produces = "text/json; charset=utf-8")
//	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> login(@RequestParam(value = "customerId",required = true) Long customerId,@RequestParam(value = "instrumentCode",required = true) String instrumentCode) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {

			checkService.recordCheckResult(instrumentCode,customerId);
			retMap.put("status",1);

		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error",e.getMessage());
		}
		return retMap;
	}





	public static void main(String [] args){
		for(int i=0;i<1000;i++){
			int max = new BigDecimal(6.215).multiply(new BigDecimal(1000)).intValue();
			int min =new BigDecimal(0.856).multiply(new BigDecimal(1000)).intValue();
			int randomNumber = (int) Math.round(Math.random()*(max-min)+min);

			BigDecimal A = new BigDecimal(randomNumber/1000d);
			if(A.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue()<min || A.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue()>max){
				System.out.println(A.setScale(3,BigDecimal.ROUND_HALF_UP));
			}

		}

	}








}
