<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${titlePrefix}</title>
	<meta name="decorator" content="default"/>
	
</head>
<body>


	
<%--图表显示 --%>	
<style>
<!--
#main {
    border: 1px solid #e3e3e3;
    
    height: 200px;
    width: 600px;
   
}
#main1 {
    border: 1px solid #e3e3e3;
    height: 200px;
    width: 600px;
  
}
-->
</style>
<div id="main"></div>
<script type="text/javascript">
var option = {
		  tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
        data:[${legendata}],
          y:'bottom'
    },
    grid: {
       x:40,
       y:20,
       x2:5,
       y2:52,
        containLabel: true
    },
    xAxis : [
        {
            type : 'category',
            data : [<c:forEach items="${mapdata}" var="testMap" varStatus="stat">
			'${testMap['labelname']}'<c:if test="${!stat.last}">,</c:if> 
		</c:forEach>]
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
     {
            name:'${nf}',
            type:'line',
            data:[
             <c:forEach items="${mapdata}" var="testMap" varStatus="stat">
            ${testMap['labelvalue1']}
            <c:if test="${!stat.last}">,</c:if> 
            </c:forEach>
            ]
        },
         {
            name:'${nf-1}',
            type:'line',
            data:[
             <c:forEach items="${mapdata}" var="testMap" varStatus="stat">
            ${testMap['labelvalue2']}
            <c:if test="${!stat.last}">,</c:if> 
            </c:forEach>
            ]
        }
    ]
	 };
</script>  




<div id="main1"></div>
<script type="text/javascript">
var option1 = {
		  tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    legend: {
        data:[${legendata}],
          y:'bottom'
    },
    grid: {
       x:40,
       y:20,
       x2:5,
       y2:52,
        containLabel: true
    },
    xAxis : [
        {
            type : 'category',
             splitLine:{ 
                               show:false
              },
            data : [<c:forEach items="${mapdata}" var="testMap" varStatus="stat">
			'${testMap['labelname']}'<c:if test="${!stat.last}">,</c:if> 
		</c:forEach>]
        }
    ],
    yAxis : [
        {
            type : 'value',
             splitLine:{ 
                               show:false
              },
        }
    ],
    series : [
     {
            name:'${nf}',
            type:'bar',
              itemStyle : {  
                                normal : {  
                                    color:'#00FF00',  
                                    lineStyle:{  
                                        color:'#00FF00'  
                                    }  
                                }  
                            },  
            data:[
             <c:forEach items="${mapdata}" var="testMap" varStatus="stat">
            ${testMap['labelvalue1']}
            <c:if test="${!stat.last}">,</c:if> 
            </c:forEach>
            ]
        },
         {
            name:'${nf-1}',
            type:'bar',
            data:[${staStusextype}
             <c:forEach items="${staStusextype}" var="testMap" varStatus="stat">
            ${testMap['student_type']}
            <c:if test="${!stat.last}">,</c:if> 
            </c:forEach>
            ]
        }
    ]
	 };
</script>  
sdfffffffffff
<div id="main2"></div>
<script type="text/javascript">
var option2= {
	    title : {
	        text: '学生住宿统计分析',
	        subtext: '按校区统计',
	        x:'center'
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient : 'vertical',
	        x : 'left',
		  	data:[
					
						'校本部',
					
						'西校区',
					
						'东校区'
					
		  	     ]
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            //mark : {show: true},
	            dataView : {show: true, readOnly: false},//数据视图
	            magicType : {
	                show: true, 
	                type: ['pie', 'funnel'],
	                option: {
	                    funnel: {
	                        x: '25%',
	                        width: '50%',
	                        funnelAlign: 'left',
	                        max: 1548
	                    }
	                }
	            },
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    series : [
	        {
	        	//name  数据视图头名称
		        name: '学生住宿统计分析（按校区统计）',
	            type:'pie',
	            radius : '55%',
	            center: ['50%', '60%'],
	            itemStyle :　{
	                normal : {//默认样式
	                    label : {
	                        show : true,
	                        position:'outer',
	                        textStyle : {
	                            fontSize : '14'
	                        },formatter: "{b} : {c} ({d}%)"
	                    },
	                    labelLine : {
	                        show : true
	                    }
	                }
	            }, 
	            data:[
					
						{value:24,name:'校本部'} , 
					
						{value:22,name:'西校区'} , 
					
						{value:14,name:'东校区'}  
					
	                 ]
	        }
	    ]
	};
</script>
<script src="${ctxStatic}/echarts/echarts/echarts.js"></script>

<script src="${ctxStatic}/echarts/launchChart.js"></script>
<script type="text/javascript">

$(function(){
	//$("#main").show();
	//$("#main1").show();
	//$("#main2").show();
	
})


</script>
</body>
</html>