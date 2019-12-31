<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>api访问码管理<small>api访问码信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/api/apiCodeInf/">api访问码管理</a></li>
        <li class="active">api访问码<shiro:hasPermission name="api:apiCodeInf:edit">${not empty apiCodeInf.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="api:apiCodeInf:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="apiCodeInf" action="${ctx}/api/apiCodeInf/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>api验证码：</label>
					<div class="col-sm-3">
						<form:input path="apicode" htmlEscape="false" maxlength="64" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>用户名：</label>
					<div class="col-sm-3">
						<form:input path="username" htmlEscape="false" maxlength="32" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>用户单位：</label>
					<div class="col-sm-3">
						<form:input path="userunit" htmlEscape="false" maxlength="255" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>限定次数：</label>
					<div class="col-sm-3">
						<form:input path="limitcount" htmlEscape="false" maxlength="255" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>应用次数：</label>
					<div class="col-sm-3">
						<form:input path="usercount" htmlEscape="false" maxlength="128" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>限定日期：</label>
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input name="deaddate" type="text" style="width: 265px" maxlength="20" class="form-control"
							value="<fmt:formatDate value="${apiCodeInf.deaddate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				        </div>
					</div>
				</div>
				<%-- <div class="form-group">
					<label class="col-sm-1 control-label">自定义统计表:</label>
					<div class="col-sm-9  checkbox">
						     
						    <c:forEach items="${statisticsList}" var="statistics" varStatus="status">
						     <label style="margin-right:10px">
						      <input type="checkbox" name="staIdList" id="staIdList" value="${statistics.id}" 
						      
						    <c:forEach items="${staIds}" var="staId" >
						      <c:if test="${staId eq statistics.id}">checked="true"</c:if> 
						      </c:forEach>  
						      >${statistics.staname} 
							 </label>
						    </c:forEach> 
						   
						    
					</div>
				</div> --%>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>备注信息：</label>
					<div class="col-sm-3">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="api:apiCodeInf:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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