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
	private CheckBigItemMapper checkBigItemMapper;


	@Inject
	private PhysicalExaminationRecordMapper physicalExaminationRecordMapper;


	@Inject
	private PhysicalExaminationResultMapper physicalExaminationResultMapper;


	@Inject
	private CfPingfenLeveMapper cfPingfenLeveMapper;


	@Inject
	private PhysicalExaminationMainReportMapper physicalExaminationMainReportMapper;


	@Inject
	private PhysicalExaminationBigResultMapper physicalExaminationBigResultMapper;


	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");





	/**
	 * 上传用户检测数据
	 * @param customBelongToId
	 * @param instrumentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "upload",  produces = "text/json; charset=utf-8")
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

			CustomInfoFormMap customInfoFormMap = getFormMap(CustomInfoFormMap.class);
			customInfoFormMap.put("id",customBelonetoEntFormMap.getLong("custom_id"));
			List<CustomInfoFormMap> customInfoFormMapList = customInfoMapper.findByNames(customInfoFormMap);
			if(CollectionUtils.isEmpty(customInfoFormMapList)){
				throw new Exception("无此用户!");
			}
			customInfoFormMap = customInfoFormMapList.get(0);

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


				Map<Long,List<PhysicalExaminationResultFormMap>> itemMap =  new HashMap<Long, List<PhysicalExaminationResultFormMap>>();

				for(CheckSmallItemFormMap checkSmallItemFormMap:checkSmallItemFormMapList){

					BigDecimal n1 = checkSmallItemFormMap.getBigDecimal("min_value");
					BigDecimal n2 = checkSmallItemFormMap.getBigDecimal("max_value");

					PhysicalExaminationResultFormMap physicalExaminationResultFormMap = getFormMap(PhysicalExaminationResultFormMap.class);
					physicalExaminationResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
					physicalExaminationResultFormMap.put("bit_item_id",checkSmallItemFormMap.getLong("big_item_id"));
					physicalExaminationResultFormMap.put("small_item_id",checkSmallItemFormMap.getLong("id"));
					physicalExaminationResultFormMap.put("gen_min_value",n1);
					physicalExaminationResultFormMap.put("gen_max_value",n2);

					BigDecimal n = (n2.subtract(n1));

					physicalExaminationResultFormMap.put("gen_in_value",n);
					physicalExaminationResultFormMap.put("gen_quanzhong",checkSmallItemFormMap.getBigDecimal("quanzhong"));
					//计算

					BigDecimal M = n.divide(new BigDecimal(20.0),3, RoundingMode.HALF_UP);
					physicalExaminationResultFormMap.put("in_value_score",M);
					//随机生成
					int max = physicalExaminationResultFormMap.getBigDecimal("gen_max_value").multiply(new BigDecimal(1000)).intValue();
					int min = physicalExaminationResultFormMap.getBigDecimal("gen_min_value").multiply(new BigDecimal(1000)).intValue();
					int randomNumber = (int) Math.round(Math.random()*(max-min)+min);

					BigDecimal A = new BigDecimal(randomNumber/1000d);
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

					physicalExaminationResultMapper.addEntity(physicalExaminationResultFormMap);

					Set<Long> keySet = itemMap.keySet();
					List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapListIn = null;
					if(keySet.contains(physicalExaminationResultFormMap.getLong("bit_item_id"))){
						physicalExaminationResultFormMapListIn = itemMap.get(physicalExaminationResultFormMap.getLong("bit_item_id"));
					}else{
						physicalExaminationResultFormMapListIn = new ArrayList<PhysicalExaminationResultFormMap>();
					}
					physicalExaminationResultFormMapListIn.add(physicalExaminationResultFormMap);
					itemMap.put(physicalExaminationResultFormMap.getLong("bit_item_id"),physicalExaminationResultFormMapListIn);

				}

				// 获取所有的检测大项
				List<CheckBigItemFormMap> checkBigItemFormMapList = checkBigItemMapper.findByNames(getFormMap(CheckBigItemFormMap.class));

				// 计算大项得分
				Set<Long> keySet = itemMap.keySet();
				Iterator<Long> iterator = keySet.iterator();
				List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = new ArrayList<PhysicalExaminationBigResultFormMap>();
				while (iterator.hasNext()){
					Long key = iterator.next();
					List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapListIn = itemMap.get(key);
					if(CollectionUtils.isNotEmpty(physicalExaminationResultFormMapListIn)){
						BigDecimal totalQuanzhong = new BigDecimal(0);
						BigDecimal totalScore = new BigDecimal(0);
						for(PhysicalExaminationResultFormMap item :physicalExaminationResultFormMapListIn){
							totalQuanzhong=totalQuanzhong.add(item.getBigDecimal("gen_quanzhong"));
							totalScore=totalScore.add(item.getBigDecimal("quanzhong_score"));
						}
						if(totalQuanzhong.doubleValue()>0.00d){
							BigDecimal checkScore = totalScore.divide(totalQuanzhong,3,BigDecimal.ROUND_HALF_UP);
							PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = getFormMap(PhysicalExaminationBigResultFormMap.class);
							physicalExaminationBigResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
							physicalExaminationBigResultFormMap.put("big_item_id",key);
							physicalExaminationBigResultFormMap.put("check_score",checkScore);

							for(CheckBigItemFormMap checkBigItemFormMap:checkBigItemFormMapList){
								if(checkBigItemFormMap.getLong("id").longValue()==key){
									physicalExaminationBigResultFormMap.put("gen_quanzhong",checkBigItemFormMap.getBigDecimal("quanzhong"));
									break;
								}
							}
							for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
								if(item.getDouble("pingfen_min")<=checkScore.doubleValue() && item.getDouble("pingfen_max")>=checkScore.doubleValue()){
									physicalExaminationBigResultFormMap.put("leve_id",item.getLong("id"));
									break;
								}
							}
							//配置项是否与性别有关系
							boolean isFix = true;
							for(CheckBigItemFormMap checkBigItemFormMap:checkBigItemFormMapList){
								if(checkBigItemFormMap.getLong("id").longValue() == physicalExaminationBigResultFormMap.getLong("big_item_id").longValue()){
									Integer withSex = checkBigItemFormMap.getInt("withsex");
									if(withSex == null || withSex == 0 || withSex.intValue() == customInfoFormMap.getInt("sex")){
										isFix = true;
									}else{
										isFix = false;
									}
									break;
								}

							}
							if(isFix){
								physicalExaminationBigResultMapper.addEntity(physicalExaminationBigResultFormMap);
								physicalExaminationBigResultFormMapList.add(physicalExaminationBigResultFormMap);
							}


						}
					}
				}

				//todo 保存总评
				PhysicalExaminationMainReportFormMap physicalExaminationMainReportFormMap = getFormMap(PhysicalExaminationMainReportFormMap.class);
				physicalExaminationMainReportFormMap.put("examination_record_id",recordFormMap.getLong("id"));

				//todo 计算总分
				BigDecimal totalScore = new BigDecimal(0);
				for(PhysicalExaminationBigResultFormMap item:physicalExaminationBigResultFormMapList){
					totalScore = totalScore.add(item.getBigDecimal("gen_quanzhong").multiply(item.getBigDecimal("check_score")));
				}
				physicalExaminationMainReportFormMap.put("check_total_score",totalScore.divide(new BigDecimal(physicalExaminationBigResultFormMapList.size()),3, RoundingMode.HALF_UP));

				for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
					if(item.getDouble("pingfen_min")<=physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").doubleValue() && item.getDouble("pingfen_max")>=physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").doubleValue()){
						physicalExaminationMainReportFormMap.put("level",item.getLong("id"));
						break;
					}
				}

				physicalExaminationMainReportMapper.addEntity(physicalExaminationMainReportFormMap);
			}
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
