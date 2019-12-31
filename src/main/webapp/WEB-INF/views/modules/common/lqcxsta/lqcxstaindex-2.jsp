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
	        <div class="col-lg-6 col-md-12"  style="height:500px" id="main2">
	      
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
	        <div class="col-lg-6 col-md-12" style="height:500px" id="main3">
	      
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
	        <div class="col-lg-6 col-md-12" style="height:500px" id="main4">
	      
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
	        <div class="col-lg-6 col-md-12" style="height:500px" id="main5">
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
	        <div class="col-lg-6 col-md-12"  style="height:500px" id="main6">
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
	        <div class="col-lg-6 col-md-12"  style="height:500px"  id="main7">
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
	        <div class="col-lg-6 col-md-12" style="height:500px"  id="main8">
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
	        <div class="col-lg-6 col-md-12" style="height:500px"  id="main9">
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
	        <div class="col-lg-6 col-md-12" style="height:500px"  id="main10">
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
	                <div class="col-lg-6 col-md-12" style="height:500px"  id="main11">
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

          <div class="row">
		   <div class="col-lg-6 col-md-12"  style="height:680px" id="main13"></div>
		   <div class="col-lg-6 col-md-12"  style="height:680px" id="main14"></div>
	      </div>
	      
	       <div class="row">
		   <div class="col-lg-6 col-md-12"  style="height:680px" id="main15"></div>
		   <div class="col-lg-6 col-md-12"  style="height:680px" id="main16"></div>
	      </div>
	      
	       <div class="row">
		   <div class="col-lg-6 col-md-12"  style="height:680px" id="main17"></div>
		   <div class="col-lg-6 col-md-12"  style="height:680px" id="main18"></div>
	      </div>
	      
	       <div class="row">
		   <div class="col-lg-6 col-md-12"  style="height:680px" id="main19"></div>
		   <div class="col-lg-6 col-md-12"  style="height:680px" id="main20"></div>
	      </div>
	      
	      <div class="row" style="height:680px" id="main2a">

	      </div>
      
      
      </div>
       

    </section>
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script src="${ctxStaticLTE}/plugins/echarts3/echarts/echarts.js"></script>
<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/map/js/china.js"></script>
<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/dark.js"></script>
<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/infographic.js"></script>
<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/macarons.js"></script>
<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/roma.js"></script>
<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/shine.js"></script>
<script type="text/javascript" src="${ctxStaticLTE}/plugins/echarts3/echarts/theme/vintage.js"></script>



<script type="text/javascript">
var theme = 'infographic'; //默认macarons 
var dom = document.getElementById("main2a");
var myChart2a = echarts.init(dom,theme);
var app = {};
option2a = null;
var geoCoordMap = {
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
    '拉萨': [91.1865,30.1465],
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
};

var BJData = [
    [{name:'北京'}, {name:'上海',value:95}],
    [{name:'北京'}, {name:'广州',value:90}],
    [{name:'北京'}, {name:'大连',value:80}],
    [{name:'北京'}, {name:'南宁',value:70}],
    [{name:'北京'}, {name:'南昌',value:60}],
    [{name:'北京'}, {name:'拉萨',value:50}],
    [{name:'北京'}, {name:'长春',value:40}],
    [{name:'北京'}, {name:'包头',value:30}],
    [{name:'北京'}, {name:'重庆',value:20}],
    [{name:'北京'}, {name:'常州',value:10}]
];

var SHData = [
    [{name:'上海'},{name:'包头',value:95}],
    [{name:'上海'},{name:'昆明',value:90}],
    [{name:'上海'},{name:'广州',value:80}],
    [{name:'上海'},{name:'郑州',value:70}],
    [{name:'上海'},{name:'长春',value:60}],
    [{name:'上海'},{name:'重庆',value:50}],
    [{name:'上海'},{name:'长沙',value:40}],
    [{name:'上海'},{name:'北京',value:30}],
    [{name:'上海'},{name:'丹东',value:20}],
    [{name:'上海'},{name:'大连',value:10}]
];

