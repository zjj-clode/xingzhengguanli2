<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<title><sitemesh:title /> 招生宣传服务平台</title>
<link rel="stylesheet" href="${ctxStaticLTE}/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/font-awesome/font-awesome.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/ionicons/ionicons.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/select2/select2.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/My97DatePicker/skin/WdatePicker.css">
<%-- <link rel="stylesheet" href="${ctxStaticLTE}/plugins/datepicker/datepicker3.css"> --%>
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/iCheck/flat/blue.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.theme.default.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/dataTables.bootstrap.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/jquery.dataTables.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/dataTables.semanticui.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/semantic_table.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/extensions/FixedColumns/css/dataTables.fixedColumns.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/jBox/jBox.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/colorpicker/colpick.css">
<link rel="stylesheet" href="${ctxStaticLTE}/dist/css/AdminLTE.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/dist/css/skins/_all-skins.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/jquery-validation/1.11.1/jquery.validate.min.css">
<!--[if lt IE 9]>
  <script src="${ctxStaticLTE}/plugins/ie8/html5shiv.min.js"></script>
  <script src="${ctxStaticLTE}/plugins/ie8/respond.min.js"></script>
  <link rel="stylesheet" href="${ctxStaticLTE}/plugins/ie8/ie8-hack.css">
  <![endif]-->
<!--[if !IE]> -->
<script src="${ctxStatic}/jquery/j2.js"></script>
<!-- <![endif]-->
<!--[if IE]>
  	<script src="${ctxStatic}/jquery/j1.js"></script>
<![endif]-->
<script src="${ctxStaticLTE}/plugins/jBox/jBox.min.js"></script>
<script src="${ctxStatic}/common/batchDelete.js"></script>
<script src="${ctxStaticLTE}/common.js"></script>
<script type="text/javascript">var ctx = '${ctx}',
		ctxStatic = '${ctxStatic}',
		ctxStaticLTE = '${ctxStaticLTE}';
</script>
<sitemesh:head />

