<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>工作安排设置管理<small>工作安排设置信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/xingzhengguanli/workArrangementSetting/">工作安排设置管理</a></li>
        <li class="active">工作安排设置<shiro:hasPermission name="xingzhengguanli:workArrangementSetting:edit">${not empty workArrangementSetting.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="xingzhengguanli:workArrangementSetting:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="workArrangementSetting" action="${ctx}/xingzhengguanli/workArrangementSetting/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<%-- <input type="hidden" id="startDate" name="startDate" value="<fmt:formatDate value="${workArrangementSetting.startDate}" pattern="yyyy-MM-dd"/>"/>
				<input type="hidden" id="endDate" name="endDate" value="<fmt:formatDate value="${workArrangementSetting.endDate}" pattern="yyyy-MM-dd"/>"/>
				 --%><form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>部门：</label>
					<div class="col-sm-3">
						<form:input path="department" htmlEscape="false" maxlength="255" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>标题：</label>
					<div class="col-sm-3">
						<form:input path="title" htmlEscape="false" maxlength="255" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>每周开始时间：</label>
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input name="startDate" type="text" style="width: 230px" maxlength="20" class="form-control required"
							value="<fmt:formatDate value="${workArrangementSetting.startDate}" pattern="yyyy-MM-dd"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				        </div>
					
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>每周结束时间：</label>
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input name="endDate" type="text" style="width: 230px" maxlength="20" class="form-control required"
							value="<fmt:formatDate value="${workArrangementSetting.endDate}" pattern="yyyy-MM-dd"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				        </div>
					
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label">备注信息：</label>
					<div class="col-sm-3">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
					</div>
				</div>
				
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="xingzhengguanli:workArrangementSetting:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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