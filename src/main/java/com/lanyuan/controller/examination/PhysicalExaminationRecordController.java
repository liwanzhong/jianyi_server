package com.lanyuan.controller.examination;

import com.alibaba.fastjson.JSON;
import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.mapper.*;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.AgeCal;
import com.lanyuan.util.Common;
import com.lanyuan.util.PropertiesUtils;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.LineChartJSONVO;
import com.lanyuan.vo.PageFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liwanzhong on 2016/2/21.
 */
@Controller
@RequestMapping("/examination/physicalExamination/")
public class PhysicalExaminationRecordController extends BaseController {
    @Inject
    private PhysicalExaminationRecordMapper physicalExaminationRecordMapper;

    @Inject
    private PhysicalExaminationBigResultMapper physicalExaminationBigResultMapper;

    @Autowired
    private CfPingfenLeveMapper cfPingfenLeveMapper;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return Common.BACKGROUND_PATH + "/examination/physicalexaminationrecord/list";
    }


    @RequestMapping("client_list")
    public String clientList(Model model) throws Exception {
        return Common.BACKGROUND_PATH + "/front/examination/list";
    }


    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
        return Common.BACKGROUND_PATH + "/front/examination/add";
    }


    @Autowired
    private BmiLeveConfigMapper bmiLeveConfigMapper;


    @Autowired
    private BmiNormalConfigMapper bmiNormalConfigMapper;


    @Autowired
    private ZongpingLeveDescConfigMapper zongpingLeveDescConfigMapper;


    @RequestMapping("report")
    public String report(Model model) throws Exception {
        try{
            PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
            List<PhysicalExaminationRecordFormMap> physicalExaminationRecordFormMapList = physicalExaminationRecordMapper.findExaminationRecordCustomInfo(physicalExaminationRecordFormMap);
            if(CollectionUtils.isEmpty(physicalExaminationRecordFormMapList)){
                throw new Exception("参数异常");
            }
            // 计算年龄
            physicalExaminationRecordFormMapList.get(0).put("age",AgeCal.getAge(physicalExaminationRecordFormMapList.get(0).getDate("birthday")));
            model.addAttribute("physicalExaminationRecordFormMap",physicalExaminationRecordFormMapList.get(0));

            physicalExaminationRecordFormMap = physicalExaminationRecordFormMapList.get(0);

            PhysicalExaminationMainReportFormMap physicalExaminationMainReportFormMap = getFormMap(PhysicalExaminationMainReportFormMap.class);
            physicalExaminationMainReportFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
            physicalExaminationMainReportFormMap = physicalExaminationMainReportMapper.findMainResultWithColor(physicalExaminationMainReportFormMap);
            model.addAttribute("physicalExaminationMainReportFormMap",physicalExaminationMainReportFormMap);

            PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = getFormMap(PhysicalExaminationBigResultFormMap.class);
            physicalExaminationBigResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
            List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findAllByOrderby(physicalExaminationBigResultFormMap);


            // 计算每个等级的有多少个
            //获取所有等级
            List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(getFormMap(CfPingfenLeveFormMap.class));
            Map<Long,Integer> leveCountGroupBy = new HashMap<Long, Integer>();
            for(CfPingfenLeveFormMap cfPingfenLeveFormMap:cfPingfenLeveFormMapList){
                Integer count = 0;
                for(PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMapTem:physicalExaminationBigResultFormMapList){
                    if(physicalExaminationBigResultFormMapTem.getLong("leve_id").longValue() == cfPingfenLeveFormMap.getLong("id")){
                        count++;
                    }
                }
                leveCountGroupBy.put(cfPingfenLeveFormMap.getLong("id"),count);
            }
            model.addAttribute("leveCountGroupBy",leveCountGroupBy);

            model.addAttribute("physicalExaminationBigResultFormMapList",physicalExaminationBigResultFormMapList);

            //todo 计算bmi
            Double bmi = physicalExaminationRecordFormMap.getDouble("weight")/(((double)physicalExaminationRecordFormMap.getInt("body_height")/100)*((double)physicalExaminationRecordFormMap.getInt("body_height")/100));

            //todo 通过bmi查询当前的肥胖标准状态
            BmiLeveConfigFormMap bmiLeveConfigFormMap = getFormMap(BmiLeveConfigFormMap.class);
            bmiLeveConfigFormMap.put("bmi",bmi);
            List<BmiLeveConfigFormMap> bmiLeveConfigFormMapList =  bmiLeveConfigMapper.findFixConfig(bmiLeveConfigFormMap);
            if(CollectionUtils.isNotEmpty(bmiLeveConfigFormMapList)){
                bmiLeveConfigFormMap = bmiLeveConfigFormMapList.get(0);
            }

            model.addAttribute("bmiLeveConfigFormMap",bmiLeveConfigFormMap);
            model.addAttribute("bmi",bmi);


            //todo 计算当前用户的标准体重范围
            BmiNormalConfigFormMap bmiNormalConfigFormMap = getFormMap(BmiNormalConfigFormMap.class);
            bmiNormalConfigFormMap.put("sex",physicalExaminationRecordFormMap.getInt("sex"));
            bmiNormalConfigFormMap = bmiNormalConfigMapper.findSexNormalConfig(bmiNormalConfigFormMap);

            BigDecimal maxWeight = bmiNormalConfigFormMap.getBigDecimal("max_bmi").multiply(new BigDecimal((((double)physicalExaminationRecordFormMap.getInt("body_height")/100))).multiply(new BigDecimal((((double)physicalExaminationRecordFormMap.getInt("body_height")/100)))));
            BigDecimal minWeight = bmiNormalConfigFormMap.getBigDecimal("min_bmi").multiply(new BigDecimal((((double)physicalExaminationRecordFormMap.getInt("body_height")/100))).multiply(new BigDecimal((((double)physicalExaminationRecordFormMap.getInt("body_height")/100)))));

            model.addAttribute("maxWeight",maxWeight.setScale(1,BigDecimal.ROUND_HALF_UP));
            model.addAttribute("minWeight",minWeight.setScale(1,BigDecimal.ROUND_HALF_UP));



            //todo 加载总评
            ZongpingLeveDescConfigFormMap zongpingLeveDescConfigFormMap = getFormMap(ZongpingLeveDescConfigFormMap.class);
            zongpingLeveDescConfigFormMap.put("check_total_score",physicalExaminationMainReportFormMap.getBigDecimal("check_total_score"));
            zongpingLeveDescConfigFormMap = zongpingLeveDescConfigMapper.findFixedItem(zongpingLeveDescConfigFormMap);
            model.addAttribute("zongpingLeveDescConfigFormMap",zongpingLeveDescConfigFormMap);

            PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMapTes = getFormMap(PhysicalExaminationBigResultFormMap.class);
            physicalExaminationBigResultFormMapTes.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
            List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapListTest = physicalExaminationBigResultMapper.findCheckResultOrderByCheckScoreAsc(physicalExaminationBigResultFormMapTes);

            String zuicha = physicalExaminationBigResultFormMapListTest.get(0).getStr("name")+"、"+physicalExaminationBigResultFormMapListTest.get(1).getStr("name")+"、"+physicalExaminationBigResultFormMapListTest.get(2).getStr("name");
            String cicha = physicalExaminationBigResultFormMapListTest.get(3).getStr("name")+"、"+physicalExaminationBigResultFormMapListTest.get(4).getStr("name");

            model.addAttribute("zuicha",zuicha);
            model.addAttribute("cicha",cicha);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Common.BACKGROUND_PATH + "/front/examination/report_version2/index";
    }


    @Autowired
    private SickRiskLeveMapper sickRiskLeveMapper;

    @Autowired
    private PhysicalExaminationSickRiskMainResultMapper physicalExaminationSickRiskMainResultMapper;

    @RequestMapping("sick_risk")
    public String sickRiskSyn(Model model) throws Exception {
        PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
        List<PhysicalExaminationRecordFormMap> physicalExaminationRecordFormMapList = physicalExaminationRecordMapper.findByNames(physicalExaminationRecordFormMap);
        if(CollectionUtils.isEmpty(physicalExaminationRecordFormMapList)){
            throw new Exception("参数异常");
        }
        physicalExaminationRecordFormMap = physicalExaminationRecordFormMapList.get(0);
        //todo 加载疾病风险结果
        PhysicalExaminationSickRiskMainResultFormMap physicalExaminationSickRiskMainResultFormMap = getFormMap(PhysicalExaminationSickRiskMainResultFormMap.class);
        physicalExaminationSickRiskMainResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
        List<PhysicalExaminationSickRiskMainResultFormMap> physicalExaminationSickRiskMainResultFormMapList = physicalExaminationSickRiskMainResultMapper.findAllShowInReportSickRiskList(physicalExaminationSickRiskMainResultFormMap);
        if(CollectionUtils.isNotEmpty(physicalExaminationSickRiskMainResultFormMapList)){
            //todo 加载疾病风险等级所有列表
            List<SickRiskLeveFormMap> sickRiskLeveFormMapList = sickRiskLeveMapper.findByNames(getFormMap(SickRiskLeveFormMap.class));
            Map<Long,List<PhysicalExaminationSickRiskMainResultFormMap>> sickLeveCountMap = new HashMap<Long, List<PhysicalExaminationSickRiskMainResultFormMap>>();
            for(SickRiskLeveFormMap sickRiskLeveFormMap:sickRiskLeveFormMapList){
                List<PhysicalExaminationSickRiskMainResultFormMap> currentLeveList = new ArrayList<PhysicalExaminationSickRiskMainResultFormMap>();
                for(PhysicalExaminationSickRiskMainResultFormMap physicalExaminationSickRiskMainResultFormMapTemp:physicalExaminationSickRiskMainResultFormMapList){
                    if(physicalExaminationSickRiskMainResultFormMapTemp.get("risk_leve")!=null){
                        if(physicalExaminationSickRiskMainResultFormMapTemp.getLong("risk_leve") == sickRiskLeveFormMap.getLong("id").longValue()){
                            currentLeveList.add(physicalExaminationSickRiskMainResultFormMapTemp);
                        }
                    }

                }
                sickLeveCountMap.put(sickRiskLeveFormMap.getLong("id"),currentLeveList);
            }

            model.addAttribute("sickLeveCountMap",sickLeveCountMap);
        }
        model.addAttribute("physicalExaminationRecordFormMap",physicalExaminationRecordFormMap);

        return Common.BACKGROUND_PATH + "/front/examination/report_version2/syn";
    }


    @RequestMapping("report_pdf_gen")
    public String report_pdf_gen(Model model) throws Exception {
        PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
        List<PhysicalExaminationRecordFormMap> physicalExaminationRecordFormMapList = physicalExaminationRecordMapper.findExaminationRecordCustomInfo(physicalExaminationRecordFormMap);
        if(CollectionUtils.isEmpty(physicalExaminationRecordFormMapList)){
            throw new Exception("参数异常");
        }
        model.addAttribute("physicalExaminationRecordFormMap",physicalExaminationRecordFormMapList.get(0));

        PhysicalExaminationMainReportFormMap physicalExaminationMainReportFormMap = physicalExaminationMainReportMapper.findbyFrist("examination_record_id",physicalExaminationRecordFormMap.getStr("id"),PhysicalExaminationMainReportFormMap.class);
        model.addAttribute("physicalExaminationMainReportFormMap",physicalExaminationMainReportFormMap);

        PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = getFormMap(PhysicalExaminationBigResultFormMap.class);
        physicalExaminationBigResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getStr("id"));
        List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findByNames(physicalExaminationBigResultFormMap);

        model.addAttribute("physicalExaminationBigResultFormMapList",physicalExaminationBigResultFormMapList);
        return Common.BACKGROUND_PATH + "/front/examination/report_pdf_gen";
    }


    @RequestMapping("report_big_item")
    public String report_big_item(Model model, @RequestParam(value = "pageNow",defaultValue = "1")String pageNow, @RequestParam(value = "pageSize",defaultValue = "1") String pageSize,  @RequestParam(value = "recordId",required = true)Long recordId, Long bigItemId) throws Exception {

        PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = getFormMap(PhysicalExaminationBigResultFormMap.class);
        physicalExaminationBigResultFormMap=toFormMap(physicalExaminationBigResultFormMap, pageNow, pageSize,physicalExaminationBigResultFormMap.getStr("orderby"));
        physicalExaminationBigResultFormMap.put("examination_record_id",recordId);

        pageView.setRecords(physicalExaminationBigResultMapper.findAllByOrderby(physicalExaminationBigResultFormMap));
        model.addAttribute("pageView",pageView);
        model.addAttribute("physicalExaminationBigResultFormMap",pageView.getRecords().get(0));

        try{
            List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = physicalExaminationResultMapper.findItemCheckResultList(recordId,((PhysicalExaminationBigResultFormMap)pageView.getRecords().get(0)).getLong("big_item_id"));
            model.addAttribute("physicalExaminationResultFormMapList",physicalExaminationResultFormMapList);

            //todo 判断图表类型是不是折线图，如果是折线图，多返回一个折线图的json list
            if(((PhysicalExaminationBigResultFormMap) pageView.getRecords().get(0)).getInt("charts_item")==7){
                List<LineChartJSONVO> lineChartJSONVOList = new ArrayList<LineChartJSONVO>();
                for(PhysicalExaminationResultFormMap item:physicalExaminationResultFormMapList){//遍历检测结果项，设置json对象
                    LineChartJSONVO lineChartJSONVO = new LineChartJSONVO();
                    if(item.getLong("tzed_leve_id_id")!=null){
                        lineChartJSONVO.setAnchorBgColor(item.getStr("tzed_show_color"));
                    }else{
                        lineChartJSONVO.setAnchorBgColor(item.getStr("org_show_color"));
                    }
                    lineChartJSONVO.setAnchorBorderColor("");
                    lineChartJSONVO.setLabel(item.getStr("name"));
                    lineChartJSONVO.setValue(item.getBigDecimal("item_score").setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()+"");

                    lineChartJSONVOList.add(lineChartJSONVO);
                }
                if(CollectionUtils.isNotEmpty(lineChartJSONVOList)){
                    model.addAttribute("chartJSON", JSON.toJSONString(lineChartJSONVOList));
                }
            }


            List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(getFormMap(CfPingfenLeveFormMap.class));
            Map<Long,Integer> leveCountGroupBy = new HashMap<Long, Integer>();
            for(CfPingfenLeveFormMap cfPingfenLeveFormMap:cfPingfenLeveFormMapList){
                Integer count = 0;
                for(PhysicalExaminationResultFormMap physicalExaminationResultFormMap:physicalExaminationResultFormMapList){
                    if((physicalExaminationResultFormMap.getLong("tzed_leve_id_id")!=null?physicalExaminationResultFormMap.getLong("tzed_leve_id_id").longValue():physicalExaminationResultFormMap.getLong("orgin_leve_id_id").longValue()) == cfPingfenLeveFormMap.getLong("id")){
                        count++;
                    }
                }
                leveCountGroupBy.put(cfPingfenLeveFormMap.getLong("id"),count);
            }
            model.addAttribute("leveCountGroupBy",leveCountGroupBy);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //todo 加载评分等级
        List<CfPingfenLeveFormMap> cfPingfenLeveFormMapList = cfPingfenLeveMapper.findByNames(getFormMap(CfPingfenLeveFormMap.class));
        model.addAttribute("cfPingfenLeveFormMapList",cfPingfenLeveFormMapList);

        return Common.BACKGROUND_PATH + "/front/examination/report_version2/item";
    }



    @RequestMapping("report_big_item_pdf_gen")
    public String report_big_item_pdf_gen(Model model,@RequestParam(value = "recordId",required = true)Long recordId, @RequestParam(value = "bigItemId",required = true) Long bigItemId) throws Exception {

        PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = getFormMap(PhysicalExaminationBigResultFormMap.class);
        physicalExaminationBigResultFormMap.put("examination_record_id",recordId);
        physicalExaminationBigResultFormMap.put("big_item_id",bigItemId);

        List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList =  physicalExaminationBigResultMapper.findAllByOrderby(physicalExaminationBigResultFormMap);
        if(CollectionUtils.isNotEmpty(physicalExaminationBigResultFormMapList)){
            model.addAttribute("physicalExaminationBigResultFormMap",physicalExaminationBigResultFormMapList.get(0));
        }


        try{
            List<PhysicalExaminationResultFormMap> physicalExaminationResultFormMapList = physicalExaminationResultMapper.findItemCheckResultList(recordId,physicalExaminationBigResultFormMapList.get(0).getLong("big_item_id"));
            model.addAttribute("physicalExaminationResultFormMapList",physicalExaminationResultFormMapList);
        }catch (Exception ex){
            ex.printStackTrace();
        }


        return Common.BACKGROUND_PATH + "/front/examination/report_big_item_pdf_gen";
    }



    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
        PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
        physicalExaminationRecordFormMap.put("orderby","id DESC");
        physicalExaminationRecordFormMap=toFormMap(physicalExaminationRecordFormMap, pageNow, pageSize,physicalExaminationRecordFormMap.getStr("orderby"));

        Session session = SecurityUtils.getSubject().getSession();
        UserFormMap userFormMap = (UserFormMap)session.getAttribute("userSession");
        if(userFormMap == null){
            throw  new Exception("用户未登陆!");
        }
        physicalExaminationRecordFormMap.put("organization_id",userFormMap.getLong("organization_id"));
        pageView.setRecords(physicalExaminationRecordMapper.findEnterprisePage_Front(physicalExaminationRecordFormMap));
        return pageView;
    }



    @ResponseBody
    @RequestMapping("dataGrid")
    @SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    public Grid dataGrid(PageFilter ph)throws Exception {
        Grid grid = new Grid();
        PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
        physicalExaminationRecordFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
        physicalExaminationRecordFormMap=toFormMap(physicalExaminationRecordFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),physicalExaminationRecordFormMap.getStr("orderby"));
        List<PhysicalExaminationRecordFormMap> userFormMapList =physicalExaminationRecordMapper.findEnterprisePage(physicalExaminationRecordFormMap);
        if(CollectionUtils.isNotEmpty(userFormMapList)){
            grid.setRows(userFormMapList);
        }
        PageView pageView = (PageView) physicalExaminationRecordFormMap.get("paging");
        grid.setTotal(pageView.getRowCount());
        return grid;

    }

    @Inject
    private PhysicalExaminationMainReportMapper physicalExaminationMainReportMapper;





    @Inject
    private PhysicalExaminationResultMapper physicalExaminationResultMapper;

    @RequestMapping("result")
    @SystemLog(module="用户管理",methods="加载用户列表")
    public String result(Model model,String recordid)throws Exception {
        try{
            PhysicalExaminationRecordFormMap recordFormMap = physicalExaminationRecordMapper.findbyFrist("id",recordid,PhysicalExaminationRecordFormMap.class);
            model.addAttribute("record",recordFormMap);

            PhysicalExaminationMainReportFormMap physicalExaminationMainReportFormMap = physicalExaminationMainReportMapper.findbyFrist("examination_record_id",recordFormMap.get("id").toString(),PhysicalExaminationMainReportFormMap.class);
            model.addAttribute("physicalExaminationMainReportFormMap",physicalExaminationMainReportFormMap);

            List<PhysicalExaminationBigResultFormMap> bigResultFormMapList = physicalExaminationBigResultMapper.findItemCheckResultList(recordFormMap.getLong("id"));
            model.addAttribute("bigResultFormMapList",bigResultFormMapList);


            List<PhysicalExaminationResultFormMap> resultList =physicalExaminationResultMapper .findItemCheckResultList(recordFormMap.getLong("id"),null);
            model.addAttribute("resultList",resultList);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Common.BACKGROUND_PATH + "/examination/physicalexaminationrecord/checkResult";
    }


    @Autowired
    private CustomInfoMapper customInfoMapper;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd");

    @RequestMapping("/downloadReport")
    public String downloadReport(@RequestParam(value = "recordid",required = true) Long recordid, HttpServletRequest request, HttpServletResponse response) throws Exception{
        try{
            PhysicalExaminationRecordFormMap recordFormMap = physicalExaminationRecordMapper.findbyFrist("id",recordid.toString(),PhysicalExaminationRecordFormMap.class);
            if(recordFormMap==null){
                throw new Exception("无效下载请求!");
            }
            if(recordFormMap.getInt("status")!=4){
                throw new Exception("请求的报告还没有生成!");
            }

            String report_path = recordFormMap.getStr("report_path");

            // 查询用户
            CustomInfoFormMap customInfoFormMap = customInfoMapper.findbyFrist("id",recordFormMap.get("custom_id").toString(),CustomInfoFormMap.class);

            //显示给客户的文件名（保存的文件名）
            String fileName = customInfoFormMap.getStr("name")+"_"+simpleDateFormat.format(recordFormMap.getDate("check_time"))+".pdf";


            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"),"ISO8859-1"));//这里做一下取byte的转码，否则中文则乱码或者不显示

            InputStream inputStream =null;
            OutputStream os = null;
            try {
                StringBuffer pdfFilePath = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_PDF_SAVED_PAHT));
                pdfFilePath.append(File.separator);
                pdfFilePath.append(fileDateFormat.format(recordFormMap.getDate("check_time")));
                pdfFilePath.append(File.separator);
                pdfFilePath.append(recordFormMap.getLong("id"));
                File pdfFile = new File(pdfFilePath.toString() + File.separator + report_path);
                if(!pdfFile.exists()||!pdfFile.isFile()){
                    throw new Exception("下载异常，文件不存在！");
                }
                inputStream = new FileInputStream(pdfFile);

                os = response.getOutputStream();
                byte[] b = new byte[2048];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    os.write(b, 0, length);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(os!=null){
                    os.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }















}
