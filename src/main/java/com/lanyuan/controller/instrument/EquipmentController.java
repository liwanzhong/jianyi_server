package com.lanyuan.controller.instrument;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.EquipmentFormMap;
import com.lanyuan.entity.UserGroupsFormMap;
import com.lanyuan.exception.SystemException;
import com.lanyuan.mapper.EquipmentMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liwanzhong on 2016/2/21.
 */
@Controller
@RequestMapping("/instrument/equipment/")
public class EquipmentController  extends BaseController {
    @Inject
    private EquipmentMapper equipmentMapper;

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        model.addAttribute("res", findByRes());
        return Common.BACKGROUND_PATH + "/instrument/equipment/list";
    }

    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
        EquipmentFormMap equipmentFormMap = getFormMap(EquipmentFormMap.class);
        equipmentFormMap=toFormMap(equipmentFormMap, pageNow, pageSize,equipmentFormMap.getStr("orderby"));
        equipmentFormMap.put("column", column);
        equipmentFormMap.put("sort", sort);

        pageView.setRecords(equipmentMapper.findEnterprisePage(equipmentFormMap));//不调用默认分页,调用自已的mapper中findUserPage
        return pageView;
    }




    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
        //todo 生成机器码
        String mscode= Md5Hash.toString(Md5Hash.toBytes(String.valueOf(System.currentTimeMillis())));
        model.addAttribute("mscode",mscode);
        return Common.BACKGROUND_PATH + "/instrument/equipment/add";
    }

    @ResponseBody
    @RequestMapping("addEntity")
    @SystemLog(module="系统管理",methods="企业管理，新增企业信息")//凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    public String addEntity(String txtGroupsSelect){
        try {
            EquipmentFormMap EquipmentFormMap = getFormMap(EquipmentFormMap.class);


            equipmentMapper.addEntity(EquipmentFormMap);//新增后返回新增信息

        } catch (Exception e) {
            throw new SystemException("添加账号异常");
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("deleteEntity")
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    @SystemLog(module="系统管理",methods="删除企业")//凡需要处理业务逻辑的.都需要记录操作日志
    public String deleteEntity() throws Exception {
        String[] ids = getParaValues("ids");
        for (String id : ids) {
            equipmentMapper.deleteByAttribute("id", id, EquipmentFormMap.class);
        }
        return "success";
    }

    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
        String id = getPara("id");
        if(Common.isNotEmpty(id)){
            model.addAttribute("enterprise", equipmentMapper.findbyFrist("id", id, EquipmentFormMap.class));
        }
        return Common.BACKGROUND_PATH + "/instrument/equipment/edit";
    }

    @ResponseBody
    @RequestMapping("editEntity")
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    @SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
    public String editEntity(String txtGroupsSelect) throws Exception {
        EquipmentFormMap EquipmentFormMap = getFormMap(EquipmentFormMap.class);
        EquipmentFormMap.put("txtGroupsSelect", txtGroupsSelect);
        equipmentMapper.editEntity(EquipmentFormMap);
        equipmentMapper.deleteByAttribute("userId", EquipmentFormMap.get("id")+"", UserGroupsFormMap.class);
        if(!Common.isEmpty(txtGroupsSelect)){
            String[] txt = txtGroupsSelect.split(",");
            for (String roleId : txt) {
                UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
                userGroupsFormMap.put("userId", EquipmentFormMap.get("id"));
                userGroupsFormMap.put("roleId", roleId);
                equipmentMapper.addEntity(userGroupsFormMap);
            }
        }
        return "success";
    }





    /**
     * 验证账号是否存在
     *
     * @author lanyuan Email：mmm333zzz520@163.com date：2014-2-19
     * @param name
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean isExist(String name) {
        EquipmentFormMap account = equipmentMapper.findbyFrist("accountName", name, EquipmentFormMap.class);
        if (account == null) {
            return true;
        } else {
            return false;
        }
    }
}
