package com.lanyuan.service.impl;

import com.lanyuan.entity.*;
import com.lanyuan.mapper.*;
import com.lanyuan.service.ICheckService;
import com.lanyuan.task.ReportPDFGenController;
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

    @Autowired
    private ReportPDFGenController reportPDFGenController;



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
            genCheckSmallItemResult(customInfoFormMap, physicalExaminationRecordFormMap );
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

                    /*new Thread(new Runnable() {
                        public void run() {
                            try{
                                Thread.sleep(2000);
                                item.put("status",3);
                                item.put("update_time",dateFormat.format(new Date()));
                                physicalExaminationRecordMapper.editEntity(item);
                                String pdfPath = null;
                                try{
                                    pdfPath = reportPDFGenController.reportGen(item);
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                                item.put("status",4);
                                item.put("report_gentime",dateFormat.format(new Date()));
                                item.put("update_time",dateFormat.format(new Date()));
                                item.put("report_path",pdfPath);
                                physicalExaminationRecordMapper.editEntity(item);
                            }catch (Exception ex){
                                logger.error(ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                    }).start();*/
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
                if(resultFormMap.getBigDecimal("item_score")==null || resultFormMap.getBigDecimal("item_score").doubleValue()<=0){
                    resultFormMap.put("item_score", new BigDecimal(Math.round(Math.random()*(100-60)+60)));
                }else{
                    if((resultFormMap.getBigDecimal("item_score").doubleValue()+randomNumber)>=100){
                        resultFormMap.put("item_score", new BigDecimal(99));
                    }else if ((resultFormMap.getBigDecimal("item_score").doubleValue()+randomNumber)<=60){
                        resultFormMap.put("item_score", new BigDecimal(62));
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

            /*if(CollectionUtils.isNotEmpty(physicalExaminationResultFormMapList)){
                physicalExaminationResultMapper.batchSave(physicalExaminationResultFormMapList);
            }*/

        }

    }







    @Inject
    private BmiCheckItemConfigMapper bmiCheckItemConfigMapper;


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


                    BigDecimal M = n.divide(new BigDecimal(20.0),3, RoundingMode.HALF_UP);
                    checkSmallItemResult.put("in_value_score",M);
                    //随机生成
                    //todo 随机的最大值为==基准高值+区间值
                    int max = checkSmallItemResult.getBigDecimal("gen_max_value").add(n).multiply(new BigDecimal(1000)).intValue();
                    int min = checkSmallItemResult.getBigDecimal("gen_min_value").multiply(new BigDecimal(1000)).intValue();
                    int randomNumber = (int) Math.round(Math.random()*(max-min)+min);

                    BigDecimal A = new BigDecimal(randomNumber/1000d);
                    checkSmallItemResult.put("check_value",A);

                    BigDecimal B = new BigDecimal(100).subtract((A.subtract(n1).divide(M,3,BigDecimal.ROUND_HALF_UP))) ;



                    checkSmallItemResult.put("item_score",B);

                    //根据计算结果判断\
                    for(CfPingfenLeveFormMap item:cfPingfenLeveFormMapList){
                        if(item.getDouble("pingfen_min")<=B.doubleValue() && item.getDouble("pingfen_max")>=B.doubleValue()){
                            checkSmallItemResult.put("orgin_leve_id",item.getLong("id"));
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
                    if(checkBigItemFormMap.getInt("withbmi") == 1){
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


                    if(CollectionUtils.isNotEmpty(bmiCheckItemConfigFormMapList) && bmiCheckItemConfigFormMapList.size()==1){
                        BmiCheckItemConfigFormMap bmiCheckItemConfigFormMapTemp = bmiCheckItemConfigFormMapList.get(0);
                        if(randomNumberTz <=bmiCheckItemConfigFormMapTemp.getBigDecimal("rout").doubleValue()*1000) {
                            checkSmallItemResult.put("tzed_leve_id", bmiCheckItemConfigFormMapTemp.getLong("tz_leve_id"));
                            // 随机生成得分等级范围内的得分值  B
                            for (CfPingfenLeveFormMap temp : cfPingfenLeveFormMapList) {
                                if (temp.getLong("id").longValue() ==  bmiCheckItemConfigFormMapTemp.getLong("tz_leve_id")) {
                                    cfPingfenLeveFormMap = temp;
                                    break;
                                }
                            }
                        }

                    }else if(CollectionUtils.isNotEmpty(cfPingfenRoutFormMapList) && cfPingfenRoutFormMapList.size() ==1){
                        CfPingfenRoutFormMap cfPingfenRoutFormMap = cfPingfenRoutFormMapList.get(0);
                        if(randomNumberTz >=cfPingfenRoutFormMap.getDouble("rout_min")*1000 && randomNumberTz<=cfPingfenRoutFormMap.getDouble("rout_max")*1000) {
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
                    }


                    if(cfPingfenLeveFormMap !=null){
                        //生成随机范围内的得分,作为调整后的得分
                        int tzmax = cfPingfenLeveFormMap.getDouble("pingfen_min").intValue()*1000;
                        int tzmin = cfPingfenLeveFormMap.getDouble("pingfen_max").intValue()*1000;
                        int tzScoreRandom = (int) Math.round(Math.random()*(tzmax-tzmin)+tzmin);
                        BigDecimal tzB = new BigDecimal(tzScoreRandom/1000d);

                        //A ? = (100-B)*M+n1
                        BigDecimal tzA = new BigDecimal(100).subtract(tzB).multiply(M).add(n1);
                        checkSmallItemResult.put("check_value",tzA);
                        checkSmallItemResult.put("item_score",tzB);
                    }

                    //根据调整概率，生成新的功能等级
                    break;
                }

            }
        }
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
                physicalExaminationResultList.add(result);
            }
            if(CollectionUtils.isNotEmpty(physicalExaminationResultList)){
                physicalExaminationResultMapper.saveBatchResult(physicalExaminationResultList);
            }
        }

    }


    @Autowired
    private DuanbanConfigMapper duanbanConfigMapper;


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

                //todo 获取短板项配置
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
                            physicalExaminationMainReportFormMap.put("check_total_score",physicalExaminationMainReportFormMap.getBigDecimal("check_total_score").add(duanbanConfigFormMapTemp.getBigDecimal("tz_rout")));
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