var GZData = [
    [{name:'广州'},{name:'福州',value:95}],
    [{name:'广州'},{name:'太原',value:90}],
    [{name:'广州'},{name:'长春',value:80}],
    [{name:'广州'},{name:'重庆',value:70}],
    [{name:'广州'},{name:'西安',value:60}],
    [{name:'广州'},{name:'成都',value:50}],
    [{name:'广州'},{name:'常州',value:40}],
    [{name:'广州'},{name:'北京',value:30}],
    [{name:'广州'},{name:'北海',value:20}],
    [{name:'广州'},{name:'海口',value:10}]
];

var planePath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';

var convertData = function (data) {
    var res = [];
    for (var i = 0; i < data.length; i++) {
        var dataItem = data[i];
        var fromCoord = geoCoordMap[dataItem[0].name];
        var toCoord = geoCoordMap[dataItem[1].name];
        if (fromCoord && toCoord) {
            res.push({
                fromName: dataItem[0].name,
                toName: dataItem[1].name,
                coords: [fromCoord, toCoord]
            });
        }
    }
    return res;
};

var color = ['#a6c84c', '#ffa022', '#46bee9'];
var series = [];
[['北京', BJData], ['上海', SHData], ['广州', GZData]].forEach(function (item, i) {
    series.push({
        name: item[0] + ' Top10',
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
                color: color[i],
                width: 0,
                curveness: 0.2
            }
        },
        data: convertData(item[1])
    },
    {
        name: item[0] + ' Top10',
        type: 'lines',
        zlevel: 2,
        symbol: ['none', 'arrow'],
        symbolSize: 10,
        effect: {
            show: true,
            period: 6,
            trailLength: 0,
            symbol: planePath,
            symbolSize: 15
        },
        lineStyle: {
            normal: {
                color: color[i],
                width: 1,
                opacity: 0.6,
                curveness: 0.2
            }
        },
        data: convertData(item[1])
    },
    {
        name: item[0] + ' Top10',
        type: 'effectScatter',
        coordinateSystem: 'geo',
        zlevel: 2,
        rippleEffect: {
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
            return val[2] / 8;
        },
        itemStyle: {
            normal: {
                color: color[i]
            }
        },
        data: item[1].map(function (dataItem) {
            return {
                name: dataItem[1].name,
                value: geoCoordMap[dataItem[1].name].concat([dataItem[1].value])
            };
        })
    });
});

option2a = {
    backgroundColor: '#404a59',
    title : {
        text: '模拟迁徙',
        subtext: '数据纯属虚构',
        left: 'center',
        textStyle : {
            color: '#fff'
        }
    },
    tooltip : {
        trigger: 'item'
    },
    legend: {
        orient: 'vertical',
        top: 'bottom',
        left: 'right',
        data:['北京 Top10', '上海 Top10', '广州 Top10'],
        textStyle: {
            color: '#fff'
        },
        selectedMode: 'single'
    },
    geo: {
        map: 'china',
        label: {
            emphasis: {
                show: false
            }
        },
        roam: true,
        itemStyle: {
            normal: {
                areaColor: '#323c48',
                borderColor: '#404a59'
            },
            emphasis: {
                areaColor: '#2a333d'
            }
        }
    },
    series: series
};;
if (option2a && typeof option2a === "object") {
    myChart2a.setOption(option2a, true);
}
     

function randomData() {
    return Math.round(Math.random()*1000);
}

