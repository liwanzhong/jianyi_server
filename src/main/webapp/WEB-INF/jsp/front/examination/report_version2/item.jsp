<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
	double randomCode = Math.random();
%>

<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>双螺旋机能检测报告</title>
	<link type="text/css" rel="stylesheet" href="${ctx}/front-static/report_version2/css/base.css">
	<link type="text/css" rel="stylesheet" href="${ctx}/front-static/report_version2/css/index.css">
	<%--<link type="text/css" rel="stylesheet" href="${ctx}/front-static/report_version2/css/index-2.css">--%>
	<script src="${ctx}/front-static/report_version2/js/jquery-1.12.1.min.js"></script>
	<script src="${ctx}/front-static/report_version2/js/index.js?randomCode=<%=randomCode%>"></script>
	<c:if test="${physicalExaminationBigResultFormMap.charts_item == 7}">
		<script type="text/javascript" src="${ctx}/js/fushioncharts/fusioncharts.js"></script>
		<script type="text/javascript" src="${ctx}/js/fushioncharts/fusioncharts.charts.js"></script>
		<script type="text/javascript" src="${ctx}/js/fushioncharts/themes/fusioncharts.theme.fint.js"></script>
	</c:if>

	<script>
		//初始化评分等级
		pingfen ={
			<c:forEach items="${cfPingfenLeveFormMapList}" var="item" varStatus="status">
			'pingfen_${item.id}min': ${item.pingfen_min},
			'pingfen_${item.id}max': ${item.pingfen_max},
			</c:forEach>
		};
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
				<c:choose>
					<c:when test="${pageView.pageNow-1==0}">
						<a class="s-btn" href="${ctx}/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id=${physicalExaminationBigResultFormMap.examination_record_id}">上一页</a>
					</c:when>
					<c:otherwise>
						<a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=${pageView.pageNow-1}&recordId=${physicalExaminationBigResultFormMap.examination_record_id}">上一页</a>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${pageView.pageNow==pageView.pageCount}">
					</c:when>
					<c:otherwise>
						<a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=${pageView.pageNow+1}&recordId=${physicalExaminationBigResultFormMap.examination_record_id}">下一页</a>
					</c:otherwise>
				</c:choose>
			</li>
		</ul>
	</div>
	<div class="msg">
		<span class="name">${physicalExaminationRecordFormMap.name}</span>
		<span class="sex">${physicalExaminationRecordFormMap.sex==1?"男":"女"}</span>
		<span class="birth">${physicalExaminationRecordFormMap.birthday}</span>
		<span class="sex">年龄：${physicalExaminationRecordFormMap.age}</span>
		<span class="sex">BMI：${physicalExaminationRecordFormMap.bmi}</span>
		<span class="time">   <fmt:formatDate value="${physicalExaminationRecordFormMap.check_time}" pattern="yyyy-MM-dd"/>检测</span>
	</div>
	<!--细胞功能-->
	<div class="cell clearfix">
		<div class="pic"><img src="${ctx}${physicalExaminationBigResultFormMap.icon}" alt=""/></div>
		<div class="cell-msg">
			<ul class="clearfix">
				<li class="cell-name">${physicalExaminationBigResultFormMap.name}</li>
				<li class="cell-score" style="color: ${physicalExaminationBigResultFormMap.show_color}"> <fmt:formatNumber value="${physicalExaminationBigResultFormMap.check_score}" pattern="#0.00"/>分</li>
				<li class="cell-color">
					<c:forEach items="${leveCountGroupBy}" var="entry">
						<c:choose>
							<c:when test="${entry.key ==1 }">
								<span class="lev-green">${entry.value}</span>
							</c:when>
							<c:when test="${entry.key ==2 }">
								<span class="lev-blue">${entry.value}</span>
							</c:when>
							<c:when test="${entry.key ==3 }">
								<span class="lev-yellow">${entry.value}</span>
							</c:when>
							<c:when test="${entry.key ==4 }">
								<span class="lev-orange">${entry.value}</span>
							</c:when>
							<c:when test="${entry.key ==5 }">
								<span class="lev-red">${entry.value}</span>
							</c:when>
						</c:choose>
					</c:forEach>

				</li>
				<li class="cell-detail">${physicalExaminationBigResultFormMap.tips_content}</li>
			</ul>
		</div>
	</div>
	<div class="w">
		<c:choose>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 2}">
				<!--渐变图表-->
				<div class="shade">
					<ul class="ul-w clearfix">
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li>
								<p>${item.name}</p>
								<p class="shade-num"><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分</p>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:when>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 3}">
				<c:choose>
					<c:when test="${fn:length(physicalExaminationResultFormMapList)==6}">
						<!--渐变图表-->
						<div class="vv">
							<ul class="clearfix">
								<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
									<li><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分<p>${item.name}</p></li>
								</c:forEach>
							</ul>
						</div>
					</c:when>
					<c:when test="${fn:length(physicalExaminationResultFormMapList)==5}">
						<div class="vvv">
							<ul class="clearfix">
								<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
									<li><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分<p>${item.name}</p></li>
								</c:forEach>
							</ul>
						</div>
					</c:when>
				</c:choose>

			</c:when>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 4}">
				<!--六边形图表-->
				<div class="six">
					<ul>
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li><p class="six-color"><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分</p><p>${item.name}</p></li>
						</c:forEach>
					</ul>
				</div>
			</c:when>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 1}">
				<!--柱形图-->
				<div class="column">
					<ul class="clearfix">
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li>
								<div class="column-score"><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分</div>
								<div class="column-bg">
									<div class="column-h">
										<div class="column-real"></div>
									</div>
								</div>
								<span>${item.name}</span>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:when>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 5}">
				<!--圆形图表-->
				<div class="column-2">
					<ul class="ul-w clearfix">
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li>
								<div class="max">
									<div class="max-top"><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分</div>
									<div class="max-main"></div>
									<div class="max-b">${item.name}</div>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:when>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 6}">
				<div class="column-3">
					<ul class="ul-w clearfix">
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li>
								<div class="up-score"><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分</div>
								<div class="up-sanjiao"></div>
								<div class="up-main">
									<div class="up-main-w"></div>
								</div>
								<div class="up-l"></div>
								<div class="up-word">${item.name}</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:when>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 7}">
				<!--折线图表-->
				<div  id="chart-container_zhexian"></div>
				<script>
					FusionCharts.ready(function () {
						var satisfactionChart = new FusionCharts({
							type: 'line',
							renderAt: 'chart-container_zhexian',
							id: 'myChart',
							width: '680',
							height: '260',
							dataFormat: 'json',
							dataSource: {
								"chart": {
//                    "caption": "Customer Satisfaction Averages",
//                    "subcaption": "Last week",
//                    "xaxisname": "Day",
									"yaxisname": "得分(0-100分)",
									"numbersuffix": "分",
									"showvalues": "0",
									//Anchor Cosmatics
									"anchorRadius": "15",
									"anchorBorderThickness": "1",
									"yAxisMaxValue": "100",
									"showValues": "1",
									"lineThickness":"10",
//									"numVDivLines": "6",
//									"vDivLineColor": "#99ccff",
//									"vDivLineThickness": "1",
//									"vDivLineAlpha": "70",
//									"vDivLineDashed": "1",
//									"vDivLineDashLen":"5",
//									"vDivLineDashGap":"3",

									"baseFontSize" : "14",
//                    "canvasPadding": "0",
									//Theme
									"theme" : "fint"

								},
								"data": ${chartJSON}
							}
						});

						satisfactionChart.render();

					});
				</script>
			</c:when>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 8}">
				<div class="column-4">
					<ul class="ul-w clearfix">
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li>
								<div class="column-4-main">
									<%--<div class="column-4-word">体<br/>液<br/>酸<br/>碱<br/>度</div>--%>
									<div class="column-4-word">${item.name}</div>
									<div class="column-4-score"><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分</div>
								</div>
							</li>
						</c:forEach>

						<%--<li>
							<div class="column-4-main">
								<div class="column-4-word">肾<br/>脏<br/>排<br/>毒<br/>功<br/>能</div>
								<div class="column-4-score"><span>70.16</span>分</div>
							</div>
						</li>
						<li>
							<div class="column-4-main">
								<div class="column-4-word">膀<br/>胱<br/>功<br/>能</div>
								<div class="column-4-score"><span>42.16</span>分</div>
							</div>
						</li>
						<li>
							<div class="column-4-main">
								<div class="column-4-word">激<br/>素<br/>分<br/>泌</div>
								<div class="column-4-score"><span>82.16</span>分</div>
							</div>
						</li>
						<li>
							<div class="column-4-main">
								<div class="column-4-word">肾<br/>脏<br/>代<br/>谢</div>
								<div class="column-4-score"><span>80</span>分</div>
							</div>
						</li>
						<li>
							<div class="column-4-main">
								<div class="column-4-word">肾<br/>脏<br/>代<br/>谢</div>
								<div class="column-4-score"><span>80</span>分</div>
							</div>
						</li>
						<li>
							<div class="column-4-main">
								<div class="column-4-word">肾<br/>脏<br/>代<br/>谢</div>
								<div class="column-4-score"><span>80</span>分</div>
							</div>
						</li>--%>

					</ul>
				</div>
			</c:when>
			<c:otherwise>
				<!--柱形图-->
				<div class="column">
					<ul class="clearfix">
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li>
								<div class="column-score"><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分</div>
								<div class="column-bg">
									<div class="column-h">
										<div class="column-real"></div>
									</div>
								</div>
								<span>${item.name}</span>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:otherwise>
		</c:choose>
		<!--表格-->
		<div class="table">
			<div class="thead">
				<ul class="clearfix">
					<li>项目</li>
					<li>检测得分</li>
					<li>检测值|参考值</li>
					<li>指标含义</li>
				</ul>
			</div>
			<div class="tbody">
				<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
					<ul class="clearfix">
						<li><em></em>${item.name}</li>
						<li class="font-color"><span><fmt:formatNumber value="${item.item_score}" pattern="#0.00"/></span>分</li>
						<%--<li><fmt:formatNumber value="${item.check_value}" pattern="#0.00"/>|<fmt:formatNumber value="${item.min_value}" pattern="#0.00"/>-<fmt:formatNumber value="${item.max_value}" pattern="#0.00"/></li>--%>
						<li><fmt:formatNumber value="${item.check_value}" pattern="#0.00"/>|<fmt:formatNumber value="${item.min_value+item.in_value_score*0}" pattern="#0.00"/>-<fmt:formatNumber value="${item.min_value+item.in_value_score*20}" pattern="#0.00"/></li>
						<li>${item.check_desc}</li>
					</ul>
				</c:forEach>
			</div>
		</div>
	</div>

	<!--底部-->
	<div class="footer">
		<div class="footer-t">
			<ul class="clearfix">
				<li>中瑞力佳（北京）生物科技有限公司</li>
				<li>
					<c:choose>
						<c:when test="${pageView.pageNow-1==0}">
							<a class="s-btn" href="${ctx}/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id=${physicalExaminationBigResultFormMap.examination_record_id}">上一页</a>
						</c:when>
						<c:otherwise>
							<a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=${pageView.pageNow-1}&recordId=${physicalExaminationBigResultFormMap.examination_record_id}">上一页</a>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${pageView.pageNow==pageView.pageCount}">

						</c:when>
						<c:otherwise>
							<a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=${pageView.pageNow+1}&recordId=${physicalExaminationBigResultFormMap.examination_record_id}">下一页</a>
						</c:otherwise>
					</c:choose>
				</li>
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
</body>
</html>
