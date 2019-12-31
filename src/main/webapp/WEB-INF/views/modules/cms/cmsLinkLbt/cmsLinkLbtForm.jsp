<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>图片管理<small>图片信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/cms/cmsLinkLbt/">图片管理</a></li>
        <li class="active">图片<shiro:hasPermission name="cms:cmsLinkLbt:edit">${not empty cmsLinkLbt.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:cmsLinkLbt:edit">查看</shiro:lacksPermission></li>
      </ol>
    </section>
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="cmsLinkLbt" action="${ctx}/cms/cmsLinkLbt/save" method="post" class="form-horizontal form-information form-lie">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>
				<div class="box-body pad">
					<div class="form-group">
						<label for="" class="col-sm-1 control-label"><span class="text-red fm">*</span> 图片标题：</label>
						<div class="col-sm-4">
							<form:input path="title" htmlEscape="false" maxlength="200" class="form-control required " id="cp3text"/>
						</div>
					</div>
			       
			       <div class="form-group">
						<label class="col-sm-1 control-label">轮播图片:</label>
						<div class="col-sm-4">
			                <input type="hidden" id="image" name="image" value="${cmsLinkLbt.image}" />
							<sys:ckfinder input="image" type="images" uploadPath="/cms/images" selectMultiple="false"/>
						</div>
					</div>
					
					   <div  class="form-group" >
			            <label class="col-sm-1 control-label">链接地址:</label>
			            <div class="col-sm-4">
			                <form:input path="href" htmlEscape="false" maxlength="200" class="form-control"/>
			                     
			            </div>
			        </div>
			        
					<div class="form-group">
						<label class="col-sm-1 control-label">权重:</label>
						<div class="col-sm-1">
							<form:input path="weight" id="weight" htmlEscape="false" maxlength="200" class="form-control required"/>
					    </div>
		                <div class="col-sm-3 zdInn">
		                    <span class="zdtxt">
		                      <label>
		                        <input type="checkbox" id="weightTop" class="chbox" onclick="$('#weight').val(this.checked?'999':'0')">置顶
		                      </label>
		                      <label for="">过期时间：</label>
		                    </span>
					
							<div class="col-sm-3">
								<div class="input-group date">
						          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
						          <input name="weightDate" type="text" style="width: 230px" maxlength="20" class="form-control "
									value="<fmt:formatDate value="${cmsLinkLbt.weightDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
						        </div>
					</div>
				</div>
		                 </div>
		                 <div class="col-sm-5 text-muted">权重数值越大排序越靠前，过期时间可为空，过期后取消置顶。</div>
		            </div>
				
					<div class="form-group">
					    <div class="col-sm-offset-1 col-sm-11">
						    <shiro:hasPermission name="cms:link:edit"><input id="btnSubmit" class="btn btn-primary" style="width:90px;margin-right:10px" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
						    <input id="btnCancel" class="btn btn-default" type="button" value="返 回" onclick="history.go(-1)"/>
						</div>
					</div>
				</div>
			</form:form>
	      </div>
     </div>
   </section>
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script type="text/javascript">
$(function () {
	  $('.gqDatepicker').datepicker({
	      autoclose: true,
	      language:'zh-CN'
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