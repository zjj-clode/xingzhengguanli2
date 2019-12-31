<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
     <div id="importBox" class="hide">
        <form id="importForm" action="${ctx}/sys/user/import" method="post" enctype="multipart/form-data" class="form-horizontal form-information"  onsubmit="loading('正在导入，请稍等...');">
		  <div class="confirmWap">
		    <div class="row" style="padding-top:10px">
		      <div class="col-md-12">
		         <div class="form-group">
		          <label for="" class="col-sm-2 control-label">选择文件：</label>
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
			        <p class="help-block pull-left text-red"><a href="${ctx}/sys/user/import/template">下载模板</a></p>
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
      <h1>
       系统管理
        <small>用户信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">用户信息</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
    <sys:message content="${message}"/>
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/list" method="post" class="form-inline form-filter" role="form">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<div class="form-group"><label>类型：</label>
			<form:select path="userType" class="form-control select2">
				<form:option value="" label="--请选择--"/>
				<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<%-- <div class="form-group"><label>院系：</label><sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}" 
			title="院系" url="/sys/office/treeData?type=1" cssClass="form-control" allowClear="true"/></div>
		<div class="form-group"><label>专业 ：</label><sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}" 
			title="部门" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/></div> --%>
		
		<div class="form-group"><label>姓名：</label><form:input path="name" htmlEscape="false" maxlength="50" class="form-control"/></div>
		<div class="form-group"><label>登录名：</label><form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control"/></div>
		<button type="submit" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
	<section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          <!-- <button type="button" class="btn btn-success pull-right dcModal" data-toggle="modal"><i class="fa fa-download" aria-hidden="true"></i> 批量导出</button>
          <button type="button" class="btn btn-success pull-right drModal"><i class="fa fa-upload" aria-hidden="true"></i> 批量导入</button> -->
          <a href="${ctx}/sys/user/form"  class="btn btn-info pull-right"><i class="fa fa-plus" aria-hidden="true"></i> 添加用户</a>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
          <%-- 	<shiro:hasPermission name="sys:user:edit">
	            <form action="${ctx}/sys/user/deleteBatch?queryString=${user.queryString}" method="post" id="batchDeleteForm">
	        </shiro:hasPermission> --%>
	            <table class="ui celled table_semantic" id="userlist">
					<thead>
					<tr>
					<!-- <th>院系</th>
					<th>专业</th> -->
					<%-- <shiro:hasPermission name="sys:user:edit">
						<th width="20" class="checkboxTh"><input type="checkbox"></th>
						</shiro:hasPermission> --%>
					<th>类型</th>
					<th class="sort-column login_name">登录名</th>
					<th class="sort-column name">姓名</th>
					<th>电话</th>
					<th>手机</th>
					<th class="tablebox-handle">操作</th>
					</tr></thead>
					<tbody>
					<c:forEach items="${page.list}" var="user">
						<tr>
							<%--  <td><c:if test="${user.userType < '3' }">${user.company.name}</c:if></td>
							<td><c:if test="${user.userType < '3' }">${user.office.name}</c:if></td> --%>
							<%-- <shiro:hasPermission name="sys:user:edit"><td><input type="checkbox" name="ids" value="${user.id}"></td></shiro:hasPermission> --%>
							<td>${fns:getDictLabel(user.userType, 'sys_user_type', '')}</td>
							<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
							<td>${user.name}</td>
							<td>${user.phone}</td>
							<td>${user.mobile}</td><%--
							<td>${user.roleNames}</td> --%>
							<td class="tablebox-handle"><shiro:hasPermission name="sys:user:edit">
			    				<a href="${ctx}/sys/user/form?id=${user.id}" class="btn btn-default btn-sm" title="编辑"><i class="fa fa-pencil"></i></a>
								<a href="${ctx}/sys/user/delete?id=${user.id}" onclick="return confirmx('确认要删除该用户吗？', this.href)" class="btn btn-default btn-sm" title="删除"><i class="fa fa-close"></i></a></shiro:hasPermission>
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
    //Initialize Select2 Elements
    $(".select2").select2();
    // 数据表格功能
   $('#userlist1').DataTable({
      "ordering": false,
      "scrollY":  550,
      "scrollCollapse": true,
      "info": false,
      "bFilter": false, 
      "paging": false
    });
    // 按钮鼠标停留效果
    $('.tablebox-handle a,.form-filter button,.tablebox-handle i').tooltip();
    
    // 批量导入弹框
    $(".drModal").bind("click",function(){
      $.jBox($("#importBox").html() ,{
        title: "批量导入",
        buttons:{}
      });
    });
    // 导出数据弹框
    $('.dcModal').bind('click',function(){
		top.$.jBox.confirm("<p class='confirmTxt'><i class='fa fa-question-circle text-yellow'></i>确认要导出用户信息吗？</p>","操作确认",function(v,h,f){
			if(v=="ok"){
			   $("#pageSize").val(-1);
			   $("#searchForm").attr("action","${ctx}/sys/user/export");
			   $("#searchForm").submit();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
	});
	$("#corporationinfo").select2({
            ajax: {
                url: "${ctx}/common/corporationinfo/selectData",
                dataType: 'json',
                delay: 250,
                data: function (params) {
                    return {
                        name: params.term, // search term
                        page: params.page
                    };
                },
                processResults: function (data, params) {
                    params.page = params.page || 1;
                    return {
                        results: data,
                        pagination: {
                            more: (params.page * 30) < data.total_count
                        }
                    };
                },
                cache: true
            },
            escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
            minimumInputLength: 2, 
            language: "zh-CN", //设置 提示语言
            maximumSelectionLength: 3,  //设置最多可以选择多少项
            placeholder: "--请选择--",
            allowClear:true,
            tags: false,  //设置必须存在的选项 才能选中
            templateResult: function (repo) { //搜索到结果返回后执行，可以控制下拉选项的样式
                if (repo.loading) return repo.text;
                var markup = "<div class=''>" + repo.text + "</div>";
                return markup;
            },
            templateSelection: function (repo) {  //选中某一个选项是执行
                 $("#corporationName").val(repo.text);
                return repo.full_name || repo.text;
            }
        });
  });
  
</script>
</body>
</html>