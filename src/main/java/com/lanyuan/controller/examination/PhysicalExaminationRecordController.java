package com.lanyuan.controller.examination;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;
import com.lanyuan.mapper.PhysicalExaminationRecordMapper;
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

    ///examination/physicalexaminationrecord/list

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return Common.BACKGROUND_PATH + "/examination/physicalexaminationrecord/list";
    }

    @RequestMapping("addPage")
    public String addPage(Model model) throws Exception {
        return Common.BACKGROUND_PATH + "/examination/physicalexaminationrecord/add";
    }

    @RequestMapping("editPage")
    public String editPage(Model model,String id) throws Exception {
        PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = physicalExaminationRecordMapper.findbyFrist("id",id,PhysicalExaminationRecordFormMap.class);
        model.addAttribute("physicalExaminationRecordFormMap",physicalExaminationRecordFormMap);
        return Common.BACKGROUND_PATH + "/examination/physicalexaminationrecord/edit";
    }

    @ResponseBody
    @RequestMapping("tree")
    @SystemLog(module="组织管理",methods="加载组织树形列表")//凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    public List<Tree> tree()throws Exception {
        List<Tree> lt = new ArrayList<Tree>();
        PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
        physicalExaminationRecordFormMap.put("orderby","  id asc");
        List<PhysicalExaminationRecordFormMap> cfPingfenLeveFormMapList = physicalExaminationRecordMapper.findEnterprisePage(physicalExaminationRecordFormMap);

        if (CollectionUtils.isNotEmpty(cfPingfenLeveFormMapList)) {
            for (PhysicalExaminationRecordFormMap r : cfPingfenLeveFormMapList) {
                Tree tree = new Tree();
                tree.setId(r.get("id").toString());
                tree.setText(r.get("name").toString());
                lt.add(tree);
            }
        }
        return lt;
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








    @ResponseBody
    @RequestMapping("delete")
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    @SystemLog(module="系统管理",methods="用户管理-删除用户")//凡需要处理业务逻辑的.都需要记录操作日志
    public Map<String,Object>  delete() throws Exception {
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("status",0);
        try {
            PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = getFormMap(PhysicalExaminationRecordFormMap.class);
            physicalExaminationRecordMapper.deleteByNames(physicalExaminationRecordFormMap);
            retMap.put("msg","删除成功");
            retMap.put("status",1);
        }catch (Exception ex){
            ex.printStackTrace();
            retMap.put("msg",ex.getMessage());
        }
        return retMap;
    }






}
