<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
    <section class="content-header">
      <h1>
       内容管理
        <small>留言信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">留言信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
	<form:form id="searchForm" modelAttribute="guestbook" action="${ctx}/cms/guestbook/" method="post" class="form-inline form-filter" role="form">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
		    <label>分类：</label>
		    <form:select id="type" path="type" class="form-control select2">
		    <form:option value="" label="--请选择--"/>
		    <form:options items="${fns:getDictList('cms_guestbook')}" itemValue="value" itemLabel="label" htmlEscape="false"/>
		    </form:select>
		</div>
		<div class="form-group">
		    <label>内容 ：</label><form:input path="content" htmlEscape="false" maxlength="50" class="form-control"/>
		</div>
		<div class="form-group">
		    <label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" />
	    </div>
	    <button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
	<sys:message content="${message}"/>
	<!-- Main content -->
    <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic" id="guestbooklist">
					<thead>
					   <tr>
					      <th>留言分类</th>
					      <th>留言内容</th>
					      <th>留言人</th>
					      <th>留言时间</th>
					      <th>回复人</th>
					      <th>回复内容</th>
					      <th>回复时间</th>
					      <th class="tablebox-handle">操作</th>
					    </tr>
					 </thead>
					<tbody>
					<c:forEach items="${page.list}" var="guestbook">
						<tr>
							<td>${fns:getDictLabel(guestbook.type, 'cms_guestbook', '无分类')}</td>
							<td><a href="${ctx}/cms/guestbook/form?id=${guestbook.id}">${fns:abbr(guestbook.content,40)}</a></td>
							<td>${guestbook.name}</td>
							<td><fmt:formatDate value="${guestbook.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>${guestbook.reUser.name}</td>
							<td>${fns:abbr(guestbook.reContent,40)}</td>
							<td><fmt:formatDate value="${guestbook.reDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="tablebox-handle"><shiro:hasPermission name="cms:guestbook:edit">
								<c:if test="${guestbook.delFlag ne '2'}">
								<a href="${ctx}/cms/guestbook/delete?id=${guestbook.id}${guestbook.delFlag ne 0?'&isRe=true':''}" 
									onclick="return confirmx('确认要${guestbook.delFlag ne 0?'恢复审核':'删除'}该留言吗？', this.href)">${guestbook.delFlag ne 0?'恢复审核':'删除'}</a>
									</c:if>
								<c:if test="${guestbook.delFlag eq '2'}">
								<a href="${ctx}/cms/guestbook/form?id=${guestbook.id}" class="btn btn-default btn-sm" title="审核"><i class="fa fa-gavel"></i></a>
								</c:if>
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
        // 数据表格功能
	    var table = $('#guestbooklist').DataTable({
	      "ordering":false,
	      "scrollY":   450,
	      "scrollCollapse": true,
	      "info": false,
	      "bFilter": false, 
	      "paging":         false
	    });
	    //Initialize Select2 Elements
        $(".select2").select2();
	    // 按钮鼠标停留效果
   		 $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
   });
	</script>
</body>
</html>
  