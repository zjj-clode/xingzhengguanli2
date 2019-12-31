<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>估分查询记录管理<small>估分查询记录信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/common/gfcxJl/">估分查询记录管理</a></li>
        <li class="active">估分查询记录<shiro:hasPermission name="common:gfcxJl:edit">${not empty gfcxJl.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="common:gfcxJl:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="gfcxJl" action="${ctx}/common/gfcxJl/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<%-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					省份名称：</label>
					
					<div class="col-sm-3">
						<form:select path="ssmc" class="form-control ">
							<form:option value="" label="=== 请选择省份名称 ==="/>
							<form:options items="${fns:getDictList('ssmc')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div> --%>
				<div class="form-group"><label class="col-sm-1 control-label">
				<!-- <span class="text-red fm">*</span> -->省份名称：</label>
				<div class="col-sm-3">
				<form:select path="ssmc" class="form-control select2">
					<form:option value="" label="=== 请选择名称 ==="/>
					<c:forEach items="${fns:getDictList('ssmc')}" var="ssmc1">
						<c:if test="${empty zsSsgrade.id}">
							<form:option value="${ssmc1.label}" label="${ssmc1.label}"/>
						</c:if>
						<c:if test="${not empty zsSsgrade.id}">
						<c:if test="${fnc:getZhSsmc(zsSsgrade.ssmc) eq ssmc1.label}">
							<form:option value="${ssmc1.label}" label="${ssmc1.label}" selected="selected"/>
						</c:if>
						<c:if test="${fnc:getZhSsmc(zsSsgrade.ssmc) != ssmc1.label}">
							<form:option value="${ssmc1.label}" label="${ssmc1.label}"/>
						</c:if>
						</c:if>
				    </c:forEach>
				</form:select>
				</div>
			</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					科类名称：</label>
					
					<div class="col-sm-3">
						<form:select path="klmc" class="form-control ">
							<form:option value="" label="=== 请选择科类名称 ==="/>
							<form:options items="${fns:getDictList('kl')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					估分查询类型：</label>
					
					<div class="col-sm-3">
						<form:select path="gfcxType" class="form-control " onchange="lxchange($(this).val());">
							<form:option value="" label="=== 请选查询类型 ==="/>
							<form:options items="${fns:getDictList('gfcx_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				<%-- <c:if test="${not empty zdx && not empty cj && empty pm }"> --%>
				<div class="form-group" id="szdx">
					
					<label class="col-sm-1 control-label">
					重点线：</label>
					
					<div class="col-sm-3">
						<form:input path="zdx" htmlEscape="false" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group" id="khcj">
					
					<label class="col-sm-1 control-label">
					成绩：</label>
					
					<div class="col-sm-3">
						<form:input path="cj" htmlEscape="false" class="form-control "/>
					
					</div>
				</div>
				<%-- </c:if> --%>
				<%-- <c:if test="${ empty zdx && empty cj && not empty pm }"> --%>
				<div class="form-group" id="khpc">
					
					<label class="col-sm-1 control-label">
					排名：</label>
					
					<div class="col-sm-3">
						<form:input path="pm" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
			<%-- 	</c:if> --%>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					查询人ip：</label>
					
					<div class="col-sm-3">
						<form:input path="ip" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					访问设备：</label>
					
					<div class="col-sm-3">
						<form:select path="device" class="form-control ">
							<form:option value="" label="=== 请选择访问设备 ==="/>
							<form:options items="${fns:getDictList('device_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					
					</div>
				</div>
				
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="common:gfcxJl:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
					</div>
				</div>
			</form:form>
	      </div>
     </div>
   </section>
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script>

$(document).ready( function(){
	if($('#gfcxType').val() == '1'){
		$('#khpc').hide();
		$('#szdx').show();
		$('#khcj').show();
	}else if($('#gfcxType').val() == '2'){
		$('#khpc').show();
		$('#szdx').hide();
		$('#khcj').hide();
	}else{
		$('#khpc').show();
		$('#szdx').show();
		$('#khcj').show();
	
	}
});

function lxchange(lx){
	if(lx == '1'){
	$('#khpc').hide();
	$('#szdx').show();
	$('#khcj').show();
	}else if(lx == '2'){
		$('#khpc').show();
		$('#szdx').hide();
		$('#khcj').hide();
	}


}
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