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
                <%--<button class="s-btn">上一页</button>
                <button class="s-btn">下一页</button>--%>
            </li>
        </ul>
    </div>

    <!--风险评估-->
    <div class="disease">
        <c:forEach items="${sickLeveCountMap}" var="entry">
            <c:choose>
                <c:when test="${entry.key ==10 }">
                    <div class="disease-red">
                        <ul class="ul-Width clearfix">
                            <c:forEach items="${entry.value}" var="item">
                                <li>
                                    <div class="disease-per disease-per-red">${item.total_rout}%</div>
                                    <div class="disease-name disease-name-b">${item.name}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:when>
            </c:choose>
        </c:forEach>

        <c:forEach items="${sickLeveCountMap}" var="entry">
            <c:choose>
                <c:when test="${entry.key ==9 }">

                    <div class="disease-orange">
                        <ul class="ul-Width clearfix">
                            <c:forEach items="${entry.value}" var="item">
                                <li>
                                    <div class="disease-per disease-per-yellow">${item.total_rout}%</div>
                                    <div class="disease-name disease-name-b">${item.name}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:when>
            </c:choose>
        </c:forEach>


        <c:forEach items="${sickLeveCountMap}" var="entry">
            <c:choose>
                <c:when test="${entry.key ==8 }">
                    <div class="disease-yellow">
                        <ul class="ul-Width clearfix">
                            <c:forEach items="${entry.value}" var="item">
                                <li>
                                    <div class="disease-per disease-per-yellow">${item.total_rout}%</div>
                                    <div class="disease-name disease-name-b">${item.name}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:when>
            </c:choose>
        </c:forEach>

        <c:forEach items="${sickLeveCountMap}" var="entry">
            <c:choose>
                <c:when test="${entry.key ==7 }">
                    <div class="disease-blue">
                        <ul class="ul-Width clearfix">
                            <c:forEach items="${entry.value}" var="item">
                                <li>
                                    <div class="disease-per disease-per-blue">${item.total_rout}%</div>
                                    <div class="disease-name disease-name-b">${item.name}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:when>
            </c:choose>
        </c:forEach>

        <c:forEach items="${sickLeveCountMap}" var="entry">
            <c:choose>
                <c:when test="${entry.key ==6 }">
                    <div class="disease-green">
                        <ul class="ul-Width clearfix">
                            <c:forEach items="${entry.value}" var="item">
                                <li>
                                    <div class="disease-per disease-per-blue">${item.total_rout}%</div>
                                    <div class="disease-name disease-name-b">${item.name}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:when>
            </c:choose>
        </c:forEach>



        <%--<div class="disease-yellow">
            <ul class="ul-Width clearfix">
                <li></li>
            </ul>
        </div>--%>
        <%--<div class="disease-blue">
            <ul class="ul-Width clearfix">
                <li>
                    <div class="disease-per disease-per-blue">55%</div>
                    <div class="disease-name disease-name-b">免疫失衡</div>
                </li>
                <li>
                    <div class="disease-per disease-per-blue">56%</div>
                    <div class="disease-name disease-name-b">泌尿障碍</div>
                </li>
                <li>
                    <div class="disease-per disease-per-blue">58%</div>
                    <div class="disease-name disease-name-b">肝胆障碍</div>
                </li>
            </ul>
        </div>--%>
        <%--<div class="disease-green">
            <ul class="ul-Width clearfix">
                <li></li>
            </ul>
        </div>--%>

    </div>

    <!--底部-->
    <div class="footer">
        <div class="footer-t">
            <ul class="clearfix">
                <li>中瑞力佳（北京）生物科技有限公司</li>
                <%--<li><button class="s-btn">下一页</button></li>--%>
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
