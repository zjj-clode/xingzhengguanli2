<%
response.setStatus(401);
%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
	<title>401 - 登录后才能操作</title>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header"><h1>你没有登录或你没有权限进行此操作.</h1></div>
	</div>
</body>
</html>