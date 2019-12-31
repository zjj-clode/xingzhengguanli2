<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lte"/>
</head>
<body>
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
         ${fns:getDictLabel("nowyear", "nowyear", "2018")}年招生情况统计
        <small>从基本情况，招生类别、来源、年龄、政治面貌、性别等组成对学校${fns:getDictLabel("nowyear", "nowyear", "")}年招生情况进行统计。</small>
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
          <li class="on"><a href="${ctx}/lqcx/zhaoshengLqsta">全校</a></li>
          <c:forEach items="${ksxy}" var="staMap" varStatus="stat">
          <li><a href="${ctx}/lqcx/zhaoshengLqsta/colindex?yxdm=${staMap["yxdm"]}">${staMap["xy"]}</a></li>
          </c:forEach>
        </ul>
      </div>
      
    </div>

    <!-- Main content -->
    <section class="content">
      <div class="chartHeader">
        <div class="census-01 text-center">
          <span><i class="fa fa-users"></i></span>
          <span><strong class="text-green">${kscount}</strong>人<br>${fns:getDictLabel("nowyear", "nowyear", "2018")}级新生人数</span>
        </div>
        <div class="row">
          <div class="col-lg-4 col-xs-12">
            <div class="census-02 clear">
              <table style="margin-top:20px;">

          <c:set value="aqua" var="colorvalue"/><!--颜色设置-->
        <c:forEach items="${ksxbcount}" var="staMap" varStatus="stat">
          <c:set var="xbper" value="${(staMap['stavalue']*100.0/kscount)}"  scope="request"/>
		  <c:if test="${stat.index eq 1}">
            <c:set value="yellow" var="colorvalue"/>
           </c:if>
			   <tr>
                  <td><i class="fa fa-circle-o text-${colorvalue}"></i> ${staMap['stalabel']}</td>
                  <td class="text-green">${staMap['stavalue']}</td>
                  <td><fmt:formatNumber type="number" value="${xbper}" pattern="0.00" maxFractionDigits="2"/> %</td>
                  <td class="rate">
                    <div class="progress progress-xs">
                      <div class="progress-bar progress-bar-${colorvalue}" role="progressbar" style="width: <fmt:formatNumber type="number" value="${xbper}" pattern="0.00" maxFractionDigits="2"/>%">
                        <span class="sr-only"><fmt:formatNumber type="number" value="${xbper}" pattern="0.00" maxFractionDigits="2"/>% Complete (success)</span>
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
              <c:set var="tjper" value="${(staMap['stavalue']*100.0/kscount)}"  scope="request"/>
               <c:if test="${stat.index eq 0}">
                   <c:set value="aqua" var="colorvalue"/>
                </c:if>
               <c:if test="${stat.index eq 1}">
                   <c:set value="yellow" var="colorvalue"/>
                </c:if>
                <c:if test="${stat.index eq 2}">
                   <c:set value="red" var="colorvalue"/>
                </c:if>
                <tr>
                  <td><i class="fa fa-circle-o text-${colorvalue}"></i> ${staMap['stalabel']}</td>
                  <td class="text-green">${staMap['stavalue']}</td>
                  <td><fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2"/>%</td>
                  <td class="rate">
                    <div class="progress progress-xs">
                      <div class="progress-bar progress-bar-${colorvalue}" role="progressbar" style="width: <fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2"/>%">
                        <span class="sr-only"><fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2"/>% Complete (success)</span>
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
              <c:set var="tjper" value="${(staMap['stavalue']*100.0/kscount)}"  scope="request"/>
               <c:if test="${stat.index eq 0}">
                   <c:set value="aqua" var="colorvalue"/>
                </c:if>
               <c:if test="${stat.index eq 1}">
                   <c:set value="yellow" var="colorvalue"/>
                </c:if>
                <c:if test="${stat.index eq 2}">
                   <c:set value="red" var="colorvalue"/>
                </c:if>
                <tr>
                  <td><i class="fa fa-circle-o text-${colorvalue}"></i> ${staMap['stalabel']}</td>
                  <td class="text-green">${staMap['stavalue']}</td>
                  <td><fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="3"/>%</td>
                  <td class="rate">
                    <div class="progress progress-xs">
                      <div class="progress-bar progress-bar-${colorvalue}" role="progressbar" style="width: <fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2"/>%">
                        <span class="sr-only"><fmt:formatNumber type="number" value="${tjper}" pattern="0.00" maxFractionDigits="2"/>% Complete (success)</span>
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
				<span class="text-green pull-left">2017级新生分院系人数统计</span>
				
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kscount}" pattern="0.00" maxFractionDigits="3"/>%</td>
				 </tr>
		         </c:forEach>
				 
				</tbody>
			  </table>
	
	        </div>
	        <div class="col-lg-6 col-md-12" style="height:680px" id="main1">
	      
	        </div>
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kscount}" pattern="0.00" maxFractionDigits="3"/>%</td>
				 </tr>
				 </c:forEach>
				</tbody>
			  </table>
	        </div>
	        <div class="col-lg-6 col-md-12" id="main2">
	      
	        </div>
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kscount}" pattern="0.00" maxFractionDigits="3"/>%</td>
				 </tr>
				 </c:forEach>
				</tbody>
			  </table>
	        </div>
	        <div class="col-lg-6 col-md-12" id="main2">
	      
	        </div>
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3"/>%</td>
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
	        <div class="col-lg-6 col-md-12" id="main2">
	      
	        </div>
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

				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stalabel']}" pattern="0" maxFractionDigits="0"/></td>
				   <td class="tc">${staMap['stavalue']}</td>
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3"/>%</td>
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
	        <div class="col-lg-6 col-md-12"  id="main2">
	        </div>
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3"/>%</td>
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
	        <div class="col-lg-6 col-md-12"  id="main2">
	        </div>
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3"/>%</td>
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
	        <div class="col-lg-6 col-md-12"  id="main2">
	        </div>
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3"/>%</td>
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
	        <div class="col-lg-6 col-md-12"  id="main2">
	        </div>
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3"/>%</td>
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
	        <div class="col-lg-6 col-md-12"  id="main2">
	        </div>
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3"/>%</td>
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
	        <div class="col-lg-6 col-md-12"  id="main2">
	        </div>
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3"/>%</td>
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
	        <div class="col-lg-6 col-md-12"  id="main2">
	        </div>
	      </div>
	      
	      <div class="row">
	      	<!-- 一行2个table -->
	        <div class="col-lg-6 col-md-12" >
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
				   <td class="tc"><fmt:formatNumber type="number" value="${staMap['stavalue']*100.0/kstjcount}" pattern="0.00" maxFractionDigits="3"/>%</td>
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
	        <div class="col-lg-6 col-md-12"  style="height:680px" id="main2c">
	        </div>
	      </div>
	      
	      
	       <div class="row" style="height:680px" id="main2a">

	      </div>
      
      
      </div>
       

    </section>
 <%@include file="/WEB-INF/views/include/jsfile.jsp" %>
 <script src="${ctxStaticLTE}/plugins/echarts/echarts/echarts.js"></script>
