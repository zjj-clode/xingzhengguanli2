<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>访问日志功能管理<small>访问日志功能信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/sys/guestAccess/">访问日志功能管理</a></li>
        <li class="active">访问日志功能<shiro:hasPermission name="sys:guestAccess:edit">${not empty guestAccess.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:guestAccess:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="guestAccess" action="${ctx}/sys/guestAccess/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					访问结束时间：</label>
					
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input name="logoutDateTime" type="text" style="width: 230px" maxlength="20" class="form-control "
							value="<fmt:formatDate value="${guestAccess.logoutDateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				        </div>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					时长：</label>
					
					<div class="col-sm-3">
						<form:input path="duration" htmlEscape="false" maxlength="10" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					最后操作时间：</label>
					
					<div class="col-sm-3">
						<div class="input-group date">
				          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
				          <input name="lastAccessTime" type="text" style="width: 230px" maxlength="20" class="form-control "
							value="<fmt:formatDate value="${guestAccess.lastAccessTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				        </div>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					访问地址：</label>
					
					<div class="col-sm-3">
						<form:input path="url" htmlEscape="false" maxlength="200" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					IP地址：</label>
					
					<div class="col-sm-3">
						<form:input path="ip" htmlEscape="false" maxlength="40" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					国家：</label>
					
					<div class="col-sm-3">
						<form:input path="country" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					省：</label>
					
					<div class="col-sm-3">
						<form:input path="region" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					市：</label>
					
					<div class="col-sm-3">
						<form:input path="city" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					地区：</label>
					
					<div class="col-sm-3">
						<form:input path="area" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					浏览器：</label>
					
					<div class="col-sm-3">
						<form:input path="browser" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					浏览器版本：</label>
					
					<div class="col-sm-3">
						<form:input path="browserVersion" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					操作系统：</label>
					
					<div class="col-sm-3">
						<form:input path="os" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					访问设备：</label>
					
					<div class="col-sm-3">
						<form:input path="deviceType" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					系统制造商：</label>
					
					<div class="col-sm-3">
						<form:input path="manufacturer" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					访问信息：</label>
					
					<div class="col-sm-3">
						<form:input path="headerInfo" htmlEscape="false" maxlength="800" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					状态：</label>
					
					<div class="col-sm-3">
						<form:select path="online" class="form-control ">
							<form:option value="" label="=== 请选择是否在线 ==="/>
							<form:options items="${fns:getDictList('is_online')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					账号：</label>
					
					<div class="col-sm-3">
						<form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					姓名：</label>
					
					<div class="col-sm-3">
						<form:input path="name" htmlEscape="false" maxlength="50" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					单位id：</label>
					
					<div class="col-sm-3">
						<form:input path="depId" htmlEscape="false" maxlength="64" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					单位名称：</label>
					
					<div class="col-sm-3">
						<form:input path="depName" htmlEscape="false" maxlength="50" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					客户端：</label>
					
					<div class="col-sm-3">
						<form:input path="platform" htmlEscape="false" maxlength="20" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					备注：</label>
					
					<div class="col-sm-3">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="sys:guestAccess:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
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