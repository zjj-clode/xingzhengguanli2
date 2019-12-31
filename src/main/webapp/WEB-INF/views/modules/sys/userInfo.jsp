<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
    <link rel="stylesheet" href="${ctxStaticLTE }/plugins/ima_up/css/amazeui.min.css">
	<link rel="stylesheet" href="${ctxStaticLTE }/plugins/ima_up/css/amazeui.cropper.css">
	<link rel="stylesheet" href="${ctxStaticLTE }/plugins/ima_up/css/custom_up_img.css">
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
              <li class="active"><a href="#tab_1" data-toggle="tab"><i class="fa fa-user"></i> 基本信息设置</a></li>
              <li><a href="${ctx}/sys/user/modifyPwd"><i class="fa fa-lock"></i> 修改密码</a></li>
              <li class="pull-right"><a href="#" class="text-muted"><i class="fa fa-gear"></i></a></li>
            </ul>
            <div class="tab-content">
              <div class="tab-pane active" id="tab_1">
				<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/info" method="post" class="form-horizontal form-information">
					<sys:message content="${message}"/>
					<form:hidden path="id"/>
					<div class="form-group">
						<label class="col-sm-1 control-label">头像:</label>
						<!-- 头像上传start -->
						<div class="pull-left" id="up-img-touch">
           					<img class="profile-user-img img-responsive img-circle"  alt="点击图片上传" src="${pageContext.request.contextPath}${user.photo}" alt="User profile picture" data-am-popover="{content: '点击上传', trigger: 'hover focus'}">
				       </div>
				       
				       <!--图片上传框-->
				    	<div class="am-modal am-modal-no-btn up-frame-bj" tabindex="-1" id="doc-modal-1">
						  <div class="am-modal-dialog up-frame-parent up-frame-radius">
						    <div class="am-modal-hd up-frame-header">
						       <label>修改头像</label>
						      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
						    </div>
						    <div class="am-modal-bd  up-frame-body">
						      <div class="am-g am-fl">
						      	<div class="am-form-group am-form-file">
							      <div class="am-fl">
							        <button type="button" class="am-btn am-btn-default am-btn-sm">
							          <i class="am-icon-cloud-upload"></i> 选择要上传的文件</button>
							      </div>
							      <input type="file" id="inputImage">
							   	</div>
						      </div>
						      <div class="am-g am-fl" >
						      	<div class="up-pre-before up-frame-radius">
						      		<img alt="" src="" id="image" >
						      	</div>
						      	<div class="up-pre-after up-frame-radius">
						      	</div>
						      </div>
						      <div class="am-g am-fl">
				   				<div class="up-control-btns">
				    				<span class="am-icon-rotate-left"  onclick="rotateimgleft()"></span>
				    				<span class="am-icon-rotate-right" onClick="rotateimgright()"></span>
				    				<span class="am-icon-check" id="up-btn-ok" imgpath="${pageContext.request.contextPath}" resumeinfoId="${user.id }" url="${ctx}/sys/user/uploadphoto"></span>
				   				</div>
					    	  </div>
						    </div>
						  </div>
						</div>
						<!--加载框-->
				    	<div class="am-modal am-modal-loading am-modal-no-btn" tabindex="-1" id="my-modal-loading">
						  <div class="am-modal-dialog">
						    <div class="am-modal-hd">正在上传...</div>
						    <div class="am-modal-bd">
						      <span class="am-icon-spinner am-icon-spin"></span>
						    </div>
						  </div>
						</div>
						<!--警告框-->
						<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">
						  <div class="am-modal-dialog">
						    <div class="am-modal-hd">信息</div>
						    <div class="am-modal-bd"  id="alert_content">
						              成功了
						    </div>
						    <div class="am-modal-footer">
						      <span class="am-modal-btn">确定</span>
						    </div>
						  </div>
						</div>
						<!-- 头像上传end -->
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">归属机构:</label>
						<div class="col-sm-3 radio">${user.company.name}</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">归属部门:</label>
						<div class="col-sm-3 radio">${user.office.name}</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>姓名:</label>
						<div class="col-sm-3">
							<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required" readonly="true"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">邮箱:</label>
						<div class="col-sm-3">
							<form:input path="email" htmlEscape="false" maxlength="50" class="form-control email"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">电话:</label>
						<div class="col-sm-3">
							<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">手机:</label>
						<div class="col-sm-3">
							<form:input path="mobile" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">备注:</label>
						<div class="col-sm-3">
							<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">用户类型:</label>
						<div class="col-sm-3 radio">${fns:getDictLabel(user.userType, 'sys_user_type', '无')}</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">用户角色:</label>
						<div class="col-sm-3 radio">${user.roleNames}</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">上次登录:</label>
						<div class="col-sm-3 radio">IP: ${user.oldLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.oldLoginDate}" type="both" dateStyle="full"/></div>
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

<script src="${ctxStaticLTE }/plugins/ima_up/js/amazeui.min.js" charset="utf-8"></script>
<script src="${ctxStaticLTE }/plugins/ima_up/js/cropper.min.js" charset="utf-8"></script>
<script src="${ctxStaticLTE }/plugins/ima_up/js/custom_up_img.js" charset="utf-8"></script>
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