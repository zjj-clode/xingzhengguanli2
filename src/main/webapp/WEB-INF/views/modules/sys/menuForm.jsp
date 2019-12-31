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
        <small>菜单信息添加与修改</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/sys/menu">菜单管理</a></li>
        <li class="active">信息添加与修改</li>
      </ol>
    </section>
    <!-- /.content-header -->
    
    <section class="content">
      <div class="row">
        <div class="col-md-12">
	<form:form id="inputForm" modelAttribute="menu" action="${ctx}/sys/menu/save" method="post" class="form-horizontal form-information pad  form-lie">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>上级菜单:</label>
					<div class="col-sm-3">
		                <sys:treeselect id="menu" name="parent.id" value="${menu.parent.id}" labelName="parent.name" labelValue="${menu.parent.name}"
							title="菜单" url="/sys/menu/treeData" extId="${menu.id}" cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>名称:</label>
					<div class="col-sm-3">
						<form:input path="name" htmlEscape="false" maxlength="50" class="required form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">英文名称:</label>
					<div class="col-sm-3">
						<form:input path="ename" htmlEscape="false" maxlength="50" class="form-control"/>
						
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">链接:</label>
					<div class="col-sm-5">
						<form:input path="href" htmlEscape="false" maxlength="2000" class="form-control"/>
					</div>
					<div class="col-sm-5 text-muted">点击菜单跳转的页面</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">目标:</label>
					<div class="col-sm-2">
						<form:input path="target" htmlEscape="false" maxlength="10" class="form-control"/>
					</div>
					<div class="col-sm-8 text-muted">链接地址打开的目标窗口，默认：mainFrame</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">图标:</label>
					<div class="col-sm-3">
						<sys:iconselect id="icon" name="icon" value="${menu.icon}"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">排序:</label>
					<div class="col-sm-1">
						<form:input path="sort" htmlEscape="false" maxlength="50" class="required digits form-control"/>
					</div>
					<div class="col-sm-9 text-muted">排列顺序，升序。</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">可见:</label>
					<div class="col-sm-2 radio">
					    <c:forEach items="${fns:getDictList('show_hide')}" var="dic" varStatus="status">
						     <label style="margin-right:10px">
						      <input type="radio" name="isShow" id="isShow" value="${dic.value}" <c:if test="${dic.value == menu.isShow}">checked="true"</c:if> >${dic.label}
							 </label>
						    </c:forEach>
					</div>
					<div class="col-sm-7 text-muted">该菜单或操作是否显示到系统菜单中。</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">权限标识:</label>
					<div class="col-sm-5">
						<form:input path="permission" htmlEscape="false" maxlength="255" class="form-control"/>
					</div>
					<div class="col-sm-5 text-muted">控制器中定义的权限标识，如：@RequiresPermissions("权限标识")</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">备注:</label>
					<div class="col-sm-5">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
			      <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				    </div>
				</div>
			</form:form>
		</div>
     </div>
   </section>
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script>
  $(function () {
       $(".select2").select2();
       $("#inputForm .select2").change(function (){
		   $(this).valid();
	   });
       // 按钮鼠标停留效果
       $('.form-information button').tooltip();
       $("#inputForm").validate({
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append") || element.parent().is(".date")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
  });
</script>
</body>
</html>