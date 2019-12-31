<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>项目支出明细管理<small>项目支出明细信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/xingzhengguanli/educationProjectSpendDetails/">项目支出明细管理</a></li>
        <li class="active">项目支出明细<shiro:hasPermission name="xingzhengguanli:educationProjectSpendDetails:edit">${not empty educationProjectSpendDetails.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="xingzhengguanli:educationProjectSpendDetails:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="educationProjectSpendDetails" action="${ctx}/xingzhengguanli/educationProjectSpendDetails/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="user.id"/>
				<form:hidden path="projectDes.id"/>
				<form:hidden path="year"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					 <span class="text-red fm">*</span>年份：</label>
					
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input id="year" name="year" type="text" style="width: 230px" maxlength="20" class="form-control required"
							value="${educationProjectSpendDetails.year}"
							onclick="WdatePicker({dateFmt:'yyyy',isShowClear:true});"/>
				        </div>
				        
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					项目活动：</label>
					
					<div class="col-sm-3">
						<form:input path="projectName" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					对项目活动的描述：</label>
					
					<div class="col-sm-3">
						<form:textarea path="projectDescription" htmlEscape="false" rows="4" maxlength="500" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					子活动：</label>
					
					<div class="col-sm-3">
						<form:input path="childProject" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					对子活动的描述：</label>
					
					<div class="col-sm-3">
						<form:textarea path="childProjectDescription" htmlEscape="false" rows="4" maxlength="500" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					分项支出：</label>
					
					<div class="col-sm-3">
						<form:input path="subItemExpenditure" htmlEscape="false" maxlength="64" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					数量/频率：</label>
					
					<div class="col-sm-3">
						<form:input path="numberFrequency" htmlEscape="false" maxlength="64" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					支出计划（万元）：</label>
					
					<div class="col-sm-3">
						<form:input path="money" htmlEscape="false" class="form-control  number"/>
					
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
					<shiro:hasPermission name="xingzhengguanli:educationProjectSpendDetails:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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