var dom = document.getElementById("main1");
var myChart1 = echarts.init(dom,"shine");
var option1 = {
    title: {
        text: 'iphone销量',
        subtext: '纯属虚构',
        left: 'center'
    },
    tooltip: {
        trigger: 'item'
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data:['iphone3','iphone4','iphone5']
    },
    visualMap: {
        min: 0,
        max: 2500,
        left: 'left',
        top: 'bottom',
        text: ['高','低'],           // 文本，默认为数值文本
        calculable: true
    },
    toolbox: {
        show: true,
        orient: 'vertical',
        left: 'right',
        top: 'center',
        feature: {
            dataView: {readOnly: false},
            restore: {},
            saveAsImage: {}
        }
    },
    series: [
        {
            name: 'iphone3',
            type: 'map',
            mapType: 'china',
            roam: false,
            label: {
                normal: {
                    show: true
                },
                emphasis: {
                    show: true
                }
            },
            data:[
                {name: '北京',value: randomData() },
                {name: '天津',value: randomData() },
                {name: '上海',value: randomData() },
                {name: '重庆',value: randomData() },
                {name: '河北',value: randomData() },
                {name: '河南',value: randomData() },
                {name: '云南',value: randomData() },
                {name: '辽宁',value: randomData() },
                {name: '黑龙江',value: randomData() },
                {name: '湖南',value: randomData() },
                {name: '安徽',value: randomData() },
                {name: '山东',value: randomData() },
                {name: '新疆',value: randomData() },
                {name: '江苏',value: randomData() },
                {name: '浙江',value: randomData() },
                {name: '江西',value: randomData() },
                {name: '湖北',value: randomData() },
                {name: '广西',value: randomData() },
                {name: '甘肃',value: randomData() },
                {name: '山西',value: randomData() },
                {name: '内蒙古',value: randomData() },
                {name: '陕西',value: randomData() },
                {name: '吉林',value: randomData() },
                {name: '福建',value: randomData() },
                {name: '贵州',value: randomData() },
                {name: '广东',value: randomData() },
                {name: '青海',value: randomData() },
                {name: '西藏',value: randomData() },
                {name: '四川',value: randomData() },
                {name: '宁夏',value: randomData() },
                {name: '海南',value: randomData() },
                {name: '台湾',value: randomData() },
                {name: '香港',value: randomData() },
                {name: '澳门',value: randomData() }
            ]
        },
        {
            name: 'iphone4',
            type: 'map',
            mapType: 'china',
            label: {
                normal: {
                    show: true
                },
                emphasis: {
                    show: true
                }
            },
            data:[
                {name: '北京',value: randomData() },
                {name: '天津',value: randomData() },
                {name: '上海',value: randomData() },
                {name: '重庆',value: randomData() },
                {name: '河北',value: randomData() },
                {name: '安徽',value: randomData() },
                {name: '新疆',value: randomData() },
                {name: '浙江',value: randomData() },
                {name: '江西',value: randomData() },
                {name: '山西',value: randomData() },
                {name: '内蒙古',value: randomData() },
                {name: '吉林',value: randomData() },
                {name: '福建',value: randomData() },
                {name: '广东',value: randomData() },
                {name: '西藏',value: randomData() },
                {name: '四川',value: randomData() },
                {name: '宁夏',value: randomData() },
                {name: '香港',value: randomData() },
                {name: '澳门',value: randomData() }
            ]
        },
        {
            name: 'iphone5',
            type: 'map',
            mapType: 'china',
            label: {
                normal: {
                    show: true
                },
                emphasis: {
                    show: true
                }
            },
            data:[
                {name: '北京',value: randomData() },
                {name: '天津',value: randomData() },
                {name: '上海',value: randomData() },
                {name: '广东',value: randomData() },
                {name: '台湾',value: randomData() },
                {name: '香港',value: randomData() },
                {name: '澳门',value: randomData() }
            ]
        }
    ]
};
if (option1 && typeof option1 === "object") {
    myChart1.setOption(option1, true);
}



