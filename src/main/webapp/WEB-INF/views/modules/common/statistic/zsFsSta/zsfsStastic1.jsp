<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${titlePrefix}</title>
	<meta name="decorator" content="default"/>
	
</head>
<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="height:400px;width:40%"></div>
    
    
     <div id="main1" style="height:400px;width:40%"></div>
    <!-- ECharts单文件引入 -->
 <script src="${ctxStatic}/echarts/echarts/echarts.js"></script>
 <script type="text/javascript">
    
    var myChart;
    var myChart1;
    var theme = theme || 'macarons'; //默认macarons 
    // 路径配置
        require.config({
            paths: {
            	  echarts: ctxStatic+'/echarts/echarts'//echarts根目录
            }
        });
        
        // 使用
        require(
            [
                'echarts',
                'echarts/theme/shine',
                'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
                'echarts/chart/pie', // 使用柱状图就加载bar模块，按需加载
                'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
               myChart = ec.init(document.getElementById('main'),theme); 
                
                var option = {
                    tooltip: {
                        show: true
                    },
                    legend: {
                        data:['销量',"产量"]
                    },
                    xAxis : [
                        {
                            type : 'category',
                            data : ["1","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
                        }
                    ],
                    yAxis : [
                        {
                            type : 'value'
                        }
                    ],
                    series : [
                        {
                            "name":"销量",
                            "type":"bar",
                            "data":[5, 20, 40, 10, 10, 20]
                        },
                        {
                            "name":"产量",
                            "type":"bar",
                            "data":[51, 210, 410, 11, 110, 210]
                        }
                    ]
                };
        
                // 为echarts对象加载数据 
                myChart.setOption(option); 
                
                
                
                var myChart1 = ec.init(document.getElementById('main1'),theme); 
                
                window.onresize = function() {
                    myChart.resize();
                    myChart1.resize();
                };
                
                var option1= {
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

        
                // 为echarts对象加载数据 
                myChart1.setOption(option1); 
            }
        );
        
    
    </script>
</body>