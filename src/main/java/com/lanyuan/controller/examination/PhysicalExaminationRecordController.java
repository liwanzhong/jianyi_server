package com.lanyuan.controller.examination;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.mapper.PhysicalExaminationBigResultMapper;
import com.lanyuan.mapper.PhysicalExaminationMainReportMapper;
import com.lanyuan.mapper.PhysicalExaminationRecordMapper;
import com.lanyuan.mapper.PhysicalExaminationResultMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.AgeCal;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.List;

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


    @RequestMapping("report")
    public String report(Model model) throws Exception {
        PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
        List<PhysicalExaminationRecordFormMap> physicalExaminationRecordFormMapList = physicalExaminationRecordMapper.findExaminationRecordCustomInfo(physicalExaminationRecordFormMap);
        if(CollectionUtils.isEmpty(physicalExaminationRecordFormMapList)){
            throw new Exception("参数异常");
        }
        //todo 计算年龄
        physicalExaminationRecordFormMapList.get(0).put("age",AgeCal.getAge(physicalExaminationRecordFormMapList.get(0).getDate("birthday")));
        model.addAttribute("physicalExaminationRecordFormMap",physicalExaminationRecordFormMapList.get(0));

        PhysicalExaminationMainReportFormMap physicalExaminationMainReportFormMap = physicalExaminationMainReportMapper.findbyFrist("examination_record_id",physicalExaminationRecordFormMap.getStr("id"),PhysicalExaminationMainReportFormMap.class);
        model.addAttribute("physicalExaminationMainReportFormMap",physicalExaminationMainReportFormMap);

        PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = getFormMap(PhysicalExaminationBigResultFormMap.class);
        physicalExaminationBigResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getStr("id"));
        List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findAllByOrderby(physicalExaminationBigResultFormMap);

        model.addAttribute("physicalExaminationBigResultFormMapList",physicalExaminationBigResultFormMapList);
        return Common.BACKGROUND_PATH + "/front/examination/report";
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
        }catch (Exception ex){
            ex.printStackTrace();
        }


        return Common.BACKGROUND_PATH + "/front/examination/report_item";
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
        PhysicalExaminationRecordFormMap recordFormMap = physicalExaminationRecordMapper.findbyFrist("id",recordid,PhysicalExaminationRecordFormMap.class);
        model.addAttribute("record",recordFormMap);

        PhysicalExaminationMainReportFormMap physicalExaminationMainReportFormMap = physicalExaminationMainReportMapper.findbyFrist("examination_record_id",recordFormMap.get("id").toString(),PhysicalExaminationMainReportFormMap.class);
        model.addAttribute("physicalExaminationMainReportFormMap",physicalExaminationMainReportFormMap);

        List<PhysicalExaminationBigResultFormMap> bigResultFormMapList = physicalExaminationBigResultMapper.findItemCheckResultList(recordFormMap.getLong("id"));
        model.addAttribute("bigResultFormMapList",bigResultFormMapList);


        List<PhysicalExaminationResultFormMap> resultList =physicalExaminationResultMapper .findItemCheckResultList(recordFormMap.getLong("id"),null);
        model.addAttribute("resultList",resultList);


        return Common.BACKGROUND_PATH + "/examination/physicalexaminationrecord/checkResult";
    }















}
