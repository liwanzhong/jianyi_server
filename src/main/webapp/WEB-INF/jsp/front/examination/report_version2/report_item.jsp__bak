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
					<c:when test="${pageView.pageNow==pageView.pageCount}">

					</c:when>
					<c:otherwise>
						<a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=${pageView.pageNow+1}&recordId=${physicalExaminationBigResultFormMap.examination_record_id}">下一页</a>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${pageView.pageNow-1==0}">
						<a class="s-btn" href="${ctx}/examination/physicalExamination/report.shtml?physicalExaminationRecordFormMap.id=${physicalExaminationBigResultFormMap.examination_record_id}">上一页</a>
					</c:when>
					<c:otherwise>
						<a class="s-btn" href="${ctx}/examination/physicalExamination/report_big_item.shtml?pageNow=${pageView.pageNow-1}&recordId=${physicalExaminationBigResultFormMap.examination_record_id}">上一页</a>
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
					<%--<span><img src="img/2.png" alt=""/></span>
					<span><img src="img/3.png" alt=""/></span>
					<span><img src="img/1.png" alt=""/></span>
					<span><img src="img/0_0.png" alt=""/></span>
					<span><img src="img/0.png" alt=""/></span>--%>
				</li>
				<li class="cell-detail">${physicalExaminationBigResultFormMap.tips_content}</li>
			</ul>
		</div>
	</div>
	<div class="w">
		<!--柱形图-->
		<div class="column">
			<ul class="clearfix">
				<li>
					<div class="column-score color-green"><span>97.89</span>分</div>
					<div class="column-bg">
						<div class="column-real column-real-green"></div>
					</div>
					<span>细胞膜蛋白</span>
				</li>
				<li>
					<div class="column-score color-blue"><span>88.60</span>分</div>
					<div class="column-bg">
						<div class="column-real column-real-blue"></div>
					</div>
					<span>细胞含氧量</span>
				</li>
				<li>
					<div class="column-score color-yellow"><span>79.50</span>分</div>
					<div class="column-bg">
						<div class="column-real column-real-yellow"></div>
					</div>
					<span>细胞含水量</span>
				</li>
				<li>
					<div class="column-score color-green"><span>90.52</span>分</div>
					<div class="column-bg">
						<div class="column-real column-real-green"></div>
					</div>
					<span>肌细胞比重</span>
				</li>
				<li>
					<div class="column-score color-blue"><span>80.52</span>分</div>
					<div class="column-bg">
						<div class="column-real column-real-blue"></div>
					</div>
					<span>脂肪细胞</span>
				</li>
				<li>
					<div class="column-score color-blue"><span>88.56</span>分</div>
					<div class="column-bg">
						<div class="column-real column-real-blue"></div>
					</div>
					<span>细胞代谢</span>
				</li>
			</ul>
		</div>
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
				<ul class="clearfix">
					<li><em class="bg-green"></em>细胞膜蛋白</li>
					<li class="color-green">97.89分</li>
					<li>123.563|23.03-128.03</li>
					<li>直接与免疫功能相关，提示脂肪和蛋白质代谢发生紊乱，有多系统疾病的可能性。</li>
				</ul>
				<ul class="clearfix">
					<li><em class="bg-blue"></em>细胞含氧量</li>
					<li class="color-blue">88.60分</li>
					<li>123.563|1.36-5.89</li>
					<li>细一切慢性病的根源，和心血管系统疾病、呼吸系统疾病以及肿瘤病变有密切关系。</li>
				</ul>
				<ul class="clearfix">
					<li><em class="bg-yellow"></em>细胞含水量</li>
					<li class="color-yellow">79.52分</li>
					<li>123.563|23.03-128.03</li>
					<li>会导致一系列的身体反应，如口渴、内火旺盛、皮肤干燥等。一般与肾脏功能相关。</li>
				</ul>
				<ul class="clearfix">
					<li><em class="bg-green"></em>肌细胞比重</li>
					<li class="color-green">90.21分</li>
					<li>123.563|23.03-128.03</li>
					<li>是躯体和四肢运动，消化、呼吸、循环、排泄等生理活动的动力来源。关系到骨</li>
				</ul>
				<ul class="clearfix">
					<li><em class="bg-blue"></em>脂肪细胞</li>
					<li class="color-blue">85.2分</li>
					<li>123.563|23.03-128.03</li>
					<li>直接与脂肪储存和脂肪代谢有关。</li>
				</ul>
				<ul class="clearfix">
					<li><em class="bg-blue"></em>细胞代谢</li>
					<li class="color-blue">88.56分</li>
					<li>123.563|23.03-128.03</li>
					<li>细胞代谢速度，直接反应了人体的衰老程度，新城代谢是生命力的重要标志。</li>
				</ul>
			</div>
		</div>
	</div>

	<!--底部-->
	<div class="footer">
		<div class="footer-t">
			<ul class="clearfix">
				<li>中瑞力佳（北京）生物科技有限公司</li>
				<li><button class="s-btn">下一页</button></li>
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
