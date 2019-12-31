<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>项目支出实施方案管理<small>项目支出实施方案信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/xingzhengguanli/educationProjectImplementationPlan/">项目支出实施方案管理</a></li>
        <li class="active">项目支出实施方案<shiro:hasPermission name="xingzhengguanli:educationProjectImplementationPlan:edit">${not empty educationProjectImplementationPlan.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="xingzhengguanli:educationProjectImplementationPlan:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="educationProjectImplementationPlan" action="${ctx}/xingzhengguanli/educationProjectImplementationPlan/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="user.id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>项目名称：</label>
					
					<div class="col-sm-3">
						<form:input path="projectName" htmlEscape="false" maxlength="255" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>项目单位：</label>
					
					<div class="col-sm-3">
						<form:input path="projectUnit" htmlEscape="false" maxlength="255" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>主管单位及代码：</label>
					
					<div class="col-sm-3">
						<form:input path="projectCode" htmlEscape="false" maxlength="255" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>项目类别：</label>
					
					<div class="col-sm-3">
						<form:input path="projectCategory" htmlEscape="false" maxlength="255" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>项目开始年份：</label>
					
					<div class="col-sm-3">
						<form:input path="projectStartYear" htmlEscape="false" maxlength="255" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>项目周期：</label>
					
					<div class="col-sm-3">
						<form:input path="projectCycle" htmlEscape="false" maxlength="255" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					一、深入推进高校创新创业教育改革：</label>
					
					<div class="col-sm-8">
						<form:textarea path="reformInEducation" htmlEscape="false" rows="6" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					二、巩固本科教学基础地位：</label>
					
					<div class="col-sm-8">
						<form:textarea path="basicPosition" htmlEscape="false" rows="6" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					三、调整优化学科专业结构：</label>
					
					<div class="col-sm-8">
						<form:textarea path="professionalStructure" htmlEscape="false" rows="6" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					四、完善协同育人机制：</label>
					
					<div class="col-sm-8">
						<form:textarea path="mechanism" htmlEscape="false" rows="6" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					五、着力推进信息技术与教育教学深度融合：</label>
					
					<div class="col-sm-8">
						<form:textarea path="fuse" htmlEscape="false" rows="6" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					六、建立完善拔尖人才培养体制机制：</label>
					
					<div class="col-sm-8">
						<form:textarea path="personnelTraining" htmlEscape="false" rows="6" class="form-control "/>
					
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
					<shiro:hasPermission name="xingzhengguanli:educationProjectImplementationPlan:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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