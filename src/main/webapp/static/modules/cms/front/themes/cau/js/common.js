$(document).ready(function(){

	/*左侧菜单*/
	$(".closeli p").click(function(){
		var $this = $(this);
		var mainmenu = $this.parent("li");
		var submenu = $this.siblings(".submenu");
		$(".submenu").hide();
		submenu.slideDown(200);
		mainmenu.siblings("li.openli").attr("class","closeli");
		mainmenu.attr("class","openli");
	});
	$(".openli p").click(function(){
		var $this = $(this);
		var mainmenu = $this.parent("li");
		var submenu = $this.siblings(".submenu");
		submenu.slideUp(200);
		mainmenu.attr("class","closeli");
	});
	$(".submenu li").click(function(){
		menuActive($(this));
	});
	$(".fnli").click(function(){
		menuActive($(this));
		$(".submenu").hide();
		$(".openli").attr("class","closeli");
	});
	function menuActive(obj){
		$(".sidemenu").find("li").removeClass("active");
		obj.addClass("active");
	};

})
