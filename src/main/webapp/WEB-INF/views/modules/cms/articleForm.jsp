<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>文章管理</title>
<meta name="decorator" content="lte" />
<style type="text/css">
.form-information .btn-default {
	height: 34px;
	padding: 5px 12px;
	font-size: 14px;
}

.form-information .btn label {
	margin: 0;
}

.form-information .chbox {
	margin: 0;
	vertical-align: middle
}

.zdInn {
	position: relative;
	padding-left: 150px;
}

.zdInn .zdtxt {
	position: absolute;
	left: 15px;
	top: 7px;
}

.zdInn .zdtxt label {
	margin-right: 10px;
}
</style>


<!-- videojs
<link href="http://vjs.zencdn.net/6.1.0/video-js.css"rel="stylesheet">
<script src="http://vjs.zencdn.net/6.1.0/video.js"></script>
-->
<script type="text/javascript">var ctxStatic = '${ctxStatic}'
</script>
</head>
<body>
	<section class="content-header">
		<h1>
			内容管理 <small>文章发布</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
			<li><a href="${ctx}/cms/article">文章信息</a></li>
			<li class="active">文章发布</li>
		</ol>
	</section>

	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info" style="padding-right:20px;padding-top:20px;">
					<form:form id="inputForm" modelAttribute="article" action="${ctx}/cms/article/save" method="post"
						class="form-horizontal form-information form-lie">
						<form:hidden path="id" />
						<sys:message content="${message}" />
						<div class="box-body pad">
							<div class="form-group">
								<label class="col-sm-1 col-sm-1 control-label"><span class="text-red fm">*</span>归属栏目:</label>
								<div class="col-sm-2">
									<sys:treeselect id="categoryId" name="category.id" value="${categoryId}" labelName="category.name" labelValue="${categoryName}" title="栏目"
										url="/cms/category/treeData" module="article" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true"
										cssClass="form-control required" checked="true" />
									&nbsp;
								</div>
								<div class="col-sm-3" style="padding-top:7px">
									<label> <input id="url" type="checkbox" onclick="if(this.checked){$('#linkBody').show()}else{$('#linkBody').hide()}$('#link').val()"><label
										for="url" style="margin-left:5px;cursor:pointer;">外部链接</label>
									</label>
								</div>
							</div>
							<!-- <div class="form-group">
						<label class="col-sm-1 col-sm-1 control-label">其他归属栏目:</label>
						
						    
					</div> -->
							<div class="form-group">
								<label for="" class="col-sm-1 control-label"><span class="text-red fm">*</span> 标题：</label>
								<div class="col-sm-4">
									<form:input path="title" htmlEscape="false" maxlength="200" class="form-control required has-ruler required" id="cp3text"
										style="color: ${article.color}" />
								</div>
								<div class="col-sm-1">
									<input id="cp3" name="cp3" type="text" class="input_colorpicker" readonly="" style="background-color: rgb(0, 0, 0);z-index: 9999">
									<form:hidden path="color" htmlEscape="false" maxlength="200" class="form-control required has-ruler required" id="cp3textcolor" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1  control-label">副标题:</label>
								<div class="col-sm-4">
									<form:input path="subtitle" htmlEscape="false" maxlength="200" class="form-control" />
								</div>
							</div>
							<div id="linkBody" class="form-group" style="display:none">
								<label class="col-sm-1 control-label">外部链接:</label>
								<div class="col-sm-4">
									<form:input path="link" htmlEscape="false" maxlength="200" class="form-control" />
									<sys:ckfinder input="link" type="files" uploadPath="/files" selectMultiple="false" maxWidth="100" maxHeight="100" />
									<span class="help-inline">绝对或相对地址。</span>
								</div>
							</div>
							<c:if test="${fns:getConfig('custmor.code') eq 'bjfu'}">
								<div class="form-group">
									<label for="" class="col-sm-1 control-label">视频连接：</label>
									<div class="col-sm-3">
										<form:input path="movieLink" htmlEscape="false" maxlength="200" class="form-control" />
									</div>
									<div class="col-sm-7 text-muted">仅招生专业栏目下文章可用</div>
								</div>
							</c:if>
							<div class="form-group">
								<label class="col-sm-1  control-label">关键字:</label>
								<div class="col-sm-3">
									<form:input path="keywords" htmlEscape="false" maxlength="200" class="form-control" />
								</div>
								<div class="col-sm-7 text-muted">多个关键字，用空格分隔。</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">权重:</label>
								<div class="col-sm-1">
									<form:input path="weight" id="weight" htmlEscape="false" maxlength="200" class="form-control required" />
								</div>
								<div class="col-sm-3 zdInn">
									<span class="zdtxt"> <input type="checkbox" id="weightTop" class="chbox" onclick="$('#weight').val(this.checked?'999':'0')"> <label
										for="weightTop" style="cursor:pointer;margin-left:5px;">置顶</label> <label for="">过期时间：</label>
									</span>
									<div class="input-group date">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" name="weightDate" id="weightDate" readonly="readonly" maxlength="20" class="form-control pull-left"
											value="<fmt:formatDate value="${article.weightDate}" pattern="yyyy-MM-dd"/>"
											onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
									</div>
								</div>
								<div class="col-sm-5 text-muted">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">摘要:</label>
								<div class="col-sm-6">
									<form:textarea path="description" htmlEscape="false" rows="4" maxlength="200" class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">缩略图:</label>
								<div class="col-sm-4">
									<input type="hidden" id="image" name="image" value="${article.image}" />
									<!--yunzhi   start  -->
									<c:if test="${article.category.parent.id eq '5334d4a1b5b24f688f71a8822cfd507b'}">
										<sys:ckfinder input="image" type="images" uploadPath="/cms/article" selectMultiple="true" />
									</c:if>
									<c:if test="${article.category.parent.id ne '5334d4a1b5b24f688f71a8822cfd507b'}">
										<sys:ckfinder input="image" type="images" uploadPath="/cms/article" selectMultiple="false" />
									</c:if>
									<!--yunzhi  end  -->
									<%-- <sys:ckfinder input="image" type="images" uploadPath="/cms/article"  selectMultiple="true"/> --%>
								</div>
							</div>
							<c:if test="${article.category.parent.id eq '5334d4a1b5b24f688f71a8822cfd507b' && fns:getConfig('custmor.code') eq 'yunzhi'}">
								<div class="form-group">
									<label class="col-sm-1 control-label">文章类型:</label>
									<div class="col-sm-3">
										<form:select path="articleType" class="form-control select2">
											<form:options items="${fns:getDictList('atticle_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
										</form:select>
									</div>
								</div>
							</c:if>
							<div class="form-group">
								<label class="col-sm-1 control-label">正文:</label>
								<div class="col-sm-9">
									<form:textarea id="content" htmlEscape="true" path="articleData.content" rows="4" maxlength="200" class="form-control" />
									<sys:ckeditor replace="content" uploadPath="/cms/article" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">来源:</label>
								<div class="col-sm-3">
									<form:input path="articleData.copyfrom" htmlEscape="false" maxlength="200" class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">相关文章:</label>
								<div class="col-sm-6">
									<form:hidden id="articleDataRelation" path="articleData.relation" htmlEscape="false" maxlength="200" class="input-xlarge" />
									<ol id="articleSelectList"></ol>
									<button type="button" class="btn btn-block btn-success" id="relationButton" style="width:auto;">添加相关文章</button>
									<script type="text/javascript">
										var articleSelect = [];
										function articleSelectAddOrDel(id, title) {
											var isExtents = false,
												index = 0;
											for (var i = 0; i < articleSelect.length; i++) {
												if (articleSelect[i][0] == id) {
													isExtents = true;
													index = i;
												}
											}
											if (isExtents) {
												articleSelect.splice(index, 1);
											} else {
												articleSelect.push([ id, title ]);
											}
											articleSelectRefresh();
										}
										function articleSelectRefresh() {
											$("#articleDataRelation").val("");
											$("#articleSelectList").children().remove();
											for (var i = 0; i < articleSelect.length; i++) {
												$("#articleSelectList").append("<li>" + articleSelect[i][1] + "&nbsp;&nbsp;<a href=\"javascript:\" onclick=\"articleSelectAddOrDel('" + articleSelect[i][0] + "','" + articleSelect[i][1] + "');\">×</a></li>");
												$("#articleDataRelation").val($("#articleDataRelation").val() + articleSelect[i][0] + ",");
											}
										}
										$.getJSON("${ctx}/cms/article/findByIds", {
											ids : $("#articleDataRelation").val()
										}, function(data) {
											for (var i = 0; i < data.length; i++) {
												articleSelect.push([ data[i][1], data[i][2] ]);
											}
											articleSelectRefresh();
										});
										$("#relationButton").click(function() {
											$.jBox.open("iframe:${ctx}/cms/article/selectList?pageSize=5", "添加相关", 1050, 500, {
												buttons : {
													"确定" : true
												},
												loaded : function(h) {
													$(".jbox-content", top.document).css("overflow-y", "hidden");
												}
											});
										});
									</script>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">是否允许评论:</label>
								<div class="col-sm-7 radio">
									<label style="margin-right:10px"> <input type="radio" name="articleData.allowComment" value="1"
										${'1' eq article.articleData.allowComment ? 'checked=\"true\"' : ''}>是
									</label> <label> <input type="radio" name="articleData.allowComment" value="0"
										${'0' eq article.articleData.allowComment ? 'checked=\"true\"' : ''}>否
									</label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">推荐位:</label>
								<div class="col-sm-7 checkbox">
									<c:forEach items="${fns:getDictList('cms_posid')}" var="dic" varStatus="status">
										<label style="margin-right:10px"> <input type="checkbox" name="posidList" id="posidList" value="${dic.value}"
											<c:if test="${fn:contains(article.posidList,dic.value)}">checked="true"</c:if>>${dic.label}
										</label>
									</c:forEach>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">发布时间:</label>
								<div class="col-sm-2">
									<div class="input-group date">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<jsp:useBean id="now" class="java.util.Date" />
										<input type="text" id="releaseDate" name="releaseDate"
											value="<fmt:formatDate value="${empty article.releaseDate ? now : article.releaseDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
											onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" class="form-control pull-left">
									</div>
								</div>
							</div>
							<shiro:hasPermission name="cms:article:audit">
								<div class="form-group">
									<label class="col-sm-1 control-label">发布状态:</label>
									<div class="col-sm-7 radio">
										<c:forEach items="${fns:getDictList('cms_del_flag')}" var="dic" varStatus="status">
											<label style="margin-right:10px"> <input type="radio" name="delFlag" value="${dic.value}"
												<c:if test="${dic.value == article.delFlag }">checked="true"</c:if> htmlEscape="false" class="required">${dic.label}
											</label>
										</c:forEach>
									</div>
								</div>
							</shiro:hasPermission>
							<shiro:hasPermission name="cms:category:edit">
								<div class="form-group">
									<label class="col-sm-1 control-label">自定义内容视图:</label>
									<div class="col-sm-2">
										<form:select path="customContentView" class="form-control select2">
											<form:options items="${fns:getDictList('front_article_view_detail')}" itemLabel="label" itemValue="value" htmlEscape="false" />
										</form:select>
									</div>
									<div class="col-sm-7 text-muted">自定义内容视图名称必须以"frontViewArticle"开始</div>
								</div>
								<div class="form-group">
									<label class="col-sm-1 control-label">自定义视图参数:</label>
									<div class="col-sm-2">
										<form:input path="viewConfig" htmlEscape="true" class="form-control" />
									</div>
									<div class="col-sm-7 text-muted">视图参数例如: {count:2, title_show:"yes"}</div>
								</div>
							</shiro:hasPermission>
							<c:if test="${not empty article.id}">
								<div class="form-group">
									<label class="col-sm-1 control-label">查看评论:</label>
									<div class="col-sm-1">
										<button type="button" id="btnComment" class="btn btn-block btn-success"
											onclick="viewComment('${ctx}/cms/comment/?module=article&contentId=${article.id}&status=0')">查看评论</button>
										<script type="text/javascript">
											function viewComment(href) {
												top.$.jBox.open('iframe:' + href, '查看评论', $(top.document).width() - 220, $(top.document).height() - 180, {
													buttons : {
														"关闭" : true
													},
													loaded : function(h) {
														$(".jbox-content", top.document).css("overflow-y", "hidden");
														$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
														$("body", h.find("iframe").contents()).css("margin", "10px");
													}
												});
												return false;
											}
										</script>
									</div>
								</div>
							</c:if>
							<div class="form-group">
								<div class="col-sm-offset-1 col-sm-11">
									<shiro:hasPermission name="cms:article:edit">
										<input id="btnSubmit" class="btn btn-primary" style="width:90px;margin-right:10px" type="submit" value="保 存" />&nbsp;</shiro:hasPermission>
									<input id="btnCancel" class="btn btn-default" type="button" value="返 回" onclick="history.go(-1)" />
								</div>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
	<!-- /.content-wrapper -->
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script type="text/javascript">
		$(function() {
			// 按钮鼠标停留效果
			$('.form-information button').tooltip();
			$(".select2").select2();
			if ($("#link").val()) {
				$('#linkBody').show();
				$('#url').attr("checked", true);
			}
			$("#title").focus();
			$("#inputForm").validate({
				submitHandler : function(form) {
					if ($("#categoryId").val() == "") {
						$("#categoryName").focus();
						top.$.jBox.tip('请选择归属栏目', 'warning');
					} else if (CKEDITOR.instances.content.getData() == "" && $("#link").val().trim() == "") {
						top.$.jBox.tip('请填写正文', 'warning');
					} else {
						loading('正在提交，请稍等...');
						form.submit();
					}
				},
				errorContainer : "#messageBox",
				errorPlacement : function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
	
			//颜色选择器
			$('#cp3').colpick({
				colorScheme : 'dark',
				layout : 'rgbhex',
				color : '000000',
				onSubmit : function(hsb, hex, rgb, el) {
					$(el).css('background-color', '#' + hex);
					$(el).colpickHide();
					$("#cp3text").css("color", '#' + hex);
					$("#cp3textcolor").val('#' + hex);
				}
			});
		});
	</script>
</body>
</html>