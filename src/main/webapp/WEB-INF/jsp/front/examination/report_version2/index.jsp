<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>双螺旋机能检测报告</title>
    <link type="text/css" rel="stylesheet" href="${ctx}/front-static/report_version2/css/base.css">
    <link type="text/css" rel="stylesheet" href="${ctx}/front-static/report_version2/css/index.css">
    <%--<link type="text/css" rel="stylesheet" href="${ctx}/front-static/report_version2/css/index-2.css">--%>
    <script src="${ctx}/front-static/report_version2/js/jquery-1.12.1.min.js"></script>
    <script src="${ctx}/front-static/report_version2/js/radialIndicator.js"></script>
    <script>
        $(function(){
            //中心点横坐标
            var dotLeft = ($(".container").width()-$(".dot").width())/2;
            //中心点纵坐标
            var dotTop = ($(".container").height()-$(".dot").height())/2;
            //起始角度
            var stard = 0;
            //半径
            var radius = 226;
            var radius2 = 280;
            //BOX对应的角度;
            var avd = 360/$("#indicatorContainer2 canvas").length;
            //BOX对应的弧度;
            var ahd = avd*Math.PI/180;

            //设置圆的中心点的位置
            $(".dot").css({"left":dotLeft,"top":dotTop});
            $("#indicatorContainer2 canvas").each(function(index, element){
                $(this).css({"left":Math.sin((ahd*index))*radius+dotLeft+16,"top":Math.cos((ahd*index))*radius+dotTop+10});
            });
            $(".container>span").each(function(index, element){
                $(this).css({"left":Math.sin((ahd*index))*radius2+dotLeft+20,"top":Math.cos((ahd*index))*radius2+dotTop+36});
            });
        })
    </script>
</head>

