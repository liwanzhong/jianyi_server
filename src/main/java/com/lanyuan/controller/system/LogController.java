package com.lanyuan.controller.system;

import com.lanyuan.annotation.SystemLog;
import com.lanyuan.controller.index.BaseController;
import com.lanyuan.entity.LogFormMap;
import com.lanyuan.mapper.LogMapper;
import com.lanyuan.plugin.PageView;
import com.lanyuan.util.Common;
import com.lanyuan.vo.Grid;
import com.lanyuan.vo.PageFilter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/log/")
public class LogController extends BaseController {
	@Inject
	private LogMapper logMapper;


	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/log/list";
	}




	@ResponseBody
	@RequestMapping("dataGrid")
	@SystemLog(module="用户管理",methods="加载用户列表")//凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public Grid dataGrid(PageFilter ph)throws Exception {
		Grid grid = new Grid();
		LogFormMap logFormMap = getFormMap(LogFormMap.class);
		String order = " order by "+ph.getSort()+" "+ph.getOrder();
		logFormMap.put("$orderby", order);
		logFormMap=toFormMap(logFormMap, String.valueOf(ph.getPage()), String.valueOf(ph.getRows()),logFormMap.getStr("orderby"));
		List<LogFormMap> logFormMapList =logMapper.findByPage(logFormMap);
		if(CollectionUtils.isNotEmpty(logFormMapList)){
			grid.setRows(logFormMapList);
		}
		PageView pageView = (PageView) logFormMap.get("paging");
		grid.setTotal(pageView.getRowCount());
		return grid;
	}

}