(function ($) {
    $.extend({
        "message": function (type,txt) {
        	var ico,alertType;
        	if(type=="success"){
        		ico = "fa-check";
        		alertType = "alert-success";
        	}
        	else if(type=="warning"){
        		ico = "fa-warning";
        		alertType = "alert-warning";
        	}
        	else if(type=="info"){
        		ico = "fa-info";
        		alertType = "alert-info";
        	}
        	var msgHtml = '<div class="alert '+alertType+' alert-dismissible" id="msgBox" style="display:none;position:fixed;width:400px;top:60px;left:50%;margin-left:-200px;text-align:center;font-size:16px;"><i class="icon fa '+ico+'"></i> '+txt+'</div>';
        	$('body').append(msgHtml);
        	$('#msgBox').fadeIn(200);
        	setTimeout(function(){
        		$('#msgBox').slideUp(200);
        		setTimeout(function(){
        			$('#msgBox').remove();
        		},300);
        	},2000);
        }
    });
})(window.jQuery);