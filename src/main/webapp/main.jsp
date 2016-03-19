<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="inc.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>检测管理系统</title>
	<script type="text/javascript">
		var index_layout;
		var index_tabs;
		var index_tabsMenu;
		var layout_west_tree;
		var layout_west_tree_url = '';

		var sessionInfo_userId = '${sessionInfo.id}';
		if (sessionInfo_userId) {//如果没有登录,直接跳转到登录页面
			layout_west_tree_url = '${ctx}/resource/tree';
		}else{
			window.location.href='${ctx}/admin/index';
		}
		$(function() {
			index_layout = $('#index_layout').layout({
				fit : true
			});

			index_tabs = $('#index_tabs').tabs({
				fit : true,
				border : false,
				tools : [{
					iconCls : 'icon-home',
					handler : function() {
						index_tabs.tabs('select', 0);
					}
				}, {
					iconCls : 'icon-refresh',
					handler : function() {
						var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
						index_tabs.tabs('getTab', index).panel('open').panel('refresh');
					}
				}, {
					iconCls : 'icon-del',
					handler : function() {
						var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
						var tab = index_tabs.tabs('getTab', index);
						if (tab.panel('options').closable) {
							index_tabs.tabs('close', index);
						}
					}
				} ]
			});

			/*layout_west_tree = $('#layout_west_tree').tree({
				url : layout_west_tree_url,
				parentField : 'pid',
				lines : true,
				onClick : function(node) {
					if (node.attributes && node.attributes.url) {
						var url = '${ctx}' + node.attributes.url;
						addTab({
							url : url,
							title : node.text,
							iconCls : node.iconCls
						});
					}
				}
			});*/
		});

		function addTab(params) {
			var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:99.5%;"></iframe>';
			var t = $('#index_tabs');
			var opts = {
				title : params.title,
				closable : true,
				iconCls : params.iconCls,
				content : iframe,
				border : false,
				fit : true
			};
			if (t.tabs('exists', opts.title)) {
				t.tabs('select', opts.title);
			} else {
				t.tabs('add', opts);
			}
		}

		function logout(){
			$.messager.confirm('提示','确定要退出?',function(r){
				if (r){
					progressLoad();
					$.post( '${ctx}/admin/logout', function(result) {
						if(result.success){
							progressClose();
							window.location.href='${ctx}/admin/index';
						}
					}, 'json');
				}
			});
		}


		function editUserPwd() {
			parent.$.modalDialog({
				title : '修改密码',
				width : 300,
				height : 250,
				href : '${ctx}/user/editPwdPage',
				buttons : [ {
					text : '修改',
					handler : function() {
						var f = parent.$.modalDialog.handler.find('#editUserPwdForm');
						f.submit();
					}
				} ]
			});
		}


	</script>
</head>
<body>
<div id="loading" style="position: fixed;top: -50%;left: -50%;width: 200%;height: 200%;background: #fff;z-index: 100;overflow: hidden;">
	<img src="${ctx}/style/images/ajax-loader.gif" style="position: absolute;top: 0;left: 0;right: 0;bottom: 0;margin: auto;"/>
