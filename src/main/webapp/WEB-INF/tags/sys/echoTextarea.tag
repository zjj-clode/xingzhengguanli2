<%-- 回显textarea提交的内容，自动换行、自适应调整高度和宽度 --%>
<%@ tag language="java" pageEncoding="UTF-8" description="回显textarea提交的内容，自动换行、自适应调整高度和宽度" body-content="empty" trimDirectiveWhitespaces="true"  %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="false"%>
<%@ attribute name="name" type="java.lang.String" required="false"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="内容，必填项"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false"%>
<%@ attribute name="rows" type="java.lang.Integer" required="false"%>
<%@ attribute name="cols" type="java.lang.Integer" required="false"%>
<%request.setAttribute("strEnter", "\n");%>
<c:set var="strEnterItems" value="${fn:split(value,strEnter)}"/>
<c:set var="strEnterNum" value="${fn:length(strEnterItems)}"/>
<c:set var="maxLengthStrEnterItem" value="${0}"/>
<c:forEach items="${strEnterItems}" var="strEnterItem">
<c:set var="strEnterItemLength" value="${fn:length(strEnterItem)}"/>
<c:set var="maxLengthStrEnterItem" value="${strEnterItemLength gt maxLengthStrEnterItem ? strEnterItemLength : maxLengthStrEnterItem}"/>
</c:forEach>
<textarea rows="${not empty rows ? rows : strEnterNum}" cols="${empty cols ? maxLengthStrEnterItem : cols}" <c:if test="${not empty id}">id="${id}"</c:if> <c:if test="${not empty name}">name="${name}"</c:if> <c:if test="${not empty cssClass}">class="${cssClass}"</c:if> 
readonly="readonly"  style="border:none;resize:none;background:none;cursor:text;${cssStyle}">${value}</textarea>