</head>
<body class="hold-transition skin-purple sidebar-mini" data-spy="scroll" data-target="#myScrollspy">
	<div class="wrapper">
		<header class="main-header">
			<a href="${ctx}" class="logo"> <span class="logo-mini"><b>C</b>I</span> <span class="logo-lg"><b>${fns:getConfig('productName')} </b></span>
			</a>
			<nav class="navbar navbar-static-top" role="navigation">
				<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"> <span class="sr-only">Toggle navigation</span>
				</a>
				<form class="navbar-form navbar-left" role="search">
					<div class="input-group">
						<input type="text" class="form-control" id="navbar-search-input" placeholder="全站搜索"> <span class="input-group-btn">
							<button type="submit" name="search" id="search-btn" class="btn btn-flat">
								<i class="fa fa-search"></i>
							</button>
						</span>
					</div>
				</form>
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<li class="dropdown messages-menu">
							<!--   <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-envelope"></i>
              <span class="label label-success">4</span>
            </a>-->
							<ul class="dropdown-menu">
								<li class="header">有<b class="text-red">4</b>条未读邮件
								</li>
								<li>
									<ul class="menu">
										<li><a href="#">
												<div class="pull-left">
													<c:choose>
														<c:when test="${not empty fns:getUser().photo}">
															<img src="${pageContext.request.contextPath}${fns:getUser().photo}" style="width:20px;height:20px;" class="img-circle">
														</c:when>
														<c:otherwise>
															<img src="${ctxStaticLTE}/dist/img/user2-160x160.jpg" style="width:20px;height:20px;" class="img-circle">
														</c:otherwise>
													</c:choose>
												</div>
												<h4>
													开始使用 window 10 <small><i class="fa fa-clock-o"></i> 5分钟前</small>
												</h4>
												<p>了解“入门”应用、相关支持及如何导航设备</p>
										</a></li>
									</ul>
								</li>
								<li class="footer"><a href="#">查看全部</a></li>
							</ul>
						</li>

						<!-- Notifications Menu -->
						<li class="dropdown notifications-menu">
							<!-- Menu toggle button --> <!--   <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-bell"></i>
              <span class="label label-warning">10</span>
            </a>-->
							<ul class="dropdown-menu">
								<li class="header">有<b class="text-red">10</b>条未读消息
								</li>
								<li>
									<!-- Inner Menu: contains the notifications -->
									<ul class="menu">
										<li>
											<!-- start notification --> <a href="#"> <i class="fa fa-warning text-yellow"></i> 你的答案已经被忽略，改进答案可以让对方重新评价 Ocrad.js 识别率还能否继续提高？
										</a>
										</li>
										<!-- end notification -->
									</ul>
								</li>
								<li class="footer"><a href="#">查看全部</a></li>
							</ul>
						</li>
						<!-- Tasks Menu -->
						<li class="dropdown tasks-menu">
							<!-- Menu Toggle Button --> <!--  <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-flag"></i>
              <span class="label label-danger">9</span>
            </a>-->
							<ul class="dropdown-menu">
								<li class="header">有<b class="text-red">9</b>个任务未完成
								</li>
								<li>
									<!-- Inner menu: contains the tasks -->
									<ul class="menu">
										<li>
											<!-- Task item --> <a href="#"> <!-- Task title and progress text -->
												<h3>
													设计页面按钮 <small class="pull-right">20%</small>
												</h3> <!-- The progress bar -->
												<div class="progress xs">
													<!-- Change the css width attribute to simulate progress -->
													<div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0"
														aria-valuemax="100">
														<span class="sr-only"> 完成度20%</span>
													</div>
												</div>
										</a>
										</li>
										<!-- end task item -->
									</ul>
								</li>
								<li class="footer"><a href="#">查看全部</a></li>
							</ul>
						</li>
						
						
						<!-- User Account Menu -->
						<shiro:hasPermission name="sys:userRunAs:runAs">
						<c:set var="hasRunAsPermission" value="true" />
					</shiro:hasPermission>

						<c:choose>
							<c:when test="${fns:getUser().admin or hasRunAsPermission}">
								<li class="dropdown user user-menu">
									<a href="#" class="dropdown-toggle" data-toggle="dropdown"> 
										<c:choose>
											<c:when test="${not empty fns:getUser().photo}">
												<img src="${pageContext.request.contextPath}${fns:getUser().photo}" style="width:20px;height:20px;" class="img-circle">
											</c:when>
											<c:otherwise>
												<img src="${ctxStaticLTE}/dist/img/user2-160x160.jpg" style="width:20px;height:20px;" class="img-circle">
											</c:otherwise>
										</c:choose> <span class="hidden-xs">${fns:getUser().name}</span>
									</a>
									
									<ul class="dropdown-menu">
										<shiro:hasPermission name="sys:userRunAs:runAs">
											<c:set var="hasRunAsPermission" value="true" />
										</shiro:hasPermission>
										<%-- <c:if test="${fns:getUser().admin or hasRunAsPermission}">
											<li class="user-footer"><a href="javascript:void(0);" onclick="runAs();"><i class="fa fa-user-circle" aria-hidden="true"></i> 模拟登录</a></li>
										</c:if> --%>
										<li class="user-footer"><a href="${ctx}/logout" onclick="return confirmx('确认要退出登录吗？', this.href)"><i class="fa fa-power-off" aria-hidden="true"></i> 退出系统</a></li>
									</ul>
								</li>
							</c:when>
							<c:otherwise>
								<li class="dropdown user user-menu">
									<a href="${ctx}/logout" onclick="return confirmx('确认要退出登录吗？', this.href)"><i class="fa fa-power-off" aria-hidden="true"></i> 退出系统</a>
								</li>
							</c:otherwise>
						</c:choose>
						
						<!-- Control Sidebar Toggle Button -->
						<!-- <li><a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a></li> -->
					</ul>
				</div>
			</nav>
		</header>
		<!-- Left side column. contains the logo and sidebar -->
		<aside class="main-sidebar">

			<!-- sidebar: style can be found in sidebar.less -->
			<section class="sidebar">

				<!-- Sidebar user panel (optional) -->
				<div class="user-panel">
					<div class="pull-left image">
						<c:choose>
							<c:when test="${not empty fns:getUser().photo}">
								<img src="${pageContext.request.contextPath}${fns:getUser().photo}" style="width:45px;height:45px;" class="img-circle" >
							</c:when>
							<c:otherwise>
								<img src="${ctxStaticLTE}/dist/img/user2-160x160.jpg" class="img-circle">
							</c:otherwise>
						</c:choose>
					</div>
					<div class="pull-left info">
						<c:set var="username" value="${fns:getUser().name }" />
						<p title="${fns:getUser().name}">${fns:abbr(username,15)}</p>
						<!-- Status -->
						<a href="javascript:vodi(0);"><i class="fa fa-circle text-success"></i> 在线</a>
					</div>
				</div>

				<!-- Sidebar Menu -->
				<%@include file="/WEB-INF/views/layouts/menuTree.jsp"%>
				<!-- /.sidebar-menu -->
			</section>

			<!-- /.sidebar -->
		</aside>
		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<sitemesh:body />
		</div>
		<!-- /.content-wrapper -->
		<!-- Main Footer -->
		<footer class="main-footer">
			<!-- To the right -->
			<div class="pull-right hidden-xs">
				<b>Version</b> ${fns:getSysConfigWithDefaultValue('version','1.0.0')}
			</div>
			<!-- Default to the left -->
			<strong>Copyright &copy; ${fns:getSysConfigWithDefaultValue('copyrightYear','2018')} <a target="_blank"
				href="http://${fns:getSysConfigWithDefaultValue('custmor.site','www.cloudintecom')}/">
					${fns:getSysConfigWithDefaultValue('copyrightUnit','云智大学')}</a></strong> 版权所有
		</footer>

		<!-- Control Sidebar -->
		<aside class="control-sidebar control-sidebar-dark">
			<!-- Create the tabs -->
			<!-- <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
      <li class="active"><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
      <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
    </ul> -->
			<!-- Tab panes -->
			<div class="tab-content">
				<div class="tab-pane active" id="control-sidebar-home-tab">
					<!-- <h3 class="control-sidebar-heading">Recent Activity</h3>
        <ul class="control-sidebar-menu">
          <li>
            <a href="javascript::;">
              <i class="menu-icon fa fa-birthday-cake bg-red"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>

                <p>Will be 23 on April 24th</p>
              </div>
            </a>
          </li>
        </ul> -->
				</div>
			</div>
		</aside>
		<div class="control-sidebar-bg"></div>
	</div>
	<c:if test="${'1' eq fns:getUser().id}">
		<script type="text/javascript">
			function runAs() {
				var html = "<div style='padding:10px 30px;'>模拟账号：<input type='text' id='loginName' name='loginName' style='width:350px;margin-left:10px;'/></div>";
				var submit = function(v, h, f) {
					if (f.loginName == '') {
						top.$.jBox.tip("请输入账号。", 'error', {
							focusId : "loginName"
						});
						return false;
					}
					new AjaxHelper({
						url : "${ctx}/sys/userRunAs?loginName=" + f.loginName,
						onSuccess : function(json) {
							//根据id关闭弹窗
							top.$.jBox.close("runAsBox");
							setTimeout(function() {
								window.location.href = '${ctx}';
							}, 500);
						}
					}).sendRequest();
					return false; //先不关闭
				};
				top.$.jBox(html, {
					id : "runAsBox",
					showIcon : true,
					icon : "info",
					title : "快速模拟其他用户登录",
					submit : submit
				});
			}
		</script>
	</c:if>
</body>
</html>
