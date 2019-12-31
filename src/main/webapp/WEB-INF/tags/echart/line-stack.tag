<%-- 折线图堆叠  http://echarts.baidu.com/demo.html#line-stack --%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="显示图标的div的id"%>
<%@ attribute name="text" type="java.lang.String" required="false" description="主标题"%>
<%@ attribute name="subtext" type="java.lang.String" required="false" description="副标题"%>
<%@ attribute name="linkedHashMapListHashMap" type="java.util.Map" required="true" description="数据Map，数据结构：LinkedHashMap&lt;String, List&lt;HashMap&lt;String, Object&gt;&gt;&gt;"%>
<%@ attribute name="labelKeyName" type="java.lang.String" required="false" description="HashMap中作为label的key，如：stalabel"%>
<%@ attribute name="valueKeyName" type="java.lang.String" required="false" description="HashMap中作为value的key，如：stavalue"%>
<%@ attribute name="showLoading" type="java.lang.Boolean" required="false" description="是否显示加载画面，默认为true"%>
<%@ attribute name="saveAsImage" type="java.lang.Boolean" required="false" description="是否显示保存图片按钮，默认为true"%>

<c:set var="showLoading" value="${not empty showLoading and showLoading eq false ? false : true}" scope="page"/>
<c:set var="labelKeyName" value="${empty labelKeyName ? 'stalabel' : labelKeyName}" scope="page"/>
<c:set var="valueKeyName" value="${empty valueKeyName ? 'stavalue' : valueKeyName}" scope="page"/>

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
	        trigger: 'axis'
	    },
	    legend: {
	        data:[
	        <%-- 统计类别
	        '农村','城镇','全部' 
	        --%>
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
	        type: 'category',
	        boundaryGap: false,
	        data: [
	        <%-- x轴：年份
	        '2010','2011','2012','2013','2014','2015','2016','2017' 
	        --%>
	        <c:forEach items="${linkedHashMapListHashMap}" var="entry" varStatus="status" end='0'>
	        	<c:forEach items="${entry.value}" var="hashMap" varStatus="subStatus">
	        		<c:if test="${not empty hashMap[labelKeyName]}">
	        		'${hashMap[labelKeyName]}'<c:if test="${!subStatus.last}">,</c:if>
	        		</c:if>
	        	</c:forEach>
	        </c:forEach>
	        ]
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: [
	        <%-- data中是每条线上的y坐标值
	        {
	            name:'农村',
	            type:'line',
	            stack: '总量',
	            areaStyle: {normal: {}},
	            data:[120, 330, 210, 320, 230, 120, 230, 210]
	        },
	        {
	            name:'城镇',
	            type:'line',
	            stack: '总量',
	            areaStyle: {normal: {}},
	            data:[120, 330, 210, 320, 230, 120, 230, 210]
	        },
	        {
	            name:'全部',
	            type:'line',
	            stack: '总量',
	            label: {
	                normal: {
	                    show: true,
	                    position: 'top'
	                }
	            },
	            areaStyle: {normal: {}},
	            data:[240, 660,420, 640,460, 240, 460, 240]
	        } 
	        --%>
	        <c:forEach items="${linkedHashMapListHashMap}" var="entry" varStatus="status">
	        {
	            name: '${entry.key}',
	            type: 'line',
	            areaStyle: {normal: {}},
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