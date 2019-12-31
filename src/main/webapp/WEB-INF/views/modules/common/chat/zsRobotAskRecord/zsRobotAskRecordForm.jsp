<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="lteblank"/>
</head>
<body>
	<section class="content">
      <div class="row">
         <div class="col-lg-3 col-md-4 col-xs-12">
          <a class="info-box clear" >
            <span class="info-box-icon bg-purple"><i class="fa fa-briefcase animated flash"></i></span>

            <div class="info-box-content text-center">
              <span class="info-box-number animation-pullDown text-purple">${fn:length(PCList)}</span>
              <span class="info-box-text animated fadeInDown text-primary">PC咨询总数</span>
            </div>
            <!-- /.info-box-content -->
          </a>
        </div>
         <div class="col-lg-3 col-md-4 col-xs-12">
          <a class="info-box clear" >
            <span class="info-box-icon bg-purple"><i class="fa fa-briefcase animated flash"></i></span>

            <div class="info-box-content text-center">
              <span class="info-box-number animation-pullDown text-purple">${fn:length(mobileList)}</span>
              <span class="info-box-text animated fadeInDown text-primary">手机咨询总数</span>
            </div>
            <!-- /.info-box-content -->
          </a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-4">
	        <div class="box box-default box-solid">
	            <div class="box-header with-border">
	              <h3 class="box-title text-primary">一月内未匹配到答案的咨询文本</h3>
	            </div>
	            <!-- /.box-header -->
	            <div class="box-body">
	              <table class="table no-margin table-bgodd animated fadeInDown">
	                  <thead>
	                  <tr>
	                    <th>文本</th>
	                    <th>咨询次数</th>
	                  </tr>
	                  </thead>
	                  <tbody>
	                      <c:if test="${empty countMapList2}"><tr><td colspan="2" align="center">无数据</td></tr></c:if>
	                      <c:if test="${not empty  countMapList2}">
			                 <c:forEach items="${countMapList}" var="item">
			                    <tr>
			                        <td>${item.text}</td>
			                        <td>${item.count}</td>
			                    </tr>
			                 </c:forEach>
		                  </c:if>
	                  </tbody>
	                </table>
	            </div>
	            <!-- /.box-body -->
	          </div>
          </div>
          <div class="col-md-4">
	          <div class="box box-default box-solid">
	            <div class="box-header with-border">
	              <h3 class="box-title text-primary">一月内未匹配到专业的咨询文本</h3>
	            </div>
	            <!-- /.box-header -->
	            <div class="box-body">
	              <table class="table no-margin table-bgodd animated fadeInDown">
	                  <thead>
		                  <tr>
		                    <th>文本</th>
		                    <th>咨询次数</th>
		                  </tr>
	                  </thead>
	                  <tbody>
	                      <c:if test="${empty countMapList2}"><tr><td colspan="2" align="center">无数据</td></tr></c:if>
	                      <c:if test="${not empty  countMapList2}">
			                 <c:forEach items="${countMapList2}" var="item">
			                    <tr>
			                        <td>${item.text}</td>
			                        <td>${item.count}</td>
			                    </tr>
			                 </c:forEach>
	                      </c:if>
	                  </tbody>
	                </table>
	            </div>
	            <!-- /.box-body -->
	          </div>
          </div>
    </div>
     
   </section>
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script>
</script>
</body>
</html>