<%@ page language="java" pageEncoding="UTF-8"%>

<html lang="en" class="app">
<head>
    <%@include file="/common/common.jspf"%>
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
                                            <li class="active">
                                                <a href="#messages-2" onclick='showPage("messages-2","${ctx}/custom/info/list_client.shtml")' data-toggle="tab" style="font-size: 18px;font-weight: bold">
                                                    <i class="fa fa-user text-default"></i><span >会员管理</span>
                                                </a>
                                            </li>
                                            <li class="">
                                                <a href="#profile-2" onclick='showPage("profile-2","${ctx}/examination/physicalExamination/client_list.shtml")' data-toggle="tab"  style="font-size: 18px;font-weight: bold">
                                                    <i class="fa fa-th-large"></i><span>检测管理</span>
                                                </a>
                                            </li>
                                        </ul>
                                        <span class="hidden-sm">&nbsp;</span>
                                    </header> <div class="panel-body">
                                    <div class="tab-content">
                                        <div class="tab-pane fade active in" id="messages-2">
                                            <ul class="breadcrumb"> <li><a href="#"><i class="fa fa-home"></i> Home</a></li> <li><a href="${ctx}/custom/info/list_client.shtml"><i class="fa fa-list-ul"></i> 会员管理</a></li>  <li class="active">验证会员</li></ul>
                                            <section class="scrollable">
                                                <section class="panel panel-default">
                                                    <div class="panel-body" style="text-align: center; vertical-align: middle">
                                                        <form class="form-inline" role="form" id="searchForm" name="searchForm">
                                                            <div class="form-group">
                                                                <label class=" control-label ">会员身份证号:</label>
                                                                <%--<input type="text" class="input-sm form-control" id="customInfoFormMap_idCard" name="customInfoFormMap.idCard" placeholder="请输入会员身份证号" style="">--%>
                                                                <input type="text" style="width: 248px" data-regexp="#^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$"  class="form-control parsley-validated" data-required="true" id="customInfoFormMap_idCard" name="customInfoFormMap.idCard" placeholder="请输入会员身份证号">
                                                            </div>
                                                            <div class="form-group">
                                                                <button type="button" id="search" class="btn btn-sm" onclick="verifyCustom()">查  询</button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </section>
                                                <section class="panel panel-default"  style="text-align: center; vertical-align: middle">
                                                    <form class="form-inline" role="form" >
                                                        <div class="form-group" id="nextStepTips" >
                                                            <label class=" control-label" id="msg">查询验证后，下一步操作将在这里!</label>
                                                        </div>
                                                    </form>
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


    function  verifyCustom(){
        if($("#customInfoFormMap_idCard").val()==null || $("#customInfoFormMap_idCard").val() =='' || $("#customInfoFormMap_idCard").val()==undefined ||($("#customInfoFormMap_idCard").val().length!=15 && $("#customInfoFormMap_idCard").val().length!=18)){
            alert("请输入正确的身份证号码！");
            return
        }

        $.ajax({
            type : "POST",
            data :$("#searchForm").serialize(),
            url : rootPath + '/custom/info/verify.shtml',
            dataType : 'json',
            success : function(data) {
                $("#nextStepTips").empty().append('<label class=" control-label" id="msg">'+data.msg+'</label>');
                switch (data.custom_status){
                    case -1:
                        break;
                    case 0:
                        $("#nextStepTips").append('<a class="btn btn-primary marR10" href="${ctx}/custom/info/addUI.shtml?customid=&idcard='+data.cardid+'">需要新建会员，新建</a>');
                        break;
                    case 1:
                        $("#nextStepTips").append('<a class="btn btn-primary marR10" href="${ctx}/custom/info/addUI.shtml?customid='+data.data.id+'&idcard='+data.cardid+'">已验证存在客户，绑定</a>');
                        break;
                    case 2:
                        $("#nextStepTips").append('<a class="btn btn-primary marR10" href="javascript:void(history.go(-1));">已经绑定关系,返回</a>');
                        break;
                }
            }
        });
    }

</script>
</body>
</html>
