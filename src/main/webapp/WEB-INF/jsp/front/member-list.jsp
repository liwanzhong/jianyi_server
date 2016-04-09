<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>会员列表</title>
    <link rel="stylesheet" href="${ctx}/front-static/css/base.css" type="text/css">
    <link rel="stylesheet" href="${ctx}/front-static/css/member.css" type="text/css">
    <link rel="stylesheet" href="${ctx}/front-static/css/bootstrap.min.css" type="text/css">
    <script src="${ctx}/front-static/js/jquery-1.11.1.min.js"></script>
    <script src="${ctx}/front-static/js/member.js"></script>
    <script src="${ctx}/front-static/js/bootstrap.min.js"></script>
    <script src="${ctx}/front-static/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/info/list.js"></script>
</head>

<body>
<!--头部-->
<jsp:include page="/common/front/header.jsp"></jsp:include>
<!--导航-->
<div class="nav">
    <div class="wrap">
        <ul class="clearfix">
            <li><a href="#" class="current">用户管理</a></li>
            <li><a href="#">检测管理</a></li>
            <li><a href="#">产品管理</a></li>
        </ul>
    </div>
</div>
<%--<div class="wrap">
    <div class="crumbs">
        <p>当前位置：<a href="#">会员管理</a> &gt; <a href="#">会员列表</a> &gt; <a href="#">文字3</a></p>
    </div>
    <div class="search-box clearfix">
        <button class="search-btn">搜索</button>
        <input type="text" class="search-inp">
    </div>
    <table class="dataintable">
        <thead>
        <tr>
            <th>文字文字</th>
            <th>文字文字</th>
            <th>文字文字</th>
            <th>文字文字</th>
            <th>文字文字</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
        </tr>
        <tr>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
        </tr>
        <tr>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
        </tr>
        <tr>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
        </tr>
        <tr>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
        </tr>
        <tr>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
            <td>文字文字</td>
        </tr>
        </tbody>
    </table>
    <div class="table-pages clearfix">
        <ul class="clearfix">
            <li><a href="#">首页</a></li>
            <li><a href="#">上一页</a></li>
            <li><a href="#" class="current">1</a></li>
            <li><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li style="border-right: 1px solid #EFEFEF; padding-right:14px;">…</li>
            <li><a href="#">99</a></li>
            <li><a href="#">100</a></li>
            <li><a href="#">下一页</a></li>
            <li><a href="#" style="border-right:none;">尾页</a></li>
        </ul>
    </div>
</div>--%>



<section>
    <section class="hbox stretch">
        <section id="content">
            <section id="id_vbox" class="vbox">
                <ul class="breadcrumb no-border no-radius b-b b-light" id="topli">
                    <li><i class="fa fa-home"></i><a href="${ctx}/index.shtml">Home</a></li>
                    <li>会员管理</li>
                    <li>会员信息</li>
                </ul>
                <section class="scrollable" style="margin-top: 35px;">
                    <section class="panel panel-default">
                        <header class="panel-heading font-bold">
                            查询面板
                        </header>
                        <div class="panel-body">
                            <form class="form-inline" role="form" id="searchForm" name="searchForm">
                                <div class="form-group">
                                    <label class=" control-label">企业名称:</label>
                                    <input type="text" class="input-sm form-control" id="enterpriseFormMap.name" name="enterpriseFormMap.name" placeholder="请输入企业名称">
                                </div>
                                <div class="form-group">
                                    <label class=" control-label">建立时间:</label>
                                    <div class="inline v-middle">
                                        <div class="input-group input-s-sm">
                                            <input type="text" class="input-sm form-control  form_datetime" readonly placeholder="开始时间" id="enterpriseFormMap_startTime" name="enterpriseFormMap.startTime" style="background-color:#fff;cursor:pointer">
                                            <script type="text/javascript">
                                                $("#enterpriseFormMap_startTime").datetimepicker({
                                                    language:'zh-CN',//汉化
                                                    format: 'yyyy-mm-dd',//选择日期后，文本框显示的日期格式
                                                    weekStart: 1,//一周的第一天
                                                    forceParse: 0,//强制解析输入框中的值
                                                    todayHighlight: 1,//今天高亮
                                                    minView: "month", //选择日期后，不会再跳转去选择时分秒
                                                    autoclose:true //选择日期后自动关闭
                                                });
                                            </script>
                                        </div>
                                        <label class=" control-label">至</label>
                                        <div class="input-group input-s-sm">
                                            <input type="text"  class="input-sm form-control form_datetime" readonly placeholder="结束时间"  id="enterpriseFormMap_endTime" name="enterpriseFormMap.endTime"  style="background-color:#fff;cursor:pointer">
                                            <script type="text/javascript">
                                                $("#enterpriseFormMap_endTime").datetimepicker({
                                                    language:'zh-CN',//汉化
                                                    format: 'yyyy-mm-dd',//选择日期后，文本框显示的日期格式
                                                    weekStart: 1,//一周的第一天
                                                    forceParse: 0,//强制解析输入框中的值
                                                    todayHighlight: 1,//今天高亮
                                                    minView: "month", //选择日期后，不会再跳转去选择时分秒
                                                    autoclose:true //选择日期后自动关闭
                                                });
                                            </script>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <button type="button" id="search" class="btn btn-sm">查  询</button>
                                </div>
                            </form>

                        </div>
                    </section>

                    <section class="panel panel-default">
                        <header class="panel-heading">
                            <div class="doc-buttons">
                                <button type="button" id="addCustomFun" class="btn btn-primary marR10" onclick="javascript:void(window.location.href='${ctx}/custom/info/toVerify.shtml')">新增</button>
                                <button type="button" id="editFun" class="btn btn-info marR10">编辑</button>
                                <button type="button" id="delFun" class="btn btn-danger marR10">删除</button>
                            </div>
                        </header>
                        <div class="table-responsive">
                            <div id="paging" class="pagclass"></div>
                        </div>
                        <div class="table-responsive">
                            <div id="paging2" class="pagclass"></div>
                        </div>
                    </section>
                </section>
            </section>
        </section>
        <aside class="bg-light lter b-l aside-md hide" id="notes">
            <div class="wrapper">Notification</div>
        </aside>
    </section>
</section>




</body>
</html>
