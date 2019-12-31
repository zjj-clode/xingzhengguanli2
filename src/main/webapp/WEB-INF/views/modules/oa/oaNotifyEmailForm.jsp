<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
    <section class="content-header">
      <h1>
                            通知公告
        <small>通知添加与修改</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/oa/oaNotify">我的通告</a></li>
        <li class="active">添加与修改</li>
      </ol>
    </section>
    <!-- /.content-header -->
    
    <section class="content">
      <div class="row">
        <div class="col-md-12">
			<form:form id="inputForm" modelAttribute="oaNotify" action="${ctx}/oa/oaNotify/sendemail" method="post" class="form-horizontal form-information pad  form-lie">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>类型：</label>
					<div class="col-sm-3">
						<form:select path="type" class="form-control select2 required">
							<form:option value="4" label="企业通知"/>
						</form:select>
					</div>
				</div>	
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>标题：</label>
					<div class="col-sm-7">
						<form:input path="title" htmlEscape="false" maxlength="200" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-1 control-label"><span class="text-red fm">*</span>内容：</label>
					<div class="col-sm-7">
						<form:textarea id="content" htmlEscape="true" path="content" rows="4" class="form-control required" />
						<sys:ckeditor replace="content" uploadPath="/cms/oaNotify" />
					</div>
				</div>
				<c:if test="${oaNotify.status ne '1'}">
					<div class="form-group">
						<label class="col-sm-1 control-label">附件：</label>
						<div class="col-sm-3">
							<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="form-control"/>
							<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>状态：</label>
						<div class="col-sm-3 radio">
						    <c:forEach items="${fns:getDictList('oa_notify_status')}" var="dic" varStatus="status">
						     <label style="margin-right:10px">
						      <input type="radio" name="status" id="status" value="${dic.value}" <c:if test="${dic.value == oaNotify.status }">checked="true"</c:if>  htmlEscape="false" class="required">${dic.label}
							 </label>
						    </c:forEach>
						</div>
						<div class="col-sm-6 text-muted">发布后不能进行操作。</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>接受人：</label>
						<div class="col-sm-7">
			                <sys:treeselect id="oaNotifyRecord" name="oaNotifyRecordIds" value="${oaNotify.oaNotifyRecordIds}" labelName="oaNotifyRecordNames" labelValue="${oaNotify.oaNotifyRecordNames}"
								title="企业" url="/common/corporationinfo/treeData2" cssClass="form-control required" notAllowSelectParent="true" checked="true"/>
						</div>
					</div>
				</c:if>
				<c:if test="${oaNotify.status eq '1'}">
					<div class="form-group">
						<label class="col-sm-1 control-label">附件：</label>
						<div class="col-sm-3">
							<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="form-control"/>
							<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true" readonly="true" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">接受人：</label>
						<div class="col-sm-8">
							<table id="contentTable" class="table table-striped table-bordered table-condensed">
								<thead>
									<tr>
										<th>接受人</th>
										<th>接受部门</th>
										<th>阅读状态</th>
										<th>阅读时间</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${oaNotify.oaNotifyRecordList}" var="oaNotifyRecord">
									<tr>
										<td>
											${oaNotifyRecord.user.name}
										</td>
										<td>
											${oaNotifyRecord.user.office.name}
										</td>
										<td>
											${fns:getDictLabel(oaNotifyRecord.readFlag, 'oa_notify_read', '')}
										</td>
										<td>
											<fmt:formatDate value="${oaNotifyRecord.readDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
							已查阅：${oaNotify.readNum} &nbsp; 未查阅：${oaNotify.unReadNum} &nbsp; 总共：${oaNotify.readNum + oaNotify.unReadNum}
						</div>
					</div>
				</c:if>
				<div class="form-group">
				   <div class="col-sm-offset-1 col-sm-11">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="发送"/>&nbsp;
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				   </div>
				</div>
			</form:form>
		</div>
     </div>
   </section>
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script>
  $(function () {
       $(".select2").select2();
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
       
       $('#contentTable').DataTable({
 	      "ordering": false,
 	      "scrollY":  400,
 	      "scrollCollapse": true,
 	      "scrollX": true, 
 	      "info": false,
 	      "bFilter": false, 
 	      "paging": false
 	    });
  });
</script>
</body>
</html>