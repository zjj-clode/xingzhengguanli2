<%-- 中国省市地图   全国地图迁徙  http://gallery.echartsjs.com/editor.html?c=xry6ib9nLW --%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="显示图标的div的id"%>
<%@ attribute name="text" type="java.lang.String" required="false" description="主标题"%>
<%@ attribute name="subtext" type="java.lang.String" required="false" description="副标题"%>
<%@ attribute name="listHashMap" type="java.util.List" required="true" description="数据Map的List集合，数据结构：List&lt;HashMap&lt;String, Object&gt;&gt;"%>
<%@ attribute name="labelKeyName" type="java.lang.String" required="false" description="HashMap中作为label的key，如：stalabel"%>
<%@ attribute name="valueKeyName" type="java.lang.String" required="false" description="HashMap中作为value的key，如：stavalue"%>
<%@ attribute name="showLoading" type="java.lang.Boolean" required="false" description="是否显示加载画面，默认为true"%>
<%@ attribute name="saveAsImage" type="java.lang.Boolean" required="false" description="是否显示保存图片按钮，默认为true"%>

<c:set var="showLoading" value="${not empty showLoading and showLoading eq false ? false : true}" scope="page"/>
<c:set var="labelKeyName" value="${empty labelKeyName ? 'stalabel' : labelKeyName}" scope="page"/>
<c:set var="valueKeyName" value="${empty valueKeyName ? 'stavalue' : valueKeyName}" scope="page"/>

<%-- 找出最大值，作为visualMap的max值 --%>
<c:set var="max" value="0" scope="page"/>
<c:forEach items="${listHashMap}" var="hashMap" varStatus="status">
   <c:if test="${max < hashMap[valueKeyName]}">
		<c:set var="max" value="${hashMap[valueKeyName]}" scope="page"/>
	</c:if>
</c:forEach>

