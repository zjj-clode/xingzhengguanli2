<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
</head>
<body>
    <section class="content-header">
      <h1>
                           栏目设置
        <small>栏目添加与修改</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx}"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/common/category">栏目信息</a></li>
        <li class="active">栏目添加与修改</li>
      </ol>
    </section>
    <!-- /.content-header -->
    
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box box-info" style="padding-right:20px;padding-top:20px;">
            <form:form id="inputForm" modelAttribute="category" action="${ctx}/cms/category/save" method="post" class="form-horizontal form-information form-lie">
               <form:hidden path="id"/>
				<sys:message content="${message}"/>		
				<div class="box-body pad">
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>归属机构:</label>
						<div class="col-sm-4">
			                <sys:treeselect id="office" name="office.id" value="${category.office.id}" labelName="office.name" labelValue="${category.office.name}"
								title="机构" url="/sys/office/treeData" cssClass="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>上级栏目:</label>
						<div class="col-sm-4">
			                <sys:treeselect id="category" name="parent.id" value="${category.parent.id}" labelName="parent.name" labelValue="${category.parent.name}"
								title="栏目" url="/cms/category/treeData" extId="${category.id}" cssClass="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">栏目模型:</label>
						<div class="col-sm-3">
							<form:select path="module" class="form-control select2">
								<form:option value="" label="公共模型"/>
								<form:options items="${fns:getDictList('cms_module')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>栏目名称:</label>
						<div class="col-sm-5">
							<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
						</div>
					</div>
							<div class="form-group">
						<label class="col-sm-1 control-label">栏目英文名称:</label>
						<div class="col-sm-5">
							<form:input path="ename" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">缩略图:</label>
						<div class="col-sm-3">
							<form:hidden path="image" htmlEscape="false" maxlength="255" class="form-control"/>
							<sys:ckfinder input="image" type="images" uploadPath="/cms/category"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">链接:</label>
						<div class="col-sm-5">
							<form:input path="href" htmlEscape="false" maxlength="200" class="form-control"/>
						</div>
						<div class="col-sm-4 text-muted">栏目超链接地址，优先级“高”</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">目标:</label>
						<div class="col-sm-5">
							<form:input path="target" htmlEscape="false" maxlength="200" class="form-control"/>
						</div>
						<div class="col-sm-4 text-muted">栏目超链接打开的目标窗口，新窗口打开，请填写：“_blank”</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">描述:</label>
						<div class="col-sm-5">
							<form:textarea path="description" htmlEscape="false" rows="4" maxlength="200" class="form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">关键字:</label>
						<div class="col-sm-5">
							<form:input path="keywords" htmlEscape="false" maxlength="200" class="form-control"/>
						</div>
						<div class="col-sm-4 text-muted">填写描述及关键字，有助于搜索引擎优化</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label"><span class="text-red fm">*</span>排序:</label>
						<div class="col-sm-1">
							<form:input path="sort" htmlEscape="false" maxlength="11" class="form-control required digits"/>
						</div>
						<div class="col-sm-4 text-muted">栏目的排列次序</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">在导航中显示:</label>
						<div class="col-sm-1 radio">
						    <c:forEach items="${fns:getDictList('show_hide')}" var="dic" varStatus="status">
							   <label style="margin-right:10px">
							      <input type="radio" name="inMenu" id="inMenu" value="${dic.value}" <c:if test="${dic.value == category.inMenu || (empty category.id && dic.value == 0)}">checked="true"</c:if> >${dic.label}
							   </label>
							</c:forEach>
						</div>
						<div class="col-sm-4 text-muted">是否在导航中显示该栏目</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">在分类页中显示列表:</label>
						<div class="col-sm-1 radio">
						    <c:forEach items="${fns:getDictList('show_hide')}" var="dic" varStatus="status">
							   <label style="margin-right:10px">
							      <input type="radio" name="inList" id="inList" value="${dic.value}" <c:if test="${dic.value == category.inList || (empty category.id && dic.value == 0)}">checked="true"</c:if> >${dic.label}
							   </label>
							</c:forEach>
						</div>
						<div class="col-sm-4 text-muted">是否在分类页中显示该栏目的文章列表</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label" title="默认展现方式：有子栏目显示栏目列表，无子栏目显示内容列表。">展现方式:</label>
						<div class="col-sm-5 radio">
						    <c:forEach items="${fns:getDictList('cms_show_modes')}" var="dic" varStatus="varStatus">
							   <label style="margin-right:10px">
							      <input type="radio" name="showModes" id="showModes" value="${dic.value}" <c:if test="${dic.value == category.showModes || (empty category.id && varStatus.index == 0)}">checked="true"</c:if> >${dic.label}
							   </label>
							</c:forEach>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">是否允许评论:</label>
						<div class="col-sm-5 radio">
						    <c:forEach items="${fns:getDictList('yes_no')}" var="dic" varStatus="status">
							   <label style="margin-right:10px">
							      <input type="radio" name="allowComment" id="allowComment" value="${dic.value}" <c:if test="${dic.value == category.allowComment || (empty category.id && dic.value == 0)}">checked="true"</c:if> >${dic.label}
							   </label>
							</c:forEach>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">是否需要审核:</label>
						<div class="col-sm-5 radio">
						    <c:forEach items="${fns:getDictList('yes_no')}" var="dic" varStatus="status">
							   <label style="margin-right:10px">
							      <input type="radio" name="isAudit" id="isAudit" value="${dic.value}" <c:if test="${dic.value == category.isAudit || (empty category.id && dic.value == 0)}">checked="true"</c:if> >${dic.label}
							   </label>
							</c:forEach>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">自定义列表视图:</label>
						<div class="col-sm-3">
			                <form:select path="customListView" class="form-control select2">
			                    <form:option value="" label="默认视图"/>
			                    <form:options items="${listViewList}" htmlEscape="false"/>
			                </form:select>
						</div>
						<div class="col-sm-4 text-muted">自定义列表视图名称必须以"${category_DEFAULT_TEMPLATE}"开始</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">自定义内容视图:</label>
						<div class="col-sm-3">
			                <form:select path="customContentView" class="form-control select2">
			                    <form:option value="" label="默认视图"/>
			                    <form:options items="${contentViewList}" htmlEscape="false"/>
			                </form:select>
						</div>
						<div class="col-sm-4 text-muted">自定义内容视图名称必须以"${article_DEFAULT_TEMPLATE}"开始</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label">自定义视图参数:</label>
						<div class="col-sm-5">
			                <form:input path="viewConfig" htmlEscape="true" class="form-control"/>
						</div>
						<div class="col-sm-4 text-muted">视图参数例如: {count:2, title_show:"yes"}</div>
					</div>
					<div class="form-group">
					    <div class="col-sm-offset-1 col-sm-11">
							<shiro:hasPermission name="cms:category:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
							<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
						</div>
					</div>
				</form:form>
				</div>
            </div>
        </div>
   </section>
    <!-- /.content -->	
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>	
<script type="text/javascript">
	$(document).ready(function() {
    	$(".select2").select2();
		$("#name").focus();
		$("#inputForm").validate({
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
	});
</script>
</body>
</html>