<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<div id="importBox" class="hide">
        <form id="importForm" action="${ctx}/xingzhengguanli/educationProjectDescription/import" method="post" enctype="multipart/form-data" class="form-horizontal form-information" onsubmit="loading('正在导入，请稍等...');">
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
			        <p class="help-block pull-left text-red"><a href="${ctx}/xingzhengguanli/educationProjectDescription/import/template">下载模板</a></p>
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
      <h1>项目描述管理<small>项目描述信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/xingzhengguanli/educationProjectDescription/">项目描述信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    
    <form:form id="searchForm" modelAttribute="educationProjectDescription" action="${ctx}/xingzhengguanli/educationProjectDescription/" method="post" class="form-inline form-filter" role="form">
    	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
			<div class="form-group"><label>项目活动：</label>
				<form:input path="projectName" htmlEscape="false" maxlength="255" class="form-control"/>
			</div>
			<div class="form-group"><label>子活动：</label>
				<form:input path="childProject" htmlEscape="false" maxlength="255" class="form-control"/>
			</div>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
    <section class="content">
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          	<shiro:hasPermission name="xingzhengguanli:educationProjectDescription:delete">
          	<button id="btnBatchDelete" type="button" class="btn btn-danger pull-right"><i class="fa fa-close" aria-hidden="true"></i> 批量删除</button>
			</shiro:hasPermission>
          	<button type="button" class="btn btn-success pull-right dcModal" data-toggle="modal"><i class="fa fa-download" aria-hidden="true"></i> 批量导出</button>
          
          	<shiro:hasPermission name="xingzhengguanli:educationProjectDescription:edit">
          	<a href="${ctx}/xingzhengguanli/educationProjectDescription/form"><button type="button" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 添加项目描述</button></a>
          	</shiro:hasPermission>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <shiro:hasPermission name="xingzhengguanli:educationProjectDescription:delete">
	            <form action="${ctx}/xingzhengguanli/educationProjectDescription/deleteBatch?queryString=${educationProjectDescription.queryString}" method="post" id="batchDeleteForm">
	            </shiro:hasPermission>
	            <div style="overflow:auto;">
		            <table class="ui celled table_semantic" id="educationProjectDescriptionListTable">
					<thead>
						<tr>
							<shiro:hasPermission name="xingzhengguanli:educationProjectDescription:delete"><th width="20" class="checkboxTh"><input type="checkbox"></th></shiro:hasPermission>
							<th class="sort-column a.project_name">项目活动</th>
							<th class="sort-column a.child_project">子活动</th>
							<th class="sort-column a.sub_item_expenditure">分项支出</th>
							<th class="sort-column a.number_frequency">数量/频率</th>
							<th class="sort-column a.sort">序号</th>
							<shiro:hasAnyPermissions name="xingzhengguanli:educationProjectDescription:edit,xingzhengguanli:educationProjectDescription:delete"><th>操作</th></shiro:hasAnyPermissions>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${page.list}" var="item">
						<tr>
							<shiro:hasPermission name="xingzhengguanli:educationProjectDescription:delete"><td><input type="checkbox" name="ids" value="${item.id}"></td></shiro:hasPermission>
									<td>
										${item.projectName}
									</td>
									<td>
										${item.childProject}
									</td>
									<td>
										${item.subItemExpenditure}
									</td>
									<td>
										${item.numberFrequency}
									</td>
									<td>
										${item.sort}
									</td>
							<shiro:hasAnyPermissions name="xingzhengguanli:educationProjectDescription:edit,xingzhengguanli:educationProjectDescription:delete">
							<td class="tablebox-handle">
			    				<shiro:hasPermission name="xingzhengguanli:educationProjectDescription:edit">
			    				<a href="${ctx}/xingzhengguanli/educationProjectDescription/form?id=${item.id}&queryString=${educationProjectDescription.queryString}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
								</shiro:hasPermission>
								<shiro:hasPermission name="xingzhengguanli:educationProjectDescription:delete">
								<a href="${ctx}/xingzhengguanli/educationProjectDescription/delete?id=${item.id}&queryString=${educationProjectDescription.queryString}" onclick="return confirmx('确认要删除该项目描述吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
								</shiro:hasPermission>
							</td>
							</shiro:hasAnyPermissions>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<shiro:hasPermission name="xingzhengguanli:educationProjectDescription:delete"></form></shiro:hasPermission>
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
		top.$.jBox.confirm("确认要导出项目描述吗？","系统提示",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/xingzhengguanli/educationProjectDescription/export");
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
	
	// checkbox iCheck
    $('.simpleTable input[type="checkbox"]').iCheck({
      	checkboxClass: 'icheckbox_flat-blue',
      	radioClass: 'iradio_flat-blue'
    });
    $(".checkboxTh ins").click(function () {
    	var checked = $(this).parent('div').hasClass('checked');
      	if (checked) {
        	$("input[type='checkbox'][name='ids']").iCheck("check");
      	} else {
         	$("input[type='checkbox'][name='ids']").iCheck("uncheck");
      	}
    });
    $("tbody td ins").click(function () {
    	var checked = $(this).parent('div').hasClass('checked');
    	var checkboxTh_checkbox = $(".checkboxTh input[type='checkbox']");
    	if (checked) {
    		// td全部选中时，th才选中
    		if($("tbody td input[type='checkbox'][name='ids']").length == $("tbody td input[type='checkbox'][name='ids']:checked").length){
	    		checkboxTh_checkbox.iCheck("check");
    		}
      	} else {
      		// th不选中
      		checkboxTh_checkbox.iCheck("uncheck");
      	}
    });
	
	// 导出数据弹框
    var dcModalCont = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要导出项目描述吗？</p>';
    $('.dcModal').bind('click',function(){
		top.$.jBox.confirm(dcModalCont,"操作确认",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/xingzhengguanli/educationProjectDescription/export");
			   $("#searchForm").submit();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	
	// 批量删除
	$("#btnBatchDelete").click(function(){
		if($("form#batchDeleteForm input:checked[name='ids']").length==0){
			$.jBox.tip("请勾选数据后再执行删除操作！",'error');
			return false;
		}
		$.jBox.confirm('<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要批量删除选中的数据吗？</p>',"系统提示",function(v,h,f){
			if(v=="ok"){
				$("#deleteAll").val(-1);
				$("#batchDeleteForm").submit();
			}
		},{buttonsFocus:1});
		return false;
	});
	
});
function page(n,s){
	$("#pageNo").val(n);
	$("#searchForm").attr("action","${ctx}/xingzhengguanli/educationProjectDescription/");
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