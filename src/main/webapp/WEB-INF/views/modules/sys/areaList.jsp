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
        <small>区域信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">区域信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
     <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          <a href="${ctx}/sys/area/form" class="btn btn-info pull-right tjTip"> <i class="fa fa-plus" aria-hidden="true"></i> 添加 </a>
        </div>
      </div>
      <div class="row">
         <div class="col-xs-12 simpleTable">
               <form id="listForm" method="post">
				<table id="treeTable" class="ui celled table_semantic">
			        <thead>
				        <tr>
					        <th>区域名称</th>
					        <th>区域编码</th>
					        <th>类型</th>
					        <th>备注</th>
					        <th style="width: 100px" class="tablebox-handle">操作</th>
				        </tr>
				    </thead>
					<tbody id="treeTableList">
					<c:forEach items="${list}" var="tpl">
					     <tr data-tt-id="${tpl.id}" data-tt-parent-id="${tpl.parent.id ne '1'?tpl.parent.id:'0'}">
							<td><span class="${tpl.parent.id ne '1'?'file':'folder'}"><a href="${ctx}/sys/area/form?id=${tpl.id}">${tpl.name}</a></span></td>
							<td>${tpl.code}</td>
							<td>${fns:getDictLabel(tpl.type,'sys_area_type','')}</td>
							<td>${tpl.remarks}</td>
							<td class="tablebox-handle">
								<a href="${ctx}/sys/area/form?id=${tpl.id}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
								<a href="${ctx}/sys/area/delete?id=${tpl.id}" onclick="return confirmx('要删除该区域及所有子区域项吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
								<a href="${ctx}/sys/area/form?parent.id=${tpl.id}" class="btn btn-default btn-sm" title="添加下级区域"><i class="fa fa-plus"></i></a>
							</td>
						</tr>
					</c:forEach>
			        </tbody>
		         </table>
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
    	$("#listForm").attr("action", "${ctx}/sys/office/updateSort");
    	$("#listForm").submit();
   	}
   	function getView(id){
      $.jBox.open('iframe:${ctx}/sys/area/form?id='+id,"基本信息",1000,500);
   }    
</script>
</body>
</html>