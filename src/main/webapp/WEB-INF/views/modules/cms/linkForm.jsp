<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>链接管理</title>
	<meta name="decorator" content="lte"/>
	<style type="text/css">
    .form-information .btn-default {
      height: 34px;
      padding: 5px 12px;
      font-size: 14px;
    }
    .form-information .btn label{
      margin:0;
    }
    .form-information .chbox{
      margin:0;
      vertical-align: middle
    }
    .zdInn{
      position: relative;
      padding-left: 150px;
    }
    .zdInn .zdtxt{
      position: absolute;
      left:15px;
      top:7px;
    }
    .zdInn .zdtxt label{
      margin-right: 10px;
    }
  </style>
</head>
<body>
	 <section class="content-header">
      <h1>
                        链接管理
        <small>链接发布</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/cms/link">链接信息</a></li>
        <li class="active">链接发布</li>
      </ol>
    </section>
	    
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box box-info" style="padding-right:20px;padding-top:20px;">
			<form:form id="inputForm" modelAttribute="link" action="${ctx}/cms/link/save" method="post" class="form-horizontal form-information form-lie">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>
				<div class="box-body pad">
					<div class="form-group">
						<label class="col-sm-1 col-sm-1 control-label"><span class="text-red fm">*</span> 归属栏目:</label>
						<div class="col-sm-2">
			                <sys:treeselect id="category" name="category.id" value="${link.category.id}" labelName="category.name" labelValue="${link.category.name}"
								title="栏目" url="/cms/category/treeData" module="link" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="form-control required"/>&nbsp;
			             </div>
			             
					</div>
					<div class="form-group">
						<label for="" class="col-sm-1 control-label"><span class="text-red fm">*</span> 链接名称：</label>
						<div class="col-sm-4">
							<form:input path="title" htmlEscape="false" maxlength="200" class="form-control required has-ruler required" id="cp3text"  style="color: ${link.color}"/>
						</div>
						<div class="col-sm-1">
						    <input id="cp3" name="cp3" type="text" class="input_colorpicker" readonly="" style="background-color: rgb(0, 0, 0);z-index: 9999">
							<form:hidden path="color" htmlEscape="false" maxlength="200" class="form-control required has-ruler required" id="cp3textcolor"/>
						</div>
					</div>
			       
			       <div class="form-group">
						<label class="col-sm-1 control-label">缩略图:</label>
						<div class="col-sm-4">
			                <input type="hidden" id="image" name="image" value="${link.image}" />
							<sys:ckfinder input="image" type="images" uploadPath="/cms/link" selectMultiple="false"/>
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
		                    <div class="input-group date">
		                        <div class="input-group-addon">
		                           <i class="fa fa-calendar"></i>
		                        </div><%--
		                        <input id="weightDate" name="weightDate" type="text" readonly="readonly" maxlength="20" class="form-control pull-left gqDatepicker">
		                     --%><input id="weightDate" name="weightDate" type="text" readonly="readonly" style="width: 120px" maxlength="20" class="form-control "
									value="<fmt:formatDate value="${link.weightDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		                     
		                     </div>
		                 </div>
		                 <div class="col-sm-5 text-muted">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</div>
		            </div>
				
					
	
					<div class="form-group">
						<label class="col-sm-1 control-label">发布时间:</label>
						<div class="col-sm-3">
						    <div class="input-group date">
		                      <div class="input-group-addon">
		                        <i class="fa fa-calendar"></i>
		                      </div>
		                      <input id="createDate" name="createDate" type="text" style="width: 120px" maxlength="20" class="form-control "
									value="<fmt:formatDate value="${link.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		                      <%--<input type="text" id="createDate" name="createDate" value="<fmt:formatDate value="${link.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control pull-left" style="width:120px">
		                    --%></div>
						</div>
					</div>
				<shiro:hasPermission name="cms:link:audit">
						<div class="form-group">
							<label class="col-sm-1 control-label">发布状态:</label>
							<div class="col-sm-7 radio">
							    <c:forEach items="${fns:getDictList('cms_del_flag')}" var="dic" varStatus="status">
							     <label style="margin-right:10px">
							      <input type="radio" name="delFlag" id="delFlag" value="${dic.value}" <c:if test="${dic.value eq link.delFlag }"> checked</c:if>  htmlEscape="false" class="required">${dic.label}
								 </label>
							    </c:forEach>
							</div>
						</div>
					</shiro:hasPermission>
				
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
      </div>
    </section>
  <!-- /.content-wrapper -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script type="text/javascript">
	  $(function () {
	    // 按钮鼠标停留效果
       $('.form-information button').tooltip();
       $(".select2").select2();
        if($("#link").val()){
            $('#linkBody').show();
            $('#url').attr("checked", true);
        }
		$("#title").focus();
		$("#inputForm").validate({
			submitHandler: function(form){
                   if ($("#categoryId").val()==""){
                       $("#categoryName").focus();
                       top.$.jBox.tip('请选择归属栏目','warning');
                   }else if (CKEDITOR.instances.content.getData()=="" && $("#link").val().trim()==""){
                       top.$.jBox.tip('请填写正文','warning');
                   }else{
                       loading('正在提交，请稍等...');
                       form.submit();
                   }
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
		
        //颜色选择器
	    $('#cp3').colpick({
	      colorScheme:'dark',
	      layout:'rgbhex',
	      color:'000000',
	      onSubmit:function(hsb,hex,rgb,el) {
	        $(el).css('background-color', '#'+hex);
	        $(el).colpickHide();
	        $("#cp3text").css("color",'#'+hex);
            $("#cp3textcolor").val('#'+hex);
	      }
	    });
        $('.gqDatepicker').datepicker({
	      autoclose: true,
	      language:'zh-CN'
	    });
	    $('#createDate').datepicker({
	      autoclose: true,
	      language:'zh-CN',
	      format: 'yyyy-mm-dd hh:mm:ss'
	    });
	});
</script>
</body>
</html>