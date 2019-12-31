<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<div id="importBox" class="hide">
        <form id="importForm" action="${ctx}/api/apiCodeInf/import" method="post" enctype="multipart/form-data" class="form-horizontal form-information" onsubmit="loading('正在导入，请稍等...');">
		  <div class="confirmWap">
		    <div class="row" style="padding-top:10px">
		      <div class="col-md-12">
		        <div class="form-group">
		          <label for="" class="col-sm-2 control-label">选择文件</label>
		          <div class="col-sm-3">
		            <div class="pull-left upfileInn">
			          <button type="button" class="btn btn-primary">点击这里上传文件</button>
			          <input type="file" class="upfileInput" id="uploadFile" name="file">
			        </div>
			      </div>
			      <div class="col-sm-5">
			        <p class="help-block pull-left text-red">5MB内的“xls”或“xlsx”文件</p>
			      </div>
			      <div class="col-sm-2">
			        <p class="help-block pull-left text-red"><a href="${ctx}/api/apiCodeInf/import/template">下载模板</a></p>
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
      <h1>api访问码管理<small>api访问码信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/api/apiCodeInf/">api访问码信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    
    <%-- <form:form id="searchForm" modelAttribute="apiCodeInf" action="${ctx}/api/apiCodeInf/" method="post" class="form-inline form-filter" role="form">
    	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form> --%>
    <section class="content">
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          	<shiro:hasPermission name="api:apiCodeInf:delete">
          	<button id="btnBatchDelete" type="button" class="btn btn-danger pull-right"><i class="fa fa-close" aria-hidden="true"></i> 批量删除</button>
			</shiro:hasPermission>
          	<button type="button" class="btn btn-success pull-right dcModal" data-toggle="modal"><i class="fa fa-download" aria-hidden="true"></i> 批量导出</button>
          	<button type="button" class="btn btn-success pull-right drModal"><i class="fa fa-upload" aria-hidden="true"></i> 批量导入</button>
          	<shiro:hasPermission name="api:apiCodeInf:edit">
          	<a href="${ctx}/api/apiCodeInf/form"><button type="button" class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 添加api访问码</button></a>
          	</shiro:hasPermission>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <shiro:hasPermission name="api:apiCodeInf:delete">
	            <form action="${ctx}/api/apiCodeInf/deleteBatch?queryString=${apiCodeInf.queryString}" method="post" id="batchDeleteForm">
	            </shiro:hasPermission>
	            <table class="ui celled table_semantic" id="apiCodeInfListTable" style="height: 100px;">
				<thead>
					<tr>
						<shiro:hasPermission name="api:apiCodeInf:delete"><th><input type="checkbox"></th></shiro:hasPermission>
						<th class="sort-column a.apicode">api验证码</th>
						<th class="sort-column a.username">用户名</th>
						<th class="sort-column a.userunit">用户单位</th>
						<th class="sort-column a.limitcount">限定次数</th>
						<th class="sort-column a.usercount">应用次数</th>
						<th class="sort-column a.deaddate">限定日期</th>
						<th class="sort-column a.update_date">更新时间</th>
						<shiro:hasAnyPermissions name="api:apiCodeInf:edit,api:apiCodeInf:delete"><th>操作</th></shiro:hasAnyPermissions>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="item">
					<tr>
						<shiro:hasPermission name="api:apiCodeInf:delete"><td><input type="checkbox" name="ids" value="${item.id}"></td></shiro:hasPermission>
								<td>
									${item.apicode}
								</td>
								<td>
									${item.username}
								</td>
								<td>
									${item.userunit}
								</td>
								<td>
									${item.limitcount}
								</td>
								<td>
									${item.usercount}
								</td>
								<td>
									<fmt:formatDate value="${item.deaddate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
								<td>
									<fmt:formatDate value="${item.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
						<shiro:hasAnyPermissions name="api:apiCodeInf:edit,api:apiCodeInf:delete">
						<td class="tablebox-handle">
		    				<shiro:hasPermission name="api:apiCodeInf:edit">
		    				<a href="${ctx}/api/apiCodeInf/form?id=${item.id}&queryString=${apiCodeInf.queryString}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
							</shiro:hasPermission>
							<shiro:hasPermission name="api:apiCodeInf:edit">
		    				<a href="${ctx}/api/apiCodeInf/sta?id=${item.id}" class="btn btn-default btn-sm" title="查看统计表"><i class="fa fa-list"></i></a>
							</shiro:hasPermission>
							<shiro:hasPermission name="api:apiCodeInf:delete">
							<a href="${ctx}/api/apiCodeInf/delete?id=${item.id}&queryString=${apiCodeInf.queryString}" onclick="return confirmx('确认要删除该api访问码吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
							</shiro:hasPermission>
						</td>
						</shiro:hasAnyPermissions>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<shiro:hasPermission name="api:apiCodeInf:delete"></form></shiro:hasPermission>
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
    
    var table = $('#apiCodeInfListTable').DataTable({
      "ordering": false,
      "scrollY":        300,
      "scrollX":        true,
      "scrollCollapse": true,
      "info": false,
      "bFilter": false, 
      "paging":         false,
      "fixedColumns":   {
          "leftColumns": 3,
          "rightColumns" : 1
      }
    });
	$("#btnExport").click(function(){
		top.$.jBox.confirm("确认要导出api访问码吗？","系统提示",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/api/apiCodeInf/export");
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
    var dcModalCont = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要导出api访问码吗？</p>';
    $('.dcModal').bind('click',function(){
		top.$.jBox.confirm(dcModalCont,"操作确认",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/api/apiCodeInf/export");
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
		$.jBox.confirm("确认要批量删除选中的数据吗？","系统提示",function(v,h,f){
			if(v=="ok"){
				$("#batchDeleteForm").submit();
			}
		},{buttonsFocus:1});
		return false;
	});
	
});
function page(n,s){
	$("#pageNo").val(n);
	$("#searchForm").attr("action","${ctx}/api/apiCodeInf/");
	$("#pageSize").val(s);
	$("#searchForm").submit();
   	return false;
}
</script>
</body>
</html>