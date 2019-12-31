<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <%@include file="/WEB-INF/views/include/ltehead.jsp" %>
</head>
<body>
    <section class="content-header">
      <h1>
                        链接管理
        <small>链接信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">文章信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    <!-- 筛选功能部分 -->
    <form:form id="searchForm" modelAttribute="link" action="${ctx}/cms/link/" method="post" class="form-inline form-filter" role="form">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="form-group">
           <label>标题：</label>
           <form:input path="title" htmlEscape="false" maxlength="50" class="form-control" placeholder="title" style="width:220px"/>
        </div>
        <div class="form-group"><label>状态：</label>
		   <form:select path="delFlag" class="form-control select2">
			  <form:options items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		   </form:select>
	    </div>
	    <button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
	<sys:message content="${message}"/>
	
   <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          <a href="${ctx}/cms/link/form?category.id=${link.category.id}" target="_top" class="btn btn-info pull-right tjTip"> <i class="fa fa-plus" aria-hidden="true"></i> 添加链接 </a>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic" id="linklist">
	                <thead>
	                  <tr>
	                    <th>标题</th>
	                    <th>权重</th>
	                    <th>发布者</th>
	                    <th>发布时间</th>
	                    <th class="tablebox-handle">操作</th>
	                  </tr>
	                </thead>
					<tbody>
					<c:forEach items="${page.list}" var="link">
						<tr>
							<td><a href="${ctx}/cms/link/form?id=${link.id}" title="${link.title}" class="newsCont" style="color: ${link.color}"  target="_top">${fns:abbr(link.title,40)}</a></td>
							<td>${link.weight}</td>
							<td>${link.user.name}</td>
							<td><fmt:formatDate value="${link.updateDate}" type="both"/></td>
							<td class="tablebox-handle">
								<a href="${link.href}" target="_blank" class="btn btn-default btn-sm" title="访问"><i class="fa fa-external-link"></i></a>
								<shiro:hasPermission name="cms:link:edit">
									<c:if test="${link.category.allowComment eq '1'}"><shiro:hasPermission name="cms:comment:view">
										<a href="${ctx}/cms/comment/?module=link&contentId=${link.id}&delFlag=2" onclick="return viewComment(this.href);" class="btn btn-default btn-sm" title="平论"><i class="fa fa-pencil"></i></a>
									</shiro:hasPermission></c:if>
				    				<a href="${ctx}/cms/link/form?id=${link.id}"  target="_top" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
				    				<shiro:hasPermission name="cms:link:audit">
										<a href="${ctx}/cms/link/delete?id=${link.id}${link.delFlag ne 0?'&isRe=true':''}&categoryId=${link.category.id}" onclick="return confirmx('确认要${link.delFlag ne 0?'发布':'删除'}该文章吗？', this.href)"  class="btn btn-default btn-sm" title="${link.delFlag ne 0?'发布':'删除'}"><i class="fa fa-close"></i></a>
									</shiro:hasPermission>
								</shiro:hasPermission>
							</td>
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
<script type="text/javascript">
	function viewComment(href){
		top.$.jBox.open('iframe:'+href,'查看评论',$(top.document).width()-220,$(top.document).height()-120,{
			buttons:{"关闭":true},
			loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
				$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
				$("body", h.find("iframe").contents()).css("margin","10px");
			}
		});
		return false;
	}
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
    $("#linklist").DataTable({
      "aoColumns": [
      null,
      null,
      null,
      null,
      { "asSorting": [ ] }
      ],
      "fixedHeader": true,
      "paging": false,
      "bFilter": false, 
      "info": false
    });
    // 按钮鼠标停留效果
    
  });
</script>
</body>
</html>