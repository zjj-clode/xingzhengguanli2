
//异步加载到指定的div
function postFormWithValidator(url,divid,formid,flag){
	  $("#"+formid).validate({
		  ignore : "",
		  errorPlacement: function(error, element) {
			  if(element.parent().is(".input-group")){
				    error.html("此项不能为空");
				}
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-group") || element.parent().is(".date")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
	  });
	  var isvali =  $("#"+formid).valid();
	  if(isvali){
		  var pageurl;
		  if(url.indexOf("?")==-1){
		     pageurl=url+"?randid="+(new Date()).valueOf();
		  }else{
		     pageurl=url+"&randid="+(new Date()).valueOf();
		  }
		  var dataval = "";
		  if(formid){
		     dataval = $('#'+formid).serialize();
		     $.ajaxSetup ({ cache: false });
			  $.ajax({
					type:"POST",
					data: dataval,
					url: pageurl,
					success: function(content){
						if(flag == 0){
							$("#"+divid).html(content);
						}else if (flag == 1){
							$("#"+divid).prepend(content);
						}
						nav(dataval);
						progress(dataval);
					},
					error: function(data){
			       },
			       complete: function(XMLHttpRequest, textStatus){
			       },
			          //调用出错执行的函数
			       error: function(){ 
			       }        
			 });
		  }
	 }
}
function progress(dataval){
	// 更新进度条
	  $.ajax({
			type:"POST",
			data: dataval,
			url: ctx+"/common/resumeinfo/progress",
			success: function(content){
				$("#progressSmall").html(content);
			},
			error: function(data){
	       },
	       complete: function(XMLHttpRequest, textStatus){
	       },
	          //调用出错执行的函数
	       error: function(){ 
	       }        
	 });
}
function nav(dataval){
	$.ajax({
		type:"POST",
		data: dataval,
		url: ctx+"/common/resumeinfo/nav",
		success: function(content){
			$("#navDiv").html(content);
		},
		error: function(data){
       },
       complete: function(XMLHttpRequest, textStatus){
       },
          //调用出错执行的函数
       error: function(){ 
       }        
 });
}
