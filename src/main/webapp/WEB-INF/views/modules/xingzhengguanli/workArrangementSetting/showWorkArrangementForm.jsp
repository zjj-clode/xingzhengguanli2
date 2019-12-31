<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lteblank"/>
	<style type="text/css">
		#xcApplySubmitTable tr td{
		    word-break: break-all;
		    word-wrap: break-word;
		    white-space: pre-line;
		}
		#xcApplySubmitTable tr th{
		    background: #f9fafb;
		    border-top: 1px solid rgba(34,36,38,0.1);
		    padding: .92857143em .78571429em;
		    text-align:center;
		}
	</style>
</head>
<body>
	
	<section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="workArrangementSetting" method="post" class="form-horizontal form-information pad form-lie">
				<form:hidden path="id"/>
				
				<form:hidden path="queryString"/>
				<sys:message content="${message}"/>
				
				<div class="form-group">
					<div class="col-sm-10 " style="font-size: 20px;text-align:center;">${workArrangementSetting.department}每周工作安排</div>
				</div>
				<div class="form-group">
					<div class="col-sm-10 " style="font-size: 16px;text-align: right;">${workArrangementSetting.title}</div>
				</div>
				<div class="form-group">
					<div class="col-sm-10">
						<table class="ui celled table_semantic" id="xcApplySubmitTable">
							<tbody>
								<c:forEach items="${arrangmentList}" var="arrangment">
									<tr>
										<th>${arrangment.keshi}</th>
									</tr>
									<tr>
										<td>
										${arrangment.contentTitle}：<br/>	${arrangment.importantJob}<br>
										常规工作事项：<br/>${arrangment.otherJob}
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<input id="btnSubmit" onclick="downloadWorkArrangement('${workArrangementSetting.id}')" class="btn btn-primary" type="button" value="下载工作安排表"/>&nbsp;
					</div>
				</div>
			</form:form>
	      </div>
     </div>
   </section>
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script>
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
  function downloadWorkArrangement(id){
	  window.location.href = '${ctx}/xingzhengguanli/workArrangementSetting/downloadWorkArrangement?id='+id;
  }
</script>
</body>
</html>