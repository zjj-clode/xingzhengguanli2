<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>业务操作日</title>
	<meta name="decorator" content="default"/>
	<shiro:hasPermission name="common:businessLog:delete">
	<script type="text/javascript" src="${ctxStatic}/modules/gy/js/batchDelete.js"></script>
	</shiro:hasPermission>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(function(){
			//批量删除
			$("#btnBatchDelete").click(function(){
				if($("form#batchDeleteForm input:checked[name='ids']").length==0){
					$.jBox.tip("请勾选数据后再执行删除操作！",'error');
					return false;
				}
				$.jBox.confirm("确认要批量删除选中的数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#batchDeleteForm").submit();
					}
				},{buttonsFocus:1});
				return false;
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
 		<li class="active"><a href="${ctx}/common/businessLog/">日志列表</a></li> 
 	</ul> 
	<form:form id="searchForm" modelAttribute="businessLog" action="${ctx}/common/businessLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>操作人姓名：</label>
				<form:input path="createBy.name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>业务类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label="===请选择==="/>
					<form:options items="${fns:getDictList('business_log_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li>
				<label class="control-label" style="width: auto;margin-left: 20px;">时间范围：</label>
				<input name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${businessLog.beginDate}" pattern="yyyy-MM-dd HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:true});"/> -
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${businessLog.endDate}" pattern="yyyy-MM-dd HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:true});"/>
			</li>
			<li><label>操作IP：</label>
				<form:input path="ip" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<shiro:hasPermission name="common:businessLog:delete">
				<input id="btnBatchDelete" class="btn btn-danger" type="button" value="批量删除"/>
				</shiro:hasPermission>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<form action="${ctx}/common/businessLog/deleteBatch" method="post" id="batchDeleteForm">
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<shiro:hasPermission name="common:businessLog:delete">
				<th><input type="checkbox"></th>
				</shiro:hasPermission>
				<th>日志标题</th>
				<th>业务类型</th>
				<th>变更前数据</th>
				<th>变更后数据</th>
				<th>操作用户</th>
				<th>操作时间</th>
				<th>操作IP</th>
				<th>备注信息</th>
				<shiro:hasPermission name="common:businessLog:delete">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="item">
			<tr>
				<shiro:hasPermission name="common:businessLog:delete">
				<td><input type="checkbox" name="ids" value="${item.id}"></td>
				</shiro:hasPermission>
				<td>${item.title}</td>
				<td>${fns:getDictLabel(item.type, 'business_log_type', '')}</td>
				<td>${item.preChangeData}</td>
				<td>${item.postChangeData}</td>
				<td>${item.createBy.name}</td>
				<td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				<td>${item.ip}</td>
				<td>${item.remarks}</td>
				<shiro:hasPermission name="common:businessLog:delete">
				<td>
					<a href="${ctx}/common/businessLog/delete?id=${item.id}&queryString=${businessLog.queryString}" onclick="return confirmx('确认要删除吗？', this.href)">删除</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</form>
	<div class="pagination">${page}</div>
</body>
</html>