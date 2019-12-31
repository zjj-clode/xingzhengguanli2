<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
  <link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.css">
  <link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.theme.default.css">
  <script src="${ctxStaticLTE}/plugins/treeTable/jquery.treeTable.js"></script>
</head>
<body>
    <section class="content-header">
      <h1>
                        内容管理
        <small>文章信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">栏目信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
     <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          <a href="${ctx}/cms/category/form" class="btn btn-info pull-right tjTip"> <i class="fa fa-plus" aria-hidden="true"></i> 栏目添加 </a>
        </div>
      </div>
      <div class="row">
         <div class="col-xs-12 simpleTable">
               <form id="listForm" method="post">
				<table id="treeTable" class="ui celled table_semantic">
				   <thead>
					<tr>
					  <th>栏目名称</th>
					  <th>归属机构</th>
					  <th>栏目模型</th>
					  <th style="text-align:center;">排序</th>
					  <th title="是否在导航中显示该栏目">导航菜单</th>
					  <th title="是否在分类页中显示该栏目的文章列表">栏目列表</th>
					  <th>展现方式</th>
					  <th class="tablebox-handle">操作</th>
					 </tr>
				   </thead>
				   <tbody>
					<c:forEach items="${list}" var="tpl">
						<tr data-tt-id="${tpl.id}" data-tt-parent-id="${tpl.parent.id ne '1'?tpl.parent.id:'0'}">
							<td><span class="${tpl.parent.id ne '1'?'file':'folder'}"><a href="${ctx}/cms/category/form?id=${tpl.id}">${tpl.name}</a></span></td>
							<td>${tpl.office.name}</td>
							<td>${fns:getDictLabel(tpl.module, 'cms_module', '公共模型')}</td>
							<td style="text-align:center;">
								<shiro:hasPermission name="cms:category:edit">
									<input type="hidden" name="ids" value="${tpl.id}"/>
									<input name="sorts" type="text" value="${tpl.sort}" style="width:50px;margin:0;padding:0;text-align:center;" class="form-control">
								</shiro:hasPermission><shiro:lacksPermission name="cms:category:edit">
									${tpl.sort}
								</shiro:lacksPermission>
							</td>
							<td>${fns:getDictLabel(tpl.inMenu, 'show_hide', '隐藏')}</td>
							<td>${fns:getDictLabel(tpl.inList, 'show_hide', '隐藏')}</td>
							<td>${fns:getDictLabel(tpl.showModes, 'cms_show_modes', '默认展现方式')}</td>
							<td class="tablebox-handle">
								<a href="${pageContext.request.contextPath}${fns:getFrontPath()}/list-${tpl.id}${fns:getUrlSuffix()}" class="btn btn-default btn-sm" title="访问" target="_blank"><i class="fa fa-external-link"></i></a>
								<shiro:hasPermission name="cms:category:edit">
									<a href="${ctx}/cms/category/form?id=${tpl.id}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
									<a href="${ctx}/cms/category/delete?id=${tpl.id}" onclick="return confirmx('要删除该栏目及所有子栏目项吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
									<a href="${ctx}/cms/category/form?parent.id=${tpl.id}" class="btn btn-default btn-sm" title="添加下级菜单"><i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<shiro:hasPermission name="cms:category:edit"><div class="form-actions pagination-left">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
			</div></shiro:hasPermission>
		</form>
		</div>
       </div>
    </section>
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script type="text/javascript">
	$(document).ready(function() {
	    $("#treeTable").treetable({ expandable: true });
		$("#treeTable tbody").on("mousedown", "tr", function() {
	      $(".selected").not(this).removeClass("selected");
	      $(this).toggleClass("selected");
	    });
	});
   	function updateSort() {
		loading('正在提交，请稍等...');
    	$("#listForm").attr("action", "${ctx}/cms/category/updateSort");
    	$("#listForm").submit();
   	}
   	
    // 按钮鼠标停留效果
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
</script>
</body>
</html>