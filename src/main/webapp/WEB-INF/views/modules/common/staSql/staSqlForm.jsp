<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>统计模板管理<small>统计模板信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/common/staSql/">统计模板管理</a></li>
        <li class="active">统计模板<shiro:hasPermission name="common:staSql:edit">${not empty staSql.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="common:staSql:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="staSql" action="${ctx}/common/staSql/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					  <span class="text-red fm">*</span>			
					统计名称：</label>
					
					<div class="col-sm-3">
						<form:input path="staname" htmlEscape="false" maxlength="64" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					  <span class="text-red fm">*</span>			
					统计SQL：</label>
					
					<div class="col-sm-3">
						<form:textarea path="stasql" htmlEscape="false" rows="10" style="width:600px" class="form-control required"/>
					
					</div>
				</div>
		        <div class="form-group">
					<label class="col-sm-1 control-label">
					  <span class="text-red fm">*</span>			
					列表头：</label>
					<div class="col-sm-3">
						<form:textarea path="colinf" htmlEscape="false" rows="4" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					 <span class="text-red fm">*</span>
					表头维度：</label>
					<div class="col-sm-3">
						<form:input path="headlevel" style="width:100px" htmlEscape="false" maxlength="64" class="form-control required digits"/>
					
					</div>
				</div>
		
				<div class="form-group">
					<label class="col-sm-1 control-label">
					 <span class="text-red fm">*</span>
					表头信息：</label>
					<div class="col-sm-3">
						<form:textarea path="headinf" htmlEscape="false" rows="4" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					 <span class="text-red fm">*</span>
					统计类型：</label>
					
					<div class="col-sm-3">
						<form:select path="statype" class="form-control required">
							<form:option value="" label="=== 请选择统计类型 ==="/>
							<form:options items="${fns:getDictList('statype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					 <span class="text-red fm">*</span>
					排序：</label>
					<div class="col-sm-3">
						<form:input path="sort" style="width:100px" htmlEscape="false" maxlength="64" class="form-control required digits"/>
					
					</div>
				</div>
					<div class="form-group">
					
					<label class="col-sm-1 control-label">		
					合并列：</label>
					<div class="col-sm-3">
						<form:input path="mercol" htmlEscape="false" maxlength="64" class="form-control "/>
					
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
					<shiro:hasPermission name="common:staSql:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
					<input id="btnCancel" class="btn btn-default" type="button" value="返 回" onclick="history.go(-1)"/>
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