﻿<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>系统登录-${fns:getConfig('productName')}</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="keywords" content="">
<meta name="description" content="">
<link rel="shortcut icon" href="${ctxStaticFront}/images/com/logo.ico" type="image/x-icon">
<link type="text/css" rel="stylesheet" href="${ctxStaticBaseLTE}/css/base.css" />
<link type="text/css" rel="stylesheet" href="${ctxStaticBaseLTE}/css/index.css" />
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/jBox/jBox.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/font-awesome/font-awesome.min.css">
<script type="text/javascript" src="${ctxStatic}/jquery/j1.js"></script>
<script type="text/javascript">
    if (self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0) {
        alert('未登录或登录超时。请重新登录，谢谢！');
        top.location = "${ctx}";
    }
</script>
<script type="text/javascript" src="${ctxStaticBaseLTE}/js/jquery.SuperSlide.2.1.1.js"></script>
<script src="${ctxStaticLTE}/plugins/jBox/jBox.min.js"></script>
<script src="${ctxStaticLTE}/plugins/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStaticLTE}/plugins/jquery-validation/1.11.1/jquery.validate.method.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStaticLTE}/common.js"></script>

<%-- 是否启用js加密 --%>
<c:set var="jsencryptEnable" value="${fns:jsencryptEnable()}" />
<c:if test="${jsencryptEnable}">
	<script src="${ctxStaticLTE}/plugins/jsencrypt/jsencrypt.min.js"></script>
</c:if>

<style>
.bg-yellow,
.callout.callout-warning,
.alert-warning,
.label-warning,
.modal-warning .modal-body {
    background-color: #febb52!important;
    color: #fff!important;
}  
.capslock{  
    /*padding:0 2px 2px 26px;*/  
    position:absolute;
    left: 0; 
    top: 41px;
    *top: 44px;
    /*margin: 0 0 0 60px;*/  
    width:150px;  
    height:30px;  
    line-height:30px;  
    display:none;  
    overflow:hidden;  
    z-index:999;  
    color:#124fed;  
    background: url(${ctxStaticBaseLTE}/images/caps.png) no-repeat;  
}
.sugg{
        position: relative;
        float: right;
        height: 32px;
        color: #848585;
        font-size: 14px;
        margin-top: 25px;
        line-height: 32px;
    }
    .sugg > a{
        height: 32px;
        line-height: 32px;
        display: inline-block;
        margin-left: 15px;
    }
    .icon_360{
        position: relative;
        top: 5px;
        margin-right: 3px;
        display: inline-block;
        width: 23px;
        height: 22px;
        background-image: url('${ctxStaticBaseLTE}/images/logo_360.png');
    }
    .icon_ie{
        position: relative;
        top: 5px;
        margin-right: 3px;
        display: inline-block;
        width: 22px;
        height: 23px;
        background-image: url('${ctxStaticBaseLTE}/images/logo_ie.png');
    }
    .icon_chrome{
        position: relative;
        top: 5px;
        margin-right: 3px;
        display: inline-block;
        width: 21px;
        height: 21px;
        background-image: url('${ctxStaticBaseLTE}/images/logo_chrome.png');
    }
    .icon_firefox{
        position: relative;
        top: 5px;
        margin-right: 3px;
        display: inline-block;
        width: 22px;
        height: 21px;
        background-image: url('${ctxStaticBaseLTE}/images/logo_firefox.png');
    }
    .verfy{
        position: relative;
    }
    .verfy .ver_code{
        position: absolute;
        left: auto!important;
        right: 9px!important;
        width: 75px!important;
        height: 29px!important;
        top: 7px!important;
    }
    .verfy input{
        width: 158px;
        padding-right: 92px;
    }
    .login .login-header{
        position: relative;
        overflow: visible;
    }
    .ie360 .hoverDown{
        position: absolute;
        top: 32px;
        right: 0px;
        opacity: 0;
        width: 600px;
        visibility: hidden;
    }
    .ie360 .hoverDown > div{
        position: relative;
        margin-top: 10px;
        border-radius: 6px;
        padding-bottom: 35px;
        background-color: #f1f2f3;
    }
    .ie360 .hoverDown img{
        width: 450px;
        margin-left: 75px;
        margin-top: 45px; 
    }
    .ie360:hover .hoverDown{
        opacity: 1;
        visibility: visible;
    }
    .ie360 .hoverDown .tip{
        margin-left: 50px;
        margin-right: 50px;
    }
    .ie360 .hoverDown .jiao{
        position: absolute;
        top: -8px;
        width: 0px;
        height: 0px;
        left: 320px;
        border-style: solid;
        border-width:0 10px 10px;
        border-color: transparent transparent #f1f2f3;
    }
