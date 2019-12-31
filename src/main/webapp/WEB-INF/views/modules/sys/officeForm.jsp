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
        <small>学院/专业信息添加与修改</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/sys/office">学院/专业管理</a></li>
        <li class="active">信息添加与修改</li>
      </ol>
    </section>
    <!-- /.content-header -->
    
    <section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/save" method="post" class="form-horizontal form-information pad  form-lie">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>		
				<c:if test="${office.id ne '1' }">
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>上级部门:</label>
						<div class="col-sm-3">
			                <sys:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
								title="机构" url="/sys/office/treeData" extId="${office.id}" cssClass="form-control required" allowClear="${office.currentUser.admin}"/>
						</div>
					</div>
				</c:if>
				<%-- <c:if test="${office.id ne '1' }"> --%>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>归属区域:</label>
						<div class="col-sm-3">
			                <sys:treeselect id="area" name="area.id" value="${office.area.id}" labelName="area.name" labelValue="${office.area.name}"
								title="区域" url="/sys/area/treeData" cssClass="form-control required"/>
						</div>
					</div>
				<%-- 	</c:if> --%>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>科室名称:</label>
						<div class="col-sm-3">
							<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">科室编码:</label>
						<div class="col-sm-3">
							<form:input path="code" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">类型:</label>
						<div class="col-sm-3">
							<form:select path="type" class="form-control select2">
								<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">级别:</label>
						<div class="col-sm-3">
							<form:select path="grade" class="form-control select2">
								<form:options items="${fns:getDictList('sys_office_grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">是否可用:</label>
						<div class="col-sm-1">
							<form:select path="useable" class="form-control select2">
								<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
						<div class="col-sm-7 text-muted">“是”代表此账号允许登陆，“否”则表示此账号不允许登陆</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">主负责人:</label>
						<div class="col-sm-3">
							 <sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
								title="用户" url="/sys/office/treeData?type=5" allowClear="true" cssClass="form-control" notAllowSelectParent="true"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">副负责人:</label>
						<div class="col-sm-3">
							 <sys:treeselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="office.deputyPerson.name" labelValue="${office.deputyPerson.name}"
								title="用户" url="/sys/office/treeData?type=5" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">联系地址:</label>
						<div class="col-sm-3">
							<form:input path="address" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">邮政编码:</label>
						<div class="col-sm-3">
							<form:input path="zipCode" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">负责人:</label>
						<div class="col-sm-3">
							<form:input path="master" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">电话:</label>
						<div class="col-sm-3">
							<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">传真:</label>
						<div class="col-sm-3">
							<form:input path="fax" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">邮箱:</label>
						<div class="col-sm-3">
							<form:input path="email" htmlEscape="false" maxlength="50" class="form-control email"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">备注:</label>
						<div class="col-sm-5">
							<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
					    <div class="col-sm-offset-1 col-sm-11">
						<shiro:hasPermission name="sys:office:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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