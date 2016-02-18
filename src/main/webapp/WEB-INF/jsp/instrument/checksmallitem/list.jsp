<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/instrument/checksmallitem/list.js"></script>
<section class="panel panel-default">
	<header class="panel-heading font-bold">
		查询面板
	</header>
	<div class="panel-body">
		<form class="form-inline" role="form">
			<div class="form-group">
				<label class=" control-label">企业名称:</label>
				<input type="text" class="input-sm form-control" id="exampleInputEmail2" placeholder="请输入企业名称">
			</div>
			<div class="form-group">
				<label class=" control-label">建立时间:</label>
				<div class="inline v-middle">
					<div class="input-group input-s-sm">
						<input type="text" class="input-sm form-control" placeholder="开始时间">
					</div>
					<label class=" control-label">至</label>
					<div class="input-group input-s-sm">
						<input type="text" id="appendedInput" class="input-sm form-control" placeholder="开始时间">
					</div>
				</div>
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-sm">查  询</button>
			</div>
		</form>
	</div>
</section>

<section class="panel panel-default">
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
</section>



<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
	 aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close"
						data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="myModalLabel">
					模态框（Modal）标题
				</h4>
			</div>
			<div class="modal-body">
				在这里添加一些文本
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default"
						data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary">
					提交更改
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

