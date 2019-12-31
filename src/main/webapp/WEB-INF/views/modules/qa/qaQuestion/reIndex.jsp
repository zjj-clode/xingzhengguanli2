<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="lte" />
</head>
<body>
	<section class="content-header">
		<h1>
			索引管理<small>一键重建索引</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
			<li><a href="${ctx}/chat/chatCustom/">索引重建</a></li>
			<li class="active">一键重建</li>
		</ol>
	</section>
	<sys:message content="${message}" />

	<section class="content">
		<div class="row tableTopBtn">
			<div class="col-xs-12">
				<shiro:hasPermission name="qa:qaQuestion:edit">
					<a href="${ctx}/qa/qaQuestion/reIndex"><button id="reIndexButton" type="button" class="btn btn-info pull-right" onclick="disabledButton()">
							<i class="fa fa-plus" aria-hidden="true"></i>一键重建索引
						</button></a>
				</shiro:hasPermission>
			</div>
		</div>
	</section>
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			//Initialize Select2 Elements
			$(".select2").select2();
			// 按钮鼠标停留效果
			$('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
			// 数据表格功能
	
		});
		function disabledButton() {
			$("#reIndexButton").text("正在创建索引...");
			$("#reIndexButton").attr("disabled", true);
		}
	</script>
</body>
</html>