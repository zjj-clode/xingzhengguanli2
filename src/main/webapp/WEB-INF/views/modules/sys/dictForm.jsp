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
        <small>字典信息添加与修改</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/sys/dict">字典管理</a></li>
        <li class="active">信息添加与修改</li>
      </ol>
    </section>
    <!-- /.content-header -->
    
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post" class="form-horizontal form-information pad  form-lie">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>键值:</label>
					<div class="col-sm-3">
						<form:input path="value" htmlEscape="false" maxlength="50" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>标签:</label>
					<div class="col-sm-3">
						<form:input path="label" htmlEscape="false" maxlength="50" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>类型:</label>
					<div class="col-sm-3">
						<form:input path="type" htmlEscape="false" maxlength="50" class="form-control required abc"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>描述:</label>
					<div class="col-sm-3">
						<form:input path="description" htmlEscape="false" maxlength="50" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>排序:</label>
					<div class="col-sm-2">
						<form:input path="sort" htmlEscape="false" maxlength="11" class="form-control required digits"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">备注:</label>
					<div class="col-sm-3">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="sys:dict:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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