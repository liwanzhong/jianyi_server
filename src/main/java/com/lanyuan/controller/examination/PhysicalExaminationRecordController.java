package com.lanyuan.controller.examination;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.*;
import com.lanyuan.mapper.PhysicalExaminationBigResultMapper;
import com.lanyuan.mapper.PhysicalExaminationMainReportMapper;
import com.lanyuan.mapper.PhysicalExaminationRecordMapper;
import com.lanyuan.mapper.PhysicalExaminationResultMapper;
import com.lanyuan.plugin.PageView;
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
        model.addAttribute("physicalExaminationRecordFormMap",physicalExaminationRecordFormMapList.get(0));

        PhysicalExaminationMainReportFormMap physicalExaminationMainReportFormMap = physicalExaminationMainReportMapper.findbyFrist("examination_record_id",physicalExaminationRecordFormMap.getStr("id"),PhysicalExaminationMainReportFormMap.class);
        model.addAttribute("physicalExaminationMainReportFormMap",physicalExaminationMainReportFormMap);

        PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = getFormMap(PhysicalExaminationBigResultFormMap.class);
        physicalExaminationBigResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getStr("id"));
        List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findByNames(physicalExaminationBigResultFormMap);

        model.addAttribute("physicalExaminationBigResultFormMapList",physicalExaminationBigResultFormMapList);
        return Common.BACKGROUND_PATH + "/front/examination/report";
    }



    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage( String pageNow,String pageSize,String column,String sort) throws Exception {
        PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
        physicalExaminationRecordFormMap=toFormMap(physicalExaminationRecordFormMap, pageNow, pageSize,physicalExaminationRecordFormMap.getStr("orderby"));
        physicalExaminationRecordFormMap.put("column", column);
        physicalExaminationRecordFormMap.put("sort", sort);

        Session session = SecurityUtils.getSubject().getSession();
        UserFormMap userFormMap = (UserFormMap)session.getAttribute("userSession");
        if(userFormMap == null){
            throw  new Exception("用户未登陆!");
        }
        physicalExaminationRecordFormMap.put("organization_id",userFormMap.getLong("organization_id"));
        pageView.setRecords(physicalExaminationRecordMapper.findEnterprisePage(physicalExaminationRecordFormMap));
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
    private PhysicalExaminationBigResultMapper physicalExaminationBigResultMapper;


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


        List<PhysicalExaminationResultFormMap> resultList =physicalExaminationResultMapper .findItemCheckResultList(recordFormMap.getLong("id"));
        model.addAttribute("resultList",resultList);


        return Common.BACKGROUND_PATH + "/examination/physicalexaminationrecord/checkResult";
    }















}
