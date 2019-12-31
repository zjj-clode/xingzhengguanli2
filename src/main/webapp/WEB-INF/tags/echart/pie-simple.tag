<%-- 简单饼图  http://echarts.baidu.com/demo.html#pie-simple --%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="显示图标的div的id"%>
<%@ attribute name="text" type="java.lang.String" required="false" description="主标题"%>
<%@ attribute name="subtext" type="java.lang.String" required="false" description="副标题"%>
<%@ attribute name="listHashMap" type="java.util.List" required="true" description="数据Map的List集合，数据结构：List&lt;HashMap&lt;String, Object&gt;&gt;"%>
<%@ attribute name="labelKeyName" type="java.lang.String" required="false" description="dataMap中作为label的key，如：stalabel"%>
<%@ attribute name="valueKeyName" type="java.lang.String" required="false" description="dataMap中作为value的key，如：stavalue"%>
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
			title : {
				text : '${empty text ? '数据统计' : text}',
				subtext : '${empty subtext ? '数据来源：本系统' : subtext}',
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{b} <br/>数量及占比 : {c} （{d}%）"
			},
			toolbox: {
		        show : true,
		        feature : {
		            <c:if test="${false ne saveAsImage}">
		            saveAsImage : {show: true}
		            </c:if>
		        }
		    },
			legend : {
				orient : 'vertical',
				left : 'left',
				data : [
				        <c:forEach items="${listHashMap}" var="hashMap" varStatus="status">
				        /* '${hashMap[labelKeyName]}'<c:if test="${!status.last}">,</c:if> */ 
				        '<c:if test="${empty hashMap[labelKeyName]}">未知${status.index+1}</c:if><c:if test="${not empty hashMap[labelKeyName]}">${hashMap[labelKeyName]}</c:if>'<c:if test="${!status.last}">,</c:if>
				 		</c:forEach>
				        ]
			},
			series : [ {
				<%--name : '${seriesName}',--%>
				type : 'pie',
				radius : '55%',
				center : [ '50%', '60%' ],
				data : [ 
				<c:forEach items="${listHashMap}" var="hashMap" varStatus="status">      
				 {
					<%-- name : '${hashMap[labelKeyName]}', --%>
					name : '<c:if test="${empty hashMap[labelKeyName]}">未知${status.index+1}</c:if><c:if test="${not empty hashMap[labelKeyName]}">${hashMap[labelKeyName]}</c:if>',
					value : ${hashMap[valueKeyName] + 0}
				 }<c:if test="${!status.last}">,</c:if> 
				 </c:forEach>
				 ],
				itemStyle : {
					emphasis : {
						shadowBlur : 10,
						shadowOffsetX : 0,
						shadowColor : 'rgba(0, 0, 0, 0.5)'  
					}
				}
			} ]
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