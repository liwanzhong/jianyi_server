package com.lanyuan.service.impl;

import com.lanyuan.entity.*;
import com.lanyuan.mapper.*;
import com.lanyuan.service.ICheckService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/26.
 */
@Service
public class CheckServiceImpl implements ICheckService {

    @Inject
    private PhysicalExaminationRecordMapper physicalExaminationRecordMapper;


    @Inject
    private CheckSmallItemMapper checkSmallItemMapper;


    @Inject
    private CustomInfoMapper customInfoMapper;


    @Inject
    private CustomBelonetoEntMapper customBelonetoEntMapper;





    @Inject
    private CheckBigItemMapper checkBigItemMapper;







    @Inject
    private CfPingfenLeveMapper cfPingfenLeveMapper;


    @Inject
    private PhysicalExaminationMainReportMapper physicalExaminationMainReportMapper;


    @Inject
    private PhysicalExaminationBigResultMapper physicalExaminationBigResultMapper;


    @Inject
    private CfPingfenRoutMapper cfPingfenRoutMapper;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PhysicalExaminationRecordFormMap saveCheckRecord(PhysicalExaminationRecordFormMap recordFormMap,CustomBelonetoEntFormMap customBelonetoEntFormMap,String instrumentId) throws Exception {
        // 保存检测记录
        recordFormMap.put("custom_id",customBelonetoEntFormMap.getLong("custom_id"));
        recordFormMap.put("organization_id",customBelonetoEntFormMap.get("organization_id").toString());
        recordFormMap.put("instrument_id",instrumentId);
        recordFormMap.put("status",PhysicalExaminationRecordFormMap.STATUS_CHECKED);
        recordFormMap.put("check_time",dateFormat.format(new Date()));
        recordFormMap.put("update_time",dateFormat.format(new Date()));
        physicalExaminationRecordMapper.addEntity(recordFormMap);
        return recordFormMap;
    }

    public List<CheckSmallItemFormMap> getCustomerCheckSmallItemsList(String customid) throws Exception {
        List<CheckSmallItemFormMap> checkSmallItemFormMapList =  checkSmallItemMapper.getAllButCustomCutedItem(customid);
        return checkSmallItemFormMapList;
    }

    public void recordCheckResult(CustomBelonetoEntFormMap customBelonetoEntFormMap,PhysicalExaminationRecordFormMap recordFormMap) throws Exception {
        // 获取所有的项目（排除切割项目关联项 ）
        /*List<CheckSmallItemFormMap> checkSmallItemFormMapList = getCustomerCheckSmallItemsList(customBelonetoEntFormMap.get("custom_id").toString());
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


                // 查找小项等级评分调整概率
                CfPingfenRoutFormMap cfPingfenRoutFormMap = getFormMap(CfPingfenRoutFormMap.class);
                // 计算用户年龄
                Date birthday = customInfoFormMap.getDate("birthday");
                cfPingfenRoutFormMap.put("age", AgeCal.getAge(birthday));
                cfPingfenRoutFormMap.put("small_id",checkSmallItemFormMap.getLong("id"));
                cfPingfenRoutFormMap.put("orgin_pingfen_id",physicalExaminationResultFormMap.getLong("orgin_leve_id"));
                List<CfPingfenRoutFormMap> cfPingfenRoutFormMapList = cfPingfenRoutMapper.findSmallItemRout(cfPingfenRoutFormMap);
                if(CollectionUtils.isNotEmpty(cfPingfenRoutFormMapList)){
                    cfPingfenRoutFormMap = cfPingfenRoutFormMapList.get(0);

                    int maxTz = 0;
                    int minTz = 1000;
                    int randomNumberTz = (int) Math.round(Math.random()*(maxTz-minTz)+minTz);

                    if(randomNumberTz >=cfPingfenRoutFormMap.getDouble("rout_min")*1000 && randomNumberTz<=cfPingfenRoutFormMap.getDouble("rout_max")*1000){
                        physicalExaminationResultFormMap.put("tzed_leve_id",cfPingfenRoutFormMap.getLong("tz_pingfen_id"));
                        //todo 反向设置调整后检测值 （需要反向计算，然后设置这个值）先拿到小项得分，然后根据公式反向出来一个随机的值

                    }
                }

                //根据调整概率，生成新的功能等级

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
            BigDecimal totalQuanzhong = new BigDecimal(0);
            for(PhysicalExaminationBigResultFormMap item:physicalExaminationBigResultFormMapList){
                totalScore = totalScore.add(item.getBigDecimal("check_score"));
                totalQuanzhong = totalQuanzhong.add(item.getBigDecimal("gen_quanzhong"));
            }
            physicalExaminationMainReportFormMap.put("check_total_score",totalScore.divide(totalQuanzhong,3, RoundingMode.HALF_UP));

            for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
                if(item.getDouble("pingfen_min")<=physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").doubleValue() && item.getDouble("pingfen_max")>=physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").doubleValue()){
                    physicalExaminationMainReportFormMap.put("level",item.getLong("id"));
                    break;
                }
            }

            physicalExaminationMainReportMapper.addEntity(physicalExaminationMainReportFormMap);
        }*/

        return;
    }