<script type="text/javascript">
	var myChart_${id} = echarts.init(document.getElementById("${id}"));
	
	<c:if test="${false ne showLoading}">
	myChart_${id}.showLoading({
	    text: '正在努力的读取数据中...',
	});
	</c:if>

	var geoCoordMap_province = {
	    '北京': [116.4551, 40.2539],
	    '上海': [121.4648, 31.2891],
	    '重庆': [107.7539, 30.1904],
	    '天津': [117.4219, 39.4189],
	    '广东': [113.5107, 23.2196],
	    '河北': [114.4995, 38.1006],
	    '山西': [112.3352, 37.9413],
	    '辽宁': [123.38, 41.8],
	    '吉林': [125.8154, 44.2584],
	    '黑龙江': [127.9688, 45.368],
	    '江苏': [118.8062, 31.9208],
	    '浙江': [119.5313, 29.8773],
	    '安徽': [117.29, 32.0581],
	    '福建': [119.4543, 25.9222],
	    '江西': [116.0046, 28.6633],
	    '山东': [117.1582, 36.8701],
	    '河南': [113.4668, 34.6234],
	    '湖北': [114.3896, 30.6628],
	    '湖南': [113.0823, 28.2568],
	    '海南': [110.3893, 19.8516],
	    '四川': [103.9526, 30.7617],
	    '贵州': [106.6992, 26.7682],
	    '云南': [102.9199, 25.4663],
	    '陕西': [109.1162, 34.2004],
	    '甘肃': [103.5901, 36.3043],
	    '青海': [101.4038, 36.8207],
	    '内蒙古': [111.4124, 40.4901],
	    '广西': [108.479, 23.1152],
	    '西藏': [91.1865, 30.1465],
	    '宁夏': [106.3586, 38.1775],
	    '新疆': [87.9236, 43.5883],
	    '香港': [114.173355,22.320048],
	    '澳门': [113.54909, 22.198951],
	    '台湾': [121.509062, 25.044332]
	};
	
	var dataFrom = '北京';
	var data_${id} = [
	    <c:forEach items="${listHashMap}" var="hashMap" varStatus="status">
	    {
			name : '${fn:replace(fn:replace(fn:replace(fn:replace(hashMap[labelKeyName],'省',''),'市',''),'区',''),'港澳台','台湾')}',
			value : ${hashMap[valueKeyName] + 0}
		 }<c:if test="${!status.last}">,</c:if> 
		</c:forEach>
	];
	
	var series = [
	    {
	        name:  '北京',
	        type: 'lines',
	        zlevel: 1,
	        effect: {
	            show: true,
	            period: 6,
	            trailLength: 0.7,
	            color: '#fff',
	            symbolSize: 3
	        },
	        lineStyle: {
	            normal: {
	                color:'#a6c84c',
	                width: 0,
	                curveness: 0.2
	            }
	        },
	        data: data_${id}.map(function (dataItem) {
	            return {
	                fromName: dataItem.name,
	                toName: dataFrom,
	                coords: [
	                    geoCoordMap_province[dataItem.name],
	                    geoCoordMap_province[dataFrom]
	                ]
	            }
	        })
	    }, 
	    {
	        name: '北京',
	        type: 'lines',
	        zlevel: 2,
	        symbol: ['none', 'arrow'],
	        symbolSize: 10,
	        effect: {
	            show: true,
	            period: 6,
	            trailLength: 0,
				symbolSize: 3	
	        },
	        lineStyle: {
	            normal: {
	                color:'#a6c84c',
	                width: 1,
	                opacity: 0.6,
	                curveness: 0.2
	            }
	        },
	        data: data_${id}.map(function (dataItem) {
	            return {
	                fromName: dataItem.name,
	                toName: dataFrom,
	                coords: [
	                    geoCoordMap_province[dataItem.name],
	                    geoCoordMap_province[dataFrom]
	                ]
	            }
	        })
	    }, 
	    {
	        name:  '北京',
	        type: 'effectScatter',
	        coordinateSystem: 'geo',
	        zlevel: 2,
	        rippleEffect: {
	            scale: 5,
	            brushType: 'stroke'
	        },
	        label: {
	            normal: {
	                show: true,
	                position: 'right',
	                formatter: '{b}'
	            }
	        },
	        symbolSize: function (val) {
	        	var v = val[2] / 10;
	        	if( v < 10){
	        		v = 10;
	        	}else if( v > 100){
	        		v = 100;
	        	}
	            return v; 
	        },
	        itemStyle: {
	            normal: {
	                color: '#a6c84c'
	            }
	        },
	        data: data_${id}.map(function (dataItem) {
	            return {
	                name: dataItem.name,
	                value: geoCoordMap_province[dataItem.name].concat([dataItem.value])
	            };
	        })
	    }
	];
	
	var option_${id} = {
	    backgroundColor: '#555',
	    dataRange: {
	        min: 0,
	        max: ${max},
	        calculable: true,
	        color: ['#ff3333', 'orange', 'yellow', 'lime', 'aqua'],
	        textStyle: {
	            color: '#fff'
	        },
	        show: true,
	        right:10
	    },
	    title: {
	        text : '${empty text ? '数据统计' : text}',
			subtext : '${empty subtext ? '数据来源：本系统' : subtext}',
	        left: 'center',
	        top : '100',
	        textStyle: {
	            color: '#fff'
	        }
	    },
	    tooltip : {
			trigger : 'item',
			<%-- formatter : "{b} <br/>数量及占比 : {a} （{d}%）" --%>
			formatter : function(value){
				for (var i = 0; i < data_${id}.length; i++) {
					if(data_${id}[i].name==value.name){
						return value.name + " ：" + data_${id}[i].value;
					}
				}
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
	    geo: {
	        map: 'china',
	        label: {
	            normal: {
	                show: false
	            },
	            emphasis: {
	                show: false
	            }
	        },
	        roam: true,
	        itemStyle: {
	            normal: {
	                areaColor: '#222',
	                borderColor: '#404a59'
	            },
	            emphasis: {
	                areaColor: '#2a333d'
	            }
	        },
	    },
	    series: series
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