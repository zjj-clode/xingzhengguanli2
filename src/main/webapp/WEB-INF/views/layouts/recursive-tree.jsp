<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%-- 递归生成 li 以及其子子孙孙。此处支持无限极循环，但后台获取的菜单级数可能只有3级。 --%>
<c:choose>
	<%-- 如果有下级菜单 --%>
	<c:when test="${not empty menu.childList}">
		<li id="${menu.id}" data-ename="${menu.ename}" class="treeview"><a href="javascript:void(0);"><i class="fa fa-caret-right" style="width:auto;"></i> <span>${menu.name}</span></a>
			<ul class="treeview-menu">
				<c:forEach items="${menu.childList}" var="subMenu">
					<c:if test="${nowMenuId eq '0'}">
						<c:if test="${subMenu.ename eq ename}">
							<c:set var="nowMenuId" value="${subMenu.id}" scope="request" />
						</c:if>
					</c:if>
					<%-- 递归调用 --%>
					<c:set var="menu" value="${subMenu}" scope="request" />
					<jsp:include page="recursive-tree.jsp" />
				</c:forEach>
			</ul></li>
	</c:when>
	<%-- 没有下级菜单 --%>
	<c:otherwise>
		<li id="${menu.id}" data-ename="${menu.ename}" tabindex="">
			<a href="${fn:indexOf(menu.href,'://') eq -1 ? ctx : ''}${not empty menu.href ? menu.href : '/404'}"> 
				<i class="fa fa ${not empty menu.icon ? menu.icon : 'fa-calculator'}"></i> ${menu.name} 
			</a>
		</li>
	</c:otherwise>
</c:choose>