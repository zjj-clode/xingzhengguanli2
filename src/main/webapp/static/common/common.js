function loadmodalpage(modalurl) {
	$("#modal_edit").modal({
		backdrop : true,
		keyboard : true
	});
	$("#modal_edit").modal("show");
	postForm(modalurl, "modal_edit");
}
function postForm(url, divid, formid) {
	if (url.indexOf("?") == -1) {
		pageurl = url + "?randid=" + (new Date()).valueOf();
	} else {
		pageurl = url + "&randid=" + (new Date()).valueOf();
	}
	var dataval = "";
	if (formid) {
		dataval = $('#' + formid).serialize();
	}
	$("#" + divid).addClass("loading");
	$("#" + divid).html("<img src='../../images/main/loading.gif' ><p class='mt10'>正在加载中</p>");
	$.ajaxSetup({
		cache : false
	});
	$.ajax({
		type : "POST",
		data : dataval,
		url : pageurl,
		success : function(content) {
			$("#" + divid).removeClass("loading");
			$("#" + divid).html(content);
		},
		error : function(data) {},
		complete : function(XMLHttpRequest, textStatus) {},
		//调用出错执行的函数
		error : function() {}
	});
}


