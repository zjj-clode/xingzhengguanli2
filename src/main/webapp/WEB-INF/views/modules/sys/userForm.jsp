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
        <small>用户信息修改</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/sys/user">用户管理</a></li>
        <li class="active">信息修改</li>
      </ol>
    </section>
    <!-- /.content-header -->
    
    <section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal form-information pad  form-lie">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-1 control-label">头像:</label>
					<div class="col-sm-3">
						<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="form-control"/>
						<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">用户类型:</label>
					<div class="col-sm-3 radio">
					    <%-- <c:if test="${not empty user.id}">
						${fns:getDictLabel(user.userType, 'sys_user_type', '')}
						</c:if>
						<c:if test="${empty user.id}"> --%>
						<form:select path="userType" class="form-control select2 required">
							<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
						<%-- </c:if> --%>
					</div>
				</div>
				 <div class="form-group">
					<label class="col-sm-1 control-label">部门:</label>
					<div class="col-sm-3 radio">
		                <sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
							title="院系" url="/sys/office/treeData?type=6" cssClass="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">科室:</label>
					<div class="col-sm-3">
		                <sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
							title="科室" url="/sys/office/treeData?type=2" cssClass="form-control required"/>
					</div>
				</div>
				 
				
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>姓名:</label>
					<div class="col-sm-3">
						<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>登录名:</label>
					<div class="col-sm-3 radio">
						<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
						<form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control required " readonly="${fns:getUser().userType >1?'true':'false'}"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><c:if test="${empty user.id}"><span class="text-red fm">*</span></c:if>密码:</label>
					<div class="col-sm-3">
						<input id="newPassword" name="newPassword" type="text" onfocus="this.type='password'" value="" maxlength="50" minlength="3" autocomplete="off"  class="form-control password ${empty user.id?'required':''}"/>
					</div>
					<div class="col-sm-7 text-muted"><c:if test="${not empty user.id}">若不修改密码，请留空。</c:if></div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><c:if test="${empty user.id}"><span class="text-red fm">*</span></c:if>确认密码:</label>
					<div class="col-sm-3">
						<input id="confirmNewPassword" name="confirmNewPassword" type="text" onfocus="this.type='password'" value="" maxlength="50" minlength="3"  class="form-control password" autocomplete="off" equalTo="#newPassword"/>
					</div>
					<div class="col-sm-7 text-muted">8-32位，不能是纯数字</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">工号:</label>
					<div class="col-sm-3">
						<form:input path="no" htmlEscape="false" maxlength="100" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">邮箱:</label>
					<div class="col-sm-3">
						<form:input path="email" htmlEscape="false" maxlength="100" class="form-control email"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">电话:</label>
					<div class="col-sm-3">
						<form:input path="phone" htmlEscape="false" maxlength="100" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">手机:</label>
					<div class="col-sm-3">
						<form:input path="mobile" htmlEscape="false" maxlength="100" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>是否允许登录:</label>
					<div class="col-sm-3">
						<form:select path="loginFlag" class="form-control select2">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
					<div class="col-sm-7 text-muted"> “是”代表此账号允许登录，“否”则表示此账号不允许登录</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>用户角色:</label>
					<div class="col-sm-9  checkbox">
						     
						    <c:forEach items="${allRoles}" var="role" varStatus="status">
						     <label style="margin-right:10px">
						      <input type="checkbox"  class="required"name="roleIdList" id="roleIdList" value="${role.id}" 
						      
						      <c:forEach items="${user.roleIdList}" var="roleId" >
						      <c:if test="${roleId eq role.id}">checked="true"</c:if> 
						      </c:forEach>
						      
						      >${role.name}
							 </label>
						    </c:forEach> 
						    
						    
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">备注:</label>
					<div class="col-sm-6">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
					</div>
				</div>
				<c:if test="${not empty user.id}">
					<div class="form-group">
						<label class="col-sm-1 control-label">创建时间:</label>
						<div class="col-sm-3 radio">
							<fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">最后登陆:</label>
						<div class="col-sm-3 radio">
							IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/>
						</div>
					</div>
				</c:if>
				<div class="form-group">
				    <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="sys:user:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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
       $("#name").focus();
		$("#inputForm").validate({
			rules: {
				loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
			},
			messages: {
				loginName: {remote: "用户登录名已存在"},
				confirmNewPassword: {equalTo: "输入与上面相同的密码"}
			},
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
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