package com.lanyuan.service.impl;

import com.lanyuan.entity.*;
import com.lanyuan.mapper.*;
import com.lanyuan.service.ICheckService;
import com.lanyuan.util.AgeCal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private CheckBigItemMapper checkBigItemMapper;


    @Inject
    private CfPingfenLeveMapper cfPingfenLeveMapper;


    @Inject
    private PhysicalExaminationMainReportMapper physicalExaminationMainReportMapper;

    @Inject
    private CfPingfenRoutMapper cfPingfenRoutMapper;




    @Inject
    private CustomBelonetoEntMapper customBelonetoEntMapper;


    @Inject
    private CustomInfoMapper customInfoMapper;


    @Autowired
    private EquipmentMapper equipmentMapper;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");




    /**
     * 记录检测项
     * @throws Exception
     */
    public void recordCheckResult(String instrumentCode,Long customerId) throws Exception {
        // 获取仪器对象
        EquipmentFormMap equipmentFormMap = equipmentMapper.findbyFrist("istmt_code",instrumentCode,EquipmentFormMap.class);
        if(null == equipmentFormMap){
            throw new Exception("无效检测!");
        }

        Long instrumentId = equipmentFormMap.getLong("id");


        // 根据用户id获取用户相关信息
        CustomBelonetoEntFormMap customBelonetoEntFormMap = new CustomBelonetoEntFormMap();
        customBelonetoEntFormMap.put("custom_id",customerId);
        customBelonetoEntFormMap.put("organization_id",equipmentFormMap.getLong("organization_id"));
        customBelonetoEntFormMap.put("isdelete",0);
        List<CustomBelonetoEntFormMap> customBelonetoEntFormMapList = customBelonetoEntMapper.findByNames(customBelonetoEntFormMap);
        if(CollectionUtils.isEmpty(customBelonetoEntFormMapList)){
            throw new Exception("无此用户!");
        }
        customBelonetoEntFormMap = customBelonetoEntFormMapList.get(0);

        CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id", String.valueOf(customerId),CustomInfoFormMap.class);
        if(null == customInfoFormMap){
            throw new Exception("无此用户!");
        }

        PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = saveCheckRecord(customBelonetoEntFormMap,instrumentId);

        //  获取所有的项目（排除切割项目关联项 ,排除性别相关项）
        List<CheckSmallItemFormMap> checkSmallItemFormMapList = getCustomerCheckSmallItemsList(customInfoFormMap);
        if(CollectionUtils.isEmpty(checkSmallItemFormMapList)){
            throw new Exception("没有配置检测项");
        }




        // 循环保存检测项
        List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = new ArrayList<PhysicalExaminationResultFormMap>();
        for(CheckSmallItemFormMap checkSmallItemFormMap:checkSmallItemFormMapList){
            PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
            physicalExaminationResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
            physicalExaminationResultFormMap.put("bit_item_id",checkSmallItemFormMap.getLong("big_item_id"));
            physicalExaminationResultFormMap.put("small_item_id",checkSmallItemFormMap.getLong("id"));
//            physicalExaminationResultMapper.addEntity(physicalExaminationResultFormMap);
            physicalExaminationResultFormMapList.add(physicalExaminationResultFormMap);
        }

        physicalExaminationResultMapper.batchSave(physicalExaminationResultFormMapList);
    }


    /**
     * 保存检测记录
     * @param customBelonetoEntFormMap
     * @param instrumentId
     * @return
     * @throws Exception
     */
    private PhysicalExaminationRecordFormMap saveCheckRecord(CustomBelonetoEntFormMap customBelonetoEntFormMap,Long instrumentId) throws Exception {
        // 保存检测记录
        PhysicalExaminationRecordFormMap recordFormMap = new PhysicalExaminationRecordFormMap();
        recordFormMap.put("custom_id",customBelonetoEntFormMap.getLong("custom_id"));
        recordFormMap.put("organization_id",customBelonetoEntFormMap.get("organization_id").toString());
        recordFormMap.put("instrument_id",instrumentId);
        recordFormMap.put("status",PhysicalExaminationRecordFormMap.STATUS_CHECKED);
        recordFormMap.put("check_time",dateFormat.format(new Date()));
        recordFormMap.put("update_time",dateFormat.format(new Date()));
        physicalExaminationRecordMapper.addEntity(recordFormMap);
        return recordFormMap;
    }


    /**
     *
     * @param customid
     * @return
     * @throws Exception
     */
    private List<CheckSmallItemFormMap> getCustomerCheckSmallItemsList(CustomInfoFormMap customInfoFormMap) throws Exception {
        CheckSmallItemFormMap checkSmallItemFormMap = new CheckSmallItemFormMap();
        checkSmallItemFormMap.put("customid",customInfoFormMap.getLong("id"));
        checkSmallItemFormMap.put("withsex",customInfoFormMap.getInt("sex"));
        //todo sql 中需要排除性别相关的项
        List<CheckSmallItemFormMap> checkSmallItemFormMapList =  checkSmallItemMapper.getAllButCustomCutedItem(checkSmallItemFormMap);
        return checkSmallItemFormMapList;
    }





    /**
     * 生成检测结果
     * @param recordFormMap
     * @throws Exception
     */
    public void genCheckResult(PhysicalExaminationRecordFormMap recordFormMap) throws Exception {

        CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id",recordFormMap.getLong("custom_id").toString(),CustomInfoFormMap.class);
        if(null == customInfoFormMap){
            throw new Exception("无效用户!");
        }

        //获取用户检测小项检测结果列表
        PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
        physicalExaminationResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
        List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = physicalExaminationResultMapper.findByNames(physicalExaminationResultFormMap);

        if(CollectionUtils.isEmpty(physicalExaminationResultFormMapList)){
            throw new Exception("还没有生成检测数据！");
        }

        List<CheckSmallItemFormMap> checkSmallItemFormMapList = getCustomerCheckSmallItemsList(customInfoFormMap);
        if(CollectionUtils.isEmpty(checkSmallItemFormMapList)){
            throw new Exception("没有配置检测小项！");
        }

        List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(new CfPingfenLeveFormMap());
        if(CollectionUtils.isEmpty(cfPingfenLeveFormMapList)){
            throw new Exception("评分等级未配置！");
        }
        genCheckSmallItemResult(customInfoFormMap, physicalExaminationResultFormMapList, checkSmallItemFormMapList, cfPingfenLeveFormMapList);


        // 获取所有的检测大项
        List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = genCheckBigItemResult(recordFormMap, cfPingfenLeveFormMapList);



        // 保存总评
        genMainResult(cfPingfenLeveFormMapList, physicalExaminationBigResultFormMapList, recordFormMap.getLong("id"));


    }


    /**
     * 生成检测项检测结果
     * @param customInfoFormMap
     * @param physicalExaminationResultFormMapList
     * @param checkSmallItemFormMapList
     * @param cfPingfenLeveFormMapList
     * @throws Exception
     */
    private void genCheckSmallItemResult(CustomInfoFormMap customInfoFormMap, List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList, List<CheckSmallItemFormMap> checkSmallItemFormMapList, List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList) throws Exception {
        // 循环检测结果，计算各项内容，并更新

        for(PhysicalExaminationResultFormMap checkSmallItemResult:physicalExaminationResultFormMapList){

            for(CheckSmallItemFormMap checkSmallItemFormMap:checkSmallItemFormMapList){
                if(checkSmallItemFormMap.getLong("id").longValue() == checkSmallItemResult.getLong("small_item_id")){
                    BigDecimal n1 = checkSmallItemFormMap.getBigDecimal("min_value");
                    BigDecimal n2 = checkSmallItemFormMap.getBigDecimal("max_value");

                    checkSmallItemResult.put("gen_min_value",n1);
                    checkSmallItemResult.put("gen_max_value",n2);

                    BigDecimal n = (n2.subtract(n1));

                    checkSmallItemResult.put("gen_in_value",n);
                    checkSmallItemResult.put("gen_quanzhong",checkSmallItemFormMap.getBigDecimal("quanzhong"));


                    BigDecimal M = n.divide(new BigDecimal(20.0),3, RoundingMode.HALF_UP);
                    checkSmallItemResult.put("in_value_score",M);
                    //随机生成
                    int max = checkSmallItemResult.getBigDecimal("gen_max_value").multiply(new BigDecimal(1000)).intValue();
                    int min = checkSmallItemResult.getBigDecimal("gen_min_value").multiply(new BigDecimal(1000)).intValue();
                    int randomNumber = (int) Math.round(Math.random()*(max-min)+min);

                    BigDecimal A = new BigDecimal(randomNumber/1000d);
                    checkSmallItemResult.put("check_value",A);

                    BigDecimal B = new BigDecimal(100).subtract((A.subtract(n1).divide(M,3,BigDecimal.ROUND_HALF_UP))) ;

                    //  B = 100-(A?-n1/M)    100-(102.076-102.001)/0.129

                    //A ? = (100-B)*M+n1


                    checkSmallItemResult.put("item_score",B);

                    //根据计算结果判断\
                    for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
                        if(item.getDouble("pingfen_min")<=B.doubleValue() && item.getDouble("pingfen_max")>=B.doubleValue()){
                            checkSmallItemResult.put("orgin_leve_id",item.getLong("id"));
                            break;
                        }
                    }


                    // 查找小项等级评分调整概率
                    CfPingfenRoutFormMap cfPingfenRoutFormMap = new CfPingfenRoutFormMap();
                    // 计算用户年龄
                    Date birthday = customInfoFormMap.getDate("birthday");
                    cfPingfenRoutFormMap.put("age", AgeCal.getAge(birthday));
                    cfPingfenRoutFormMap.put("small_id",checkSmallItemFormMap.getLong("id"));
                    cfPingfenRoutFormMap.put("orgin_pingfen_id",checkSmallItemResult.getLong("orgin_leve_id"));
                    List<CfPingfenRoutFormMap> cfPingfenRoutFormMapList = cfPingfenRoutMapper.findSmallItemRout(cfPingfenRoutFormMap);
                    if(CollectionUtils.isNotEmpty(cfPingfenRoutFormMapList)){
                        cfPingfenRoutFormMap = cfPingfenRoutFormMapList.get(0);

                        int maxTz = 0;
                        int minTz = 1000;
                        int randomNumberTz = (int) Math.round(Math.random()*(maxTz-minTz)+minTz);

                        if(randomNumberTz >=cfPingfenRoutFormMap.getDouble("rout_min")*1000 && randomNumberTz<=cfPingfenRoutFormMap.getDouble("rout_max")*1000){
                            checkSmallItemResult.put("tzed_leve_id",cfPingfenRoutFormMap.getLong("tz_pingfen_id"));
                            //todo 随机生成得分等级范围内的得分值  B
                            CfPingfenLeveFormMap cfPingfenLeveFormMap = null;
                            for(CfPingfenLeveFormMap temp:cfPingfenLeveFormMapList){
                                if(temp.getLong("id").longValue() == cfPingfenRoutFormMap.getLong("tz_pingfen_id")){
                                    cfPingfenLeveFormMap = temp;
                                    break;
                                }
                            }

                            //随机生成
                            int tzmax = cfPingfenLeveFormMap.getDouble("pingfen_min").intValue()*1000;
                            int tzmin = cfPingfenLeveFormMap.getDouble("pingfen_max").intValue()*1000;
                            int tzrandomNumber = (int) Math.round(Math.random()*(tzmax-tzmin)+tzmin);

                            BigDecimal tzB = new BigDecimal(tzrandomNumber/1000d);

                            //A ? = (100-B)*M+n1
                            BigDecimal tzA = new BigDecimal(100).subtract(tzB).multiply(M).add(n1);



                            checkSmallItemResult.put("check_value",tzA);


                            checkSmallItemResult.put("item_score",tzB);


                        }
                    }

                    //根据调整概率，生成新的功能等级

                    BigDecimal Sco = checkSmallItemResult.getBigDecimal("item_score").multiply(checkSmallItemFormMap.getBigDecimal("quanzhong"));
                    checkSmallItemResult.put("quanzhong_score",Sco);

                    physicalExaminationResultMapper.editEntity(checkSmallItemResult);
                    break;
                }

            }
        }
    }


    /**
     * 生成检测大项检测结果
     * @param recordFormMap
     * @param cfPingfenLeveFormMapList
     * @return
     * @throws Exception
     */
    private List<PhysicalExaminationBigResultFormMap> genCheckBigItemResult(PhysicalExaminationRecordFormMap recordFormMap, List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList) throws Exception {
        List<CheckBigItemFormMap> checkBigItemFormMapList = checkBigItemMapper.findByNames(new CheckBigItemFormMap());

        // 循环大项，查询所有的检测结果，计算结果保存到用户检测大项记录
        if(CollectionUtils.isEmpty(checkBigItemFormMapList)){
            throw new Exception("没有配置检测大项");
        }
        List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = new ArrayList<PhysicalExaminationBigResultFormMap>();
        for(CheckBigItemFormMap checkBigItemFormMap:checkBigItemFormMapList){
            PhysicalExaminationResultFormMap physicalExaminationResultFormMapTmp = new PhysicalExaminationResultFormMap();
            physicalExaminationResultFormMapTmp.put("examination_record_id",recordFormMap.getLong("id"));
            physicalExaminationResultFormMapTmp.put("bit_item_id",checkBigItemFormMap.getLong("id"));
            List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapListIn = physicalExaminationResultMapper.findByNames(physicalExaminationResultFormMapTmp);
            if(CollectionUtils.isEmpty(physicalExaminationResultFormMapListIn)){
                continue;
            }

            BigDecimal totalQuanzhong = new BigDecimal(0);
            BigDecimal totalScore = new BigDecimal(0);
            for(PhysicalExaminationResultFormMap item :physicalExaminationResultFormMapListIn){
                totalQuanzhong=totalQuanzhong.add(item.getBigDecimal("gen_quanzhong"));
                totalScore=totalScore.add(item.getBigDecimal("quanzhong_score"));
            }
            if(totalQuanzhong.doubleValue()>0.00d){
                BigDecimal checkScore = totalScore.divide(totalQuanzhong,3,BigDecimal.ROUND_HALF_UP);
                PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = new PhysicalExaminationBigResultFormMap();
                physicalExaminationBigResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
                physicalExaminationBigResultFormMap.put("big_item_id",checkBigItemFormMap.getLong("id"));
                physicalExaminationBigResultFormMap.put("check_score",checkScore);

                physicalExaminationBigResultFormMap.put("gen_quanzhong",checkBigItemFormMap.getBigDecimal("quanzhong"));

                for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
                    if(item.getDouble("pingfen_min")<=checkScore.doubleValue() && item.getDouble("pingfen_max")>=checkScore.doubleValue()){
                        physicalExaminationBigResultFormMap.put("leve_id",item.getLong("id"));
                        break;
                    }
                }
                physicalExaminationBigResultMapper.addEntity(physicalExaminationBigResultFormMap);
                physicalExaminationBigResultFormMapList.add(physicalExaminationBigResultFormMap);


            }
        }
        return physicalExaminationBigResultFormMapList;
    }



    /**
     * 生成总评
     * @param cfPingfenLeveFormMapList
     * @param physicalExaminationBigResultFormMapList
     * @param recordid
     * @throws Exception
     */
    private void genMainResult(List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList, List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList, Long recordid) throws Exception {
        PhysicalExaminationMainReportFormMap physicalExaminationMainReportFormMap = new PhysicalExaminationMainReportFormMap();
        physicalExaminationMainReportFormMap.put("examination_record_id",recordid);
        // 计算总分
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
    }


    @Autowired
    private SickRiskItemMapper sickRiskItemMapper;

    @Autowired
    private SickRiskMapper sickRiskMapper;

    @Autowired
    private PhysicalExaminationResultMapper physicalExaminationResultMapper;

    @Autowired
    private PhysicalExaminationBigResultMapper physicalExaminationBigResultMapper;


    @Autowired
    private PhysicalExaminationSickRiskResultMapper physicalExaminationSickRiskResultMapper;

    @Autowired
    private PhysicalExaminationSickRiskMainResultMapper physicalExaminationSickRiskMainResultMapper;


    @Autowired
    private SickRiskLeveMapper sickRiskLeveMapper;


    public void genSickRiskResult(PhysicalExaminationRecordFormMap recordFormMap) throws Exception {
        //todo 获取所有的疾病风险
        List<SickRiskItemFormMap> sickRiskItemFormMapList =  sickRiskItemMapper.findEnterprisePage(new SickRiskItemFormMap());
        if(CollectionUtils.isEmpty(sickRiskItemFormMapList)){
            throw new Exception("没有配置疾病项");
        }
        //todo 查询当前用户
        CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id",recordFormMap.getLong("custom_id").toString(),CustomInfoFormMap.class);
        if(null == customInfoFormMap){
            throw new Exception("无效用户!");
        }


        //todo 查询风险等级
        List<SickRiskLeveFormMap> sickRiskLeveFormMapList = sickRiskLeveMapper.findByNames(new SickRiskLeveFormMap());
        if(CollectionUtils.isEmpty(sickRiskLeveFormMapList)){
            throw new Exception("风险等级没有配置");
        }



        for(SickRiskItemFormMap sickRiskItemFormMap:sickRiskItemFormMapList){
            //todo 查询当前疾病项关联的所有配置
            SickRiskFormMap sickRiskFormMap = new SickRiskFormMap();
            sickRiskFormMap.put("sick_risk_id",sickRiskItemFormMap.getLong("id"));
            List<SickRiskFormMap> sickRiskFormMapList = sickRiskMapper.findByNames(sickRiskFormMap);
            if(CollectionUtils.isEmpty(sickRiskFormMapList)){
                continue;
            }
            StringBuffer smallItemIds =new StringBuffer("");
            StringBuffer bigItemIds =new StringBuffer("");
            for(int i=0;i<sickRiskFormMapList.size();i++){
                SickRiskFormMap sickItem =sickRiskFormMapList.get(i);
                if(sickItem!=null){
                    if(sickItem.getInt("check_item_type")==1){//大项
                        if(i==sickRiskFormMapList.size()-1){
                            bigItemIds.append(sickItem.getLong("check_item_id"));
                        }else{
                            bigItemIds.append(sickItem.getLong("check_item_id")).append(",");
                        }
                    }else{
                        if(i==sickRiskFormMapList.size()-1){
                            smallItemIds.append(sickItem.getLong("check_item_id"));
                        }else{
                            smallItemIds.append(sickItem.getLong("check_item_id")).append(",");
                        }
                    }
                }
            }

            //todo 获取当前用户的检测结果记录

            List<PhysicalExaminationSickRiskResultFormMap> physicalExaminationSickRiskResultFormMapList = new ArrayList<PhysicalExaminationSickRiskResultFormMap>();

            PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
            if(StringUtils.isNotBlank(smallItemIds.toString())){
                physicalExaminationResultFormMap.put("where","where examination_record_id="+recordFormMap.getLong("id")+" and  small_item_id in ("+smallItemIds.toString()+") order by id desc");
                List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = physicalExaminationResultMapper.findByWhere(physicalExaminationResultFormMap);
                if(CollectionUtils.isNotEmpty(physicalExaminationResultFormMapList)){
                    for(PhysicalExaminationResultFormMap item:physicalExaminationResultFormMapList){
                        //todo 计算检测小项的风险值
                        Map<String,BigDecimal> sickReskMap = this.calSickRiskVal(item.getBigDecimal("quanzhong_score"),item.getLong("small_item_id"),sickRiskFormMapList);
                        PhysicalExaminationSickRiskResultFormMap physicalExaminationSickRiskResultFormMap = new PhysicalExaminationSickRiskResultFormMap();
                        physicalExaminationSickRiskResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
                        physicalExaminationSickRiskResultFormMap.put("check_item_id",item.getLong("big_item_id"));
                        physicalExaminationSickRiskResultFormMap.put("check_item_type",2);
                        physicalExaminationSickRiskResultFormMap.put("sick_risk_item_id",sickRiskItemFormMap.getLong("sick_risk_id"));
                        physicalExaminationSickRiskResultFormMap.put("risk_rout",(BigDecimal)sickReskMap.get("sickRiskValue"));
                        physicalExaminationSickRiskResultFormMap.put("score",(BigDecimal)sickReskMap.get("rout"));
                        physicalExaminationSickRiskResultMapper.addEntity(physicalExaminationSickRiskResultFormMap);
                        physicalExaminationSickRiskResultFormMapList.add(physicalExaminationSickRiskResultFormMap);
                    }
                }
            }


            if(StringUtils.isNotBlank(bigItemIds.toString())){
                PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = new PhysicalExaminationBigResultFormMap();
                physicalExaminationBigResultFormMap.put("where","where examination_record_id="+recordFormMap.getLong("id")+" and  big_item_id in ("+bigItemIds.toString()+") order by id desc");
                List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findByWhere(physicalExaminationBigResultFormMap);
                if(CollectionUtils.isNotEmpty(physicalExaminationBigResultFormMapList)){
                    for(PhysicalExaminationBigResultFormMap item:physicalExaminationBigResultFormMapList){
                        //todo 计算检测大项的风险值
                        Map<String,BigDecimal> sickReskMap = this.calSickRiskVal(item.getBigDecimal("check_score"),item.getLong("big_item_id"),sickRiskFormMapList);
                        PhysicalExaminationSickRiskResultFormMap physicalExaminationSickRiskResultFormMap = new PhysicalExaminationSickRiskResultFormMap();
                        physicalExaminationSickRiskResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
                        physicalExaminationSickRiskResultFormMap.put("check_item_id",item.getLong("big_item_id"));
                        physicalExaminationSickRiskResultFormMap.put("check_item_type",1);
                        physicalExaminationSickRiskResultFormMap.put("sick_risk_item_id",sickRiskItemFormMap.getLong("id"));
                        physicalExaminationSickRiskResultFormMap.put("risk_rout",(BigDecimal)sickReskMap.get("sickRiskValue"));
                        physicalExaminationSickRiskResultFormMap.put("score",(BigDecimal)sickReskMap.get("rout"));
                        physicalExaminationSickRiskResultMapper.addEntity(physicalExaminationSickRiskResultFormMap);
                        physicalExaminationSickRiskResultFormMapList.add(physicalExaminationSickRiskResultFormMap);
                    }

                }
            }


            if(CollectionUtils.isEmpty(physicalExaminationSickRiskResultFormMapList)){
                continue;
            }



            //todo 计算检测项疾病风险概率
            PhysicalExaminationSickRiskMainResultFormMap physicalExaminationSickRiskMainResultFormMap = this.calSickRiskScore(physicalExaminationSickRiskResultFormMapList);
            //todo 计算bmi疾病风险率
            physicalExaminationSickRiskMainResultFormMap =this.calSickRiskRoutByBMI(physicalExaminationSickRiskMainResultFormMap,customInfoFormMap,sickRiskItemFormMap.getLong("id"));
            //todo 计算年龄疾病风险率
            physicalExaminationSickRiskMainResultFormMap =this.calSickRiskRoutByAge(physicalExaminationSickRiskMainResultFormMap,customInfoFormMap,sickRiskItemFormMap.getLong("id"));

            physicalExaminationSickRiskMainResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
            physicalExaminationSickRiskMainResultFormMap.put("sick_risk_item_id",sickRiskItemFormMap.getLong("id"));

            BigDecimal bmi_risk_rout = physicalExaminationSickRiskMainResultFormMap.getBigDecimal("bmi_risk_rout")==null?new BigDecimal(0):physicalExaminationSickRiskMainResultFormMap.getBigDecimal("bmi_risk_rout");
            BigDecimal age_risk_rout = physicalExaminationSickRiskMainResultFormMap.getBigDecimal("age_risk_rout")==null?new BigDecimal(0):physicalExaminationSickRiskMainResultFormMap.getBigDecimal("age_risk_rout");
            BigDecimal check_item_rout = physicalExaminationSickRiskMainResultFormMap.getBigDecimal("check_item_rout")==null?new BigDecimal(0):physicalExaminationSickRiskMainResultFormMap.getBigDecimal("check_item_rout");
            BigDecimal total_rout = bmi_risk_rout.add(age_risk_rout).add(check_item_rout);


            physicalExaminationSickRiskMainResultFormMap.put("total_rout",total_rout);


            //todo 生成风险等级
            for(SickRiskLeveFormMap sickRiskLeveFormMap:sickRiskLeveFormMapList){
                if(sickRiskLeveFormMap.getBigDecimal("rout_min").doubleValue()<physicalExaminationSickRiskMainResultFormMap.getBigDecimal("total_rout").doubleValue() && sickRiskLeveFormMap.getBigDecimal("rout_max").doubleValue()>=physicalExaminationSickRiskMainResultFormMap.getBigDecimal("total_rout").doubleValue()){
                    physicalExaminationSickRiskMainResultFormMap.put("risk_leve",sickRiskLeveFormMap.getLong("id"));
                    break;
                }
            }

            physicalExaminationSickRiskMainResultMapper.addEntity(physicalExaminationSickRiskMainResultFormMap);


        }

    }

    @Autowired
    private AgeSickRiskRoutMapper ageSickRiskRoutMapper;

    private SimpleDateFormat ageFormate = new SimpleDateFormat("yyyy-MM-dd");
    private PhysicalExaminationSickRiskMainResultFormMap calSickRiskRoutByAge(PhysicalExaminationSickRiskMainResultFormMap physicalExaminationSickRiskMainResultFormMap, CustomInfoFormMap customInfoFormMap,Long sickRiskId)throws Exception{
        String birthday = customInfoFormMap.get("birthday").toString();
        Integer age = AgeCal.getAge(ageFormate.parse(birthday));

        AgeSickRiskRoutFormMap ageSickRiskRoutFormMap = new AgeSickRiskRoutFormMap();
        ageSickRiskRoutFormMap.put("sick_risk_id",sickRiskId);
        ageSickRiskRoutFormMap.put("age",age);
        List<AgeSickRiskRoutFormMap> ageSickRiskRoutFormMapList = ageSickRiskRoutMapper.findFixConfig(ageSickRiskRoutFormMap);
        if(CollectionUtils.isNotEmpty(ageSickRiskRoutFormMapList)){
            ageSickRiskRoutFormMap = ageSickRiskRoutFormMapList.get(0);
            physicalExaminationSickRiskMainResultFormMap.put("age_risk_rout",ageSickRiskRoutFormMap.getBigDecimal("rout"));
        }
        return physicalExaminationSickRiskMainResultFormMap;
    }

    @Autowired
    private BmiSickRiskRoutMapper bmiSickRiskRoutMapper;

    private PhysicalExaminationSickRiskMainResultFormMap calSickRiskRoutByBMI(PhysicalExaminationSickRiskMainResultFormMap physicalExaminationSickRiskMainResultFormMap, CustomInfoFormMap customInfoFormMap,Long sickRiskId) throws Exception{
        Double weight =customInfoFormMap.getDouble("weight");
        Double height =Double.parseDouble(customInfoFormMap.get("body_height").toString());
        Double bmi = weight/((height/100)*(height/100));

        BmiSickRiskRoutFormMap bmiSickRiskRoutFormMap = new BmiSickRiskRoutFormMap();
        bmiSickRiskRoutFormMap.put("sick_risk_id",sickRiskId);
        bmiSickRiskRoutFormMap.put("bmi",new BigDecimal(bmi).setScale(3,BigDecimal.ROUND_HALF_UP));
        List<BmiSickRiskRoutFormMap> bmiSickRiskRoutFormMapList = bmiSickRiskRoutMapper.findFixConfig(bmiSickRiskRoutFormMap);
        if(CollectionUtils.isNotEmpty(bmiSickRiskRoutFormMapList)){
            bmiSickRiskRoutFormMap = bmiSickRiskRoutFormMapList.get(0);
            physicalExaminationSickRiskMainResultFormMap.put("bmi_risk_rout",bmiSickRiskRoutFormMap.getBigDecimal("rout"));
        }
        return physicalExaminationSickRiskMainResultFormMap;
    }


    /**
     *
     * 计算疾病风险得分
     * 计算公式   FM=60%+（80-YM）*2.5*100%
     * @param physicalExaminationSickRiskResultFormMapList
     * @return
     * @throws Exception
     */
    private PhysicalExaminationSickRiskMainResultFormMap calSickRiskScore(List<PhysicalExaminationSickRiskResultFormMap> physicalExaminationSickRiskResultFormMapList) throws Exception{
        if(CollectionUtils.isEmpty(physicalExaminationSickRiskResultFormMapList)){
            throw new Exception("数据异常！");
        }
        //todo 计算风险得分
        BigDecimal sumSickRiskValue = new BigDecimal(0);
        for(PhysicalExaminationSickRiskResultFormMap item:physicalExaminationSickRiskResultFormMapList){
            BigDecimal sickRiskValue = item.getBigDecimal("score");
            sumSickRiskValue = sumSickRiskValue.add(sickRiskValue);
        }
        BigDecimal score = new BigDecimal(0.6).add(new BigDecimal(80).subtract(sumSickRiskValue).multiply(new BigDecimal(2.5)).multiply(new BigDecimal(1)));
        PhysicalExaminationSickRiskMainResultFormMap physicalExaminationSickRiskMainResultFormMap = new PhysicalExaminationSickRiskMainResultFormMap();
        physicalExaminationSickRiskMainResultFormMap.put("risk_value",sumSickRiskValue);
        physicalExaminationSickRiskMainResultFormMap.put("check_item_rout",score);
        return physicalExaminationSickRiskMainResultFormMap;
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
