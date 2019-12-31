<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>客服管理<small>在线客服人员</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/chat/chatCustom/">客服管理</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    
    <form:form id="searchForm" modelAttribute="chatCustom" action="${ctx}/chat/chatCustom/" method="post" class="form-inline form-filter hide" role="form">
    	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
    <section class="content">
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          	<shiro:hasPermission name="chat:chatCustom:edit">
          	<a href="${ctx}/chat/chatCustom/form"><button type="button" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i>添加</button></a>
          	</shiro:hasPermission>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic" id="chatCustomListTable">
				<thead>
					<tr>
						<th>部门</th>
						<th>姓名</th>
						<th>账号</th>
						<th>优先级</th>
						<th>暂停服务</th>
						<shiro:hasAnyPermissions name="chat:chatCustom:edit,chat:chatCustom:delete"><th width="100">操作</th></shiro:hasAnyPermissions>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="item">
					<c:set var="u" value="${fns:getUserById(item.user.id)}"/>
					<tr>
						<td>${u.office.name}</td>
						<td>${item.user.name}</td>
						<td>${u.loginName}</td>
						<td>${item.priority}</td>
						<td>${fns:getDictLabel(item.delFlag, 'yes_no', '')}</td>
						<shiro:hasAnyPermissions name="chat:chatCustom:edit,chat:chatCustom:delete">
						<td class="tablebox-handle">
							<shiro:hasPermission name="chat:chatCustom:edit">
							<a  href="${ctx}/chat/chatCustom/form?id=${item.id}&queryString=${chatCustom.queryString}"  target="_top" class="btn btn-default btn-sm" title="修改"><i class="fa fa-pencil"></i></a>
							</shiro:hasPermission>
							
							<shiro:hasPermission name="chat:chatCustom:delete">
							<a href="${ctx}/chat/chatCustom/delete?id=${item.id}&queryString=${chatCustom.queryString}" onclick="return confirmx('确认要删除该客服吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
							</shiro:hasPermission>
						</td>
						</shiro:hasAnyPermissions>
					</tr>
				</c:forEach>
				</tbody>
			</table>
              <!-- 分页 -->
              <div class="row">
                ${page}
              </div>
          </div>
      </div>
    </section>
    <%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
    //Initialize Select2 Elements
    $(".select2").select2();
    // 按钮鼠标停留效果
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
    // 数据表格功能

});

function page(n,s){
	$("#pageNo").val(n);
	$("#searchForm").attr("action","${ctx}/chat/chatCustom/");
	$("#pageSize").val(s);
	$("#searchForm").submit();
   	return false;
}
</script>
</body>
</html>