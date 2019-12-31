<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <title><sitemesh:title/> -- 招生宣传服务平台</title>
  <link rel="stylesheet" href="${ctxStaticLTE}/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/font-awesome/font-awesome.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/ionicons/ionicons.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/select2/select2.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/My97DatePicker/skin/WdatePicker.css">
<%-- <link rel="stylesheet" href="${ctxStaticLTE}/plugins/datepicker/datepicker3.css"> --%>
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/iCheck/flat/blue.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/treeTable/jquery.treetable.theme.default.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/dataTables.bootstrap.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/jquery.dataTables.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/dataTables.semanticui.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/semantic_table.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/datatables/extensions/FixedColumns/css/dataTables.fixedColumns.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/jBox/jBox.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/colorpicker/colpick.css">
<link rel="stylesheet" href="${ctxStaticLTE}/dist/css/AdminLTE.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/dist/css/skins/_all-skins.min.css">
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/jquery-validation/1.11.1/jquery.validate.min.css">
<!--[if lt IE 9]>
  <script src="${ctxStaticLTE}/plugins/ie8/html5shiv.min.js"></script>
  <script src="${ctxStaticLTE}/plugins/ie8/respond.min.js"></script>
  <link rel="stylesheet" href="${ctxStaticLTE}/plugins/ie8/ie8-hack.css">
  <![endif]-->
<!--[if !IE]> -->
<script src="${ctxStatic}/jquery/j2.js"></script>
<!-- <![endif]-->
<!--[if IE]>
  	<script src="${ctxStatic}/jquery/j1.js"></script>
<![endif]-->
<script src="${ctxStaticLTE}/plugins/jBox/jBox.min.js"></script>
<script src="${ctxStatic}/common/batchDelete.js"></script>
<script src="${ctxStaticLTE}/common.js"></script>
<script type="text/javascript">var ctx = '${ctx}',
		ctxStatic = '${ctxStatic}',
		ctxStaticLTE = '${ctxStaticLTE}';
</script>
  <sitemesh:head/>
</head>
</head>
<body style="min-width: auto;">
	<sitemesh:body/>
</body>
<script type="text/javascript">
jQuery.validator.addMethod("chinese", function(value, element) {
    var tel = /^[^\u4e00-\u9fa5]{0,}$/;
    return this.optional(element) || (tel.test(value));
}, "请不要输入中文");
</script>
</html>