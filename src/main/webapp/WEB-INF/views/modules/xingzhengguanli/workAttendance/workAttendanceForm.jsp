<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>工作考勤管理<small>工作考勤信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/xingzhengguanli/workAttendance/">工作考勤管理</a></li>
        <li class="active">工作考勤<shiro:hasPermission name="xingzhengguanli:workAttendance:edit">${not empty workAttendance.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="xingzhengguanli:workAttendance:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="workAttendance" action="${ctx}/xingzhengguanli/workAttendance/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>人员：</label>
					<div class="col-sm-3">
						<form:select path="overtimeUser.id" class="form-control select2 required">
							<form:option value="" label="--请选择--"/>
							<form:options items="${userList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<%-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					科室：</label>
					
					<div class="col-sm-3">
						<form:input path="keshi" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					工号：</label>
					
					<div class="col-sm-3">
						<form:input path="jobNumber" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					姓名：</label>
					
					<div class="col-sm-3">
						<form:input path="name" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div> --%>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>加班天数：</label>
					<div class="col-sm-3">
						<input name="overtimeDays" id="overtimeDays" maxlength="11" class="form-control required digits"/>
					
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>时间：</label>
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input name="yearMon" type="text" style="width: 230px" maxlength="20" class="form-control required"
							value="<fmt:formatDate value="${workAttendance.yearMon}" pattern="yyyy-MM"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
				        </div>
					
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
					<shiro:hasPermission name="xingzhengguanli:workAttendance:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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
				//form.submit();
				$.ajax({
			          data: $("#inputForm").serialize(),
			          url :"${ctx}/xingzhengguanli/workAttendance/checkDaysSave",
			          type:"POST",
			          dataType:"JSON",
			          success:function(data){
			        	  
			        	  if(data.re == 'error'){
			        		  
			        		  var message = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>'+data.message+'</p>';
			        		  top.$.jBox.confirm(message,"系统提示",function(v,h,f){
			        			},{buttonsFocus:1});
			        		  closeLoading();
			        	  }else{
			        		  top.$.jBox.tip("保存成功");
			        		  window.location.href = '${ctx}/xingzhengguanli/workAttendance';
			        	  }
			        	  
			          }
				  });
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