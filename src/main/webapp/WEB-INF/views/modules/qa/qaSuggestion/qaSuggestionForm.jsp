<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>投诉建议信息管理<small>投诉建议信息信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/qa/qaSuggestion/">投诉建议信息管理</a></li>
        <li class="active">投诉建议信息<shiro:hasPermission name="qa:qaSuggestion:edit">${not empty qaSuggestion.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="qa:qaSuggestion:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="qaSuggestion" action="${ctx}/qa/qaSuggestion/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<!-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					  <span class="text-red fm">*</span>			
					建议者id：</label>
					
					<div class="col-sm-3">
						<sys:treeselect id="user" name="user.id" value="${qaSuggestion.user.id}" labelName="user.name" labelValue="${qaSuggestion.user.name}"
							title="建议者id" url="/sys/office/treeData?type=3" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					
					</div>
				</div> -->
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					  <span class="text-red fm">*</span>			
					投诉建议内容：</label>
					
					<div class="col-sm-3">
						<form:textarea path="content" htmlEscape="false" rows="4" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					备注信息：</label>
					
					<div class="col-sm-3">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="qa:qaSuggestion:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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