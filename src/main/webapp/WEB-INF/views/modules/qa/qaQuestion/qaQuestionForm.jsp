<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>在线咨询管理<small>在线咨询信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/qa/qaQuestion/">在线咨询管理</a></li>
        <li class="active">在线咨询<shiro:hasPermission name="qa:qaQuestion:edit">${not empty qaQuestion.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="qa:qaQuestion:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="qaQuestion" action="${ctx}/qa/qaQuestion/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<%-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					提问者：</label>
					
					<div class="col-sm-3">
						<sys:treeselect id="user" name="user.id" value="${qaQuestion.user.id}" labelName="user.name" labelValue="${qaQuestion.user.name}"
							title="提问者" url="/sys/office/treeData?type=3" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>
					
					</div>
				</div> --%>
				<c:if test="${question.questioner eq '0'}">
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					提问者姓名：</label>
					
					<div class="col-sm-3">
						<form:input path="name" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					Email：</label>
					
					<div class="col-sm-3">
						<form:input path="email" htmlEscape="false" maxlength="255" class="form-control email"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					手机：</label>
					
					<div class="col-sm-3">
						<form:input path="mobile" htmlEscape="false" maxlength="20" class="form-control mobile"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					身份：</label>
					
					<div class="col-sm-3">
						<form:select path="identify" class="form-control ">
							<form:option value="" label="=== 请选择提问者身份 ==="/>
							<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				</c:if>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					科类：</label>
					
					<div class="col-sm-3">
						<form:select path="klmc" class="form-control ">
							<form:option value="" label="=== 请选择科类名称 ==="/>
							<form:options items="${fns:getDictList('kl')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					省份：</label>
					
					<div class="col-sm-3">
						<form:select path="province" class="form-control ">
							<form:option value="" label="=== 请选择提问人省份 ==="/>
							<form:options items="${fns:getDictList('ssmc')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				<%-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					提问人ip：</label>
					
					<div class="col-sm-3">
						<form:input path="ip" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div> --%>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					提问时间：</label>
					
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input name="time" type="text" maxlength="20" class="form-control "
							value="<fmt:formatDate value="${qaQuestion.time}" pattern="yyyy-MM-dd HH:mm"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"/>
				        </div>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					访问设备：</label>
					
					<div class="col-sm-3">
						<form:select path="device" class="form-control ">
							<form:option value="" label="=== 请选择访问设备 ==="/>
							<form:options items="${fns:getDictList('device_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					提问内容：</label>
					
					<div class="col-sm-3">
						<form:textarea path="content" htmlEscape="false" rows="4" class="form-control "/>
					
					</div>
				</div>
				<%-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					信息状态：</label>
					
					<div class="col-sm-3">
						<form:select path="state" class="form-control ">
							<form:option value="" label="=== 请选择信息状态 ==="/>
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div> --%>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					是否公开：</label>
					
					<div class="col-sm-3">
						<form:select path="isopen" class="form-control ">
							<form:option value="" label="=== 请选择是否公开 ==="/>
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					权重：</label>
					<span>0-999值越大排序越靠前</span>
					<div class="col-sm-3">
						<form:input path="weight" htmlEscape="false" maxlength="20" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					咨询次数：</label>
					<div class="col-sm-3">
						<form:input path="searchTimes" htmlEscape="false" maxlength="20" class="form-control" disabled="true"/>
					</div>
				</div>
				<%-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					已发送邮件：</label>
					
					<div class="col-sm-3">
						<form:select path="hassendemail" class="form-control ">
							<form:option value="" label="=== 请选择已发送邮件 ==="/>
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div> --%>
				<%-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					是否被索引：</label>
					
					<div class="col-sm-3">
						<form:select path="hasindex" class="form-control ">
							<form:option value="" label="=== 请选择是否被索引 ==="/>
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div> --%>
				<%-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					openid：</label>
					
					<div class="col-sm-3">
						<form:input path="openid" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					type：</label>
					
					<div class="col-sm-3">
						<form:input path="type" htmlEscape="false" maxlength="64" class="form-control "/>
					
					</div>
				</div> --%>
				
				
				<c:choose>
					<c:when test="${fn:length(qaQuestion.answerList)>0}">
						<c:forEach items="${qaQuestion.answerList}" var="answer">
							<c:if test="${answer.responderType eq '1'}">
								<div class="form-group">
									<label class="col-sm-1 control-label">
									回复内容：</label>
									<div class="col-sm-3">
										<%--<form:textarea path="content" htmlEscape="false" rows="4" class="form-control "/>--%>
										<textarea rows="6" cols="30" disabled="disabled" class="form-control ">
										${answer.respContent}
										</textarea>
									</div>
								</div>
								
							</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="form-group">
							<label class="col-sm-1 control-label">
							回复内容：</label>
							<div class="col-sm-3">
								<%--<form:textarea path="content" htmlEscape="false" rows="4" class="form-control "/>--%>
								<textarea rows="6" cols="30" disabled="disabled">
								未回复！
								</textarea>
							</div>
						</div>
					</c:otherwise>
				</c:choose>				
				
				
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