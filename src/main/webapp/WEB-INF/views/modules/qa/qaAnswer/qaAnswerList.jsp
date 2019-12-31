<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<div id="importBox" class="hide">
        <form id="importForm" action="${ctx}/qa/qaAnswer/import" method="post" enctype="multipart/form-data" class="form-horizontal form-information" onsubmit="loading('正在导入，请稍等...');">
		  <div class="confirmWap">
		    <div class="row" style="padding-top:10px">
		      <div class="col-md-12">
		        <div class="form-group">
		          <label for="" class="col-sm-2 control-label">选择文件</label>
		          <div class="col-sm-3">
		            <div class="pull-left upfileInn">
			          <button type="button" class="btn btn-primary">点击这里上传文件</button>
			          <input type="file" class="upfileInput" id="uploadFile" name="file" onchange="showFileName(this)" >
			        </div>
			      </div>
			      <div class="col-sm-5">
			        <p class="help-block pull-left text-red fileName">5MB内的“xls”或“xlsx”文件</p>
			      </div>
			      <div class="col-sm-2">
			        <p class="help-block pull-left text-red"><a href="${ctx}/qa/qaAnswer/import/template">下载模板</a></p>
			      </div>
		        </div>
		        <div class="form-group">
		          <label for="" class="col-sm-2 control-label"></label>
		          <div class="col-sm-10 radio">
		              <input class="btn btn-primary" type="submit" value="   导    入   "/>
		              <input id="btnCancel" class="btn" type="button" value="取消" onclick="parent.jBox.close();"/>
		          </div>
		        </div>
		      </div>
		    </div>
		  </div>
		</form>
	</div>
	<section class="content-header">
      <h1>在线咨询回复管理<small>在线咨询回复信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/qa/qaAnswer/">在线咨询回复信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    
    <form:form id="searchForm" modelAttribute="qaAnswer" action="${ctx}/qa/qaAnswer/" method="post" class="form-inline form-filter" role="form">
    	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
			<div class="form-group"><label>所属问题：</label>
				<form:input path="question.id" htmlEscape="false" maxlength="64" class="form-control"/>
			</div>
			<div class="form-group"><label>回复人：</label>
				<form:input path="responder.id" htmlEscape="false" maxlength="64" class="form-control"/>
			</div>
			<div class="form-group"><label>回复时间：</label>
				<div class="input-group date">
		          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
		          <input name="beginRespTime" type="text" style="width: 130px" maxlength="20" class="form-control pull-right"
					value="<fmt:formatDate value="${qaAnswer.beginRespTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
		        </div>  - 
		        <div class="input-group date">
		          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
		          <input name="endRespTime" type="text" style="width: 130px" maxlength="20" class="form-control pull-right"
					value="<fmt:formatDate value="${qaAnswer.endRespTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
		        </div>
			</div>
			<div class="form-group"><label>信息状态：</label>
				<form:select path="state" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="form-group"><label>是否公开：</label>
				<form:select path="isopen" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="form-group"><label>点赞次数：</label>
				<form:input path="liketimes" htmlEscape="false" maxlength="255" class="form-control"/>
			</div>
			<div class="form-group"><label>是否被索引：</label>
				<form:select path="hasindex" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
    <section class="content">
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          	<shiro:hasPermission name="qa:qaAnswer:delete">
          	<button id="btnBatchDelete" type="button" class="btn btn-danger pull-right"><i class="fa fa-close" aria-hidden="true"></i> 批量删除</button>
			</shiro:hasPermission>
          	<button type="button" class="btn btn-success pull-right dcModal" data-toggle="modal"><i class="fa fa-download" aria-hidden="true"></i> 批量导出</button>
          	<button type="button" class="btn btn-success pull-right drModal"><i class="fa fa-upload" aria-hidden="true"></i> 批量导入</button>
          	<shiro:hasPermission name="qa:qaAnswer:edit">
          	<a href="${ctx}/qa/qaAnswer/form"><button type="button" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 添加在线咨询回复</button></a>
          	</shiro:hasPermission>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <shiro:hasPermission name="qa:qaAnswer:delete">
	            <form action="${ctx}/qa/qaAnswer/deleteBatch?queryString=${qaAnswer.queryString}" method="post" id="batchDeleteForm">
	            </shiro:hasPermission>
	            <table class="ui celled table_semantic" id="qaAnswerListTable">
				<thead>
					<tr>
						<shiro:hasPermission name="qa:qaAnswer:delete">
						<th width="20" class="checkboxTh"><input type="checkbox"></th>
						</shiro:hasPermission>
						<th class="sort-column a.question_id">所属问题</th>
						<th class="sort-column a.responder_id">回复人</th>
						<th class="sort-column a.resp_content">回复内容</th>
						<th class="sort-column a.resp_time">回复时间</th>
						<th class="sort-column a.resp_ip">回复ip</th>
						<th class="sort-column a.state">信息状态</th>
						<th class="sort-column a.isopen">是否公开</th>
						<th class="sort-column a.liketimes">点赞次数</th>
						<th class="sort-column a.hasindex">是否被索引</th>
						<shiro:hasAnyPermissions name="qa:qaAnswer:edit,qa:qaAnswer:delete"><th>操作</th></shiro:hasAnyPermissions>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="item">
					<tr>
						<shiro:hasPermission name="qa:qaAnswer:delete"><td><input type="checkbox" name="ids" value="${item.id}"></td></shiro:hasPermission>
								<td>
									${item.question.id}
								</td>
								<td>
									${item.responder.id}
								</td>
								<td>
									${item.respContent}
								</td>
								<td>
									<fmt:formatDate value="${item.respTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
								<td>
									${item.respIp}
								</td>
								<td>
									${fns:getDictLabel(item.state, 'yes_no', '')}
								</td>
								<td>
									${fns:getDictLabel(item.isopen, 'yes_no', '')}
								</td>
								<td>
									${item.liketimes}
								</td>
								<td>
									${fns:getDictLabel(item.hasindex, 'yes_no', '')}
								</td>
						<shiro:hasAnyPermissions name="qa:qaAnswer:edit,qa:qaAnswer:delete">
						<td class="tablebox-handle">
		    				<shiro:hasPermission name="qa:qaAnswer:edit">
		    				<a href="${ctx}/qa/qaAnswer/form?id=${item.id}&queryString=${qaAnswer.queryString}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
							</shiro:hasPermission>
							<shiro:hasPermission name="qa:qaAnswer:delete">
							<a href="${ctx}/qa/qaAnswer/delete?id=${item.id}&queryString=${qaAnswer.queryString}" onclick="return confirmx('确认要删除该在线咨询回复吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
							</shiro:hasPermission>
						</td>
						</shiro:hasAnyPermissions>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<shiro:hasPermission name="qa:qaAnswer:delete"></form></shiro:hasPermission>
              <!-- 分页 -->
              <div class="row">
                ${page}
              </div>
          </div>
      </div>
    </section>
    <%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
    //Initialize Select2 Elements
    $(".select2").select2();
    // 按钮鼠标停留效果
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
    // 数据表格功能

	$("#btnExport").click(function(){
		top.$.jBox.confirm("确认要导出在线咨询回复吗？","系统提示",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/qa/qaAnswer/export");
			   $("#searchForm").submit();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	
	// 批量导入弹框
    $(".drModal").bind("click",function(){
      $.jBox($("#importBox").html() ,{
        title: "批量导入",
        buttons:{}
      });
    });

	
	// 导出数据弹框
    var dcModalCont = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要导出在线咨询回复吗？</p>';
    $('.dcModal').bind('click',function(){
		top.$.jBox.confirm(dcModalCont,"操作确认",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/qa/qaAnswer/export");
			   $("#searchForm").submit();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	
 // 批量删除
    //iCheck for checkbox and radio inputs
  	$('.simpleTable input[type="checkbox"]').iCheck({
  		checkboxClass : 'icheckbox_flat-blue',
  		radioClass : 'iradio_flat-blue'
  	});

  	// ie8下全选兼容
  	if (navigator.userAgent.indexOf("MSIE") > 0) {
  		if (navigator.userAgent.indexOf("MSIE 8.0") > 0
  				&& !window.innerWidth) {
  			//Enable check and uncheck all functionality
  			$(".checkboxTh")
  					.click(
  							function() {
  								var checkdiv = $(this)
  										.find('div');
  								var clicks = checkdiv
  										.hasClass('checked');
  								var thisTable = $(this)
  										.parents(
  												'.dataTables_scroll')
  										.find(
  												'.dataTables_scrollBody table');
  								if (!clicks) {
  									//Uncheck all checkboxes
  									thisTable
  											.find(
  													"input[type='checkbox']")
  											.iCheck(
  													"check");
  									checkdiv
  											.addClass('checked');
  									// checkdiv.attr('aria-checked',true);
  								} else {
  									//Check all checkboxes
  									thisTable
  											.find(
  													"input[type='checkbox']")
  											.iCheck(
  													"uncheck");
  									checkdiv
  											.removeClass('checked');
  									// checkdiv.attr('aria-checked',false);
  								}
  							});
  		}
  	}

  	//Enable check and uncheck all functionality
  	$(".checkboxTh ins").click(
  			function() {
  				var clicks = $(this).parent('div')
  						.hasClass('checked');
  				if (clicks) {
  					//Uncheck all checkboxes
  					$("input[type=checkbox][name=ids]")
  							.iCheck("check");
  				} else {
  					//Check all checkboxes
  					$("input[type=checkbox][name=ids]")
  							.iCheck("uncheck");
  				}
  			});
	
});
function page(n,s){
	$("#pageNo").val(n);
	$("#searchForm").attr("action","${ctx}/qa/qaAnswer/");
	$("#pageSize").val(s);
	$("#searchForm").submit();
   	return false;
}
function showFileName(obj){
    var fileName = obj.value;//C:\fakepath\学生信息模板.xlsx
    var lastIndex = fileName.lastIndexOf("\\"); //'\'最后一次出现的索引
    $(".fileName").text(fileName.substring(lastIndex+1));
}
</script>
</body>
</html>