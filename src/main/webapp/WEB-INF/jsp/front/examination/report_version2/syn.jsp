<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>疾病风险评估分析</title>
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
			<li><h1>疾病风险评估分析</h1></li>
			<li style="float:right;">
				<a class="s-btn"  href="${ctx}/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id=${physicalExaminationRecordFormMap.id}">上一页</a>
				<a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=1&recordId=${physicalExaminationRecordFormMap.id}">下一页</a>
			</li>
		</ul>
	</div>

	<!--风险评估-->
	<div class="disease">
		<c:forEach items="${sickLeveCountMap}" var="entry">
			<c:choose>
				<c:when test="${entry.key ==10 }">
					<ul class="disease-red clearfix">
						<c:forEach items="${entry.value}" var="item">
							<li>
								<div class="disease-per disease-per-red">${item.total_rout}%</div>
								<div class="disease-name disease-name-b">${item.name}</div>
							</li>
						</c:forEach>
					</ul>
				</c:when>
				<c:when test="${entry.key ==9 }">
					<ul class="disease-orange clearfix">
						<c:forEach items="${entry.value}" var="item">
							<li>
								<div class="disease-per disease-per-yellow">${item.total_rout}%</div>
								<div class="disease-name">${item.name}</div>
							</li>
						</c:forEach>
					</ul>
				</c:when>
				<c:when test="${entry.key ==8 }">
					<ul class="disease-yellow clearfix">
						<c:forEach items="${entry.value}" var="item">
							<li>
								<div class="disease-per disease-per-yellow">${item.total_rout}%</div>
								<div class="disease-name disease-name-b">${item.name}</div>
							</li>
						</c:forEach>

					</ul>
				</c:when>
				<c:when test="${entry.key ==7 }">
					<ul class="disease-blue clearfix">
						<c:forEach items="${entry.value}" var="item">
							<li>
								<div class="disease-per disease-per-blue">${item.total_rout}%</div>
								<div class="disease-name disease-name-b">${item.name}</div>
							</li>
						</c:forEach>

					</ul>
				</c:when>
				<c:when test="${entry.key ==6 }">
					<ul class="disease-green clearfix">
						<c:forEach items="${entry.value}" var="item">
							<li>
								<div class="disease-per disease-per-blue">${item.total_rout}%</div>
								<div class="disease-name disease-name-b">${item.name}</div>
							</li>
						</c:forEach>
					</ul>
				</c:when>
			</c:choose>
		</c:forEach>
	</div>

	<!--底部-->
	<div class="footer">
		<div class="footer-t">
			<ul class="clearfix">
				<li>中瑞力佳（北京）生物科技有限公司</li>
				<li>
					<a class="s-btn"  href="${ctx}/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id=${physicalExaminationRecordFormMap.id}">上一页</a>
					<a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=1&recordId=${physicalExaminationRecordFormMap.id}">下一页</a>
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
