<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <%@include file="/WEB-INF/views/include/ltehead.jsp" %>
</head>
<body>
    <section class="content-header">
      <h1>
                        视频管理
        <small>视频信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">文章信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    <!-- 筛选功能部分 -->
    <form:form id="searchForm" modelAttribute="movie" action="${ctx}/cms/movie/" method="post" class="form-inline form-filter" role="form">
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
          <a href="${ctx}/cms/movie/form?category.id=${movie.category.id}" target="_top" class="btn btn-info pull-right tjTip"> <i class="fa fa-plus" aria-hidden="true"></i> 添加视频 </a>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic" id="movielist">
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
					<c:forEach items="${page.list}" var="movie">
						<tr>
							<td><a href="${ctx}/cms/movie/form?id=${movie.id}" title="${movie.title}" class="newsCont" style="color: ${movie.color}" target="_top">${fns:abbr(movie.title,40)}</a></td>
							<td>${movie.weight}</td>
							<td>${movie.user.name}</td>
							<td><fmt:formatDate value="${movie.updateDate}" type="both"/></td>
							<td class="tablebox-handle">
								<a href="${pageContext.request.contextPath}${fns:getFrontPath()}/movieview-${movie.category.id}-${movie.id}${fns:getUrlSuffix()}"  
									onclick="return viewMovie(this.href);" class="btn btn-default btn-sm" title="访问" target="_blank"><i class="fa fa-external-link"></i></a>
								<shiro:hasPermission name="cms:movie:edit">
									<c:if test="${movie.category.allowComment eq '1'}"><shiro:hasPermission name="cms:comment:view">
										<a href="${ctx}/cms/comment/?module=movie&contentId=${movie.id}&delFlag=2" onclick="return viewComment(this.href);" 
											class="btn btn-default btn-sm" title="评论"><i class="fa fa-comment-o"></i></a>
									</shiro:hasPermission></c:if>
				    				<a href="${ctx}/cms/movie/form?id=${movie.id}" target="_top"
				    					class="btn btn-default btn-sm" title="修改"><i class="fa fa-pencil"></i></a>
				    				<shiro:hasPermission name="cms:movie:edit">
										<a href="${ctx}/cms/movie/delete?id=${movie.id}${movie.delFlag ne 0?'&isRe=true':''}&categoryId=${categoryId}" onclick="return confirmx('确认要${movie.delFlag ne 0?'发布':'删除'}该视频吗？', this.href)" 
											class="btn btn-default btn-sm delModal" title="${movie.delFlag ne 0?'发布':'删除'}"><i class="fa fa-close"></i></a>
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
	
	function viewMovie(href){
	
		top.$.jBox.open('iframe:'+href,'预览视频',750,500,{
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
    $("#movielist").DataTable({
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