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
        <small>日志信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">日志信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
	<form:form id="searchForm" action="${ctx}/sys/log/" method="post" class="form-inline form-filter" role="form">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label>操作菜单：</label><input id="title" name="title" type="text" maxlength="50" class="form-control" value="${log.title}"/>
		</div>
		<div class="form-group">
			<label>用户ID：</label><input id="createBy.id" name="createBy.id" type="text" maxlength="50" class="form-control" value="${log.createBy.id}"/>
	    </div>
		<div class="form-group">
			<label>URI：</label><input id="requestUri" name="requestUri" type="text" maxlength="50" class="form-control" value="${log.requestUri}"/>
		</div>
		<div class="form-group">
           <label>日期范围：</label>
           <div class="input-group date">
             <div class="input-group-addon">
               <i class="fa fa-calendar"></i>
             </div>
             <input type="text" id="beginDate" name="beginDate" value="<fmt:formatDate value="${log.beginDate}" pattern="yyyy-MM-dd"/>" class="form-control pull-left" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:100px"/>
           </div>
           <div class="input-group date">
             <div class="input-group-addon">
               <i class="fa fa-calendar"></i>
             </div>
             <input type="text" id="endDate" name="endDate" value="<fmt:formatDate value="${log.endDate}" pattern="yyyy-MM-dd"/>" class="form-control pull-left" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:100px"/>
           </div>
        </div>
        <div class="form-group">
			<input id="exception" name="exception" type="checkbox"${log.exception eq '1'?' checked':''} value="1"/>只查询异常信息</label>
		</div>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
	<sys:message content="${message}"/>
	<section class="content">
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic"  id="contentTable">
		            <thead>
		                <tr>
		                   <th>操作菜单</th>
		                   <th>操作用户</th>
		                   <th>所在公司</th>
		                   <th>所在部门</th>
		                   <th>URI</th>
		                   <th>提交方式</th>
		                   <th>操作者IP</th>
		                   <th>操作时间</th>
		           </thead>
		           <tbody>
		               <%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
		               <c:forEach items="${page.list}" var="log">
			           <tr>
							<td>${log.title}</td>
							<td>${log.createBy.name}</td>
							<td>${log.createBy.company.name}</td>
							<td>${log.createBy.office.name}</td>
							<td><strong>${log.requestUri}</strong></td>
							<td>${log.method}</td>
							<td>${log.remoteAddr}</td>
							<td><fmt:formatDate value="${log.createDate}" type="both"/></td>
			          </tr>
			            <c:if test="${not empty log.exception}">
			          <tr>
				            <td colspan="8" style="word-wrap:break-word;word-break:break-all;">
<%-- 					用户代理: ${log.userAgent}<br/> --%>
<%-- 					提交参数: ${fns:escapeHtml(log.params)} <br/> --%>
				        	异常信息: <br/>
					        ${fn:replace(fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}
					        </td>
			          </tr></c:if>
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
    //Initialize Select2 ElementsE
    $(".select2").select2();
    // 数据表格功能
    // 按钮鼠标停留效果
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
  });
</script>
</body>
</html>