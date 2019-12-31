<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
     <section class="content-header">
      <h1>
      通知通告
        <small>${requestScope.oaNotify.self?'我的通知':'通告管理'}</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">通知通告</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="oaNotify" action="${ctx}/oa/oaNotify/${oaNotify.self?'self':''}" method="post" class="form-inline form-filter" role="form">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
			    <label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="200" class="form-control"/>
			</div>
			<div class="form-group">
			    <label>类型：</label>
				<form:select path="type" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<c:if test="${!requestScope.oaNotify.self}">
			<div class="form-group">
			    <label>状态：</label>
				<form:radiobuttons  element="label class='radio' style='margin-right:5px;'" path="status" items="${fns:getDictList('oa_notify_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div></c:if>
			<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
	<!-- Main content -->
    <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12"> 
          <c:if test="${!requestScope.oaNotify.self}">
         <%--  <a href="${ctx}/oa/oaNotify/email"><button type="button" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 企业通知</button></a> --%>
          <a href="${ctx}/oa/oaNotify/form"><button type="button" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 通知添加</button></a>
          </c:if>
        </div>
      </div>
     <div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic" id="oaNotifylist">
					<thead>
						<tr>
							<th>标题</th>
							<th>类型</th>
							<th>状态</th>
							<th>查阅状态</th>
							<th>更新时间</th>
							<c:if test="${!oaNotify.self}"><shiro:hasPermission name="oa:oaNotify:edit"><th class="tablebox-handle">操作</th></shiro:hasPermission></c:if>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${page.list}" var="oaNotify">
						<tr>
							<td><a href="${ctx}/oa/oaNotify/${requestScope.oaNotify.self?'view':'form'}?id=${oaNotify.id}">
								${fns:abbr(oaNotify.title,50)}
							</a></td>
							<td>
								${fns:getDictLabel(oaNotify.type, 'oa_notify_type', '')}
							</td>
							<td>
								${fns:getDictLabel(oaNotify.status, 'oa_notify_status', '')}
							</td>
							<td>
								<c:if test="${requestScope.oaNotify.self}">
									${fns:getDictLabel(oaNotify.readFlag, 'oa_notify_read', '')}
								</c:if>
								<c:if test="${!requestScope.oaNotify.self}">
									${oaNotify.readNum} / ${oaNotify.readNum + oaNotify.unReadNum}
								</c:if>
							</td>
							<td>
								<fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<c:if test="${!requestScope.oaNotify.self}"><shiro:hasPermission name="oa:oaNotify:edit"><td class="tablebox-handle">
							   <a href="${ctx}/oa/oaNotify/form?id=${oaNotify.id}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
			    				<a href="${ctx}/oa/oaNotify/email?id=${oaNotify.id}" class="btn btn-default btn-sm" title="发送邮件"><i class="fa fa-envelope-o"></i></a>
								<a href="${ctx}/oa/oaNotify/delete?id=${oaNotify.id}" onclick="return confirmx('确认要删除该通知吗？', this.href)"  class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
							</td></shiro:hasPermission></c:if>
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
     // 按钮鼠标停留效果
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
    // 数据表格功能
    var table = $('#oaNotifylist1').DataTable({
      "ordering":false,
      "scrollY":   500,
      "scrollCollapse": true,
      "info": false,
      "bFilter": false, 
      "paging":         false
    });
   
  });
</script>
</body>
</html>