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
	<script src="${ctx}/front-static/report_version2/js/jquery-1.12.1.min.js"></script>
	<script src="${ctx}/front-static/report_version2/js/index.js"></script>
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

	<!--细胞功能-->
	<div class="cell clearfix">
		<div class="pic"><img src="${ctx}${physicalExaminationBigResultFormMap.icon}" alt=""/></div>
		<div class="cell-msg">
			<ul class="clearfix">
				<li class="cell-name">${physicalExaminationBigResultFormMap.name}</li>
				<li class="cell-score">${physicalExaminationBigResultFormMap.check_score}分</li>
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
					<ul class="clearfix">
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li>
								<p>${item.name}</p>
								<p class="shade-num"><span>${item.item_score}</span>分</p>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:when>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 3}">
				<!--渐变图表-->
				<div class="vv">
					<ul class="clearfix">
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li><span>${item.item_score}</span>分<p>${item.name}</p></li>
						</c:forEach>
					</ul>
				</div>
			</c:when>
			<c:when test="${physicalExaminationBigResultFormMap.charts_item == 4}">
				<!--六边形图表-->
				<div class="six">
					<ul>
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li><p class="six-color"><span>${item.item_score}</span>分</p><p>${item.name}</p></li>
						</c:forEach>
					</ul>
				</div>
			</c:when>
			<c:otherwise>
				<!--柱形图-->
				<div class="column">
					<ul class="clearfix">
						<c:forEach items="${physicalExaminationResultFormMapList}" var="item">
							<li>
								<div class="column-score"><span>${item.item_score}</span>分</div>
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
						<li class="font-color"><span>${item.quanzhong_score==null ?item.item_score:item.quanzhong_score}</span>分</li>
						<li>${item.check_value}|${item.check_min}-${item.check_min}</li>
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
