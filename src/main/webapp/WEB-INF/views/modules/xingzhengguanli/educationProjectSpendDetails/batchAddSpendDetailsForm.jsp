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
			<form:form id="inputForm" modelAttribute="educationProjectSpendDetails" action="${ctx}/xingzhengguanli/educationProjectSpendDetails/batchsave" method="post" class="form-horizontal form-information pad form-lie">
				
				<input type="hidden" name="user.id" value="${educationProjectSpendDetails.user.id}">
				<input type="hidden" id="addDetails" name="addDetails" value="">
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
					
					<div class="col-sm-10">
						
						<table class="ui celled table_semantic" id="educationProjectSpendDetailsListTable">
							<thead>
								<tr>
									<th>项目活动</th>
									<th>对项目活动的描述</th>
									<th>子活动</th>
									<th>对子活动的描述</th>
									<th>分项支出</th>
									<th>数量/频率</th>
									<th>支出计划（万元）</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${descriptionList}" var="item" varStatus="st">
									<tr class="inputSpendDetails">
										<td>
											<input type="hidden" name="id" >
											<input type="hidden" name="userid" value="${educationProjectSpendDetails.user.id}">
											<input type="hidden" name="projectDesid"  value="${item.id}">
											<textarea name="projectName" maxlength="255" class="form-control" rows="2">${item.projectName}</textarea>
										
										</td>
										<td>
											<textarea name="projectDescription" maxlength="500" class="form-control" rows="4">${item.projectDescription}</textarea>
										</td>
										<td>
											<textarea name="childProject" maxlength="255" class="form-control" rows="2">${item.childProject}</textarea>
										
										</td>
										<td>
											<textarea name="childProjectDescription" maxlength="255" class="form-control" rows="4">${item.childProjectDescription}</textarea>
										</td>
										<td>
											<input name="subItemExpenditure"  value="${item.subItemExpenditure}"  class="form-control ">
										</td>
										<td>
											<input name="numberFrequency"  value="${item.numberFrequency}"  class="form-control ">
										</td>
										<td>
											<input name="money"  value=""  class="form-control number">
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					
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
				getData();
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
  function getData(){
	  var array = [];
	  var year = $("#year").val();
	  $(".inputSpendDetails").each(function(){
		  
		  var id = $(this).find("input[name='id']").val();
		 // var year = $(this).find("input[name='yearnow']").val();
		  var userid = $(this).find("input[name='userid']").val();
		  var projectDesid = $(this).find("input[name='projectDesid']").val();
		  var projectName = $(this).find("textarea[name='projectName']").val();
		  var projectDescription = $(this).find("textarea[name='projectDescription']").val();
		  var childProject = $(this).find("textarea[name='childProject']").val();
		  var childProjectDescription = $(this).find("textarea[name='childProjectDescription']").val();
		  var subItemExpenditure = $(this).find("input[name='subItemExpenditure']").val();
		  var numberFrequency = $(this).find("input[name='numberFrequency']").val();
		  var money = $(this).find("input[name='money']").val();
		  
		  var data = {
				  "id":id,
				  "year":year,
				  "userid":userid,
				  "projectDesid":projectDesid,
				  "projectName":projectName,
				  "projectDescription":projectDescription,
				  "childProject":childProject,
				  "childProjectDescription":childProjectDescription,
				  "subItemExpenditure":subItemExpenditure,
				  "numberFrequency":numberFrequency,
				  "money":money
		  };
		  array.push(data);
	  });
	  var arrayStr=JSON.stringify(array);
	  $("#addDetails").val(arrayStr);
  }
</script>
</body>
</html>