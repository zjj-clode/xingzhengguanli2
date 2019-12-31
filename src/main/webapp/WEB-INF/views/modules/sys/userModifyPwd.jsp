<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
    <section class="content-header">
      <h1>
        个人信息管理
        <small>基本信息和密码修改</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/sys/user">个人信息管理</a></li>
        <li class="active">信息修改</li>
      </ol>
    </section>
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="nav-tabs-custom">
            <ul class="nav nav-tabs">
              <li><a href=" ${ctx}/sys/user/info"><i class="fa fa-user"></i> 基本信息设置</a></li>
              <li class="active"><a href="#tab_1"  data-toggle="tab"><i class="fa fa-lock"></i> 修改密码</a></li>
              <li class="pull-right"><a href="#" class="text-muted"><i class="fa fa-gear"></i></a></li>
            </ul>
            <div class="tab-content">
              <div class="tab-pane active" id="tab_1">
				<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd" method="post" class="form-horizontal form-information">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>旧密码:</label>
						<div class="col-sm-3">
							<input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3" class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>新密码:</label>
						<div class="col-sm-3">
							<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>确认新密码:</label>
						<div class="col-sm-3">
							<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" class="form-control required" equalTo="#newPassword"/>
						</div>
					</div>
					<div class="form-group">
                        <label for="" class="col-sm-1 control-label"><i class="fa fa-info-circle"></i>注意事项：</label>
                        <div class="col-sm-8 text-yellow radio">
                          密码需要包含8-20个字符。<br>不能是纯数字
                        </div>
                      </div>
					<div class="form-group">
					    <div class="col-sm-offset-1 col-sm-11">
						<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
						</div>
					</div>
				</div>
				</form:form>
				</div>
				</div>
         </div>
     </div>
   </section>
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script>
  $(function () {
       $(".select2").select2();
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