</style> 
</head>
<body style="background: #fff;">
    <div class="login">
        <div class="login-header">
            <h1 class="login-logo"><a href="#"><%-- <img style="width: 100%;" src="${ctxStaticFront}/images/com/logoa.png"/> --%></a></h1>
            <a href="" class="headerIntro" style="font-size: 30px;left: 300px;line-height: 36px;top: 25px;width: 255px;">行政管理系统</a>
            <div class="sugg">
                                                 建议浏览器：
                <a class="ie360" href="javascript:void(0);" style="margin-left: 25px;">
                    <i class="icon_360"></i>360极速模式
                </a>
                <a target="_blank" href="https://support.microsoft.com/zh-cn/help/17621/internet-explorer-downloads">
                    <i class="icon_ie"></i>IE11
                </a>
                <a target="_blank" href="http://www.pc6.com/softview/SoftView_113062.html">
                    <i class="icon_chrome"></i>谷歌
                </a>
                <a target="_blank" href="http://www.firefox.com.cn/">
                    <i class="icon_firefox"></i>火狐
                </a>
            </div>
        </div>
        <div class="login-banner" style="background:url('${ctxStaticFront}/images/com/banner.jpg') no-repeat center center;">
            <div class="loginBannerMid">
                <div class="loginBlock">
                     <div class="loginFunc" style="background-color: white;">
                         <div class="loginFuncNormal" style="width:100%;font-size: 20px;">密码登录</div>
                         <!-- <div class="loginFuncApp">二维码登录</div> -->
                     </div>
                     <form:form id="loginForm" action="${ctx}/login" class="loginForm" method="post">
                        <div class="account-number input-box">
                            <img src="${ctxStaticBaseLTE}/images/login_icon_03.png" alt=""  class="accimg"/>
                            <input id="username" name="username" value="${username}" type="text"  placeholder="学号/工号/邮箱" class="username required"/>
                        </div>
                        <div class="account-number input-box">
                            <img src="${ctxStaticBaseLTE}/images/login_icon_07.png" alt="" />
                            <input type="password"  placeholder="密码"  class="password required" id="password" name="password" autocomplete="off" style="overflow: hidden;"/>
                        </div>
                        <div class="input-box" id="validateDiv">
                            <div class="validate i-con">
                                <div id="mpanel4"></div>
                            </div>
                            <input type="hidden" id="isvalidate" name="isvalidate" value="0" />
                        </div>
                        <div class="loginBtnBox">
                            <span id="sub" class="submit" onclick="$('#loginForm').submit();return false;" style="width: 100%; background: url('${ctxStaticBaseLTE}/images/btn_v3_1.png') 0 0px no-repeat;height: 44px">登录</span>
                        </div>
                    </form:form>
                    <div class="appLogin" style="display: none;">
                        <h3>功能暂未开通<br />敬请期待</h3>
                        <img src="${ctxStaticBaseLTE}/images/smGirl_03.jpg" alt="" />
                        <p>更多内容请关注<a href="javascript:void(0);">${fns:getConfig('copyrightUnit')}</a>微信公众号</p>
                    </div>
                </div>              
            </div>
        </div>
        <div class="loginFooter">
            <div class="loginFootMid">
                <div class="footLeft">
                    <%-- <img src="${ctxStaticFront}/basic/images/com/mana_code.png" alt="" /> --%>
                </div>
                <div class="footRight">
                    <p class="links">
                       <a href="${fns:getSysConfig('custmor.site')}" target="_blank">${fns:getSysConfig('copyrightUnit')}官网</a>
                       
                    </p>
                    
                    <p class="schoolAdd">
				              联系我们:${fns:getSysConfig('contact_email')}   &nbsp; &nbsp; 地址：${fns:getSysConfig('contact_address')}     
                    </p>
                    <p class="version">版权所有©${fns:getSysConfig('copyrightUnit')}&nbsp; &nbsp;  技术支持：<a href="" class="support">北京云智小橙科技有限公司</a></p>
                </div>
            </div>
        </div>
    </div>

