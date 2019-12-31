<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
    <section class="content-header">
      <h1>
       内容管理
        <small>信息量统计</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">信息量统计</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
	<form:form id="searchForm" modelAttribute="article" action="${ctx}/cms/stats/article" method="post" class="form-inline form-filter" role="form">
		<div class="form-group">
			<label>归属栏目：</label><sys:treeselect id="category" name="categoryId" value="${paramMap.id}" labelName="categoryName" labelValue="${paramMap.name}"
				title="栏目" url="/cms/category/treeData" module="article" cssClass="form-control" allowClear="true"/>
		</div>
		<div class="form-group">	
			<label>归属机构：</label><sys:treeselect id="office" name="officeId" value="${paramMap.office.id}" labelName="officeName" labelValue="${paramMap.office.name}" 
				title="机构" url="/sys/office/treeData" cssClass="form-control" allowClear="true"/>
		</div>
		<div class="form-group">	
			<label>开始日期：</label>
			<div class="input-group date">
	          <div class="input-group-addon">
	            <i class="fa fa-calendar"></i>
	          </div>
				<input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="form-control"
					value="${paramMap.beginDate}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</div>
	    </div>
	    <div class="form-group">
			<label>结束日期：</label>
			<div class="input-group date">
	          <div class="input-group-addon">
	            <i class="fa fa-calendar"></i>
	          </div>
				<input id="endDate"  name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control"
					value="${paramMap.endDate}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</div>
		</div>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
	<sys:message content="${message}"/>
	<!-- Main content -->
    <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
	            <table class="ui celled table_semantic" id="statslist">
					<thead><tr><th>父级栏目</th><th>栏目名称</th><th>信息量</th><th>点击量</th><th>最后更新时间</th><th>归属机构</th>
					<tbody>
					<c:forEach items="${list}" var="stats">
						<tr>
							<td><a href="javascript:" onclick="$('#categoryId').val('${stats.parent.id}');$('#categoryName').val('${stats.parent.name}');$('#searchForm').submit();return false;">${stats.parent.name}</a></td>
							<td><a href="javascript:" onclick="$('#categoryId').val('${stats.id}');$('#categoryName').val('${stats.name}');$('#searchForm').submit();return false;">${stats.name}</a></td>
							<td>${stats.cnt}</td>
							<td>${stats.hits}</td>
							<td><fmt:formatDate value="${stats.updateDate}" type="both"/></td>
							<td><a href="javascript:" onclick="$('#officeId').val('${stats.office.id}');$('#officeName').val('${stats.office.name}');$('#searchForm').submit();return false;">${stats.office.name}</a></td>
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
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script type="text/javascript">
	function autoRowSpan(tb,row,col){
        var lastValue="",value="",pos=1;  
        for(var i=row;i<tb.rows.length;i++){
            value = tb.rows[i].cells[col].innerText;  
            if(lastValue == value){
                tb.rows[i].deleteCell(col); 
                tb.rows[i-pos].cells[col].rowSpan = tb.rows[i-pos].cells[col].rowSpan+1;
                pos++;
            }else{
                lastValue = value;
                pos=1;
            }
        }
    }
	$(document).ready(function(){
		autoRowSpan(contentTable,0,0);
        $("td,th").css({"text-align":"center","vertical-align":"middle"});
        
        // 数据表格功能
	    var table = $('#statslist').DataTable({
	      "ordering":false,
	      "scrollY":   450,
	      "scrollCollapse": true,
	      "info": false,
	      "bFilter": false, 
	      "paging":         false
	    });
	    //Initialize Select2 Elements
        $(".select2").select2();
	    // 按钮鼠标停留效果
   		 $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
	});
</script>
</body>
</html>