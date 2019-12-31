<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>选择文章</title>
<!-- <meta name="decorator" content="lte"/> -->
<link rel="stylesheet" href="${ctxStaticLTE}/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/font-awesome/font-awesome.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/ionicons/ionicons.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/select2/select2.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/My97DatePicker/skin/WdatePicker.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datepicker/datepicker3.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/iCheck/flat/blue.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.theme.default.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/dataTables.bootstrap.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/jquery.dataTables.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/dataTables.semanticui.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/semantic_table.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/extensions/FixedColumns/css/dataTables.fixedColumns.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/jBox/jBox.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/colorpicker/colpick.css">
<link rel="stylesheet" href="${ctxStaticLTE}/dist/css/AdminLTE.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/dist/css/skins/_all-skins.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/jquery-validation/1.11.1/jquery.validate.min.css">
<!--[if lt IE 9]>
  <script src="${ctxStaticLTE}/plugins/ie8/html5shiv.min.js"></script>
  <script src="${ctxStaticLTE}/plugins/ie8/respond.min.js"></script>
  <link rel="stylesheet" href="${ctxStaticLTE}/plugins/ie8/ie8-hack.css">
  <![endif]-->
<!--[if !IE]> -->
<script src="${ctxStatic}/jquery/j2.js"></script>
<!-- <![endif]-->
<!--[if IE]>
  	<script src="${ctxStatic}/jquery/j1.js"></script>
<![endif]-->
<script src="${ctxStaticLTE}/plugins/jBox/jBox.min.js"></script>
<script src="${ctxStatic}/common/batchDelete.js"></script>
<script src="${ctxStaticLTE}/common.js"></script>
<script type="text/javascript">var ctx = '${ctx}',
		ctxStatic = '${ctxStatic}',
		ctxStaticLTE = '${ctxStaticLTE}';
</script>

<script type="text/javascript">
	$(document).ready(function() {
		var articleSelect = top.articleSelect;
		$("input[name=id]").each(function() {
			for (var i = 0; i < articleSelect.length; i++) {
				if (articleSelect[i][0] == $(this).val()) {
					this.checked = true;
				}
			}
			$(this).click(function() {
				var id = $(this).val(),
					title = $(this).attr("title");
				top.articleSelectAddOrDel(id, title);
			});
		});
	});
	function view(href) {
		top.$.jBox.open('iframe:' + href, '查看文章', $(top.document).width() - 220, $(top.document).height() - 120, {
			buttons : {
				"关闭" : true
			},
			loaded : function(h) {
				$(".jbox-content", top.document).css("overflow-y", "hidden");
				$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
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
</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="article" action="${ctx}/cms/article/selectList" method="post" class="form-inline form-filter" role="form">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();" />
		<div class="form-group">
			<label>栏目：</label>
			<sys:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}" title="栏目" url="/cms/category/treeData"
				module="article" cssClass="input-small" />
		</div>
		<div class="form-group">
			<label>标题：</label>
			<form:input path="title" htmlEscape="false" maxlength="50" class="input-small" />
		</div>
		<button type="submit" class="btn btn-default btn-sm" title="搜索">
			<i class="fa fa-search"></i>
		</button>
	</form:form>
	<section class="content">
		<div class="row" style="margin:10px;">
			<div class="simpleTable">
				<div style="overflow:auto;width:1000px;">
					<table class="ui celled table_semantic" id="zhaoshengLqjcListTable">
						<thead>
							<tr>
								<th style="text-align:center;">选择</th>
								<th>栏目</th>
								<th>标题</th>
								<th>权重</th>
								<th>点击数</th>
								<th>发布者</th>
								<th>更新时间</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="article">
								<tr>
									<td style="text-align:center;"><input type="checkbox" name="id" value="${article.id}" title="${fns:abbr(article.title,40)}" /></td>
									<td><a href="javascript:" onclick="$('#categoryId').val('${article.category.id}');$('#categoryName').val('${article.category.name}');$('#searchForm').submit();return false;">${article.category.name}</a></td>
									<td><a href="${ctx}/cms/article/form?id=${article.id}" title="${article.title}" onclick="return view(this.href);">${fns:abbr(article.title,40)}</a></td>
									<td>${article.weight}</td>
									<td>${article.hits}</td>
									<td>${article.createBy.name}</td>
									<td><fmt:formatDate value="${article.updateDate}" type="both" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="row">${page}</div>
				</div>
			</div>
		</div>
	</section>
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
</body>
</html>
