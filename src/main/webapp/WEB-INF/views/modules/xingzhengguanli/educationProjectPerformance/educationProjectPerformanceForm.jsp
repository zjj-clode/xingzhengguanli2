<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>项目支出绩效目标申报管理<small>项目支出绩效目标申报信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/xingzhengguanli/educationProjectPerformance/">项目支出绩效目标申报管理</a></li>
        <li class="active">项目支出绩效目标申报<shiro:hasPermission name="xingzhengguanli:educationProjectPerformance:edit">${not empty educationProjectPerformance.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="xingzhengguanli:educationProjectPerformance:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="educationProjectPerformance" action="${ctx}/xingzhengguanli/educationProjectPerformance/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="user.id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					年份：</label>
					
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input name="year" type="text" style="width: 230px" maxlength="20" class="form-control pull-right"
							value="${educationProjectPerformance.year}"
							onclick="WdatePicker({dateFmt:'yyyy',isShowClear:true});"/>
				        </div>
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					项目名称：</label>
					
					<div class="col-sm-3">
						<form:input path="projectName" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					主管部门及代码：</label>
					
					<div class="col-sm-3">
						<form:input path="projectCode" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					实施单位：</label>
					
					<div class="col-sm-3">
						<form:input path="projectUnit" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					项目属性：</label>
					
					<div class="col-sm-3">
						<form:input path="projectCategory" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					项目周期：</label>
					
					<div class="col-sm-3">
						<form:input path="projectCycle" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					中期资金总额：</label>
					
					<div class="col-sm-3">
						<form:input path="metaphaseMoney" htmlEscape="false" class="form-control  number"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					中期其他资金：</label>
					
					<div class="col-sm-3">
						<form:input path="metaphaseOtherMoney" htmlEscape="false" class="form-control  number"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					中期财政拨款：</label>
					
					<div class="col-sm-3">
						<form:input path="metaphaseFinanceMoney" htmlEscape="false" class="form-control  number"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					年度资金总额：</label>
					
					<div class="col-sm-3">
						<form:input path="shortTermMoney" htmlEscape="false" class="form-control  number"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					年度其他资金：</label>
					
					<div class="col-sm-3">
						<form:input path="shortTermOtherMoney" htmlEscape="false" class="form-control  number"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					年度财政拨款：</label>
					
					<div class="col-sm-3">
						<form:input path="shortTermFinanceMoney" htmlEscape="false" class="form-control  number"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					年度目标：</label>
					
					<div class="col-sm-3">
						<form:textarea path="shortTermContent" htmlEscape="false" rows="4" class="form-control "/>
					
					</div>
				</div>
				
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					中期目标：</label>
					
					<div class="col-sm-3">
						<form:textarea path="metaphaseContent" htmlEscape="false" rows="4" class="form-control "/>
					
					</div>
				</div>
				
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					一级指标：</label>
					
					<div class="col-sm-3">
						<form:select path="shortTermFirstTarget" class="form-control ">
							<form:option value="" label="=== 请选择一级指标 ==="/>
							<form:options items="${fns:getDictList('first_target')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				
				
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					年度二级指标：</label>
					
					<div class="col-sm-3">
						<form:select path="shortTermTargetType" class="form-control ">
							<form:option value="" label="=== 请选择短期二级指标 ==="/>
							<form:options items="${fns:getDictList('target_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					年度三级指标：</label>
					
					<div class="col-sm-3">
						<form:input path="shortTermThirdTargetName" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					短期指标值：</label>
					
					<div class="col-sm-3">
						<form:input path="shortTermTargetValue" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					中期二级指标：</label>
					
					<div class="col-sm-3">
						<form:select path="metaphaseTargetType" class="form-control ">
							<form:option value="" label="=== 请选择中期二级指标 ==="/>
							<form:options items="${fns:getDictList('target_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					中期三级指标：</label>
					
					<div class="col-sm-3">
						<form:input path="metaphaseThirdTargetName" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					中期指标值：</label>
					
					<div class="col-sm-3">
						<form:input path="metaphaseTargetValue" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					单位：</label>
					
					<div class="col-sm-3">
						<form:select path="quantityUnit" class="form-control ">
							<form:option value="" label="=== 请选择单位 ==="/>
							<form:options items="${fns:getDictList('quantity_unit')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
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
					<shiro:hasPermission name="xingzhengguanli:educationProjectPerformance:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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