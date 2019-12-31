<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
    <link rel="stylesheet" href="${ctxStaticLTE}/plugins/ztree/zTreeStyle.css">
  <script src="${ctxStaticLTE}/plugins/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
</head>
<body>
    <section class="content-header">
      <h1>
                            系统管理
        <small>角色信息添加与修改</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="${ctx}/sys/role">角色管理</a></li>
        <li class="active">信息添加与修改</li>
      </ol>
    </section>
    <!-- /.content-header -->
    
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <form:form id="inputForm" modelAttribute="role" action="${ctx}/sys/role/save" method="post" class="form-horizontal form-information pad  form-lie">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>	
			<div class="form-group">
				<label class="col-sm-1 control-label"><span class="text-red fm">*</span>归属机构:</label>
				<div class="col-sm-3">
	                <sys:treeselect id="office" name="office.id" value="${role.office.id}" labelName="office.name" labelValue="${role.office.name}"
						title="机构" url="/sys/office/treeData" cssClass="form-control required"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label"><span class="text-red fm">*</span>角色名称:</label>
				<div class="col-sm-3">
					<input id="oldName" name="oldName" type="hidden" value="${role.name}">
					<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label"><span class="text-red fm">*</span>英文名称:</label>
				<div class="col-sm-3">
					<input id="oldEnname" name="oldEnname" type="hidden" value="${role.enname}">
					<form:input path="enname" htmlEscape="false" maxlength="50" class="form-control required"/>
				</div>
				<div class="col-sm-7 text-muted">工作流用户组标识</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">角色类型:</label>
				<div class="col-sm-3">
					<form:select path="roleType" class="form-control selec2">
						<form:option value="assignment">任务分配</form:option>
						<form:option value="security-role">管理角色</form:option>
						<form:option value="user">普通角色</form:option>
					</form:select>
				</div>
				<div class="col-sm-7 text-muted" title="activiti有3种预定义的组类型：security-role、assignment、user 如果使用Activiti Explorer，需要security-role才能看到manage页签，需要assignment才能claim任务">工作流组用户组类型（任务分配：assignment、管理角色：security-role、普通角色：user）</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">是否系统数据:</label>
				<div class="col-sm-2">
					<form:select path="sysData" class="form-control select2">
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="col-sm-8 text-muted">“是”代表此数据只有超级管理员能进行修改，“否”则表示拥有角色修改人员的权限都能进行修改</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">是否可用</label>
				<div class="col-sm-2">
					<form:select path="useable" class="form-control select2">
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="col-sm-8 text-muted">“是”代表此数据可用，“否”则表示此数据不可用</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">数据范围:</label>
				<div class="col-sm-3">
					<form:select path="dataScope" class="form-control select2">
						<form:options items="${fns:getDictList('sys_data_scope')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="col-sm-7 text-muted">特殊情况下，设置为“按明细设置”，可进行跨机构授权</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">角色授权:</label>
				<div class="col-sm-5">
					<div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
					<form:hidden path="menuIds"/>
					<div id="officeTree" class="ztree" style="margin-left:100px;margin-top:3px;float:left;"></div>
					<form:hidden path="officeIds"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">备注:</label>
				<div class="col-sm-3">
					<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
				</div>
			</div>
			<div class="form-group">
			   <div class="col-sm-offset-1 col-sm-11">
				<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
					<shiro:hasPermission name="sys:role:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				</c:if>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				</div>
			</div>
		</form:form>
         </div>
     </div>
   </section>
    <!-- /.content -->	
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>	
<script type="text/javascript">

	$(document).ready(function(){
		$("#name").focus();
	    $(".select2").select2();
	    $("#inputForm .select2").change(function (){
		   $(this).valid();
	    });
       // 按钮鼠标停留效果
        $('.form-information button').tooltip();
		$("#inputForm").validate({
			rules: {
				name: {remote: "${ctx}/sys/role/checkName?oldName=" + encodeURIComponent("${role.name}")},
				enname: {remote: "${ctx}/sys/role/checkEnname?oldEnname=" + encodeURIComponent("${role.enname}")}
			},
			messages: {
				name: {remote: "角色名已存在"},
				enname: {remote: "英文名已存在"}
			},
			submitHandler: function(form){
				var ids = [], nodes = tree.getCheckedNodes(true);
				for(var i=0; i<nodes.length; i++) {
					ids.push(nodes[i].id);
				}
				$("#menuIds").val(ids);
				var ids2 = [], nodes2 = tree2.getCheckedNodes(true);
				for(var i=0; i<nodes2.length; i++) {
					ids2.push(nodes2[i].id);
				}
				$("#officeIds").val(ids2);
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append") || element.parent().is(".date")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});

		var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
				data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
					tree.checkNode(node, !node.checked, true, true);
					return false;
				}}};
		
		// 用户-菜单
		var zNodes= ${menuList};
		// 初始化树结构
		var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
		// 不选择父节点
		tree.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
		// 默认选择节点
		var ids = "${role.menuIds}".split(",");
		for(var i=0; i<ids.length; i++) {
			var node = tree.getNodeByParam("id", ids[i]);
			try{tree.checkNode(node, true, false);}catch(e){}
		}
		// 默认展开全部节点
		tree.expandAll(true);
		
		// 用户-机构
		var zNodes2=${officeList}
		// 初始化树结构
		var tree2 = $.fn.zTree.init($("#officeTree"), setting, zNodes2);
		// 不选择父节点
		tree2.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
		// 默认选择节点
		var ids2 = "${role.officeIds}".split(",");
		for(var i=0; i<ids2.length; i++) {
			var node = tree2.getNodeByParam("id", ids2[i]);
			try{tree2.checkNode(node, true, false);}catch(e){}
		}
		// 默认展开全部节点
		tree2.expandAll(true);
		// 刷新（显示/隐藏）机构
		refreshOfficeTree();
		$("#dataScope").change(function(){
			refreshOfficeTree();
		});
	});
	function refreshOfficeTree(){
		if($("#dataScope").val()==9){
			$("#officeTree").show();
		}else{
			$("#officeTree").hide();
		}
	}
</script>
</body>
</html>