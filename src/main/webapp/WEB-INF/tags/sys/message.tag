<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、error、loading"%>
<%@ attribute name="showMessageBoxDiv" type="java.lang.Boolean" required="false" description="是否显示messageBox的div"%>
<script type="text/javascript">top.$.jBox.closeTip();</script>
<c:if test="${not empty content}">
	<c:if test="${not empty type}"><c:set var="ctype" value="${type}"/></c:if><c:if test="${empty type}"><c:set var="ctype" value="${(fn:indexOf(content,'失败') > 0) or (fn:indexOf(content,'异常') > 0) ? 'error' : 'success'}"/></c:if>
	<c:if test="${showMessageBoxDiv ne false}">
	<div id="messageBox" class="alert alert-${ctype} hide"><button data-dismiss="alert" class="close" onclick="$('#messageBox').hide();">×</button>${content}</div> 
	</c:if>
	<script type="text/javascript">if(!top.$.jBox.tip.mess){top.$.jBox.tip.mess=1;top.$.jBox.tip("${content}","${ctype}",{persistent:true,opacity:0,timeout:${ctype eq 'success' ? 2000 : 5000}});<c:if test="${showMessageBoxDiv ne false}">$("#messageBox").show();</c:if>}</script>
</c:if>