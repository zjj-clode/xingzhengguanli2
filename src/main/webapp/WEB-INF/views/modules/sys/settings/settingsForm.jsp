<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%request.setAttribute("enterCode", "\n") ;%>
<html>
<head>
<title>运行参数管理</title>
<meta name="decorator" content="lte" />
<style type="text/css">
span.select2-container--disabled {
	display: none;
}
</style>
</head>
<body>
	<section class="content-header">
		<h1>
			运行参数管理
		</h1>
		<ol class="breadcrumb">
			<li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
			<li><a href="${ctx}">系统设置</a></li>
			<li class="active">运行参数管理</li>
		</ol>
	</section>
	<div style="margin-top: 20px;margin-left: 15px;">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/settings/?${fns:getBase64DecodedQueryString(settings.queryString)}">运行参数列表</a></li>
			<li class="active">
			
				<c:choose>
					<c:when test="${empty settings.id}">
						<shiro:hasPermission name="sys:settings:edit">
						<a href="${ctx}/sys/settings/form?queryString=${settings.queryString}">运行参数添加</a>
						</shiro:hasPermission>
					</c:when>
					<c:otherwise>
						<shiro:hasAnyPermissions name="sys:settings:edit,sys:settings:set">
						<a href="${ctx}/sys/settings/form?id=${settings.id}&queryString=${settings.queryString}">运行参数修改</a>
						</shiro:hasAnyPermissions>
					</c:otherwise>
				</c:choose>
			</li>
			<%-- <shiro:hasPermission name="sys:settings:edit">
				<li><a href="${ctx}/sys/settings/setForm">运行参数配置</a></li>
			</shiro:hasPermission> --%>
		</ul>
		<br />
	</div>
	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<form:form id="inputForm" modelAttribute="settings" action="${ctx}/sys/settings/save" method="post" class="form-horizontal form-information pad  form-lie">
					<form:hidden path="id" />
					<form:hidden path="queryString" />
					<sys:message content="${message}" />

					<shiro:hasPermission name="sys:settings:edit">
						<div class="form-group">
							<label class="col-sm-1 control-label"><span class="text-red fm">*</span>参数名:</label>
							<div class="col-sm-3">
								<form:input path="name" htmlEscape="false" maxlength="100" class="form-control required" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-1 control-label"><span class="text-red fm">*</span>参数键:</label>
							<div class="col-sm-3">
								<form:input path="key" htmlEscape="false" maxlength="100" class="form-control required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-1 control-label"><span class="text-red fm">*</span>数据类型:</label>
							<div class="col-sm-3">
								<form:select path="dataType" class="form-control select2">
									<form:options items="${fns:getDictList('sys_setttings_data_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
								</form:select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-1 control-label"><span class="text-red fm">*</span>参数值:</label>
							<div class="col-sm-3">
								<c:choose>
									<c:when test="${not empty settings.id}">
										
										<c:choose>
											<c:when test="${settings.dataType eq 'Long' or settings.dataType eq 'Double'}">
												<form:input path="value" htmlEscape="false" maxlength="100" class="form-control settings_value_text settings_value required" cssStyle="" />
											</c:when>
											<c:when test="${settings.dataType eq 'String'}">
												<c:set var="rows" value="${fn:length(fn:split(settings.value,enterCode))}"/>
												<form:textarea path="value" htmlEscape="false" rows="${rows gt 5 ? rows : 5}" maxlength="1000" class="form-control settings_value_textarea settings_value required" />
											</c:when>
											<c:when test="${settings.dataType eq 'Boolean'}">
												<select name="value" data-id="${settings.id}" class="form-control select2 settings_value settings_value_select" style="">
													<option value="">请选择 ===</option>
													<option value="true" ${'Boolean' eq  settings.dataType and settings.value eq 'true' ? 'selected="selected"' : ''}>是</option>
													<option value="false" ${'Boolean' eq  settings.dataType and settings.value eq 'false' ? 'selected="selected"' : ''}>否</option>
												</select>
											</c:when>
											<c:when test="${settings.dataType eq 'Date'}">
												<input name="value" type="text" readonly="readonly" maxlength="20" data-id="${settings.id}" class="form-control settings_value settings_value_date "
													value="${settings.value}"
													onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true,onpicked:function(dp){updateValue($(this).attr('data-id'),dp.cal.getNewDateStr())}});" />
											</c:when>
											<c:otherwise>
											
											</c:otherwise>
										</c:choose>
										 
									</c:when>
									
									<c:otherwise>
										<form:input path="value" htmlEscape="false" maxlength="100" class="form-control settings_value_text settings_value required" cssStyle="" />
										<select name="value" data-id="${settings.id}" class="form-control select2 settings_value_select settings_value" style="display:none;">
											<option value="true" ${settings.value eq 'true' ? 'selected="selected"' : ''}>是</option>
											<option value="false" ${settings.value eq 'false' ? 'selected="selected"' : ''}>否</option>
										</select>
										<input name="value" type="text" readonly="readonly" maxlength="20" data-id="${settings.id}" class="form-control settings_value settings_value_date " 
											value="${settings.value}"
											onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true,onpicked:function(dp){updateValue($(this).attr('data-id'),dp.cal.getNewDateStr())}});" style="display:none;" />
										<form:textarea path="value" htmlEscape="false" rows="5" maxlength="1000" class="form-control settings_value_textarea settings_value required" cssStyle="display:none;"/>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-1 control-label"><span class="text-red fm">*</span>参数级别:</label>
							<div class="col-sm-3">
								<form:select path="systemDefined" class="form-control select2 required">
									<%-- <form:option value="" label=" == 请选择 == " /> --%>
									<form:option value="1" label="系统级参数" />
									<form:option value="0" label="用户级参数" />
								</form:select>
							</div>
							<div class="col-sm-7 text-muted"> 系统级参数一般由系统管理员管理， 用户级参数可以交由招办老师账号进行管理。</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-1 control-label">备注:</label>
							<div class="col-sm-4">
								<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control" />
							</div>
						</div>
						
					</shiro:hasPermission>
					
					
					<shiro:lacksPermission name="sys:settings:edit">
						<div class="form-group">
							<label class="col-sm-1 control-label"> 参数名:</label>
							<div class="col-sm-3 control-label" style="text-align:left;">
								${settings.name}
							</div>
						</div>
						<%-- 
						<div class="form-group">
							<label class="col-sm-1 control-label"> 参数键:</label>
							<div class="col-sm-3 control-label" style="text-align:left;">
								${settings.key}
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-1 control-label"> 数据类型:</label>
							<div class="col-sm-3 control-label" style="text-align:left;">
								${fns:getDictLabel(settings.dataType, 'sys_setttings_data_type', '')}
							</div> 
						</div> 
						--%>
						<div class="form-group">
							<label class="col-sm-1 control-label"><span class="text-red fm">*</span>参数值:</label>
							<div class="col-sm-3">
								<c:choose>
									<c:when test="${not empty settings.id}">
										<c:choose>
											<c:when test="${settings.dataType eq 'Long' or settings.dataType eq 'Double'}">
												<form:input path="value" htmlEscape="false" maxlength="100" class="form-control settings_value_text settings_value required" cssStyle="" />
											</c:when>
											<c:when test="${settings.dataType eq 'String'}">
												<c:set var="rows" value="${fn:length(fn:split(settings.value,enterCode))}"/>
												<form:textarea path="value" htmlEscape="false" rows="${rows gt 5 ? rows : 5}" maxlength="1000" class="form-control settings_value_textarea settings_value required" />
											</c:when>
											<c:when test="${settings.dataType eq 'Boolean'}">
												<select name="value" data-id="${settings.id}" class="form-control select2 settings_value settings_value_select required" style="">
													<option value="">请选择 ===</option>
													<option value="true" ${'Boolean' eq  settings.dataType and settings.value eq 'true' ? 'selected="selected"' : ''}>是</option>
													<option value="false" ${'Boolean' eq  settings.dataType and settings.value eq 'false' ? 'selected="selected"' : ''}>否</option>
												</select>
											</c:when>
											<c:when test="${settings.dataType eq 'Date'}">
												<input name="value" type="text" readonly="readonly" maxlength="20" data-id="${settings.id}" class="form-control settings_value settings_value_date required"
													value="${'Date' eq  settings.dataType ? settings.value : ''}" style="background-color:#fff;"
													onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false,onpicked:function(dp){updateValue($(this).attr('data-id'),dp.cal.getNewDateStr())}});" />
											</c:when>
											<c:otherwise></c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-1 control-label">备注说明:</label>
							<div class="col-sm-4">
								<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control" disabled="true"/>
							</div>
						</div>
						
					</shiro:lacksPermission>
					
					
					
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-11">
							<shiro:hasAnyPermissions name="sys:settings:edit,sys:settings:set">
								<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;
							</shiro:hasAnyPermissions>
							<input id="btnCancel" class="btn btn-default" type="button" value="返 回" onclick="history.go(-1)" />
						</div>
					</div>
					
				</form:form>
			</div>
		</div>
	</section>
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			jQuery.validator.addMethod("Long", function(value, element) {
				return this.optional(element) || /^\d+$/.test(value);
			}, "请输入整数");
			jQuery.validator.addMethod("Double", function(value, element) {
				return this.optional(element) || /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value);
			}, "请输入数字");
			jQuery.validator.addMethod("String", function(value, element) {
				var length = $.isArray(value) ? value.length : this.getLength($.trim(value), element);
				return this.optional(element) || (length >= 1 && length <= 1000);
			}, "请输入1000以内的字符");
	
			$("select#dataType").change(function() {
				var v = $(this).val();
				$(".settings_value").removeClass("required");
				$(".settings_value").removeClass("error");
				$(".settings_value").removeClass("Long");
				$(".settings_value").removeClass("Double");
				$(".settings_value").removeClass("Boolean");
				$(".settings_value").removeClass("String");
				$(".settings_value").removeClass("Date");
				$(".settings_value").attr("disabled", "disabled");
				$(".settings_value").siblings("label.error").remove();
				$(".settings_value").hide();
				if ("Long" == v) {
					$(".settings_value_text").removeAttr("disabled");
					$(".settings_value_text").addClass("required");
					$(".settings_value_text").addClass(v);
					$(".settings_value_text").show();
				} else if ("Double" == v) {
					$(".settings_value_text").removeAttr("disabled");
					$(".settings_value_text").addClass("required");
					$(".settings_value_text").addClass(v);
					$(".settings_value_text").show();
				} else if ("Boolean" == v) {
					$(".settings_value_select").removeAttr("disabled");
					$(".settings_value_select").addClass("required");
					$(".settings_value_text").addClass(v);
					$(".settings_value_select").show();
				} else if ("String" == v) {
					$(".settings_value_textarea").removeAttr("disabled");
					$(".settings_value_textarea").addClass("required");
					$(".settings_value_textarea").addClass(v);
					$(".settings_value_textarea").show();
				} else if ("Date" == v) {
					$(".settings_value_date").removeAttr("disabled");
					$(".settings_value_date").addClass("required");
					$(".settings_value_text").addClass(v);
					$(".settings_value_date").show();
				}
			}).change();
				
	
			$(".select2").select2();
			$('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
	
			var oldData = $("#inputForm").serialize();
			
			$("#inputForm").validate({
				submitHandler : function(form) {
					if(oldData == $("#inputForm").serialize()){
						top.$.jBox.tip("数据未改动，直接返回列表页面。","success");
						setTimeout(function() {
							window.location = '${ctx}/sys/settings/?${fns:getBase64DecodedQueryString(settings.queryString)}';
						}, 800);
					}else{
						loading('正在提交，请稍等...');
						form.submit();
					}
				},
				errorContainer : "#messageBox",
				errorPlacement : function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
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