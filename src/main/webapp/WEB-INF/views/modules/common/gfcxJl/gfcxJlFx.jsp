<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="lte" />
</head>
<body>

<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script src="${ctxStaticLTE}/plugins/echarts3/echarts/echarts.js"></script>

	<%-- <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${kszycount }" id="main1_1" text="${yearLabel}新生分专业人数统计"/> --%>

	<jsp:useBean id='klcount_map' class='java.util.HashMap' scope='page'>
		<c:set target='${klcount_map}' property='' value='${klcount}' />
	</jsp:useBean>
	
	<div class="col-lg-6 col-md-12" style="height:500px;width:50%;" id="main1"></div>

	<jsp:useBean id='sscount_map' class='java.util.HashMap' scope='page'>
		<c:set target='${sscount_map}' property='' value='${sscount}' />
	</jsp:useBean>
	
	<div class="col-lg-6 col-md-12" style="height:500px;width:50%;" id="main2"></div>
	
	<%-- <jsp:useBean id='sscount_map' class='java.util.HashMap' scope='page'>
		<c:set target='${sscount_map}' property='' value='${sscount}' />
	</jsp:useBean>
	
	<div class="col-lg-6 col-md-12" style="height:500px;width:50%;" id="main3"></div>
 --%>
	<%-- <echart:bar valueKeyName="stavalue" labelKeyName="stalabel" linkedHashMapListHashMap="${klcount_map}" id="main1_1" text="查询科类数统计" saveAsImage="false" /> --%>
	<%-- <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${klcount}" id="main1" text="查询科类人数统计" saveAsImage="false" /> --%>




	<script type="text/javascript">		
 var myChart = echarts.init(document.getElementById('main1'));
  var option = {
    title: {
        text: '查询科类统计'    
        
        },
   tooltip:{},
    xAxis: {
     type: 'category',
    data: [
        <c:set var="yxsize" value="${fn:length(klcount)}"/>
         <c:forEach items="${klcount}" var="testMap"  varStatus="stat">
         <c:set var="ksinf" value="${klcount[yxsize- stat.index - 1]}"/>
				'${ksinf.stalabel}'<c:if test="${!stat.last}">,</c:if>
		</c:forEach>
       ]
    },
    yAxis: {
           type: 'value'
        
    },
    series: [
        {   
        name:'查询次数',
            data:
             [ 
             <c:set var="yxsize" value="${fn:length(klcount)}"/>
             <c:forEach items="${klcount}" var="testMap"  varStatus="stat">
             <c:set var="ksinf" value="${klcount[yxsize- stat.index - 1]}"/>
				${ksinf.stavalue}<c:if test="${!stat.last}">,</c:if>
		     </c:forEach>
		    ],
		     type: 'bar'
        }
    ]
};

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option); 
        
        
        var myChart1 = echarts.init(document.getElementById('main2'));
  var option1 = {
    title: {
        text: '查询省市统计'    
        
        },
    tooltip:{},
    xAxis: {
     type: 'category',
     nage:'查询次数',
    data: [
        <c:set var="yxsize" value="${fn:length(sscount)}"/>
         <c:forEach items="${sscount}" var="testMap"  varStatus="stat">
         <c:set var="ksinf" value="${sscount[yxsize- stat.index - 1]}"/>
				'${ksinf.stalabel}'<c:if test="${!stat.last}">,</c:if>
		</c:forEach>
       ]
    },
    yAxis: {
           type: 'value'
        
    },
    series: [
        {   
        name:'查询次数',
            data:
             [ 
             <c:set var="yxsize" value="${fn:length(sscount)}"/>
             <c:forEach items="${sscount}" var="testMap"  varStatus="stat">
             <c:set var="ksinf" value="${sscount[yxsize- stat.index - 1]}"/>
				${ksinf.stavalue}<c:if test="${!stat.last}">,</c:if>
		     </c:forEach>
		    ],
		     type: 'bar'
        }
    ]
};

        // 使用刚指定的配置项和数据显示图表。
        myChart1.setOption(option1); 
        
      
 /* var dom = document.getElementById("main3");
var myChart3 = echarts.init(dom);
var app = {};
option = null;

var dataCount = 5e5;
var data = generateData(dataCount);

var option2 = {
    title: {
        text: '分数统计',
      
    },
    toolbox: {
        feature: {
            dataZoom: {
                yAxisIndex: false
            },
            saveAsImage: {
                pixelRatio: 2
            }
        }
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    grid: {
        bottom: 90
    },
    dataZoom: [{
        type: 'inside'
    }, {
        type: 'slider'
    }],
    xAxis: {
        data: [ 
        <c:set var="yxsize" value="${fn:length(nftypessmccount)}"/>
         <c:forEach items="${nftypessmccount}" var="testMap"  varStatus="stat">
         <c:set var="ksinf" value="${nftypessmccount[yxsize- stat.index - 1]}"/>
				'${ksinf.stalabel}'<c:if test="${!stat.last}">,</c:if>
		</c:forEach>
		],
        silent: false,
        splitLine: {
            show: false
        },
        splitArea: {
            show: false
        }
    },
    yAxis: {
     type: 'value',
        splitArea: {
            show: false
        }
    },
    series: [{
        type: 'bar',
        data: [ 
             <c:set var="yxsize" value="${fn:length(sscount)}"/>
             <c:forEach items="${sscount}" var="testMap"  varStatus="stat">
             <c:set var="ksinf" value="${sscount[yxsize- stat.index - 1]}"/>
				${ksinf.stavalue}<c:if test="${!stat.last}">,</c:if>
		     </c:forEach>
		    ],
        large: true
    }]
};

myChart3.setOption(option2, true); */
      
    </script>
</body>
</html>
