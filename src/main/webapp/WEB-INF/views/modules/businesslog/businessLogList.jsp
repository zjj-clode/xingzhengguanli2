<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
    <section class="content-header">
      <h1>
       	系统设置
        <small>操作日志</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">系统设置</a></li>
        <li class="active">操作日志</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
    <!-- 筛选功能部分 -->
    <form:form id="searchForm" modelAttribute="businessLog" action="${ctx}/common/businessLog/" method="post" class="form-inline form-filter" role="form">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
      <div class="form-group">
      	<label>操作人姓名：</label>
			<form:input path="createBy.name" htmlEscape="false" maxlength="64" class="form-control" placeholder="操作人姓名" style="width:120px"/>
	</div>
	<div class="form-group"><label>业务类型：</label>
		<form:select path="type" class="form-control select2">
			<form:option value="" label="---请选择---"/>
			<form:options items="${fns:getDictList('business_log_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		</form:select>
	  </div>
	<div class="form-group">
           <label>时间范围：</label>
           <div class="input-group date">
             <div class="input-group-addon">
               <i class="fa fa-calendar"></i>
             </div>
             <input type="text" id="beginDate" name="beginDate" value="<fmt:formatDate value="${businessLog.beginDate}" pattern="yyyy-MM-dd"/>" class="form-control pull-left" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:100px"/>
           </div>
           <div class="input-group date">
             <div class="input-group-addon">
               <i class="fa fa-calendar"></i>
             </div>
             <input type="text" id="endDate" name="endDate" value="<fmt:formatDate value="${businessLog.endDate}" pattern="yyyy-MM-dd"/>" class="form-control pull-left" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" style="width:100px"/>
           </div>
        </div>
      <button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
    </form:form>

    <!-- Main content -->
    <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
        <shiro:hasPermission name="expert:expertAdvice:delete">
			<button type="button" class="btn btn-danger pull-right delModel"><i class="fa fa-close" aria-hidden="true"></i> 删除</button>
		</shiro:hasPermission>
        </div>
      </div>
     <div class="row">
          <div class="col-xs-12 simpleTable">
          <form:form id="deleteForm" action="${ctx}/common/businessLog/deleteBatch" method="post">
	          <table class="ui celled table_semantic" id="businessLogList">
                <thead>
                  <tr>
                  	<shiro:hasPermission name="common:businessLog:delete">
                    <th width="20" class="checkboxTh"><input type="checkbox"></th>
                    </shiro:hasPermission>
                    <th>日志标题</th>
					<th>业务类型</th>
					<th>变更前数据</th>
					<th>变更后数据</th>
					<th>操作用户</th>
					<th>操作时间</th>
					<th>操作IP</th>
					<th>备注信息</th>
					<shiro:hasPermission name="common:businessLog:delete"><th class="tablebox-handle">操作</th></shiro:hasPermission>
                  </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="item">
					<tr>
						<shiro:hasPermission name="common:businessLog:delete">
					    <td><input type="checkbox" name="ids" value="${item.id}"></td>
					    </shiro:hasPermission>
						<td>${item.title}</td>
						<td>${fns:getDictLabel(item.type, 'business_log_type', '')}</td>
						<td>${item.preChangeData}</td>
						<td>${item.postChangeData}</td>
						<td>${item.createBy.name}</td>
						<td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td>${item.ip}</td>
						<td>${item.remarks}</td>
						<shiro:hasPermission name="common:businessLog:delete">
						<td class="tablebox-handle">
						<a href="${ctx}/common/businessLog/delete?id=${item.id}" onclick="return confirmx('确认要删除该专家咨询吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a>
						</td>
						</shiro:hasPermission>
					</tr>
				</c:forEach>
                </tbody>
              </table>
              </form:form>
              <!-- 分页 -->
              <div class="row">
                ${page}
              </div>
          </div>
      </div>
    </section>
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script>
  function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
      	return false;
  }
  $(function () {
    
    // 批量导入弹框
    $(".drModal").bind("click",function(){
      $.jBox($("#importBox").html() ,{
        title: "批量导入",
        buttons:{}
      });
    });
    // 导出数据弹框
    $('.dcModal').bind('click',function(){
		top.$.jBox.confirm("<p class='confirmTxt'><i class='fa fa-question-circle text-yellow'></i>确认要导出学生信息吗？</p>","操作确认",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/common/studentemployment/export?current=${studentEmployment.current }");
			   $("#searchForm").submit();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	// 删除数据弹框
    $('.delModel').bind('click',function(){
        var length = $("input[type=checkbox][name=ids]:checked").length;
        if(length == 0){
            $.jBox.tip("<i class='fa fa-times-circle'></i>请选择要删除的专家信息！");
        }else{
			top.$.jBox.confirm("<p class='confirmTxt'><i class='fa fa-question-circle text-yellow'></i>确认要删除选择的专家信息吗？</p>","操作确认",function(v,h,f){
				if(v=="ok"){
				   $("#deleteForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		}
	});
	//添加数据
	$('.addModel').bind('click',function(){
        window.location.href="${ctx}/expert/expertAdvice/form";
	});
	
    //Initialize Select2 Elements
    $(".select2").select2();
    // 按钮鼠标停留效果
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
	// 数据表格功能
    
    $('#businessLogList1').DataTable({
      "ordering": false,
      //"scrollY":  450,
      //"scrollX":        true,
      //"scrollCollapse": true,
      "info": false,
      "bFilter": false, 
      "paging":         false
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
</script>
</body>
</html>