<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!doctype html>
<html lang="en">
<head>
<title><sitemesh:title />--${fns:getSysConfig('copyrightUnit')}招生宣传服务系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="keywords" content="">
<meta name="description" content="">
<meta name="viewport" content="width=1440">
<meta http-equiv="X-UA-Compatible" content="edge" />
<link rel="shortcut icon" href="${ctxStaticFront}/images/com/logo.ico" type="image/x-icon">
<script type="text/javascript" src="${ctxStaticFront}/js/j3.js"></script>
<!--[if lt IE 9]>
	<script type="text/javascript" src="${ctxStaticFront}/js/html5shiv.js"></script>
	<script type="text/javascript" src="${ctxStaticFront}/js/j1.js"></script>
<![endif]-->
<script type="text/javascript" src="${ctxStaticFront}/js/util.js"></script>
<script type="text/javascript" src="${ctxStaticFront}/js/common.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctxStaticFront}/js/jquery-validate/jquery.validate.css"/>
	<link rel="stylesheet" href="${ctxStaticFront}/css/jquery-ui.min.css">
	<link rel="stylesheet" type="text/css" href="${ctxStaticFront}/plugins/select2/select2.min.css"/>
<link rel="stylesheet" type="text/css" href="${ctxStaticFront}/plugins/jQuery-Timepicker/jquery-ui-timepicker-addon.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxStaticFront}/css/hfmx.min.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxStaticFront}/css/base.min.css"/>
	<script type="text/javascript" src="${ctxStaticFront}/js/swiper.js"></script>
	<script src="${ctxStaticFront}/js/jquery-ui.min.js"></script>
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/css/webuploader.css">
	<script type="text/javascript" src="${ctxStatic}/webuploader/js/webuploader.min.js"></script>
<style type="text/css">

	.comp_upfile{
			font-size: 13px;
		}
		.comp_upfile #video{
			display: none;
		}
		.webuploader-pick{
			color: #fff;
			border: 1px solid #273eb0;
			background-color: #273eb0;
			padding: 3px 15px;
			font-size: 12px;
		}
		.webuploader-pick + div{
			width:100%!important;
			height:100%!important;
		}
		.ctlBtn{
			height: 32px;
			margin-top: 15px;
			padding: 0px 15px!important;
			line-height: 32px;
			border-radius: 4px!important;
			border: 1px solid #ddd;
			font-size: 13px;
		}
		.text-muted p{
			font-size: 12px;
			color: #999;
			word-break: break-all;
			word-wrap: break-word;
			white-space: normal;
			text-align: justify;
			line-height: 145%;
		}
		.comp_textarea > .label{
			white-space:pre-line;
		}
		.comp_textarea textarea{
	    	white-space:pre-line;
		}
		.confirm_box .entity {
		    min-height: 250px;
		    height: auto;
		}
		.confirm_box .btns {
		   margin-bottom: 10px;
		}
