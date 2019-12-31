<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>客服管理<small>在线客服人员</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/chat/chatCustom/">客服管理</a></li>
        <li class="active">在线客服人员<shiro:hasPermission name="chat:chatCustom:edit">${not empty chatCustom.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="chat:chatCustom:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="chatCustom" action="${ctx}/chat/chatCustom/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				
				<c:choose>
					<c:when test="${empty chatCustom.id}">
						<div class="form-group">
							<label class="col-sm-1 control-label">
							客服人员：</label>
							<div class="col-sm-3">
								<sys:treeselect id="user" name="user.id" value="${chatCustom.user.id}" labelName="user.name" labelValue="${chatCustom.user.name}"
									title="客服人员" url="/sys/office/treeData?type=5" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="form-group">
							<label class="col-sm-1 control-label">
							客服人员：</label>
							<div class="col-sm-3">
								<label class="control-label">${chatCustom.user.name}</label>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
				
				<div class="form-group">
					<label class="col-sm-1 control-label">
					优先级：</label>
					<div class="col-sm-3">
						<form:select path="priority" class="input-medium select2" cssStyle="width:100%;">
							<c:forEach begin="1" end="10" step="1" varStatus="status" var="item">
								<form:option value="${item}" label="${item}"/>
							</c:forEach>
						</form:select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-1 control-label">
					暂停服务：</label>
					<div class="col-sm-3">
						<form:select path="delFlag" class="input-medium select2" cssStyle="width:100%;">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="chat:chatCustom:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
					</div>
				</div>
			</form:form>
	      </div>
     </div>
   </section>
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