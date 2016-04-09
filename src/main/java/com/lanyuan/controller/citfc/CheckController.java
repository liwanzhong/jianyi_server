package com.lanyuan.controller.citfc;

import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@Inject
	private CustomInfoMapper customInfoMapper;


	@Inject
	private CustomBelonetoEntMapper customBelonetoEntMapper;


	@Inject
	private CheckSmallItemMapper checkSmallItemMapper;


	@Inject
	private PhysicalExaminationRecordMapper physicalExaminationRecordMapper;


	@Inject
	private PhysicalExaminationResultMapper physicalExaminationResultMapper;


	@Inject
	private CfPingfenLeveMapper cfPingfenLeveMapper;


	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");





	/**
	 * 上传用户检测数据
	 * @param customBelongToId
	 * @param instrumentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "upload", method = RequestMethod.GET, produces = "text/json; charset=utf-8")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Map<String,Object> login(@RequestParam(value = "customBelongToId",defaultValue = "1") String customBelongToId,@RequestParam(value = "instrumentId",defaultValue = "7") String instrumentId) {
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("status",0);
		try {
			// 根据用户id获取用户相关信息
			CustomBelonetoEntFormMap customBelonetoEntFormMap = customBelonetoEntMapper.findbyFrist("id",customBelongToId,CustomBelonetoEntFormMap.class);
			if(null == customBelonetoEntFormMap){
				throw new Exception("无此用户!");
			}

			// 保存检测记录
			PhysicalExaminationRecordFormMap recordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
			recordFormMap.put("custom_id",customBelonetoEntFormMap.getLong("custom_id"));
			recordFormMap.put("organization_id",customBelonetoEntFormMap.get("organization_id").toString());
			recordFormMap.put("instrument_id",instrumentId);
			recordFormMap.put("status",PhysicalExaminationRecordFormMap.STATUS_CHECKED);
			recordFormMap.put("check_time",dateFormat.format(new Date()));
			recordFormMap.put("update_time",dateFormat.format(new Date()));
			physicalExaminationRecordMapper.addEntity(recordFormMap);


			// 获取所有的项目（排除切割项目关联项 ）
			List<CheckSmallItemFormMap> checkSmallItemFormMapList =  checkSmallItemMapper.getAllButCustomCutedItem(customBelonetoEntFormMap.get("custom_id").toString());
			if(CollectionUtils.isNotEmpty(checkSmallItemFormMapList)){

				// 获取评分标准
				List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(getFormMap(CfPingfenLeveFormMap.class));
				System.out.println(cfPingfenLeveFormMapList);

				List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = new ArrayList<PhysicalExaminationResultFormMap>();
				for(CheckSmallItemFormMap checkSmallItemFormMap:checkSmallItemFormMapList){

					BigDecimal n1 = checkSmallItemFormMap.getBigDecimal("min_value");
					BigDecimal n2 = checkSmallItemFormMap.getBigDecimal("max_value");

					PhysicalExaminationResultFormMap physicalExaminationResultFormMap = getFormMap(PhysicalExaminationResultFormMap.class);
					physicalExaminationResultFormMap.put("examination_record_id",recordFormMap.get("id").toString());
					physicalExaminationResultFormMap.put("bit_item_id",checkSmallItemFormMap.get("big_item_id").toString());
					physicalExaminationResultFormMap.put("small_item_id",checkSmallItemFormMap.get("id").toString());
					physicalExaminationResultFormMap.put("gen_min_value",n1);
					physicalExaminationResultFormMap.put("gen_max_value",n2);

					BigDecimal n = (n2.subtract(n1));

					physicalExaminationResultFormMap.put("gen_in_value",n);
					physicalExaminationResultFormMap.put("gen_quanzhong",checkSmallItemFormMap.get("quanzhong").toString());
					//计算

					BigDecimal M = n.divide(new BigDecimal(20.0),3, RoundingMode.HALF_UP);
					physicalExaminationResultFormMap.put("in_value_score",M);
					//随机生成
					Random random = new Random();
					int max = physicalExaminationResultFormMap.getBigDecimal("gen_max_value").multiply(new BigDecimal(1000)).intValue();
					int min = physicalExaminationResultFormMap.getBigDecimal("gen_min_value").multiply(new BigDecimal(1000)).intValue();
					int randomNumber = (int) Math.round(Math.random()*(max-min)+min);

					BigDecimal A = new BigDecimal(randomNumber/1000);
					physicalExaminationResultFormMap.put("check_value",A);

					BigDecimal B = new BigDecimal(100).subtract((A.subtract(n1).divide(M,3,BigDecimal.ROUND_HALF_UP))) ;

					physicalExaminationResultFormMap.put("item_score",B);

					//根据计算结果判断\
					for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
						if(item.getDouble("pingfen_min")<=B.doubleValue() && item.getDouble("pingfen_max")>=B.doubleValue()){
							physicalExaminationResultFormMap.put("orgin_leve_id",item.getLong("id"));
							break;
						}
					}

					BigDecimal Sco = B.multiply(checkSmallItemFormMap.getBigDecimal("quanzhong"));
					physicalExaminationResultFormMap.put("quanzhong_score",Sco);

					physicalExaminationResultFormMapList.add(physicalExaminationResultFormMap);



				}
				physicalExaminationResultMapper.batchSave(physicalExaminationResultFormMapList);
			}


		} catch (Exception e) {
			e.printStackTrace();
			retMap.put("error",e.getMessage());
		}
		return retMap;
	}













}
