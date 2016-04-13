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
    <%@include file="/common/common.jspf"%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/info/list.js"></script>
</head>

<body>
<!--头部-->
<jsp:include page="/common/front/header.jsp"></jsp:include>
<!--导航-->
<div class="nav">
    <div class="wrap">
        <ul class="clearfix">
            <li><a href="${ctx}/custom/info/list_client.shtml" class="current">用户管理</a></li>
            <li><a href="${ctx}/examination/physicalExamination/client_list.shtml">检测管理</a></li>
            <li><a href="#">产品管理</a></li>
        </ul>
    </div>
</div>

<section>
    <section class="hbox stretch">
        <section id="content">
            <section id="id_vbox" class="vbox">
                <ul class="breadcrumb no-border no-radius b-b b-light" id="topli">
                    <li><i class="fa fa-home"></i><a href="${ctx}/index.shtml">Home</a></li>
                    <li>会员管理</li>
                    <li>会员信息</li>
                </ul>
                <section class="scrollable">
                    <section class="panel panel-default">
                        <%--<header class="panel-heading font-bold">
                            查询面板
                        </header>--%>
                        <div class="panel-body">
                            <form class="form-inline" role="form" id="searchForm" name="searchForm">
                                <div class="form-group">
                                    <label class=" control-label">会员姓名:</label>
                                    <input type="text" class="input-sm form-control" id="customInfoFormMap_name" name="customInfoFormMap.name" placeholder="请输入会员姓名">
                                </div>
                                <div class="form-group">
                                    <label class=" control-label">会员手机:</label>
                                    <input type="text" class="input-sm form-control" id="customInfoFormMap_mobile" name="customInfoFormMap.mobile" placeholder="请输入会员手机">
                                </div>
                                <div class="form-group">
                                    <label class=" control-label">会员身份证号:</label>
                                    <input type="text" class="input-sm form-control" id="customInfoFormMap_idCard" name="customInfoFormMap.idCard" placeholder="请输入会员身份证号">
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
                                <button type="button" id="addCustomFun" class="btn btn-primary marR10" onclick="javascript:void(window.location.href='${ctx}/custom/info/toVerify.shtml')">新增会员</button>
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
