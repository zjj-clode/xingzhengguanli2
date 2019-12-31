<%-- 条形图  http://echarts.baidu.com/demo.html#bar-y-category --%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="显示图标的div的id"%>
<%@ attribute name="text" type="java.lang.String" required="false" description="主标题"%>
<%@ attribute name="subtext" type="java.lang.String" required="false" description="副标题"%>
<%@ attribute name="linkedHashMapListHashMap" type="java.util.Map" required="true" description="数据Map，数据结构：LinkedHashMap&lt;String, List&lt;HashMap&lt;String, Object&gt;&gt;&gt;"%>
<%@ attribute name="labelKeyName" type="java.lang.String" required="false" description="dataMap中作为label的key，如：stalabel"%>
<%@ attribute name="valueKeyName" type="java.lang.String" required="false" description="dataMap中作为value的key，如：stavalue"%>
<%@ attribute name="showLoading" type="java.lang.Boolean" required="false" description="是否显示加载画面，默认为true"%>
<%@ attribute name="saveAsImage" type="java.lang.Boolean" required="false" description="是否显示保存图片按钮，默认为true"%>

<c:set var="showLoading" value="${not empty showLoading and showLoading eq false ? false : true}" scope="page"/>
<c:set var="labelKeyName" value="${empty labelKeyName ? 'stalabel' : labelKeyName}" scope="page"/>
<c:set var="valueKeyName" value="${empty valueKeyName ? 'stavalue' : valueKeyName}" scope="page"/>


<%-- 此处处理label（实际上应该在后台就应该把数据整理好，使label一致） --%>
<%-- 以第一个ListHashMap的label为准 
<c:forEach items="${linkedHashMapListHashMap}" var="entry" varStatus="status" end='0'>
	<c:set var="maxLenMap" value="${linkedHashMapListHashMap[entry.key]}"/>
</c:forEach>
--%>
<%-- 以最长ListHashMap的label为准 --%>
<c:set var="maxLen" value="0"/>
<c:set var="maxLenMapKey" value=""/>
<c:forEach items="${linkedHashMapListHashMap}" var="entry" varStatus="status">
	<c:if test="${maxLen < fn:length(entry.value)}">
		<c:set var="maxLen" value="${fn:length(entry.value)}"/>
		<c:set var="maxLenMapKey" value="${entry.key}"/>
	</c:if>
</c:forEach>
<c:set var="maxLenMap" value="${linkedHashMapListHashMap[maxLenMapKey]}"/>


<script type="text/javascript"> 	
	var myChart_${id} = echarts.init(document.getElementById("${id}"));
	
	<c:if test="${false ne showLoading}">
	myChart_${id}.showLoading({
	    text: '正在努力的读取数据中...',
	});
	</c:if>
	
	var option_${id} = {
		title: {
	        text : '${empty text ? '数据统计' : text}',
			subtext : '${empty subtext ? '数据来源：本系统' : subtext}'
	    },
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    legend: {
	        data: [
	        <c:forEach items="${linkedHashMapListHashMap}" var="entry" varStatus="status">
				'${entry.key}'<c:if test="${!status.last}">,</c:if>
			</c:forEach>
	        ]
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            <c:if test="${false ne saveAsImage}">
	            saveAsImage : {show: true}
	            </c:if>
	        }
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        boundaryGap: [0, 0.01]
	    },
	    yAxis: {
	        type: 'category',
	        data: [
	        <c:forEach items="${maxLenMap}" var="hashMap" varStatus="subStatus">
        		'${hashMap[labelKeyName]}'<c:if test="${!subStatus.last}">,</c:if>
        	</c:forEach>
	        ]
	    },
	    series: [
	    	<c:forEach items="${linkedHashMapListHashMap}" var="entry" varStatus="status">
	        {
	            name: '${entry.key}',
	            type: 'bar',
	            data: [
	            <c:forEach items="${entry.value}" var="hashMap" varStatus="subStatus">
	            	${hashMap[valueKeyName] + 0}<c:if test="${!subStatus.last}">,</c:if>
				</c:forEach>
	            ]
	        }<c:if test="${!status.last}">,</c:if>
	    	</c:forEach>
	    ]
	};

	<c:if test="${false ne showLoading}">
	setTimeout(function(){
		myChart_${id}.hideLoading();
	</c:if>
	
		if (option_${id} && typeof option_${id} === "object") {
			myChart_${id}.setOption(option_${id}, true);
		}
		
	<c:if test="${false ne showLoading}">
	}, 500);
	</c:if>
</script>