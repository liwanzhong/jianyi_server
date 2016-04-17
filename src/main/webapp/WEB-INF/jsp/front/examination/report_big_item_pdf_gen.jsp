<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>人体机能检测报告</title>
    <link type="text/css" rel="stylesheet" href="${ctx}/front-static/report_item/css/base.css">
    <link type="text/css" rel="stylesheet" href="${ctx}/front-static/report_item/css/index2.css">
    <script src="${ctx}/front-static/report_item/js/jquery-1.12.1.min.js"></script>
    <script src="${ctx}/front-static/report_item/js/radialIndicator.js"></script>

</head>

<body>
<div class="wrap">
    <div class="header">
        <h1>人体机能检测报告</h1>
        <ol>
            <%--<c:choose>
                <c:when test="${pageView.pageNow==pageView.pageCount}">

                </c:when>
                <c:otherwise>
                    <li><a href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=${pageView.pageNow+1}&recordId=${physicalExaminationBigResultFormMap.examination_record_id}">下一页</a></li>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${pageView.pageNow-1==0}">
                    <li style="margin-right:10px;"><a href="${ctx}/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id=${physicalExaminationBigResultFormMap.examination_record_id}">上一页</a></li>
                </c:when>
                <c:otherwise>
                    <li style="margin-right:10px;"><a href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=${pageView.pageNow-1}&recordId=${physicalExaminationBigResultFormMap.examination_record_id}">上一页</a></li>
                </c:otherwise>
            </c:choose>--%>
        </ol>
    </div>
    <div class="main">
        <button class="btn-1">${physicalExaminationBigResultFormMap.name}详解</button>
        <p class="word">${physicalExaminationBigResultFormMap.tips_content}</p>
        <div id="indicatorContainerWrap" style="background-image:${ctx}${physicalExaminationBigResultFormMap.icon} ">
            <img src="${ctx}${physicalExaminationBigResultFormMap.icon}"  id="prgLogo"/>
            <div id="indicatorContainer"></div>
        </div>
        <div class="cell">
            <ol>
                <li style="color: ${physicalExaminationBigResultFormMap.show_color}">${physicalExaminationBigResultFormMap.check_score}分</li>
                <li  style="color: ${physicalExaminationBigResultFormMap.show_color}">${physicalExaminationBigResultFormMap.leve}</li>
            </ol>
        </div>
        <script>
            $('#indicatorContainer').radialIndicator({
                radius: 60, //圆的半径，默认50
                barBgColor: '#91fcf1', //刻度条的背景颜色,默认#eee
                barColor: {
                    0: '#f00',
                    60: '#ec890a',
                    70: '#e5bf16',
                    80: '#3da2ec',
                    100: '#28980a'
                }, //刻度条的颜色
                barWidth: 10, //刻度条的宽度，默认5
                initValue: ${physicalExaminationBigResultFormMap.check_score}, //圆形指示器初始化的值
                roundCorner:false, //刻度条是否圆角
                percentage: false, //显示百分数
                displayNumber: false
            });

        </script>
        <c:forEach items="${physicalExaminationResultFormMapList}" var="item">
            <div class="list clearfix" style="margin-top:20px;">
                <div class="list-l">
                    <ul>
                        <li>${item.name}</li>
                        <li>检测结果：<span>${item.check_value}</span></li>
                        <li>参考范围：${item.gen_min_value}-${item.gen_max_value}</li>
                    </ul>
                </div>
                <div class="list-r">
                    <ul>
                        <li style="color:${item.tzed_leve_id==null ||item.tzed_leve_id==""?item.org_show_color: item.tzed_show_color  } ">${item.item_score}分</li>
                        <li style="color:${item.tzed_leve_id==null ||item.tzed_leve_id==""?item.org_show_color: item.tzed_show_color  } ">${item.tzed_leve_id==null ||item.tzed_leve_id==""?item.orgin_leve_id: item.tzed_leve_id  }</li>
                    </ul>
                </div>
            </div>
        </c:forEach>


    </div>
</div>
</body>
</html>