</style>
<sitemesh:head />
</head>
<body>
	<c:choose>
		<c:when test="${fns:getUser().userType eq '2'}"><!-- 老师 -->
			<div id="uploadMaterialForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>材料信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						
						<form:form style="width: 600px;"  id="modifyUploadMaterialForm" action="${ctx}/teacher/apply/uploadMaterial" method="post" enctype="multipart/form-data">
							
							<input type="hidden" id="materialid" name="id" value=""/>
							<input type="hidden" id="schid" name="school.id" value="${xcTeacherSchool.id}"/>
							<input type="hidden" id="materialPlanId" name="plan.id" value="${plan.id}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>材料名称：</label>
										<input type="text" class="required" id="stuffname" name="stuffname" value=""/>
									</div>
								</div>
							</div>
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_upfile">
										<label class="label"><span class="req">*</span>选择文件：</label>
										<input type="hidden" id="materialstuffurl" name="stuffurl" value="" />
										<cms:playVideo videoUrl="" style="width:200px;height:100px;" />
										<div id="uploader" class="wu-example" style="margin-top:10px;">
											<div id="materialStuffurlThelist" class="uploader-list"></div>
											<div class="btns" style="">
												<div id="picker" style="width:150px;"></div>
											</div>
										</div>
										<div class="text-muted" style="clear:left;margin-top:10px;">
											<p class="">提示： 1、上传文件类型为：${fns:getSysConfigWithDefaultValue("cms.movie.fileType", "")}；<br>
											2、上传文件大小不超过${fns:getSysConfigWithDefaultValue("cms.movie.fileSize", 500)}M。</p>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="button" id="materialStuffurlBtn" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#uploadMaterialForm .btn_cancle',function(){
						$("#uploadMaterialForm #modifyUploadMaterialForm").find("input").val("");
						$('#uploadMaterialForm').fadeOut();
					});
					$('body').on('click','#uploadMaterialForm .icon_close',function(){
						$("#uploadMaterialForm #modifyUploadMaterialForm").find("input").val("");
						$('#uploadMaterialForm').fadeOut();
					});
				</script>
			</div>
			<div class="wrapper">
				<div class="header">
					<a class="logo" href="#">
						<img src="${ctxStaticFront}/images/hfmx/logo.png"/>
					</a>
					<div class="right">
						<div class="funs">
							<a class="one" href="${ctx}/hf/apply/notify/self">
								 <i id="notifyNum" class="tip_num" style="display:none;"></i>
								消息通知
							</a> 
							
							<a class="two" href="${ctx}/teacher/apply/list">
								业务
							</a>
							<div class="user">
								<a href="#" class="u_con">
									<c:choose>
										<c:when test="${not empty fns:getUser() and not empty fns:getUser().photo}">
											<i class="stiker" style="background-image: url('${pageContext.request.contextPath}${fns:getUser().photo}?v=<%=Math.random()*100%>')"></i>
										</c:when>
										<c:otherwise>
											<i class="stiker"></i>
										</c:otherwise>
									</c:choose>
									
									<span>${fns:getUser().name}</span>
								</a>
								<div class="u_other">
									<ul>
										<li class="modify_pass">
											<a style="background-image: url(${ctxStaticFront}/images/hfmx/icon_menu_user.png);" href="${ctx}/hf/apply/modifyPersonInfo">个人中心</a>
										</li>
										<li class="modify_pass">
											<a href="${ctx}/hf/apply/modifyPasswordForm">修改密码</a>
										</li>
										<li class="quiz">
											<a href="javascript:void(0);" onclick="logout()">退出登录</a>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="body">
					<div class="l_l">
						<div class="m_user" onclick="window.location.href='${ctx}/hf/apply/modifyPersonInfo'">
							<c:choose>
								<c:when test="${not empty fns:getUser() and not empty fns:getUser().photo}">
									<div class="u_sticker" style="border-radius: 50%;background-image: url('${pageContext.request.contextPath}${fns:getUser().photo}?v=<%=Math.random()*100%>')"></div>
								</c:when>
								<c:otherwise>
									<div class="u_sticker" style="background-image: url('${ctxStaticFront}/images/hfmx/sticker.png')"></div>
								</c:otherwise>
							</c:choose>
							<div class="u_info">
								<div class="u_name">${fns:getUser().name}</div>
								<div class="u_profession">${fns:getUser().companyName}</div>
							</div>
						</div>
						<div class="menus">
							<ul>
								<li class="bm active">
									<a href="javascript:void(0);">
										宣讲计划报名<span class="icon_arrow"></span>
									</a>
									<div class="sub" style="height:90px;">
										<ul>
											<li <c:if test="${ename == 'xcTeacherPlan'}"> class="active"</c:if>>
												<a href="${ctx}/teacher/apply/list">计划列表</a>
											</li>
											
											<li <c:if test="${ename == 'schoolList'}">class="active"</c:if> >
												<a href="${ctx}/teacher/apply/schoolList">总结上传</a>
											</li>
										</ul>
									</div>
								</li>
								
							</ul>
							<script type="text/javascript">
								$(function(){
									var $active = $('.l_l .menus li.active');
									var $lis = $active.parents('li');
									if( $lis.length > 0 ){
										$.each($lis,function(){
											$(this).addClass('active');
											var h = $(this).find('.sub>ul').height();
											$(this).find('.sub').css( 'height' , h + 'px' );
										});
									}
									$('.l_l .menus li>a').filter(function(){
										return $(this).parents('li').eq(0).find('.sub').length > 0
									}).on('click',function(){
										var $li = $(this).parents('li').eq(0);
										if( $li.hasClass('active') ){
											closeActive();
										}else{
											closeActive();
											var $sub = $(this).parents('li').eq(0).find('.sub');
											if( $sub.length > 0 ){
												var h = $sub.children('ul').height();
												$sub.css( 'height' , h + 'px' );
											}
											$(this).parents('li').eq(0).addClass('active');
										};
										function closeActive(){
											var $lis = $('.menus').find('.active').filter(function(){
												return $(this).parents('.sub').length === 0;
											});
											$.each($lis,function(){
												var $sub = $(this).find('.sub');
												if( $sub.length > 0 ){
													$(this).removeClass('active');
													var $ul = $sub.children('ul');
													$sub.css( 'height' , '0px' );
												}else{
													$(this).removeClass('active');
												}
											});
										}
									});
								});
							</script>
							
							<div class="bottom_fun">
								<a href="${ctx}/hf/apply/modifyPasswordForm"><i class="icon_set"></i>修改密码</a>
								<a href="javascript:void(0);" onclick="logout()"><i class="icon_quiz"></i>退出登录</a>
							</div>
							
						</div>
					</div>
				
				  	<sitemesh:body/>
				  	
				</div>
				<div class="footer" style="padding: 20px 0px;">
					<div class="right">
						<div class="contact tc">
							<span>电话：${fns:getSysConfig('contact_phone')} </span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span>地址：${fns:getSysConfig('contact_address')} </span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span class="copy_right">版权所有：${fns:getSysConfig('copyrightUnit')} </span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span class="suport">技术支持：北京云智小橙科技有限公司</span>&nbsp;&nbsp;&nbsp;&nbsp;
						</div>
					</div>
				</div>
			</div>
			
			<div id="schoolModifyForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>修改学校信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						
						<form:form style="width: 600px;"  id="modifySchoolForm" action="" method="post">
							
							<input type="hidden" id="schoolid" name="id" value=""/>
							<input type="hidden" id="schoolPlanId" name="plan.id" value=""/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select_addr">
										<label class="label"><span class="req">*</span>请选择区域：</label>
										<div>
											<select class="select" id="schoolprovince" name="province.id"  onchange="provinceChange2()" >
												<option value="">-请选择省份、直辖市-</option>
												 <c:forEach items="${provinceAreaList}" var="provinceArea">
							                    	<option value="${provinceArea.id}">${provinceArea.name}</option>
							                    </c:forEach>
											</select>
										</div>
										<div>
											<select class="select" id="schoolcity" name="city.id" onchange="cityChange2()" >
												<option value="">-请选择地市-</option>
												<c:forEach items="${cityAreaList}" var="cityArea">
							                    	<option value="${cityArea.id}">${cityArea.name}</option>
							                    </c:forEach>
											</select>
										</div>
										<div>
											<select class="select" id="schoolcountry" name="country.id" onchange="countryChange2()" >
												<option value="">-请选择区县-</option>
												<c:forEach items="${countyAreaList}" var="countryArea">
							                    	<option value="${countryArea.id}" >${countryArea.name}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>学校：</label>
										<div class="schoolSelectModifyDiv">
											<select class="select" id="schoolModify" name="school.id"  onchange="schoolChange2()" >
												<option value="">-请选择-</option>
												 <c:forEach items="${highSchoolList}" var="highschool">
							                    	<option value="${highschool.id}">${highschool.schoolName}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="row mt_15" id="schoolModifyDiv" style="display:none;">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>具体学校名称：</label>
										<input type="text"  placeholder="请输入具体学校名称" class="" id="schoolNameModify" name="schoolName" value="" />
									</div>
								</div>
							</div>
							<div class="row mt_20 repeatSchoolModifyDiv" style="display:none;">
								<div class="chunk_12">
									<div class="comp_select_addr">
										<label class="label messageLabel" style="color:red;text-align: justify;"></label>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>中学联系人：</label>
										<input type="text" class="required" id="schoolModifyLxr" name="schoolLxr" value="" />
									</div>
								</div>
							</div>
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>中学联系人职务：</label>
										<input type="text" class="required" id="schoolModifyLxrzw" name="schoolLxrzw" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>中学联系人电话：</label>
										<input type="text" class="required" id="schoolModifyLxrphone" name="schoolLxrphone" value="" />
									</div>
								</div>
								
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
								<div class="comp_button">
									<button type="button" class="btn normal form_normal btn_cancle">
										取消
									</button>
									<button type="submit" class="btn normal form_theme ml_20">
										保存
									</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#schoolModifyForm .btn_cancle',function(){
						
						$('#schoolModifyForm').fadeOut();
					});
					$('body').on('click','#schoolModifyForm .icon_close',function(){
						
						$('#schoolModifyForm').fadeOut();
					});
				</script>
			</div>
			
			<div id="memberModifyForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>修改组员信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						
						<form:form style="width: 600px;"  id="modifyMemberForm" action="" method="post">
							
							<input type="hidden" id="memberid" name="id" value=""/>
							<input type="hidden" id="memberplanId" name="plan.id" value=""/>
							<input type="hidden" id="memberisLeader" name="isLeader" value=""/>
							<input type="hidden" id="memberisHead" name="isHead" value=""/>
							<input type="hidden" id="memberisJoin" name="isJoin" value=""/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>学号：</label>
										<input type="text" class="required" id="memberXh" name="gh" value="" <c:choose><c:when test="${xcTeacherPlan.checkResult == '1'}">readonly="readonly"</c:when><c:when test="${xcTeacherPlan.isSubmit == '1'}">readonly="readonly"</c:when><c:otherwise></c:otherwise></c:choose>/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>姓名：</label>
										<input type="text" class="required" id="memberXm" name="xm" value="" <c:choose><c:when test="${xcTeacherPlan.checkResult == '1'}">readonly="readonly"</c:when><c:when test="${xcTeacherPlan.isSubmit == '1'}">readonly="readonly"</c:when><c:otherwise></c:otherwise></c:choose>/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>手机号：</label>
										<input type="text" class="required mobile" id="membermobile" name="mobile" value="" <c:choose><c:when test="${xcTeacherPlan.checkResult == '1'}">readonly="readonly"</c:when><c:when test="${xcTeacherPlan.isSubmit == '1'}">readonly="readonly"</c:when><c:otherwise></c:otherwise></c:choose>/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>邮箱：</label>
										<input type="text" class="required email" id="memberemail" name="email" value="" <c:choose><c:when test="${xcTeacherPlan.checkResult == '1'}">readonly="readonly"</c:when><c:when test="${xcTeacherPlan.isSubmit == '1'}">readonly="readonly"</c:when><c:otherwise></c:otherwise></c:choose>/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#memberModifyForm .btn_cancle',function(){
						
						$('#memberModifyForm').fadeOut();
					});
					$('body').on('click','#memberModifyForm .icon_close',function(){
						
						$('#memberModifyForm').fadeOut();
					});
				</script>
			</div>
			
			<div id="applyModifyForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>申请修改</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						
						<form:form style="width: 600px;"  id="applyModify" action="" method="post">
							
							<input type="hidden" id="applyGroupModifyId" name="id" value="${xcGroup.id}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_textarea">
										<label class="label"><span class="req">*</span>申请修改原因：</label>
										<textarea  class="required"  id="applyModifyReason" name="applyModifyReason">${xcGroup.applyModifyReason}</textarea>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#applyModifyForm .btn_cancle',function(){
						
						$('#applyModifyForm').fadeOut();
					});
					$('body').on('click','#applyModifyForm .icon_close',function(){
						
						$('#applyModifyForm').fadeOut();
					});
				</script>
			</div>
			
			<div id="modifyLeadHeadDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>变更负责人</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						<div class="tip mt_30">
							<div class="con">
								<div class="desc fc_red">
									提示：如若变更了负责人，你将不再有权限看到申请信息，所有信息由你所选择的负责人进行管理和修改！
								</div>
							</div>
						</div>
						<form:form style="width: 600px;"  id="modifyLeadHeadForm" action="" method="post">
							
							<input type="hidden" id="modifyLeadHeadId" name="id" value="${xcGroup.id}"/>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>请选择负责人：</label>
										<div>
											<select class="select" id="newleader" name="newleader">
												<option value="">-请选择-</option>
												 <c:forEach items="${xcGroup.memberList}" var="member">
							                    	<option value="${member.id}">${member.xm}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#modifyLeadHeadDiv .btn_cancle',function(){
						
						$('#modifyLeadHeadDiv').fadeOut();
					});
					$('body').on('click','#modifyLeadHeadDiv .icon_close',function(){
						
						$('#modifyLeadHeadDiv').fadeOut();
					});
				</script>
			</div>
			
		</c:when>
		<c:when test="${fns:getUser().userType eq '3'}"><!-- 学生 -->
			<div id="uploadMaterialForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>材料信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						
						<form:form style="width: 600px;"  id="modifyUploadMaterialForm" action="${ctx}/hf/apply/uploadMaterial" method="post" enctype="multipart/form-data">
							
							<input type="hidden" id="materialid" name="id" value=""/>
							<input type="hidden" id="schid" name="school.id" value="${xcHfSchool.id}"/>
							<input type="hidden" id="materialGroupId" name="group.id" value="${xcGroup.id}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>材料名称：</label>
										<input type="text" class="required" id="stuffname" name="stuffname" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_upfile">
										<label class="label"><span class="req">*</span>选择文件：</label>
										<!-- <input  type="file" class="input-text"  style="width: 200px;border: none;" id="file" name="file"> -->
										<input type="hidden" id="materialstuffurl" name="stuffurl" value="" />
										<cms:playVideo videoUrl="" style="width:200px;height:100px;" />
										<div id="uploader" class="wu-example" style="margin-top:10px;">
											<div id="materialStuffurlThelist" class="uploader-list"></div>
											<div class="btns" style="">
												<div id="picker" style="width:150px;"></div>
												<!-- <button  id="materialStuffurlBtn" type="button" class="btn btn-danger ctlBtn">开始上传</button> -->
											</div>
										</div>
										<div class="text-muted" style="clear:left;margin-top:10px;">
											<p class="">提示： 1、上传文件类型为：${fns:getSysConfigWithDefaultValue("cms.movie.fileType", "")}；<br>
											2、上传文件大小不超过${fns:getSysConfigWithDefaultValue("cms.movie.fileSize", 500)}M。</p>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="button" id="materialStuffurlBtn" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#uploadMaterialForm .btn_cancle',function(){
						$("#uploadMaterialForm #modifyUploadMaterialForm").find("input").val("");
						$('#uploadMaterialForm').fadeOut();
					});
					$('body').on('click','#uploadMaterialForm .icon_close',function(){
						$("#uploadMaterialForm #modifyUploadMaterialForm").find("input").val("");
						$('#uploadMaterialForm').fadeOut();
					});
				</script>
			</div>
			<div id="uploadMemberMaterialDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>材料信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						
						<form:form style="width: 600px;"  id="uploadMemberMaterialForm" action="${ctx}/hf/apply/uploadMemberMaterial" method="post" enctype="multipart/form-data">
							
							<input type="hidden" id="materialEssayid" name="id" value=""/>
							<input type="hidden" id="materialMemberGroupId" name="group.id" value="${xcGroup.id}"/>
							<input type="hidden" id="materialMembeSchoolId" name="school.id" value="${xcHfSchool.id}"/>
							<input type="hidden" id="materialMemberid" name="member.id" value=""/>
							<input type="hidden" id="memberstuffname" name="stuffname" value=""/>
							<input type="hidden" id="membermaterialType" name="materialType" value=""/>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_upfile ">
										<label class="label"><span class="req">*</span>选择文件：</label>
										<!-- <input  type="file" class="input-text"  style="width: 200px;border: none;" id="file" name="file"> -->
										<input type="hidden" id="membermaterialstuffurl" name="stuffurl" value="" />
										<cms:playVideo videoUrl="" style="width:200px;height:100px;" />
										<div id="uploader" class="wu-example" style="margin-top:10px;">
											<div id="membermaterialstuffurlthelist" class="uploader-list"></div>
											<div class="btns" style="">
												<div id="pickerMember" style="width:150px;"></div>
												<!-- <button id="membermaterialstuffurlctlBtn" type="button" class="btn btn-danger ctlBtn">开始上传</button> -->
											</div>
										</div>
										<div class="text-muted" style="clear:left;margin-top:10px;">
											<p class="">提示： 1、上传文件类型为：${fns:getSysConfigWithDefaultValue("cms.movie.fileType", "")}；<br>
											2、上传文件大小不超过${fns:getSysConfigWithDefaultValue("cms.movie.fileSize", 500)}M。</p>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button  id="membermaterialstuffurlctlBtn" type="button" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#uploadMemberMaterialDiv .btn_cancle',function(){
						$("#uploadMemberMaterialDiv #uploadMemberMaterialForm").find("input").val("");
						$('#uploadMemberMaterialDiv').fadeOut();
					});
					$('body').on('click','#uploadMemberMaterialDiv .icon_close',function(){
						$("#uploadMemberMaterialDiv #uploadMemberMaterialForm").find("input").val("");
						$('#uploadMemberMaterialDiv').fadeOut();
					});
				</script>
			</div>
			
			<div id="uploadRegistrationFormDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>上传登记表</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						
						<form:form style="width: 600px;"  id="uploadRegistrationForm" action="${ctx}/hf/apply/uploadRegistration" method="post" enctype="multipart/form-data">
							
							<input type="hidden" id="uploadRegistrationFormGroupId" name="id" value=""/>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_upfile">
										<label class="label"><span class="req">*</span>选择文件：</label>
										<!-- <input  type="file" class="input-text"  style="width: 200px;border: none;" id="file" name="file"> -->
										<input type="hidden" id="uploadRegistrationFormGroupUrl" name="registrationform" value="" />
										<cms:playVideo videoUrl="" style="width:200px;height:100px;" />
										<div id="uploader" class="wu-example" style="margin-top:10px;">
											<div id="uploadRegistrationFormGroupThelist" class="uploader-list"></div>
											<div class="btns" style="">
												<div id="uploadRegistrationFormGroup" style="width:150px;"></div>
											</div>
										</div>
										<div class="text-muted" style="clear:left;margin-top:10px;">
											<p class="">提示： 1、上传文件类型为：word或者PDF；<br>
											2、上传文件大小不超过${fns:getSysConfigWithDefaultValue("cms.movie.fileSize", 500)}M。</p>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="button" id="uploadRegistrationFormGroupBtn" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#uploadRegistrationFormDiv .btn_cancle',function(){
						$("#uploadRegistrationFormDiv #uploadRegistrationForm").find("input").val("");
						$('#uploadRegistrationFormDiv').fadeOut();
					});
					$('body').on('click','#uploadRegistrationFormDiv .icon_close',function(){
						$("#uploadRegistrationFormDiv #uploadRegistrationForm").find("input").val("");
						$('#uploadRegistrationFormDiv').fadeOut();
					});
				</script>
			</div>
			
			<div class="wrapper">
				<div class="header">
					<a class="logo" href="#">
						<img src="${ctxStaticFront}/images/hfmx/logo.png"/>
					</a>
					<div class="right">
						<div class="funs">
							<%-- <a class="one" href="${ctx}/hf/apply/notify/self">
								 <i id="notifyNum" class="tip_num" style="display:none;"></i>
								消息通知
							</a>  --%>
							<%-- <a class="two" href="${ctx}/hf/apply/auditForm">
								业务
							</a> --%>
							<div class="user">
								<a href="#" class="u_con">
									<c:choose>
										<c:when test="${not empty fns:getUser() and not empty fns:getUser().photo}">
											<i class="stiker" style="background-image: url('${pageContext.request.contextPath}${fns:getUser().photo}?v=<%=Math.random()*100%>')"></i>
										</c:when>
										<c:otherwise>
											<i class="stiker"></i>
										</c:otherwise>
									</c:choose>
									
									<span>${fns:getUser().name}</span>
								</a>
								<div class="u_other">
									<ul>
										<li class="modify_pass">
											<a style="background-image: url(${ctxStaticFront}/images/hfmx/icon_menu_user.png);" href="${ctx}/hf/apply/modifyPersonInfo">个人中心</a>
										</li>
										<li class="modify_pass">
											<a href="${ctx}/hf/apply/modifyPasswordForm">修改密码</a>
										</li>
										<li class="quiz">
											<a href="javascript:void(0);" onclick="logout()">退出登录</a>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="body">
					<div class="l_l">
						<div class="m_user" onclick="window.location.href='${ctx}/hf/apply/modifyPersonInfo'">
							<c:choose>
								<c:when test="${not empty fns:getUser() and not empty fns:getUser().photo}">
									<div class="u_sticker" style="border-radius: 50%;background-image: url('${pageContext.request.contextPath}${fns:getUser().photo}?v=<%=Math.random()*100%>')"></div>
								</c:when>
								<c:otherwise>
									<div class="u_sticker" style="background-image: url('${ctxStaticFront}/images/hfmx/sticker.png')"></div>
								</c:otherwise>
							</c:choose>
							<div class="u_info">
								<div class="u_name">${fns:getUser().name}</div>
								<div class="u_profession">${fns:getUser().companyName}</div>
							</div>
						</div>
						<div class="menus">
							<ul>
								<li class="bm active">
									<a href="javascript:void(0);">
										${fns:getSysConfig('system.name')}报名<span class="icon_arrow"></span>
									</a>
									<div class="sub" style="height:180px;">
										<ul>
											<li <c:if test="${ename == 'applyGroup'}"> class="active"</c:if>>
												<a href="${ctx}/hf/apply">报名信息</a>
											</li>
											<li <c:if test="${ename == 'xcCommanderReport'}"> class="active"</c:if>>
												<a href="${ctx}/hf/apply/commanderList">团长报名</a>
											</li>
											<li <c:if test="${ename == 'auditForm'}">class="active"</c:if>>
												<a href="${ctx}/hf/apply/auditForm">活动管理</a>
											</li>
											<li <c:if test="${ename == 'activityArrange'}">class="active"</c:if>>
												<a href="${ctx}/hf/apply/activityArrange">活动计划</a>
											</li>
											
										<%-- 	<li <c:if test="${ename == 'printForm'}">class="active"</c:if> >
												<a href="${ctx}/hf/apply/printForm" target="_blank">
													报名表打印
												</a>
											</li> --%>
											<li <c:if test="${ename == 'uploadRegistrationForm'}">class="active"</c:if>>
												<a href="${ctx}/hf/apply/uploadRegistrationForm">登记表上传</a>
											</li>
											<li <c:if test="${ename == 'schoolList'}">class="active"</c:if> >
												<a href="${ctx}/hf/apply/schoolList">总结上传</a>
											</li>
										</ul>
									</div>
								</li>
								<li class="dy <c:if test="${ename == 'oaNotifySelf'}"> active</c:if>" >
									<a href="${ctx}/hf/apply/notify/self" style="background-image: url(${ctxStaticFront}/images/com/notify.png);">
										消息通知
									</a> 
								</li>
								
							</ul>
							<script type="text/javascript">
								$(function(){
									var $active = $('.l_l .menus li.active');
									var $lis = $active.parents('li');
									if( $lis.length > 0 ){
										$.each($lis,function(){
											$(this).addClass('active');
											var h = $(this).find('.sub>ul').height();
											$(this).find('.sub').css( 'height' , h + 'px' );
										});
									}
									$('.l_l .menus li>a').filter(function(){
										return $(this).parents('li').eq(0).find('.sub').length > 0
									}).on('click',function(){
										var $li = $(this).parents('li').eq(0);
										if( $li.hasClass('active') ){
											closeActive();
										}else{
											closeActive();
											var $sub = $(this).parents('li').eq(0).find('.sub');
											if( $sub.length > 0 ){
												var h = $sub.children('ul').height();
												$sub.css( 'height' , h + 'px' );
											}
											$(this).parents('li').eq(0).addClass('active');
										};
										function closeActive(){
											var $lis = $('.menus').find('.active').filter(function(){
												return $(this).parents('.sub').length === 0;
											});
											$.each($lis,function(){
												var $sub = $(this).find('.sub');
												if( $sub.length > 0 ){
													$(this).removeClass('active');
													var $ul = $sub.children('ul');
													$sub.css( 'height' , '0px' );
												}else{
													$(this).removeClass('active');
												}
											});
										}
									});
								});
							</script>
							
							<div class="bottom_fun">
								<a href="${ctx}/hf/apply/modifyPasswordForm"><i class="icon_set"></i>修改密码</a>
								<a href="javascript:void(0);" onclick="logout()"><i class="icon_quiz"></i>退出登录</a>
							</div>
							
						</div>
					</div>
				
				  	<sitemesh:body/>
				  	
				</div>
				<div class="footer" style="padding: 20px 0px;">
					<div class="right">
						<div class="contact tc">
							<span>电话：${fns:getSysConfig('contact_phone')} </span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span>地址：${fns:getSysConfig('contact_address')} </span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span class="copy_right">版权所有：${fns:getSysConfig('copyrightUnit')} </span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span class="suport">技术支持：北京云智小橙科技有限公司</span>&nbsp;&nbsp;&nbsp;&nbsp;
						</div>
					</div>
				</div>
			</div>
			
			<div id="schoolModifyForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>回访母校信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						
						<form:form style="width: 600px;"  id="modifySchoolForm" action="" method="post">
							
							<input type="hidden" id="schoolid" name="id" value=""/>
							<input type="hidden" id="schoolGroupId" name="group.id" value=""/>
							<input type="hidden" id="schoolActivityId" name="activity.id" value=""/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select_addr">
										<label class="label"><span class="req">*</span>请选择区域：</label>
										<div>
											<select class="select" id="schoolprovince" name="province.id"  onchange="provinceChange2()">
												<option value="">-请选择省份、直辖市-</option>
												 <c:forEach items="${provinceAreaList}" var="provinceArea">
							                    	<option value="${provinceArea.id}">${provinceArea.name}</option>
							                    </c:forEach>
											</select>
										</div>
										<div>
											<select class="select" id="schoolcity" name="city.id" onchange="cityChange2()">
												<option value="">-请选择地市-</option>
												<c:forEach items="${cityAreaList}" var="cityArea">
							                    	<option value="${cityArea.id}">${cityArea.name}</option>
							                    </c:forEach>
											</select>
										</div>
										<div>
											<select class="select" id="schoolcountry" name="country.id" onchange="countryChange2()">
												<option value="">-请选择区县-</option>
												<c:forEach items="${countyAreaList}" var="countryArea">
							                    	<option value="${countryArea.id}" >${countryArea.name}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>学校：</label>
										<div class="schoolSelectModifyDiv">
											<select class="select" id="schoolModify" name="school.id"  onchange="schoolChange2()">
												<option value="">-请选择-</option>
												 <c:forEach items="${highSchoolList}" var="highschool">
							                    	<option value="${highschool.id}">${highschool.schoolName}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="row mt_15" id="schoolModifyDiv" style="display:none;">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>具体学校名称：</label>
										<input type="text"  placeholder="请输入具体学校名称" class="" id="schoolNameModify" name="schoolName" value=""/>
									</div>
								</div>
							</div>
							<div class="row mt_20 repeatSchoolModifyDiv" style="display:none;">
								<div class="chunk_12">
									<div class="comp_select_addr">
										<label class="label messageLabel" style="color:red;text-align: justify;"></label>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>中学联系人：</label>
										<input type="text" class="required" id="schoolModifyLxr" name="schoolLxr" value=""/>
									</div>
								</div>
							</div>
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>中学联系人职务：</label>
										<input type="text" class="required" id="schoolModifyLxrzw" name="schoolLxrzw" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>中学联系人电话：</label>
										<input type="text" class="required" id="schoolModifyLxrphone" name="schoolLxrphone" value=""/>
									</div>
								</div>
								
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#schoolModifyForm .btn_cancle',function(){
						
						$('#schoolModifyForm').fadeOut();
					});
					$('body').on('click','#schoolModifyForm .icon_close',function(){
						
						$('#schoolModifyForm').fadeOut();
					});
				</script>
			</div>
			
			<div id="memberModifyForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>组员信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						
						<form:form style="width: 600px;"  id="modifyMemberForm" action="" method="post">
							
							<input type="hidden" id="memberid" name="id" value=""/>
							<input type="hidden" id="membergroupId" name="group.id" value=""/>
							<input type="hidden" id="memberactivityId" name="activity.id" value=""/>
							<input type="hidden" id="memberisLeader" name="isLeader" value=""/>
							<input type="hidden" id="memberisHead" name="isHead" value=""/>
							<input type="hidden" id="memberisJoin" name="isJoin" value=""/>
							<input type="hidden" id="memberschoolids" name="schoolids" value="${schoolids}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>学号：</label>
										<input type="text" class="required" id="memberXh" name="xh" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>姓名：</label>
										<input type="text" class="required" id="memberXm" name="xm" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>校区：</label>
										<div>
											<select class="select"  id="memberofficexqid" name="officexq.id" >
												<option value="">-请选择校区-</option>
												 <c:forEach items="${xqList}" var="xq">
						                       		 <option value="${xq.id}">${xq.name}</option>
						                         </c:forEach>
											</select>
										</div>
										
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>院系：</label>
										
										<div>
											<select class="select" id="memberofficexyid" name="officexy.id" onchange="xyChange2()" >
												<option value="">-请选择学院-</option>
												<c:forEach items="${xyList}" var="xy">
						                       		<option value="${xy.id}">${xy.name}</option>
						                        </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="row mt_15 memberacademydiv">
								<div class="chunk_12">
									<div class="comp_input">
										<input type="text" id="memberofficexy" name="xy" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>专业：</label>
										<div>
											<select class="select" id="memberofficezyid" name="officezy.id"  onchange="zyChange2()">
												<option value="">-请选择专业-</option>
												<c:forEach items="${zyList}" var="zy">
							                      	<option value="${zy.id}">${zy.name}</option>
							                     </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="row mt_15 membermajordiv">
								<div class="chunk_12">
									<div class="comp_input">
										<input type="text" id="memberofficezy" name="zy" value="" />
									</div>
								</div>
							</div>
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>年级：</label>
										<div>
											<select class="select required" id="membergrade" name="grade">
												<option value="">-请选择年级-</option>
												<c:forEach items="${fns:getDictList('grade')}" var="item">
							                      	<option  value="${item.value}">${item.label}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>班级（填写班号，例如08211001）：</label>
										<input type="text" class="required" id="memberclassname" name="classname" value="" />
									</div>
									
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>身份证号：</label>
										<input type="text" maxlength="18" class="required" id="membersfz" name="sfz" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>手机号：</label>
										<input type="text" class="required mobile" id="membermobile" name="mobile" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>邮箱：</label>
										<input type="text" class="required email" id="memberemail" name="email" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#memberModifyForm .btn_cancle',function(){
						
						$('#memberModifyForm').fadeOut();
					});
					$('body').on('click','#memberModifyForm .icon_close',function(){
						
						$('#memberModifyForm').fadeOut();
					});
				</script>
			</div>
			
			<div id="applyModifyForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>申请修改</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						
						<form:form style="width: 600px;"  id="applyModify" action="" method="post">
							
							<input type="hidden" id="applyGroupModifyId" name="id" value="${xcGroup.id}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_textarea">
										<label class="label"><span class="req">*</span>申请修改原因：</label>
										<textarea  class="required"  id="applyModifyReason" name="applyModifyReason">${xcGroup.applyModifyReason}</textarea>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#applyModifyForm .btn_cancle',function(){
						
						$('#applyModifyForm').fadeOut();
					});
					$('body').on('click','#applyModifyForm .icon_close',function(){
						
						$('#applyModifyForm').fadeOut();
					});
				</script>
			</div>
			
			<div id="modifyLeadHeadDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>变更负责人</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						<div class="tip mt_30">
							<div class="con">
								<div class="desc fc_red">
									提示：如若变更了负责人，你将不再有权限看到申请信息，所有信息由你所选择的负责人进行管理和修改！
								</div>
							</div>
						</div>
						<form:form style="width: 600px;"  id="modifyLeadHeadForm" action="" method="post">
							
							<input type="hidden" id="modifyLeadHeadId" name="id" value="${xcGroup.id}"/>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>请选择负责人：</label>
										<div>
											<select class="select" id="newleader" name="newleader">
												<option value="">-请选择-</option>
												 <c:forEach items="${addMemberList}" var="item">
							                    	<option value="${item.member.id}">${item.member.xm}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#modifyLeadHeadDiv .btn_cancle',function(){
						
						$('#modifyLeadHeadDiv').fadeOut();
					});
					$('body').on('click','#modifyLeadHeadDiv .icon_close',function(){
						
						$('#modifyLeadHeadDiv').fadeOut();
					});
				</script>
			</div>
			
			
			<div id="modifyAddresseeDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>修改地址信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						
						<form:form style="width: 600px;"  id="modifyAddresseeForm" action="" method="post">
							
							<input type="hidden" id="addresseeId" name="id" value=""/>
							<input type="hidden" id="addresseeAddressId" name="address.id" value=""/>
							<input type="hidden" id="addresseeActivityId" name="activity.id" value=""/>
							<input type="hidden" id="addresseeisFirst" name="isFirst" value=""/>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>收件人：</label>
										<input type="text" class="required" id="addresseeLinkXm" name="linkXm" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>收件人电话：</label>
										<input type="text" class="required" id="addresseeLinkMobile" name="linkMobile" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#modifyAddresseeDiv .btn_cancle',function(){
						
						$('#modifyAddresseeDiv').fadeOut();
					});
					$('body').on('click','#modifyAddresseeDiv .icon_close',function(){
						
						$('#modifyAddresseeDiv').fadeOut();
					});
				</script>
			</div>
			<!-- 添加时间 -->
			<div id="timeArrangeDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>时间安排</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						<div class="tip mt_30">
							<div class="con">
								<div class="fb">友情提示</div>
								<div class="desc">
									填写示例：2019年11月10日10时-2019年11月10日12时，进行招生宣讲。
								</div>
							</div>
						</div>
						<form:form style="width: 600px;"  id="timeArrangeForm" action="" method="post">
							
							<input type="hidden" id="timeArrangeId" name="id" value=""/>
							<input type="hidden" id="timeArrangeSchoolId" name="school.id" value="${xcHfSchool.id}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>开始时间：</label>
										<input  class="datetimepicker required" id="timeArrangeStartDate" name="startDate" type="text" 
										value="" 
										onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});" />
									</div>
								</div>
							</div>
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>结束时间：</label>
										<input  class="datetimepicker required" id="timeArrangeEndDate" name="endDate" type="text" 
										value="" 
										onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"  />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>活动安排：</label>
										<input type="text" class="required" id="timeArrangeDescription" name="description" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#timeArrangeDiv .btn_cancle',function(){
						
						$('#timeArrangeDiv').fadeOut();
					});
					$('body').on('click','#timeArrangeDiv .icon_close',function(){
						
						$('#timeArrangeDiv').fadeOut();
					});
				</script>
			</div>
			
		</c:when>
		<c:otherwise>
			<div id="uploadMaterialForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>材料信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						
						<form:form style="width: 600px;"  id="modifyUploadMaterialForm" action="${ctx}/hf/apply/uploadMaterial" method="post" enctype="multipart/form-data">
							
							<input type="hidden" id="materialid" name="id" value=""/>
							<input type="hidden" id="schid" name="school.id" value="${xcHfSchool.id}"/>
							<input type="hidden" id="materialGroupId" name="group.id" value="${xcGroup.id}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>材料名称：</label>
										<input type="text" class="required" id="stuffname" name="stuffname" value=""/>
									</div>
								</div>
							</div>
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_upfile">
										<label class="label"><span class="req">*</span>选择文件：</label>
										<!-- <input  type="file" class="input-text"  style="width: 200px;border: none;" id="file" name="file"> -->
										<input type="hidden" id="materialstuffurl" name="stuffurl" value="" />
										<cms:playVideo videoUrl="" style="width:200px;height:100px;" />
										<div id="uploader" class="wu-example" style="margin-top:10px;">
											<div id="materialStuffurlThelist" class="uploader-list"></div>
											<div class="btns" style="">
												<div id="picker" style="width:150px;"></div>
												<!-- <button  id="materialStuffurlBtn" type="button" class="btn btn-danger ctlBtn">开始上传</button> -->
											</div>
										</div>
										<div class="text-muted" style="clear:left;margin-top:10px;">
											<p class="">提示： 1、上传文件类型为：${fns:getSysConfigWithDefaultValue("cms.movie.fileType", "")}；<br>
											2、上传文件大小不超过${fns:getSysConfigWithDefaultValue("cms.movie.fileSize", 500)}M。</p>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="button" id="materialStuffurlBtn" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#uploadMaterialForm .btn_cancle',function(){
						$("#uploadMaterialForm #modifyUploadMaterialForm").find("input").val("");
						$('#uploadMaterialForm').fadeOut();
					});
					$('body').on('click','#uploadMaterialForm .icon_close',function(){
						$("#uploadMaterialForm #modifyUploadMaterialForm").find("input").val("");
						$('#uploadMaterialForm').fadeOut();
					});
				</script>
			</div>
			<div id="uploadMemberMaterialDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>材料信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						
						<form:form style="width: 600px;"  id="uploadMemberMaterialForm" action="${ctx}/hf/apply/uploadMemberMaterial" method="post" enctype="multipart/form-data">
							
							<input type="hidden" id="materialEssayid" name="id" value=""/>
							<input type="hidden" id="materialMemberGroupId" name="group.id" value="${xcGroup.id}"/>
							<input type="hidden" id="materialMembeSchoolId" name="school.id" value="${xcHfSchool.id}"/>
							<input type="hidden" id="materialMemberid" name="member.id" value=""/>
							<input type="hidden" id="memberstuffname" name="stuffname" value=""/>
							<input type="hidden" id="membermaterialType" name="materialType" value=""/>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_upfile ">
										<label class="label"><span class="req">*</span>选择文件：</label>
										<!-- <input  type="file" class="input-text"  style="width: 200px;border: none;" id="file" name="file"> -->
										<input type="hidden" id="membermaterialstuffurl" name="stuffurl" value="" />
										<cms:playVideo videoUrl="" style="width:200px;height:100px;" />
										<div id="uploader" class="wu-example" style="margin-top:10px;">
											<div id="membermaterialstuffurlthelist" class="uploader-list"></div>
											<div class="btns" style="">
												<div id="pickerMember" style="width:150px;"></div>
												<!-- <button id="membermaterialstuffurlctlBtn" type="button" class="btn btn-danger ctlBtn">开始上传</button> -->
											</div>
										</div>
										<div class="text-muted" style="clear:left;margin-top:10px;">
											<p class="">提示： 1、上传文件类型为：${fns:getSysConfigWithDefaultValue("cms.movie.fileType", "")}；<br>
											2、上传文件大小不超过${fns:getSysConfigWithDefaultValue("cms.movie.fileSize", 500)}M。</p>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button  id="membermaterialstuffurlctlBtn" type="button" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#uploadMemberMaterialDiv .btn_cancle',function(){
						$("#uploadMemberMaterialDiv #uploadMemberMaterialForm").find("input").val("");
						$('#uploadMemberMaterialDiv').fadeOut();
					});
					$('body').on('click','#uploadMemberMaterialDiv .icon_close',function(){
						$("#uploadMemberMaterialDiv #uploadMemberMaterialForm").find("input").val("");
						$('#uploadMemberMaterialDiv').fadeOut();
					});
				</script>
			</div>
			
			<div id="uploadRegistrationFormDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>上传登记表</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						
						<form:form style="width: 600px;"  id="uploadRegistrationForm" action="${ctx}/hf/apply/uploadRegistration" method="post" enctype="multipart/form-data">
							
							<input type="hidden" id="uploadRegistrationFormGroupId" name="id" value=""/>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_upfile">
										<label class="label"><span class="req">*</span>选择文件：</label>
										<!-- <input  type="file" class="input-text"  style="width: 200px;border: none;" id="file" name="file"> -->
										<input type="hidden" id="uploadRegistrationFormGroupUrl" name="registrationform" value="" />
										<cms:playVideo videoUrl="" style="width:200px;height:100px;" />
										<div id="uploader" class="wu-example" style="margin-top:10px;">
											<div id="uploadRegistrationFormGroupThelist" class="uploader-list"></div>
											<div class="btns" style="">
												<div id="uploadRegistrationFormGroup" style="width:150px;"></div>
											</div>
										</div>
										<div class="text-muted" style="clear:left;margin-top:10px;">
											<p class="">提示： 1、上传文件类型为：word或者PDF；<br>
											2、上传文件大小不超过${fns:getSysConfigWithDefaultValue("cms.movie.fileSize", 500)}M。</p>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="button" id="uploadRegistrationFormGroupBtn" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#uploadRegistrationFormDiv .btn_cancle',function(){
						$("#uploadRegistrationFormDiv #uploadRegistrationForm").find("input").val("");
						$('#uploadRegistrationFormDiv').fadeOut();
					});
					$('body').on('click','#uploadRegistrationFormDiv .icon_close',function(){
						$("#uploadRegistrationFormDiv #uploadRegistrationForm").find("input").val("");
						$('#uploadRegistrationFormDiv').fadeOut();
					});
				</script>
			</div>
			
			<div class="wrapper">
				<div class="header">
					<a class="logo" href="#">
						<img src="${ctxStaticFront}/images/hfmx/logo.png"/>
					</a>
					<div class="right">
						<div class="funs">
							<%-- <a class="one" href="${ctx}/hf/apply/notify/self">
								 <i id="notifyNum" class="tip_num" style="display:none;"></i>
								消息通知
							</a> 
							<a class="two" href="${ctx}/hf/apply/auditForm">
								业务
							</a> --%>
							<div class="user">
								<a href="#" class="u_con">
									<c:choose>
										<c:when test="${not empty fns:getUser() and not empty fns:getUser().photo}">
											<i class="stiker" style="background-image: url('${pageContext.request.contextPath}${fns:getUser().photo}?v=<%=Math.random()*100%>')"></i>
										</c:when>
										<c:otherwise>
											<i class="stiker"></i>
										</c:otherwise>
									</c:choose>
									
									<span>${fns:getUser().name}</span>
								</a>
								<div class="u_other">
									<ul>
										<li class="modify_pass">
											<a style="background-image: url(${ctxStaticFront}/images/hfmx/icon_menu_user.png);" href="${ctx}/hf/apply/modifyPersonInfo">个人中心</a>
										</li>
										<li class="modify_pass">
											<a href="${ctx}/hf/apply/modifyPasswordForm">修改密码</a>
										</li>
										<li class="quiz">
											<a href="javascript:void(0);" onclick="logout()">退出登录</a>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="body">
					<div class="l_l">
						<div class="m_user" onclick="window.location.href='${ctx}/hf/apply/modifyPersonInfo'">
							<c:choose>
								<c:when test="${not empty fns:getUser() and not empty fns:getUser().photo}">
									<div class="u_sticker" style="border-radius: 50%;background-image: url('${pageContext.request.contextPath}${fns:getUser().photo}?v=<%=Math.random()*100%>')"></div>
								</c:when>
								<c:otherwise>
									<div class="u_sticker" style="background-image: url('${ctxStaticFront}/images/hfmx/sticker.png')"></div>
								</c:otherwise>
							</c:choose>
							<div class="u_info">
								<div class="u_name">${fns:getUser().name}</div>
								<div class="u_profession">${fns:getUser().companyName}</div>
							</div>
						</div>
						<div class="menus">
							<ul>
								<li class="bm active">
									<a href="javascript:void(0);">
										${fns:getSysConfig('system.name')}报名<span class="icon_arrow"></span>
									</a>
									<div class="sub" style="height:315px;">
										<ul>
											<li <c:if test="${ename == 'applyGroup'}"> class="active"</c:if>>
												<a href="${ctx}/hf/apply">报名信息</a>
											</li>
											<li <c:if test="${ename == 'xcCommanderReport'}"> class="active"</c:if>>
												<a href="${ctx}/hf/apply/commanderList">团长报名</a>
											</li>
											<li <c:if test="${ename == 'auditForm'}">class="active"</c:if>>
												<a href="${ctx}/hf/apply/auditForm">活动管理</a>
											</li>
											<li <c:if test="${ename == 'activityArrange'}">class="active"</c:if>>
												<a href="${ctx}/hf/apply/activityArrange">活动计划</a>
											</li>
											
											<%-- <li <c:if test="${ename == 'printForm'}">class="active"</c:if> >
												<a href="${ctx}/hf/apply/printForm" target="_blank">
													报名表打印
												</a>
											</li> --%>
											<li <c:if test="${ename == 'uploadRegistrationForm'}">class="active"</c:if>>
												<a href="${ctx}/hf/apply/uploadRegistrationForm">登记表上传</a>
											</li>
											<li <c:if test="${ename == 'schoolList'}">class="active"</c:if> >
												<a href="${ctx}/hf/apply/schoolList">总结上传</a>
											</li>
										</ul>
									</div>
								</li>
								<li class="dy <c:if test="${ename == 'oaNotifySelf'}"> active</c:if>" >
									<a href="${ctx}/hf/apply/notify/self" style="background-image: url(${ctxStaticFront}/images/com/notify.png);">
										消息通知
									</a> 
								</li>
							</ul>
							<script type="text/javascript">
								$(function(){
									var $active = $('.l_l .menus li.active');
									var $lis = $active.parents('li');
									if( $lis.length > 0 ){
										$.each($lis,function(){
											$(this).addClass('active');
											var h = $(this).find('.sub>ul').height();
											$(this).find('.sub').css( 'height' , h + 'px' );
										});
									}
									$('.l_l .menus li>a').filter(function(){
										return $(this).parents('li').eq(0).find('.sub').length > 0
									}).on('click',function(){
										var $li = $(this).parents('li').eq(0);
										if( $li.hasClass('active') ){
											closeActive();
										}else{
											closeActive();
											var $sub = $(this).parents('li').eq(0).find('.sub');
											if( $sub.length > 0 ){
												var h = $sub.children('ul').height();
												$sub.css( 'height' , h + 'px' );
											}
											$(this).parents('li').eq(0).addClass('active');
										};
										function closeActive(){
											var $lis = $('.menus').find('.active').filter(function(){
												return $(this).parents('.sub').length === 0;
											});
											$.each($lis,function(){
												var $sub = $(this).find('.sub');
												if( $sub.length > 0 ){
													$(this).removeClass('active');
													var $ul = $sub.children('ul');
													$sub.css( 'height' , '0px' );
												}else{
													$(this).removeClass('active');
												}
											});
										}
									});
								});
							</script>
							
							<div class="bottom_fun">
								<a href="${ctx}/hf/apply/modifyPasswordForm"><i class="icon_set"></i>修改密码</a>
								<a href="javascript:void(0);" onclick="logout()"><i class="icon_quiz"></i>退出登录</a>
							</div>
							
						</div>
					</div>
				
				  	<sitemesh:body/>
				  	
				</div>
				<div class="footer" style="padding: 20px 0px;">
					<div class="right">
						<div class="contact tc">
							<span>电话：${fns:getSysConfig('contact_phone')} </span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span>地址：${fns:getSysConfig('contact_address')} </span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span class="copy_right">版权所有：${fns:getSysConfig('copyrightUnit')} </span>&nbsp;&nbsp;&nbsp;&nbsp;
							<span class="suport">技术支持：北京云智小橙科技有限公司</span>&nbsp;&nbsp;&nbsp;&nbsp;
						</div>
					</div>
				</div>
			</div>
			
			<div id="schoolModifyForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>修改回访母校信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						
						<form:form style="width: 600px;"  id="modifySchoolForm" action="" method="post">
							
							<input type="hidden" id="schoolid" name="id" value=""/>
							<input type="hidden" id="schoolGroupId" name="group.id" value=""/>
							<input type="hidden" id="schoolActivityId" name="activity.id" value=""/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select_addr">
										<label class="label"><span class="req">*</span>请选择区域：</label>
										<div>
											<select class="select" id="schoolprovince" name="province.id"  onchange="provinceChange2()">
												<option value="">-请选择省份、直辖市-</option>
												 <c:forEach items="${provinceAreaList}" var="provinceArea">
							                    	<option value="${provinceArea.id}">${provinceArea.name}</option>
							                    </c:forEach>
											</select>
										</div>
										<div>
											<select class="select" id="schoolcity" name="city.id" onchange="cityChange2()">
												<option value="">-请选择地市-</option>
												<c:forEach items="${cityAreaList}" var="cityArea">
							                    	<option value="${cityArea.id}">${cityArea.name}</option>
							                    </c:forEach>
											</select>
										</div>
										<div>
											<select class="select" id="schoolcountry" name="country.id" onchange="countryChange2()">
												<option value="">-请选择区县-</option>
												<c:forEach items="${countyAreaList}" var="countryArea">
							                    	<option value="${countryArea.id}" >${countryArea.name}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>学校：</label>
										<div class="schoolSelectModifyDiv">
											<select class="select" id="schoolModify" name="school.id"  onchange="schoolChange2()">
												<option value="">-请选择-</option>
												 <c:forEach items="${highSchoolList}" var="highschool">
							                    	<option value="${highschool.id}">${highschool.schoolName}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="row mt_15" id="schoolModifyDiv" style="display:none;">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>具体学校名称：</label>
										<input type="text"  placeholder="请输入具体学校名称" class="" id="schoolNameModify" name="schoolName" value=""/>
									</div>
								</div>
							</div>
							<div class="row mt_20 repeatSchoolModifyDiv" style="display:none;">
								<div class="chunk_12">
									<div class="comp_select_addr">
										<label class="label messageLabel" style="color:red;text-align: justify;"></label>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>中学联系人：</label>
										<input type="text" class="required" id="schoolModifyLxr" name="schoolLxr" value=""/>
									</div>
								</div>
							</div>
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>中学联系人职务：</label>
										<input type="text" class="required" id="schoolModifyLxrzw" name="schoolLxrzw" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>中学联系人电话：</label>
										<input type="text" class="required" id="schoolModifyLxrphone" name="schoolLxrphone" value="" />
									</div>
								</div>
								
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#schoolModifyForm .btn_cancle',function(){
						
						$('#schoolModifyForm').fadeOut();
					});
					$('body').on('click','#schoolModifyForm .icon_close',function(){
						
						$('#schoolModifyForm').fadeOut();
					});
				</script>
			</div>
			
			<div id="memberModifyForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>组员信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						
						<form:form style="width: 600px;"  id="modifyMemberForm" action="" method="post">
							
							<input type="hidden" id="memberid" name="id" value=""/>
							<input type="hidden" id="membergroupId" name="group.id" value=""/>
							<input type="hidden" id="memberactivityId" name="activity.id" value=""/>
							<input type="hidden" id="memberisLeader" name="isLeader" value=""/>
							<input type="hidden" id="memberisHead" name="isHead" value=""/>
							<input type="hidden" id="memberisJoin" name="isJoin" value=""/>
							<input type="hidden" id="memberschoolids" name="schoolids" value="${schoolids}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>学号：</label>
										<input type="text" class="required" id="memberXh" name="xh" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>姓名：</label>
										<input type="text" class="required" id="memberXm" name="xm" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>校区：</label>
										<div>
											<select class="select"  id="memberofficexqid" name="officexq.id" >
												<option value="">-请选择校区-</option>
												 <c:forEach items="${xqList}" var="xq">
						                       		 <option value="${xq.id}">${xq.name}</option>
						                         </c:forEach>
											</select>
										</div>
										
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>院系：</label>
										
										<div>
											<select class="select" id="memberofficexyid" name="officexy.id" onchange="xyChange2()" >
												<option value="">-请选择学院-</option>
												<c:forEach items="${xyList}" var="xy">
						                       		<option value="${xy.id}">${xy.name}</option>
						                        </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="row mt_15 memberacademydiv">
								<div class="chunk_12">
									<div class="comp_input">
										<input type="text" id="memberofficexy" name="xy" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>专业：</label>
										<div>
											<select class="select" id="memberofficezyid" name="officezy.id"  onchange="zyChange2()">
												<option value="">-请选择专业-</option>
												<c:forEach items="${zyList}" var="zy">
							                      	<option value="${zy.id}">${zy.name}</option>
							                     </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="row mt_15 membermajordiv">
								<div class="chunk_12">
									<div class="comp_input">
										<input type="text" id="memberofficezy" name="zy" value="" />
									</div>
								</div>
							</div>
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>年级：</label>
										<div>
											<select class="select required" id="membergrade" name="grade">
												<option value="">-请选择年级-</option>
												<c:forEach items="${fns:getDictList('grade')}" var="item">
							                      	<option  value="${item.value}">${item.label}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>班级（填写班号，例如08211001）：</label>
										<input type="text" class="required" id="memberclassname" name="classname" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>身份证号：</label>
										<input type="text" maxlength="18" class="required" id="membersfz" name="sfz" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>手机号：</label>
										<input type="text" class="required mobile" id="membermobile" name="mobile" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>邮箱：</label>
										<input type="text" class="required email" id="memberemail" name="email" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#memberModifyForm .btn_cancle',function(){
						
						$('#memberModifyForm').fadeOut();
					});
					$('body').on('click','#memberModifyForm .icon_close',function(){
						
						$('#memberModifyForm').fadeOut();
					});
				</script>
			</div>
			
			<div id="applyModifyForm" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>申请修改</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						
						<form:form style="width: 600px;"  id="applyModify" action="" method="post">
							
							<input type="hidden" id="applyGroupModifyId" name="id" value="${xcGroup.id}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_textarea">
										<label class="label"><span class="req">*</span>申请修改原因：</label>
										<textarea  class="required"  id="applyModifyReason" name="applyModifyReason">${xcGroup.applyModifyReason}</textarea>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#applyModifyForm .btn_cancle',function(){
						
						$('#applyModifyForm').fadeOut();
					});
					$('body').on('click','#applyModifyForm .icon_close',function(){
						
						$('#applyModifyForm').fadeOut();
					});
				</script>
			</div>
			
			<div id="modifyLeadHeadDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>变更负责人</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						<div class="tip mt_30">
							<div class="con">
								<div class="desc fc_red">
									提示：如若变更了负责人，你将不再有权限看到申请信息，所有信息由你所选择的负责人进行管理和修改！
								</div>
							</div>
						</div>
						<form:form style="width: 600px;"  id="modifyLeadHeadForm" action="" method="post">
							
							<input type="hidden" id="modifyLeadHeadId" name="id" value="${xcGroup.id}"/>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_select">
										<label class="label"><span class="req">*</span>请选择负责人：</label>
										<div>
											<select class="select" id="newleader" name="newleader">
												<option value="">-请选择-</option>
												 <c:forEach items="${xcGroup.memberList}" var="member">
							                    	<option value="${member.id}">${member.xm}</option>
							                    </c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#modifyLeadHeadDiv .btn_cancle',function(){
						
						$('#modifyLeadHeadDiv').fadeOut();
					});
					$('body').on('click','#modifyLeadHeadDiv .icon_close',function(){
						
						$('#modifyLeadHeadDiv').fadeOut();
					});
				</script>
			</div>
			
			
			<div id="modifyAddresseeDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>修改组员信息</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30">
						
						<form:form style="width: 600px;"  id="modifyAddresseeForm" action="" method="post">
							
							<input type="hidden" id="addresseeId" name="id" value=""/>
							<input type="hidden" id="addresseeAddressId" name="address.id" value=""/>
							<input type="hidden" id="addresseeActivityId" name="activity.id" value=""/>
							<input type="hidden" id="addresseeisFirst" name="isFirst" value=""/>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>收件人：</label>
										<input type="text" class="required" id="addresseeLinkXm" name="linkXm" value=""/>
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>收件人电话：</label>
										<input type="text" class="required" id="addresseeLinkMobile" name="linkMobile" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#modifyAddresseeDiv .btn_cancle',function(){
						
						$('#modifyAddresseeDiv').fadeOut();
					});
					$('body').on('click','#modifyAddresseeDiv .icon_close',function(){
						
						$('#modifyAddresseeDiv').fadeOut();
					});
				</script>
			</div>
			
			<!-- 添加时间 -->
			<div id="timeArrangeDiv" class="form_box" style="display: none;">
				<div class="entity">
					<div class="tit">
						<span>时间安排</span>
						<i class="icon_close"></i>
					</div>
					<div class="ml_30 mr_30 mt_30">
						<div class="tip mt_30">
							<div class="con">
								<div class="fb">友情提示</div>
								<div class="desc">
									填写示例：2019年11月10日10时-2019年11月10日12时，进行招生宣讲。
								</div>
							</div>
						</div>
						<form:form style="width: 600px;"  id="timeArrangeForm" action="" method="post">
							
							<input type="hidden" id="timeArrangeId" name="id" value=""/>
							<input type="hidden" id="timeArrangeSchoolId" name="school.id" value="${xcHfSchool.id}"/>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>开始时间：</label>
										<input  class="datetimepicker" id="timeArrangeStartDate" name="startDate" type="text" 
										value="" 
										onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"  />
									</div>
								</div>
							</div>
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>结束时间：</label>
										<input  class="datetimepicker" id="timeArrangeEndDate" name="endDate" type="text" 
										value="" 
										onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"  />
									</div>
								</div>
							</div>
							
							<div class="row mt_15">
								<div class="chunk_12">
									<div class="comp_input">
										<label class="label"><span class="req">*</span>活动安排：</label>
										<input type="text" class="required" id="timeArrangeDescription" name="description" value="" />
									</div>
								</div>
							</div>
							
							<div class="row mt_20">
								<div class="chunk_12">
									<div class="comp_button">
										<button type="button" class="btn normal form_normal btn_cancle">
											取消
										</button>
										<button type="submit" class="btn normal form_theme ml_20">
											保存
										</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
				<script type="text/javascript">
					
					$('body').on('click','#timeArrangeDiv .btn_cancle',function(){
						
						$('#timeArrangeDiv').fadeOut();
					});
					$('body').on('click','#timeArrangeDiv .icon_close',function(){
						
						$('#timeArrangeDiv').fadeOut();
					});
				</script>
			</div>
			
		</c:otherwise>
	</c:choose>

	
	
	
	<div class="confirm_box plugs_tip" style="display: none;">
		<div class="entity">
			<div class="img">
				<img src="${ctxStaticFront}/images/hfmx/plugs_tip.png"/>
			</div>
			<div class="tit" id="tipTit"></div>
			<div class="desc" id="tipDesc"></div>
			<div class="btns">
				<button type="button" class="btn small" fun_type='cancel'>取消</button>
				<button type="button" class="btn small form_theme ml_5" fun_type='confirm'>确定</button>
			</div>
		</div>
	</div>
	<div class="confirm_box plugs_error" style="display: none;">
		<div class="entity">
			<div class="img">
				<img src="${ctxStaticFront}/images/hfmx/plugs_error.png"/>
			</div>
			<div class="tit" id="errorTit"></div>
			<div class="desc" id="errorDesc"></div>
			<div class="btns">
				<button type="button" class="btn small form_error" fun_type="confirm">确认</button>
			</div>
		</div>
	</div>
	<div class="confirm_box plugs_right" style="display: none;">
		<div class="entity">
			<div class="img">
				<img src="${ctxStaticFront}/images/hfmx/plugs_right.png"/>
			</div>
			<div class="tit" id="rightTit"></div>
			<div class="desc" id="rightDesc"></div>
			<div class="btns">
				<button type="button" class="btn small form_theme" fun_type="confirm">确认</button>
			</div>
		</div>
	</div>
	<link rel="stylesheet" href="${ctxStaticFront}/plugins/My97DatePicker/skin/WdatePicker.css">
	<script src="${ctxStaticFront}/plugins/My97DatePicker/WdatePicker.js"></script>
		
	<script type="text/javascript" src="${ctxStaticFront}/plugins/select2/select2.min.js"></script>
	<script type="text/javascript" src="${ctxStaticFront}/js/jquery-validate/jquery.validate.js"></script>
	<script src="${ctxStaticFront}/plugins/jQuery-Timepicker/jquery-ui-timepicker-addon.js"></script>
	<script src="${ctxStaticFront}/plugins/jQuery-Timepicker/jquery.ui.datepicker-zh-CN.js.js"></script>
	<script src="${ctxStaticFront}/plugins/jQuery-Timepicker/jquery-ui-timepicker-zh-CN.js"></script>
	<script type="text/javascript">
		
		$(function(){
			//清空所有的tip
			$('.select').select2();
			/* $( ".datepicker" ).datepicker();
			$( ".datetimepicker" ).datetimepicker({
				showSecond: false,
				timeFormat: 'HH:mm:ss'
			}); */
			$(".base_form").validate();
		});
		function logout(){
			$(".plugs_tip #tipTit").html("提示");
    		$(".plugs_tip #tipDesc").html("确认退出登录吗？");
    		$('.plugs_tip').comfirmbox({
				confirmCal: function(e){
					
					e.close(); // 关闭输入框
					window.location.href = '${ctx}/logout';
				},
				cancelCal: function(e){
			
					e.close(); // 关闭输入框
				}
			});
		}
	</script>
	
	<script language="javascript">
		//获取通知数目  
		function getNotifyNum(){
			$.get("${ctx}/hf/apply/self/count?updateSession=0&t="+new Date().getTime(),function(data){
				//alert(num);
				var num = parseFloat(data);
				if (num > 0){
					$("#notifyNum").show().html(num);
				}else{
					$("#notifyNum").hide();
					$("#notifyNum").html(0);
				}
			});
		}
		/* getNotifyNum(); 
	
		<c:set var="oaNotifyRemindInterval" value="${fns:getConfig('oa.notify.remind.interval')}"/>
		<c:if test="${oaNotifyRemindInterval ne '' && oaNotifyRemindInterval ne '0'}">
		     setInterval(getNotifyNum, ${oaNotifyRemindInterval}); 
		</c:if> */

		function openFormBox($t){
			var h = $t.find('.entity').actual('height');
			$t.find('.entity').css('margin-top',-h/2+'px');
			$t.fadeIn();
		}
	</script>

</body>
</html>