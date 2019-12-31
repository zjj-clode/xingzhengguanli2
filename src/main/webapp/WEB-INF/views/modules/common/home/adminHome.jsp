<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta name="decorator" content="lte"/>
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>云智</title>
</head>
<!--
BODY TAG OPTIONS:
=================
Apply one or more of the following classes to get the
desired effect
|---------------------------------------------------------|
| SKINS         | skin-blue                               |
|               | skin-black                              |
|               | skin-purple                             |
|               | skin-yellow                             |
|               | skin-red                                |
|               | skin-green                              |
|---------------------------------------------------------|
|LAYOUT OPTIONS | fixed                                   |
|               | layout-boxed                            |
|               | layout-top-nav                          |
|               | sidebar-collapse                        |
|               | sidebar-mini                            |
|---------------------------------------------------------|
-->
<body>
<!-- Content Header (Page header) -->
  <section class="content">
      <div class="row">
       
        <div class="col-lg-3 col-md-6 col-xs-12">
          <a class="info-box clear" >
            <span class="info-box-icon bg-purple"><i class="fa fa-briefcase animated flash"></i></span>

            <div class="info-box-content text-center">
              <span class="info-box-number animation-pullDown text-purple">${aCount}</span>
              <span class="info-box-text animated fadeInDown text-primary">今日资讯发布</span>
            </div>
            <!-- /.info-box-content -->
          </a>
        </div>
        <div class="col-lg-3 col-md-6 col-xs-12">
          <a class="info-box clear">
            <span class="info-box-icon bg-aqua"><i class="fa fa-vcard animated flash"></i></span>

            <div class="info-box-content text-center">
              <span class="info-box-number animation-pullDown text-aqua">${saViewMum }</span>
              <span class="info-box-text animated fadeInDown text-primary">今日访问量</span>
            </div>
            <!-- /.info-box-content -->
          </a>
          <!-- /.info-box -->
        </div>
        <div class="col-lg-3 col-md-6 col-xs-12">
          <a class="info-box clear">
            <span class="info-box-icon bg-green"><i class="fa fa-users animated flash"></i></span>

            <div class="info-box-content text-center">
              <span class="info-box-number animation-pullDown text-green">${saNum }</span>
              <span class="info-box-text animated fadeInDown text-primary">今日登录次数</span>
            </div>
            <!-- /.info-box-content -->
          </a>
          <!-- /.info-box -->
        </div>
      </div>
      <div class="row">
        <div class="col-md-6 col-sm-12">
          <div class="box box-default box-solid">
            <div class="box-header with-border">
              <h3 class="box-title text-primary">系统访问统计</h3>

              <div class="box-tools pull-right btn-group">
                  <button type="button" id="bt71"  class="btn btn-sm btn-default on">7天</button>
                  <button type="button" id="bt101" class="btn btn-sm btn-default">10天</button>
                  <button type="button" id="bt301" class="btn btn-sm btn-default">30天</button>
              </div>
            </div>
            <div class="box-body chart-responsive">
              <div class="chart" id="revenue-chart" style="height: 200px;"></div>
            </div>
            <!-- /.box-body -->
          </div>
        </div>
        <div class="col-md-6 col-sm-12">
          <div class="box box-default box-solid">
            <div class="box-header with-border">
              <h3 class="box-title text-primary">系统访问统计</h3>

              <div class="box-tools pull-right btn-group">
                  <button type="button" id="bt7"  class="btn btn-sm btn-default on">7天</button>
                  <button type="button" id="bt10" class="btn btn-sm btn-default">10天</button>
                  <button type="button" id="bt30" class="btn btn-sm btn-default">30天</button>
              </div>
            </div>
            <div class="box-body chart-responsive">
              <div class="chart" id="revenue-chart2" style="height: 200px;"></div>
            </div>
            <!-- /.box-body -->
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6 col-sm-12">
          <div class="box box-default box-solid">
            <div class="box-header with-border">
              <h3 class="box-title text-primary">最新注册考生</h3>

              <div class="box-tools pull-right">
                <a href="${ctx}/sys/user" class="btn btn-box-tool"><i class="fa fa-angle-right"></i> MORE</a>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <table class="table no-margin table-bgodd animated fadeInDown">
                  <thead>
                  <tr>
                    <th >姓名</th>
                    <th>省份</th>
                    <th>科类名称</th>
                 	<th>注册时间</th>
                  </tr>
                  </thead>
                  <tbody>
                 <c:forEach items="${uList}" var="uList">
                 	<tr>
		             	<td>${uList.name}</td>
		             	<td>
		             		${fns:getDictLabel(uList.province, 'ssmc', '')}
		             	</td>
		             	<td>
		             		${uList.kelei}
		             	</td>
		          		<td>
		             		<fmt:formatDate value="${uList.createDate}" pattern="yy-MM-dd HH:mm:ss"/> 
		             	</td>
                 	</tr>
                 </c:forEach>
                  </tbody>
                </table>
            </div>
            <%-- <div class="box-body">
              <ul class="todo-list bg-odd animated fadeInDown">
              <c:set var="LabelColors" value="${fn:split('label-danger,label-purple,label-warning,label-blue,label-success',',') }"></c:set>
             
              </ul>
            </div> --%>
            <!-- /.box-body -->
          </div>
          
          <!-- /.box -->
        </div>
        <div class="col-md-6 col-sm-12">
          <div class="box box-default box-solid">
            <div class="box-header with-border">
              <h3 class="box-title text-primary">最新操作记录</h3>

              <div class="box-tools pull-right">
                <a href="${ctx}/common/businessLog" class="btn btn-box-tool"><i class="fa fa-angle-right"></i> MORE</a>
              </div>
            </div>
            <div class="box-body chart-responsive" style="padding:30px">
              <ul class="timeline homeTime animated fadeInDown">
                <!-- timeline item -->
                <c:forEach items="${businessLogList}" var="businessLog">
	                <li>
	                	<c:choose>
	                		 <c:when test="${businessLog.type ==1 }">
	                		 	<i class="fa fa-user bg-purple"></i>
	                		 </c:when>
	                		 <c:when test="${businessLog.type ==2 }">
			                  	<i class="fa fa-plus bg-aqua"></i>
			                  </c:when>
			                  <c:when test="${businessLog.type ==3 }">
			                  	<i class="fa fa-remove bg-green"></i>
			                  </c:when>
			                  <c:when test="${businessLog.type ==4 }">
			                  	<i class="fa fa-legal bg-yellow"></i>
			                  </c:when>
			                  <c:when test="${businessLog.type ==5 }">
			                  	<i class="fa fa-mail-forward bg-green"></i>
			                  </c:when>
			                  <c:when test="${businessLog.type ==6 }"><!-- 修改 -->
			                  	<i class="fa fa-edit bg-green"></i>
			                  </c:when>
			                  <c:otherwise>
			                  	<i class="fa fa-mail-forward bg-aqua"></i>
			                  </c:otherwise>
	                	</c:choose>
	                  <div class="timeline-item">
	                    <span class="time">
			                    <c:if test="${fns:pastDays(businessLog.createDate) == 0}">
				                     <c:if test="${fns:pastHour(businessLog.createDate) == 0}">
				                     	<c:if test="${fns:pastMinutes(businessLog.createDate) == 0}">
				                     		刚刚
				                     	</c:if>
				                     	<c:if test="${fns:pastMinutes(businessLog.createDate) > 0}">
				                        	${fns:pastMinutes(businessLog.createDate)} mins ago
				                     	</c:if>
				                     </c:if>
				                     <c:if test="${fns:pastHour(businessLog.createDate) > 0}">
				                        ${fns:pastHour(businessLog.createDate)} hours ago
				                     </c:if>
			                  </c:if> 
			                  <c:if test="${fns:pastDays(businessLog.createDate) > 0}">
			                        ${fns:pastDays(businessLog.createDate)} days ago
			                  </c:if>
	                    </span>
	                    <h3 class="timeline-header no-border"> ${businessLog.title }</h3>
	                  </div>
	                </li>
                	<!-- END timeline item -->
                </c:forEach>
                <!-- timeline item -->
              </ul>
            </div>
            <!-- /.box-body -->
          </div>
        </div>
      </div>
    </section>


    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script src="${ctxStaticLTE}/plugins/Highcharts/code/js/highcharts.js"></script>
