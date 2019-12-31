$(function () {
    $(".menu li").hover(function(){
        var $current = $(this);
        if($current.find(".subNavi").length!=0){
            $(".subNavi").stop(true, true).hide();
            $current.find(".subNavi").fadeIn(200);
        }
    },function(){
        if($(this).find(".subNavi").css("display")=="block"){
            $(this).find(".subNavi").hide();
        }
    })
});