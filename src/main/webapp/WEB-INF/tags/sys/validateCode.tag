<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="验证码输入框名称"%>
<%@ attribute name="placeholder" type="java.lang.String" required="false" description="验证码输入框placeholder"%>
<%@ attribute name="inputCssStyle" type="java.lang.String" required="false" description="验证框样式"%>
<%@ attribute name="inputCssClass" type="java.lang.String" required="false" description="验证框样式class"%>
<%@ attribute name="imageCssStyle" type="java.lang.String" required="false" description="验证码图片样式"%>
<%@ attribute name="imageCssClass" type="java.lang.String" required="false" description="验证码图片样式class"%>
<%@ attribute name="showButton" type="java.lang.Boolean" required="false" description="是否显示看不清按钮"%>
<%@ attribute name="buttonCssStyle" type="java.lang.String" required="false" description="看不清按钮样式"%>
<%@ attribute name="refreshText" type="java.lang.String" required="false" description="“看不清”文字"%>
<%@ attribute name="inputMaxLength" type="java.lang.Integer" required="false" description="验证框maxlength属性"%>
<input type="text" id="${name}" name="${name}" maxlength="${empty inputMaxLength ? '5' : inputMaxLength}" placeholder="${empty placeholder ? '验证码' : placeholder}" class="required ${inputCssClass}" style="${inputCssStyle}"/>
<img src="${pageContext.request.contextPath}/servlet/validateCodeServlet" onclick="$('.${name}').attr('src','${pageContext.request.contextPath}/servlet/validateCodeServlet?'+new Date().getTime());" class="${name} ${imageCssClass}" style="${imageCssStyle}" title="${empty refreshText ? '看不清，点我！' : refreshText}"/>
<c:if test="${false ne showButton}">
<a href="javascript:" onclick="$('.${name}').attr('src','${pageContext.request.contextPath}/servlet/validateCodeServlet?'+new Date().getTime());" class="${name}Refresh" style="${buttonCssStyle}">${empty refreshText ? '看不清' : refreshText}</a>
</c:if>