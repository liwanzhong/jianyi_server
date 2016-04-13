<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>验证会员</title>
    <link rel="stylesheet" href="${ctx}/front-static/css/base.css" type="text/css">
    <link rel="stylesheet" href="${ctx}/front-static/css/member.css" type="text/css">
    <script src="${ctx}/front-static/js/jquery-1.11.1.min.js"></script>
    <script src="${ctx}/front-static/js/member.js"></script>
    <%@include file="/common/common.jspf"%>
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
<%--<div class="wrap">
    <!--面包屑-->
    <div class="crumbs">
        <p>当前位置：<a href="#">文字1</a> &gt; <a href="#">文字2</a> &gt; <a href="#">文字3</a></p>
    </div>
    <!--输入身份证-->
    <div class="import-box">
        <span>请输入会员身份证：</span>	<input type="text" class="IDnum"><button class="check-btn">验&nbsp;&nbsp;证</button>
    </div>
</div>

<!--弹窗-->
<div class="popup">
    <div class="dark"></div>
    <div class="popup-box">
        <p>请确认是否将会员【徐小明】绑定到本检测点？</p>
        <button class="popup-sure popup-btn">确认绑定</button>
        <button class="popup-back popup-btn">返回</button>
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
                <section class="scrollable">
                    <section class="panel panel-default">
                        <div class="panel-body" style="text-align: center; vertical-align: middle">
                            <form class="form-inline" role="form" id="searchForm" name="searchForm">
                                <div class="form-group">
                                    <label class=" control-label ">会员身份证号:</label>
                                    <input type="text" class="input-sm form-control" id="customInfoFormMap_idCard" name="customInfoFormMap.idCard" placeholder="请输入会员身份证号" style="">
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
                                <label class=" control-label" id="msg">这里是查询验证以后的提示信息!</label>
                                <a class="btn btn-primary marR10" href="${ctx}/custom/info/addUI.shtml?customid=">已验证存在客户，绑定</a>
                                <a class="btn btn-primary marR10" href="#">已经绑定关系,返回</a>
                                <a class="btn btn-primary marR10" href="#">需要新建会员，新建</a>
                            </div>
                        </form>
                    </section>
                </section>
            </section>
        </section>
        <aside class="bg-light lter b-l aside-md hide" id="notes">
            <div class="wrapper">Notification</div>
        </aside>
    </section>
</section>


    <script type="text/javascript">

            function  verifyCustom(){
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