</div>
<div id="index_layout">
	<div data-options="region:'north',border:false" style=" overflow: hidden;" >
		<div id="header">
			<span style="float: right; padding-right: 20px;">欢迎 <b>${sessionInfo.name}</b>&nbsp;&nbsp; <a href="javascript:void(0)" onclick="editUserPwd()" style="color: #fff">修改密码</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="logout()" style="color: #fff">安全退出</a>
        	&nbsp;&nbsp;&nbsp;&nbsp;
    		</span>
			<span class="header"></span>
		</div>
	</div>
	<div data-options="region:'west',split:true" title="主导航" style="width: 160px; overflow: hidden;overflow-y:auto;">
		<%--<div class="well well-small" style="padding: 5px 5px 5px 5px;">
			<ul id="layout_west_tree"></ul>
		</div>--%>
			<div class="easyui-accordion" data-options="fit:true,border:false">
				<div title="营销管理" data-options="selected:true,iconCls:'icon-yxgl'" style="padding: 10px">
					<a href="javascript:openTab('营销机会管理','saleChanceManage.jsp','icon-yxjhgl')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-yxjhgl'" style="width: 150px;">营销机会管理</a>
					<a href="javascript:openTab('客户开发计划','cusdevplanManage.jsp','icon-khkfjh')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-khkfjh'" style="width: 150px;">客户开发计划</a>
				</div>

				<div title="客户管理"  data-options="iconCls:'icon-khgl'" style="padding:10px;">
					<a href="javascript:openTab('客户信息管理','customerManage.jsp','icon-khxxgl')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-khxxgl'" style="width: 150px;">客户信息管理</a>
					<a href="javascript:openTab('客户流失管理','customerLossManage.jsp','icon-khlsgl')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-khlsgl'" style="width: 150px;">客户流失管理</a>
				</div>
				<div title="服务管理" data-options="iconCls:'icon-fwgl'" style="padding:10px">
					<a href="javascript:openTab('服务创建','customerServiceCreate.jsp','icon-fwcj')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-fwcj'" style="width: 150px;">服务创建</a>
					<a href="javascript:openTab('服务分配','customerServiceAssign.jsp','icon-fwfp')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-fwfp'" style="width: 150px;">服务分配</a>
					<a href="javascript:openTab('服务处理','customerServiceProce.jsp','icon-fwcl')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-fwcl'" style="width: 150px;">服务处理</a>
					<a href="javascript:openTab('服务反馈','customerServiceFeedback.jsp','icon-fwfk')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-fwfk'" style="width: 150px;">服务反馈</a>
					<a href="javascript:openTab('服务归档','customerServiceFile.jsp','icon-fwgd')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-fwgd'" style="width: 150px;">服务归档</a>
				</div>
				<div title="统计报表"  data-options="iconCls:'icon-tjbb'" style="padding:10px">
					<a href="javascript:openTab('客户贡献分析','khgxfx.jsp','icon-khgxfx')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-khgxfx'" style="width: 150px;">客户贡献分析</a>
					<a href="javascript:openTab('客户构成分析','khgcfx.jsp','icon-khgcfx')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-khgcfx'" style="width: 150px;">客户构成分析</a>
					<a href="javascript:openTab('客户服务分析','khfwfx.jsp','icon-khfwfx')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-khfwfx'" style="width: 150px;">客户服务分析</a>
					<a href="javascript:openTab('客户流失分析','khlsfx.jsp','icon-khlsfx')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-khlsfx'" style="width: 150px;">客户流失分析</a>
				</div>
				<div title="基础数据管理"  data-options="iconCls:'icon-jcsjgl'" style="padding:10px">
					<a href="javascript:openTab('数据字典管理','dataDicManage.jsp','icon-sjzdgl')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-sjzdgl'" style="width: 150px;">数据字典管理</a>
					<a href="javascript:openTab('产品信息查询','productSearch.jsp','icon-cpxxgl')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cpxxgl'" style="width: 150px;">产品信息查询</a>
					<a href="javascript:openTab('用户信息管理','userManage.jsp','icon-user')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user'" style="width: 150px;">用户信息管理</a>
				</div>
				<div title="系统管理"  data-options="iconCls:'icon-item'" style="padding:10px">
					<a href="javascript:openPasswordModifyDialog()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-modifyPassword'" style="width: 150px;">修改密码</a>
					<a href="javascript:logout()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exit'" style="width: 150px;">安全退出</a>
				</div>

			</div>
	</div>
	<div data-options="region:'center'" style="overflow: hidden;">
		<div id="index_tabs" style="overflow: hidden;">
			<div title="首页" data-options="border:false" style="overflow: hidden;">
				<div style="padding:10px 0 10px 10px">
					<h2>系统介绍</h2>
					<div class="light-info">
						<div class="light-tip icon-tip"></div>
						<div>JAVA快速开发平台。</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div data-options="region:'south',border:false" style="height: 30px;line-height:30px; overflow: hidden;text-align: center;background-color: #eee" >版权所有@飞奔的鸵鸟 </div>
</div>

<!--[if lte IE 7]>
<div id="ie6-warning"><p>您正在使用 低版本浏览器，在本页面可能会导致部分功能无法使用。建议您升级到 <a href="http://www.microsoft.com/china/windows/internet-explorer/" target="_blank">Internet Explorer 8</a> 或以下浏览器：
	<a href="http://www.mozillaonline.com/" target="_blank">Firefox</a> / <a href="http://www.google.com/chrome/?hl=zh-CN" target="_blank">Chrome</a> / <a href="http://www.apple.com.cn/safari/" target="_blank">Safari</a> / <a href="http://www.operachina.com/" target="_blank">Opera</a></p></div>
<![endif]-->

<style>
	/*ie6提示*/
	#ie6-warning{width:100%;position:absolute;top:0;left:0;background:#fae692;padding:5px 0;font-size:12px}
	#ie6-warning p{width:960px;margin:0 auto;}
</style>
</body>
</html>