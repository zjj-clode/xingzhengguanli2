$(document).ready(function(){
	var allCheckbox = $("form#batchDeleteForm table thead th input:checkbox");
	var checkboxs = $("form#batchDeleteForm table tbody td input:checkbox");
	allCheckbox.click(function(){
		if($(this).prop("checked")==true){
			checkboxs.prop("checked",true);
		}else{
			checkboxs.removeAttr("checked");
		}
	});
	checkboxs.click(function(){
		if(allCheckbox.prop("checked")==true){
			if($(this).prop("checked")!=true){
				allCheckbox.removeAttr("checked");
			}
		}else{
			var bool = true;
			for(var i=0;i<checkboxs.length;i++){
				if($(checkboxs.get(i)).prop("checked")!=true){
					bool = false;
					break;
				}
			}
			if(bool){
				allCheckbox.prop("checked",true);
			}  
		}
	});
	
})


function isAnyChecked(checkboxs){
	for(var i=0;i<checkboxs.length;i++){
		if($(checkboxs.get(i)).attr("checked")!="checked"){
			return true;
		}
	}
	return false;
}
function serialize(checkboxs){
	var str = "";
	$.each( checkboxs, function(i, checkbox){
		if($(checkbox).attr("checked")=="checked"){
			str+="ids="+$(checkbox).val()+"&";
		}
	});
	return str.substring(0, str.length-1);
}