    @Autowired
    private SickRiskItemMapper sickRiskItemMapper;

    @Autowired
    private SickRiskMapper sickRiskMapper;

    @Autowired
    private PhysicalExaminationResultMapper physicalExaminationResultMapper;

    @Autowired
    private PhysicalExaminationBigResultMapper physicalExaminationBigResultMapper;


    public void genSickRiskResult(PhysicalExaminationRecordFormMap recordFormMap) throws Exception {
        //todo 获取所有的疾病风险
        List<SickRiskItemFormMap> sickRiskItemFormMapList =  sickRiskItemMapper.findEnterprisePage(new SickRiskItemFormMap());
        if(CollectionUtils.isEmpty(sickRiskItemFormMapList)){
            throw new Exception("没有配置疾病项");
        }

        for(SickRiskItemFormMap sickRiskItemFormMap:sickRiskItemFormMapList){
            //todo 查询当前疾病项关联的所有配置
            SickRiskFormMap sickRiskFormMap = new SickRiskFormMap();
            sickRiskFormMap.put("sick_risk_id",sickRiskItemFormMap.getLong("id"));
            List<SickRiskFormMap> sickRiskFormMapList = sickRiskMapper.findByNames(sickRiskFormMap);
            if(CollectionUtils.isEmpty(sickRiskFormMapList)){
                continue;
            }
            StringBuffer smallItemIds =new StringBuffer("(");
            StringBuffer bigItemIds =new StringBuffer("(");
            for(int i=0;i<sickRiskFormMapList.size();i++){
                SickRiskFormMap sickItem =sickRiskFormMapList.get(i);
                if(sickItem!=null){
                    if(sickItem.getInt("check_item_type")==1){//大项
                        if(i==sickRiskFormMapList.size()-1){
                            bigItemIds.append(sickItem.getLong("check_item_id")).append(")");
                        }else{
                            bigItemIds.append(sickItem.getLong("check_item_id")).append(",");
                        }
                    }else{
                        if(i==sickRiskFormMapList.size()-1){
                            smallItemIds.append(sickItem.getLong("check_item_id")).append(")");
                        }else{
                            smallItemIds.append(sickItem.getLong("check_item_id")).append(",");
                        }
                    }
                }
            }

            //todo 获取当前用户的检测结果记录
            PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
            physicalExaminationResultFormMap.put("where","examination_record_id="+recordFormMap.getLong("id")+" and  small_item_id in "+smallItemIds.toString()+" order by id desc");
            List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = physicalExaminationResultMapper.findByWhere(physicalExaminationResultFormMap);
            if(CollectionUtils.isNotEmpty(physicalExaminationResultFormMapList)){
                for(PhysicalExaminationResultFormMap item:physicalExaminationResultFormMapList){
                    //todo 计算检测小项的风险值
                    Map<String,BigDecimal> sickReskMap = this.calSickRiskVal(item.getBigDecimal("quanzhong_score"),item.getLong("small_item_id"),sickRiskFormMapList);

                    //todo 保存检测风险值
                }
            }

            PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = new PhysicalExaminationBigResultFormMap();
            physicalExaminationBigResultFormMap.put("where","examination_record_id="+recordFormMap.getLong("id")+" and  big_item_id in "+bigItemIds.toString()+" order by id desc");
            List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findByWhere(physicalExaminationBigResultFormMap);
            if(CollectionUtils.isNotEmpty(physicalExaminationBigResultFormMapList)){
                for(PhysicalExaminationBigResultFormMap item:physicalExaminationBigResultFormMapList){
                    //todo 计算检测大项的风险值
                    Map<String,BigDecimal> sickReskMap = this.calSickRiskVal(item.getBigDecimal("check_score"),item.getLong("big_item_id"),sickRiskFormMapList);

                    //todo 保存检测风险值
                }

            }

            //todo 获取当前疾病

            //todo 根据大小项的风险值，计算疾病风险得分


        }







    }


    /**
     * 根据疾病风险系数和权重得分计算出疾病风险值
     * @param quanzhong_score
     * @param sickRiskFormMapList
     * @return
     */
    private Map<String,BigDecimal> calSickRiskVal(BigDecimal quanzhong_score, Long checkItemid, List<SickRiskFormMap> sickRiskFormMapList) {
        BigDecimal sickRiskValue= null;
        BigDecimal rout = null;
        for(SickRiskFormMap sickRiskFormMap:sickRiskFormMapList){
            if(sickRiskFormMap.getLong("check_item_id").longValue()==checkItemid){
                rout=sickRiskFormMap.getBigDecimal("risk_rout");
                sickRiskValue = quanzhong_score.multiply(rout);
            }
        }
        Map<String,BigDecimal> resultMap = new HashMap<String, BigDecimal>();
        resultMap.put("sickRiskValue",sickRiskValue);
        resultMap.put("rout",rout);
        return resultMap;
    }
}
