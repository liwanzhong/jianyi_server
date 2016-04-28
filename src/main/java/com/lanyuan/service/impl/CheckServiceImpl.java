package com.lanyuan.service.impl;

import com.lanyuan.entity.*;
import com.lanyuan.mapper.*;
import com.lanyuan.service.ICheckService;
import com.lanyuan.util.AgeCal;
import org.apache.commons.collections.CollectionUtils;
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
        List<CheckSmallItemFormMap> checkSmallItemFormMapList = getCustomerCheckSmallItemsList(customBelonetoEntFormMap.get("custom_id").toString());
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
        }

        return;
    }


    @Inject
    private CustomInfoMapper customInfoMapper;


    /**
     * 生成检测结果
     * @param recordFormMap
     * @throws Exception
     */
    public void genCheckResult(PhysicalExaminationRecordFormMap recordFormMap) throws Exception {

        CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id",recordFormMap.getLong("custom_id").toString(),CustomInfoFormMap.class);
        if(null == customInfoFormMap){
            throw new Exception("无此用户!");
        }

        //获取用户检测小项检测结果列表
        PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
        physicalExaminationResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
        List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = physicalExaminationResultMapper.findByNames(physicalExaminationResultFormMap);

        if(CollectionUtils.isEmpty(physicalExaminationResultFormMapList)){
            throw new Exception("还没有生成检测数据！");
        }

        List<CheckSmallItemFormMap> checkSmallItemFormMapList = getCustomerCheckSmallItemsList(recordFormMap.getLong("custom_id").toString());
        if(CollectionUtils.isEmpty(checkSmallItemFormMapList)){
            throw new Exception("还没有生成检测数据！");
        }

        List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(new CfPingfenLeveFormMap());
        if(CollectionUtils.isEmpty(cfPingfenLeveFormMapList)){
            throw new Exception("评分等级未配置！");
        }

        //todo 循环检测结果，计算各项内容，并更新
        for(PhysicalExaminationResultFormMap checkSmallItemResult:physicalExaminationResultFormMapList){

            for(CheckSmallItemFormMap checkSmallItemFormMap:checkSmallItemFormMapList){
                if(checkSmallItemFormMap.getLong("").longValue() == checkSmallItemResult.getLong("small_item_id")){
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

                    BigDecimal Sco = B.multiply(checkSmallItemFormMap.getBigDecimal("quanzhong"));
                    checkSmallItemResult.put("quanzhong_score",Sco);

                    physicalExaminationResultMapper.addEntity(checkSmallItemResult);
                    break;
                }

            }
        }


        if(CollectionUtils.isNotEmpty(checkSmallItemFormMapList)){

            // 获取评分标准



            Map<Long,List<PhysicalExaminationResultFormMap>> itemMap =  new HashMap<Long, List<PhysicalExaminationResultFormMap>>();

            for(CheckSmallItemFormMap checkSmallItemFormMap:checkSmallItemFormMapList){









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
        }
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

            List<PhysicalExaminationSickRiskResultFormMap> physicalExaminationSickRiskResultFormMapList = new ArrayList<PhysicalExaminationSickRiskResultFormMap>();

            PhysicalExaminationResultFormMap physicalExaminationResultFormMap = new PhysicalExaminationResultFormMap();
            physicalExaminationResultFormMap.put("where","examination_record_id="+recordFormMap.getLong("id")+" and  small_item_id in "+smallItemIds.toString()+" order by id desc");
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
                    physicalExaminationSickRiskResultFormMapList.add(physicalExaminationSickRiskResultFormMap);
                }
            }

            PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = new PhysicalExaminationBigResultFormMap();
            physicalExaminationBigResultFormMap.put("where","examination_record_id="+recordFormMap.getLong("id")+" and  big_item_id in "+bigItemIds.toString()+" order by id desc");
            List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findByWhere(physicalExaminationBigResultFormMap);
            if(CollectionUtils.isNotEmpty(physicalExaminationBigResultFormMapList)){
                for(PhysicalExaminationBigResultFormMap item:physicalExaminationBigResultFormMapList){
                    //todo 计算检测大项的风险值
                    Map<String,BigDecimal> sickReskMap = this.calSickRiskVal(item.getBigDecimal("check_score"),item.getLong("big_item_id"),sickRiskFormMapList);
                    PhysicalExaminationSickRiskResultFormMap physicalExaminationSickRiskResultFormMap = new PhysicalExaminationSickRiskResultFormMap();
                    physicalExaminationSickRiskResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
                    physicalExaminationSickRiskResultFormMap.put("check_item_id",item.getLong("big_item_id"));
                    physicalExaminationSickRiskResultFormMap.put("check_item_type",1);
                    physicalExaminationSickRiskResultFormMap.put("sick_risk_item_id",sickRiskItemFormMap.getLong("sick_risk_id"));
                    physicalExaminationSickRiskResultFormMap.put("risk_rout",(BigDecimal)sickReskMap.get("sickRiskValue"));
                    physicalExaminationSickRiskResultFormMap.put("score",(BigDecimal)sickReskMap.get("rout"));
                    physicalExaminationSickRiskResultFormMapList.add(physicalExaminationSickRiskResultFormMap);
                    //todo 保存检测风险值
                }


            }

            if(CollectionUtils.isNotEmpty(physicalExaminationSickRiskResultFormMapList)){
                physicalExaminationSickRiskResultMapper.batchSave(physicalExaminationSickRiskResultFormMapList);
            }



            //todo 根据大小项的风险值，计算疾病风险得分
            PhysicalExaminationSickRiskMainResultFormMap physicalExaminationSickRiskMainResultFormMap = this.calSickRiskScore(physicalExaminationSickRiskResultFormMapList);
            physicalExaminationSickRiskMainResultFormMap.put("examination_record_id",recordFormMap.getLong("id"));
            physicalExaminationSickRiskMainResultFormMap.put("sick_risk_item_id",sickRiskItemFormMap.getLong("sick_risk_id"));


            //todo 生成评分等级
            List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(new CfPingfenLeveFormMap());
            if(CollectionUtils.isEmpty(cfPingfenLeveFormMapList)){
                throw new Exception("没有配置得分等级!");
            }
            for(CfPingfenLeveFormMap cfPingfenLeveFormMap:cfPingfenLeveFormMapList){
                if(cfPingfenLeveFormMap.getBigDecimal("pingfen_min").doubleValue()<physicalExaminationSickRiskMainResultFormMap.getBigDecimal("risk_score").doubleValue() && cfPingfenLeveFormMap.getBigDecimal("pingfen_max").doubleValue()>=physicalExaminationSickRiskMainResultFormMap.getBigDecimal("risk_score").doubleValue()){
                    physicalExaminationSickRiskMainResultFormMap.put("risk_leve",cfPingfenLeveFormMap.getLong("id"));
                    break;
                }
            }
            physicalExaminationSickRiskMainResultMapper.addEntity(physicalExaminationSickRiskMainResultFormMap);


        }

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
        physicalExaminationSickRiskMainResultFormMap.put("risk_score",score);
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
