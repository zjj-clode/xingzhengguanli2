<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="lte" />
</head>

<c:choose>
	<c:when test="${-3 eq zhaoshengLqcx.nianji}"><c:set var="yearLabel" value="近3年"/></c:when>
	<c:when test="${-5 eq zhaoshengLqcx.nianji}"><c:set var="yearLabel" value="近5年"/></c:when>
	<c:otherwise><c:set var="yearLabel" value="${zhaoshengLqcx.nianji}"/></c:otherwise>
</c:choose>

<body>
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			${yearLabel}年招生情况统计 <small>从基本情况，招生类别、来源、年龄、政治面貌、性别等组成对学校${yearLabel}年招生情况进行统计。</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="/careeryou/static/lte/starter.html"><i class="fa fa-dashboard"></i> 首页</a></li>
			<li><a href="#">模块</a></li>
			<li class="active">统计图表</li>
		</ol>
	</section>

 	<form class="form-inline form-filter form-short">
      	<div class="form-group">
	       <a href="${ctx}/lqcx/zhaoshengLqsta?nianji=${nowyear}"> <span class="btn <c:if test="${nowyear eq zhaoshengLqcx.nianji}">btn-info</c:if> <c:if test="${nowyear ne zhaoshengLqcx.nianji}">btn-default</c:if>">今年</span></a>
	       <a href="${ctx}/lqcx/zhaoshengLqsta?nianji=${nowyear-1}"><span class="btn <c:if test="${(nowyear-1) eq zhaoshengLqcx.nianji}">btn-info</c:if> <c:if test="${(nowyear-1) ne zhaoshengLqcx.nianji}">btn-default</c:if>">去年</span></a>
	       <a href="${ctx}/lqcx/zhaoshengLqsta?nianji=-3"><span class="btn <c:if test="${-3 eq zhaoshengLqcx.nianji}">btn-info</c:if> <c:if test="${-3 ne zhaoshengLqcx.nianji}">btn-default</c:if>">近3年</span></a>
	       <a href="${ctx}/lqcx/zhaoshengLqsta?nianji=-5"><span class="btn <c:if test="${-5 eq zhaoshengLqcx.nianji}">btn-info</c:if> <c:if test="${-5 ne zhaoshengLqcx.nianji}">btn-default</c:if>">近5年</span></a>
      	</div>
    </form>

	<!-- 筛选功能部分 -->
	<div class="selector">

		<div class="selectorLine clear">
			<p class="sl-key pull-left text-deep-blue">招生院系：</p>
			<ul class="sl-value pull-left">
				<%-- 
				<li class="on"><a href="${ctx}/lqcx/zhaoshengLqsta">全校</a></li>
				<c:forEach items="${ksxy}" var="staMap" varStatus="stat">
					<li><a href="${ctx}/lqcx/zhaoshengLqsta/colindex?yxdm=${staMap["yxdm"]}">${staMap["xy"]}</a></li>
				</c:forEach> 
				--%>
				<li class="on"><a href="${ctx}/lqcx/zhaoshengLqsta/colindex?nianji=${zhaoshengLqcx.nianji}">全校</a></li>
				<c:forEach items="${ksxy}" var="staMap" varStatus="stat"> 
					<li><a <c:if test="${zhaoshengLqcx.yxdm eq staMap['yxdm']}">class="on"</c:if> href="${ctx}/lqcx/zhaoshengLqsta/colindex?nianji=${zhaoshengLqcx.nianji}&yxdm=${staMap['yxdm']}">${staMap['xy']}</a></li>
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
								<c:set var="xbper" value="${(staMap['stavalue']*100.0/kstjcount)}" scope="request" />
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
						<table style="margin-top:20px;">

							<c:set value="aqua" var="colorvalue" />
							<!--颜色设置-->
							<c:forEach items="${ksncczlist}" var="staMap" varStatus="stat">
								<c:set var="xbper" value="${(staMap['stavalue']*100.0/kstjcount)}" scope="request" />
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
							<c:forEach items="${ksklcount}" var="staMap" varStatus="stat">
								<c:set var="tjper" value="${(staMap['stavalue']*100.0/kstjcount)}" scope="request" />
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
						<span class="text-green pull-left">${yearLabel}级新生分院系人数统计</span>

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
								<th class="tc">学院代码</th>
								<th class="tc">学院名称</th>
								<th class="tc">新生人数</th>
								<th class="tc">百分比</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ksyxcount}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc"><a data-toggle="modal" role="button" href="#modal-table">${staMap['yxdm']}</a></td>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>

						</tbody>
					</table>

				</div>
				
				<div class="col-lg-6 col-md-12" style="height:670px" id="main1"></div>
				
				<div class="col-lg-6 col-md-12" style="height:1000px;width:100%;" id="main1_1"></div>
				
			</div>

			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">外语语种分布</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="200">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">外语语种</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${kswyyzlist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:600px;" id="main2"></div>
			</div>

			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生类型组成</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="100">
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">统计类型</th>
								<th class="tc">统计名称</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${kstjflist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['tjfdm']}</td>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:500px" id="main3"></div>
			</div>


			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生学科门类统计</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">学科</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ksxkmllist}" var="staMap" varStatus="stat">
								<tr>

									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
							<tr>

								<td class="tc">合计</td>
								<td class="tc">${kstjcount}</td>
								<td class="tc">100%</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:500px" id="main4"></div>
				
				
	
	<script language="javascript">
	var myChart_main4 = echarts.init(document.getElementById("main4"));
	var option_main4 = {
			title : {
				text : '20171新生学科门类统计分析',
				subtext : '数据来源：本系统',
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{b} <br/>数量及占比 : {c} （{d}%）"
			},
			toolbox: {
		        show : true,
		        feature : {
		            
		            saveAsImage : {show: true}
		            
		        }
		    },
			legend : {
				orient : 'vertical',
				left : 'left',
				data : [
				        
				        /* '工学', */ 
				        '工学',
				 		
				        /* '农学', */ 
				        '农学',
				 		
				        /* '管理学', */ 
				        '管理学',
				 		
				        /* '理学', */ 
				        '理学',
				 		
				        /* '经济学', */ 
				        '经济学',
				 		
				        /* '文学', */ 
				        '文学',
				 		
				        /* '法学' */ 
				        '法学'
				 		
				        ]
			},
			series : [ {
				
				type : 'pie',
				radius : '55%',
				center : [ '50%', '60%' ],
				data : [ 
				      
				 {
					
					name : '工学',
					value : 1019
				 }, 
				       
				 {
					
					name : '农学',
					value : 735
				 }, 
				       
				 {
					
					name : '管理学',
					value : 370
				 }, 
				       
				 {
					
					name : '理学',
					value : 310
				 }, 
				       
				 {
					
					name : '经济学',
					value : 262
				 }, 
				       
				 {
					
					name : '文学',
					value : 144
				 }, 
				       
				 {
					
					name : '法学',
					value : 73
				 } 
				 
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
	
	
	
		if (option_main4 && typeof option_main4 === "object") {
			myChart_main4.setOption(option_main4, true);
		}
		</script>
				<div class="col-lg-6 col-md-12" style="height:500px;width:100%;" id="main4_1"></div>
			</div>


			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生年龄统计</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">年龄</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ksagelist}" var="staMap" varStatus="stat">
								<tr>

									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stalabel']}" pattern="0" maxFractionDigits="0" /></td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
							<tr>

								<td class="tc">合计</td>
								<td class="tc">${kstjcount}</td>
								<td class="tc">100%</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:500px" id="main5"></div>
			</div>


			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生生源统计</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">省市</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ksssmclist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
							<tr>
								<td class="tc">合计</td>
								<td class="tc">${kstjcount}</td>
								<td class="tc">100%</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<div class="col-lg-6 col-md-12" style="height:680px" id="main6"></div>
				
				<div class="col-lg-6 col-md-12" style="height:600px;width:100%;" id="main6_3"></div>
				
				<div class="col-lg-6 col-md-12" style="height:1000px;width:100%;" id="main6_1"></div>
				
				<div class="col-lg-6 col-md-12" style="height:1000px;width:100%;" id="main6_2"></div>
				
			</div>

			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生政治面貌统计</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">政治面貌</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${kszzmmlist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
							<tr>
								<td class="tc">合计</td>
								<td class="tc">${kstjcount}</td>
								<td class="tc">100%</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:500px" id="main7"></div>
			</div>


			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生民族统计</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">民族统计</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ksmzmclist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
							<tr>
								<td class="tc">合计</td>
								<td class="tc">${kstjcount}</td>
								<td class="tc">100%</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:500px" id="main8"></div>
			</div>





			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生农村城镇统计</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">类别</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ksncczlist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
							<tr>
								<td class="tc">合计</td>
								<td class="tc">${kstjcount}</td>
								<td class="tc">100%</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:500px" id="main9"></div>
			</div>

			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生应往届统计</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">类别</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ksywjlist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
							<tr>
								<td class="tc">合计</td>
								<td class="tc">${kstjcount}</td>
								<td class="tc">100%</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:500px" id="main10"></div>
			</div>


			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生录取专业志愿统计</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">类别</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${kslqzyzylist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
							<tr>
								<td class="tc">合计</td>
								<td class="tc">${kstjcount}</td>
								<td class="tc">100%</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:500px" id="main11"></div>
			</div>
			<div class="row">
				<!-- 一行2个table -->
				<div class="col-lg-6 col-md-12">
					<div class="tabHeader box-header with-border">
						<span class="text-green pull-left">新生投档志愿统计</span>
					</div>
					<table class="nowrap dataTable display">
						<colgroup>
							<col width="200">
							<col width="100">
							<col width="*">
						</colgroup>
						<thead>
							<tr>
								<th class="tc">类别</th>
								<th class="tc">人数</th>
								<th class="tc">比例</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${kstdzylist}" var="staMap" varStatus="stat">
								<tr>
									<td class="tc">${staMap['stalabel']}</td>
									<td class="tc">${staMap['stavalue']}</td>
									<td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3" />%</td>
								</tr>
							</c:forEach>
							<tr>
								<td class="tc">合计</td>
								<td class="tc">${kstjcount}</td>
								<td class="tc">100%</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-lg-6 col-md-12" style="height:680px" id="main12"></div>
			</div>

			<div class="row">
				<div class="col-lg-6 col-md-12" style="height:680px" id="main13"></div>
				<div class="col-lg-6 col-md-12" style="height:680px" id="main14"></div>
			</div>

			<div class="row">
				<div class="col-lg-6 col-md-12" style="height:680px" id="main15"></div>
				<div class="col-lg-6 col-md-12" style="height:680px" id="main16"></div>
			</div>

			<div class="row">
				<div class="col-lg-6 col-md-12" style="height:680px" id="main17"></div>
				<div class="col-lg-6 col-md-12" style="height:680px" id="main18"></div>
			</div>

			<div class="row">
				<div class="col-lg-6 col-md-12" style="height:680px" id="main19"></div>
				<div class="col-lg-6 col-md-12" style="height:680px" id="main20"></div>
			</div>

			<div class="row">
				<div class="col-lg-6 col-md-12" style="height:680px" id="main21"></div>
				<div class="col-lg-6 col-md-12" style="height:680px" id="main22"></div>
			</div>

			<div class="row">
				<div class="col-lg-6 col-md-12" style="height:680px" id="main23"></div>
				<div class="col-lg-6 col-md-12" style="height:680px" id="main24"></div>
			</div>

			<div class="row" style="height:680px" id="main2a"></div>


		</div>


	</section>
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/echarts.js"></script>
	<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/map/js/china.js"></script>
	<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/dark.js"></script>
	<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/infographic.js"></script>
	<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/macarons.js"></script>
	<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/roma.js"></script>
	<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/shine.js"></script>
	<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/vintage.js"></script>

    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${ksyxcount}" id="main1" text="${yearLabel}新生院系统计分析" subtext="数据来源：本系统"/>
    <echart:bar-y-category valueKeyName="stavalue" labelKeyName="stalabel" linkedHashMapListHashMap="${ksyx_mapListHashMap}" id="main1_1" text="院系统计分析" subtext="数据来源：本系统"/>
    
    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${kswyyzlist}" id="main2" text="${yearLabel}新生外语语种统计分析" subtext="数据来源：本系统"/>
    
    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${kstjflist}" id="main3" text="${yearLabel}新生类型组成统计分析" subtext="数据来源：本系统"/>
    


    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${ksagelist}" id="main5" text="${yearLabel}新生年龄统计分析" subtext="数据来源：本系统"/>
    
    <echart:map-china valueKeyName="stavalue" labelKeyName="stalabel" linkedHashMapListHashMap="${mapListHashMap}" id="main6" text="新生生源统计分析" subtext="数据来源：本系统"/>
    
    <echart:line-stack valueKeyName="stavalue" labelKeyName="stalabel" linkedHashMapListHashMap="${mapListHashMap}" id="main6_3" text="新生生源统计分析" subtext="数据来源：本系统"/>
    
    <echart:map-china-move valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${ksssmclist}" id="main6_1" text="${yearLabel}新生来源" subtext="数据来源：本系统"/>
    <echart:map-china-change valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${ksssmclist}" id="main6_2" text="${yearLabel}新生来源分布" subtext="数据来源：本系统"/>
    
    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${kszzmmlist}" id="main7" text="${yearLabel}新生政治面貌统计分析" subtext="数据来源：本系统"/>
    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${ksmzmclist}" id="main8" text="${yearLabel}新生民族统计分析" subtext="数据来源：本系统"/>
    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${ksncczlist}" id="main9" text="${yearLabel}新生农村城镇统计分析" subtext="数据来源：本系统"/>
    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${ksywjlist}" id="main10" text="${yearLabel}新生应往届统计分析" subtext="数据来源：本系统"/>
    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${kslqzyzylist}" id="main11" text="${yearLabel}录取专业志愿统计分析" subtext="数据来源：本系统"/>
    <echart:pie-simple valueKeyName="stavalue" labelKeyName="stalabel" listHashMap="${kstdzylist}" id="main12" text="${yearLabel}投档志愿统计分析" subtext="数据来源：本系统"/>

	<script type="text/javascript">
		<%-- 新生分院系人数统计—————— 饼图 --%>
		/* var option3 = {
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b}: {c} ({d}%)"
				},
				legend : {
					orient : 'vertical',
					x : 'left',
					data : [ '直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎' ]
				},
				series : [ {
					name : '访问来源',
					type : 'pie',
					radius : [ '50%', '70%' ],
					avoidLabelOverlap : false,
					label : {
						normal : {
							show : false,
							position : 'center'
						},
						emphasis : {
							show : true,
							textStyle : {
								fontSize : '30',
								fontWeight : 'bold'
							}
						}
					},
					labelLine : {
						normal : {
							show : false
						}
					},
					data : [ {
						value : 335,
						name : '直接访问'
					}, {
						value : 310,
						name : '邮件营销'
					}, {
						value : 234,
						name : '联盟广告'
					}, {
						value : 135,
						name : '视频广告'
					}, {
						value : 1548,
						name : '搜索引擎'
					} ]
				} ]
			};
			var dom3 = document.getElementById("main3");
			var myChart3 = echarts.init(dom3, "macarons");
			if (option3 && typeof option3 === "object") {
				myChart3.setOption(option3, true);
			}
	 */
	
	
	
	
	
	
	
	
	
		var theme = 'infographic'; //默认macarons 
		var dom = document.getElementById("main2a");
		var myChart2a = echarts.init(dom, theme);
		var app = {};
		option2a = null;
		var geoCoordMap = {
			'上海' : [ 121.4648, 31.2891 ],
			'东莞' : [ 113.8953, 22.901 ],
			'东营' : [ 118.7073, 37.5513 ],
			'中山' : [ 113.4229, 22.478 ],
			'临汾' : [ 111.4783, 36.1615 ],
			'临沂' : [ 118.3118, 35.2936 ],
			'丹东' : [ 124.541, 40.4242 ],
			'丽水' : [ 119.5642, 28.1854 ],
			'乌鲁木齐' : [ 87.9236, 43.5883 ],
			'佛山' : [ 112.8955, 23.1097 ],
			'保定' : [ 115.0488, 39.0948 ],
			'兰州' : [ 103.5901, 36.3043 ],
			'包头' : [ 110.3467, 41.4899 ],
			'北京' : [ 116.4551, 40.2539 ],
			'北海' : [ 109.314, 21.6211 ],
			'南京' : [ 118.8062, 31.9208 ],
			'南宁' : [ 108.479, 23.1152 ],
			'南昌' : [ 116.0046, 28.6633 ],
			'南通' : [ 121.1023, 32.1625 ],
			'厦门' : [ 118.1689, 24.6478 ],
			'台州' : [ 121.1353, 28.6688 ],
			'合肥' : [ 117.29, 32.0581 ],
			'呼和浩特' : [ 111.4124, 40.4901 ],
			'咸阳' : [ 108.4131, 34.8706 ],
			'哈尔滨' : [ 127.9688, 45.368 ],
			'唐山' : [ 118.4766, 39.6826 ],
			'嘉兴' : [ 120.9155, 30.6354 ],
			'大同' : [ 113.7854, 39.8035 ],
			'大连' : [ 122.2229, 39.4409 ],
			'天津' : [ 117.4219, 39.4189 ],
			'太原' : [ 112.3352, 37.9413 ],
			'威海' : [ 121.9482, 37.1393 ],
			'宁波' : [ 121.5967, 29.6466 ],
			'宝鸡' : [ 107.1826, 34.3433 ],
			'宿迁' : [ 118.5535, 33.7775 ],
			'常州' : [ 119.4543, 31.5582 ],
			'广州' : [ 113.5107, 23.2196 ],
			'廊坊' : [ 116.521, 39.0509 ],
			'延安' : [ 109.1052, 36.4252 ],
			'张家口' : [ 115.1477, 40.8527 ],
			'徐州' : [ 117.5208, 34.3268 ],
			'德州' : [ 116.6858, 37.2107 ],
			'惠州' : [ 114.6204, 23.1647 ],
			'成都' : [ 103.9526, 30.7617 ],
			'扬州' : [ 119.4653, 32.8162 ],
			'承德' : [ 117.5757, 41.4075 ],
			'拉萨' : [ 91.1865, 30.1465 ],
			'无锡' : [ 120.3442, 31.5527 ],
			'日照' : [ 119.2786, 35.5023 ],
			'昆明' : [ 102.9199, 25.4663 ],
			'杭州' : [ 119.5313, 29.8773 ],
			'枣庄' : [ 117.323, 34.8926 ],
			'柳州' : [ 109.3799, 24.9774 ],
			'株洲' : [ 113.5327, 27.0319 ],
			'武汉' : [ 114.3896, 30.6628 ],
			'汕头' : [ 117.1692, 23.3405 ],
			'江门' : [ 112.6318, 22.1484 ],
			'沈阳' : [ 123.1238, 42.1216 ],
			'沧州' : [ 116.8286, 38.2104 ],
			'河源' : [ 114.917, 23.9722 ],
			'泉州' : [ 118.3228, 25.1147 ],
			'泰安' : [ 117.0264, 36.0516 ],
			'泰州' : [ 120.0586, 32.5525 ],
			'济南' : [ 117.1582, 36.8701 ],
			'济宁' : [ 116.8286, 35.3375 ],
			'海口' : [ 110.3893, 19.8516 ],
			'淄博' : [ 118.0371, 36.6064 ],
			'淮安' : [ 118.927, 33.4039 ],
			'深圳' : [ 114.5435, 22.5439 ],
			'清远' : [ 112.9175, 24.3292 ],
			'温州' : [ 120.498, 27.8119 ],
			'渭南' : [ 109.7864, 35.0299 ],
			'湖州' : [ 119.8608, 30.7782 ],
			'湘潭' : [ 112.5439, 27.7075 ],
			'滨州' : [ 117.8174, 37.4963 ],
			'潍坊' : [ 119.0918, 36.524 ],
			'烟台' : [ 120.7397, 37.5128 ],
			'玉溪' : [ 101.9312, 23.8898 ],
			'珠海' : [ 113.7305, 22.1155 ],
			'盐城' : [ 120.2234, 33.5577 ],
			'盘锦' : [ 121.9482, 41.0449 ],
			'石家庄' : [ 114.4995, 38.1006 ],
			'福州' : [ 119.4543, 25.9222 ],
			'秦皇岛' : [ 119.2126, 40.0232 ],
			'绍兴' : [ 120.564, 29.7565 ],
			'聊城' : [ 115.9167, 36.4032 ],
			'肇庆' : [ 112.1265, 23.5822 ],
			'舟山' : [ 122.2559, 30.2234 ],
			'苏州' : [ 120.6519, 31.3989 ],
			'莱芜' : [ 117.6526, 36.2714 ],
			'菏泽' : [ 115.6201, 35.2057 ],
			'营口' : [ 122.4316, 40.4297 ],
			'葫芦岛' : [ 120.1575, 40.578 ],
			'衡水' : [ 115.8838, 37.7161 ],
			'衢州' : [ 118.6853, 28.8666 ],
			'西宁' : [ 101.4038, 36.8207 ],
			'西安' : [ 109.1162, 34.2004 ],
			'贵阳' : [ 106.6992, 26.7682 ],
			'连云港' : [ 119.1248, 34.552 ],
			'邢台' : [ 114.8071, 37.2821 ],
			'邯郸' : [ 114.4775, 36.535 ],
			'郑州' : [ 113.4668, 34.6234 ],
			'鄂尔多斯' : [ 108.9734, 39.2487 ],
			'重庆' : [ 107.7539, 30.1904 ],
			'金华' : [ 120.0037, 29.1028 ],
			'铜川' : [ 109.0393, 35.1947 ],
			'银川' : [ 106.3586, 38.1775 ],
			'镇江' : [ 119.4763, 31.9702 ],
			'长春' : [ 125.8154, 44.2584 ],
			'长沙' : [ 113.0823, 28.2568 ],
			'长治' : [ 112.8625, 36.4746 ],
			'阳泉' : [ 113.4778, 38.0951 ],
			'青岛' : [ 120.4651, 36.3373 ],
			'韶关' : [ 113.7964, 24.7028 ]
		};

		var BJData = [ [ {
			name : '北京'
		}, {
			name : '上海',
			value : 95
		} ], [ {
			name : '北京'
		}, {
			name : '广州',
			value : 90
		} ], [ {
			name : '北京'
		}, {
			name : '大连',
			value : 80
		} ], [ {
			name : '北京'
		}, {
			name : '南宁',
			value : 70
		} ], [ {
			name : '北京'
		}, {
			name : '南昌',
			value : 60
		} ], [ {
			name : '北京'
		}, {
			name : '拉萨',
			value : 50
		} ], [ {
			name : '北京'
		}, {
			name : '长春',
			value : 40
		} ], [ {
			name : '北京'
		}, {
			name : '包头',
			value : 30
		} ], [ {
			name : '北京'
		}, {
			name : '重庆',
			value : 20
		} ], [ {
			name : '北京'
		}, {
			name : '常州',
			value : 10
		} ] ];

		var SHData = [ [ {
			name : '上海'
		}, {
			name : '包头',
			value : 95
		} ], [ {
			name : '上海'
		}, {
			name : '昆明',
			value : 90
		} ], [ {
			name : '上海'
		}, {
			name : '广州',
			value : 80
		} ], [ {
			name : '上海'
		}, {
			name : '郑州',
			value : 70
		} ], [ {
			name : '上海'
		}, {
			name : '长春',
			value : 60
		} ], [ {
			name : '上海'
		}, {
			name : '重庆',
			value : 50
		} ], [ {
			name : '上海'
		}, {
			name : '长沙',
			value : 40
		} ], [ {
			name : '上海'
		}, {
			name : '北京',
			value : 30
		} ], [ {
			name : '上海'
		}, {
			name : '丹东',
			value : 20
		} ], [ {
			name : '上海'
		}, {
			name : '大连',
			value : 10
		} ] ];

		var GZData = [ [ {
			name : '广州'
		}, {
			name : '福州',
			value : 95
		} ], [ {
			name : '广州'
		}, {
			name : '太原',
			value : 90
		} ], [ {
			name : '广州'
		}, {
			name : '长春',
			value : 80
		} ], [ {
			name : '广州'
		}, {
			name : '重庆',
			value : 70
		} ], [ {
			name : '广州'
		}, {
			name : '西安',
			value : 60
		} ], [ {
			name : '广州'
		}, {
			name : '成都',
			value : 50
		} ], [ {
			name : '广州'
		}, {
			name : '常州',
			value : 40
		} ], [ {
			name : '广州'
		}, {
			name : '北京',
			value : 30
		} ], [ {
			name : '广州'
		}, {
			name : '北海',
			value : 20
		} ], [ {
			name : '广州'
		}, {
			name : '海口',
			value : 10
		} ] ];

		var planePath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';

		var convertData = function(data) {
			var res = [];
			for (var i = 0; i < data.length; i++) {
				var dataItem = data[i];
				var fromCoord = geoCoordMap[dataItem[0].name];
				var toCoord = geoCoordMap[dataItem[1].name];
				if (fromCoord && toCoord) {
					res.push({
						fromName : dataItem[0].name,
						toName : dataItem[1].name,
						coords : [ fromCoord, toCoord ]
					});
				}
			}
			return res;
		};

		var color = [ '#a6c84c', '#ffa022', '#46bee9' ];
		var series = [];
		[ [ '北京', BJData ], [ '上海', SHData ], [ '广州', GZData ] ]
				.forEach(function(item, i) {
					series.push({
						name : item[0] + ' Top10',
						type : 'lines',
						zlevel : 1,
						effect : {
							show : true,
							period : 6,
							trailLength : 0.7,
							color : '#fff',
							symbolSize : 3
						},
						lineStyle : {
							normal : {
								color : color[i],
								width : 0,
								curveness : 0.2
							}
						},
						data : convertData(item[1])
					}, {
						name : item[0] + ' Top10',
						type : 'lines',
						zlevel : 2,
						symbol : [ 'none', 'arrow' ],
						symbolSize : 10,
						effect : {
							show : true,
							period : 6,
							trailLength : 0,
							symbol : planePath,
							symbolSize : 15
						},
						lineStyle : {
							normal : {
								color : color[i],
								width : 1,
								opacity : 0.6,
								curveness : 0.2
							}
						},
						data : convertData(item[1])
					}, {
						name : item[0] + ' Top10',
						type : 'effectScatter',
						coordinateSystem : 'geo',
						zlevel : 2,
						rippleEffect : {
							brushType : 'stroke'
						},
						label : {
							normal : {
								show : true,
								position : 'right',
								formatter : '{b}'
							}
						},
						symbolSize : function(val) {
							return val[2] / 8;
						},
						itemStyle : {
							normal : {
								color : color[i]
							}
						},
						data : item[1].map(function(dataItem) {
							return {
								name : dataItem[1].name,
								value : geoCoordMap[dataItem[1].name]
										.concat([ dataItem[1].value ])
							};
						})
					});
				});

		option2a = {
			backgroundColor : '#404a59',
			title : {
				text : '模拟迁徙',
				subtext : '数据纯属虚构',
				left : 'center',
				textStyle : {
					color : '#fff'
				}
			},
			tooltip : {
				trigger : 'item'
			},
			legend : {
				orient : 'vertical',
				top : 'bottom',
				left : 'right',
				data : [ '北京 Top10', '上海 Top10', '广州 Top10' ],
				textStyle : {
					color : '#fff'
				},
				selectedMode : 'single'
			},
			geo : {
				map : 'china',
				label : {
					emphasis : {
						show : false
					}
				},
				roam : true,
				itemStyle : {
					normal : {
						areaColor : '#323c48',
						borderColor : '#404a59'
					},
					emphasis : {
						areaColor : '#2a333d'
					}
				}
			},
			series : series
		};
		;
		if (option2a && typeof option2a === "object") {
			myChart2a.setOption(option2a, true);
		}

		
		// 
		/*
		function randomData() {
			return Math.round(Math.random() * 1000);
		}

		var dom = document.getElementById("main1");
		var myChart1 = echarts.init(dom, "shine");
		var option1 = {
			title : {
				text : 'iphone销量',
				subtext : '纯属虚构',
				left : 'center'
			},
			tooltip : {
				trigger : 'item'
			},
			legend : {
				orient : 'vertical',
				left : 'left',
				data : [ 'iphone3', 'iphone4', 'iphone5' ]
			},
			visualMap : {
				min : 0,
				max : 2500,
				left : 'left',
				top : 'bottom',
				text : [ '高', '低' ], // 文本，默认为数值文本
				calculable : true
			},
			toolbox : {
				show : true,
				orient : 'vertical',
				left : 'right',
				top : 'center',
				feature : {
					dataView : {
						readOnly : false
					},
					restore : {},
					saveAsImage : {}
				}
			},
			series : [ {
				name : 'iphone3',
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
				data : [ {
					name : '北京',
					value : randomData()
				}, {
					name : '天津',
					value : randomData()
				}, {
					name : '上海',
					value : randomData()
				}, {
					name : '重庆',
					value : randomData()
				}, {
					name : '河北',
					value : randomData()
				}, {
					name : '河南',
					value : randomData()
				}, {
					name : '云南',
					value : randomData()
				}, {
					name : '辽宁',
					value : randomData()
				}, {
					name : '黑龙江',
					value : randomData()
				}, {
					name : '湖南',
					value : randomData()
				}, {
					name : '安徽',
					value : randomData()
				}, {
					name : '山东',
					value : randomData()
				}, {
					name : '新疆',
					value : randomData()
				}, {
					name : '江苏',
					value : randomData()
				}, {
					name : '浙江',
					value : randomData()
				}, {
					name : '江西',
					value : randomData()
				}, {
					name : '湖北',
					value : randomData()
				}, {
					name : '广西',
					value : randomData()
				}, {
					name : '甘肃',
					value : randomData()
				}, {
					name : '山西',
					value : randomData()
				}, {
					name : '内蒙古',
					value : randomData()
				}, {
					name : '陕西',
					value : randomData()
				}, {
					name : '吉林',
					value : randomData()
				}, {
					name : '福建',
					value : randomData()
				}, {
					name : '贵州',
					value : randomData()
				}, {
					name : '广东',
					value : randomData()
				}, {
					name : '青海',
					value : randomData()
				}, {
					name : '西藏',
					value : randomData()
				}, {
					name : '四川',
					value : randomData()
				}, {
					name : '宁夏',
					value : randomData()
				}, {
					name : '海南',
					value : randomData()
				}, {
					name : '台湾',
					value : randomData()
				}, {
					name : '香港',
					value : randomData()
				}, {
					name : '澳门',
					value : randomData()
				} ]
			}, {
				name : 'iphone4',
				type : 'map',
				mapType : 'china',
				label : {
					normal : {
						show : true
					},
					emphasis : {
						show : true
					}
				},
				data : [ {
					name : '北京',
					value : randomData()
				}, {
					name : '天津',
					value : randomData()
				}, {
					name : '上海',
					value : randomData()
				}, {
					name : '重庆',
					value : randomData()
				}, {
					name : '河北',
					value : randomData()
				}, {
					name : '安徽',
					value : randomData()
				}, {
					name : '新疆',
					value : randomData()
				}, {
					name : '浙江',
					value : randomData()
				}, {
					name : '江西',
					value : randomData()
				}, {
					name : '山西',
					value : randomData()
				}, {
					name : '内蒙古',
					value : randomData()
				}, {
					name : '吉林',
					value : randomData()
				}, {
					name : '福建',
					value : randomData()
				}, {
					name : '广东',
					value : randomData()
				}, {
					name : '西藏',
					value : randomData()
				}, {
					name : '四川',
					value : randomData()
				}, {
					name : '宁夏',
					value : randomData()
				}, {
					name : '香港',
					value : randomData()
				}, {
					name : '澳门',
					value : randomData()
				} ]
			}, {
				name : 'iphone5',
				type : 'map',
				mapType : 'china',
				label : {
					normal : {
						show : true
					},
					emphasis : {
						show : true
					}
				},
				data : [ {
					name : '北京',
					value : randomData()
				}, {
					name : '天津',
					value : randomData()
				}, {
					name : '上海',
					value : randomData()
				}, {
					name : '广东',
					value : randomData()
				}, {
					name : '台湾',
					value : randomData()
				}, {
					name : '香港',
					value : randomData()
				}, {
					name : '澳门',
					value : randomData()
				} ]
			} ]
		}; */
		<%-- 待处理： 招生数据中港澳台是放在一起的，地图上港澳台是分开的。简单粗暴处理就是港澳台招生数据绑定到台湾上。  --%>
		<%-- var option1 = {
				title : {
					text : '${nowyear}新生生源',
					subtext : '数据来源：本系统',
					left : 'center'
				},
				tooltip : {
					trigger : 'item'
				},
				legend : {
					orient : 'vertical',
					left : 'left',
					/* data : [ 'iphone3', 'iphone4', 'iphone5' ] */
					data : ['新生生源']
				},
				visualMap : {
					min : 0,
					<c:forEach items="${ksssmclist}" var="staMap" varStatus="stat" end='0'>
					max : ${staMap['stavalue']},
					</c:forEach>
					left : 'left',
					top : 'bottom',
					text : [ '高', '低' ], // 文本，默认为数值文本
					calculable : true
				},
				toolbox : {
					show : true,
					orient : 'vertical',
					left : 'right',
					top : 'center',
					feature : {
						dataView : {
							readOnly : false
						},
						restore : {},
						saveAsImage : {}
					}
				},
				series : [ {
					name : '新生生源',
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
						<c:forEach items="${ksssmclist}" var="staMap" varStatus="stat">
					 {
						name : '${fn:replace(fn:replace(fn:replace(staMap['stalabel'],'省',''),'市',''),'区','')}',
						value : ${staMap['stavalue']}
					 }<c:if test="${!stat.last}">,</c:if>
						</c:forEach>
					]
				}]
		};
		
		if (option1 && typeof option1 === "object") {
			myChart1.setOption(option1, true);
		}  --%>
		
		
		//
		/* var option2 = {
			title : {
				text : '世界人口总量',
				subtext : '数据来自网络'
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'shadow'
				}
			},
			legend : {
				data : [ '2011年', '2012年' ]
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : {
				type : 'value',
				boundaryGap : [ 0, 0.01 ]
			},
			yAxis : {
				type : 'category',
				data : [ '巴西', '印尼', '美国', '印度', '中国', '世界人口(万)' ]
			},
			series : [ {
				name : '2011年',
				type : 'bar',
				data : [ 18203, 23489, 29034, 104970, 131744, 630230 ]
			}, {
				name : '2012年',
				type : 'bar',
				data : [ 19325, 23438, 31000, 121594, 134141, 681807 ]
			} ]
		};
		var dom2 = document.getElementById("main2");
		var myChart2 = echarts.init(dom2, "roma");
		if (option2 && typeof option2 === "object") {
			myChart2.setOption(option2, true);
		} */

		
		

		/* var option4 = {
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b}: {c} ({d}%)"
			},
			legend : {
				orient : 'vertical',
				x : 'left',
				data : [ '直达', '营销广告', '搜索引擎', '邮件营销', '联盟广告', '视频广告', '百度',
						'谷歌', '必应', '其他' ]
			},
			series : [ {
				name : '访问来源',
				type : 'pie',
				selectedMode : 'single',
				radius : [ 0, '30%' ],

				label : {
					normal : {
						position : 'inner'
					}
				},
				labelLine : {
					normal : {
						show : false
					}
				},
				data : [ {
					value : 335,
					name : '直达',
					selected : true
				}, {
					value : 679,
					name : '营销广告'
				}, {
					value : 1548,
					name : '搜索引擎'
				} ]
			}, {
				name : '访问来源',
				type : 'pie',
				radius : [ '40%', '55%' ],

				data : [ {
					value : 335,
					name : '直达'
				}, {
					value : 310,
					name : '邮件营销'
				}, {
					value : 234,
					name : '联盟广告'
				}, {
					value : 135,
					name : '视频广告'
				}, {
					value : 1048,
					name : '百度'
				}, {
					value : 251,
					name : '谷歌'
				}, {
					value : 147,
					name : '必应'
				}, {
					value : 102,
					name : '其他'
				} ]
			} ]
		};

		var dom4 = document.getElementById("main4");
		var myChart4 = echarts.init(dom4, "infographic");
		if (option4 && typeof option4 === "object") {
			myChart4.setOption(option4, true);
		} */
		
		

		/* var option5 = {
			title : {
				text : '某站点用户访问来源',
				subtext : '纯属虚构',
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				orient : 'vertical',
				left : 'left',
				data : [ '直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎' ]
			},
			series : [ {
				name : '访问来源',
				type : 'pie',
				radius : '55%',
				center : [ '50%', '60%' ],
				data : [ {
					value : 335,
					name : '直接访问'
				}, {
					value : 310,
					name : '邮件营销'
				}, {
					value : 234,
					name : '联盟广告'
				}, {
					value : 135,
					name : '视频广告'
				}, {
					value : 1548,
					name : '搜索引擎'
				} ],
				itemStyle : {
					emphasis : {
						shadowBlur : 10,
						shadowOffsetX : 0,
						shadowColor : 'rgba(0, 0, 0, 0.5)'
					}
				}
			} ]
		};

		var dom5 = document.getElementById("main5");
		var myChart5 = echarts.init(dom5);
		if (option5 && typeof option5 === "object") {
			myChart5.setOption(option5, true);
		} */

		var option6 = {
			title : {
				text : '南丁格尔玫瑰图',
				subtext : '纯属虚构',
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				x : 'center',
				y : 'bottom',
				data : [ 'rose1', 'rose2', 'rose3', 'rose4', 'rose5', 'rose6',
						'rose7', 'rose8' ]
			},
			toolbox : {
				show : true,
				feature : {
					mark : {
						show : true
					},
					dataView : {
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'pie', 'funnel' ]
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			calculable : true,
			series : [ {
				name : '半径模式',
				type : 'pie',
				radius : [ 20, 110 ],
				center : [ '50%', '50%' ],
				roseType : 'radius',
				label : {
					normal : {
						show : false
					},
					emphasis : {
						show : true
					}
				},
				lableLine : {
					normal : {
						show : false
					},
					emphasis : {
						show : true
					}
				},
				data : [ {
					value : 10,
					name : 'rose1'
				}, {
					value : 5,
					name : 'rose2'
				}, {
					value : 15,
					name : 'rose3'
				}, {
					value : 25,
					name : 'rose4'
				}, {
					value : 20,
					name : 'rose5'
				}, {
					value : 35,
					name : 'rose6'
				}, {
					value : 30,
					name : 'rose7'
				}, {
					value : 40,
					name : 'rose8'
				} ]
			} ]
		};

		// 
		/* var option_pie_yuanxi = {
			title : {
				text : '新生分院系人数统计 ',
				subtext : '数据来源：本系统',
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				orient : 'vertical',
				left : 'left',
				data : [ 
				        <c:forEach items="${ksyxcount}" var="staMap" varStatus="stat">
				        '${staMap['stalabel']}'<c:if test="${!stat.last}">,</c:if> 
				 		</c:forEach>
				        ]
			},
			series : [ {
				name : '院系',
				type : 'pie',
				radius : '55%',
				center : [ '50%', '60%' ],
				data : [ 
				<c:forEach items="${ksyxcount}" var="staMap" varStatus="stat">      
				 {
					value : ${staMap['stavalue']},
					name : '${staMap['stalabel']}'
				 }<c:if test="${!stat.last}">,</c:if> 
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
		var dom6 = document.getElementById("main1");
		var myChart6 = echarts.init(dom6);
		if (option_pie_yuanxi && typeof option_pie_yuanxi === "object") {
			myChart6.setOption(option_pie_yuanxi, true);
		} */

		
		////////////////////////////////////////////////////////////////////////////////
		/* var option7 = {
			tooltip : {
				formatter : "{a} <br/>{b} : {c}%"
			},
			toolbox : {
				feature : {
					restore : {},
					saveAsImage : {}
				}
			},
			series : [ {
				name : '业务指标',
				type : 'gauge',
				detail : {
					formatter : '{value}%'
				},
				data : [ {
					value : 50,
					name : '完成率'
				} ]
			} ]
		};

		var dom7 = document.getElementById("main7");
		var myChart7 = echarts.init(dom7);
		if (option7 && typeof option7 === "object") {
			myChart7.setOption(option7, true);
		}

		setInterval(
				function() {
					option7.series[0].data[0].value = (Math.random() * 100)
							.toFixed(2) - 0;
					myChart7.setOption(option7, true);
				}, 2000);

		var base = +new Date(1968, 9, 3);
		var oneDay = 24 * 3600 * 1000;
		var date = [];

		var data = [ Math.random() * 300 ];

		for (var i = 1; i < 20000; i++) {
			var now = new Date(base += oneDay);
			date.push([ now.getFullYear(), now.getMonth() + 1, now.getDate() ]
					.join('/'));
			data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]));
		} */

		//////////////////////////////////
		/* var option8 = {
			tooltip : {
				trigger : 'axis',
				position : function(pt) {
					return [ pt[0], '10%' ];
				}
			},
			title : {
				left : 'center',
				text : '大数据量面积图',
			},
			toolbox : {
				feature : {
					dataZoom : {
						yAxisIndex : 'none'
					},
					restore : {},
					saveAsImage : {}
				}
			},
			xAxis : {
				type : 'category',
				boundaryGap : false,
				data : date
			},
			yAxis : {
				type : 'value',
				boundaryGap : [ 0, '100%' ]
			},
			dataZoom : [
					{
						type : 'inside',
						start : 0,
						end : 10
					},
					{
						start : 0,
						end : 10,
						handleIcon : 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
						handleSize : '80%',
						handleStyle : {
							color : '#fff',
							shadowBlur : 3,
							shadowColor : 'rgba(0, 0, 0, 0.6)',
							shadowOffsetX : 2,
							shadowOffsetY : 2
						}
					} ],
			series : [ {
				name : '模拟数据',
				type : 'line',
				smooth : true,
				symbol : 'none',
				sampling : 'average',
				itemStyle : {
					normal : {
						color : 'rgb(255, 70, 131)'
					}
				},
				areaStyle : {
					normal : {
						color : new echarts.graphic.LinearGradient(0, 0, 0, 1,
								[ {
									offset : 0,
									color : 'rgb(255, 158, 68)'
								}, {
									offset : 1,
									color : 'rgb(255, 70, 131)'
								} ])
					}
				},
				data : data
			} ]
		};
		var dom8 = document.getElementById("main8");
		var myChart8 = echarts.init(dom8);
		if (option8 && typeof option8 === "object") {
			myChart8.setOption(option8, true);
		} */

		var locations = [ {
			name : '上海',
			coord : [ 121.472644, 31.231706 ]
		}, {
			name : '北京',
			coord : [ 116.405285, 39.904989 ]
		}, {
			name : '广东',
			coord : [ 113.280637, 23.839463714285714 ]
		} ];
		
		/* var option9 = {
			tooltip : {
				trigger : 'item',
				formatter : '{b}'
			},
			series : [ {
				name : '中国',
				type : 'map',
				mapType : 'china',
				selectedMode : 'multiple',
				label : {
					normal : {
						show : true
					},
					emphasis : {
						show : true
					}
				}
			} ]
		};

		var dom9 = document.getElementById("main9");
		var myChart9 = echarts.init(dom9, theme);
		if (option9 && typeof option9 === "object") {
			myChart9.setOption(option9, true);
		} 

		var currentLoc = 0;
		setInterval(function() {
			myChart9.setOption({
				series : [ {
					center : locations[currentLoc].coord,
					zoom : 4,
					data : [ {
						name : locations[currentLoc].name,
						selected : true
					} ],
					animationDurationUpdate : 1000,
					animationEasingUpdate : 'cubicInOut'
				} ]
			});
			currentLoc = (currentLoc + 1) % locations.length;
		}, 2000);
		
		*/

		/* var option10 = {
			title : {
				text : '柱形图',
				subtext : 'ECharts'
			},
			legend : {
				data : [ '直接', '邮件', '联盟', '搜索' ]
			},
			toolbox : {
				show : true,
				feature : {
					magicType : {
						show : true,
						type : [ 'line', 'bar', 'stack', 'tiled' ]
					},
					dataView : {
						show : true,
						readOnly : false
					},
					restore : {
						show : true
					}
				}
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			yAxis : [ {
				type : 'value'
			} ],
			xAxis : [ {
				type : 'category',
				data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
			} ],
			series : [ {
				name : '直接',
				type : 'bar',
				data : [ 80, 132, 101, 134, 90, 180, 200 ]
			}, {
				name : '邮件',
				type : 'bar',
				data : [ 120, 102, 151, 164, 230, 230, 280 ]
			}, {
				name : '联盟',
				type : 'bar',
				data : [ 180, 182, 191, 264, 290, 350, 410 ]
			}, {
				name : '搜索',
				type : 'bar',
				data : [ 210, 232, 301, 254, 390, 430, 510 ]
			} ]

		};
		var dom10 = document.getElementById("main10");
		var myChart10 = echarts.init(dom10, "macarons");
		if (option10 && typeof option10 === "object") {
			myChart10.setOption(option10, true);
		} */

		var option13 = {
			title : {
				text : '雷达图',
				subtext : 'ECharts'
			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				orient : 'vertical',
				x : 'right',
				data : [ '预算分配', '实际开销' ]
			},
			toolbox : {
				show : true,
				y : 'bottom',
				feature : {
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			polar : [ {
				radius : '100%',
				indicator : [ {
					text : '销售',
					max : 6000
				}, {
					text : '管理',
					max : 16000
				}, {
					text : '信息技术',
					max : 30000
				}, {
					text : '客服',
					max : 38000
				}, {
					text : '研发',
					max : 52000
				}, {
					text : '市场',
					max : 25000
				} ]
			} ],
			series : [ {
				name : '预算 vs 开销',
				type : 'radar',
				data : [ {
					value : [ 4300, 10000, 28000, 35000, 50000, 19000 ],
					name : '预算分配'
				}, {
					value : [ 5000, 14000, 28000, 31000, 42000, 21000 ],
					name : '实际开销'
				} ]
			} ]

		};

		var dom13 = document.getElementById("main13");
		var myChart13 = echarts.init(dom13);
		if (option13 && typeof option13 === "object") {
			myChart13.setOption(option13, true);
		}

		var option14 = {
			title : {
				text : '仪表盘',
				subtext : 'ECharts'
			},
			tooltip : {
				formatter : "{a} <br/>{b} : {c}%"
			},
			toolbox : {
				show : true,
				y : 'bottom',
				feature : {
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			series : [ {
				name : '业务指标',
				type : 'gauge',
				detail : {
					formatter : '{value}%'
				},
				data : [ {
					value : 16.8,
					name : '完成率'
				} ]
			} ]
		};

		var dom14 = document.getElementById("main14");
		var myChart14 = echarts.init(dom14);
		if (option14 && typeof option14 === "object") {
			myChart14.setOption(option14, true);
		}

		var option15 = {
			title : {
				text : '漏斗图',
				subtext : 'ECharts'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c}%"
			},
			toolbox : {
				show : true,
				y : 'bottom',
				feature : {
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			legend : {
				orient : 'vertical',
				x : 'left',
				y : 'bottom',
				data : [ '展现', '点击', '访问', '咨询', '订单' ]
			},
			series : [ {
				name : '漏斗图',
				type : 'funnel',
				data : [ {
					value : 60,
					name : '访问'
				}, {
					value : 40,
					name : '咨询'
				}, {
					value : 20,
					name : '订单'
				}, {
					value : 80,
					name : '点击'
				}, {
					value : 100,
					name : '展现'
				} ]
			} ]
		};

		var dom15 = document.getElementById("main15");
		var myChart15 = echarts.init(dom15);
		if (option15 && typeof option15 === "object") {
			myChart15.setOption(option15, true);
		}

		var option17 = {
			title : {
				text : '某站点用户访问来源',
				subtext : '纯属虚构',
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				orient : 'vertical',
				x : 'left',
				data : [ '直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎' ]
			},
			calculable : true,
			series : [ {
				name : '访问来源',
				type : 'pie',
				radius : '55%',
				center : [ '50%', 225 ],
				data : [ {
					value : 335,
					name : '直接访问'
				}, {
					value : 310,
					name : '邮件营销'
				}, {
					value : 234,
					name : '联盟广告'
				}, {
					value : 135,
					name : '视频广告'
				}, {
					value : 1548,
					name : '搜索引擎'
				} ]
			} ]
		};

		var option18 = {
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'shadow'
				}
			},
			legend : {
				data : [ '直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎' ]
			},
			toolbox : {
				show : true,
				orient : 'vertical',
				y : 'center',
				feature : {
					mark : {
						show : true
					},
					magicType : {
						show : true,
						type : [ 'line', 'bar', 'stack', 'tiled' ]
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			calculable : true,
			xAxis : [ {
				type : 'category',
				data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
			} ],
			yAxis : [ {
				type : 'value',
				splitArea : {
					show : true
				}
			} ],
			grid : {
				x2 : 40
			},
			series : [ {
				name : '直接访问',
				type : 'bar',
				stack : '总量',
				data : [ 320, 332, 301, 334, 390, 330, 320 ]
			}, {
				name : '邮件营销',
				type : 'bar',
				stack : '总量',
				data : [ 120, 132, 101, 134, 90, 230, 210 ]
			}, {
				name : '联盟广告',
				type : 'bar',
				stack : '总量',
				data : [ 220, 182, 191, 234, 290, 330, 310 ]
			}, {
				name : '视频广告',
				type : 'bar',
				stack : '总量',
				data : [ 150, 232, 201, 154, 190, 330, 410 ]
			}, {
				name : '搜索引擎',
				type : 'bar',
				stack : '总量',
				data : [ 820, 932, 901, 934, 1290, 1330, 1320 ]
			} ]
		};
		/*myChart17 = ec.init(document.getElementById('main17'),theme);
		myChart117.setOption(option17); 	
		
		myChart18 = ec.init(document.getElementById('main18'),theme);
		myChart18.setOption(option18); 	
		 */
		var dom17 = document.getElementById("main17");
		var myChart17 = echarts.init(dom17);
		if (option17 && typeof option17 === "object") {
			myChart17.setOption(option17, true);
		}

		var dom18 = document.getElementById("main18");
		var myChart18 = echarts.init(dom18);
		if (option18 && typeof option18 === "object") {
			myChart18.setOption(option18, true);
		}

		var labelTop = {
			normal : {
				label : {
					show : true,
					position : 'center',
					formatter : '{b}',
					textStyle : {
						baseline : 'bottom'
					}
				},
				labelLine : {
					show : false
				}
			}
		};
		var labelFromatter = {
			normal : {
				label : {
					formatter : function(params) {
						return 100 - params.value + '%'
					},
					textStyle : {
						baseline : 'top'
					}
				}
			},
		};
		var labelBottom = {
			normal : {
				color : '#ccc',
				label : {
					show : true,
					position : 'center'
				},
				labelLine : {
					show : false
				}
			},
			emphasis : {
				color : 'rgba(0,0,0,0)'
			}
		};
		var radius = [ 40, 55 ];
		var option19 = {
			legend : {
				x : 'center',
				y : 'top',
				data : [ 'GoogleMaps', 'Facebook', 'Youtube', 'Google+',
						'Weixin', 'Twitter', 'Skype', 'Messenger', 'Whatsapp',
						'Instagram' ]
			},
			title : {
				text : 'The App World',
				subtext : 'from global web index',
				x : 'left'
			},
			toolbox : {
				show : true,
				feature : {
					dataView : {
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'pie', 'funnel' ],
						option : {
							funnel : {
								width : '20%',
								height : '30%',
								itemStyle : {
									normal : {
										label : {
											formatter : function(params) {
												return 'other\n' + params.value
														+ '%\n'
											},
											textStyle : {
												baseline : 'middle'
											}
										}
									},
								}
							}
						}
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			series : [ {
				type : 'pie',
				center : [ '10%', '30%' ],
				radius : radius,
				x : '0%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 46,
					itemStyle : labelBottom
				}, {
					name : 'GoogleMaps',
					value : 54,
					itemStyle : labelTop
				} ]
			}, {
				type : 'pie',
				center : [ '30%', '30%' ],
				radius : radius,
				x : '20%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 56,
					itemStyle : labelBottom
				}, {
					name : 'Facebook',
					value : 44,
					itemStyle : labelTop
				} ]
			}, {
				type : 'pie',
				center : [ '50%', '30%' ],
				radius : radius,
				x : '40%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 65,
					itemStyle : labelBottom
				}, {
					name : 'Youtube',
					value : 35,
					itemStyle : labelTop
				} ]
			}, {
				type : 'pie',
				center : [ '70%', '30%' ],
				radius : radius,
				x : '60%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 70,
					itemStyle : labelBottom
				}, {
					name : 'Google+',
					value : 30,
					itemStyle : labelTop
				} ]
			}, {
				type : 'pie',
				center : [ '90%', '30%' ],
				radius : radius,
				x : '80%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 73,
					itemStyle : labelBottom
				}, {
					name : 'Weixin',
					value : 27,
					itemStyle : labelTop
				} ]
			}, {
				type : 'pie',
				center : [ '10%', '70%' ],
				radius : radius,
				y : '55%', // for funnel
				x : '0%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 78,
					itemStyle : labelBottom
				}, {
					name : 'Twitter',
					value : 22,
					itemStyle : labelTop
				} ]
			}, {
				type : 'pie',
				center : [ '30%', '70%' ],
				radius : radius,
				y : '55%', // for funnel
				x : '20%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 78,
					itemStyle : labelBottom
				}, {
					name : 'Skype',
					value : 22,
					itemStyle : labelTop
				} ]
			}, {
				type : 'pie',
				center : [ '50%', '70%' ],
				radius : radius,
				y : '55%', // for funnel
				x : '40%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 78,
					itemStyle : labelBottom
				}, {
					name : 'Messenger',
					value : 22,
					itemStyle : labelTop
				} ]
			}, {
				type : 'pie',
				center : [ '70%', '70%' ],
				radius : radius,
				y : '55%', // for funnel
				x : '60%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 83,
					itemStyle : labelBottom
				}, {
					name : 'Whatsapp',
					value : 17,
					itemStyle : labelTop
				} ]
			}, {
				type : 'pie',
				center : [ '90%', '70%' ],
				radius : radius,
				y : '55%', // for funnel
				x : '80%', // for funnel
				itemStyle : labelFromatter,
				data : [ {
					name : 'other',
					value : 89,
					itemStyle : labelBottom
				}, {
					name : 'Instagram',
					value : 11,
					itemStyle : labelTop
				} ]
			} ]
		};
		var dom19 = document.getElementById("main19");
		var myChart19 = echarts.init(dom19, "infographic");
		if (option19 && typeof option19 === "object") {
			myChart19.setOption(option19, true);
		}

		/* var option20 = {
			title : {
				text : '浏览器占比变化',
				subtext : '纯属虚构',
				x : 'right',
				y : 'bottom'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				orient : 'vertical',
				x : 'left',
				data : [ 'Chrome', 'Firefox', 'Safari', 'IE9+', 'IE8-' ]
			},
			toolbox : {
				show : true,
				feature : {
					mark : {
						show : true
					},
					dataView : {
						show : true,
						readOnly : false
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			calculable : false,
			series : (function() {
				var series = [];
				for (var i = 0; i < 30; i++) {
					series.push({
						name : '浏览器（数据纯属虚构）',
						type : 'pie',
						itemStyle : {
							normal : {
								label : {
									show : i > 28
								},
								labelLine : {
									show : i > 28,
									length : 20
								}
							}
						},
						radius : [ i * 4 + 40, i * 4 + 43 ],
						data : [ {
							value : i * 128 + 80,
							name : 'Chrome'
						}, {
							value : i * 64 + 160,
							name : 'Firefox'
						}, {
							value : i * 32 + 320,
							name : 'Safari'
						}, {
							value : i * 16 + 640,
							name : 'IE9+'
						}, {
							value : i * 8 + 1280,
							name : 'IE8-'
						} ]
					})
				}
				series[0].markPoint = {
					symbol : 'emptyCircle',
					symbolSize : series[0].radius[0],
					effect : {
						show : true,
						scaleSize : 12,
						color : 'rgba(250,225,50,0.8)',
						shadowBlur : 10,
						period : 30
					},
					data : [ {
						x : '50%',
						y : '50%'
					} ]
				};
				return series;
			})()
		};
		setTimeout(function() {
			var _ZR = myChart.getZrender();
			var TextShape = require('zrender/shape/Text');
			// 补充千层饼
			_ZR.addShape(new TextShape({
				style : {
					x : _ZR.getWidth() / 2,
					y : _ZR.getHeight() / 2,
					color : '#666',
					text : '恶梦的过去',
					textAlign : 'center'
				}
			}));
			_ZR.addShape(new TextShape({
				style : {
					x : _ZR.getWidth() / 2 + 200,
					y : _ZR.getHeight() / 2,
					brushType : 'fill',
					color : 'orange',
					text : '美好的未来',
					textAlign : 'left',
					textFont : 'normal 20px 微软雅黑'
				}
			}));
			_ZR.refresh();
		}, 2000);

		var myChart20 = echarts.init(document.getElementById('main20'),
				"infographic");
		myChart20.setOption(option20); */
		
		

		var symbolSize = 20;
		var data = [ [ 15, 0 ], [ -50, 10 ], [ -56.5, 20 ], [ -46.5, 30 ],
				[ -22.1, 40 ] ];

		var option16 = {
			title : {
				text : 'Try Dragging these Points'
			},
			tooltip : {
				triggerOn : 'none',
				formatter : function(params) {
					return 'X: ' + params.data[0].toFixed(2) + '<br>Y: '
							+ params.data[1].toFixed(2);
				}
			},
			grid : {},
			xAxis : {
				min : -100,
				max : 80,
				type : 'value',
				axisLine : {
					onZero : false
				}
			},
			yAxis : {
				min : -30,
				max : 60,
				type : 'value',
				axisLine : {
					onZero : false
				}
			},
			dataZoom : [ {
				type : 'slider',
				xAxisIndex : 0,
				filterMode : 'empty'
			}, {
				type : 'slider',
				yAxisIndex : 0,
				filterMode : 'empty'
			}, {
				type : 'inside',
				xAxisIndex : 0,
				filterMode : 'empty'
			}, {
				type : 'inside',
				yAxisIndex : 0,
				filterMode : 'empty'
			} ],
			series : [ {
				id : 'a',
				type : 'line',
				smooth : true,
				symbolSize : symbolSize,
				data : data
			} ]
		};

		var myChart16 = echarts.init(document.getElementById('main16'),
				"infographic");
		myChart16.setOption(option16);
		if (!app.inNode) {
			setTimeout(function() {
				// Add shadow circles (which is not visible) to enable drag.
				myChart16.setOption({
					graphic : echarts.util.map(data, function(item, dataIndex) {
						return {
							type : 'circle',
							position : myChart16.convertToPixel('grid', item),
							shape : {
								cx : 0,
								cy : 0,
								r : symbolSize / 2
							},
							invisible : true,
							draggable : true,
							ondrag : echarts.util.curry(onPointDragging,
									dataIndex),
							onmousemove : echarts.util.curry(showTooltip,
									dataIndex),
							onmouseout : echarts.util.curry(hideTooltip,
									dataIndex),
							z : 100
						};
					})
				});
			}, 0);

			window.addEventListener('resize', updatePosition);
		}

		myChart16.on('dataZoom', updatePosition);

		function updatePosition() {
			myChart16.setOption({
				graphic : echarts.util.map(data, function(item, dataIndex) {
					return {
						position : myChart16.convertToPixel('grid', item)
					};
				})
			});
		}

		function showTooltip(dataIndex) {
			myChart16.dispatchAction({
				type : 'showTip',
				seriesIndex : 0,
				dataIndex : dataIndex
			});
		}

		function hideTooltip(dataIndex) {
			myChart16.dispatchAction({
				type : 'hideTip'
			});
		}

		function onPointDragging(dataIndex, dx, dy) {
			data[dataIndex] = myChart16.convertFromPixel('grid', this.position);

			// Update data
			myChart16.setOption({
				series : [ {
					id : 'a',
					data : data
				} ]
			});
		}

		var dataMap = {};
		function dataFormatter(obj) {
			var pList = [ '北京', '天津', '河北', '山西', '内蒙古', '辽宁', '吉林', '黑龙江',
					'上海', '江苏', '浙江', '安徽', '福建', '江西', '山东', '河南', '湖北', '湖南',
					'广东', '广西', '海南', '重庆', '四川', '贵州', '云南', '西藏', '陕西', '甘肃',
					'青海', '宁夏', '新疆' ];
			var temp;
			for (var year = 2002; year <= 2011; year++) {
				var max = 0;
				var sum = 0;
				temp = obj[year];
				for (var i = 0, l = temp.length; i < l; i++) {
					max = Math.max(max, temp[i]);
					sum += temp[i];
					obj[year][i] = {
						name : pList[i],
						value : temp[i]
					}
				}
				obj[year + 'max'] = Math.floor(max / 100) * 100;
				obj[year + 'sum'] = sum;
			}
			return obj;
		}

		dataMap.dataGDP = dataFormatter({
			//max : 60000,
			2011 : [ 16251.93, 11307.28, 24515.76, 11237.55, 14359.88, 22226.7,
					10568.83, 12582, 19195.69, 49110.27, 32318.85, 15300.65,
					17560.18, 11702.82, 45361.85, 26931.03, 19632.26, 19669.56,
					53210.28, 11720.87, 2522.66, 10011.37, 21026.68, 5701.84,
					8893.12, 605.83, 12512.3, 5020.37, 1670.44, 2102.21,
					6610.05 ],
			2010 : [ 14113.58, 9224.46, 20394.26, 9200.86, 11672, 18457.27,
					8667.58, 10368.6, 17165.98, 41425.48, 27722.31, 12359.33,
					14737.12, 9451.26, 39169.92, 23092.36, 15967.61, 16037.96,
					46013.06, 9569.85, 2064.5, 7925.58, 17185.48, 4602.16,
					7224.18, 507.46, 10123.48, 4120.75, 1350.43, 1689.65,
					5437.47 ],
			2009 : [ 12153.03, 7521.85, 17235.48, 7358.31, 9740.25, 15212.49,
					7278.75, 8587, 15046.45, 34457.3, 22990.35, 10062.82,
					12236.53, 7655.18, 33896.65, 19480.46, 12961.1, 13059.69,
					39482.56, 7759.16, 1654.21, 6530.01, 14151.28, 3912.68,
					6169.75, 441.36, 8169.8, 3387.56, 1081.27, 1353.31, 4277.05 ],
			2008 : [ 11115, 6719.01, 16011.97, 7315.4, 8496.2, 13668.58,
					6426.1, 8314.37, 14069.87, 30981.98, 21462.69, 8851.66,
					10823.01, 6971.05, 30933.28, 18018.53, 11328.92, 11555,
					36796.71, 7021, 1503.06, 5793.66, 12601.23, 3561.56,
					5692.12, 394.85, 7314.58, 3166.82, 1018.62, 1203.92,
					4183.21 ],
			2007 : [ 9846.81, 5252.76, 13607.32, 6024.45, 6423.18, 11164.3,
					5284.69, 7104, 12494.01, 26018.48, 18753.73, 7360.92,
					9248.53, 5800.25, 25776.91, 15012.46, 9333.4, 9439.6,
					31777.01, 5823.41, 1254.17, 4676.13, 10562.39, 2884.11,
					4772.52, 341.43, 5757.29, 2703.98, 797.35, 919.11, 3523.16 ],
			2006 : [ 8117.78, 4462.74, 11467.6, 4878.61, 4944.25, 9304.52,
					4275.12, 6211.8, 10572.24, 21742.05, 15718.47, 6112.5,
					7583.85, 4820.53, 21900.19, 12362.79, 7617.47, 7688.67,
					26587.76, 4746.16, 1065.67, 3907.23, 8690.24, 2338.98,
					3988.14, 290.76, 4743.61, 2277.35, 648.5, 725.9, 3045.26 ],
			2005 : [ 6969.52, 3905.64, 10012.11, 4230.53, 3905.03, 8047.26,
					3620.27, 5513.7, 9247.66, 18598.69, 13417.68, 5350.17,
					6554.69, 4056.76, 18366.87, 10587.42, 6590.19, 6596.1,
					22557.37, 3984.1, 918.75, 3467.72, 7385.1, 2005.42,
					3462.73, 248.8, 3933.72, 1933.98, 543.32, 612.61, 2604.19 ],
			2004 : [ 6033.21, 3110.97, 8477.63, 3571.37, 3041.07, 6672,
					3122.01, 4750.6, 8072.83, 15003.6, 11648.7, 4759.3,
					5763.35, 3456.7, 15021.84, 8553.79, 5633.24, 5641.94,
					18864.62, 3433.5, 819.66, 3034.58, 6379.63, 1677.8,
					3081.91, 220.34, 3175.58, 1688.49, 466.1, 537.11, 2209.09 ],
			2003 : [ 5007.21, 2578.03, 6921.29, 2855.23, 2388.38, 6002.54,
					2662.08, 4057.4, 6694.23, 12442.87, 9705.02, 3923.11,
					4983.67, 2807.41, 12078.15, 6867.7, 4757.45, 4659.99,
					15844.64, 2821.11, 713.96, 2555.72, 5333.09, 1426.34,
					2556.02, 185.09, 2587.72, 1399.83, 390.2, 445.36, 1886.35 ],
			2002 : [ 4315, 2150.76, 6018.28, 2324.8, 1940.94, 5458.22, 2348.54,
					3637.2, 5741.03, 10606.85, 8003.67, 3519.72, 4467.55,
					2450.48, 10275.5, 6035.48, 4212.82, 4151.54, 13502.42,
					2523.73, 642.73, 2232.86, 4725.01, 1243.43, 2312.82,
					162.04, 2253.39, 1232.03, 340.65, 377.16, 1612.6 ]
		});

		dataMap.dataPI = dataFormatter({
			//max : 4000,
			2011 : [ 136.27, 159.72, 2905.73, 641.42, 1306.3, 1915.57, 1277.44,
					1701.5, 124.94, 3064.78, 1583.04, 2015.31, 1612.24,
					1391.07, 3973.85, 3512.24, 2569.3, 2768.03, 2665.2,
					2047.23, 659.23, 844.52, 2983.51, 726.22, 1411.01, 74.47,
					1220.9, 678.75, 155.08, 184.14, 1139.03 ],
			2010 : [ 124.36, 145.58, 2562.81, 554.48, 1095.28, 1631.08,
					1050.15, 1302.9, 114.15, 2540.1, 1360.56, 1729.02, 1363.67,
					1206.98, 3588.28, 3258.09, 2147, 2325.5, 2286.98, 1675.06,
					539.83, 685.38, 2482.89, 625.03, 1108.38, 68.72, 988.45,
					599.28, 134.92, 159.29, 1078.63 ],
			2009 : [ 118.29, 128.85, 2207.34, 477.59, 929.6, 1414.9, 980.57,
					1154.33, 113.82, 2261.86, 1163.08, 1495.45, 1182.74,
					1098.66, 3226.64, 2769.05, 1795.9, 1969.69, 2010.27,
					1458.49, 462.19, 606.8, 2240.61, 550.27, 1067.6, 63.88,
					789.64, 497.05, 107.4, 127.25, 759.74 ],
			2008 : [ 112.83, 122.58, 2034.59, 313.58, 907.95, 1302.02, 916.72,
					1088.94, 111.8, 2100.11, 1095.96, 1418.09, 1158.17,
					1060.38, 3002.65, 2658.78, 1780, 1892.4, 1973.05, 1453.75,
					436.04, 575.4, 2216.15, 539.19, 1020.56, 60.62, 753.72,
					462.27, 105.57, 118.94, 691.07 ],
			2007 : [ 101.26, 110.19, 1804.72, 311.97, 762.1, 1133.42, 783.8,
					915.38, 101.84, 1816.31, 986.02, 1200.18, 1002.11, 905.77,
					2509.14, 2217.66, 1378, 1626.48, 1695.57, 1241.35, 361.07,
					482.39, 2032, 446.38, 837.35, 54.89, 592.63, 387.55, 83.41,
					97.89, 628.72 ],
			2006 : [ 88.8, 103.35, 1461.81, 276.77, 634.94, 939.43, 672.76,
					750.14, 93.81, 1545.05, 925.1, 1011.03, 865.98, 786.14,
					2138.9, 1916.74, 1140.41, 1272.2, 1532.17, 1032.47, 323.48,
					386.38, 1595.48, 382.06, 724.4, 50.9, 484.81, 334, 67.55,
					79.54, 527.8 ],
			2005 : [ 88.68, 112.38, 1400, 262.42, 589.56, 882.41, 625.61,
					684.6, 90.26, 1461.51, 892.83, 966.5, 827.36, 727.37,
					1963.51, 1892.01, 1082.13, 1100.65, 1428.27, 912.5, 300.75,
					463.4, 1481.14, 368.94, 661.69, 48.04, 435.77, 308.06,
					65.34, 72.07, 509.99 ],
			2004 : [ 87.36, 105.28, 1370.43, 276.3, 522.8, 798.43, 568.69,
					605.79, 83.45, 1367.58, 814.1, 950.5, 786.84, 664.5,
					1778.45, 1649.29, 1020.09, 1022.45, 1248.59, 817.88,
					278.76, 428.05, 1379.93, 334.5, 607.75, 44.3, 387.88,
					286.78, 60.7, 65.33, 461.26 ],
			2003 : [ 84.11, 89.91, 1064.05, 215.19, 420.1, 615.8, 488.23,
					504.8, 81.02, 1162.45, 717.85, 749.4, 692.94, 560, 1480.67,
					1198.7, 798.35, 886.47, 1072.91, 658.78, 244.29, 339.06,
					1128.61, 298.69, 494.6, 40.7, 302.66, 237.91, 48.47, 55.63,
					412.9 ],
			2002 : [ 82.44, 84.21, 956.84, 197.8, 374.69, 590.2, 446.17, 474.2,
					79.68, 1110.44, 685.2, 783.66, 664.78, 535.98, 1390,
					1288.36, 707, 847.25, 1015.08, 601.99, 222.89, 317.87,
					1047.95, 281.1, 463.44, 39.75, 282.21, 215.51, 47.31,
					52.95, 305 ]
		});

		dataMap.dataSI = dataFormatter({
			//max : 26600,
			2011 : [ 3752.48, 5928.32, 13126.86, 6635.26, 8037.69, 12152.15,
					5611.48, 5962.41, 7927.89, 25203.28, 16555.58, 8309.38,
					9069.2, 6390.55, 24017.11, 15427.08, 9815.94, 9361.99,
					26447.38, 5675.32, 714.5, 5543.04, 11029.13, 2194.33,
					3780.32, 208.79, 6935.59, 2377.83, 975.18, 1056.15, 3225.9 ],
			2010 : [ 3388.38, 4840.23, 10707.68, 5234, 6367.69, 9976.82,
					4506.31, 5025.15, 7218.32, 21753.93, 14297.93, 6436.62,
					7522.83, 5122.88, 21238.49, 13226.38, 7767.24, 7343.19,
					23014.53, 4511.68, 571, 4359.12, 8672.18, 1800.06, 3223.49,
					163.92, 5446.1, 1984.97, 744.63, 827.91, 2592.15 ],
			2009 : [ 2855.55, 3987.84, 8959.83, 3993.8, 5114, 7906.34, 3541.92,
					4060.72, 6001.78, 18566.37, 11908.49, 4905.22, 6005.3,
					3919.45, 18901.83, 11010.5, 6038.08, 5687.19, 19419.7,
					3381.54, 443.43, 3448.77, 6711.87, 1476.62, 2582.53,
					136.63, 4236.42, 1527.24, 575.33, 662.32, 1929.59 ],
			2008 : [ 2626.41, 3709.78, 8701.34, 4242.36, 4376.19, 7158.84,
					3097.12, 4319.75, 6085.84, 16993.34, 11567.42, 4198.93,
					5318.44, 3554.81, 17571.98, 10259.99, 5082.07, 5028.93,
					18502.2, 3037.74, 423.55, 3057.78, 5823.39, 1370.03,
					2452.75, 115.56, 3861.12, 1470.34, 557.12, 609.98, 2070.76 ],
			2007 : [ 2509.4, 2892.53, 7201.88, 3454.49, 3193.67, 5544.14,
					2475.45, 3695.58, 5571.06, 14471.26, 10154.25, 3370.96,
					4476.42, 2975.53, 14647.53, 8282.83, 4143.06, 3977.72,
					16004.61, 2425.29, 364.26, 2368.53, 4648.79, 1124.79,
					2038.39, 98.48, 2986.46, 1279.32, 419.03, 455.04, 1647.55 ],
			2006 : [ 2191.43, 2457.08, 6110.43, 2755.66, 2374.96, 4566.83,
					1915.29, 3365.31, 4969.95, 12282.89, 8511.51, 2711.18,
					3695.04, 2419.74, 12574.03, 6724.61, 3365.08, 3187.05,
					13469.77, 1878.56, 308.62, 1871.65, 3775.14, 967.54,
					1705.83, 80.1, 2452.44, 1043.19, 331.91, 351.58, 1459.3 ],
			2005 : [ 2026.51, 2135.07, 5271.57, 2357.04, 1773.21, 3869.4,
					1580.83, 2971.68, 4381.2, 10524.96, 7164.75, 2245.9,
					3175.92, 1917.47, 10478.62, 5514.14, 2852.12, 2612.57,
					11356.6, 1510.68, 240.83, 1564, 3067.23, 821.16, 1426.42,
					63.52, 1951.36, 838.56, 264.61, 281.05, 1164.79 ],
			2004 : [ 1853.58, 1685.93, 4301.73, 1919.4, 1248.27, 3061.62,
					1329.68, 2487.04, 3892.12, 8437.99, 6250.38, 1844.9,
					2770.49, 1566.4, 8478.69, 4182.1, 2320.6, 2190.54, 9280.73,
					1253.7, 205.6, 1376.91, 2489.4, 681.5, 1281.63, 52.74,
					1553.1, 713.3, 211.7, 244.05, 914.47 ],
			2003 : [ 1487.15, 1337.31, 3417.56, 1463.38, 967.49, 2898.89,
					1098.37, 2084.7, 3209.02, 6787.11, 5096.38, 1535.29,
					2340.82, 1204.33, 6485.05, 3310.14, 1956.02, 1777.74,
					7592.78, 984.08, 175.82, 1135.31, 2014.8, 569.37, 1047.66,
					47.64, 1221.17, 572.02, 171.92, 194.27, 719.54 ],
			2002 : [ 1249.99, 1069.08, 2911.69, 1134.31, 754.78, 2609.85,
					943.49, 1843.6, 2622.45, 5604.49, 4090.48, 1337.04,
					2036.97, 941.77, 5184.98, 2768.75, 1709.89, 1523.5, 6143.4,
					846.89, 148.88, 958.87, 1733.38, 481.96, 934.88, 32.72,
					1007.56, 501.69, 144.51, 153.06, 603.15 ]
		});

		dataMap.dataTI = dataFormatter({
			//max : 25000,
			2011 : [ 12363.18, 5219.24, 8483.17, 3960.87, 5015.89, 8158.98,
					3679.91, 4918.09, 11142.86, 20842.21, 14180.23, 4975.96,
					6878.74, 3921.2, 17370.89, 7991.72, 7247.02, 7539.54,
					24097.7, 3998.33, 1148.93, 3623.81, 7014.04, 2781.29,
					3701.79, 322.57, 4355.81, 1963.79, 540.18, 861.92, 2245.12 ],
			2010 : [ 10600.84, 4238.65, 7123.77, 3412.38, 4209.03, 6849.37,
					3111.12, 4040.55, 9833.51, 17131.45, 12063.82, 4193.69,
					5850.62, 3121.4, 14343.14, 6607.89, 6053.37, 6369.27,
					20711.55, 3383.11, 953.67, 2881.08, 6030.41, 2177.07,
					2892.31, 274.82, 3688.93, 1536.5, 470.88, 702.45, 1766.69 ],
			2009 : [ 9179.19, 3405.16, 6068.31, 2886.92, 3696.65, 5891.25,
					2756.26, 3371.95, 8930.85, 13629.07, 9918.78, 3662.15,
					5048.49, 2637.07, 11768.18, 5700.91, 5127.12, 5402.81,
					18052.59, 2919.13, 748.59, 2474.44, 5198.8, 1885.79,
					2519.62, 240.85, 3143.74, 1363.27, 398.54, 563.74, 1587.72 ],
			2008 : [ 8375.76, 2886.65, 5276.04, 2759.46, 3212.06, 5207.72,
					2412.26, 2905.68, 7872.23, 11888.53, 8799.31, 3234.64,
					4346.4, 2355.86, 10358.64, 5099.76, 4466.85, 4633.67,
					16321.46, 2529.51, 643.47, 2160.48, 4561.69, 1652.34,
					2218.81, 218.67, 2699.74, 1234.21, 355.93, 475, 1421.38 ],
			2007 : [ 7236.15, 2250.04, 4600.72, 2257.99, 2467.41, 4486.74,
					2025.44, 2493.04, 6821.11, 9730.91, 7613.46, 2789.78, 3770,
					1918.95, 8620.24, 4511.97, 3812.34, 3835.4, 14076.83,
					2156.76, 528.84, 1825.21, 3881.6, 1312.94, 1896.78, 188.06,
					2178.2, 1037.11, 294.91, 366.18, 1246.89 ],
			2006 : [ 5837.55, 1902.31, 3895.36, 1846.18, 1934.35, 3798.26,
					1687.07, 2096.35, 5508.48, 7914.11, 6281.86, 2390.29,
					3022.83, 1614.65, 7187.26, 3721.44, 3111.98, 3229.42,
					11585.82, 1835.12, 433.57, 1649.2, 3319.62, 989.38,
					1557.91, 159.76, 1806.36, 900.16, 249.04, 294.78, 1058.16 ],
			2005 : [ 4854.33, 1658.19, 3340.54, 1611.07, 1542.26, 3295.45,
					1413.83, 1857.42, 4776.2, 6612.22, 5360.1, 2137.77,
					2551.41, 1411.92, 5924.74, 3181.27, 2655.94, 2882.88,
					9772.5, 1560.92, 377.17, 1440.32, 2836.73, 815.32, 1374.62,
					137.24, 1546.59, 787.36, 213.37, 259.49, 929.41 ],
			2004 : [ 4092.27, 1319.76, 2805.47, 1375.67, 1270, 2811.95,
					1223.64, 1657.77, 4097.26, 5198.03, 4584.22, 1963.9,
					2206.02, 1225.8, 4764.7, 2722.4, 2292.55, 2428.95, 8335.3,
					1361.92, 335.3, 1229.62, 2510.3, 661.8, 1192.53, 123.3,
					1234.6, 688.41, 193.7, 227.73, 833.36 ],
			2003 : [ 3435.95, 1150.81, 2439.68, 1176.65, 1000.79, 2487.85,
					1075.48, 1467.9, 3404.19, 4493.31, 3890.79, 1638.42,
					1949.91, 1043.08, 4112.43, 2358.86, 2003.08, 1995.78,
					7178.94, 1178.25, 293.85, 1081.35, 2189.68, 558.28,
					1013.76, 96.76, 1063.89, 589.91, 169.81, 195.46, 753.91 ],
			2002 : [ 2982.57, 997.47, 2149.75, 992.69, 811.47, 2258.17, 958.88,
					1319.4, 3038.9, 3891.92, 3227.99, 1399.02, 1765.8, 972.73,
					3700.52, 1978.37, 1795.93, 1780.79, 6343.94, 1074.85,
					270.96, 956.12, 1943.68, 480.37, 914.5, 89.56, 963.62,
					514.83, 148.83, 171.14, 704.5 ]
		});

		dataMap.dataEstate = dataFormatter({
			//max : 3600,
			2011 : [ 1074.93, 411.46, 918.02, 224.91, 384.76, 876.12, 238.61,
					492.1, 1019.68, 2747.89, 1677.13, 634.92, 911.16, 402.51,
					1838.14, 987, 634.67, 518.04, 3321.31, 465.68, 208.71,
					396.28, 620.62, 160.3, 222.31, 17.44, 398.03, 134.25,
					29.05, 79.01, 176.22 ],
			2010 : [ 1006.52, 377.59, 697.79, 192, 309.25, 733.37, 212.32,
					391.89, 1002.5, 2600.95, 1618.17, 532.17, 679.03, 340.56,
					1622.15, 773.23, 564.41, 464.21, 2813.95, 405.79, 188.33,
					266.38, 558.56, 139.64, 223.45, 14.54, 315.95, 110.02,
					25.41, 60.53, 143.44 ],
			2009 : [ 1062.47, 308.73, 612.4, 173.31, 286.65, 605.27, 200.14,
					301.18, 1237.56, 2025.39, 1316.84, 497.94, 656.61, 305.9,
					1329.59, 622.98, 546.11, 400.11, 2470.63, 348.98, 121.76,
					229.09, 548.14, 136.15, 205.14, 13.28, 239.92, 101.37,
					23.05, 47.56, 115.23 ],
			2008 : [ 844.59, 227.88, 513.81, 166.04, 273.3, 500.81, 182.7,
					244.47, 939.34, 1626.13, 1052.03, 431.27, 506.98, 281.96,
					1104.95, 512.42, 526.88, 340.07, 2057.45, 282.96, 95.6,
					191.21, 453.63, 104.81, 195.48, 15.08, 193.27, 93.8, 19.96,
					38.85, 89.79 ],
			2007 : [ 821.5, 183.44, 467.97, 134.12, 191.01, 410.43, 153.03,
					225.81, 958.06, 1365.71, 981.42, 366.57, 511.5, 225.96,
					953.69, 447.44, 409.65, 301.8, 2029.77, 239.45, 67.19,
					196.06, 376.84, 93.19, 193.59, 13.24, 153.98, 83.52, 16.98,
					29.49, 91.28 ],
			2006 : [ 658.3, 156.64, 397.14, 117.01, 136.5, 318.54, 131.01,
					194.7, 773.61, 1017.91, 794.41, 281.98, 435.22, 184.67,
					786.51, 348.7, 294.73, 254.81, 1722.07, 192.2, 44.45,
					158.2, 336.2, 80.24, 165.92, 11.92, 125.2, 73.21, 15.17,
					25.53, 68.9 ],
			2005 : [ 493.73, 122.67, 330.87, 106, 98.75, 256.77, 112.29,
					163.34, 715.97, 799.73, 688.86, 231.66, 331.8, 171.88,
					664.9, 298.19, 217.17, 215.63, 1430.37, 165.05, 38.2,
					143.88, 286.23, 76.38, 148.69, 10.02, 108.62, 63.78, 14.1,
					22.97, 55.79 ],
			2004 : [ 436.11, 106.14, 231.08, 95.1, 73.81, 203.1, 97.93, 137.74,
					666.3, 534.17, 587.83, 188.28, 248.44, 167.2, 473.27,
					236.44, 204.8, 191.5, 1103.75, 122.52, 30.64, 129.12,
					264.3, 68.3, 116.54, 5.8, 95.9, 56.84, 13, 20.78, 53.55 ],
			2003 : [ 341.88, 92.31, 185.19, 78.73, 61.05, 188.49, 91.99, 127.2,
					487.82, 447.47, 473.16, 162.63, 215.84, 138.02, 418.21,
					217.58, 176.8, 186.49, 955.66, 100.93, 25.14, 113.69,
					231.72, 59.86, 103.79, 4.35, 83.9, 48.09, 11.41, 16.85,
					47.84 ],
			2002 : [ 298.02, 73.04, 140.89, 65.83, 51.48, 130.94, 76.11, 118.7,
					384.86, 371.09, 360.63, 139.18, 188.09, 125.27, 371.13,
					199.31, 145.17, 165.29, 808.16, 82.83, 21.45, 90.48,
					210.82, 53.49, 95.68, 3.42, 77.68, 41.52, 9.74, 13.46,
					43.04 ]
		});

		dataMap.dataFinancial = dataFormatter({
			//max : 3200,
			2011 : [ 2215.41, 756.5, 746.01, 519.32, 447.46, 755.57, 207.65,
					370.78, 2277.4, 2600.11, 2730.29, 503.85, 862.41, 357.44,
					1640.41, 868.2, 674.57, 501.09, 2916.13, 445.37, 105.24,
					704.66, 868.15, 297.27, 456.23, 31.7, 432.11, 145.05,
					62.56, 134.18, 288.77 ],
			2010 : [ 1863.61, 572.99, 615.42, 448.3, 346.44, 639.27, 190.12,
					304.59, 1950.96, 2105.92, 2326.58, 396.17, 767.58, 241.49,
					1361.45, 697.68, 561.27, 463.16, 2658.76, 384.53, 78.12,
					496.56, 654.7, 231.51, 375.08, 27.08, 384.75, 100.54,
					54.53, 97.87, 225.2 ],
			2009 : [ 1603.63, 461.2, 525.67, 361.64, 291.1, 560.2, 180.83,
					227.54, 1804.28, 1596.98, 1899.33, 359.6, 612.2, 165.1,
					1044.9, 499.92, 479.11, 402.57, 2283.29, 336.82, 65.73,
					389.97, 524.63, 194.44, 351.74, 23.17, 336.21, 88.27,
					45.63, 75.54, 198.87 ],
			2008 : [ 1519.19, 368.1, 420.74, 290.91, 219.09, 455.07, 147.24,
					177.43, 1414.21, 1298.48, 1653.45, 313.81, 497.65, 130.57,
					880.28, 413.83, 393.05, 334.32, 1972.4, 249.01, 47.33,
					303.01, 411.14, 151.55, 277.66, 22.42, 287.16, 72.49,
					36.54, 64.8, 171.97 ],
			2007 : [ 1302.77, 288.17, 347.65, 218.73, 148.3, 386.34, 126.03,
					155.48, 1209.08, 1054.25, 1251.43, 223.85, 385.84, 101.34,
					734.9, 302.31, 337.27, 260.14, 1705.08, 190.73, 34.43,
					247.46, 359.11, 122.25, 168.55, 11.51, 231.03, 61.6, 27.67,
					51.05, 149.22 ],
			2006 : [ 982.37, 186.87, 284.04, 169.63, 108.21, 303.41, 100.75,
					74.17, 825.2, 653.25, 906.37, 166.01, 243.9, 79.75, 524.94,
					219.72, 174.99, 204.72, 899.91, 129.14, 16.37, 213.7,
					299.5, 89.43, 143.62, 6.44, 152.25, 50.51, 23.69, 36.99,
					99.25 ],
			2005 : [ 840.2, 147.4, 213.47, 135.07, 72.52, 232.85, 83.63, 35.03,
					675.12, 492.4, 686.32, 127.05, 186.12, 69.55, 448.36,
					181.74, 127.32, 162.37, 661.81, 91.93, 13.16, 185.18,
					262.26, 73.67, 130.5, 7.57, 127.58, 44.73, 20.36, 32.25,
					80.34 ],
			2004 : [ 713.79, 136.97, 209.1, 110.29, 55.89, 188.04, 77.17, 32.2,
					612.45, 440.5, 523.49, 94.1, 171, 65.1, 343.37, 170.82,
					118.85, 118.64, 602.68, 74, 11.56, 162.38, 236.5, 60.3,
					118.4, 5.4, 90.1, 42.99, 19, 27.92, 70.3 ],
			2003 : [ 635.56, 112.79, 199.87, 118.48, 55.89, 145.38, 73.15,
					32.2, 517.97, 392.11, 451.54, 87.45, 150.09, 64.31, 329.71,
					165.11, 107.31, 99.35, 534.28, 61.59, 10.68, 147.04,
					206.24, 48.01, 105.48, 4.74, 77.87, 42.31, 17.98, 24.8,
					64.92 ],
			2002 : [ 561.91, 76.86, 179.6, 124.1, 48.39, 137.18, 75.45, 31.6,
					485.25, 368.86, 347.53, 81.85, 138.28, 76.51, 310.07,
					158.77, 96.95, 92.43, 454.65, 35.86, 10.08, 134.52, 183.13,
					41.45, 102.39, 2.81, 67.3, 42.08, 16.75, 21.45, 52.18 ]
		});

		var option21 = {
			baseOption : {
				timeline : {
					// y: 0,
					axisType : 'category',
					// realtime: false,
					// loop: false,
					autoPlay : true,
					// currentIndex: 2,
					playInterval : 1000,
					// controlStyle: {
					//     position: 'left'
					// },
					data : [ '2002-01-01', '2003-01-01', '2004-01-01', {
						value : '2005-01-01',
						tooltip : {
							formatter : '{b} GDP达到一个高度'
						},
						symbol : 'diamond',
						symbolSize : 16
					}, '2006-01-01', '2007-01-01', '2008-01-01', '2009-01-01',
							'2010-01-01', {
								value : '2011-01-01',
								tooltip : {
									formatter : function(params) {
										return params.name + 'GDP达到又一个高度';
									}
								},
								symbol : 'diamond',
								symbolSize : 18
							}, ],
					label : {
						formatter : function(s) {
							return (new Date(s)).getFullYear();
						}
					}
				},
				title : {
					subtext : '数据来自国家统计局'
				},
				tooltip : {},
				legend : {
					x : 'right',
					data : [ '第一产业', '第二产业', '第三产业', 'GDP', '金融', '房地产' ],
					selected : {
						'GDP' : false,
						'金融' : false,
						'房地产' : false
					}
				},
				calculable : true,
				grid : {
					top : 80,
					bottom : 100,
					tooltip : {
						trigger : 'axis',
						axisPointer : {
							type : 'shadow',
							label : {
								show : true,
								formatter : function(params) {
									return params.value.replace('\n', '');
								}
							}
						}
					}
				},
				xAxis : [ {
					'type' : 'category',
					'axisLabel' : {
						'interval' : 0
					},
					'data' : [ '北京', '\n天津', '河北', '\n山西', '内蒙古', '\n辽宁', '吉林',
							'\n黑龙江', '上海', '\n江苏', '浙江', '\n安徽', '福建', '\n江西',
							'山东', '\n河南', '湖北', '\n湖南', '广东', '\n广西', '海南',
							'\n重庆', '四川', '\n贵州', '云南', '\n西藏', '陕西', '\n甘肃',
							'青海', '\n宁夏', '新疆' ],
					splitLine : {
						show : false
					}
				} ],
				yAxis : [ {
					type : 'value',
					name : 'GDP（亿元）'
				} ],
				series : [ {
					name : 'GDP',
					type : 'bar'
				}, {
					name : '金融',
					type : 'bar'
				}, {
					name : '房地产',
					type : 'bar'
				}, {
					name : '第一产业',
					type : 'bar'
				}, {
					name : '第二产业',
					type : 'bar'
				}, {
					name : '第三产业',
					type : 'bar'
				}, {
					name : 'GDP占比',
					type : 'pie',
					center : [ '75%', '35%' ],
					radius : '28%',
					z : 100
				} ]
			},
			options : [ {
				title : {
					text : '2002全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2002']
				}, {
					data : dataMap.dataFinancial['2002']
				}, {
					data : dataMap.dataEstate['2002']
				}, {
					data : dataMap.dataPI['2002']
				}, {
					data : dataMap.dataSI['2002']
				}, {
					data : dataMap.dataTI['2002']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2002sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2002sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2002sum']
					} ]
				} ]
			}, {
				title : {
					text : '2003全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2003']
				}, {
					data : dataMap.dataFinancial['2003']
				}, {
					data : dataMap.dataEstate['2003']
				}, {
					data : dataMap.dataPI['2003']
				}, {
					data : dataMap.dataSI['2003']
				}, {
					data : dataMap.dataTI['2003']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2003sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2003sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2003sum']
					} ]
				} ]
			}, {
				title : {
					text : '2004全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2004']
				}, {
					data : dataMap.dataFinancial['2004']
				}, {
					data : dataMap.dataEstate['2004']
				}, {
					data : dataMap.dataPI['2004']
				}, {
					data : dataMap.dataSI['2004']
				}, {
					data : dataMap.dataTI['2004']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2004sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2004sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2004sum']
					} ]
				} ]
			}, {
				title : {
					text : '2005全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2005']
				}, {
					data : dataMap.dataFinancial['2005']
				}, {
					data : dataMap.dataEstate['2005']
				}, {
					data : dataMap.dataPI['2005']
				}, {
					data : dataMap.dataSI['2005']
				}, {
					data : dataMap.dataTI['2005']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2005sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2005sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2005sum']
					} ]
				} ]
			}, {
				title : {
					text : '2006全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2006']
				}, {
					data : dataMap.dataFinancial['2006']
				}, {
					data : dataMap.dataEstate['2006']
				}, {
					data : dataMap.dataPI['2006']
				}, {
					data : dataMap.dataSI['2006']
				}, {
					data : dataMap.dataTI['2006']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2006sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2006sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2006sum']
					} ]
				} ]
			}, {
				title : {
					text : '2007全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2007']
				}, {
					data : dataMap.dataFinancial['2007']
				}, {
					data : dataMap.dataEstate['2007']
				}, {
					data : dataMap.dataPI['2007']
				}, {
					data : dataMap.dataSI['2007']
				}, {
					data : dataMap.dataTI['2007']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2007sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2007sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2007sum']
					} ]
				} ]
			}, {
				title : {
					text : '2008全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2008']
				}, {
					data : dataMap.dataFinancial['2008']
				}, {
					data : dataMap.dataEstate['2008']
				}, {
					data : dataMap.dataPI['2008']
				}, {
					data : dataMap.dataSI['2008']
				}, {
					data : dataMap.dataTI['2008']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2008sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2008sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2008sum']
					} ]
				} ]
			}, {
				title : {
					text : '2009全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2009']
				}, {
					data : dataMap.dataFinancial['2009']
				}, {
					data : dataMap.dataEstate['2009']
				}, {
					data : dataMap.dataPI['2009']
				}, {
					data : dataMap.dataSI['2009']
				}, {
					data : dataMap.dataTI['2009']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2009sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2009sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2009sum']
					} ]
				} ]
			}, {
				title : {
					text : '2010全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2010']
				}, {
					data : dataMap.dataFinancial['2010']
				}, {
					data : dataMap.dataEstate['2010']
				}, {
					data : dataMap.dataPI['2010']
				}, {
					data : dataMap.dataSI['2010']
				}, {
					data : dataMap.dataTI['2010']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2010sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2010sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2010sum']
					} ]
				} ]
			}, {
				title : {
					text : '2011全国宏观经济指标'
				},
				series : [ {
					data : dataMap.dataGDP['2011']
				}, {
					data : dataMap.dataFinancial['2011']
				}, {
					data : dataMap.dataEstate['2011']
				}, {
					data : dataMap.dataPI['2011']
				}, {
					data : dataMap.dataSI['2011']
				}, {
					data : dataMap.dataTI['2011']
				}, {
					data : [ {
						name : '第一产业',
						value : dataMap.dataPI['2011sum']
					}, {
						name : '第二产业',
						value : dataMap.dataSI['2011sum']
					}, {
						name : '第三产业',
						value : dataMap.dataTI['2011sum']
					} ]
				} ]
			} ]
		};

		var myChart21 = echarts.init(document.getElementById('main21'),
				"infographic");
		myChart21.setOption(option21);
	</script>
	
	<script type="text/javascript">
	var myChart_main4_1 = echarts.init(document.getElementById("main4_1"));
	
	
	myChart_main4_1.showLoading({
	    text: '正在努力的读取数据中...',
	});
	
	
	var option_main4_1 = {
	    title : {
	        text : '近三年各省理工科最低分排名',
			subtext : '数据来源：本系统'
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:[
'2017年',
			'2015年',
			
			'2016年'
			
			
			
	        ]
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            
	            saveAsImage : {show: true}
	            
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            data : [

<c:forEach items="${fspm}" var="entry" varStatus="status">
'${entry.ssmc}'<c:if test="${!status.last}">,</c:if>
</c:forEach>
	
	            ]
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : [
	              

{
    name: '2017年',
    type: 'bar',
    data: [
    
<c:forEach items="${fspm}" var="entry" varStatus="status">
'${entry.ljrs1}'<c:if test="${!status.last}">,</c:if>
</c:forEach>
    ]
    
},

	    	
	        {
	            name: '2016年',
	            type: 'bar',
	            data: [
	            
<c:forEach items="${fspm}" var="entry" varStatus="status">
'${entry.ljrs2}'<c:if test="${!status.last}">,</c:if>
</c:forEach>
				
	            ]
	            
	        },
{
    name: '2015年',
    type: 'bar',
    data: [
<c:forEach items="${fspm}" var="entry" varStatus="status">
'${entry.ljrs3}'<c:if test="${!status.last}">,</c:if>
</c:forEach>
	
    ]
    
}
	    	
	    ]
	};

	
	setTimeout(function(){
		myChart_main4_1.hideLoading();
	
	
		if (option_main4_1 && typeof option_main4_1 === "object") {
			myChart_main4_1.setOption(option_main4_1, true);
		}
		
	
	}, 500);
	
</script>
</body>
</html>
