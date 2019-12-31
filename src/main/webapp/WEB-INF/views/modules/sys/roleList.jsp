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
        <small>角色信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">角色信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
	<section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          <a href="${ctx}/sys/role/form"><button type="button" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 角色添加</button></a>
        </div>
      </div>
	<sys:message content="${message}"/>
	<div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic" id="rolelist">
	               <thead>
					<tr>
					  <th>角色名称</th>
					  <th>英文名称</th>
					  <th>归属机构</th>
					  <th>数据范围</th>
					  <th style="width: 80px" class="tablebox-handle">操作</th>
					</tr>
				   </thead>
				   <tbody>
					<c:forEach items="${list}" var="role">
						<tr>
							<td>
								<c:if test="${role.id == '1'}">
									${role.name}
								</c:if>
								<c:if test="${role.id != '1'}">
									<a href="form?id=${role.id}">${role.name}</a>
								</c:if>
							</td>
							<td>
								<c:if test="${role.id == '1'}">
									${role.enname}
								</c:if>
								<c:if test="${role.id != '1'}">
									<a href="form?id=${role.id}">${role.enname}</a>
								</c:if>
							</td>
							<td>${role.office.name}</td>
							<td>${fns:getDictLabel(role.dataScope, 'sys_data_scope', '无')}</td>
							<td class="tablebox-handle"><shiro:hasPermission name="sys:role:edit">
								<c:if test="${role.id != '1'}">
									<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
										<a href="${ctx}/sys/role/form?id=${role.id}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
									</c:if>
								</c:if>
								<a href="${ctx}/sys/role/delete?id=${role.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
							</shiro:hasPermission>	</td>
						</tr>
					</c:forEach>
				   </tbody>
				</table>
          </div>
      </div>
    </section>
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script>
 $(function () {
    //Initialize Select2 Elements
    $(".select2").select2();
    // 数据表格功能
    var table = $('#rolelist1').DataTable({
      "ordering": false,
      "info": false,
      "scrollY": 450,
      "scrollCollapse": true,
      "bFilter": false, 
      "paging":  false
    });
    // 按钮鼠标停留效果
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
  });
</script>
</body>
</html>