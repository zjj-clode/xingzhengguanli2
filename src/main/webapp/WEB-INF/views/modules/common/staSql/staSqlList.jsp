<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<div id="importBox" class="hide">
        <form id="importForm" action="${ctx}/common/staSql/import" method="post" enctype="multipart/form-data" class="form-horizontal form-information" onsubmit="loading('正在导入，请稍等...');">
		  <div class="confirmWap">
		    <div class="row" style="padding-top:10px">
		      <div class="col-md-12">
		        <div class="form-group">
		          <label for="" class="col-sm-2 control-label">选择文件</label>
		          <div class="col-sm-3">
		            <div class="pull-left upfileInn">
			          <button type="button" class="btn btn-primary">点击这里上传文件</button>
			          <input type="file" class="upfileInput" id="uploadFile" name="file" onchange="showFileName(this)">
			        </div>
			      </div>
			      <div class="col-sm-5">
			        <p class="help-block pull-left text-red fileName">5MB内的“xls”或“xlsx”文件</p>
			      </div>
			      <div class="col-sm-2">
			        <p class="help-block pull-left text-red"><a href="${ctx}/common/staSql/import/template">下载模板</a></p>
			      </div>
		        </div>
		        <div class="form-group">
		          <label for="" class="col-sm-2 control-label"></label>
		          <div class="col-sm-10 radio">
		              <input class="btn btn-primary" type="submit" value="   导    入   "/>
		              <input id="btnCancel" class="btn btn-default" type="button" value="取消" onclick="parent.jBox.close();"/>
		          </div>
		        </div>
		      </div>
		    </div>
		  </div>
		</form>
	</div>
	<section class="content-header">
      <h1>统计模板管理<small>统计模板信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/common/staSql/">统计模板信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    
    <form:form id="searchForm" modelAttribute="staSql" action="${ctx}/common/staSql/" method="post" class="form-inline form-filter" role="form">
    	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
			<div class="form-group"><label>统计名称：</label>
				<form:input path="staname" htmlEscape="false" maxlength="64" class="form-control"/>
			</div>
			<div class="form-group"><label>统计类型：</label>
				<form:select path="statype" class="form-control select2">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('statype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		<button type="submit" class="btn btn-default btn-sm" title="搜索" onclick="return page();"><i class="fa fa-search"></i></button>
	</form:form>
    <section class="content">
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          	<shiro:hasPermission name="common:staSql:delete">
          	<button id="btnBatchDelete" type="button" class="btn btn-danger pull-right"><i class="fa fa-close" aria-hidden="true"></i> 批量删除</button>
			</shiro:hasPermission>
          	<button type="button" class="btn btn-success pull-right dcModal" data-toggle="modal"><i class="fa fa-download" aria-hidden="true"></i> 批量导出</button>
          	<button type="button" class="btn btn-success pull-right drModal"><i class="fa fa-upload" aria-hidden="true"></i> 批量导入</button>
          	<shiro:hasPermission name="common:staSql:edit">
          	<a href="${ctx}/common/staSql/form"><div style="font-size:12px;" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 添加统计模板</div></a>
          	</shiro:hasPermission>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <shiro:hasPermission name="common:staSql:delete">
	            <form action="${ctx}/common/staSql/deleteBatch?queryString=${staSql.queryString}" method="post" id="batchDeleteForm">
	            </shiro:hasPermission>
	            <table class="ui celled table_semantic" id="staSqlListTable">
				<thead>
					<tr>
						<shiro:hasPermission name="common:staSql:delete"><th width="20" class="checkboxTh"><input type="checkbox"></th></shiro:hasPermission>
						<th class="sort-column a.staname">统计名称</th>
						<th class="sort-column a.statype">统计类型</th>
						<th class="sort-column a.sort">排序</th>
						<shiro:hasAnyPermissions name="common:staSql:edit,common:staSql:delete"><th class="tablebox-handle">操作</th></shiro:hasAnyPermissions>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="item">
					<tr>
						<shiro:hasPermission name="common:staSql:delete"><td><input type="checkbox" name="ids" value="${item.id}"></td></shiro:hasPermission>
								<td>
									${item.staname}
								</td>
								<td>
									${fns:getDictLabel(item.statype, 'statype', '')}
								</td>
								<td>
									${item.sort}
								</td>
						<shiro:hasAnyPermissions name="common:staSql:edit,common:staSql:delete">
						<td class="tablebox-handle">
						<a target="_blank" href="${ctx}/common/staSql/commonstajson?id=${item.id}">json</a>
		    				<shiro:hasPermission name="common:staSql:edit">
		    				<a href="${ctx}/common/staSql/form?id=${item.id}&queryString=${staSql.queryString}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
							</shiro:hasPermission>
							<shiro:hasPermission name="common:staSql:delete">
							<a href="${ctx}/common/staSql/delete?id=${item.id}&queryString=${staSql.queryString}" onclick="return confirmx('确认要删除该统计模板吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
							</shiro:hasPermission>
						</td>
						</shiro:hasAnyPermissions>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<shiro:hasPermission name="common:staSql:delete"></form></shiro:hasPermission>
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
    $(".select2").select2();
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
	
	$("#btnExport").click(function(){
		top.$.jBox.confirm("确认要导出统计模板吗？","系统提示",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/common/staSql/export");
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
    var dcModalCont = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要导出统计模板吗？</p>';
    $('.dcModal').bind('click',function(){
		top.$.jBox.confirm(dcModalCont,"操作确认",function(v,h,f){
			if(v=="ok"){
			   $("#searchForm").attr("action","${ctx}/common/staSql/export");
			   $("#searchForm").submit();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	
	// 批量删除
	var deModalCont = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要批量删除选中的数据吗? </p>';
	$("#btnBatchDelete").click(function(){
		if($("form#batchDeleteForm input:checked[name='ids']").length==0){
			$.jBox.tip("请勾选数据后再执行删除操作！",'error');
			return false;
		}
		$.jBox.confirm(deModalCont,"系统提示",function(v,h,f){
			if(v=="ok"){
				$("#batchDeleteForm").submit();
			}
		},{buttonsFocus:1});
		return false;
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
});

function page(n,s){
	if(n){
		$("#pageNo").val(n);
	}
	if(s){
		$("#pageSize").val(s);
	}
	$("#searchForm").attr("action","${ctx}/common/staSql/").submit();
   	return false;
}

//选择文件后触发，回显文件名给用户
  function showFileName(obj){
	  var fileName = obj.value;//C:\fakepath\学生信息模板.xlsx
	  var lastIndex = fileName.lastIndexOf("\\"); //'\'最后一次出现的索引
	  $(".fileName").text(fileName.substring(lastIndex+1));
  }
</script>
</body>
</html>