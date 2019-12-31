<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<!-- <meta name="decorator" content="lte"/> -->
	<%@include file="/WEB-INF/views/include/ltehead.jsp" %>
</head>
<body  style="min-width: 980px;">
    <section class="content-header">
      <h1>
       学生统计
        <small>${staname}</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${ctx }/home"><i class="fa fa-dashboard"></i> 首页</a></li>
        <li><a href="#">统计列表</a></li>
        <li class="active">信息列表</li>
      </ol>
    </section>
	<form:form id="searchForm" modelAttribute="staSql" action="${ctx}/common/staSql/commonsta" method="post" class="form-inline form-filter" role="form">
		<form:hidden path="id"/>
		<div class="form-group"><label>活动：</label>
			<form:select path="activity.id" class="form-control select2">
                   <form:option value="" label="--请选择--"/>
                   <form:options items="${activityList}" itemValue="id" itemLabel="proname"  htmlEscape="false"/>
             </form:select>
		</div>
		<button type="button" id="btnSearch" class="btn btn-default btn-sm" title="搜索"><i class="fa fa-search"></i></button>
	</form:form>
	 <!-- Main content -->
    <section class="content">
      <!-- button -->
      <div class="row tableTopBtn">
        <div class="col-xs-12">
          <button type="button" class="btn btn-success pull-right" id="btnExport" data-toggle="modal"><i class="fa fa-download" aria-hidden="true"></i>导出</button>
        </div>
      </div>
      <div class="row">
          <div class="col-xs-12 simpleTable">
               <table id="dg" class="ui celled table_semantic">
               		<thead>
						 <c:if test="${headlevel ne 1}">
						  <c:forEach var="item" items="${headinf}">
						    <tr>
						      <c:forEach var="tditem" items="${item}">
						       <th <c:if test="${tditem.rowspan>1}">rowspan='${tditem.rowspan}'</c:if> <c:if test="${tditem.colspan>1}">colspan="${tditem.colspan}"</c:if> >${tditem.tdvalue}</th>
						     </c:forEach>
						   </tr>
						  </c:forEach>
						</c:if>
						<c:if test="${headlevel eq 1}">
						  <tr><c:forEach var="item" items="${headinf}"><th>${item}</th></c:forEach></tr>
						</c:if>
					</thead>
					<tbody>
						<c:if test="${isrowspan}">
							<c:forEach var="item" items="${commonsta}" varStatus="rowv">
							    <tr>
							    <c:forEach var="colname" items="${colinf}" varStatus="colv">
							    	
							     <c:if test="${colmer[colv.index]}">
							     	
							      <c:if test="${colrowspan[colv.index][rowv.index]>0}">
							        <td style="border-left: 1px solid rgba(34,36,38,0.1);border-right: 1px solid rgba(34,36,38,0.1);" rowspan="${colrowspan[colv.index][rowv.index]}"> ${item[colname]}</td>
							      </c:if>
						         </c:if>
						           <c:if test="${!colmer[colv.index]}">
							        <td > ${item[colname]}</td>
						         </c:if>
						       </c:forEach>
							   </tr>
							</c:forEach>
						</c:if>	
						<c:if test="${!isrowspan}">
								<c:forEach var="item" items="${commonsta}" varStatus="rowv">
							    <tr>
							    <c:forEach var="colname" items="${colinf}" varStatus="colv">
							        <td>${item[colname]}</td>
						       </c:forEach>
							   </tr>
							</c:forEach>
						</c:if>	
					</tbody>
					<%-- <thead>
					 <c:if test="${headlevel ne 1}">
					  <c:forEach var="item" items="${headinf}">
					    <tr>
					      <c:forEach var="tditem" items="${item}">
					       <th <c:if test="${tditem.rowspan>1}">rowspan='${tditem.rowspan}'</c:if> <c:if test="${tditem.colspan>1}">colspan="${tditem.colspan}"</c:if> >${tditem.tdvalue}</th>
					     </c:forEach>
					   </tr>
					  </c:forEach>
					</c:if>
					<c:if test="${headlevel eq 1}">
					  <tr><c:forEach var="item" items="${headinf}"><th>${item}</th></c:forEach></tr>
					</c:if>	
					<tbody>
						<c:if test="${isrowspan}">
								<c:forEach var="item" items="${commonsta}" varStatus="rowv">
							    <tr>
							    <c:forEach var="colname" items="${colinf}" varStatus="colv">
							     <c:if test="${colmer[colv.index]}">
							      <c:if test="${rowspan[rowv.index]>0}">
							        <td rowspan="${rowspan[rowv.index]}">${item[colname]}</td>
							      </c:if>
						         </c:if>
						           <c:if test="${!colmer[colv.index]}">
							        <td >${item[colname]}</td>
						         </c:if>
						       </c:forEach>
							   </tr>
							</c:forEach>
						</c:if>	
						<c:if test="${!isrowspan}">
							<c:forEach var="item" items="${commonsta}" varStatus="rowv">
						    <tr>
						    <c:forEach var="colname" items="${colinf}" varStatus="colv">
						        <td>${item[colname]}</td>
					       </c:forEach>
						   </tr>
						</c:forEach>
					</c:if>				
				</tbody> --%>
	
			</table>
    	</div>
    </div>
   </section>
    <!-- /.content -->
<%@include file="/WEB-INF/views/include/jsfile.jsp" %>
<script type="text/javascript">
	$(document).ready(function() {
	    // 导出数据弹框
	    var dcModalCont = '<p class="confirmTxt"><i class="fa fa-question-circle text-yellow"></i>确认要导出${staname}统计数据吗？</p>';
	    
	     $("#btnExport").bind('click',function(){
			top.$.jBox.confirm(dcModalCont,"操作确认",function(v,h,f){
				if(v=="ok"){
				   $("#pageSize").val(-1);
				   $("#searchForm").attr("action","${ctx}/common/staSql/exportCommonStaFile");
				   $("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		});
	
	     $("#btnSearch").bind('click',function(){

				   $("#searchForm").attr("action","${ctx}/common/staSql/commonsta");
				   $("#searchForm").submit();
		});	
		
		
		$("#dg1").DataTable({
		      "ordering": false,
		      "scrollY":        300,
		      "scrollX":        true,
		      "scrollCollapse": true,
		      "info": false,
		      "bFilter": false, 
		      "paging":         false,
		      "fixedColumns":   {
		          "leftColumns": 1
		      }
		});        
	});
</script>
</body>
</html>