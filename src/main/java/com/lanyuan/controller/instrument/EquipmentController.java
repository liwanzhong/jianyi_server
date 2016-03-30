package com.lanyuan.controller.instrument;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.EquipmentFormMap;
import com.lanyuan.mapper.EquipmentMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import com.lanyuan.vo.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
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
@RequestMapping("/instrument/equipment/")
public class EquipmentController  extends BaseController {
    @Inject
    private EquipmentMapper equipmentMapper;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return Common.BACKGROUND_PATH + "/instrument/equipment/list";
    }

    @RequestMapping("addPage")
    public String addPage(Model model) throws Exception {
        //todo 生成机器码
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt=randomNumberGenerator.nextBytes().toHex();
        String istmt_code = new SimpleHash("md5", "", ByteSource.Util.bytes(salt), 2).toHex();
        model.addAttribute("istmt_code",istmt_code);
        return Common.BACKGROUND_PATH + "/instrument/equipment/add";
    }

    @RequestMapping("editPage")
    public String editPage(Model model,String id) throws Exception {
        EquipmentFormMap equipmentFormMap = equipmentMapper.findbyFrist("id",id,EquipmentFormMap.class);
        model.addAttribute("equipmentFormMap",equipmentFormMap);
        return Common.BACKGROUND_PATH + "/instrument/equipment/edit";
    }

    @ResponseBody
    @RequestMapping("tree")
    @SystemLog(module="组织管理",methods="加载组织树形列表")//凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    public List<Tree> tree()throws Exception {
        List<Tree> lt = new ArrayList<Tree>();
        EquipmentFormMap equipmentFormMap = getFormMap(EquipmentFormMap.class);
        equipmentFormMap.put("orderby","  id asc");
        List<EquipmentFormMap> cfPingfenLeveFormMapList = equipmentMapper.findEnterprisePage(equipmentFormMap);

        if (CollectionUtils.isNotEmpty(cfPingfenLeveFormMapList)) {
            for (EquipmentFormMap r : cfPingfenLeveFormMapList) {
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
        EquipmentFormMap equipmentFormMap = getFormMap(EquipmentFormMap.class);
        equipmentFormMap.put("orderby",ph.getSort()+" "+ph.getOrder());
        equipmentFormMap=toFormMap(equipmentFormMap,  String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),equipmentFormMap.getStr("orderby"));
        List<EquipmentFormMap> userFormMapList =equipmentMapper.findEnterprisePage(equipmentFormMap);
        if(CollectionUtils.isNotEmpty(userFormMapList)){
            grid.setRows(userFormMapList);
        }
        PageView pageView = (PageView) equipmentFormMap.get("paging");
        grid.setTotal(pageView.getRowCount());
        return grid;

    }




    @ResponseBody
    @RequestMapping("add")
    @SystemLog(module="用户管理",methods="新增用户")//凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    public Map<String,Object> add(){
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("status",0);
        try {
            EquipmentFormMap equipmentFormMap = getFormMap(EquipmentFormMap.class);
            equipmentFormMap.put("create_time",dateFormat.format(new Date()));
            equipmentMapper.addEntity(equipmentFormMap);
            retMap.put("msg","添加成功");
            retMap.put("status",1);
        }catch (Exception ex){
            retMap.put("msg",ex.getMessage());
        }
        return retMap;
    }



    @ResponseBody
    @RequestMapping("delete")
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    @SystemLog(module="系统管理",methods="用户管理-删除用户")//凡需要处理业务逻辑的.都需要记录操作日志
    public Map<String,Object>  delete() throws Exception {
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("status",0);
        try {
            EquipmentFormMap equipmentFormMap = getFormMap(EquipmentFormMap.class);
            equipmentMapper.deleteByNames(equipmentFormMap);
            retMap.put("msg","删除成功");
            retMap.put("status",1);
        }catch (Exception ex){
            ex.printStackTrace();
            retMap.put("msg",ex.getMessage());
        }
        return retMap;
    }





    @ResponseBody
    @RequestMapping("update")
    @SystemLog(module="权限组管理",methods="修改权限组")//凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    public Map<String,Object>  update(Model model) {
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("status",0);
        try {
            EquipmentFormMap equipmentFormMap = getFormMap(EquipmentFormMap.class);
            equipmentMapper.editEntity(equipmentFormMap);
            retMap.put("msg","修改成功");
            retMap.put("status",1);
        }catch (Exception ex){
            retMap.put("msg",ex.getMessage());
            ex.printStackTrace();
        }
        return retMap;
    }
}
