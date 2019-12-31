<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" 	uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" 		uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="shiro" 	uri="/WEB-INF/tlds/shiros.tld" %>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fns" 	uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="fnc" 	uri="/WEB-INF/tlds/fnc.tld" %>
<%@ taglib prefix="fnz" 	uri="/WEB-INF/tlds/fnz.tld" %>

<%@ taglib prefix="sys" 	tagdir="/WEB-INF/tags/sys" %>
<%@ taglib prefix="frt" 	tagdir="/WEB-INF/tags/frt" %>
<%@ taglib prefix="act" 	tagdir="/WEB-INF/tags/act" %>
<%@ taglib prefix="cms" 	tagdir="/WEB-INF/tags/cms" %>
<%@ taglib prefix="echart" 	tagdir="/WEB-INF/tags/echart" %>

<c:set var="ctxPath"                value="${pageContext.request.contextPath}"/>
<c:set var="ctx" 					value="${pageContext.request.contextPath}${fns:getAdminPath()}"/>
<c:set var="ctxf" 					value="${pageContext.request.contextPath}${fns:getFrontPath()}"/>

<c:set var="ctxStatic" 				value="${pageContext.request.contextPath}/static"/>
<c:set var="ctxStaticLTE" 			value="${ctxStatic}/lte"/>
<c:set var="ctxStaticBaseLTE" 		value="${ctxStaticLTE}/modules/cms/front/themes/basic"/>
<c:set var="ctxStaticFront" 		value="${ctxStatic}/front/${fns:getSysConfig('custmor.code')}"/>
<c:set var="ctxStaticLteFront" 		value="${ctxStaticLte}/modules/cms/front"/>
<c:set var="ctxStaticModulesFront" 	value="${ctxStatic}/modules/front"/>
<c:set var="ctxStaticTheme" 		value="${ctxStatic}/front/${fns:getConfig('custmor.code')}"/>
<c:set var="ctxStaticLteTheme" 		value="${ctxStaticLteFront}/themes/basic"/>
<c:set var="ctxStaticMobileFront" 	value="${ctxStatic}/mobile/front/${fns:getSysConfig('custmor.code')}/${fns:getSysConfig('custmor.theme')}"/>
<c:set var="ctxStaticThemeMobile" 	value="${ctxStaticFront}/themes/mobile"/>
<c:set var="urlSuffix" 				value="${fns:getUrlSuffix()}"/>
<c:set var="root" 					value="${pageContext.request.contextPath}"/>








<%-- 
<meta name="_csrf" content="${sessionScope['com.thinkgem.jeesite.modules.sys.security.csrf.CSRFTokenManager.tokenval']}"/>
<meta name="_csrf_header" content="CSRFToken"/>
<script type="text/javascript">
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$.ajaxSetup({
    beforeSend: function (xhr) {
        if(header && token ){
            xhr.setRequestHeader(header, token);
        }
    }}
); 
</script>
--%>