<link rel="stylesheet" type="text/css" href="${ctxStatic}/verify/css/verify.css">
    <script type="text/javascript" src="${ctxStatic}/verify/js/verify.js"></script>
<script type="text/javascript">

        $('#mpanel4').slideVerify({
            mode : 'pop', //pop,fixed
            initUrl : '${ctxf}/slide/init',
            imgUrl : '${ctxf}/slide/backImg',
            slideUrl : '${ctxf}/slide/slideImg',
            checkUrl : '${ctxf}/slide/check',
            imgSize : {
                width : '250px',
                height : '200px'
            },
            blockSize : {
                width : '50px',
                height : '50px'
            },
            barSize : {
                width : '250px',
                height : '40px'
            },
            ready : function() {},
            success : function() {
                $('.isvalidateDiv').hide();
                $('#isvalidate').val('1');
                //
                $("#sub").click();
            },
            error : function() {
                //$('.validatemessage').html('验证码错误');
                //$('.isvalidateDiv').show();
                $.jBox.tip('验证码错误');
                $('#isvalidate').val('0');
            }
        });
        
        
        $(function() {
        	var loginHeight = $('.login').height();
        	if(window.innerHeight>loginHeight){
        	    var paddingTop = (window.innerHeight-loginHeight)/2;
        	    $('.login').css('paddingTop',paddingTop)
        	}else{
        	    $('.login').css({'paddingTop':'60px','paddingBottom':'60px'});
        	}
        	//登录框选项卡
        	$('.loginFuncNormal').mouseover(function(){
        	    $('.loginBlock').css('background','url(${ctxStaticBaseLTE}/images/loginRightBg.png) no-repeat center center')
        	    $('.appLogin').hide();
        	    $('.loginForm').show();             
        	})
        	$('.loginFuncApp').mouseover(function(){
        	    $('.loginBlock').css('background','url(${ctxStaticBaseLTE}/images/loginLeftBg.png) no-repeat center center')
        	    $('.appLogin').show();
        	    $('.loginForm').hide();             
        	})
        	
            //<c:if test="${not empty message}">
            $.jBox.tip('<i class="fa fa-exclamation-circle"></i>${message}');
            //</c:if>
    
    
            $("#loginForm").validate({
                onfocusout:function(ele){
                    var name = $(ele).attr('name');
                    if( name === 'username' || name === 'password' ){
                        $('.jbox-tip').hide();
                        $(ele).valid();
                    }
                },
                
                onkeyup:function(ele){
                    var name = $(ele).attr('name');
                    if( name === 'username' || name === 'password' ){
                        $('.jbox-tip').hide();
                        $(ele).valid();
                    }
                },
                messages : {
                    username : {
                        required : "请填写用户名."
                    },
                    password : {
                        required : "请填写密码."
                    }
                },
    
                submitHandler : function(form) {
                    if ($('#isvalidate').val() == '0') {
                        $.jBox.tip('请拖动滑块进行验证');
                        return false;
                    }
                    loading("登录中，请稍后。。。");
                    
                    <%-- js 加密 --%>
                    <c:if test="${jsencryptEnable}">
                    var jsEncrypt = new JSEncrypt();
                    jsEncrypt.setPublicKey("${fns:getSysConfig('sys.login.encrypt.publicKey')}");
                    $("#password").val(jsEncrypt.encrypt($("#password").val()));
                    </c:if>
                    
                    form.submit();
                },
                errorPlacement : function(error, element) {
                    $('.jbox-tip').hide();
                    $.jBox.tip(error.text());
                }
            });
    
            //
            $("input").keydown(function(e) {
                var event = e || window.event;
                if (event.keyCode == 13) {
                    $("#sub").click();
                }
            });
            
            //
            <%--
            needValidateCode(${isValidateCodeLogin});
            $("#username").blur(function() {
                var u = $("#username").val();
                if($.trim(u).length > 3){
                    $.get("${pageContext.request.contextPath}/login/check", {username:u,t:new Date().getTime()},function(json){
                        needValidateCode(json.needValidateCode);//是否显示验证码输入框
                        if(json.isLocked == true){//是否账号被锁定
                            $.jBox.tip('尝试次数过多，账号被锁定${fns:getSysConfigWithDefaultValue("sys.login.lockAccount.lock_minutes","5")}分钟');
                        }
                    });
                }   
            }).blur(); 
            --%>
        })
        function needValidateCode(flag) {
            if (flag) {
                $("#validateDiv").show();
                $('#isvalidate').val('0');
            } else {
                $("#validateDiv").hide();
                $('#isvalidate').val('1');
            }
        }
    
    
        //解决ie的placeholder
        if ((navigator.appName == "Microsoft Internet Explorer") && (document.documentMode < 10 || document.documentMode == undefined)) {
            var $placeholder = $("input[placeholder]");
            for (var i = 0; i < $placeholder.length; i++) {
                if ($placeholder.eq(i).attr("type") == "password") {
                    $placeholder.eq(i).siblings("label").text($placeholder.eq(i).attr("placeholder")).show()
                } else {
                    $placeholder.eq(i).val($placeholder.eq(i).attr("placeholder")).css({
                        "color" : "#ccc"
                    })
                }
            }
            $placeholder.focus(function() {
                if ($(this).attr("type") == "password") {
                    $(this).siblings("label").hide()
                } else {
                    if ($(this).val() == $(this).attr("placeholder")) {
                        $(this).val("").css({
                            "color" : "#333"
                        })
                    }
                }
            }).blur(function() {
                if ($(this).attr("type") == "password") {
                    if ($(this).val() == "") {
                        $(this).siblings("label").text($(this).attr("placeholder")).show()
                    }
                } else {
                    if ($(this).val() == "") {
                        $(this).val($(this).attr("placeholder")).css({
                            "color" : "#ccc"
                        })
                    }
                }
            });
        }
        
        function capitalTip(id){
            if (!!window.ActiveXObject || "ActiveXObject" in window){
                return false;
            }
            $('#' + id).after('<div class="capslock" id="capital_password"><span></span></div>');
            var capital = false; //聚焦初始化，防止刚聚焦时点击Caps按键提示信息显隐错误
            
            // 获取大写提示的标签，并提供大写提示显示隐藏的调用接口
            var capitalTip = {
                $elem: $('#capital_'+id),
                toggle: function (s) {
                    if(s === 'none'){
                        this.$elem.hide();
                    }else if(s === 'block'){
                        this.$elem.show();
                    }else if(this.$elem.is(':hidden')){
                        this.$elem.show();
                    }else{
                        this.$elem.hide();
                       }
                }
            }
            $('#' + id).on('keydown.caps',function(e){
                if (e.keyCode === 20 && capital) { // 点击Caps大写提示显隐切换
                    capitalTip.toggle();
                }
            }).on('focus.caps',function(){capital = false}).on('keypress.caps',function(e){capsLock(e)}).on('blur.caps',function(e){
                
                //输入框失去焦点，提示隐藏
                capitalTip.toggle('none');
            });
            function capsLock(e){
                var keyCode = e.keyCode || e.which;// 按键的keyCode
                var isShift = e.shiftKey || keyCode === 16 || false;// shift键是否按住
                if(keyCode === 9){
                    capitalTip.toggle('none');
                }else{
                  //指定位置的字符的 Unicode 编码 , 通过与shift键对于的keycode，就可以判断capslock是否开启了
                  // 90 Caps Lock 打开，且没有按住shift键
                  if (((keyCode >= 65 && keyCode <= 90) && !isShift) || ((keyCode >= 97 && keyCode <= 122) && isShift)) {
                      // 122 Caps Lock打开，且按住shift键
                      capitalTip.toggle('block'); // 大写开启时弹出提示框
                      capital = true;
                  } else {
                      capitalTip.toggle('none');
                      capital = true;
                  }
                }
            }
        }
        capitalTip('password');
    </script>
</body>
</html>