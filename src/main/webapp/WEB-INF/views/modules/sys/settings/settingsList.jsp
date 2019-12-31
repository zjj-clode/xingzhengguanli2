<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<c:set var="isSystem" value="${fns:getUser().admin}" />
<%
	request.setAttribute("enterCode", "\n");
%>
<html>
<head>
<title>运行参数管理</title>
<meta name="decorator" content="lte" />
</head>
<body>
	<section class="content-header">
		<h1>运行参数管理</h1>
		<ol class="breadcrumb">
			<li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
			<li><a href="${ctx}">系统设置</a></li>
			<li class="active">运行参数管理</li>
		</ol>
	</section>

	<div style="margin-top: 20px;margin-left: 15px;">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/settings/">运行参数列表</a></li>
			<shiro:hasPermission name="sys:settings:edit">
				<li><a href="${ctx}/sys/settings/form">运行参数添加</a></li>
			</shiro:hasPermission>
		</ul>
	</div>

	<form:form id="searchForm" modelAttribute="settings" action="${ctx}/sys/settings/" method="post" class="form-inline form-filter">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />

		<div class="form-group">
			<label>参数名称 ：</label>
			<form:input path="name" htmlEscape="false" maxlength="50" class="form-control" />
		</div>

		<c:if test="${isSystem}">
			<div class="form-group">
				<label>参数键 ：</label>
				<form:input path="key" htmlEscape="false" maxlength="50" class="form-control" />
			</div>
			<div class="form-group">
				<label>数据类型：</label>
				<form:select path="dataType" class="form-control select2">
					<form:option value="" label=" == 不限 == " />
					<form:options items="${fns:getDictList('sys_setttings_data_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</c:if>

		<c:if test="${isSystem}">
			<div class="form-group">
				<label>参数级别：</label>
				<form:select path="systemDefined" class="form-control select2">
					<form:option value="" label=" == 不限 == " />
					<%-- <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" /> --%>
					<form:option value="1" label="系统级参数" />
					<form:option value="0" label="用户级参数" />
				</form:select>
			</div>
		</c:if>

		<button type="submit" class="btn btn-default btn-sm" title="搜索">
			<i class="fa fa-search"></i>
		</button>
	</form:form>
	<sys:message content="${message}" />
	<section class="content">
		<div class="row">
			<div class="col-xs-12 simpleTable">
				<table id="contentTable" class="ui celled table_semantic">
					<thead>
						<tr>
							<th>参数名称</th>

							<c:if test="${isSystem}">
								<th>参数键</th>
							</c:if>

							<th>参数值</th>

							<c:if test="${isSystem}">
								<th>数据类型</th>
							</c:if>

							<c:if test="${isSystem}">
								<th>参数级别</th>
							</c:if>
							<th>备注信息</th>
							<shiro:hasAnyPermissions name="sys:settings:edit,sys:settings:set">
								<th>操作</th>
							</shiro:hasAnyPermissions>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="item">
							<tr>
								<td>${item.name}</td>

								<c:if test="${isSystem}">
									<td>${item.key}</td>
								</c:if>

								<td>
									<c:set var="rows" value="${fn:length(fn:split(item.value,enterCode))}" /> <c:choose>
										<c:when test="${item.dataType eq 'String' and rows gt 1}">
											<textarea rows="${rows}" class="form-control " disabled="true"
												style="border:none;background-color:#fff;cursor:default;resize:none;padding:0;">${item.value}</textarea>
										</c:when>
										<c:otherwise>
											${item.value}
										</c:otherwise>
									</c:choose>
								</td>

								<c:if test="${isSystem}">
									<td>${fns:getDictLabel(item.dataType, 'sys_setttings_data_type', '')}</td>
								</c:if>

								<c:if test="${isSystem}">
									<td><c:choose>
											<c:when test="${'1' eq item.systemDefined}">
												<span class="text-red"> 系统级参数 </span>
											</c:when>
											<c:when test="${'0' eq item.systemDefined}">
												<span class="text-green"> 用户级参数 </span>
											</c:when>
											<c:otherwise>未知</c:otherwise>
										</c:choose></td>
								</c:if>
								<td>${item.remarks}</td>
								<shiro:hasAnyPermissions name="sys:settings:edit,sys:settings:set">

									<td class="tablebox-handle"><a href="${ctx}/sys/settings/form?id=${item.id}&queryString=${settings.queryString}"
										class="btn btn-default btn-sm" title="编辑"> <i class="fa fa-pencil"></i></a> <shiro:hasPermission name="sys:settings:edit">
											<a href="${ctx}/sys/settings/delete?id=${item.id}&queryString=${settings.queryString}" onclick="return confirmx('确认要删除该用户吗？', this.href)"
												class="btn btn-default btn-sm" title="删除"> <i class="fa fa-close"></i>
											</a>
										</shiro:hasPermission></td>
								</shiro:hasAnyPermissions>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="row">${page}</div>
			</div>
		</div>
	</section>
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script type="text/javascript">
		function page(n, s) {
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
		$(function() {
			$(".select2").select2();
			$('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
		});
	</script>
</body>
</html>