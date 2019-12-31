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
        <small>评论信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">评论信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <!-- 筛选功能部分 -->
	<form:form id="searchForm" modelAttribute="comment" action="${ctx}/cms/comment/" method="post" class="form-inline form-filter" role="form">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
		<label>文档标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="form-control"/>
        </div>
        <div class="form-group">
		<label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" />
		</div>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
	<sys:message content="${message}"/>
	<section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic" id="commentlist">
		           <thead>
		              <tr>
		              <th>评论内容</th>
		              <th>文档标题</th>
		              <th>评论人</th>
		              <th>评论IP</th>
		              <th>评论时间</th>
		              <th class="tablebox-handle">操作</th>
		              </tr>
		            </thead>
					<tbody>
					<c:forEach items="${page.list}" var="comment">
						<tr>
							<td><a href="javascript:" onclick="$('#c_${comment.id}').toggle()">${fns:abbr(fns:replaceHtml(comment.content),40)}</a></td>
							<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${comment.category.id}-${comment.contentId}${fns:getUrlSuffix()}" title="${comment.title}" onclick="return view(this.href);">${fns:abbr(comment.title,40)}</a></td>
							<td>${comment.name}</td>
							<td>${comment.ip}</td>
							<td><fmt:formatDate value="${comment.createDate}" type="both"/></td>
							<td  class="tablebox-handle"><shiro:hasPermission name="cms:comment:edit">
								<c:if test="${comment.delFlag ne '2'}"><a href="${ctx}/cms/comment/delete?id=${comment.id}${comment.delFlag ne 0?'&isRe=true':''}" 
									onclick="return confirmx('确认要${comment.delFlag ne 0?'恢复审核':'删除'}该审核吗？', this.href)">${comment.delFlag ne 0?'恢复审核':'删除'}</a></c:if>
								<c:if test="${comment.delFlag eq '2'}">
									<a href="${ctx}/cms/comment/save?id=${comment.id}">审核通过</a>
									</c:if>
									</shiro:hasPermission>
							</td>
						</tr>
						<tr id="c_${comment.id}" style="background:#fdfdfd;display:none;"><td colspan="6">${fns:replaceHtml(comment.content)}</td></tr>
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
		function view(href){
			top.$.jBox.open('iframe:'+href,'查看文档',$(top.document).width()-220,$(top.document).height()-180,{
				buttons:{"关闭":true},
				loaded:function(h){
					//$(".jbox-content", top.document).css("overflow-y","hidden");
					//$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
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
        // 数据表格功能
	    var table = $('#commentlist').DataTable({
	      "ordering":false,
	      "scrollY":   450,
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