<script type="text/javascript">
    
    var myChart1;
    var theme = theme || 'macarons'; //默认macarons 
	
    // 路径配置
        require.config({
            paths: {
            	  echarts: '${ctxStaticLTE}/plugins/echarts/echarts'//echarts根目录
            }
        });
        
        // 使用
        require(
            [
			'echarts',
			'echarts/theme/infographic',
			'echarts/chart/line',
			'echarts/chart/bar',
			'echarts/chart/scatter',
			'echarts/chart/k',
			'echarts/chart/pie',
			'echarts/chart/radar',
			'echarts/chart/force',
			'echarts/chart/chord',
			'echarts/chart/map',
			'echarts/chart/gauge',
			'echarts/chart/tree',
			'echarts/chart/funnel'
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
               myChart1 = ec.init(document.getElementById('main1'),theme); 
               var option1 =  {
            		    title : {
            		        text: '2017级新生分院系人数统计',
            		        subtext: '数据来自招生办'
            		    },
            		    tooltip : {
            		    	show: true,
            		        trigger: 'axis',
            		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            		        }
            		    },
            		    toolbox: {
            		        show : true,
            		        feature : {
            		            magicType: {show: true, type: ['line', 'bar']},
            		            restore : {show: true},
            		            saveAsImage : {show: true}
            		        }
            		    },
            		    calculable : true,
            		    xAxis : [
            		        {
            		            type : 'value'
            		        }
            		    ],
            		    yAxis : [
            		        {
            		            type : 'category',
            		            data : [<c:set var="yxsize" value="${fn:length(ksyxcount)}"/>
										<c:forEach items="${ksyxcount}" var="testMap"  varStatus="stat">
										<c:set var="ksinf" value="${ksyxcount[yxsize- stat.index - 1]}"/>'${ksinf.stalabel}'<c:if test="${!stat.last}">,</c:if>
										</c:forEach>
            		                    ]
            		        }
            		    ],
            		    series : [
            		        {
            		            name:'${fns:getDictLabel("nowyear", "nowyear", "")}级新生人数',
            		            type:'bar',
            		            stack: '人数',
            		            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
            		            data:[<c:set var="yxsize" value="${fn:length(ksyxcount)}"/>
            		                  <c:forEach items="${ksyxcount}" var="testMap"  varStatus="stat">
            		                  <c:set var="ksinf" value="${ksyxcount[yxsize- stat.index - 1]}"/>${ksinf.stavalue}<c:if test="${!stat.last}">,</c:if></c:forEach>
            		                  ]
            		        }
            		    ]
            		};
                // 为echarts对象加载数据 
                myChart1.setOption(option1); 
                var option2 = {
                	    backgroundColor: '#1b1b1b',
                	    color: ['gold','aqua','lime'],
                	    title : {
                	        text: '中国农业大学学生就业分布',
                	        subtext:'就业分布',
                	        x:'center',
                	        textStyle : {
                	            color: '#fff'
                	        }
                	    },
                	    tooltip : {
                	        trigger: 'item',
                	        formatter: '{b}'
                	    },
                	    roamController: {
                	        show: true,
                	        x: 'right',
                	        mapTypeControl: {
                	            'china': true
                	        }
                	    },

                	    dataRange: {
                	        min : 0,
                	        max : 100,
                	        show:false,
                	        calculable : true,
                	        color: ['red', 'orange', 'yellow','lime','green'],
                	        textStyle:{
                	            color:'#fff'
                	        }
                	    },
                	    series : [
                	        {
                	            name: '全国',
                	            type: 'map',
                	            roam: true,
                	            hoverable: false,
                	            mapType: 'china',
                	            itemStyle:{
                	                normal:{
                	                    borderColor:'rgba(255,159,237,1)',
                	                    borderWidth:0.5,
                	                    label:{show:true},
                	                    areaStyle:{
                	                        color: '#1b1b1b'
                	                    }
                	                },
                	                emphasis:{label:{show:true}}
                	       
                	          },

                	            data:[],
                	            geoCoord: {
                	                '上海': [121.4648,31.2891],
                	                '东莞': [113.8953,22.901],
                	                '东营': [118.7073,37.5513],
                	                '中山': [113.4229,22.478],
                	                '临汾': [111.4783,36.1615],
                	                '临沂': [118.3118,35.2936],
                	                '丹东': [124.541,40.4242],
                	                '丽水': [119.5642,28.1854],
                	                '乌鲁木齐': [87.9236,43.5883],
                	                '佛山': [112.8955,23.1097],
                	                '保定': [115.0488,39.0948],
                	                '兰州': [103.5901,36.3043],
                	                '包头': [110.3467,41.4899],
                	                '北京': [116.4551,40.2539],
                	                '北海': [109.314,21.6211],
                	                '南京': [118.8062,31.9208],
                	                '南宁': [108.479,23.1152],
                	                '南昌': [116.0046,28.6633],
                	                '南通': [121.1023,32.1625],
                	                '厦门': [118.1689,24.6478],
                	                '台州': [121.1353,28.6688],
                	                '合肥': [117.29,32.0581],
                	                '呼和浩特': [111.4124,40.4901],
                	                '咸阳': [108.4131,34.8706],
                	                '哈尔滨': [127.9688,45.368],
                	                '唐山': [118.4766,39.6826],
                	                '嘉兴': [120.9155,30.6354],
                	                '大同': [113.7854,39.8035],
                	                '大连': [122.2229,39.4409],
                	                '天津': [117.4219,39.4189],
                	                '太原': [112.3352,37.9413],
                	                '威海': [121.9482,37.1393],
                	                '宁波': [121.5967,29.6466],
                	                '宝鸡': [107.1826,34.3433],
                	                '宿迁': [118.5535,33.7775],
                	                '常州': [119.4543,31.5582],
                	                '广州': [113.5107,23.2196],
                	                '廊坊': [116.521,39.0509],
                	                '延安': [109.1052,36.4252],
                	                '张家口': [115.1477,40.8527],
                	                '徐州': [117.5208,34.3268],
                	                '德州': [116.6858,37.2107],
                	                '惠州': [114.6204,23.1647],
                	                '成都': [103.9526,30.7617],
                	                '扬州': [119.4653,32.8162],
                	                '承德': [117.5757,41.4075],
                	                '西藏': [91.1865,30.1465],
                	                '无锡': [120.3442,31.5527],
                	                '日照': [119.2786,35.5023],
                	                '昆明': [102.9199,25.4663],
                	                '杭州': [119.5313,29.8773],
                	                '枣庄': [117.323,34.8926],
                	                '柳州': [109.3799,24.9774],
                	                '株洲': [113.5327,27.0319],
                	                '武汉': [114.3896,30.6628],
                	                '汕头': [117.1692,23.3405],
                	                '江门': [112.6318,22.1484],
                	                '沈阳': [123.1238,42.1216],
                	                '沧州': [116.8286,38.2104],
                	                '河源': [114.917,23.9722],
                	                '泉州': [118.3228,25.1147],
                	                '泰安': [117.0264,36.0516],
                	                '泰州': [120.0586,32.5525],
                	                '济南': [117.1582,36.8701],
                	                '济宁': [116.8286,35.3375],
                	                '海口': [110.3893,19.8516],
                	                '淄博': [118.0371,36.6064],
                	                '淮安': [118.927,33.4039],
                	                '深圳': [114.5435,22.5439],
                	                '清远': [112.9175,24.3292],
                	                '温州': [120.498,27.8119],
                	                '渭南': [109.7864,35.0299],
                	                '湖州': [119.8608,30.7782],
                	                '湘潭': [112.5439,27.7075],
                	                '滨州': [117.8174,37.4963],
                	                '潍坊': [119.0918,36.524],
                	                '烟台': [120.7397,37.5128],
                	                '玉溪': [101.9312,23.8898],
                	                '珠海': [113.7305,22.1155],
                	                '盐城': [120.2234,33.5577],
                	                '盘锦': [121.9482,41.0449],
                	                '石家庄': [114.4995,38.1006],
                	                '福州': [119.4543,25.9222],
                	                '秦皇岛': [119.2126,40.0232],
                	                '绍兴': [120.564,29.7565],
                	                '聊城': [115.9167,36.4032],
                	                '肇庆': [112.1265,23.5822],
                	                '舟山': [122.2559,30.2234],
                	                '苏州': [120.6519,31.3989],
                	                '莱芜': [117.6526,36.2714],
                	                '菏泽': [115.6201,35.2057],
                	                '营口': [122.4316,40.4297],
                	                '葫芦岛': [120.1575,40.578],
                	                '衡水': [115.8838,37.7161],
                	                '衢州': [118.6853,28.8666],
                	                '西宁': [101.4038,36.8207],
                	                '西安': [109.1162,34.2004],
                	                '贵阳': [106.6992,26.7682],
                	                '连云港': [119.1248,34.552],
                	                '邢台': [114.8071,37.2821],
                	                '邯郸': [114.4775,36.535],
                	                '郑州': [113.4668,34.6234],
                	                '鄂尔多斯': [108.9734,39.2487],
                	                '重庆': [107.7539,30.1904],
                	                '金华': [120.0037,29.1028],
                	                '铜川': [109.0393,35.1947],
                	                '银川': [106.3586,38.1775],
                	                '镇江': [119.4763,31.9702],
                	                '长春': [125.8154,44.2584],
                	                '长沙': [113.0823,28.2568],
                	                '长治': [112.8625,36.4746],
                	                '阳泉': [113.4778,38.0951],
                	                '青岛': [120.4651,36.3373],
                	                '韶关': [113.7964,24.7028]
                	            }
                	        },
                	        {
                	            name: '北京 Top10',
                	            type: 'map',
                	            mapType: 'china',
                	            data:[],
                	            markLine : {
                	                smooth:true,
                	                effect : {
                	                    show: true,
                	                    scaleSize: 1,
                	                    period: 30,
                	                    color: '#fff',
                	                    shadowBlur: 10
                	                },
                	                itemStyle : {
                	                    normal: {
                	                        borderWidth:1,
                	                        lineStyle: {
                	                            type: 'solid',
                	                            shadowBlur: 10
                	                        }
                	                    }
                	                },
                	                data : [
                	                    [ {name:'上海',value:95},{name:'北京'}]
                	                   
                	                ]
                	            },
                	            markPoint : {
                	                symbol:'emptyCircle',
                	                symbolSize : function (v){
                	                    return 10 + v/10;
                	                },
                	                effect : {
                	                    show: false,
                	                    shadowBlur : 0
                	                },
                	                itemStyle:{
                	                    normal:{
                	                        label:{show:false}
                	                    },
                	                    emphasis: {
                	                        label:{position:'top'}
                	                    }
                	                },
                	                data : [
                	                    {name:'上海',value:95}
                	                ]
                	            }
                	        },
                	    ]
                	};


                 
                  var citynamearr=new Array("上海","广州","潍坊","重庆","南京"); 
                  var cityvaluearr=new Array("30","40","50","60","70"); 
                  var cityindex=0;
                  var citylength=34;
                 /* setInterval(function (){
                	  
                	  
                	  option2.series[1].markPoint.effect.show=false; 
                	  option2.series[1].markLine.data[0][0].name=citynamearr[cityindex]; 
                	  option2.series[1].markLine.data[0][0].value=cityvaluearr[cityindex]; 
                	  option2.series[1].markPoint.data[0].name=citynamearr[cityindex]; 
                	  option2.series[1].markPoint.data[0].value=cityvaluearr[cityindex]; 
                	  option2.series[1].markPoint.effect.show=true; 
                	  
                	  cityindex=cityindex+1;
                	  
                	  if(cityindex>=citylength)
                		  cityindex=0;
                	  
                	  myChart2.setOption(option2);
                	},2000);
                	*/
                	var tooltip1 = "";
                	setInterval(function (){
                  	  
                  	  for(var i=0;i<citylength;i++)
                  	   option2a.series[0].data[i].selected=false;
                  	   option2a.series[0].data[cityindex].selected=true;
                  	   myChart2.setOption(option2a);
                  	  /*
                  	   myChart2.dispatchAction({
                  		type: 'showTip',
                  		seriesIndex:0 ,//第几条series
                  		dataIndex:cityindex//第几个tooltip
                  		})*/
                  		
                  		myChart2.component.tooltip.showing=true;
                  		myChart2.component.tooltip.showTip({ seriesIndex: 0,dataIndex:cityindex}); 
                  		
                  	 // alert(tooltip1);
                  	 cityindex=cityindex+1;
                  	 if(cityindex>=citylength)
              		   cityindex=0;
                  	
                  	},2000);
                	
                	
                var  option2a = {
                	    title : {
                	        text: '中国农业大学新生生源分布',
                	        subtext: '中国农业大学招生办',
                	        x:'center'
                	    },
                	    tooltip : {
                	        trigger: 'item',
                	        show:'true'
                	        
                	    },
                	    dataRange: {
                	        min: 0,
                	        max: 2500,
                	        x: 'left',
                	        y: 'bottom',
                	        text:['高','低'],           // 文本，默认为数值文本
                	        calculable : true
                	    },
                	    toolbox: {
                	        show: true,
                	        orient : 'vertical',
                	        x: 'right',
                	        y: 'center',
                	        feature : {
                	            mark : {show: true},
                	            dataView : {show: true, readOnly: false},
                	            restore : {show: true},
                	            saveAsImage : {show: true}
                	        }
                	    },
                	    roamController: {
                	        show: true,
                	        x: 'right',
                	        mapTypeControl: {
                	            'china': true
                	        }
                	    },
                	    series : [
                	           {
                	            name: '2017年新生',
                	            type: 'map',
                	            mapType: 'china',
                	            roam: false,
                	            itemStyle:{
                	                normal:{label:{show:true}},
                	                emphasis:{label:{show:true}}
                	            },
                	            data:[
                	                {name: '北京',value: Math.round(Math.random()*1000)},
                	                {name: '天津',value: Math.round(Math.random()*1000)},
                	                {name: '上海',value: Math.round(Math.random()*1000)},
                	                {name: '重庆',value: Math.round(Math.random()*1000)},
                	                {name: '河北',value: Math.round(Math.random()*1000)},
                	                {name: '河南',value: Math.round(Math.random()*1000)},
                	                {name: '云南',value: Math.round(Math.random()*1000)},
                	                {name: '辽宁',value: Math.round(Math.random()*1000)},
                	                {name: '黑龙江',value: Math.round(Math.random()*1000)},
                	                {name: '湖南',value: Math.round(Math.random()*1000)},
                	                {name: '安徽',value: Math.round(Math.random()*1000)},
                	                {name: '山东',value: Math.round(Math.random()*1000)},
                	                {name: '新疆',value: Math.round(Math.random()*1000)},
                	                {name: '江苏',value: Math.round(Math.random()*1000)},
                	                {name: '浙江',value: Math.round(Math.random()*1000)},
                	                {name: '江西',value: Math.round(Math.random()*1000)},
                	                {name: '湖北',value: Math.round(Math.random()*1000)},
                	                {name: '广西',value: Math.round(Math.random()*1000)},
                	                {name: '甘肃',value: Math.round(Math.random()*1000)},
                	                {name: '山西',value: Math.round(Math.random()*1000)},
                	                {name: '内蒙古',value: Math.round(Math.random()*1000)},
                	                {name: '陕西',value: Math.round(Math.random()*1000)},
                	                {name: '吉林',value: Math.round(Math.random()*1000)},
                	                {name: '福建',value: Math.round(Math.random()*1000)},
                	                {name: '贵州',value: Math.round(Math.random()*1000)},
                	                {name: '广东',value: Math.round(Math.random()*1000)},
                	                {name: '青海',value: Math.round(Math.random()*1000)},
                	                {name: '西藏',value: Math.round(Math.random()*1000)},
                	                {name: '四川',value: Math.round(Math.random()*1000)},
                	                {name: '宁夏',value: Math.round(Math.random()*1000)},
                	                {name: '海南',value: Math.round(Math.random()*1000)},
                	                {name: '台湾',value: Math.round(Math.random()*1000)},
                	                {name: '香港',value: Math.round(Math.random()*1000)},
                	                {name: '澳门',value: Math.round(Math.random()*1000)}
                	            ]
                	        }
                	    ]
                	};
                
                var myChart2 = ec.init(document.getElementById('main2a'));
                myChart2.setOption(option2a);
				 window.onresize = function() {
                    myChart1.resize();
         
                };
            }
        );
        
    
    </script>
</body>
</html>
