<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
	<script type="text/javascript" src="${ctxStaticLTE}/plugins/ckeditor/ckeditor.js"></script>
</head>
<body>
	<section class="content-header">
      <h1>首页联系方式和页面备注管理<small>首页联系方式和页面备注信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/common/infoAndRemark/">首页联系方式和页面备注管理</a></li>
        <li class="active">首页联系方式和页面备注<shiro:hasPermission name="common:infoAndRemark:edit">${not empty infoAndRemark.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="common:infoAndRemark:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="infoAndRemark" action="${ctx}/common/infoAndRemark/save" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					邮编：</label>
					
					<div class="col-sm-3">
						<form:input path="postcode" htmlEscape="false" maxlength="50" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					邮箱：</label>
					
					<div class="col-sm-3">
						<form:input path="email" htmlEscape="false" maxlength="50" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					电话1：</label>
					
					<div class="col-sm-3">
						<form:input path="phone1" htmlEscape="false" maxlength="50" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					电话2：</label>
					
					<div class="col-sm-3">
						<form:input path="phone2" htmlEscape="false" maxlength="50" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					传真：</label>
					
					<div class="col-sm-3">
						<form:input path="fax" htmlEscape="false" maxlength="50" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					地址：</label>
					
					<div class="col-sm-3">
						<form:input path="address" htmlEscape="false" maxlength="50" class="form-control "/>
					
					</div>
				</div>
				<hr style="width:85%;">
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					录取状态图表提示：</label>
					
					<div class="col-sm-6">
						<form:textarea path="remark2" htmlEscape="false" maxlength="500" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					录取查询备注：</label>
					
					<div class="col-sm-3">
						<form:input path="remark1" htmlEscape="false" maxlength="60" class="form-control "/>
					</div>
				</div>
				<%-- <div class="form-group">
					
					<label class="col-sm-1 control-label">
					备注3：</label>
					
					<div class="col-sm-3">
						<form:input path="remark3" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					备注4：</label>
					
					<div class="col-sm-3">
						<form:input path="remark4" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					备注5：</label>
					
					<div class="col-sm-3">
						<form:input path="remark5" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div>
				<div class="form-group">
					
					<label class="col-sm-1 control-label">
					备注6：</label>
					
					<div class="col-sm-3">
						<form:input path="remark6" htmlEscape="false" maxlength="255" class="form-control "/>
					
					</div>
				</div> --%>
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<shiro:hasPermission name="common:infoAndRemark:edit"><input id="btnSubmit" class="btn btn-primary" type="button" value="保 存"/>&nbsp;</shiro:hasPermission>
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
	   
	  CKEDITOR.replace('remark1', { height: '140px', width: '452px',
          toolbar :[     
                    ['Bold' , 'NumberedList', 'BulletedList', '-', 'Link'],
                ],
          }
      );
	  
	  
	  CKEDITOR.instances.remark1.setData($("#remark1").val()); 
	  
      
      $("#btnSubmit").click(function(){
	      var text1 = CKEDITOR.instances.remark1.getData(); 
    	  $("#remark1").val(text1);
    	  $("#inputForm").submit();
      });
      
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