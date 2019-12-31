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
                        系统管理
        <small>菜单信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">菜单信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
     <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          <a href="${ctx}/sys/menu/form" class="btn btn-info pull-right tjTip"> <i class="fa fa-plus" aria-hidden="true"></i> 添加菜单 </a>
        </div>
      </div>
      <div class="row">
         <div class="col-xs-12 simpleTable">
               <form id="listForm" method="post">
				  <table id="treeTable" class="ui celled table_semantic">
					<thead>
						<tr>
						<th style="width: 180px">名称</th>
						<th>链接</th>
						<th>权限标识</th>
						<th>ename</th>
						<th style="width: 40px">排序</th>
						<th style="width: 40px">可见</th>
						<th style="width: 100px" class="tablebox-handle">操作</th>
                        </tr>
					<tbody id="treeTableList">
					<c:forEach items="${list}" var="menu">
					     <tr data-tt-id="${menu.id}" data-tt-parent-id="${menu.parent.id ne '1'?menu.parent.id:'0'}">
								<td><span class="${menu.parent.id ne '1'?'file':'folder'}"><a href="${ctx}/sys/menu/form?id=${menu.id}">${menu.name}</a></span></td>
								<td title="${menu.href}">${fns:abbr(menu.href,30)}</td>
								<td title="${menu.permission}">${fns:abbr(menu.permission,30)}</td>
								<td title="${menu.ename}">${menu.ename}</td>
								<td style="text-align:center;">
									<shiro:hasPermission name="sys:menu:edit">
										<input type="hidden" name="ids" value="${menu.id}"/>
										<input name="sorts" type="text" value="${menu.sort}" style="width:50px;margin:0;padding:0;text-align:center;" class="form-control">
									</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">
										${menu.sort}
									</shiro:lacksPermission>
								</td>
								<td>${menu.isShow eq '1'?'显示':'隐藏'}</td>
								<td class="tablebox-handle"><shiro:hasPermission name="sys:menu:edit">
									<a href="${ctx}/sys/menu/form?id=${menu.id}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
									<a href="${ctx}/sys/menu/delete?id=${menu.id}" onclick="return confirmx('要删除该菜单及所有子菜单项吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
									<a href="${ctx}/sys/menu/form?parent.id=${menu.id}" class="btn btn-default btn-sm" title="添加下级菜单"><i class="fa fa-plus"></i></a>
								</shiro:hasPermission></td>
							</tr>
						</c:forEach></tbody>
					</table>
			    <div class="form-actions pagination-left">
					<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
				</div>
		    </form>
		</div>
       </div>
    </section>
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script type="text/javascript">
	/* $(document).ready(function() {
	    $("#treeTable").treetable({ expandable: true });
		$("#treeTable tbody").on("mousedown", "tr", function() {
	      $(".selected").not(this).removeClass("selected");
	      $(this).toggleClass("selected");
	    });
	}); */
	
	//递归获取父tr的id存入数组
	function findParentTrs(id, arry) {
		var tr = $("tr[data-tt-id='" + id + "']");
		var parentId = tr.attr("data-tt-parent-id");
		var parent = $("tr[data-tt-id='" + parentId + "']");
		if (parent.length != 0) {
			arry.push(parentId);
			findParentTrs(parentId, arry);
		} else {
			return;
		}
	}
	$(document).ready(function() {
		var arry = [];
		findParentTrs("${editId}", arry);
		var delay = 100;
		try {
			$("tr[data-tt-id='" + arry[4] + "']").find("span.indenter a").click();
		} catch (e) {}
		setTimeout(function() {
			try {
				$("tr[data-tt-id='" + arry[3] + "']").find("span.indenter a").click();
			} catch (e) {}
			setTimeout(function() {
				try {
					$("tr[data-tt-id='" + arry[2] + "']").find("span.indenter a").click();
				} catch (e) {}
				setTimeout(function() {
					try {
						$("tr[data-tt-id='" + arry[1] + "']").find("span.indenter a").click();
					} catch (e) {}
					setTimeout(function() {
						try {
							$("tr[data-tt-id='" + arry[0] + "']").find("span.indenter a").click();
						} catch (e) {}
					}, delay);
				}, delay);
			}, delay);
		}, delay);
		$("#treeTable").treetable({
			expandable : true
		});
		$("#treeTable tbody").on("mousedown", "tr", function() {
			$(".selected").not(this).removeClass("selected");
			$(this).toggleClass("selected");
		});
	});
	
   	function updateSort() {
		loading('正在提交，请稍等...');
    	$("#listForm").attr("action", "${ctx}/sys/menu/updateSort");
    	$("#listForm").submit();
   	}
</script>
</body>
</html>