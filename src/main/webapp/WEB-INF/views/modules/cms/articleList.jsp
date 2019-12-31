<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<!-- <meta name="decorator" content="lte"/> -->
<%@include file="/WEB-INF/views/include/ltehead.jsp"%>
</head>
<body>
	<section class="content-header">
		<h1>
			内容管理 <small>文章信息</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
			<li><a href="#">文章信息</a></li>
			<li class="active">信息列表</li>
		</ol>
	</section>
	<sys:message content="${message}" />
	
	<c:if test="${article.category.parent.id ne '5334d4a1b5b24f688f71a8822cfd507b' || fns:getConfig('custmor.code') ne 'yunzhi'}">
		<!-- 筛选功能部分 -->
		<form:form id="searchForm" modelAttribute="article" action="${ctx}/cms/article/" method="post" class="form-inline form-filter" role="form">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
			<form:hidden path="category.id"/>
			<div class="form-group">
				<label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="50" class="form-control" placeholder="文章标题，支持模糊查询" style="width:220px" />
			</div>
			<div class="form-group">
				<label>状态：</label>
				<form:select path="delFlag" class="form-control select2">
					<form:options items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<button type="submit" class="btn btn-default btn-sm" title="搜索">
				<i class="fa fa-search"></i>
			</button>
		</form:form>

		<section class="content">
			<div class="row tableTopBtn">
				<div class="col-xs-12">
					 
					<%-- <a style="margin-left: 20px;" href="${ctx}/cms/article/staticAll" onclick="ajaxPost('全部静态化，此操作会比较耗时，确认要执行吗？',this.href);return false;" class="btn btn-info pull-right tjTip"> <i
						class="fa fa-indent" aria-hidden="true"></i> 全部静态化
					</a>
					<a style="margin-left: 20px;" href="${ctx}/cms/article/staticIndex" onclick="ajaxPost('确认要执行首页静态化吗？',this.href);return false;" class="btn btn-info pull-right tjTip"> <i
						class="fa fa-indent" aria-hidden="true"></i> 首页静态化
					</a>
					<a style="margin-left: 20px;" href="${ctx}/cms/article/staticCategory?category.id=${article.category.id}" onclick="ajaxPost('确认要执行本栏目页静态化吗？',this.href);return false;" class="btn btn-info pull-right tjTip"> <i
						class="fa fa-indent" aria-hidden="true"></i> 栏目页静态化
					</a>
					<a style="margin-left: 20px;" href="${ctx}/cms/article/staticArticle?category.id=${article.category.id}" onclick="ajaxPost('确认要执行本栏目内容页静态化吗？',this.href);return false;" class="btn btn-info pull-right tjTip"> <i
						class="fa fa-indent" aria-hidden="true"></i> 内容页静态化
					</a>
					
					<a style="margin-left: 20px;" href="${ctx}/cms/article/doIndexAll" onclick="ajaxPost('生成本系统所有文章的索引，此操作会比较耗时，确认要执行吗？',this.href);return false;" class="btn btn-info pull-right tjTip"> <i
						class="fa fa-plus" aria-hidden="true"></i> 生成所有文章的索引
					</a>
					<a style="margin-left: 20px;" href="${ctx}/cms/article/doIndexByCatetory?category.id=${article.category.id}" onclick="ajaxPost('确认要生成本栏目下所有文章的索引吗？',this.href);return false;"
						class="btn btn-info pull-right tjTip"> <i class="fa fa-plus" aria-hidden="true"></i> 生成该栏目全部文章的索引
					</a> 
					<a style="margin-left: 20px;" href="${ctx}/cms/article/doIndexByIds?" onclick="if($('input[type=checkbox][name=ids]:checked').length<1){$.jBox.tip('未选中任何数据');return false;}ajaxPost('确认要生成选中文章的索引吗？',this.href+$('#batchDeleteForm').serialize());return false;" class="btn btn-info pull-right tjTip"> <i
						class="fa fa-plus" aria-hidden="true"></i> 生成选中文章的索引
					</a> --%>
					<a href="${ctx}/cms/article/form?category.id=${article.category.id}" target="_top" class="btn btn-info pull-right tjTip"> <i class="fa fa-plus" aria-hidden="true"></i>
						发布新闻
					</a>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 simpleTable">
					<form action="${ctx}/cms/article/doIndexByIds" method="post" id="batchDeleteForm">
						<table class="ui celled table_semantic" id="articlelist">
							<thead>
								<tr>
									<th class="checkboxTh"><input type="checkbox"></th>
									<th>标题</th>
									<th>点击数</th>
									<th>权重</th>
									<th>发布者</th>
									<th>发布时间</th>
									<th class="tablebox-handle">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="item">
									<tr>
										<td><input type="checkbox" name="ids" value="${item.id}"></td>
										<td><a href="${ctx}/cms/article/form?id=${item.id}" title="${item.title}" class="newsCont" style="color: ${item.color}" target="_top">${fns:abbr(item.title,40)}</a></td>
										<td>${item.hits}</td>
										<td>${item.weight}</td>
										<td>${item.user.name}</td>
										<td><fmt:formatDate value="${empty item.releaseDate ? item.createDate : item.releaseDate}" type="both" /></td>
										<td class="tablebox-handle text-right">
											<a href="${item.url}" target="_blank" class="btn btn-default btn-sm" title="访问"><i class="fa fa-external-link"></i> </a> 
											<shiro:hasPermission	name="cms:article:edit">
												<c:if test="${item.category.allowComment eq '1'}">
													<shiro:hasPermission name="cms:comment:view">
														<a href="${ctx}/cms/comment/?module=article&contentId=${item.id}&delFlag=2" onclick="return viewComment(this.href);" class="btn btn-default btn-sm" title="评论"><i
															class="fa fa-comment-o"></i></a>
													</shiro:hasPermission>
												</c:if> 
												<%-- <a href="${ctx}/cms/form?module=article&id=${item.id}&category.id=${item.category.id}&queryString=${article.queryString}" target="_top" class="btn btn-default btn-sm" title="修改"><i class="fa fa-pencil"></i></a> --%>
												<a href="${ctx}/cms/article/form?id=${item.id}&queryString=${article.queryString}" target="_top" class="btn btn-default btn-sm" title="修改"><i class="fa fa-pencil"></i></a>
												<c:if test="${item.category.id eq fns:getSysConfigWithDefaultValue('red.file.id','')}">
													<a href="${ctx}/cms/article/showRedFile?id=${item.id}" target="_blank" class="btn btn-default btn-sm" title="查看红头文件">查看红头文件</a>
												</c:if>
												<shiro:hasPermission name="cms:article:audit">
													<a href="${ctx}/cms/article/delete?id=${item.id}${item.delFlag ne 0?'&isRe=true':''}&queryString=${article.queryString}"
														onclick="return confirmx('确认要${item.delFlag ne 0?'发布':'删除'}该文章吗？', this.href)" class="btn btn-default btn-sm delModal" title="${item.delFlag ne 0?'发布':'删除'}"><i
														class="fa fa-close"></i></a>
													<c:if test="${item.category.id ne fns:getSysConfigWithDefaultValue('red.file.id','')}">
														<a href="${ctx}/cms/article/doIndex?id=${item.id}" onclick="ajaxPost('确认要生成本文章的索引吗？',this.href);return false;"
															class="btn btn-default btn-sm delModal" title="生成索引"><i class="fa fa-plus"></i></a>
														
														<a href="${ctx}/cms/article/staticArticle?id=${item.id}" onclick="ajaxPost('确认要静态化吗？',this.href);return false;"
															class="btn btn-default btn-sm delModal" title="静态化"><i class="fa fa-indent"></i></a>
                                                   </c:if>
												</shiro:hasPermission>
											</shiro:hasPermission></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</form>
					<div class="row">${page}</div>
				</div>
			</div>
		</section>
	</c:if>
	
	
	<c:if test="${article.category.parent.id eq '5334d4a1b5b24f688f71a8822cfd507b' && fns:getConfig('custmor.code') eq 'yunzhi'}">
		<!-- 产品服务 -->
		<form:form id="searchForm" modelAttribute="article" action="${ctx}/cms/article/" method="post" class="form-inline form-filter" role="form">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
			<input id="category.id" name="category.id" type="hidden" value="${article.category.id}" />
			<div class="form-group">
				<label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="50" class="form-control" placeholder="title" style="width:220px" />
			</div>
			<div class="form-group">
				<label>状态：</label>
				<form:select path="delFlag" class="form-control select2">
					<form:options items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<button type="submit" class="btn btn-default btn-sm" title="搜索">
				<i class="fa fa-search"></i>
			</button>

			<div class="row tableTopBtn">

				<div class="col-xs-12">
					<a style="margin-right: 29PX;" href="${ctx}/cms/article/form?category.id=${article.category.id}" target="_top" class="btn btn-info pull-right tjTip"> <i class="fa fa-plus" aria-hidden="true"></i>
						发布新闻
					</a> 
					<a style=" margin-right: 10px;" href="${ctx}/cms/article/doindexbycatetory?category.id=${article.category.id}" onclick="return confirmx('确认索引该栏目下所有文章吗？', this.href,'','索引正在生成，请稍后!')"
						class="btn btn-info pull-right tjTip"> <i class="fa fa-plus" aria-hidden="true"></i> 生成全部索引
					</a>

				</div>
			</div>
		</form:form>
		 
		<div style="background: #eaedf1;">
			<br>
		</div>
		<section class="content form-filter">
			<!-- button -->

			<div class="row tableTopBtn">

				<div class="col-xs-12">
					<span target="_top" class="btn btn-info tjTip"> 产品介绍</span>
				</div>
			</div>
			<div class="row">

				<div class="col-xs-12 simpleTable">
					<form action="${ctx}/cms/article/doindexbyIds" method="post" id="batchDeleteForm">
						<table class="ui celled table_semantic" id="articlelist">
							<thead>
								<tr>
									<th><input type="checkbox"></th>
									<th>标题</th>
									<th>点击数</th>
									<th>权重</th>
									<th>发布者</th>
									<th>发布时间</th>
									<th class="tablebox-handle">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pagept.list}" var="article">
									<tr>
										<td><input type="checkbox" name="ids" value="${artilce.id}"></td>
										<td><a href="${ctx}/cms/article/form?id=${article.id}" title="${article.title}" class="newsCont" style="color: ${article.color}" target="_top">${fns:abbr(article.title,40)}</a></td>
										<td>${article.hits}</td>
										<td>${article.weight}</td>
										<td>${article.user.name}</td>
										<td><c:if test="${empty article.releaseDate}">
												<fmt:formatDate value="${article.createDate}" type="both" />
											</c:if> <c:if test="${ not empty article.releaseDate}">
												<fmt:formatDate value="${article.releaseDate}" type="both" />
											</c:if></td>
										<td class="tablebox-handle text-right">
												<a href="${article.url}" target="_blank" class="btn btn-default btn-sm" title="访问"><i class="fa fa-external-link"></i> </a> 
												<shiro:hasPermission  name="cms:article:edit">
												<c:if test="${article.category.allowComment eq '1'}">
													<shiro:hasPermission name="cms:comment:view">
														<a href="${ctx}/cms/comment/?module=article&contentId=${article.id}&delFlag=2" onclick="return viewComment(this.href);" class="btn btn-default btn-sm" title="评论"><i
															class="fa fa-comment-o"></i></a>
													</shiro:hasPermission>
												</c:if>
												<a href="${ctx}/cms/article/form?id=${article.id}" target="_top" class="btn btn-default btn-sm" title="修改"><i class="fa fa-pencil"></i></a>
												<shiro:hasPermission name="cms:article:audit">
													<a href="${ctx}/cms/article/delete?id=${article.id}${article.delFlag ne 0?'&isRe=true':''}&categoryId=${categoryId}"
														onclick="return confirmx('确认要${article.delFlag ne 0?'发布':'删除'}该文章吗？', this.href)" class="btn btn-default btn-sm delModal" title="${article.delFlag ne 0?'发布':'删除'}"><i
														class="fa fa-close"></i></a>

													<a href="${ctx}/cms/article/doindex?id=${article.id}${article.delFlag ne 0?'&isRe=true':''}&categoryId=${categoryId}" onclick="return confirmx('确认要索引该文章吗？', this.href,'','索引正在生成，请稍后!')"
														class="btn btn-default btn-sm delModal" title="索引"><i class="fa fa-search"></i></a>


												</shiro:hasPermission>
											</shiro:hasPermission></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</form>
					<!-- 分页 -->
					<%-- <div class="row">
                ${page}
              </div> --%>
				</div>
			</div>
		</section>

		<div style="background: #eaedf1;">
			<br>
		</div>
		<!--产品优势  -->

		<section class="content form-filter">
			<!-- button -->
			<div class="row tableTopBtn">

				<div class="col-xs-12">

					<span target="_top" class="btn btn-info pull-right tjTip"> 产品优势</span>
				</div>
			</div>
			<div class="row">

				<div class="col-xs-12 simpleTable">
					<form action="${ctx}/cms/article/doindexbyIds" method="post" id="batchDeleteForm">
						<table class="ui celled table_semantic" id="articlelist">
							<thead>
								<tr>
									<th><input type="checkbox"></th>
									<th>标题</th>
									<th>点击数</th>
									<th>权重</th>
									<th>发布者</th>
									<th>发布时间</th>
									<th class="tablebox-handle">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pageys.list}" var="article">
									<tr>
										<td><input type="checkbox" name="ids" value="${artilce.id}"></td>
										<td><a href="${ctx}/cms/article/form?id=${article.id}" title="${article.title}" class="newsCont" style="color: ${article.color}" target="_top">${fns:abbr(article.title,40)}</a></td>
										<td>${article.hits}</td>
										<td>${article.weight}</td>
										<td>${article.user.name}</td>
										<td><c:if test="${empty article.releaseDate}">
												<fmt:formatDate value="${article.createDate}" type="both" />
											</c:if> <c:if test="${ not empty article.releaseDate}">
												<fmt:formatDate value="${article.releaseDate}" type="both" />
											</c:if></td>
										<td class="tablebox-handle text-right"><a href="${article.url}" target="_blank" class="btn btn-default btn-sm" title="访问"><i class="fa fa-external-link"></i> </a> <shiro:hasPermission
												name="cms:article:edit">
												<c:if test="${article.category.allowComment eq '1'}">
													<shiro:hasPermission name="cms:comment:view">
														<a href="${ctx}/cms/comment/?module=article&contentId=${article.id}&delFlag=2" onclick="return viewComment(this.href);" class="btn btn-default btn-sm" title="评论"><i
															class="fa fa-comment-o"></i></a>
													</shiro:hasPermission>
												</c:if>
												<a href="${ctx}/cms/article/form?id=${article.id}" target="_top" class="btn btn-default btn-sm" title="修改"><i class="fa fa-pencil"></i></a>
												<shiro:hasPermission name="cms:article:audit">
													<a href="${ctx}/cms/article/delete?id=${article.id}${article.delFlag ne 0?'&isRe=true':''}&categoryId=${categoryId}"
														onclick="return confirmx('确认要${article.delFlag ne 0?'发布':'删除'}该文章吗？', this.href)" class="btn btn-default btn-sm delModal" title="${article.delFlag ne 0?'发布':'删除'}"><i
														class="fa fa-close"></i></a>

													<a href="${ctx}/cms/article/doindex?id=${article.id}${article.delFlag ne 0?'&isRe=true':''}&categoryId=${categoryId}" onclick="return confirmx('确认要索引该文章吗？', this.href,'','索引正在生成，请稍后!')"
														class="btn btn-default btn-sm delModal" title="索引"><i class="fa fa-search"></i></a>


												</shiro:hasPermission>
											</shiro:hasPermission></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</form>
					<!-- 分页 -->
					<%-- <div class="row">
                ${page}
              </div> --%>
				</div>
			</div>
		</section>

		<div style="background: #eaedf1;">
			<br>
		</div>
		<!-- 标杆案例 -->
		<section class="content form-filter">
			<!-- button -->
			<div class="row tableTopBtn">

				<div class="col-xs-12">
					<span target="_top" class="btn btn-info pull-right tjTip">标杆案例</span>
				</div>
			</div>

			<div class="row">

				<div class="col-xs-12 simpleTable">
					<form action="${ctx}/cms/article/doindexbyIds" method="post" id="batchDeleteForm">
						<table class="ui celled table_semantic" id="articlelist">
							<thead>
								<tr>
									<th class="checkboxTh"><input type="checkbox"></th>
									<th>标题</th>
									<th>点击数</th>
									<th>权重</th>
									<th>发布者</th>
									<th>发布时间</th>
									<th class="tablebox-handle">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pagebg.list}" var="article">
									<tr>
										<td><input type="checkbox" name="ids" value="${artilce.id}"></td>
										<td><a href="${ctx}/cms/article/form?id=${article.id}" title="${article.title}" class="newsCont" style="color: ${article.color}" target="_top">${fns:abbr(article.title,40)}</a></td>
										<td>${article.hits}</td>
										<td>${article.weight}</td>
										<td>${article.user.name}</td>
										<td><c:if test="${empty article.releaseDate}">
												<fmt:formatDate value="${article.createDate}" type="both" />
											</c:if> <c:if test="${ not empty article.releaseDate}">
												<fmt:formatDate value="${article.releaseDate}" type="both" />
											</c:if></td>
										<td class="tablebox-handle text-right"><a href="${article.url}" target="_blank" class="btn btn-default btn-sm" title="访问"><i class="fa fa-external-link"></i> </a> <shiro:hasPermission
												name="cms:article:edit">
												<c:if test="${article.category.allowComment eq '1'}">
													<shiro:hasPermission name="cms:comment:view">
														<a href="${ctx}/cms/comment/?module=article&contentId=${article.id}&delFlag=2" onclick="return viewComment(this.href);" class="btn btn-default btn-sm" title="评论"><i
															class="fa fa-comment-o"></i></a>
													</shiro:hasPermission>
												</c:if>
												<a href="${ctx}/cms/article/form?id=${article.id}" target="_top" class="btn btn-default btn-sm" title="修改"><i class="fa fa-pencil"></i></a>
												<shiro:hasPermission name="cms:article:audit">
													<a href="${ctx}/cms/article/delete?id=${article.id}${article.delFlag ne 0?'&isRe=true':''}&categoryId=${categoryId}"
														onclick="return confirmx('确认要${article.delFlag ne 0?'发布':'删除'}该文章吗？', this.href)" class="btn btn-default btn-sm delModal" title="${article.delFlag ne 0?'发布':'删除'}"><i
														class="fa fa-close"></i></a>

													<a href="${ctx}/cms/article/doindex?id=${article.id}${article.delFlag ne 0?'&isRe=true':''}&categoryId=${categoryId}" onclick="return confirmx('确认要索引该文章吗？', this.href,'','索引正在生成，请稍后!')"
														class="btn btn-default btn-sm delModal" title="索引"><i class="fa fa-search"></i></a>


												</shiro:hasPermission>
											</shiro:hasPermission></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</form>
					<!-- 分页 -->
					<%--  <div class="row">
                ${page}
              </div> --%>
				</div>
			</div>
		</section>
		<br>
	</c:if>


	<!-- /.content -->
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script type="text/javascript">
		function ajaxPost(title,url){
			confirmx(title, function(){
				new AjaxHelper({type:"post",url:url}).sendRequest();
			});
		}
		function viewComment(href) {
			top.$.jBox.open('iframe:' + href, '查看评论', $(top.document).width() - 220, $(top.document).height() - 120, {
				buttons : {
					"关闭" : true
				},
				loaded : function(h) {
					$(".jbox-content", top.document).css("overflow-y", "hidden");
					$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
					$("body", h.find("iframe").contents()).css("margin", "10px");
				}
			});
			return false;
		}
		function page(n, s) {
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
		$(function() {
			$(".select2").select2();
			
			/* // 数据表格功能
			$("#articlelist").DataTable({
				"aoColumns" : [
					null,
					null,
					null,
					null,
					null,
					{
						"asSorting" : []
					}
				],
				"fixedHeader" : true,
				"paging" : false,
				"bFilter" : false,
				"info" : false
			}); */
			
			
			// checkbox iCheck
		    $('.simpleTable input[type="checkbox"]').iCheck({
		      	checkboxClass: 'icheckbox_flat-blue',
		      	radioClass: 'iradio_flat-blue'
		    });
		    $(".checkboxTh ins").click(function () {
		    	var checked = $(this).parent('div').hasClass('checked');
		      	if (checked) {
		        	$("input[type='checkbox'][name='ids']").iCheck("check");
		      	} else {
		         	$("input[type='checkbox'][name='ids']").iCheck("uncheck");
		      	}
		    });
		    $("tbody td ins").click(function () {
		    	var checked = $(this).parent('div').hasClass('checked');
		    	var checkboxTh_checkbox = $(".checkboxTh input[type='checkbox']");
		    	if (checked) {
		    		// td全部选中时，th才选中
		    		if($("tbody td input[type='checkbox'][name='ids']").length == $("tbody td input[type='checkbox'][name='ids']:checked").length){
			    		checkboxTh_checkbox.iCheck("check");
		    		}
		      	} else {
		      		// th不选中
		      		checkboxTh_checkbox.iCheck("uncheck");
		      	}
		    });
		});
	</script>
</body>
</html>