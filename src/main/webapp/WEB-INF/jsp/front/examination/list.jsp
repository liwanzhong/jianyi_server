<%@ page language="java" pageEncoding="UTF-8"%>
<%
    double randomCode = Math.random();
%>
<html lang="en" class="app">
<head>
    <%@include file="/common/common.jspf"%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/examination/physicalexaminationrecord/list.js?randomCode=<%=randomCode%>"></script>
</head>
<body>
<section class="vbox">
    <%@include file="/common/header.jsp"%>
    <section>
        <section class="hbox stretch">
            <section id="content">
                <section  class="vbox">
                    <section class="scrollable wrapper">
                        <div class="row">
                            <div class="col-lg-12">
                                <section class="panel panel-default">
                                    <header class="panel-heading text-right bg-light">
                                        <ul class="nav nav-tabs pull-left">
                                            <li class="">
                                                <a href="#messages-2" onclick='showPage("messages-2","${ctx}/custom/info/list_client.shtml")' data-toggle="tab" style="font-size: 18px;font-weight: bold">
                                                    <i class="fa fa-user text-default"></i><span >会员管理</span>
                                                </a>
                                            </li>
                                            <li class="active">
                                                <a href="#profile-2" onclick='showPage("profile-2","${ctx}/examination/physicalExamination/client_list.shtml")' data-toggle="tab"  style="font-size: 18px;font-weight: bold">
                                                    <i class="fa fa-th-large"></i><span>检测管理</span>
                                                </a>
                                            </li>
                                        </ul>
                                        <span class="hidden-sm">&nbsp;</span>
                                    </header> <div class="panel-body">
                                    <div class="tab-content">
                                        <div class="tab-pane fade active in" id="messages-2">
                                            <ul class="breadcrumb"> <li><a href="#"><i class="fa fa-home"></i> Home</a></li> <li><a href="#"><i class="fa fa-list-ul"></i> 检测管理</a></li> <li class="active">检测列表</li> </ul>
                                            <section class="scrollable">
                                                <section class="panel panel-default">
                                                    <div class="panel-body">
                                                        <form class="form-inline" role="form" id="searchForm" name="searchForm">
                                                            <div class="form-group">
                                                                <label class=" control-label">会员姓名:</label>
                                                                <input type="text" class="input-sm form-control" id="physicalExaminationRecordFormMap_name" name="physicalExaminationRecordFormMap.name" placeholder="请输入会员姓名">
                                                            </div>
                                                            <div class="form-group">
                                                                <label class=" control-label">会员手机:</label>
                                                                <input type="text" class="input-sm form-control" id="physicalExaminationRecordFormMap_mobile" name="physicalExaminationRecordFormMap.mobile" placeholder="请输入会员手机">
                                                            </div>
                                                            <div class="form-group">
                                                                <button type="button" id="search" class="btn btn-sm">查  询</button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </section>

                                                <section class="panel panel-default">
                                                    <div class="table-responsive">
                                                        <div id="paging" class="pagclass"></div>
                                                    </div>
                                                    <div class="table-responsive">
                                                        <div id="paging2" class="pagclass"></div>
                                                    </div>
                                                </section>

                                            </section>
                                        </div>
                                        <div class="tab-pane fade" id="profile-2"></div>
                                    </div>
                                </div>
                                </section>
                            </div>
                        </div>
                    </section>
                </section>
            </section>
            <aside class="bg-light lter b-l aside-md hide" id="notes">
                <div class="wrapper">Notification</div>
            </aside>
        </section>
    </section>
</section>
<!-- Bootstrap -->
<div id="flotTip" style="display: none; position: absolute;"></div>
<script language="javascript">

    function showPage(tabId, url){
        window.location.href=url;
    }

</script>
</body>
</html>
