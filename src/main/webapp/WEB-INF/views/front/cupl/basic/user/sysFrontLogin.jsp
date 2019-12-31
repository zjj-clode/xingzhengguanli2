<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Pragma",content="no-cache">

	<meta name="viewport" content="width=device-width, height=device-height, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">
	<title>登录</title>
	<meta name="keywords" content="">
	<meta name="description" content="">
	<meta name="applicable-device" content="mobile">
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<link rel="stylesheet" href="${ctxStaticMobileFront}/css/base.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStaticMobileFront}/css/style.min.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStaticMobileFront}/plugins/Swiper-3.4.2/css/swiper.min.css" />
    <link rel="stylesheet" href="${ctxStaticMobileFront}/plugins/pushbutton/pushbutton.css">
	<script type="text/javascript" src="${ctxStaticMobileFront}/js/rem.js"></script>
	<script src="${ctxStaticMobileFront}/js/zepto.min.js"></script>
	<script src="${ctxStatic}/jquery/j1.js"></script>
    <script src="${ctxStaticMobileFront}/plugins/Swiper-3.4.2/js/swiper.min.js"></script>
    <script src="${ctxStaticMobileFront}/plugins/pushbutton/pushbutton.min.js"></script>
    <script src="${ctxStatic}/lte/plugins/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/lte/plugins/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<link rel="stylesheet" href="${ctxStaticFront}/css/password.min.css">
	<style type="text/css">
    .pa-body, .pa-body ,.error {
    position: relative;
    box-sizing: border-box;
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    color: red;
}
    </style>
</head>
<body>
	<div class="login">
		<section class="pa-body">
			<form:form action="${ctx}/loginf" method="post" id="loginForm">
				<h2 class="tit"><img src="${ctxStaticFront}/images/com/logoa.png"/></h2>
				<div class="flexTab enNum">
					<div class="icon_con"><i class="icon_brief_case"></i></div>
					<input type="text" id="username" name="username" value="${username}" placeholder="请输入工号/学号" class="required" />
				</div>
				<div class="flexTab enNum">
					<div class="icon_con"><i class="icon_user"></i></div>
					<input type="password" id="password" AUTOCOMPLETE="off" name="password" placeholder="请输入密码" class="required" />
				</div>
				<div class="flexTab enNum">
					<div class="icon_con"><i class="icon_write_file"></i></div>
					<!--<sys:validateCode name="validateCode" inputCssStyle="margin-left: 10px;" inputMaxLength="4" refreshText=" "  imageCssStyle="vertical-align:middle; width: 98px;margin-left: 204px;position: absolute;" />-->
					<sys:validateCode name="validateCode" inputCssStyle="width:60%;" showButton="false" inputMaxLength="4" imageCssStyle="position:relative;height: 40px;" />
				</div>
				
				<div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}" style="margin-right: 5px;width: auto;text-align: center;padding-bottom:5px; margin-left: 2px;margin-top:15px;min-height: 24px;<%-- ${empty message ? 'display: none;' : ''} --%>">
				<label id="loginError" class="error" style="">${empty message ? '&nbsp;&nbsp;&nbsp;&nbsp;' : message}</label>
				</div>
				<button class="submit" type="submit">
					登录
				</button>
				
				<%-- <div class="flexTab otherFun">
					<a href="${ctxf}/sys/user/findPassword" class="b-fo">忘记密码</a>
					<a href="javascript:void(0);" onclick="window.location='${ctxf}/sys/user/register';return false;"class="b-re">立即注册</a>
				</div> --%>
			</form:form>
			<!-- <div class="share">
				<div class="tit-s">其他登录方式</div>
				<div class="con flexTab">
					<div class="item"></div>
					<div class="item">
						<img src="${ctxStaticMobileFront}/images/qq0.png">
						<div>QQ</div>
					</div>
					<div class="item">
						<img src="${ctxStaticMobileFront}/images/wx0.png">
						<div>微信</div>
					</div>
					<div class="item"></div>
				</div>
			</div> -->
		</section>
		<script type="text/javascript">
			var $ins=document.querySelectorAll('input');
			for(var i=0,len=$ins.length;i<len;i++){
				$ins[i].onfocus=function(){
					this.parentNode.className+=' active';
				}
				$ins[i].onblur=function(){
					this.parentNode.className=this.parentNode.className.replace(' active','');
				}
			};
				$(document).ready(function() {
					$("#loginForm").validate({
						rules : {
							validateCode : {
								remote : "${pageContext.request.contextPath}/servlet/validateCodeServlet"
							}
						},
						messages : {
							username : {
								required : "请填写账号。"
							},
							password : {
								required : "请填写密码。"
							},
							validateCode : {
								remote : "验证码不正确。",
								required : "请填写验证码。"
							}
						},
						submitHandler : function(form) {
							form.submit();
						},
						errorLabelContainer : "#messageBox",
						errorPlacement : function(error, element) {
						}
					});

					//
					$("input").keydown(function(e) {
						var event = e || window.event;
						if (event.keyCode == 13) {
							$("input[type='submit']").click();
						}
					});
				});
		</script>

	</div>
</body>
</html>