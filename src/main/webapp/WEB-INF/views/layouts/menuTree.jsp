<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%-- 左边菜单树 --%>
<ul class="sidebar-menu">
	<li class="header"></li>
	<c:if test="${empty treeMenuList}">
		<%-- <c:set var="treeMenuList" value="${fns:getChildMenuList()}" /> --%>
		<c:set var="treeMenuList" value="${fns:getMenuList()}" />
	</c:if>
	<c:set var="nowMenuId" value="0" scope="request"/>
	<c:forEach items="${treeMenuList}" var="menu">
		<c:if test="${nowMenuId eq '0'}">
			<c:if test="${not empty ename and menu.ename eq ename}">
				<c:set var="nowMenuId" value="${menu.id}" scope="request"/>
			</c:if>
		</c:if>
		<%@include file="/WEB-INF/views/layouts/recursive-tree.jsp"%>
	</c:forEach>
</ul>
<script type="text/javascript">
	<%-- java方式获取当前菜单 --%>
	var m = $("#${nowMenuId}");
	if (m != null) {
		m.addClass("active").parents("li").addClass("active").end().find("a").addClass("current");
	}
</script>
<%-- 覆盖static/lte/dist/css/skins/_all-skins.min.css的第173行。待直接修改其样式 --%>
<style>
.skin-purple .treeview-menu > li.active > a{
	background: none;
}
.skin-purple .treeview-menu > li > a.current{
	background: #00c1de none repeat scroll 0 0;
	color: #fff;
}
</style>