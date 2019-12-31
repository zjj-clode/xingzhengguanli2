<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
    <section class="content-header">
      <h1>
                        系统管理
        <small>字典信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">字典信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
	<form:form id="searchForm" modelAttribute="dict" action="${ctx}/sys/dict/" method="post" class="form-inline form-filter" role="form">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label>键值 ：</label>
			<form:input path="value" htmlEscape="false" maxlength="50" class="form-control"/>
		</div>
		<div class="form-group">
			<label>标签 ：</label>
			<form:input path="label" htmlEscape="false" maxlength="50" class="form-control"/>
		</div>
		<div class="form-group">
			<label>类型：</label>
			<form:select id="type" path="type" class="form-control select2">
			<form:option value="" label="--请选择--"/>
			<form:options items="${typeList}" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
			<label>描述 ：</label>
			<form:input path="description" htmlEscape="false" maxlength="50" class="form-control"/>
		</div>
	    <button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
    </form:form>
    <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          <a href="${ctx}/sys/dict/form"><button type="button" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 字典添加</button></a>
        </div>
      </div>
	<sys:message content="${message}"/>
	<div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic" id="dictlist">
					<thead>
						<tr>
							<th>键值</th>
							<th>标签</th>
							<th>类型</th>
							<th>描述</th>
							<th>排序</th>
							<th style="width: 100px" class="tablebox-handle">操作</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${page.list}" var="dict">
						<tr>
							<td>${dict.value}</td>
							<td><a href="${ctx}/sys/dict/form?id=${dict.id}">${dict.label}</a></td>
							<td><a href="javascript:" onclick="$('#type').val('${dict.type}');$('#searchForm').submit();return false;">${dict.type}</a></td>
							<td>${dict.description}</td>
							<td>${dict.sort}</td>
							<td class="tablebox-handle"><shiro:hasPermission name="sys:dict:edit">
			    				<a href="${ctx}/sys/dict/form?id=${dict.id}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
								<a href="${ctx}/sys/dict/delete?id=${dict.id}&type=${dict.type}" onclick="return confirmx('确认要删除该字典吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
			    				<a href="<c:url value='${fns:getAdminPath()}/sys/dict/form?type=${dict.type}&sort=${dict.sort+10}'><c:param name='description' value='${dict.description}'/></c:url>" class="btn btn-default btn-sm" title="添加键值"><i class="fa fa-plus"></i></a></a>
							</shiro:hasPermission></td>
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
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script>
  function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
      	return false;
  }
 $(function () {
    //Initialize Select2 Elements
    $(".select2").select2();
    // 数据表格功能
    var table = $('#dictlist1').DataTable({
      "ordering": false,
      "scrollY":        500,
      "scrollCollapse": true,
      "info": false,
      "bFilter": false, 
      "paging":         false
    });
    // 按钮鼠标停留效果
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
  });
</script>
</body>
</html>