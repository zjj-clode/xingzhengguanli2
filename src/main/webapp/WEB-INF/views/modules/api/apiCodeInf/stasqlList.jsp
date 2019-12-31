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
      <h1>${apiCodeInf.userunit}<small>自定义统计表信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
      <%--   <li><a href="${ctx}/api/apiCodeInf/">自定义统计表信息</a></li> --%>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    
  <form:form id="searchForm" modelAttribute="apiCodeInf" action="${ctx}/api/apiCodeInf/sta?id=${apiCodeInf.id}" method="post" class="form-inline form-filter" role="form">
    	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<%-- <input id="id" name="id" type="hidden" value="${apiCodeInf.id}"/> --%>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
			<div class="form-group"><label>统计名称：</label>
				<form:input path="staname" htmlEscape="false" maxlength="255" class="form-control"/>
			</div>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
    <section class="content">
     <div class="row tableTopBtn">
        <div class="col-xs-12">
          	<a href="${ctx}/api/apiCodeInf"><input id="btnCancel" class="btn" type="button" value="返 回" /></a>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <shiro:hasPermission name="api:apiCodeInf:delete">
	            <form action="${ctx}/api/apiCodeInf/deleteBatch?queryString=${apiCodeInf.queryString}" method="post" id="batchDeleteForm">
	            </shiro:hasPermission>
	           <table class="ui celled table_semantic" id="zhaoshengStasqlListTable">
				<thead>
					<tr>
						<th class="sort-column a.staname">统计名称</th>
						<th class="sort-column a.tjfilter">统计类型</th>
						<th class="sort-column a.ischart">是否生成图表</th>
						<th class="sort-column a.isselect">是否被选择</th>
						<th class="sort-column a.permissions">是否为公共表</th>
						<shiro:hasAnyPermissions name="api:apiCodeInf:edit,api:apiCodeInf:delete,common:zhaoshengStasql:edit,common:zhaoshengStasql:delete"><th>操作</th></shiro:hasAnyPermissions>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="item">
					<tr>
						<td>
							${item.staname}
						</td>
						<td>
							${item.tjfilter}
						</td>
						<td>
							${fns:getDictLabel(item.ischart, 'yes_no', '')}
						</td>
						<td>
							${fns:getDictLabel(item.isselect, 'yes_no', '')}
						</td>
						<td>
							${fns:getDictLabel(item.permissions, 'yes_no', '')}
						</td>
						<shiro:hasAnyPermissions name="api:apiCodeInf:edit,api:apiCodeInf:delete,common:zhaoshengStasql:edit,common:zhaoshengStasql:edit">
							<td class="tablebox-handle" align="center">
								
						     	 <c:if test="${ item.status eq '1'}">
						     	 	<div id="sta${ item.id}">
						     	 	<a  href="javascript:void(0);" style="color: red;" title="取消" onclick="changeStaQX('${apiCodeInf.id}','${item.id}');">
						     	 		取消
						     	 	</a>
						     	 	</div>
						     	 </c:if>
								<c:if test="${ item.status eq '0'}">
									<div id="sta${item.id}">
									<a href="javascript:void(0);" title="选择" onclick="changeSta('${apiCodeInf.id}','${item.id}');">
										选择
									</a>
									</div>
								</c:if>
				
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
	var apiId = $("#id").val();
	//$("#searchForm").attr("action","${ctx}/api/apiCodeInf/sta?id="+apiId);
	$("#pageSize").val(s);
	$("#searchForm").submit();
   	return false;
}
function changeSta(apiCodeInfId,staId) {
	$.post(
		   "${ctx}/api/apiCodeInf/editStaSql",
			{"id":apiCodeInfId,"staList":staId},	
			 function(state){
			 if(state == "true"){
			  var idxz = "sta"+staId;
		
			 $("#"+idxz).html('');	
			$("#"+idxz).append('<a  href="javascript:void(0);" style="color: red;" title="取消" onclick="changeStaQX('+"'"+apiCodeInfId+"'"+","+"'"+staId+"'"+');">取消</a>');
			 $.jBox.tip("修改成功","info");
			
			 }	
				
				}
		);
}
function changeStaQX(apiCodeInfId,staId) {
	$.post(
		   "${ctx}/api/apiCodeInf/editCancelStaSql",
			{"id":apiCodeInfId,"staid":staId},	
			 function(state){
			 if(state == "true"){
			 var id = "sta"+staId;
			 $("#"+id).html('');
			$("#"+id).append('<a href="javascript:void(0);" title="选择" onclick="changeSta('+"'"+apiCodeInfId+"'"+","+"'"+staId+"'"+');">选择</a>');
			  $.jBox.tip("取消选中成功","info");
			 }	
				
				}
		);
}
</script>
</body>
</html>