var option2 = {
    title: {
        text: '世界人口总量',
        subtext: '数据来自网络'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    legend: {
        data: ['2011年', '2012年']
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
        data: ['巴西','印尼','美国','印度','中国','世界人口(万)']
    },
    series: [
        {
            name: '2011年',
            type: 'bar',
            data: [18203, 23489, 29034, 104970, 131744, 630230]
        },
        {
            name: '2012年',
            type: 'bar',
            data: [19325, 23438, 31000, 121594, 134141, 681807]
        }
    ]
};
var dom2 = document.getElementById("main2");
var myChart2 = echarts.init(dom2,"roma");
if (option2 && typeof option2 === "object") {
    myChart2.setOption(option2, true);
}

var option3 = {
	    tooltip: {
	        trigger: 'item',
	        formatter: "{a} <br/>{b}: {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        x: 'left',
	        data:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
	    },
	    series: [
	        {
	            name:'访问来源',
	            type:'pie',
	            radius: ['50%', '70%'],
	            avoidLabelOverlap: false,
	            label: {
	                normal: {
	                    show: false,
	                    position: 'center'
	                },
	                emphasis: {
	                    show: true,
	                    textStyle: {
	                        fontSize: '30',
	                        fontWeight: 'bold'
	                    }
	                }
	            },
	            labelLine: {
	                normal: {
	                    show: false
	                }
	            },
	            data:[
	                {value:335, name:'直接访问'},
	                {value:310, name:'邮件营销'},
	                {value:234, name:'联盟广告'},
	                {value:135, name:'视频广告'},
	                {value:1548, name:'搜索引擎'}
	            ]
	        }
	    ]
	};
var dom3 = document.getElementById("main3");
var myChart3 = echarts.init(dom3,"macarons");
if (option3 && typeof option3 === "object") {
    myChart3.setOption(option3, true);
}


var option4 = {
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
	
var dom4 = document.getElementById("main4");
var myChart4 = echarts.init(dom4,"infographic");
if (option4 && typeof option4 === "object") {
    myChart4.setOption(option4, true);
}

var option5 = {
	    title : {
	        text: '某站点用户访问来源',
	        subtext: '纯属虚构',
	        x:'center'
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        left: 'left',
	        data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
	    },
	    series : [
	        {
	            name: '访问来源',
	            type: 'pie',
	            radius : '55%',
	            center: ['50%', '60%'],
	            data:[
	                {value:335, name:'直接访问'},
	                {value:310, name:'邮件营销'},
	                {value:234, name:'联盟广告'},
	                {value:135, name:'视频广告'},
	                {value:1548, name:'搜索引擎'}
	            ],
	            itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            }
	        }
	    ]
	};

var dom5 = document.getElementById("main5");
var myChart5 = echarts.init(dom5);
if (option5 && typeof option5 === "object") {
    myChart5.setOption(option5, true);
}

var option6 = {
	    title : {
	        text: '南丁格尔玫瑰图',
	        subtext: '纯属虚构',
	        x:'center'
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        x : 'center',
	        y : 'bottom',
	        data:['rose1','rose2','rose3','rose4','rose5','rose6','rose7','rose8']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {
	                show: true,
	                type: ['pie', 'funnel']
	            },
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    series : [
	        {
	            name:'半径模式',
	            type:'pie',
	            radius : [20, 110],
	            center : ['50%', '50%'],
	            roseType : 'radius',
	            label: {
	                normal: {
	                    show: false
	                },
	                emphasis: {
	                    show: true
	                }
	            },
	            lableLine: {
	                normal: {
	                    show: false
	                },
	                emphasis: {
	                    show: true
	                }
	            },
	            data:[
	                {value:10, name:'rose1'},
	                {value:5, name:'rose2'},
	                {value:15, name:'rose3'},
	                {value:25, name:'rose4'},
	                {value:20, name:'rose5'},
	                {value:35, name:'rose6'},
	                {value:30, name:'rose7'},
	                {value:40, name:'rose8'}
	            ]
	        }
	    ]
	};

var dom6 = document.getElementById("main6");
var myChart6 = echarts.init(dom6);
if (option6 && typeof option6 === "object") {
    myChart6.setOption(option6, true);
}

var option7 = {
	    tooltip : {
	        formatter: "{a} <br/>{b} : {c}%"
	    },
	    toolbox: {
	        feature: {
	            restore: {},
	            saveAsImage: {}
	        }
	    },
	    series: [
	        {
	            name: '业务指标',
	            type: 'gauge',
	            detail: {formatter:'{value}%'},
	            data: [{value: 50, name: '完成率'}]
	        }
	    ]
	};


	
	var dom7 = document.getElementById("main7");
	var myChart7 = echarts.init(dom7);
	if (option7 && typeof option7 === "object") {
	    myChart7.setOption(option7, true);
	}
	
	setInterval(function () {
	    option7.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
	    myChart7.setOption(option7, true);
	},2000);

	
	var base = +new Date(1968, 9, 3);
	var oneDay = 24 * 3600 * 1000;
	var date = [];

	var data = [Math.random() * 300];

	for (var i = 1; i < 20000; i++) {
	    var now = new Date(base += oneDay);
	    date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'));
	    data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]));
	}

	var option8 = {
	    tooltip: {
	        trigger: 'axis',
	        position: function (pt) {
	            return [pt[0], '10%'];
	        }
	    },
	    title: {
	        left: 'center',
	        text: '大数据量面积图',
	    },
	    toolbox: {
	        feature: {
	            dataZoom: {
	                yAxisIndex: 'none'
	            },
	            restore: {},
	            saveAsImage: {}
	        }
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: date
	    },
	    yAxis: {
	        type: 'value',
	        boundaryGap: [0, '100%']
	    },
	    dataZoom: [{
	        type: 'inside',
	        start: 0,
	        end: 10
	    }, {
	        start: 0,
	        end: 10,
	        handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
	        handleSize: '80%',
	        handleStyle: {
	            color: '#fff',
	            shadowBlur: 3,
	            shadowColor: 'rgba(0, 0, 0, 0.6)',
	            shadowOffsetX: 2,
	            shadowOffsetY: 2
	        }
	    }],
	    series: [
	        {
	            name:'模拟数据',
	            type:'line',
	            smooth:true,
	            symbol: 'none',
	            sampling: 'average',
	            itemStyle: {
	                normal: {
	                    color: 'rgb(255, 70, 131)'
	                }
	            },
	            areaStyle: {
	                normal: {
	                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
	                        offset: 0,
	                        color: 'rgb(255, 158, 68)'
	                    }, {
	                        offset: 1,
	                        color: 'rgb(255, 70, 131)'
	                    }])
	                }
	            },
	            data: data
	        }
	    ]
	};
	var dom8 = document.getElementById("main8");
	var myChart8 = echarts.init(dom8);
	if (option8 && typeof option8 === "object") {
	    myChart8.setOption(option8, true);
	}
	
	var locations = [{
	    name: '上海',
	    coord: [121.472644, 31.231706]
	}, {
	    name: '北京',
	    coord: [116.405285, 39.904989]
	}, {
	    name: '广东',
	    coord: [113.280637, 23.839463714285714]
	}];
	var option9 = {
	    tooltip: {
	        trigger: 'item',
	        formatter: '{b}'
	    },
	    series: [
	        {
	            name: '中国',
	            type: 'map',
	            mapType: 'china',
	            selectedMode : 'multiple',
	            label: {
	                normal: {
	                    show: true
	                },
	                emphasis: {
	                    show: true
	                }
	            }
	        }
	    ]
	};

	var dom9 = document.getElementById("main9");
	var myChart9 = echarts.init(dom9,theme);
	if (option9 && typeof option9 === "object") {
	    myChart9.setOption(option9, true);
	}
	
	var currentLoc = 0;
	setInterval(function () {
	    myChart9.setOption({
	        series: [{
	            center: locations[currentLoc].coord,
	            zoom: 4,
	            data:[
	                {name: locations[currentLoc].name, selected: true}
	            ],
	            animationDurationUpdate: 1000,
	            animationEasingUpdate: 'cubicInOut'
	        }]
	    });
	    currentLoc = (currentLoc + 1) % locations.length;
	}, 2000);
	
	var option10={
			title : {
	            text: '柱形图',
	            subtext: 'ECharts'
	        },
	        legend: {
	            data:['直接','邮件','联盟','搜索']
	        },
	        toolbox: {
	            show : true,
	            feature : {
	                magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	                dataView : {show: true, readOnly: false},
	                restore : {show: true}
	            }
	        },
	        tooltip : {
	            trigger: 'axis',
	            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	            }
	        },
	        yAxis : [
	            {
	                type : 'value'
	            }
	        ],
	        xAxis : [
	            {
	                type : 'category',
	                data : ['周一','周二','周三','周四','周五','周六','周日']
	            }
	        ],
	        series : [
	            {
	                name:'直接',
	                type:'bar',
	                data:[80, 132, 101, 134, 90, 180, 200]
	            },
	            {
	                name:'邮件',
	                type:'bar',
	                data:[120, 102, 151, 164, 230, 230, 280]
	            },
	            {
	                name:'联盟',
	                type:'bar',
	                data:[180, 182, 191, 264, 290, 350, 410]
	            },
	            {
	                name:'搜索',
	                type:'bar',
	                data:[210, 232, 301, 254, 390, 430, 510]
	            }
	        ]
			
	};
	var dom10 = document.getElementById("main10");
	var myChart10 = echarts.init(dom10,"macarons");
	if (option10 && typeof option10 === "object") {
	    myChart10.setOption(option10, true);
	}
	
	var option13={
	  title : {
	            text: '雷达图',
	            subtext: 'ECharts'
	        },
	        tooltip : {
	            trigger: 'axis'
	        },
	        legend: {
	            orient : 'vertical',
	            x : 'right',
	            data:['预算分配','实际开销']
	        },
	        toolbox: {
	            show : true,
	            y:'bottom',
	            feature : {
	                restore : {show: true},
	                saveAsImage : {show: true}
	            }
	        },
	        polar : [
	           {
	               radius: '100%',
	               indicator : [
	                   { text: '销售', max: 6000},
	                   { text: '管理', max: 16000},
	                   { text: '信息技术', max: 30000},
	                   { text: '客服', max: 38000},
	                   { text: '研发', max: 52000},
	                   { text: '市场', max: 25000}
	                ]
	            }
	        ],
	        series : [
	            {
	                name: '预算 vs 开销',
	                type: 'radar',
	                data : [
	                    {
	                        value : [4300, 10000, 28000, 35000, 50000, 19000],
	                        name : '预算分配'
	                    },
	                     {
	                        value : [5000, 14000, 28000, 31000, 42000, 21000],
	                        name : '实际开销'
	                    }
	                ]
	            }
	        ]
	 
	  };
	
	var dom13 = document.getElementById("main13");
	var myChart13 = echarts.init(dom13);
	if (option13 && typeof option13 === "object") {
	    myChart13.setOption(option13, true);
	}
	
	var option14={ title : {
         text: '仪表盘',
         subtext: 'ECharts'
     },
     tooltip : {
         formatter: "{a} <br/>{b} : {c}%"
     },
     toolbox: {
         show : true,
         y: 'bottom',
         feature : {
             restore : {show: true},
             saveAsImage : {show: true}
         }
     },
     series : [
         {
             name:'业务指标',
             type:'gauge',
             detail : {formatter:'{value}%'},
             data:[{value: 16.8, name: '完成率'}]
         }
     ]
 };
	
	var dom14 = document.getElementById("main14");
	var myChart14 = echarts.init(dom14);
	if (option14 && typeof option14 === "object") {
	    myChart14.setOption(option14, true);
	}
	
	var option15={	
			title : {
        text: '漏斗图',
        subtext: 'ECharts'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c}%"
    },
    toolbox: {
        show : true,
        y: 'bottom',
        feature : {
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    legend: {
        orient : 'vertical',
        x : 'left',
        y : 'bottom',
        data : ['展现','点击','访问','咨询','订单']
    },
    series : [
        {
            name:'漏斗图',
            type:'funnel',
            data:[
                {value:60, name:'访问'},
                {value:40, name:'咨询'},
                {value:20, name:'订单'},
                {value:80, name:'点击'},
                {value:100, name:'展现'}
            ]
        }
    ]
 };
	
	var dom15 = document.getElementById("main15");
	var myChart15 = echarts.init(dom15);
	if (option15 && typeof option15 === "object") {
	    myChart15.setOption(option15, true);
	}
	
	var option17 = {
		    title : {
		        text: '某站点用户访问来源',
		        subtext: '纯属虚构',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        orient : 'vertical',
		        x : 'left',
		        data:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
		    },
		    calculable : true,
		    series : [
		        {
		            name:'访问来源',
		            type:'pie',
		            radius : '55%',
		            center: ['50%', 225],
		            data:[
		                {value:335, name:'直接访问'},
		                {value:310, name:'邮件营销'},
		                {value:234, name:'联盟广告'},
		                {value:135, name:'视频广告'},
		                {value:1548, name:'搜索引擎'}
		            ]
		        }
		    ]
		};

		var option18 = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {
		            type: 'shadow'
		        }
		    },
		    legend: {
		        data:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
		    },
		    toolbox: {
		        show : true,
		        orient : 'vertical',
		        y : 'center',
		        feature : {
		            mark : {show: true},
		            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            data : ['周一','周二','周三','周四','周五','周六','周日']
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            splitArea : {show : true}
		        }
		    ],
		    grid: {
		        x2:40
		    },
		    series : [
		        {
		            name:'直接访问',
		            type:'bar',
		            stack: '总量',
		            data:[320, 332, 301, 334, 390, 330, 320]
		        },
		        {
		            name:'邮件营销',
		            type:'bar',
		            stack: '总量',
		            data:[120, 132, 101, 134, 90, 230, 210]
		        },
		        {
		            name:'联盟广告',
		            type:'bar',
		            stack: '总量',
		            data:[220, 182, 191, 234, 290, 330, 310]
		        },
		        {
		            name:'视频广告',
		            type:'bar',
		            stack: '总量',
		            data:[150, 232, 201, 154, 190, 330, 410]
		        },
		        {
		            name:'搜索引擎',
		            type:'bar',
		            stack: '总量',
		            data:[820, 932, 901, 934, 1290, 1330, 1320]
		        }
		    ]
		};	
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
	
			myChart17.connect(myChart18);
		    myChart18.connect(myChart17);

		    var labelTop = {
		    	    normal : {
		    	        label : {
		    	            show : true,
		    	            position : 'center',
		    	            formatter : '{b}',
		    	            textStyle: {
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
		    	            formatter : function (params){
		    	                return 100 - params.value + '%'
		    	            },
		    	            textStyle: {
		    	                baseline : 'top'
		    	            }
		    	        }
		    	    },
		    	}
		    	var labelBottom = {
		    	    normal : {
		    	        color: '#ccc',
		    	        label : {
		    	            show : true,
		    	            position : 'center'
		    	        },
		    	        labelLine : {
		    	            show : false
		    	        }
		    	    },
		    	    emphasis: {
		    	        color: 'rgba(0,0,0,0)'
		    	    }
		    	};
		    	var radius = [40, 55];
		    	var option19 = {
		    	    legend: {
		    	        x : 'center',
		    	        y : 'top',
		    	        data:[
		    	            'GoogleMaps','Facebook','Youtube','Google+','Weixin',
		    	            'Twitter', 'Skype', 'Messenger', 'Whatsapp', 'Instagram'
		    	        ]
		    	    },
		    	    title : {
		    	        text: 'The App World',
		    	        subtext: 'from global web index',
		    	        x: 'left'
		    	    },
		    	    toolbox: {
		    	        show : true,
		    	        feature : {
		    	            dataView : {show: true, readOnly: false},
		    	            magicType : {
		    	                show: true, 
		    	                type: ['pie', 'funnel'],
		    	                option: {
		    	                    funnel: {
		    	                        width: '20%',
		    	                        height: '30%',
		    	                        itemStyle : {
		    	                            normal : {
		    	                                label : {
		    	                                    formatter : function (params){
		    	                                        return 'other\n' + params.value + '%\n'
		    	                                    },
		    	                                    textStyle: {
		    	                                        baseline : 'middle'
		    	                                    }
		    	                                }
		    	                            },
		    	                        } 
		    	                    }
		    	                }
		    	            },
		    	            restore : {show: true},
		    	            saveAsImage : {show: true}
		    	        }
		    	    },
		    	    series : [
		    	        {
		    	            type : 'pie',
		    	            center : ['10%', '30%'],
		    	            radius : radius,
		    	            x: '0%', // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:46, itemStyle : labelBottom},
		    	                {name:'GoogleMaps', value:54,itemStyle : labelTop}
		    	            ]
		    	        },
		    	        {
		    	            type : 'pie',
		    	            center : ['30%', '30%'],
		    	            radius : radius,
		    	            x:'20%', // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:56, itemStyle : labelBottom},
		    	                {name:'Facebook', value:44,itemStyle : labelTop}
		    	            ]
		    	        },
		    	        {
		    	            type : 'pie',
		    	            center : ['50%', '30%'],
		    	            radius : radius,
		    	            x:'40%', // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:65, itemStyle : labelBottom},
		    	                {name:'Youtube', value:35,itemStyle : labelTop}
		    	            ]
		    	        },
		    	        {
		    	            type : 'pie',
		    	            center : ['70%', '30%'],
		    	            radius : radius,
		    	            x:'60%', // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:70, itemStyle : labelBottom},
		    	                {name:'Google+', value:30,itemStyle : labelTop}
		    	            ]
		    	        },
		    	        {
		    	            type : 'pie',
		    	            center : ['90%', '30%'],
		    	            radius : radius,
		    	            x:'80%', // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:73, itemStyle : labelBottom},
		    	                {name:'Weixin', value:27,itemStyle : labelTop}
		    	            ]
		    	        },
		    	        {
		    	            type : 'pie',
		    	            center : ['10%', '70%'],
		    	            radius : radius,
		    	            y: '55%',   // for funnel
		    	            x: '0%',    // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:78, itemStyle : labelBottom},
		    	                {name:'Twitter', value:22,itemStyle : labelTop}
		    	            ]
		    	        },
		    	        {
		    	            type : 'pie',
		    	            center : ['30%', '70%'],
		    	            radius : radius,
		    	            y: '55%',   // for funnel
		    	            x:'20%',    // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:78, itemStyle : labelBottom},
		    	                {name:'Skype', value:22,itemStyle : labelTop}
		    	            ]
		    	        },
		    	        {
		    	            type : 'pie',
		    	            center : ['50%', '70%'],
		    	            radius : radius,
		    	            y: '55%',   // for funnel
		    	            x:'40%', // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:78, itemStyle : labelBottom},
		    	                {name:'Messenger', value:22,itemStyle : labelTop}
		    	            ]
		    	        },
		    	        {
		    	            type : 'pie',
		    	            center : ['70%', '70%'],
		    	            radius : radius,
		    	            y: '55%',   // for funnel
		    	            x:'60%', // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:83, itemStyle : labelBottom},
		    	                {name:'Whatsapp', value:17,itemStyle : labelTop}
		    	            ]
		    	        },
		    	        {
		    	            type : 'pie',
		    	            center : ['90%', '70%'],
		    	            radius : radius,
		    	            y: '55%',   // for funnel
		    	            x:'80%', // for funnel
		    	            itemStyle : labelFromatter,
		    	            data : [
		    	                {name:'other', value:89, itemStyle : labelBottom},
		    	                {name:'Instagram', value:11,itemStyle : labelTop}
		    	            ]
		    	        }
		    	    ]
		    	};             
		    	alert("main19");
		    	var dom19 = document.getElementById("main19");
				var myChart19 = echarts.init(dom19);
				if (option19 && typeof option19 === "object") {
				    myChart19.setOption(option19, true);
				} 

					                 		
	
    </script>
</body>
</html>
