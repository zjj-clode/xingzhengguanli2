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
			<form:form id="inputForm" modelAttribute="educationProjectPerformance" action="${ctx}/xingzhengguanli/educationProjectPerformance/batchsave" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="user.id"/>
				<form:hidden path="projectName"/>
				<form:hidden path="projectCode"/>
				<form:hidden path="projectUnit"/>
				<form:hidden path="projectCategory"/>
				<form:hidden path="projectCycle"/>
				<input type="hidden" id="targetContentData" name="targetContentData" value=""/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>年份：</label>
					
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input id="year" name="year" type="text" style="width: 230px" maxlength="20" class="form-control required"
							value="${educationProjectPerformance.year}"
							onclick="WdatePicker({dateFmt:'yyyy',isShowClear:true});"/>
				        </div>
					</div>
				</div>
			
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>年度目标：</label>
					
					<div class="col-sm-8">
						<form:textarea path="shortTermContent" htmlEscape="false" rows="4" class="form-control required"/>
					
					</div>
				</div>
				
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>中期目标：</label>
					
					<div class="col-sm-8">
						<form:textarea path="metaphaseContent" htmlEscape="false" rows="4" class="form-control required"/>
					
					</div>
				</div>
				
				<div class="form-group">
					
					<div class="col-sm-10">
						 <table class="ui celled table_semantic" id="educationProjectPerformanceListTable">
							<thead>
								<tr>
									<th colspan="5">年度目标</th>
									<th colspan="3">中期目标</th>
									<th></th>
								</tr>
								<tr>
									<th>一级指标</th>
									<th>二级指标</th>
									<th>三级指标</th>
									<th>指标值</th>
									<th>单位</th>
									<th>二级指标</th>
									<th>三级指标</th>
									<th>指标值</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${performanceIndicatorsList}" var="item" varStatus="st">
									<tr class="targetInput" id="target${st.index+1}">
										<td>
											<input type="hidden" name="shortTermFirstTarget" value="${item.firstTarget}"/>
											<input type="hidden" name="shortTermFirstTargetName" value="${item.firstTargetName}"/>
											${item.firstTargetName}
										</td>
										<td>
											<input type="hidden" name="shortTermTargetType" value="${item.targetType}"/>
											<input type="hidden" name="shortTermSecondTargetName" value="${item.secondTargetName}"/>
											${item.secondTargetName}
										
										</td>
										<td>
											<textarea onchange="targetNameChange('shortTermThirdTargetName${st.index+1}','metaphaseThirdTargetName${st.index+1}')" id="shortTermThirdTargetName${st.index+1}" name="shortTermThirdTargetName" maxlength="255" class="form-control" rows="2">${item.thirdTargetName}</textarea>
										
										</td>
										<td>
											<input class="form-control <c:if test="${item.targetType == '1'}"> number</c:if> " 
											id="shortTermTargetValue${st.index+1}" name="shortTermTargetValue" value=""
											 onchange="targetValueChange('${item.targetType}','shortTermTargetValue${st.index+1}','metaphaseTargetValue${st.index+1}')" />
										</td>
										<td>
											<select name="quantityUnit" class="form-control">
												<option value="">请选择</option>
												<c:forEach items="${fns:getDictList('quantity_unit')}" var="dict">
													<option value="${dict.value}">${dict.label}</option>
												</c:forEach>
											</select>
										</td>
										<td>
											<input type="hidden" name="metaphaseTargetType" value="${item.targetType}"/>
											<input type="hidden" name="metaphaseSecondTargetName" value="${item.secondTargetName}"/>
											${item.secondTargetName}
										</td>
										<td>
											<textarea  id="metaphaseThirdTargetName${st.index+1}" name="metaphaseThirdTargetName" maxlength="255" class="form-control" rows="2">${item.thirdTargetName}</textarea>
										</td>
										<td>
											<input id="metaphaseTargetValue${st.index+1}" class="form-control <c:if test="${item.targetType == '1'}"> number</c:if>" name="metaphaseTargetValue" value=""/>
										</td>
										<td>
										<a href="javascript:void(0);" onclick="addTarget('target${st.index+1}')" class="btn btn-default btn-sm" title="添加"><i class="fa fa-plus-square"></i></a>
										<a href="javascript:void(0);" onclick="deleteTarget('target${st.index+1}')" class="btn btn-default btn-sm" title="删除"><i class="fa fa-minus-circle"></i></a>
								
										</td>
										
									</tr>
								</c:forEach>
							</tbody>
						</table>
					
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
  function deleteTarget(id){
	  $("#"+id).remove();
  }
  var num = 0;
  function addTarget(id){
	  num++;
	  var str = '<tr class="targetInput" id="targetadd'+num+'">'+
						'<td>'+
							'<select name="shortTermFirstTarget" class="form-control">'+
								'<option value="">请选择</option>'+
								'<c:forEach items="${fns:getDictList('first_target')}" var="dict">'+
									'<option value="${dict.value}">${dict.label}</option>'+
								'</c:forEach>'+
							'</select>'+
							'<input type="hidden" name="shortTermFirstTargetName" value=""/>'+
							
						'</td>'+
						'<td>'+
							'<select id="shortTermTargetTypeAdd'+num+'" name="shortTermTargetType" class="form-control" '+
							'onchange="targetTypeChange(\'shortTermTargetTypeAdd'+num+'\',\'shortTermTargetValueAdd'+num+'\',\'metaphaseTargetValueAdd'+num+'\',\'metaphaseTargetTypeAdd'+num+'\')">'+
								'<option value="">请选择</option>'+
								'<c:forEach items="${fns:getDictList('target_type')}" var="dict">'+
									'<option value="${dict.value}">${dict.label}</option>'+
								'</c:forEach>'+
							'</select>'+
							'<input type="hidden" name="shortTermSecondTargetName" value=""/>'+
							
						'</td>'+
						'<td>'+
							'<textarea onchange="targetNameChange(\'shortTermThirdTargetNameAdd'+num+'\',\'metaphaseThirdTargetNameAdd'+num+'\')" id="shortTermThirdTargetNameAdd'+num+'" name="shortTermThirdTargetName" maxlength="255" class="form-control" rows="2"></textarea>'+
						
						'</td>'+
						'<td>'+
							'<input id="shortTermTargetValueAdd'+num+'" class="form-control" name="shortTermTargetValue" value=""/>'+
						'</td>'+
						'<td>'+
							'<select name="quantityUnit" class="form-control">'+
								'<option value="">请选择</option>'+
								'<c:forEach items="${fns:getDictList('quantity_unit')}" var="dict">'+
									'<option value="${dict.value}">${dict.label}</option>'+
								'</c:forEach>'+
							'</select>'+
						'</td>'+
						'<td>'+
							'<select id="metaphaseTargetTypeAdd'+num+'" name="metaphaseTargetType" class="form-control">'+
								'<option value="">请选择</option>'+
								'<c:forEach items="${fns:getDictList('target_type')}" var="dict">'+
									'<option value="${dict.value}">${dict.label}</option>'+
								'</c:forEach>'+
							'</select>'+
							'<input type="hidden" name="metaphaseSecondTargetName" value=""/>'+
							
						'</td>'+
						'<td>'+
							'<textarea id="metaphaseThirdTargetNameAdd'+num+'" name="metaphaseThirdTargetName" maxlength="255" class="form-control" rows="2"></textarea>'+
						'</td>'+
						'<td>'+
							'<input id="metaphaseTargetValueAdd'+num+'" class="form-control" name="metaphaseTargetValue" value=""/>'+
						'</td>'+
						'<td>'+
						'<a href="javascript:void(0);" onclick="addTarget(\'targetadd'+num+'\')" class="btn btn-default btn-sm" title="添加"><i class="fa fa-plus-square"></i></a>'+
						'<a href="javascript:void(0);" onclick="deleteTarget(\'targetadd'+num+'\')" class="btn btn-default btn-sm" title="删除"><i class="fa fa-minus-circle"></i></a>'+
					
						'</td>'+
						
					'</tr>';
		$("#"+id).after(str);
  }
  function targetValueChange(targetVlaue,shortTermTargetValueId,metaphaseTargetValueId){
	  var shortTermTargetValue = $("#"+shortTermTargetValueId).val();
	  if('1' == targetVlaue && shortTermTargetValue != null && shortTermTargetValue != ''){
		  var value = parseFloat(shortTermTargetValue)*3;
		  $("#"+metaphaseTargetValueId).val(value);
	  }else{
		  $("#"+metaphaseTargetValueId).val(shortTermTargetValue);
	  }
  }
  function targetTypeChange(targetTypeId,targetValueId,metaphaseTargetValueId,metaphaseTargetTypeId){
	  var targetType = $("#"+targetTypeId).val();
	  if(targetType != null && targetType != '' && targetType == '1'){
		  $("#"+targetValueId).addClass("number");
		  $("#"+metaphaseTargetValueId).addClass("number");
	  }else{
		  $("#"+targetValueId).removeClass("number");
		  $("#"+metaphaseTargetValueId).removeClass("number");
	  }
	  if(targetType != null && targetType != ''){
		  $("#"+metaphaseTargetTypeId).find("option[value='"+targetType+"']").prop("selected", true);
	  }else{
		  $("#"+metaphaseTargetTypeId).find("option[value='']").prop("selected", true);
	  }
	 
  }
  function targetNameChange(targetName1,targetName2){
	  var targetName = $("#"+targetName1).val();
	  if(targetName != null ){
		  $("#"+targetName2).val(targetName)
	  }
  }
</script>
</body>
</html>