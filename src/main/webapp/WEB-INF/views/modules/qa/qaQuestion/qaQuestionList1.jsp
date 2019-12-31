<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<div id="importBox" class="hide">
        <form id="importForm" action="${ctx}/qa/qaQuestion/import" method="post" enctype="multipart/form-data" class="form-horizontal form-information" onsubmit="loading('正在导入，请稍等...');">
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
			        <p class="help-block pull-left text-red"><a href="${ctx}/qa/qaQuestion/import/template">下载模板</a></p>
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
      <h1>在线咨询管理<small>在线咨询信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/qa/qaQuestion/list1">在线咨询信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    
    <form:form id="searchForm" modelAttribute="qaQuestion" action="${ctx}/qa/qaQuestion/list1" method="post" class="form-inline form-filter" role="form">
    	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
			<%-- <div class="form-group"><label>提问者：</label>
				<sys:treeselect id="user" name="user.id" value="${qaQuestion.user.id}" labelName="user.name" labelValue="${qaQuestion.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
			</div> --%>
			<div class="form-group"><label>提问者姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="form-control"/>
			</div>
			<div class="form-group"><label>提问者身份：</label>
				<form:select path="identify" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<%-- <form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/> --%>
					<c:forEach items="${fns:getDictList('sys_user_type')}" var="item">
						<c:if test="${item.value eq '3' or item.value eq '4' or item.value eq '5'}">
							<form:option value="${item.value}" label="${item.label}"/>
						</c:if>
					</c:forEach>
				</form:select>
			</div>
			<div class="form-group"><label>科类名称：</label>
				<form:select path="klmc" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('kl')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="form-group"><label>提问人省份：</label>
				<form:select path="province" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('ssmc')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="form-group"><label>是否回复：</label>
				<form:select path="state" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<%-- <div class="form-group"><label>提问人ip：</label>
				<form:input path="ip" htmlEscape="false" maxlength="255" class="form-control"/>
			</div> --%>
			<div class="form-group"><label>提&nbsp;问&nbsp;时&nbsp;间：</label>
				<div class="input-group date">
		          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
		          <input name="beginTime" type="text" style="width: 140px" maxlength="20" class="form-control pull-right"
					value="<fmt:formatDate value="${qaQuestion.beginTime}" pattern="yyyy-MM-dd HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:true});"/>
		        </div>  - 
		        <div class="input-group date">
		          <div class="input-group-addon"><i class="fa fa-calendar"></i></div>
		          <input name="endTime" type="text" style="width: 140px" maxlength="20" class="form-control pull-right"
					value="<fmt:formatDate value="${qaQuestion.endTime}" pattern="yyyy-MM-dd HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:true});"/>
		        </div>
			</div>
			<%-- <div class="form-group"><label>访问设备：</label>
				<form:select path="device" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('device_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div> --%>
			<%-- <div class="form-group"><label>是否回复：</label>
				<form:select path="state" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div> --%>
			<div class="form-group"><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;是否公开：</label>
				<form:select path="isopen" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="form-group"><label>已发送邮件：</label>
				<form:select path="hassendemail" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<%-- <div class="form-group"><label>是否被索引：</label>
				<form:select path="hasindex" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div> --%>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
    <section class="content">
      <div class="row tableTopBtn">
        <div class="col-xs-12">
            <a href="${ctx}/qa/qaQuestion"><button type="button" class="btn btn-info pull-right">切换到学生咨询页</button></a>
            <button id="notAllOpen" type="button" class="btn btn-success pull-right">全部不公开</button>
            <button id="allOpen" type="button" class="btn btn-success pull-right">全部公开</button>
            <shiro:hasPermission name="qa:qaQuestion:delete">
                <button id="btnBatchDeleteAll" type="button" class="btn btn-danger pull-right"><i class="fa fa-close" aria-hidden="true"></i> 全部删除</button>
            </shiro:hasPermission>
          	<shiro:hasPermission name="qa:qaQuestion:delete">
          	<button id="btnBatchDelete" type="button" class="btn btn-danger pull-right"><i class="fa fa-close" aria-hidden="true"></i> 批量删除</button>
			</shiro:hasPermission>
          	<button type="button" class="btn btn-success pull-right dcModal" data-toggle="modal"><i class="fa fa-download" aria-hidden="true"></i> 批量导出</button>
          	<button type="button" class="btn btn-success pull-right drModal"><i class="fa fa-upload" aria-hidden="true"></i> 批量导入</button>
          	<shiro:hasPermission name="qa:qaQuestion:edit">
          	<a href="${ctx}/qa/qaQuestion/createQuestionForm1"><button type="button" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 添加咨询问题</button></a>
          	</shiro:hasPermission>
          	<shiro:hasPermission name="qa:qaQuestion:edit">
				<a href="${ctx}/qa/qaQuestion/reIndex1">
					<button id="reIndexButton" type="button" class="btn btn-info pull-right" onclick="disabledButton()">
						<i class="fa fa-plus" aria-hidden="true"></i>一键重建索引
					</button>
				</a>
			</shiro:hasPermission>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <shiro:hasPermission name="qa:qaQuestion:delete">
	            <form action="${ctx}/qa/qaQuestion/deleteBatch1?queryString=${qaQuestion.queryString}" method="post" id="batchDeleteForm">
	            </shiro:hasPermission>
	            <div style="overflow-x: auto;">
	            <table class="ui celled table_semantic" id="qaQuestionListTable">
				<thead>
					<tr>
						<shiro:hasPermission name="qa:qaQuestion:delete">
						<th width="20" class="checkboxTh"><input type="checkbox"></th>
						</shiro:hasPermission>
						<!-- <th class="sort-column a.user_id">提问者</th> -->
						<th class="sort-column a.name">提问者姓名</th>
						<th class="sort-column a.email">Email</th>
						<th class="sort-column a.mobile">手机</th>
						<th class="sort-column a.identify">身份</th>
						<th class="sort-column a.klmc">科类名称</th>
						<th class="sort-column a.province">省份</th>
						<!-- <th class="sort-column a.ip">提问人ip</th> -->
						<th class="sort-column a.time">提问时间</th>
						<!-- <th class="sort-column a.device">访问设备</th> -->
						<th class="sort-column a.content">提问内容</th>
						<th class="sort-column a.isopen">是否公开</th>
						<th class="sort-column a.hasindex">是否被索引</th>
						<shiro:hasAnyPermissions name="qa:qaQuestion:edit,qa:qaQuestion:delete"><th>操作</th></shiro:hasAnyPermissions>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="item">
					<tr>
						<shiro:hasPermission name="qa:qaQuestion:delete"><td><input type="checkbox" name="ids" value="${item.id}"></td></shiro:hasPermission>
								<%-- <td>
									${item.user.name}
								</td> --%>
								<td>
									${item.name}
								</td>
								<td>
									${item.email}
								</td>
								<td>
									${item.mobile}
								</td>
								<td>
									${fns:getDictLabel(item.identify, 'sys_user_type', '')}
								</td>
								<td>
									${fns:getDictLabel(item.klmc, 'kl', '')}
								</td>
								<td>
									${fns:getDictLabel(item.province, 'ssmc', '')}
								</td>
								<%-- <td>
									${item.ip}
								</td> --%>
								<td>
									<fmt:formatDate value="${item.time}" pattern="yyyy-MM-dd HH:mm"/>
								</td>
								<%-- <td>
									${fns:getDictLabel(item.device, 'device_type', '')}
								</td> --%>
								<td>
									${fns:abbr(item.content,15)}
								</td>
								<td>
									${fns:getDictLabel(item.isopen, 'yes_no', '')}
								</td>
								<td>
									${fns:getDictLabel(item.hasindex, 'yes_no', '')}
								</td>
						<shiro:hasAnyPermissions name="qa:qaQuestion:edit,qa:qaQuestion:delete">
						<td class="tablebox-handle">
		    				
		    				<shiro:hasPermission name="qa:qaQuestion:edit">
		    					<a href="${ctx}/qa/qaQuestion/form1?id=${item.id}&queryString=${qaQuestion.queryString}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
							</shiro:hasPermission>
							
		    				<shiro:hasPermission name="qa:qaQuestion:edit">
		    					<a href="${ctx}/qa/qaQuestion/replyForm1?question.id=${item.id}&queryString=${qaQuestion.queryString}" class="btn btn-default btn-sm" title="${item.state eq '1' ? '修改' :'' }回复"><i class="fa fa-reply"></i></a>
							</shiro:hasPermission>
							
							<shiro:hasPermission name="qa:qaQuestion:delete">
								<a href="${ctx}/qa/qaQuestion/delete1?id=${item.id}&queryString=${qaQuestion.queryString}" onclick="return confirmx('确认要删除该在线咨询吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
							</shiro:hasPermission>
							
						</td>
						</shiro:hasAnyPermissions>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			</div>
			<shiro:hasPermission name="qa:qaQuestion:delete"></form></shiro:hasPermission>
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
		top.$.jBox.confirm("确认要导出在线咨询吗？","系统提示",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/qa/qaQuestion/export");
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
    var dcModalCont = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要导出在线咨询吗？</p>';
    $('.dcModal').bind('click',function(){
		top.$.jBox.confirm(dcModalCont,"操作确认",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/qa/qaQuestion/export?questioner=1");
			   $("#searchForm").submit();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
    
  //全部删除
    var dcModalContsc = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要删除全部数据吗？</p>';
    $('#btnBatchDeleteAll').bind('click',function(){
        top.$.jBox.confirm(dcModalContsc,"操作确认",function(v,h,f){
            if(v=="ok"){
              // $("#pageSize").val(-1);
               $("#searchForm").attr("action","${ctx}/qa/qaQuestion/deleteBatchAll");
               $("#searchForm").submit();
            }
        },{buttonsFocus:1});
        top.$('.jbox-body .jbox-icon').css('top','55px');
    });
    
    //全部公开
    var openStr = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要修改为公开状态吗吗？</p>';
    $('#allOpen').bind('click',function(){
        top.$.jBox.confirm(openStr,"操作确认",function(v,h,f){
            if(v=="ok"){
              // $("#pageSize").val(-1);
               $("#searchForm").attr("action","${ctx}/qa/qaQuestion/allOpen");
               $("#searchForm").submit();
            }
        },{buttonsFocus:1});
        top.$('.jbox-body .jbox-icon').css('top','55px');
    });
    
    //全部不公开
    var notOpenStr = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要修改为不公开状态吗吗？</p>';
    $('#notAllOpen').bind('click',function(){
        top.$.jBox.confirm(notOpenStr,"操作确认",function(v,h,f){
            if(v=="ok"){
              // $("#pageSize").val(-1);
               $("#searchForm").attr("action","${ctx}/qa/qaQuestion/notAllOpen");
               $("#searchForm").submit();
            }
        },{buttonsFocus:1});
        top.$('.jbox-body .jbox-icon').css('top','55px');
    });
	
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
  	// 批量删除
      $("#btnBatchDelete")
  	.click(
  			function() {
  				if ($("form#batchDeleteForm input:checked[name='ids']").length == 0) {
  					$.jBox.tip(
  							"请勾选数据后再执行删除操作！",
  							'error');
  					return false;
  				}

  				$.jBox
  						.confirm(
  								'<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要批量删除选中的数据吗？</p>',
  								"系统提示",
  								function(v, h,
  										f) {
  									if (v == "ok") {
  										$(
  												"#deleteAll")
  												.val(
  														-1);
  										$(
  												"#batchDeleteForm")
  												.submit();
  									}
  								},
  								{
  									buttonsFocus : 1
  								});
  				return false;
  			});
	
});
function page(n,s){
	$("#pageNo").val(n);
	$("#searchForm").attr("action","${ctx}/qa/qaQuestion/list1/");
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