<body>
<div class="wrap">

    <!--头部-->
    <div class="header">
        <ul style="position:relative; height:66px;">
            <li>
                <a href="#" class="logo"><img src="${ctx}/front-static/report_version2/img/logo.png" alt=""/></a>
            </li>
            <li><h1>双螺旋机能检测报告</h1></li>
            <li style="float:right;">
                <a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=1&recordId=${physicalExaminationRecordFormMap.id}">下一页</a>
            </li>
        </ul>
    </div>


    <div class="w">
        <!--msg-->
        <div class="msg">
            <span class="name">${physicalExaminationRecordFormMap.name}</span>
            <span class="sex">${physicalExaminationRecordFormMap.sex==1?"男":"女"}</span>
            <span class="birth">${physicalExaminationRecordFormMap.birthday}</span>
            <span class="sex">年龄：${physicalExaminationRecordFormMap.age}</span>
            <span class="sex">BMI：${physicalExaminationRecordFormMap.bmi}</span>
            <span class="time">   <fmt:formatDate value="${physicalExaminationRecordFormMap.check_time}" pattern="yyyy-MM-dd"/>检测</span>
        </div>
        <!--details-->
        <div class="details">
            <p class="detaile-t">健康特工007提醒您：您的身体总分<span> <fmt:formatNumber value="${physicalExaminationMainReportFormMap.check_total_score}" pattern="#0"/>分</span>，您的<span>健康状态处于${zongpingLeveDescConfigFormMap.leve_name}</span>！
            <c:choose>
                <c:when test="${physicalExaminationMainReportFormMap.leve_id==1}">
                    <img src="${ctx}/front-static/report_version2/img/smile.png">
                </c:when>
                <c:when test="${physicalExaminationMainReportFormMap.leve_id==2}">
                    <img src="${ctx}/front-static/report_version2/img/sad.png">
                </c:when>
                <c:when test="${physicalExaminationMainReportFormMap.leve_id==3}">
                    <img src="${ctx}/front-static/report_version2/img/nanguo.png">
                </c:when>
                <c:when test="${physicalExaminationMainReportFormMap.leve_id==4}">
                    <img src="${ctx}/front-static/report_version2/img/cry.png">
                </c:when>
                <c:when test="${physicalExaminationMainReportFormMap.leve_id==5}">
                    <img src="${ctx}/front-static/report_version2/img/cry.png">
                </c:when>
            </c:choose>
            </p>
            <p>您身体的短板是 <span>${zuicha}</span> ，同时需要注意的还有 <span>${cicha}</span> 。${zongpingLeveDescConfigFormMap.leve_des}</p>
        </div>
        <!--main-->
        <div class="body-bg">
            <!--grade-->
            <div class="grade clearfix">
                <div class="grade-1">
                    <ul>
                        <c:forEach items="${leveCountGroupBy}" var="entry">
                            <c:choose>
                                <c:when test="${entry.key ==1 }">
                                    <li class="s-pic-green">${entry.value}</li>
                                </c:when>
                                <c:when test="${entry.key ==2 }">
                                    <li class="s-pic-blue">${entry.value}</li>
                                </c:when>
                                <c:when test="${entry.key ==3 }">
                                    <li class="s-pic-yellow">${entry.value}</li>
                                </c:when>
                                <c:when test="${entry.key ==4 }">
                                    <li class="s-pic-orange">${entry.value}</li>
                                </c:when>
                                <c:when test="${entry.key ==5 }">
                                    <li class="s-pic-red">${entry.value}</li>
                                </c:when>
                            </c:choose>
                        </c:forEach>
                    </ul>
                </div>
                <div class="grade-2">
                    <div id="indicatorContainer">
                    </div>
                    <script>
                        $('#indicatorContainer').radialIndicator({
                            radius: 66, //圆的半径，默认50
                            barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
                            barColor:'${physicalExaminationMainReportFormMap.show_color}', //刻度条的颜色
                            barWidth: 10, //刻度条的宽度，默认5
                            initValue: <fmt:formatNumber value="${physicalExaminationMainReportFormMap.check_total_score}" pattern="#0"/>, //圆形指示器初始化的值
                            roundCorner:false, //刻度条是否圆角
                            percentage: false //显示百分数
                        });
                    </script>
                </div>
                <div class="grade-3">
                    <div id="indicatorContainer1">
                    </div>
                    <script>
                        $('#indicatorContainer1').radialIndicator({
                            radius: 38, //圆的半径，默认50
                            barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
                            barColor:'${bmiLeveConfigFormMap.show_color}', //刻度条的背景颜色,默认#eee
                            barWidth: 8, //刻度条的宽度，默认5
                            initValue:${bmi},
                            roundCorner:false, //刻度条是否圆角
                            percentage: false //显示百分数
                        });
                    </script>
                </div>
                <div class="grade-4" style="margin-right:0;">
                    <ul>
                        <li>身高：<span>${physicalExaminationRecordFormMap.body_height}</span></li>
                        <li>体重：<span>${physicalExaminationRecordFormMap.weight}</span>KG</li>
                        <li style="color:#27930b;">标准体重：<span>${minWeight}-${maxWeight}</span>KG</li>
                        <li style="color:${bmiLeveConfigFormMap.show_color}; font-size:16px;">${bmiLeveConfigFormMap.health_leve_name}</li>
                    </ul>
                </div>

            </div>
            <!--body-->
            <div class="main-bottom">
                <div class="container">
                    <div class="dot"></div>
                    <div id="indicatorContainer2"></div>
                    <c:forEach items="${physicalExaminationBigResultFormMapList}" var="item" varStatus="status">
                        <span class="score-${status.index+1}">${item.name}</span>
                    </c:forEach>

                </div>
                <script>
                    <c:forEach items="${physicalExaminationBigResultFormMapList}" var="item">

                    $('#indicatorContainer2').radialIndicator({
                        radius: 30, //圆的半径，默认50
                        barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
                        barColor:'${item.show_color}', //刻度条的颜色
                        barWidth: 7, //刻度条的宽度，默认5
                        initValue:  ${item.check_score}, //圆形指示器初始化的值
                        roundCorner:false, //刻度条是否圆角
                        percentage: false //显示百分数
                    });
                    </c:forEach>

                </script>
            </div>
        </div>

        <!--底部-->
        <div class="footer">
            <div class="footer-t">
                <ul class="clearfix">
                    <li>中瑞力佳（北京）生物科技有限公司</li>
                    <li><a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=1&recordId=${physicalExaminationRecordFormMap.id}">下一页</a></li>
                </ul>
            </div>
            <div class="footer-b">
                <ul class="clearfix">
                    <li>地址：北京市朝阳区东三环南路17号</li>
                    <li>此报告不作为临床检测诊断依据</li>
                </ul>
            </div>
        </div>
    </div>


</div>
</body>
</html>
