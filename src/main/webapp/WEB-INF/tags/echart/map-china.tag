<%-- 中国省市地图 http://gallery.echartsjs.com/editor.html?c=map-china-dataRange--%>
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

<%-- 分别找出子分类的最大值，相加，作为visualMap的max值 --%>
<c:set var="max" value="0" scope="page"/>
<c:forEach items="${linkedHashMapListHashMap}" var="entry" varStatus="status">
	<c:set var="max_sub" value="0" scope="page"/>
	<c:forEach items="${entry.value}" var="hashMap" varStatus="subStatus">
		<c:if test="${max_sub < hashMap[valueKeyName]}">
			<c:set var="max_sub" value="${hashMap[valueKeyName]}" scope="page"/>
		</c:if>
	</c:forEach>
	<c:set var="max" value="${max + max_sub}" scope="page"/>
</c:forEach>

<script type="text/javascript">
	var myChart_${id} = echarts.init(document.getElementById("${id}"));
	
	<c:if test="${false ne showLoading}">
	myChart_${id}.showLoading({
	    text: '正在努力的读取数据中...',
	});
	</c:if>
	
	var option_${id} = {  
		title : {
			text : '${empty text ? '数据统计' : text}',
			subtext : '${empty subtext ? '数据来源：本系统' : subtext}',
			left : 'center'
		},
		tooltip : {
			trigger : 'item'
		},
		legend : {
			orient : 'vertical',
			left : 'left',
			data : [
				<c:forEach items="${linkedHashMapListHashMap}" var="entry" varStatus="status">
				'${entry.key}'<c:if test="${!status.last}">,</c:if>
				</c:forEach>
			]
		},
		visualMap : {
			min : 0,
			max : ${max},
			left : 'left',
			top : 'bottom',
			text : [ '高', '低' ],
			calculable : true
		}, 
		toolbox: {
	        show : true,
	        feature : {
	            <c:if test="${false ne saveAsImage}">
	            saveAsImage : {show: true}
	            </c:if>
	        }
	    },
		series : [
		 <c:forEach items="${linkedHashMapListHashMap}" var="entry" varStatus="status">
		{
			name : '${entry.key}',
			type : 'map',
			mapType : 'china',
			roam : false,
			label : {
				normal : {
					show : true
				},
				emphasis : {
					show : true
				}
			},
			data : [ 
				<%-- 省份名称适配，使与js中定义的相匹配：去掉 '省'、'市'、'区'字样，'港澳台'替换成'台湾'(待议？) --%>
				<c:forEach items="${entry.value}" var="hashMap" varStatus="subStatus">
				 {
					name : '${fn:replace(fn:replace(fn:replace(fn:replace(hashMap[labelKeyName],'省',''),'市',''),'区',''),'港澳台','台湾')}',
					value : ${hashMap[valueKeyName] + 0}
				 }<c:if test="${!subStatus.last}">,</c:if>
				</c:forEach>
			]
		} <c:if test="${!status.last}">,</c:if>
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