<script type="text/javascript">

function showline1(timeline,data1,data2)
{
	  Highcharts.chart('revenue-chart', {
	        chart: {
	            type: 'line'
	        },
	        title: {
	            text: ''
	        },
	        subtitle: {
	            text: ''
	        },
	        xAxis: {
	            categories: timeline
	        },
	        yAxis: {
	            title: {
	                text: ''
	            }
	        },
	        plotOptions: {
	            line: {
	                dataLabels: {
	                    enabled: false
	                },
	                enableMouseTracking: true
	            }
	        },
	        series: [
	          {
	            name: '登录次数',
	       		color : "#58d1ff",
	            data: data1
	        },
	        {
	            name: '登录人数',
	       		color : "#6f92f0",
	            data: data2
	        }
	        
	        ]
	    });
}

function showline2(timeline,data1,data2)
{
	  Highcharts.chart('revenue-chart2', {
	        chart: {
	            type: 'line'
	        },
	        title: {
	            text: ''
	        },
	        subtitle: {
	            text: ''
	        },
	        xAxis: {
	            categories: timeline
	        },
	        yAxis: {
	            title: {
	                text: ''
	            }
	        },
	        plotOptions: {
	            line: {
	                dataLabels: {
	                    enabled: false
	                },
	                enableMouseTracking: true
	            }
	        },
	        series: [
	          {
	            name: '登录次数',
	       		color : "#58d1ff",
	            data: data1
	        },
	        {
	            name: '登录人数',
	       color : "#6f92f0",
	            data: data2
	        }
	        
	        ]
	    });
}
  $(function () {
	  //页面加载完毕，调取hchars图表
	  //系统访问图表数据
	  var timeline=[];
	  var data1=[];
	  var data2=[];
	  <c:forEach items="${fnc:getSysAccessCountList()}" varStatus="status" var="item"> 
		  timeline[${status.index}]="${item.login_year_month_day}".substr(5);
		  data1[${status.index}]=${item.login_count};
		  data2[${status.index}]=${item.login_name_count};
	  </c:forEach>
	  console.log(timeline);
	  console.log(data1);
	  console.log(data2);
	  showline1(timeline.slice(-7),data1.slice(-7),data2.slice(-7));
	  //学生访问图表数据
	  var stutimeline=[];
	  var studata=[];
	  var studata2=[];
	  <c:forEach items="${fnc:getSysAccessCountList()}" varStatus="status" var="item"> 
	  	stutimeline[${status.index}]="${item.login_year_month_day}".substr(5);
	  	studata[${status.index}]=${item.login_count};
	  	studata2[${status.index}]=${item.login_name_count};
  	  </c:forEach>
	  showline2(stutimeline.slice(-7),studata.slice(-7),studata2.slice(-7));
	  
	 $("#bt7").click(function(){
		showline2(stutimeline.slice(-7),studata.slice(-7),studata2.slice(-7));
	 });
	$("#bt10").click(function(){
		showline2(stutimeline.slice(-10),studata.slice(-10),studata2.slice(-10));
	  });
	 $("#bt30").click(function(){ 
		 showline2(stutimeline.slice(-30),studata.slice(-30),studata2.slice(-30));
	 });    
	 $("#bt71").click(function(){
	  showline1(timeline.slice(-7),data1.slice(-7),data2.slice(-7));
	 });
	$("#bt101").click(function(){
	  showline1(timeline.slice(-10),data1.slice(-10),data2.slice(-10));
	 });
	$("#bt301").click(function(){  
	  showline1(timeline.slice(-30),data1.slice(-30),data2.slice(-30));
	});
	//鼠标移入增加交互效果
    $('.info-box').bind('mouseover',function(){
      var $this = $(this);
      $this.find('.fa').removeClass('flash animated');
      $this.find('.info-box-number,.info-box-text').removeClass('animated fadeInDown');
      setTimeout(function () {
          $this.find('.fa').addClass('flash animated');
          $this.find('.info-box-number,.info-box-text').addClass('animated fadeInDown')
      },200)
    })
});
</script>
</body>
</html>
