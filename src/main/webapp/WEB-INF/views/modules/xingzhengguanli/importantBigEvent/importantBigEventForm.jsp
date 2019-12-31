<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>三重一大事项管理<small>三重一大事项信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/xingzhengguanli/importantBigEvent/">三重一大事项管理</a></li>
        <li class="active">三重一大事项<shiro:hasPermission name="xingzhengguanli:importantBigEvent:edit">${not empty importantBigEvent.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="xingzhengguanli:importantBigEvent:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="importantBigEvent" action="${ctx}/xingzhengguanli/importantBigEvent/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>科室：</label>
					
					<div class="col-sm-3">
						<form:input path="keshi" htmlEscape="false" maxlength="255" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>事项日期：</label>
					
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input name="startDate" type="text" style="width: 230px" maxlength="20" class="form-control required"
							value="<fmt:formatDate value="${importantBigEvent.startDate}" pattern="yyyy-MM-dd HH:mm"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"/>
				        </div>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					经费号：</label>
					
					<div class="col-sm-3">
						<form:input path="fundNumber" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>事项名称：</label>
					
					<div class="col-sm-3">
						<form:input path="itemName" htmlEscape="false" maxlength="255" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					金额（元）：</label>
					
					<div class="col-sm-3">
						<form:input path="money" htmlEscape="false" class="form-control  number"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>会议时间：</label>
					
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input id="meetingDate" name="meetingDate" type="text" style="width: 230px" maxlength="20" class="form-control required"
							value="<fmt:formatDate value="${importantBigEvent.meetingDate}" pattern="yyyy-MM-dd HH:mm"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});" 
							onchange="changeMeetingDate()"/>
				        </div>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					会议地点：</label>
					
					<div class="col-sm-3">
						<form:input path="meeingPlace" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					主持人：</label>
					
					<div class="col-sm-3">
						<form:input path="meeingHost" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					出席人员：</label>
					
					<div class="col-sm-3">
						<form:input path="meeingMember" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					记录人：</label>
					
					<div class="col-sm-3">
						<form:input path="meeingNoteTaker" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>会议主题：</label>
					
					<div class="col-sm-3">
						<form:input path="meeingTheme" htmlEscape="false" maxlength="255" class="form-control required"/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					<span class="text-red fm">*</span>主要内容：</label>
					
					<div class="col-sm-3">
						<form:textarea path="meeingContent" htmlEscape="false" rows="4" maxlength="500" class="form-control required"/>
					
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
					<shiro:hasPermission name="xingzhengguanli:importantBigEvent:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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
  function changeMeetingDate(){
	  var meetingDate = $("#meetingDate").val();
	  var eventId = $("#id").val();
	  if(meetingDate != null && meetingDate != ''){
		  $.ajax({
	          data: {"meetingDate":meetingDate,"eventId":eventId},
	          url :"${ctx}/xingzhengguanli/importantBigEvent/checkMeetingTime",
	          type:"POST",
	          dataType:"JSON",
	          success:function(data){
	        	  
	        	  if(data.re == 'error'){
	        		  $("#meetingDate").val('');
	        		  var message = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>'+data.message+'</p>';
	        		  top.$.jBox.confirm(message,"系统提示",function(v,h,f){
	        			},{buttonsFocus:1});
	        	  }
	        	  
	          }
		  });
	  }
  }
</script>
</body>
</html>