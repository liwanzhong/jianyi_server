<%--
  Created by IntelliJ IDEA.
  User: liwanzhong
  Date: 2016/4/9
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>检测结果</title>
</head>
<body>
<table>
    <tr>
        <td>
            姓名：
        </td>
        <td>
            检测时间：${record.check_time}
        </td>
        <td>
            得分:${physicalExaminationMainReportFormMap.check_total_score}
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <c:forEach items="${bigResultFormMapList}" var="item">
               <table border="1">
                   <tr>
                       <td>${item.name}</td>
                       <td>基准低值</td>
                       <td>基准高值</td>
                       <td>区间值</td>
                       <td>区间分值</td>
                       <td>测量值</td>
                       <td> 得分</td>
                       <td>功能等级（${item.leve}）</td>
                       <td>权重</td>
                       <td>&nbsp;</td>
                   </tr>
                   <c:set var="quanzhong"></c:set>
                   <c:forEach items="${resultList}" var="smItem">
                       <c:if test="${item.big_item_id ==smItem.bit_item_id }">
                           <tr>
                               <td>${smItem.name}</td>
                               <td>${smItem.gen_min_value}</td>
                               <td>${smItem.gen_max_value}</td>
                               <td>${smItem.gen_in_value}</td>
                               <td>${smItem.in_value_score}</td>
                               <td>${smItem.check_value}</td>
                               <td> ${smItem.item_score}</td>
                               <td>${smItem.orgin_leve_id}</td>
                               <td>${smItem.gen_quanzhong}</td>
                               <td>${smItem.quanzhong_score}</td>
                           </tr>
                           <c:set var="quanzhong" value="${quanzhong+smItem.gen_quanzhong}"></c:set>
                       </c:if>

                   </c:forEach>
                   <tr>
                       <td>子项总分</td>
                       <td></td>
                       <td></td>
                       <td></td>
                       <td></td>
                       <td></td>
                       <td> </td>
                       <td></td>
                       <td>${quanzhong}</td>
                       <td>${item.check_score}</td>
                   </tr>
               </table>
                <br>
                <hr>
                <br>
            </c:forEach>
        </td>
    </tr>
</table>
</body>
</html>
