<%-- 中国省市地图   动态数据切换 http://gallery.echartsjs.com/editor.html?c=xHkU_8Avbe --%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="显示图标的div的id"%>
<%@ attribute name="text" type="java.lang.String" required="false" description="主标题"%>
<%@ attribute name="subtext" type="java.lang.String" required="false" description="副标题"%>
<%@ attribute name="listHashMap" type="java.util.List" required="true" description="数据Map的List集合，数据结构：List&lt;HashMap&lt;String, Object&gt;&gt;"%>
<%@ attribute name="labelKeyName" type="java.lang.String" required="false" description="HashMap中作为label的key，如：stalabel"%>
<%@ attribute name="valueKeyName" type="java.lang.String" required="false" description="HashMap中作为value的key，如：stavalue"%>
<%@ attribute name="showLoading" type="java.lang.Boolean" required="false" description="是否显示加载画面，默认为true"%>

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

	var rltData_${id} = [
		<c:forEach items="${listHashMap}" var="hashMap" varStatus="status">      
		 {
			name : '${fn:replace(fn:replace(fn:replace(fn:replace(hashMap[labelKeyName],'省',''),'市',''),'区',''),'港澳台','台湾')}',
			value : ${hashMap[valueKeyName] + 0}
		 }<c:if test="${!status.last}">,</c:if> 
		 </c:forEach>
	];
	
	var curIndx_${id} = 0;
	
	option_${id}={
	    title: {
	        x: 'center',
	        y: '180',
	        text : '${empty text ? '数据统计' : text}',
	        subtext: rltData_${id}[curIndx_${id}].name + '录取人数为：' + rltData_${id}[curIndx_${id}].value,
	        textStyle: {
	            color: '#888',
	            fontSize: 16
	        }
	    },
	    series: [{
	         type: 'map',
	         mapType: 'china',
	         itemStyle: {
	            normal: {
	                areaColor: '#404a59',
	                borderColor: 'rgba(100,149,237,1)',
	                borderWidth: 0.5
	            },
	            emphasis: {
	                borderWidth: 3,
	                borderColor: '#fff',
	                areaColor: '#32cd32',
	                label: {
	                    shadowColor: '#fff', //默认透明
	                    shadowBlur: 10,
	                    show: true
	                }
	            }
	        },
	        data: (function() {
	            var res = [];
	            var len = -1;
	            while (len++ < rltData_${id}.length - 1) {
	                res.push({
	                    name: rltData_${id}[len].name,
	                    value: rltData_${id}[len].value,
	                    selected: ifSelecte(len)
	                });
	            }
	            return res;
	        })()
	    }]
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
	
	
	setInterval(function() {
	    curIndx_${id} = (curIndx_${id} + 1) % rltData_${id}.length;
	    myChart_${id}.setOption({
	        title: {
	            x: 'center',
	            y: '180',
	            text : '${empty text ? '数据统计' : text}',
	        	subtext: rltData_${id}[curIndx_${id}].name + '录取人数为：' + rltData_${id}[curIndx_${id}].value,
	            textStyle: {
	                color: '#888',
	                fontSize: 16
	            }
	        },
	        toolbox: {
		        show : true,
		        feature : {
		            <c:if test="${false ne saveAsImage}">
		            saveAsImage : {show: true}
		            </c:if>
		        }
		    },
	       series: [{  
	            data: (function() {
	                var res = [];
	                var len = -1;
	                while (len++ < rltData_${id}.length - 1) {
	                    res.push({
	                        name: rltData_${id}[len].name,
	                        value: rltData_${id}[len].value,
	                        selected: ifSelecte(len)
	                    });
	                }
	                return res;
	            })()
	       }]
	    });
	}, 2000);
	
	var flag_${id} = false;
	function ifSelecte(num) {
	    if (num == curIndx_${id}) {
	        flag_${id} = true
	    } else {
	        flag_${id} = false
	    }
	    return flag_${id};
	}
</script>