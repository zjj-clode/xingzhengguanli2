<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="lte" />
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.theme.default.css">
<script src="${ctxStaticLTE}/plugins/treeTable/jquery.treeTable.js"></script>
</head>
<body>
	<section class="content-header">
		<h1>
			系统管理 <small>缓存信息</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="${ctx}"><i class="fa fa-dashboard"></i> 首页</a></li>
			<li><a href="#">系统缓存</a></li>
			<li class="active">缓存列表</li>
		</ol>
	</section>
	<sys:message content="${message}" />
	<section class="content">

		<div class="row">
			<div class="col-xs-12 simpleTable" style="overflow:auto;">
				<form id="listForm" method="post">
					<table id="treeTable" class="ui celled table_semantic">
						<colgroup>
							<col width="10%">
							<col width="10%">
							<col width="10%">
							<col width="70%">
						</colgroup>
						<thead>
							<tr>
								<th>缓存名称</th>
								<th>缓存键</th>
								<th>缓存值类型</th>
								<th>缓存值</th>
							</tr>
						</thead>
						<tbody id="treeTableList">


							<c:forEach items="${cacheMap}" var="entry">

								<tr data-tt-id="${entry.key}" data-tt-parent-id="0">
									<td>${entry.key }</td>
									<td colspan="3"><a href="${ctx}/sys/cache/delete?cacheName=${entry.key }" onclick="return confirmx('危险操作，确定要执行吗？', this.href)" class="btn btn-danger btn-sm" title="删除"><i
											class="fa fa-close"></i> 清除此名称下所有缓存</a></td>
								</tr>

								<c:set var="cache" value="${entry.value}" />
								<c:forEach items="${cache.keys}" var="key">
									<tr data-tt-id="${key}" data-tt-parent-id="${entry.key}">
										<td></td>
										<td class="tablebox-handle">${key }<a href="${ctx}/sys/cache/delete?cacheName=${entry.key }&key=${key }" onclick="return confirmx('危险操作，确定要执行吗？', this.href)"
											class="btn btn-warning btn-sm" style="margin-left:20px;" title="删除"><i class="fa fa-close"></i> 清除此键对应的缓存</a>
										</td>
										<td>${cache.get(key).objectValue.getClass().getName() }</td>
										<td>${cache.get(key).objectValue }</td>
									</tr>
								</c:forEach>

							</c:forEach>

						</tbody>
					</table>
				</form>
			</div>
		</div>
	</section>
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treetable({
				clickableNodeNames : true,
				isPadding : true,
				indent : 20,
				expandable : true,
				onInitialized : function() {
					$("#treeTable").css('display', 'inline-table');
					$("#treeTable tbody tr.collapsed").each(function(){
						$(this).find("td:first").trigger('click');
					});
				}
			});
			$("#treeTable tbody").on("mousedown", "tr", function() {
				$(".selected").not(this).removeClass("selected");
				$(this).toggleClass("selected");
			});
		});
	</script>
</body>
</html>