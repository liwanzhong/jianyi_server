<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>	
<script type="text/javascript" src="${pageContext.request.contextPath}/js/organization/sub_point/list.js"></script>
<div>
	<a href="javascript:void(backtolastpage());">返回企业列表</a>
</div>
	<div class="m-b-md">
		<form class="form-inline" role="form" id="searchForm" name="searchForm">
			<input type="hidden" id="entid" name="endid" value="${entid}">
			<div class="form-group">
				<label class="control-label"> <span
					class="h4 font-thin v-middle">检测点名称:</span></label> <input
					class="input-medium ui-autocomplete-input" id="name"
					name="enterpriseFormMap.name">
			</div>
			<div class="form-group">
				<label class="control-label">
					<span class="h4 font-thin v-middle">建立时间:</span>
				</label>
				<input class="input-medium ui-autocomplete-input" id="inserttime_start" name="enterpriseFormMap.insert_time">至
				<input class="input-medium ui-autocomplete-input" id="inserttime_end" name="enterpriseFormMap.insert_time">
			</div>
			<a href="javascript:void(0)" class="btn btn-default" id="search">查询</a>
		</form>
	</div>
	<header class="panel-heading">
	<div class="doc-buttons">
		<c:forEach items="${res}" var="key">
			${key.description}
		</c:forEach>
	</div>
	</header>
	<div class="table-responsive">
		<div id="paging" class="pagclass"></div>
	</div>
	
	<div class="table-responsive">
		<div id="paging2" class="pagclass"></div>
	</div>
