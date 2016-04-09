package com.lanyuan.controller.examination;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.PhysicalExaminationBigResultFormMap;
import com.lanyuan.entity.PhysicalExaminationMainReportFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;
import com.lanyuan.entity.PhysicalExaminationResultFormMap;
import com.lanyuan.mapper.PhysicalExaminationBigResultMapper;
import com.lanyuan.mapper.PhysicalExaminationMainReportMapper;
import com.lanyuan.mapper.PhysicalExaminationRecordMapper;
import com.lanyuan.mapper.PhysicalExaminationResultMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import com.lanyuan.vo.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

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
