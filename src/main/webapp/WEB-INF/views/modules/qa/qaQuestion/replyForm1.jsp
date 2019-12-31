<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>在线咨询回复管理<small>在线咨询回复信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/qa/qaAnswer/">在线咨询回复管理</a></li>
        <li class="active">在线咨询回复</li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="qaAnswer" action="${ctx}/qa/qaQuestion/reply1" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="question.id"/>
				<form:hidden path="question.queryString"/>
				<sys:message content="${message}"/>
				<c:if test="${question.questioner eq '0'}">
					<div class="form-group">
						
						<label class="col-sm-1 control-label">
						提问者姓名：</label>
						
						<div class="col-sm-3">
							<form:input path="question.name" htmlEscape="false" maxlength="255" class="form-control " readonly="true" disabled="true"/>
						
						</div>
					</div>
					<div class="form-group">
						
						<label class="col-sm-1 control-label">
						身份：</label>
						
						<div class="col-sm-3">
							<form:select path="question.identify" class="form-control " readonly="true" disabled="true">
								<form:option value="" label=""/>
								<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
						
						</div>
					</div>
				 </c:if>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					科类：</label>
					
					<div class="col-sm-3">
						<form:select path="question.klmc" class="form-control " readonly="true" disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('kl')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					省份：</label>
					
					<div class="col-sm-3">
						<form:select path="question.province" class="form-control " readonly="true" disabled="true">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('ssmc')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				 
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					提问内容：</label>
					
					<div class="col-sm-3">
						<form:textarea path="question.content" htmlEscape="false" rows="4" class="form-control " readonly="true" disabled="true"/>
					
					</div>
				</div>

				<%--<div class="form-group">
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>			
					回复内容：</label>
					<div class="col-sm-3">
						<form:textarea path="respContent" htmlEscape="false" rows="8" class="form-control required"/>
					</div>
				</div>--%>
				<div class="form-group">
						<label class="col-sm-1 control-label">回复内容:</label>
						<div class="col-sm-9">
							<form:textarea id="respContent" htmlEscape="true" path="respContent" rows="4" maxlength="200" class="form-control"/>
							<sys:ckeditor replace="respContent" uploadPath="/cms/article" />
						</div>
					</div> 
				 <c:if test="${qaQuestion.questioner eq '0'}"> 
					<div class="form-group">
						
						<label class="col-sm-1 control-label">
						<span class="text-red fm">*</span>			
						是否发送邮件：</label>
						
						<div class="col-sm-3">
							<select name="sendMail" class="form-control">
								<option value="false">否</option>
								<option value="true">是</option>
							</select>
						</div>
					</div>
			</c:if>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>			
					是否公开：</label>
					
					<div class="col-sm-3">
						<form:select path="question.isopen" class="form-control required">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="qa:qaQuestion:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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