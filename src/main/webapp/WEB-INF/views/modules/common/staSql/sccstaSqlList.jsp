<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
	<section class="content-header">
      <h1>就业中心统计分析<small>统计模板信息</small></h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/common/staSql/">统计模板信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    
    <form:form id="searchForm" modelAttribute="staSql" action="${ctx}/common/staSql/scc" method="post" class="form-inline form-filter" role="form">
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

      <div class="row">
          <div class="col-xs-12 simpleTable">
	            
	            <table class="ui celled table_semantic" id="staSqlListTable">
				<thead>
					<tr>
						<th class="sort-column a.staname">统计名称</th>
						<th class="sort-column a.statype">统计类型</th>
						<th class="sort-column a.sort">排序</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="item">
					<tr>
								<td>
									<a href="${ctx}/common/staSql/commonsta?id=${item.id}">${item.staname}</a>
								</td>
								<td>
									${fns:getDictLabel(item.statype, 'statype', '')}
								</td>
								<td>
									${item.sort}
								</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
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
	$("#searchForm").attr("action","${ctx}/common/staSql/scc").submit();
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