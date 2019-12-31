<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="lte" />
</head>


<body>

<c:choose>
	<c:when test="${zhaoshengLqcx.nianji lt 0}"><c:set var="yearLabel" value="近${-zhaoshengLqcx.nianji}年"/></c:when>
	<c:otherwise><c:set var="yearLabel" value="${zhaoshengLqcx.nianji}年"/></c:otherwise>
</c:choose>


	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			${yearLabel}招生情况统计 <small>从基本情况，招生类别、来源、年龄、政治面貌、性别等组成对学校${yearLabel}招生情况进行统计。</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="/careeryou/static/lte/starter.html"><i class="fa fa-dashboard"></i> 首页</a></li>
			<li><a href="#">模块</a></li>
			<li class="active">统计图表</li>
		</ol>
	</section>

	<!-- 筛选功能部分 -->
	<div class="selector">

		<div class="selectorLine clear">
			<p class="sl-key pull-left text-deep-blue">招生院系：</p>
			<ul class="sl-value pull-left">
				<li><a href="${ctx}/lqcx/zhaoshengLqsta">全校</a></li>
				<c:forEach items="${ksxy}" var="staMap" varStatus="stat">
					<li <c:if test='${lqcx.yxdm eq staMap["yxdm"]}'>class='on'</c:if>><a href="${ctx}/lqcx/zhaoshengLqsta/colindex?yxdm=${staMap['yxdm']}&nianji=${zhaoshengLqcx.nianji}">${staMap["xy"]}</a></li>
				</c:forEach>
			</ul>
		</div>

	</div>

	<!-- Main content -->
	<section class="content">
		<div class="chartHeader">
			<div class="census-01 text-center">
				<span><i class="fa fa-users"></i></span> <span><strong class="text-green">${kscount}</strong>人<br>${yearLabel}新生人数</span>
			</div>
			<div class="row">
				<div class="col-lg-4 col-xs-12">
					<div class="census-02 clear">
						<table style="margin-top:20px;">

							<c:set value="aqua" var="colorvalue" />
							<!--颜色设置-->
							<c:forEach items="${ksxbcount}" var="staMap" varStatus="stat">
								<c:set var="xbper" value="${(staMap['stavalue']*100.0/kscount)}" scope="request" />
								<c:if test="${stat.index eq 1}">
									<c:set value="yellow" var="colorvalue" />
								</c:if>
								<tr>
									<td><i class="fa fa-circle-o text-${colorvalue}"></i> ${staMap['stalabel']}</td>
									<td class="text-green">${staMap['stavalue']}</td>
									<td><fmt:formatNumber type="number" value="${xbper}" pattern="0.00" maxFractionDigits="2" /> %</td>
									<td class="rate">
										<div class="progress progress-xs">
											<div class="progress-bar progress-bar-${colorvalue}" role="progressbar" style="width: <fmt:formatNumber type="number" value="${xbper}" pattern="0.00" maxFractionDigits="2"/>%">
												<span class="sr-only"><fmt:formatNumber type="number" value="${xbper}" pattern="0.00" maxFractionDigits="2" />% Complete (success)</span>
											</div>
										</div>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
				<div class="col-lg-4 col-xs-12">
					<div class="census-02 clear">
						<table>

							<c:forEach items="${kstjmccount}" var="staMap" varStatus="stat">
								<c:set var="tjper" value="${(staMap['stavalue']*100.0/kscount)}" scope="request" />
								<c:if test="${stat.index eq 0}">
									<c:set value="aqua" var="colorvalue" />
								</c:if>
								<c:if test="${stat.index eq 1}">
									<c:set value="yellow" var="colorvalue" />
								</c:if>
								<c:if test="${stat.index eq 2}">
									<c:set value="red" var="colorvalue" />
								</c:if>
								<tr>
									<td><i class="fa fa-circle-o text-${colorvalue}"></i> ${staMap['stalabel']}</td>
									<td class="text-green">${staMap['stavalue']}</td>
									<td><fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2" />%</td>
									<td class="rate">
										<div class="progress progress-xs">
											<div class="progress-bar progress-bar-${colorvalue}" role="progressbar" style="width: <fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2"/>%">
												<span class="sr-only"><fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2" />% Complete (success)</span>
											</div>
										</div>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
				<div class="col-lg-4 col-xs-12">
					<div class="census-02 clear">
						<table>
							<c:forEach items="${ksklcount}" var="staMap" varStatus="stat">
								<c:set var="tjper" value="${(staMap['stavalue']*100.0/kscount)}" scope="request" />
								<c:if test="${stat.index eq 0}">
									<c:set value="aqua" var="colorvalue" />
								</c:if>
								<c:if test="${stat.index eq 1}">
									<c:set value="yellow" var="colorvalue" />
								</c:if>
								<c:if test="${stat.index eq 2}">
									<c:set value="red" var="colorvalue" />
								</c:if>
								<tr>
									<td><i class="fa fa-circle-o text-${colorvalue}"></i> ${staMap['stalabel']}</td>
									<td class="text-green">${staMap['stavalue']}</td>
									<td><fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="3" />%</td>
									<td class="rate">
										<div class="progress progress-xs">
											<div class="progress-bar progress-bar-${colorvalue}" role="progressbar" style="width: <fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2"/>%">
												<span class="sr-only"><fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2" />% Complete (success)</span>
											</div>
										</div>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="chartWrap tableWrap">

			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">${yearLabel}新生分专业人数统计</span>

					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="100">
							<col width="200">
							<col width="200">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">专业代号</th>
								<th class="tc">专业名称</th>
								<th class="tc">新生人数</th>
								<th class="tc">百分比</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${kszycount}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc"><a data-toggle="modal" role="button" href="#modal-table">${staMap['zydh']}</a></td>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kscount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>

						</tbody>
					</table>

				</div>
				<div class="col-lg-6 col-md-12" style="height:550px" id="main1_1"></div>
				<div class="col-lg-6 col-md-12" style="height:550px;width:100%;" id="main1"></div>

				<div class="row">
					<!-- 一行2个table -->
					<div class="col-lg-6 col-md-12">
						<div class="tabHeader box-header with-border">
							<span class="text-green pull-left">各省份人数排名</span>
							<!-- <p class="box-tools">
								<a href="#" class="btn btn-box-tool" data-toggle="tooltip" title="" data-original-title="Previous"><i class="fa fa-chevron-left"></i></a> <a href="#" class="btn btn-box-tool" data-toggle="tooltip" title="" data-original-title="Next"><i class="fa fa-chevron-right"></i></a>
							</p> -->
						</div>
						<table class="nowrap dataTable display">
							<colgroup>
							<!-- 	<col width="100">
								<col width="200">
								<col width="200"> -->
								<col width="*">
								<col width="*">
							</colgroup>
							<thead>
								<tr>
							<!-- 		<th class="tc">专业编号</th>
									<th class="tc">专业名称</th>
									<th class="tc">学院</th>
									<th class="tc">学历</th> -->
									<th class="tc">省市名称</th>
									<th class="tc">人数</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${ksssmclist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
								</tr>
							</c:forEach>
								<!-- <tr>
									<td class="tc"><a data-toggle="modal" role="button" href="#modal-table">2</a></td>
									<td class="tc">草业科学</td>
									<td class="tc">动物科技学院</td>
									<td class="tc">本科</td>
								</tr>-->
							</tbody>
						</table>
						<!-- <div class="row pagenav">
							<div class="col-sm-5">
								<div class="dataTables_info" id="example1_info" role="status" aria-live="polite">共 3 条，当前显示 1 到 3 条</div>
							</div>
							<div class="col-sm-7">
								<div class="dataTables_paginate paging_simple_numbers" id="example1_paginate">
									<ul class="pagination">
										<li class="paginate_button previous disabled" id="example1_previous"><a href="#" aria-controls="example1" data-dt-idx="0" tabindex="0">上一页</a></li>
										<li class="paginate_button active"><a href="#" aria-controls="example1" data-dt-idx="1" tabindex="0">1</a></li>
										<li class="paginate_button next disabled" id="example1_next"><a href="#" aria-controls="example1" data-dt-idx="2" tabindex="0">下一页</a></li>
									</ul>
								</div>
							</div>
						</div> -->
					</div>
					<div class="col-lg-6 col-md-12" style="height:500px" id="main2"></div>
				</div>
			</div>
	</section>
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script src="${ctxStaticLTE}/plugins/echarts3/echarts/echarts.js"></script>

	<%-- <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${kszycount }" id="main1_1" text="${yearLabel}新生分专业人数统计"/> --%>

	<jsp:useBean id='kszycount_map' class='java.util.HashMap' scope='page'>
		<c:set target='${kszycount_map}' property='${yearLabel}' value='${kszycount}' />
	</jsp:useBean>

	<div class="col-lg-6 col-md-12" style="height:500px;width:50%;" id="main1_2"></div>
	<div class="col-lg-6 col-md-12" style="height:500px;width:50%;" id="main1_3"></div>
	<div class="col-lg-6 col-md-12" style="height:500px;width:50%;" id="main1_4"></div>
	<div class="col-lg-6 col-md-12" style="height:500px;width:50%;" id="main1_5"></div>


	<echart:bar valueKeyName="stavalue" labelKeyName="stalabel" linkedHashMapListHashMap="${kszycount_map}" id="main1_1" text="${yearLabel}新生分专业人数统计" saveAsImage="false" />

	<echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${ksklcount}" id="main1_2" text="${yearLabel}考生类别人数统计" saveAsImage="false" />
	<echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${ksxbcount}" id="main1_3" text="${yearLabel}考生性别人数统计" saveAsImage="false" />
	<echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${kstjmccount}" id="main1_4" text="${yearLabel}考生类型人数统计" saveAsImage="false" />
	<echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${kszycount}" id="main1_5" text="${yearLabel}考生专业人数统计" saveAsImage="false" />



	<script type="text/javascript">
 var myChart = echarts.init(document.getElementById('main1'));
  var option = {
    title: {
        text: '${yearLabel}级新生分专业人数统计'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
   toolbox: {
        show: true,
        feature: {
            saveAsImage: {}
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
        <c:set var="yxsize" value="${fn:length(kszycount)}"/>
         <c:forEach items="${kszycount}" var="testMap"  varStatus="stat">
         <c:set var="ksinf" value="${kszycount[yxsize- stat.index - 1]}"/>
				'${ksinf.stalabel}'<c:if test="${!stat.last}">,</c:if>
		</c:forEach>
       ]
    },
    series: [
        {
           
            type: 'bar',
            data:
             [ 
             <c:set var="yxsize" value="${fn:length(kszycount)}"/>
             <c:forEach items="${kszycount}" var="testMap"  varStatus="stat">
             <c:set var="ksinf" value="${kszycount[yxsize- stat.index - 1]}"/>
				${ksinf.stavalue}<c:if test="${!stat.last}">,</c:if>
		     </c:forEach>
		    ]
        }
    ]
};

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option); 
        
        
        
        
        var myChart2 = echarts.init(document.getElementById('main2'));
        var option2 = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data:['直达','营销广告','搜索引擎','邮件营销','联盟广告','视频广告','百度','谷歌','必应','其他']
            },
            series: [
                {
                    name:'访问来源',
                    type:'pie',
                    selectedMode: 'single',
                    radius: [0, '30%'],

                    label: {
                        normal: {
                            position: 'inner'
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data:[
                        {value:335, name:'直达', selected:true},
                        {value:679, name:'营销广告'},
                        {value:1548, name:'搜索引擎'}
                    ]
                },
                {
                    name:'访问来源',
                    type:'pie',
                    radius: ['40%', '55%'],

                    data:[
                        {value:335, name:'直达'},
                        {value:310, name:'邮件营销'},
                        {value:234, name:'联盟广告'},
                        {value:135, name:'视频广告'},
                        {value:1048, name:'百度'},
                        {value:251, name:'谷歌'},
                        {value:147, name:'必应'},
                        {value:102, name:'其他'}
                    ]
                }
            ]
        };
        
     
     myChart2.setOption(option2);
	 window.onresize = function() {
                myChart.resize();
                myChart2.resize();
            };
    </script>
</body>
</html>
