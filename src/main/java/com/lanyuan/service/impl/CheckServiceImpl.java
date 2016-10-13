package com.lanyuan.service.impl;

import com.lanyuan.entity.*;
import com.lanyuan.mapper.*;
import com.lanyuan.service.ICheckService;
import com.lanyuan.util.AgeCal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

    private static Logger logger = Logger.getLogger(CheckServiceImpl.class);








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


        //todo 过滤生成报告的规则（如果在一定时间内有做过体检的，直接拷贝上一份报告的数据即可）
        // 获取最后一次检测记录
        PhysicalExaminationRecordFormMap lastRecordMap = new PhysicalExaminationRecordFormMap();
        lastRecordMap.put("custom_id",physicalExaminationRecordFormMap.getLong("custom_id"));
        lastRecordMap.put("exclude_id",physicalExaminationRecordFormMap.getLong("id"));
        lastRecordMap = physicalExaminationRecordMapper.findLastTeastRecord(lastRecordMap);

        int resultCount = 0;
        if(lastRecordMap!=null&&lastRecordMap.get("id")!=null){
            resultCount = physicalExaminationResultMapper.resultSizeByRecordid(lastRecordMap.getLong("id"));
        }
        if(lastRecordMap!=null && resultCount >0){
            getCheckSmallItemResultByHistory(lastRecordMap.getLong("id"),physicalExaminationRecordFormMap.getLong("id"));
        }else {
//            genCheckSmallItemResult(customInfoFormMap, physicalExaminationRecordFormMap );
            genCheckSmallItemResult_new(customInfoFormMap, physicalExaminationRecordFormMap );
        }

        final ICheckService checkService = this;
        final PhysicalExaminationRecordFormMap item = physicalExaminationRecordFormMap;
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(2000);
                    //todo 生成检测数据
                    checkService.deleteGenedData(item);
                    checkService.genCheckResult(item);
                    checkService.genSickRiskResult(item);
                    item.put("status",2);
                    item.put("update_time",dateFormat.format(new Date()));
                    physicalExaminationRecordMapper.editEntity(item);

                }catch (Exception ex){
                    logger.error(ex.getMessage());
                    ex.printStackTrace();
                }


            }
        }).start();
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
        checkSmallItemFormMap.put("big_item_id",customInfoFormMap.getLong("big_item_id"));
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
        // 获取所有的检测大项
        List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = genCheckBigItemResult(recordFormMap, cfPingfenLeveFormMapList);

        // 保存总评
        genMainResult(cfPingfenLeveFormMapList, physicalExaminationBigResultFormMapList, recordFormMap.getLong("id"));


    }


    /**
     * 根据历史数据生成报告
     */
    private void getCheckSmallItemResultByHistory(Long sourceRecordId,Long distRecordId)throws Exception {

        List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(new CfPingfenLeveFormMap());
        if(CollectionUtils.isEmpty(cfPingfenLeveFormMapList)){
            throw new Exception("评分等级未配置！");
        }

        //在原来的基础上复制一份作为新的检测数据
        PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
        physicalExaminationResultFormMap.put("sourceRecordId",sourceRecordId);
        physicalExaminationResultFormMap.put("distRecordId",distRecordId);
        physicalExaminationResultMapper.insertByHistory(physicalExaminationResultFormMap);

        //获取新的数据，在此基础上面做调整（得分，检测值）
        physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
        physicalExaminationResultFormMap.put("examination_record_id",distRecordId);
        List<PhysicalExaminationResultFormMap> sourcePhysicalExaminationResultFormMapList = physicalExaminationResultMapper.findByNames(physicalExaminationResultFormMap);
        //todo 遍历上次体检的结果，在结果上面加上（-3  --- 3 随机数值）
        if(CollectionUtils.isNotEmpty(sourcePhysicalExaminationResultFormMapList)){
//            List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = new ArrayList<PhysicalExaminationResultFormMap>();
            for(PhysicalExaminationResultFormMap resultFormMap : sourcePhysicalExaminationResultFormMapList){
                int max = 3;
                int min = 0;
                int chosenRandom = Math.round(Math.random())%2==0?1:-1;
                int randomNumber = (int) Math.round(Math.random()*(max-min)+min)*chosenRandom;
                resultFormMap.put("bmiorage",3);
                resultFormMap.put("bmiorage_leve_change",resultFormMap.get("orgin_leve_id"));
                resultFormMap.put("check_value_tzbef",resultFormMap.get("check_value"));
                resultFormMap.put("item_score_tzbef",resultFormMap.get("item_score"));

                if(resultFormMap.getBigDecimal("item_score")==null || resultFormMap.getBigDecimal("item_score").doubleValue()<=0){
                    resultFormMap.put("item_score", new BigDecimal(Math.round(Math.random()*(95-60)+60)));
                }else{
                    if((resultFormMap.getBigDecimal("item_score").doubleValue()+randomNumber)>95){
                        resultFormMap.put("item_score", new BigDecimal(94.23));
                    }else if ((resultFormMap.getBigDecimal("item_score").doubleValue()+randomNumber)<60){
                        resultFormMap.put("item_score", new BigDecimal(60.23));
                    }else{
                        resultFormMap.put("item_score", resultFormMap.getBigDecimal("item_score").add(new BigDecimal(randomNumber)));
                    }
                }

                BigDecimal tzA = new BigDecimal(100).subtract(resultFormMap.getBigDecimal("item_score")).multiply(resultFormMap.getBigDecimal("in_value_score")).add(resultFormMap.getBigDecimal("gen_min_value"));
                resultFormMap.put("check_value", tzA);

                //设置等级
                for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
                    if(item.getDouble("pingfen_min")<=resultFormMap.getBigDecimal("item_score").doubleValue() && item.getDouble("pingfen_max")>=resultFormMap.getBigDecimal("item_score").doubleValue()){
                        resultFormMap.put("tzed_leve_id",item.getLong("id"));
                        resultFormMap.put("orgin_leve_id",item.getLong("id"));
                        break;
                    }
                }
//                physicalExaminationResultFormMapList.add(resultFormMap);
                physicalExaminationResultMapper.editEntity(resultFormMap);


            }


        }

    }





    @Inject
    private BmiCheckItemConfigMapper bmiCheckItemConfigMapper;

    @Autowired
    private CheckItemReleationConfigMapper checkItemReleationConfigMapper;


    @Autowired
    private CheckItemDegrLeveZhanbiMapper checkItemDegrLeveZhanbiMapper;




    private void genCheckSmallItemResult_new(CustomInfoFormMap customInfoFormMap,  PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap) throws Exception {
        List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(new CfPingfenLeveFormMap());
        if(CollectionUtils.isEmpty(cfPingfenLeveFormMapList)){
            throw new Exception("评分等级未配置！");
        }
        //体重
        Double weight =customInfoFormMap.getDouble("weight");
        //身高
        Double height =Double.parseDouble(customInfoFormMap.get("body_height").toString());
        //bmi
        Double bmi = weight/((height/100)*(height/100));
        //年龄
        Date birthday = customInfoFormMap.getDate("birthday");
        int age = AgeCal.getAge(birthday);

        //todo 获取检测大项
        List<CheckBigItemFormMap> checkBigItemFormMapList = checkBigItemMapper.findByNames(new CheckBigItemFormMap());
        for(CheckBigItemFormMap checkBigItemFormMap:checkBigItemFormMapList){
            //获取用户所有需要检测的检测小项
            customInfoFormMap.put("big_item_id",checkBigItemFormMap.getLong("id"));
            List<CheckSmallItemFormMap> checkSmallItemFormMapList = getCustomerCheckSmallItemsList(customInfoFormMap);
            if(CollectionUtils.isEmpty(checkSmallItemFormMapList)){
                continue;
            }
            //获取用户的所有占比
            CheckItemDegrLeveZhanbiFormMap smallZhanbiCondition = new CheckItemDegrLeveZhanbiFormMap();
            smallZhanbiCondition.put("check_id",checkBigItemFormMap.getLong("id"));
            smallZhanbiCondition.put("check_type",1);
            smallZhanbiCondition.put("age",age);
            smallZhanbiCondition.put("bmi",new BigDecimal(bmi).setScale(1,BigDecimal.ROUND_HALF_UP));
            smallZhanbiCondition.put("bodyheight",height);
            smallZhanbiCondition.put("weight",weight);
            List<CheckItemDegrLeveZhanbiFormMap> zhanbiFormMapList =checkItemDegrLeveZhanbiMapper.findFZhanbiList(smallZhanbiCondition);
            if(CollectionUtils.isEmpty(zhanbiFormMapList)){//获取总的占比配置
                logger.error("没有找到匹配的占比，无法完成计算!检测大项：" + checkBigItemFormMap.toString() + "用户:" + customInfoFormMap.toString() + "年龄：" + age + "bmi:" + new BigDecimal(bmi).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "身高:" + height + "体重:" + weight);
                smallZhanbiCondition = new CheckItemDegrLeveZhanbiFormMap();
                smallZhanbiCondition.put("check_type",0);
                smallZhanbiCondition.put("age",age);
                smallZhanbiCondition.put("bmi",new BigDecimal(bmi).setScale(1,BigDecimal.ROUND_HALF_UP));
                smallZhanbiCondition.put("bodyheight",height);
                smallZhanbiCondition.put("weight",weight);
                zhanbiFormMapList =checkItemDegrLeveZhanbiMapper.findFZhanbiList(smallZhanbiCondition);
            }
            if(CollectionUtils.isEmpty(zhanbiFormMapList)){
                throw new Exception("没有找到匹配的占比，无法完成计算!"+checkBigItemFormMap.toString());
            }

            // 在占比配置随机一种占比方案，此大项下的所有小项跟随这个占比配置


            int max =zhanbiFormMapList.size()-1;
            int min = 0;
            int randomNumber = (int) Math.round(Math.random()*(max-min)+min);

            CheckItemDegrLeveZhanbiFormMap zhanbiFormMap = null;
            try{
                zhanbiFormMap = zhanbiFormMapList.get(randomNumber);
            }catch (Exception ex){
                ex.printStackTrace();
                logger.error("占比方案选择随机数有问题！");
            }
            if(null == zhanbiFormMap){
                throw new Exception("系统异常，获取占比配置方案异常");
            }


            Map<CfPingfenLeveFormMap,List<CheckSmallItemFormMap>> mapListMap = this.getZhanbiLeveList(cfPingfenLeveFormMapList,zhanbiFormMap,checkSmallItemFormMapList);

            if(mapListMap!=null && mapListMap.size()>0){
                Set<CfPingfenLeveFormMap> cfPingfenLeveFormMapskeys = mapListMap.keySet();
                Iterator<CfPingfenLeveFormMap> leveFormMapIterator = cfPingfenLeveFormMapskeys.iterator();
                while (leveFormMapIterator.hasNext()){
                    CfPingfenLeveFormMap key = leveFormMapIterator.next();
                    List<CheckSmallItemFormMap> checkSmallItemFormMapsLeveValues = mapListMap.get(key);
                    if(CollectionUtils.isEmpty(checkSmallItemFormMapsLeveValues)){
                        continue;
                    }

                    List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = new ArrayList<PhysicalExaminationResultFormMap>();
                    for(CheckSmallItemFormMap checkSmallItemFormMap:checkSmallItemFormMapsLeveValues){
                        PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
                        physicalExaminationResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
                        physicalExaminationResultFormMap.put("bit_item_id",checkBigItemFormMap.getLong("id"));
                        physicalExaminationResultFormMap.put("small_item_id",checkSmallItemFormMap.getLong("id"));


                        BigDecimal n1 = checkSmallItemFormMap.getBigDecimal("min_value");
                        BigDecimal n2 = checkSmallItemFormMap.getBigDecimal("max_value");

                        physicalExaminationResultFormMap.put("gen_min_value",n1);
                        physicalExaminationResultFormMap.put("gen_max_value",n2);

                        BigDecimal n = (n2.subtract(n1));

                        physicalExaminationResultFormMap.put("gen_in_value",n);


                        //计算区间得分
                        BigDecimal M = n.divide(new BigDecimal(30.0),3, RoundingMode.HALF_UP);
                        physicalExaminationResultFormMap.put("in_value_score",M);
                        //随机生成
                        //todo 随机范围值，采用公式：A小 = N1+5*M   A大=N1+35*M
                        int checkMax =(n1.add(new BigDecimal(35).multiply(M)).multiply(new BigDecimal(1000))).setScale(3,BigDecimal.ROUND_HALF_UP).intValue();
                        int checkMin = (n1.add(new BigDecimal(5).multiply(M)).multiply(new BigDecimal(1000))).setScale(3,BigDecimal.ROUND_HALF_UP).intValue();

                        //记录随机值范围的最大和最小值
                        physicalExaminationResultFormMap.put("random_max",new BigDecimal(checkMax/1000d));
                        physicalExaminationResultFormMap.put("random_min",new BigDecimal(checkMin/1000d));


                        CheckItemDegrLeveZhanbiFormMap checkItemDegrLeveZhanbiFormMapConditonSmall = new CheckItemDegrLeveZhanbiFormMap();
                        checkItemDegrLeveZhanbiFormMapConditonSmall.put("check_id",checkSmallItemFormMap.getLong("id"));
                        checkItemDegrLeveZhanbiFormMapConditonSmall.put("check_type",2);
                        checkItemDegrLeveZhanbiFormMapConditonSmall.put("age",age);
                        checkItemDegrLeveZhanbiFormMapConditonSmall.put("bmi",new BigDecimal(bmi).setScale(1,BigDecimal.ROUND_HALF_UP));
                        checkItemDegrLeveZhanbiFormMapConditonSmall.put("bodyheight",height);
                        checkItemDegrLeveZhanbiFormMapConditonSmall.put("weight",weight);
                        List<CheckItemDegrLeveZhanbiFormMap> smallZhanbiList = checkItemDegrLeveZhanbiMapper.findFZhanbiList(checkItemDegrLeveZhanbiFormMapConditonSmall);
                        boolean needTZ = false;
                        CheckItemDegrLeveZhanbiFormMap checkItemDegrLeveZhanbiFormMapTemp =null;
                        if(CollectionUtils.isNotEmpty(smallZhanbiList)){
                            //todo 随机一个分布方案
                            int maxSmall =smallZhanbiList.size()-1;
                            int minSmall = 0;
                            int randomIndexSmall = (int) Math.round(Math.random()*(maxSmall-minSmall)+minSmall);
                            checkItemDegrLeveZhanbiFormMapTemp = smallZhanbiList.get(randomIndexSmall);
                            boolean canChosen= false;
                            switch ((int)key.getLong("id").longValue()){
                                case 1:
                                    canChosen = checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_1_procent").doubleValue()>0;
                                    break;
                                case 2:
                                    canChosen = checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_2_procent").doubleValue()>0;
                                    break;
                                case 3:
                                    canChosen = checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_3_procent").doubleValue()>0;
                                    break;
                                case 4:
                                    canChosen = checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_4_procent").doubleValue()>0;
                                    break;
                                case 5:
                                    canChosen = checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_5_procent").doubleValue()>0;
                                    break;
                            }
                            if(!canChosen){//如果没有匹配的，则需要调整
                                needTZ = true;
                            }

                        }

                        if(needTZ &&checkItemDegrLeveZhanbiFormMapTemp!=null ){//需要调整，且有调整方案

                            List<Integer> leveList = new ArrayList<Integer>();
                            if(checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_1_procent").doubleValue()>0){
                                leveList.add(1);
                            }
                            if(checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_2_procent").doubleValue()>0){
                                leveList.add(2);
                            }
                            if(checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_3_procent").doubleValue()>0){
                                leveList.add(3);
                            }
                            if(checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_4_procent").doubleValue()>0){
                                leveList.add(4);
                            }
                            if(checkItemDegrLeveZhanbiFormMapTemp.getBigDecimal("leve_5_procent").doubleValue()>0){
                                leveList.add(5);
                            }
                            if(leveList!=null){
                                int maxSmall =leveList.size()-1;
                                int minSmall = 0;
                                int randomIndexSmall = (int) Math.round(Math.random()*(maxSmall-minSmall)+minSmall);
                                for(CfPingfenLeveFormMap pingfenLeveFormMap:cfPingfenLeveFormMapList){
                                    if(pingfenLeveFormMap.getLong("id").intValue() == leveList.get(randomIndexSmall).intValue()){


                                        physicalExaminationResultFormMap.put("bmiorage",6);
                                        physicalExaminationResultFormMap.put("bmiorage_leve_change",physicalExaminationResultFormMap.getLong("tzed_leve_id"));
                                        physicalExaminationResultFormMap.put("check_value_tzbef",physicalExaminationResultFormMap.getBigDecimal("check_value"));
                                        physicalExaminationResultFormMap.put("item_score_tzbef",physicalExaminationResultFormMap.getBigDecimal("item_score"));

                                        //随机在等级范围内随机生成得分
                                        int randomMaxScore = 95*1000;
                                        int randomMinScore = 65*1000;


                                        int tzmin = (int) (pingfenLeveFormMap.getDouble("pingfen_min")*1000d);
                                        int  tzmax = (int) (pingfenLeveFormMap.getDouble("pingfen_max")*1000d);
                                        if(tzmin<=randomMinScore){
                                            tzmin = randomMinScore;
                                        }
                                        if(tzmax>=randomMaxScore){
                                            tzmax = randomMaxScore;
                                        }
                                        int tzScoreRandom = (int) Math.round(Math.random()*(tzmax-tzmin)+tzmin);
                                        //随机的得分
                                        BigDecimal tzB = new BigDecimal(tzScoreRandom/1000d);
                                        physicalExaminationResultFormMap.put("item_score",tzB);

                                        //反推回去的检测值 A ? = (100-B)*M+n1
                                        BigDecimal tzA = new BigDecimal(100).subtract(tzB).multiply(M).add(n1);
                                        physicalExaminationResultFormMap.put("check_value",tzA);
                                        physicalExaminationResultFormMap.put("orgin_leve_id",pingfenLeveFormMap.getLong("id"));
                                        physicalExaminationResultFormMap.put("tzed_leve_id",pingfenLeveFormMap.getLong("id"));

                                        break;

                                    }
                                }
                            }

                        }else{
                            //随机在等级范围内随机生成得分
                            int randomMaxScore = 95*1000;
                            int randomMinScore = 65*1000;


                            int tzmin = (int) (key.getDouble("pingfen_min")*1000d);
                            int  tzmax = (int) (key.getDouble("pingfen_max")*1000d);
                            if(tzmin<=randomMinScore){
                                tzmin = randomMinScore;
                            }
                            if(tzmax>=randomMaxScore){
                                tzmax = randomMaxScore;
                            }
                            int tzScoreRandom = (int) Math.round(Math.random()*(tzmax-tzmin)+tzmin);
                            //随机的得分
                            BigDecimal tzB = new BigDecimal(tzScoreRandom/1000d);
                            physicalExaminationResultFormMap.put("item_score",tzB);

                            //反推回去的检测值 A ? = (100-B)*M+n1
                            BigDecimal tzA = new BigDecimal(100).subtract(tzB).multiply(M).add(n1);
                            physicalExaminationResultFormMap.put("check_value",tzA);
                            physicalExaminationResultFormMap.put("orgin_leve_id",key.getLong("id"));
                            physicalExaminationResultFormMap.put("tzed_leve_id",key.getLong("id"));
                        }




                        physicalExaminationResultFormMapList.add(physicalExaminationResultFormMap);
                    }
                    List<PhysicalExaminationResult> physicalExaminationResultList = new ArrayList<PhysicalExaminationResult>();
                    for(PhysicalExaminationResultFormMap item:physicalExaminationResultFormMapList){
                        PhysicalExaminationResult result = new PhysicalExaminationResult();
                        result.setBit_item_id(item.get("bit_item_id")==null?null:item.getLong("bit_item_id"));
                        result.setCheck_value(item.get("check_value")==null?null:item.getBigDecimal("check_value"));
                        result.setExamination_record_id(item.get("examination_record_id")==null?null:item.getLong("examination_record_id"));
                        result.setGen_in_value(item.get("gen_in_value")==null?null:item.getBigDecimal("gen_in_value"));
                        result.setGen_max_value(item.get("gen_max_value")==null?null:item.getBigDecimal("gen_max_value"));
                        result.setGen_min_value(item.get("gen_min_value")==null?null:item.getBigDecimal("gen_min_value"));
                        result.setGen_quanzhong(item.get("gen_quanzhong")==null?null:item.getBigDecimal("gen_quanzhong"));
                        result.setIn_value_score(item.get("in_value_score")==null?null:item.getBigDecimal("in_value_score"));
                        result.setItem_score(item.get("item_score")==null?null:item.getBigDecimal("item_score"));
                        result.setOrgin_leve_id(item.get("orgin_leve_id")==null?null:item.getLong("orgin_leve_id"));
                        result.setQuanzhong_score(item.get("quanzhong_score")==null?null:item.getBigDecimal("quanzhong_score"));
                        result.setSmall_item_id(item.get("small_item_id")==null?null:item.getLong("small_item_id"));
                        result.setTzed_leve_id(item.get("tzed_leve_id")==null?null:item.getLong("tzed_leve_id"));

                        result.setRandom_max(item.get("random_max")==null?null:item.getBigDecimal("random_max"));
                        result.setRandom_min(item.get("random_min")==null?null:item.getBigDecimal("random_min"));
                        result.setBmiorage(item.get("bmiorage")==null?null:item.getInt("bmiorage"));
                        result.setBmiorage_leve_change(item.get("bmiorage_leve_change")==null?null:item.getLong("bmiorage_leve_change"));
                        result.setCheck_value_tzbef(item.get("check_value_tzbef")==null?null:item.getBigDecimal("check_value_tzbef"));
                        result.setItem_score_tzbef(item.get("item_score_tzbef")==null?null:item.getBigDecimal("item_score_tzbef"));


                        physicalExaminationResultList.add(result);
                    }
                    if(CollectionUtils.isNotEmpty(physicalExaminationResultList)){
                        physicalExaminationResultMapper.saveBatchResult(physicalExaminationResultList);
                    }

                }
            }

        }
        //todo 关联等级调整
        List<CheckItemReleationConfigMap> checkItemReleationConfigMapList = checkItemReleationConfigMapper.findAll();
        if(CollectionUtils.isNotEmpty(checkItemReleationConfigMapList)){
            List<Long> checkItemIdList = new ArrayList<Long>();
            for(CheckItemReleationConfigMap checkItemReleationConfigMap:checkItemReleationConfigMapList){
                checkItemIdList.add(checkItemReleationConfigMap.getLong("dist_check_id"));
                checkItemIdList.add(checkItemReleationConfigMap.getLong("org_check_id"));
            }
            if(CollectionUtils.isNotEmpty(checkItemIdList)){
                StringBuffer smallItemIds =new StringBuffer("");
                for(int i=0;i<checkItemIdList.size();i++){
                    Long ids =checkItemIdList.get(i);
                    smallItemIds.append(ids).append(",");
                }
                //todo 查询列表中的检测项
                PhysicalExaminationResultFormMap physicalExaminationResultFormMapWhereCondtion = new PhysicalExaminationResultFormMap();

                physicalExaminationResultFormMapWhereCondtion.put("where","where examination_record_id="+physicalExaminationRecordFormMap.getLong("id")+" and  small_item_id in ("+smallItemIds.toString().substring(0,smallItemIds.toString().lastIndexOf(","))+") order by id desc");
                List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapRelList = physicalExaminationResultMapper.findByWhere(physicalExaminationResultFormMapWhereCondtion);
                if(CollectionUtils.isNotEmpty(physicalExaminationResultFormMapRelList)){
                    //todo 遍历结果，处理关联项
                    for(CheckItemReleationConfigMap checkItemReleationConfigMap:checkItemReleationConfigMapList){
                        Long dist_check_id = checkItemReleationConfigMap.getLong("dist_check_id");
                        Long org_check_id = checkItemReleationConfigMap.getLong("org_check_id");

                        for(PhysicalExaminationResultFormMap tempDist:physicalExaminationResultFormMapRelList){
                            if(dist_check_id.longValue() == tempDist.getLong("small_item_id")){
                                for(PhysicalExaminationResultFormMap tempOrg:physicalExaminationResultFormMapRelList){
                                    if(org_check_id.longValue() == tempOrg.getLong("small_item_id")){//todo 调整
                                        tempOrg.put("bmiorage",5);
                                        tempOrg.put("bmiorage_leve_change",tempOrg.getLong("tzed_leve_id"));
                                        tempOrg.put("check_value_tzbef",tempOrg.getBigDecimal("check_value"));
                                        tempOrg.put("item_score_tzbef",tempOrg.getBigDecimal("item_score"));


                                        tempOrg.put("tzed_leve_id",tempDist.getLong("tzed_leve_id"));
                                        for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
                                            if(item.getLong("id").longValue() ==tempDist.getLong("tzed_leve_id")){
                                                int randomMaxScore = 95*1000;
                                                int randomMinScore = 65*1000;


                                                int tzmin = (int) (item.getDouble("pingfen_min")*1000d);
                                                int  tzmax = (int) (item.getDouble("pingfen_max")*1000d);
                                                if(tzmin<=randomMinScore){
                                                    tzmin = randomMinScore;
                                                }
                                                if(tzmax>=randomMaxScore){
                                                    tzmax = randomMaxScore;
                                                }
                                                int tzScoreRandom = (int) Math.round(Math.random()*(tzmax-tzmin)+tzmin);
                                                //随机的得分
                                                BigDecimal tzB = new BigDecimal(tzScoreRandom/1000d);
                                                tempOrg.put("item_score",tzB);

                                                break;
                                            }
                                        }




                                        BigDecimal tzA = new BigDecimal(100).subtract(tempDist.getBigDecimal("item_score")).multiply(tempOrg.getBigDecimal("in_value_score")).add(tempOrg.getBigDecimal("gen_min_value"));

                                        tempOrg.put("check_value",tzA);
                                        physicalExaminationResultMapper.editEntity(tempOrg);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

    }


    /**
     * 生成不同等级中对应有哪些项
     * @param cfPingfenLeveFormMapList
     * @param zhanbiFormMap
     * @param checkSmallItemFormMapList
     * @return
     */
    private Map<CfPingfenLeveFormMap,List<CheckSmallItemFormMap>> getZhanbiLeveList(List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList, CheckItemDegrLeveZhanbiFormMap zhanbiFormMap,List<CheckSmallItemFormMap> checkSmallItemFormMapList) {
        //todo 根据占比方案，计算占比项
        BigDecimal size = new BigDecimal(checkSmallItemFormMapList.size());

        BigDecimal leve1BD = zhanbiFormMap.getBigDecimal("leve_1_procent").divide(new BigDecimal(100)).multiply(size);
        int leve1Count =  getLeve2Count(leve1BD);
        BigDecimal leve2BD = zhanbiFormMap.getBigDecimal("leve_2_procent").divide(new BigDecimal(100)).multiply(size);
        int leve2Count = getLeve2Count(leve2BD);
        BigDecimal leve3BD = zhanbiFormMap.getBigDecimal("leve_3_procent").divide(new BigDecimal(100)).multiply(size);
        int leve3Count = getLeve2Count(leve3BD);
        BigDecimal leve4BD = zhanbiFormMap.getBigDecimal("leve_4_procent").divide(new BigDecimal(100)).multiply(size);
        int leve4Count = getLeve2Count(leve4BD);
        BigDecimal leve5BD = zhanbiFormMap.getBigDecimal("leve_5_procent").divide(new BigDecimal(100)).multiply(size);
        int leve5Count = getLeve2Count(leve5BD);

        //todo 相加是否等于小项数之和？
        int totalCount = leve1Count+leve2Count+leve3Count+leve4Count+leve5Count;
        if(totalCount<checkSmallItemFormMapList.size()){//表示是却项的
            List<Integer> leveList = new ArrayList<Integer>();
            if(zhanbiFormMap.getBigDecimal("leve_1_procent").doubleValue()>0){
                leveList.add(1);
            }
            if(zhanbiFormMap.getBigDecimal("leve_2_procent").doubleValue()>0){
                leveList.add(2);
            }
            if(zhanbiFormMap.getBigDecimal("leve_3_procent").doubleValue()>0){
                leveList.add(3);
            }
            if(zhanbiFormMap.getBigDecimal("leve_4_procent").doubleValue()>0){
                leveList.add(4);
            }
            if(zhanbiFormMap.getBigDecimal("leve_5_procent").doubleValue()>0){
                leveList.add(5);
            }
            if(CollectionUtils.isNotEmpty(leveList)){
                int maxSmall =leveList.size()-1;
                int minSmall = 0;
                int randomIndexSmall = (int) Math.round(Math.random()*(maxSmall-minSmall)+minSmall);
                switch (leveList.get(randomIndexSmall)){
                    case 1:
                        leve1Count+=(checkSmallItemFormMapList.size()-totalCount);
                        break;
                    case 2:
                        leve2Count+=(checkSmallItemFormMapList.size()-totalCount);
                        break;
                    case 3:
                        leve3Count+=(checkSmallItemFormMapList.size()-totalCount);
                        break;
                    case 4:
                        leve4Count+=(checkSmallItemFormMapList.size()-totalCount);
                        break;
                    case 5:
                        leve5Count+=(checkSmallItemFormMapList.size()-totalCount);
                        break;
                }
            }
        }

        Map<CfPingfenLeveFormMap,List<CheckSmallItemFormMap>> mapListMap = new HashMap<CfPingfenLeveFormMap, List<CheckSmallItemFormMap>>();
        List<CheckSmallItemFormMap> checkSmallItemFormMapListTemp =  checkSmallItemFormMapList;

        genCount(cfPingfenLeveFormMapList, checkSmallItemFormMapListTemp, leve1Count, mapListMap,1);
        genCount(cfPingfenLeveFormMapList, checkSmallItemFormMapListTemp, leve2Count, mapListMap,2);
        genCount(cfPingfenLeveFormMapList, checkSmallItemFormMapListTemp, leve3Count, mapListMap,3);
        genCount(cfPingfenLeveFormMapList, checkSmallItemFormMapListTemp, leve4Count, mapListMap,4);
        genCount(cfPingfenLeveFormMapList, checkSmallItemFormMapListTemp, leve5Count, mapListMap,5);
        return mapListMap;

    }

    /**
     * 传入的参数如果是大于1的，则向下取整，反之则向上取整
     * @param leveBD
     * @return
     */
    private int getLeve2Count(BigDecimal leveBD) {
        if(leveBD.doubleValue() == 0){
            return 0;
        }
//        return leveBD.doubleValue()>=1? leveBD.setScale(0,BigDecimal.ROUND_DOWN).intValue(): leveBD.setScale(0,BigDecimal.ROUND_UP).intValue();
//        return leveBD.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
        return leveBD.doubleValue()>1? leveBD.setScale(0,BigDecimal.ROUND_HALF_UP).intValue(): 1;
    }


    private void genCount(List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList, List<CheckSmallItemFormMap> checkSmallItemFormMapList, int leveCount, Map<CfPingfenLeveFormMap, List<CheckSmallItemFormMap>> mapListMap, long leveid) {
        if(leveCount >0&&CollectionUtils.isNotEmpty(checkSmallItemFormMapList)){
            for(CfPingfenLeveFormMap cfPingfenLeveFormMap:cfPingfenLeveFormMapList){
                if(cfPingfenLeveFormMap.getLong("id").longValue() == leveid){
                    List<CheckSmallItemFormMap> randomList = new ArrayList<CheckSmallItemFormMap>();
                    for(int i = 0; i< leveCount &&checkSmallItemFormMapList.size()>0; i++){
                        int max =checkSmallItemFormMapList.size()-1;
                        int min = 0;
                        int  randomNumber = (int) Math.round(Math.random()*(max-min)+min);
                        CheckSmallItemFormMap checkSmallItemFormMap = checkSmallItemFormMapList.get(randomNumber);
                        randomList.add(checkSmallItemFormMap);
                        checkSmallItemFormMapList.remove(randomNumber);
                    }
                    mapListMap.put(cfPingfenLeveFormMap,randomList);
                    break;
                }
            }
        }
    }


    /**
     * 生成检测项检测结果
     * @param customInfoFormMap
     * @param physicalExaminationRecordFormMap
     * @throws Exception
     */
    private void genCheckSmallItemResult(CustomInfoFormMap customInfoFormMap,  PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap) throws Exception {
        List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(new CfPingfenLeveFormMap());
        if(CollectionUtils.isEmpty(cfPingfenLeveFormMapList)){
            throw new Exception("评分等级未配置！");
        }
        List<CheckSmallItemFormMap> checkSmallItemFormMapList = getCustomerCheckSmallItemsList(customInfoFormMap);
        if(CollectionUtils.isEmpty(checkSmallItemFormMapList)){
            throw new Exception("没有配置检测项");
        }
        List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = new ArrayList<PhysicalExaminationResultFormMap>();
        for(CheckSmallItemFormMap checkSmallItemFormMap:checkSmallItemFormMapList){
            PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
            physicalExaminationResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
            physicalExaminationResultFormMap.put("bit_item_id",checkSmallItemFormMap.getLong("big_item_id"));
            physicalExaminationResultFormMap.put("small_item_id",checkSmallItemFormMap.getLong("id"));
            physicalExaminationResultFormMapList.add(physicalExaminationResultFormMap);
        }



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


                    //计算区间得分
                    BigDecimal M = n.divide(new BigDecimal(30.0),3, RoundingMode.HALF_UP);
                    checkSmallItemResult.put("in_value_score",M);
                    //随机生成
                    //todo 随机范围值，采用公式：A小 = N1+5*M   A大=N1+35*M
                    int max =(n1.add(new BigDecimal(35).multiply(M)).multiply(new BigDecimal(1000))).setScale(3,BigDecimal.ROUND_HALF_UP).intValue();
                    int min = (n1.add(new BigDecimal(5).multiply(M)).multiply(new BigDecimal(1000))).setScale(3,BigDecimal.ROUND_HALF_UP).intValue();
                    int randomNumber = (int) Math.round(Math.random()*(max-min)+min);

                    BigDecimal A = new BigDecimal(randomNumber/1000d);

                    //记录随机值范围的最大和最小值
                    checkSmallItemResult.put("random_max",new BigDecimal(max/1000d));
                    checkSmallItemResult.put("random_min",new BigDecimal(min/1000d));
                    checkSmallItemResult.put("check_value",A);

                    //计算得分
                    BigDecimal B = new BigDecimal(100).subtract((A.subtract(n1).divide(M,3,BigDecimal.ROUND_HALF_UP))) ;



                    checkSmallItemResult.put("item_score",B);

                    //获取得分等级
                    for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
                        if(item.getDouble("pingfen_min")<=B.doubleValue() && item.getDouble("pingfen_max")>=B.doubleValue()){
                            checkSmallItemResult.put("orgin_leve_id",item.getLong("id"));
                            checkSmallItemResult.put("tzed_leve_id",item.getLong("id"));
                            break;
                        }
                    }

                    Date birthday = customInfoFormMap.getDate("birthday");


                    //看看当前大项是否需要关联BMi，如果需要关联BMI，则查询出BMI关联配置，否则就直接去原等级调整
                    CheckBigItemFormMap checkBigItemFormMap = checkBigItemMapper.findbyFrist("id",checkSmallItemFormMap.get("big_item_id").toString(),CheckBigItemFormMap.class);
                    if(checkBigItemFormMap == null ){
                        throw new Exception("配置异常!");
                    }
                    List<BmiCheckItemConfigFormMap> bmiCheckItemConfigFormMapList = new ArrayList<BmiCheckItemConfigFormMap>();
                    List<CfPingfenRoutFormMap> cfPingfenRoutFormMapList = new ArrayList<CfPingfenRoutFormMap>();
                    if(checkBigItemFormMap.getInt("withbmi") == 1){//先去找小项配置，如果有配置就不去理会大项配置，否则使用大项配置


                        //todo 获取bmi评分调整配置（如果bmi评分调整配置的有，则忽略年龄评分配置）
                        BmiCheckItemConfigFormMap bmiCheckItemConfigFormMap = new BmiCheckItemConfigFormMap();
                        Double weight =customInfoFormMap.getDouble("weight");
                        Double height =Double.parseDouble(customInfoFormMap.get("body_height").toString());
                        Double bmi = weight/((height/100)*(height/100));

                        bmiCheckItemConfigFormMap.put("age",AgeCal.getAge(birthday));//年龄
                        bmiCheckItemConfigFormMap.put("bmi",new BigDecimal(bmi).setScale(3,BigDecimal.ROUND_HALF_UP));//BMI
                        bmiCheckItemConfigFormMap.put("check_item_id",checkSmallItemFormMap.getLong("id"));//检测小项
                        bmiCheckItemConfigFormMap.put("check_type",2);
                        bmiCheckItemConfigFormMap.put("org_leve_id",checkSmallItemResult.getLong("orgin_leve_id"));
                        bmiCheckItemConfigFormMapList = bmiCheckItemConfigMapper.findFixedOneItem(bmiCheckItemConfigFormMap);

                        if(CollectionUtils.isEmpty(bmiCheckItemConfigFormMapList)){
                            //todo 获取bmi评分调整配置（如果bmi评分调整配置的有，则忽略年龄评分配置）
                            BmiCheckItemConfigFormMap bmiCheckItemConfigFormMapBig = new BmiCheckItemConfigFormMap();
                            bmiCheckItemConfigFormMap.put("age",AgeCal.getAge(birthday));//年龄
                            bmiCheckItemConfigFormMap.put("bmi",new BigDecimal(bmi).setScale(3,BigDecimal.ROUND_HALF_UP));//BMI
                            bmiCheckItemConfigFormMap.put("check_item_id",checkSmallItemFormMap.getLong("big_item_id"));//检测小项
                            bmiCheckItemConfigFormMap.put("check_type",1);
                            bmiCheckItemConfigFormMap.put("org_leve_id",checkSmallItemResult.getLong("orgin_leve_id"));
                            bmiCheckItemConfigFormMapList = bmiCheckItemConfigMapper.findFixedOneItem(bmiCheckItemConfigFormMap);
                        }

                    }else{
                        // 查找小项等级评分调整概率
                        CfPingfenRoutFormMap cfPingfenRoutFormMap = new CfPingfenRoutFormMap();
                        // 计算用户年龄
                        cfPingfenRoutFormMap.put("age", AgeCal.getAge(birthday));
                        cfPingfenRoutFormMap.put("small_id",checkSmallItemFormMap.getLong("id"));
                        cfPingfenRoutFormMap.put("orgin_pingfen_id",checkSmallItemResult.getLong("orgin_leve_id"));
                        cfPingfenRoutFormMapList = cfPingfenRoutMapper.findSmallItemRout(cfPingfenRoutFormMap);
                    }



                    int maxTz = 0;
                    int minTz = 1000;
                    int randomNumberTz = (int) Math.round(Math.random()*(maxTz-minTz)+minTz);

                    CfPingfenLeveFormMap cfPingfenLeveFormMap = null;


                    if(CollectionUtils.isNotEmpty(bmiCheckItemConfigFormMapList) && bmiCheckItemConfigFormMapList.size()==1){//按照bmi配置等级调整
                        BmiCheckItemConfigFormMap bmiCheckItemConfigFormMapTemp = bmiCheckItemConfigFormMapList.get(0);
                        if(randomNumberTz <=bmiCheckItemConfigFormMapTemp.getBigDecimal("rout").doubleValue()*1000) {
                            checkSmallItemResult.put("bmiorage", 1);
                            checkSmallItemResult.put("bmiorage_leve_change",checkSmallItemResult.getLong("tzed_leve_id"));
                            checkSmallItemResult.put("tzed_leve_id", bmiCheckItemConfigFormMapTemp.getLong("tz_leve_id"));
                            // 随机生成得分等级范围内的得分值  B
                            for (CfPingfenLeveFormMap temp : cfPingfenLeveFormMapList) {
                                if (temp.getLong("id").longValue() ==  bmiCheckItemConfigFormMapTemp.getLong("tz_leve_id")) {
                                    cfPingfenLeveFormMap = temp;
                                    break;
                                }
                            }
                        }

                    }else if(CollectionUtils.isNotEmpty(cfPingfenRoutFormMapList) && cfPingfenRoutFormMapList.size() ==1){//按照评分概率调整
                        CfPingfenRoutFormMap cfPingfenRoutFormMap = cfPingfenRoutFormMapList.get(0);
                        if(randomNumberTz >=cfPingfenRoutFormMap.getDouble("rout_min")*1000 && randomNumberTz<=cfPingfenRoutFormMap.getDouble("rout_max")*1000) {
                            checkSmallItemResult.put("bmiorage", 2);
                            checkSmallItemResult.put("bmiorage_leve_change", checkSmallItemResult.getLong("tzed_leve_id"));
                            checkSmallItemResult.put("tzed_leve_id", cfPingfenRoutFormMap.getLong("tz_pingfen_id"));
                            // 随机生成得分等级范围内的得分值  B
                            for (CfPingfenLeveFormMap temp : cfPingfenLeveFormMapList) {
                                if (temp.getLong("id").longValue() == cfPingfenRoutFormMap.getLong("tz_pingfen_id")) {
                                    cfPingfenLeveFormMap = temp;
                                    break;
                                }
                            }
                        }
                    }else{
                        System.out.println("无等级调整");
                        checkSmallItemResult.put("bmiorage", 0);
                    }


                    if(cfPingfenLeveFormMap !=null){
                        //生成随机范围内的得分,作为调整后的得分
                        int randomMax = 95*1000;
                        int randomMin = 65*1000;


                        int tzmin = (int) (cfPingfenLeveFormMap.getDouble("pingfen_min")*1000d);
                        int  tzmax = (int) (cfPingfenLeveFormMap.getDouble("pingfen_max")*1000d);
                        if(tzmin<=randomMin){
                            tzmin = randomMin;
                        }
                        if(tzmax>=randomMax){
                            tzmax = randomMax;
                        }
                        int tzScoreRandom = (int) Math.round(Math.random()*(tzmax-tzmin)+tzmin);
                        BigDecimal tzB = new BigDecimal(tzScoreRandom/1000d);

                        //A ? = (100-B)*M+n1
                        BigDecimal tzA = new BigDecimal(100).subtract(tzB).multiply(M).add(n1);

                        checkSmallItemResult.put("check_value_tzbef",checkSmallItemResult.getBigDecimal("check_value"));
                        checkSmallItemResult.put("item_score_tzbef",checkSmallItemResult.getBigDecimal("item_score"));

                        checkSmallItemResult.put("check_value",tzA);
                        checkSmallItemResult.put("item_score",tzB);


                    }

                    //根据调整概率，生成新的功能等级
                    break;
                }

            }
        }
        //保存到数据库
        List<PhysicalExaminationResult> physicalExaminationResultList = new ArrayList<PhysicalExaminationResult>();
        if(CollectionUtils.isNotEmpty(physicalExaminationResultFormMapList)){
            for(PhysicalExaminationResultFormMap item:physicalExaminationResultFormMapList){
                PhysicalExaminationResult result = new PhysicalExaminationResult();
                result.setBit_item_id(item.get("bit_item_id")==null?null:item.getLong("bit_item_id"));
                result.setCheck_value(item.get("check_value")==null?null:item.getBigDecimal("check_value"));
                result.setExamination_record_id(item.get("examination_record_id")==null?null:item.getLong("examination_record_id"));
                result.setGen_in_value(item.get("gen_in_value")==null?null:item.getBigDecimal("gen_in_value"));
                result.setGen_max_value(item.get("gen_max_value")==null?null:item.getBigDecimal("gen_max_value"));
                result.setGen_min_value(item.get("gen_min_value")==null?null:item.getBigDecimal("gen_min_value"));
                result.setGen_quanzhong(item.get("gen_quanzhong")==null?null:item.getBigDecimal("gen_quanzhong"));
                result.setIn_value_score(item.get("in_value_score")==null?null:item.getBigDecimal("in_value_score"));
                result.setItem_score(item.get("item_score")==null?null:item.getBigDecimal("item_score"));
                result.setOrgin_leve_id(item.get("orgin_leve_id")==null?null:item.getLong("orgin_leve_id"));
                result.setQuanzhong_score(item.get("quanzhong_score")==null?null:item.getBigDecimal("quanzhong_score"));
                result.setSmall_item_id(item.get("small_item_id")==null?null:item.getLong("small_item_id"));
                result.setTzed_leve_id(item.get("tzed_leve_id")==null?null:item.getLong("tzed_leve_id"));

                result.setRandom_max(item.get("random_max")==null?null:item.getBigDecimal("random_max"));
                result.setRandom_min(item.get("random_min")==null?null:item.getBigDecimal("random_min"));
                result.setBmiorage(item.get("bmiorage")==null?null:item.getInt("bmiorage"));
                result.setBmiorage_leve_change(item.get("bmiorage_leve_change")==null?null:item.getLong("bmiorage_leve_change"));
                result.setCheck_value_tzbef(item.get("check_value_tzbef")==null?null:item.getBigDecimal("check_value_tzbef"));
                result.setItem_score_tzbef(item.get("item_score_tzbef")==null?null:item.getBigDecimal("item_score_tzbef"));


                physicalExaminationResultList.add(result);
            }
            if(CollectionUtils.isNotEmpty(physicalExaminationResultList)){
                physicalExaminationResultMapper.saveBatchResult(physicalExaminationResultList);
            }


            //todo 调整关联项
            //查询所有的关联项
            List<CheckItemReleationConfigMap> checkItemReleationConfigMapList = checkItemReleationConfigMapper.findAll();
            if(CollectionUtils.isNotEmpty(checkItemReleationConfigMapList)){
                List<Long> checkItemIdList = new ArrayList<Long>();
                for(CheckItemReleationConfigMap checkItemReleationConfigMap:checkItemReleationConfigMapList){
                    checkItemIdList.add(checkItemReleationConfigMap.getLong("dist_check_id"));
                    checkItemIdList.add(checkItemReleationConfigMap.getLong("org_check_id"));
                }
                if(CollectionUtils.isNotEmpty(checkItemIdList)){
                    StringBuffer smallItemIds =new StringBuffer("");
                    for(int i=0;i<checkItemIdList.size();i++){
                        Long ids =checkItemIdList.get(i);
                        smallItemIds.append(ids).append(",");
                    }
                    //todo 查询列表中的检测项
                    PhysicalExaminationResultFormMap physicalExaminationResultFormMapWhereCondtion = new PhysicalExaminationResultFormMap();

                    physicalExaminationResultFormMapWhereCondtion.put("where","where examination_record_id="+physicalExaminationRecordFormMap.getLong("id")+" and  small_item_id in ("+smallItemIds.toString().substring(0,smallItemIds.toString().lastIndexOf(","))+") order by id desc");
                    List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapRelList = physicalExaminationResultMapper.findByWhere(physicalExaminationResultFormMapWhereCondtion);
                    if(CollectionUtils.isNotEmpty(physicalExaminationResultFormMapRelList)){
                        //todo 遍历结果，处理关联项
                        for(CheckItemReleationConfigMap checkItemReleationConfigMap:checkItemReleationConfigMapList){
                            Long dist_check_id = checkItemReleationConfigMap.getLong("dist_check_id");
                            Long org_check_id = checkItemReleationConfigMap.getLong("org_check_id");

                            for(PhysicalExaminationResultFormMap tempDist:physicalExaminationResultFormMapRelList){
                                if(dist_check_id.longValue() == tempDist.getLong("small_item_id")){
                                    for(PhysicalExaminationResultFormMap tempOrg:physicalExaminationResultFormMapRelList){
                                        if(org_check_id.longValue() == tempOrg.getLong("small_item_id")){//todo 调整
                                            tempOrg.put("bmiorage",5);
                                            tempOrg.put("bmiorage_leve_change",tempOrg.getLong("tzed_leve_id"));
                                            tempOrg.put("check_value_tzbef",tempOrg.getBigDecimal("check_value"));
                                            tempOrg.put("item_score_tzbef",tempOrg.getBigDecimal("item_score"));


                                            tempOrg.put("tzed_leve_id",tempDist.getLong("tzed_leve_id"));
                                            tempOrg.put("item_score",tempDist.getBigDecimal("item_score"));


                                            BigDecimal tzA = new BigDecimal(100).subtract(tempDist.getBigDecimal("item_score")).multiply(tempOrg.getBigDecimal("in_value_score")).add(tempOrg.getBigDecimal("gen_min_value"));

                                            tempOrg.put("check_value",tzA);
                                            physicalExaminationResultMapper.editEntity(tempOrg);
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }

    }





    @Autowired
    private DuanbanConfigMapper duanbanConfigMapper;

    @Autowired
    private BigItemResultLeveConfigMapper bigItemResultLeveConfigMapper;


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
                totalQuanzhong=totalQuanzhong.add(new BigDecimal(1));
                totalScore=totalScore.add(item.getBigDecimal("item_score"));
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

                /*//todo 获取短板项配置
                DuanbanConfigFormMap duanbanConfigFormMap = new DuanbanConfigFormMap();
                duanbanConfigFormMap.set("big_item_id",checkBigItemFormMap.getLong("id"));
                List<DuanbanConfigFormMap> duanbanConfigFormMapList =duanbanConfigMapper.findByNames(duanbanConfigFormMap);
                if(CollectionUtils.isNotEmpty(duanbanConfigFormMapList)){
                    //获取检测小项的等级数量列表
                    PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
                    physicalExaminationResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
                    physicalExaminationResultFormMap.put("bit_item_id",checkBigItemFormMap.getLong("id"));
                    List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = physicalExaminationResultMapper.findLeveGroupCount(physicalExaminationResultFormMap);
                    if(CollectionUtils.isNotEmpty(physicalExaminationResultFormMapList)){
                        for (DuanbanConfigFormMap duanbanConfigFormMapTemp:duanbanConfigFormMapList){
                            for(PhysicalExaminationResultFormMap physicalExaminationResultFormMapTemp:physicalExaminationResultFormMapList){
                                if (physicalExaminationResultFormMapTemp.getLong("leveid").longValue() == duanbanConfigFormMapTemp.getLong("duanban_leve_id").longValue() && duanbanConfigFormMapTemp.getInt("duanban_item_tz_count")<=physicalExaminationResultFormMapTemp.getLong("leveCount").intValue()){
                                    physicalExaminationBigResultFormMap.put("check_score",physicalExaminationBigResultFormMap.getBigDecimal("check_score").add(duanbanConfigFormMapTemp.getBigDecimal("tz_rout")));
                                }
                            }
                        }
                    }
                }*/

                //todo 得分等级结果调整
                //读取配置表，如果有匹配的大项配置，调整分数，调整等级
                //获取用户bmi和年龄
                CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id", recordFormMap.get("custom_id").toString(),CustomInfoFormMap.class);
                //体重
                Double weight =customInfoFormMap.getDouble("weight");
                //身高
                Double height =Double.parseDouble(customInfoFormMap.get("body_height").toString());
                //bmi
                Double bmi = weight/((height/100)*(height/100));
                //年龄
                Date birthday = customInfoFormMap.getDate("birthday");
                int age = AgeCal.getAge(birthday);

                BigItemResultLeveConfigFormMap bigItemResultLeveConfigFormMapCondition = new BigItemResultLeveConfigFormMap();
                bigItemResultLeveConfigFormMapCondition.put("check_item_id",checkBigItemFormMap.getLong("id"));
                bigItemResultLeveConfigFormMapCondition.put("check_type",1);
                bigItemResultLeveConfigFormMapCondition.put("age",age);
                bigItemResultLeveConfigFormMapCondition.put("bmi",bmi);
                bigItemResultLeveConfigFormMapCondition.put("org_leve_id",physicalExaminationBigResultFormMap.getLong("leve_id"));
                List<BigItemResultLeveConfigFormMap> bigItemResultLeveConfigMapperFixedList = bigItemResultLeveConfigMapper.findFixedList(bigItemResultLeveConfigFormMapCondition);
                if(CollectionUtils.isNotEmpty(bigItemResultLeveConfigMapperFixedList)){
                    //todo 分数调整
                    BigItemResultLeveConfigFormMap bigItemResultLeveConfigFormMap = bigItemResultLeveConfigMapperFixedList.get(0);
                    physicalExaminationBigResultFormMap.put("check_score",physicalExaminationBigResultFormMap.getBigDecimal("check_score").add(bigItemResultLeveConfigFormMap.getBigDecimal("change_score")));
                    //todo 等级调整
                    for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
                        if(item.getDouble("pingfen_min")<=physicalExaminationBigResultFormMap.getBigDecimal("check_score").doubleValue() && item.getDouble("pingfen_max")>=physicalExaminationBigResultFormMap.getBigDecimal("check_score").doubleValue()){
                            physicalExaminationBigResultFormMap.put("leve_id",item.getLong("id"));
                            break;
                        }
                    }
                }

                physicalExaminationBigResultMapper.addEntity(physicalExaminationBigResultFormMap);
                physicalExaminationBigResultFormMapList.add(physicalExaminationBigResultFormMap);


            }
        }
        return physicalExaminationBigResultFormMapList;
    }


    @Autowired
    private ZongpingLeveDescConfigMapper zongpingLeveDescConfigMapper;

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
//            totalQuanzhong = totalQuanzhong.add(item.getBigDecimal("gen_quanzhong"));
            totalQuanzhong = totalQuanzhong.add(new BigDecimal(1));
        }
        physicalExaminationMainReportFormMap.put("check_total_score",totalScore.divide(totalQuanzhong,3, RoundingMode.HALF_UP));

        for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
            if(item.getDouble("pingfen_min")<=physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").doubleValue() && item.getDouble("pingfen_max")>=physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").doubleValue()){
                physicalExaminationMainReportFormMap.put("level",item.getLong("id"));
                break;
            }
        }


        // 获取短板项配置
        DuanbanConfigFormMap duanbanConfigFormMap = new DuanbanConfigFormMap();
        duanbanConfigFormMap.set("big_item_id",0l);
        List<DuanbanConfigFormMap> duanbanConfigFormMapList =duanbanConfigMapper.findByNames(duanbanConfigFormMap);
        if(CollectionUtils.isNotEmpty(duanbanConfigFormMapList)){
            //获取大项的等级数量列表
            PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = new PhysicalExaminationBigResultFormMap();
            physicalExaminationBigResultFormMap.put("examination_record_id",recordid);
            List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapTempsList = physicalExaminationBigResultMapper.findLeveGroupCount(physicalExaminationBigResultFormMap);
            if(CollectionUtils.isNotEmpty(physicalExaminationBigResultFormMapTempsList)){
                for (DuanbanConfigFormMap duanbanConfigFormMapTemp:duanbanConfigFormMapList){
                    for(PhysicalExaminationBigResultFormMap physicalExaminationResultFormMapTemp:physicalExaminationBigResultFormMapTempsList){
                        if (physicalExaminationResultFormMapTemp.getLong("leveid").longValue() == duanbanConfigFormMapTemp.getLong("duanban_leve_id").longValue() && duanbanConfigFormMapTemp.getInt("duanban_item_tz_count")<=physicalExaminationResultFormMapTemp.getLong("leveCount").intValue()){
                            logger.info("原等级："+physicalExaminationMainReportFormMap.getLong("level")+"------原得分："+physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").doubleValue());
                            physicalExaminationMainReportFormMap.put("check_total_score",physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").add(duanbanConfigFormMapTemp.getBigDecimal("tz_rout")));
                            logger.info("原等级："+physicalExaminationMainReportFormMap.getLong("level")+"------原得分："+physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").doubleValue());
                        }
                    }
                }
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

        //todo 查询当前用户
        CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id",recordFormMap.getLong("custom_id").toString(),CustomInfoFormMap.class);
        if(null == customInfoFormMap){
            throw new Exception("无效用户!");
        }

        //todo 获取所有的疾病风险(根据用户性别，查询当前性别的疾病风险项)
        SickRiskItemFormMap sickRiskItemFormMapTp = new SickRiskItemFormMap();
        sickRiskItemFormMapTp.put("with_sex",customInfoFormMap.getInt("sex"));
        List<SickRiskItemFormMap> sickRiskItemFormMapList =  sickRiskItemMapper.findItemsWithSex(sickRiskItemFormMapTp);
        if(CollectionUtils.isEmpty(sickRiskItemFormMapList)){
            throw new Exception("没有配置疾病项");
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
                        bigItemIds.append(sickItem.getLong("check_item_id")).append(",");
                    }else{
                        smallItemIds.append(sickItem.getLong("check_item_id")).append(",");
                    }
                }
            }

            //todo 获取当前用户的检测结果记录

            List<PhysicalExaminationSickRiskResultFormMap> physicalExaminationSickRiskResultFormMapList = new ArrayList<PhysicalExaminationSickRiskResultFormMap>();

            PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
            if(StringUtils.isNotBlank(smallItemIds.toString())){
                physicalExaminationResultFormMap.put("where","where examination_record_id="+recordFormMap.getLong("id")+" and  small_item_id in ("+smallItemIds.toString().substring(0,smallItemIds.toString().lastIndexOf(","))+") order by id desc");
                List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = physicalExaminationResultMapper.findByWhere(physicalExaminationResultFormMap);
                if(CollectionUtils.isNotEmpty(physicalExaminationResultFormMapList)){
                    for(PhysicalExaminationResultFormMap item:physicalExaminationResultFormMapList){
                        //todo 计算检测小项的风险值
                        Map<String,BigDecimal> sickReskMap = this.calSickRiskVal(item.getBigDecimal("item_score"),item.getLong("small_item_id"),sickRiskFormMapList);
                        PhysicalExaminationSickRiskResultFormMap physicalExaminationSickRiskResultFormMap = new PhysicalExaminationSickRiskResultFormMap();
                        physicalExaminationSickRiskResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
                        physicalExaminationSickRiskResultFormMap.put("check_item_id",item.getLong("small_item_id"));
                        physicalExaminationSickRiskResultFormMap.put("check_item_type",2);
                        physicalExaminationSickRiskResultFormMap.put("sick_risk_item_id",sickRiskItemFormMap.getLong("id"));
                        physicalExaminationSickRiskResultFormMap.put("risk_rout",(BigDecimal)sickReskMap.get("rout"));
                        physicalExaminationSickRiskResultFormMap.put("score",(BigDecimal)sickReskMap.get("sickRiskValue") );
//                        physicalExaminationSickRiskResultMapper.addEntity(physicalExaminationSickRiskResultFormMap);
                        physicalExaminationSickRiskResultFormMapList.add(physicalExaminationSickRiskResultFormMap);
                    }
                }
            }


            if(StringUtils.isNotBlank(bigItemIds.toString())){
                PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = new PhysicalExaminationBigResultFormMap();
                physicalExaminationBigResultFormMap.put("where","where examination_record_id="+recordFormMap.getLong("id")+" and  big_item_id in ("+bigItemIds.toString().substring(0,bigItemIds.toString().lastIndexOf(","))+") order by id desc");
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
                        physicalExaminationSickRiskResultFormMap.put("risk_rout",(BigDecimal) sickReskMap.get("rout"));
                        physicalExaminationSickRiskResultFormMap.put("score",(BigDecimal)sickReskMap.get("sickRiskValue"));
//                        physicalExaminationSickRiskResultMapper.addEntity(physicalExaminationSickRiskResultFormMap);
                        physicalExaminationSickRiskResultFormMapList.add(physicalExaminationSickRiskResultFormMap);
                    }

                }
            }


            if(CollectionUtils.isEmpty(physicalExaminationSickRiskResultFormMapList)){
                continue;
            }

            List<PhysicalExaminationSickRiskResult> physicalExaminationSickRiskResultList = new ArrayList<PhysicalExaminationSickRiskResult>();
            for(PhysicalExaminationSickRiskResultFormMap physicalExaminationSickRiskResultFormMap:physicalExaminationSickRiskResultFormMapList){


                PhysicalExaminationSickRiskResult result = new PhysicalExaminationSickRiskResult();
                result.setCheck_item_id(physicalExaminationSickRiskResultFormMap.get("check_item_id")==null?null:physicalExaminationSickRiskResultFormMap.getLong("check_item_id"));
                result.setCheck_item_type(physicalExaminationSickRiskResultFormMap.get("check_item_type")==null?null:physicalExaminationSickRiskResultFormMap.getInt("check_item_type"));
                result.setExamination_record_id(physicalExaminationSickRiskResultFormMap.get("examination_record_id")==null?null:physicalExaminationSickRiskResultFormMap.getLong("examination_record_id"));
                result.setRisk_rout(physicalExaminationSickRiskResultFormMap.get("risk_rout")==null?null:physicalExaminationSickRiskResultFormMap.getBigDecimal("risk_rout"));
                result.setScore(physicalExaminationSickRiskResultFormMap.get("score")==null?null:physicalExaminationSickRiskResultFormMap.getBigDecimal("score"));
                result.setSick_risk_item_id(physicalExaminationSickRiskResultFormMap.get("sick_risk_item_id")==null?null:physicalExaminationSickRiskResultFormMap.getLong("sick_risk_item_id"));
                physicalExaminationSickRiskResultList.add(result);
            }
            if(CollectionUtils.isNotEmpty(physicalExaminationSickRiskResultList)){
                physicalExaminationSickRiskResultMapper.saveBatchResult(physicalExaminationSickRiskResultList);
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

            if(physicalExaminationSickRiskMainResultFormMap.getLong("risk_leve") == null ){//如果风险等级计算出来是空的，这设置为无风险等级
                physicalExaminationSickRiskMainResultFormMap.put("risk_leve",6l);
            }

            physicalExaminationSickRiskMainResultMapper.addEntity(physicalExaminationSickRiskMainResultFormMap);


        }

    }


    /**
     * 删除已经生成的数据x
     * @throws Exception
     */
    public void deleteGenedData(PhysicalExaminationRecordFormMap recordFormMap) throws Exception {
        //todo 删除检测大项数据
        physicalExaminationBigResultMapper.deleteByAttribute("examination_record_id",recordFormMap.get("id").toString(),PhysicalExaminationBigResultFormMap.class);

        //todo 删除主报告记录
        physicalExaminationMainReportMapper.deleteByAttribute("examination_record_id",recordFormMap.get("id").toString(),PhysicalExaminationMainReportFormMap.class);

        //todo 删除疾病风险记录
        physicalExaminationSickRiskMainResultMapper.deleteByAttribute("examination_record_id",recordFormMap.get("id").toString(),PhysicalExaminationSickRiskMainResultFormMap.class);


        physicalExaminationSickRiskResultMapper.deleteByAttribute("examination_record_id",recordFormMap.get("id").toString(),PhysicalExaminationSickRiskResultFormMap.class);
    }

    public void genCheckResultByChosen(PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap) throws Exception {
        CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id", physicalExaminationRecordFormMap.get("custom_id").toString(),CustomInfoFormMap.class);
        if(null == customInfoFormMap){
            throw new Exception("无此用户!");
        }
        // 获取最后一次检测记录
        PhysicalExaminationRecordFormMap lastRecordMap = new PhysicalExaminationRecordFormMap();
        lastRecordMap.put("custom_id",physicalExaminationRecordFormMap.getLong("custom_id"));
        lastRecordMap.put("exclude_id",physicalExaminationRecordFormMap.getLong("id"));
        lastRecordMap = physicalExaminationRecordMapper.findLastTeastRecord(lastRecordMap);


        int resultCount = 0;
        if(lastRecordMap!=null&&lastRecordMap.get("id")!=null){
            resultCount = physicalExaminationResultMapper.resultSizeByRecordid(lastRecordMap.getLong("id"));
        }
        if(lastRecordMap!=null && resultCount >0){
            getCheckSmallItemResultByHistory(lastRecordMap.getLong("id"),physicalExaminationRecordFormMap.getLong("id"));
        }else {
            genCheckSmallItemResult(customInfoFormMap, physicalExaminationRecordFormMap );
        }

        genCheckResult(physicalExaminationRecordFormMap);
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
        BigDecimal score = new BigDecimal(0);
        /*if(sumSickRiskValue.doubleValue()<=65.5){
            //todo 如果几率总和小于65.5则，生成93-98的随机数
            int max = 98*1000;
            int min = 93*1000;
            int randomNumber = (int) Math.round(Math.random()*(max-min)+min);
            score = new BigDecimal(randomNumber/1000d);
        }else if(sumSickRiskValue.doubleValue()>80){
            //todo 如果几率和大于80，则生成5-30的随机数
            int max = 30*1000;
            int min = 5*1000;
            int randomNumber = (int) Math.round(Math.random()*(max-min)+min);
            score = new BigDecimal(randomNumber/1000d);
        }else {
            score = new BigDecimal(0.6).add(new BigDecimal(80).subtract(sumSickRiskValue).multiply(new BigDecimal(2.5)).multiply(new BigDecimal(1)));
        }*/
        score = new BigDecimal(60).add(new BigDecimal(80).subtract(sumSickRiskValue).multiply(new BigDecimal(2.5)).multiply(new BigDecimal(1)));
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
                break;
            }
        }
        Map<String,BigDecimal> resultMap = new HashMap<String, BigDecimal>();
        resultMap.put("sickRiskValue",sickRiskValue);
        resultMap.put("rout",rout);
        return resultMap;
    }
}
