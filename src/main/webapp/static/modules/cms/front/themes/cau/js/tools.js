(function($){
    //tab切换
    /* --------------------------
        调用方法：
        $('#tabs1').tabSwitch({
            mouseevent: 'click',
            tab: '.tablist',
            tabcont: '.tabcont',
        });
    ----------------------------*/
    $.fn.tabSwitch = function(options){
        var defaults = {
            mouseevent: "mouseenter", //选项mouseenter click
            attribute: "data-href",
            tab: '> ul',
            tabcont: '> div',
            active: 1
        };
        var options = $.extend(defaults, options);
        this.each(function(){
            var $this = $(this);
            $this.find(options.tabcont).hide();
            $this.find(options.tabcont).eq(options.active - 1).show();
            $this.find(options.tab).find("li").eq(options.active - 1).addClass("active");
            var eventState;
            options.mouseevent == "mouseenter" ? eventState=true : eventState=false;
            var delayTime;
            /*------------------------------------------------------
            eval的使用场合是什么呢？有时候我们预先不知道要执行什么语句，
            只有当条件和参数给时才知道执行什么语句，这时候eval就派上用场了。
            --------------------------------------------------------*/
            var fn = eval(function() {
                var $tab = $(this);
                var currentTab = $tab.find("a").attr(options.attribute);
                if(eventState){
                    delayTime = setTimeout(function(){
                        $this.find(options.tab).find("li").removeClass("active");
                        $tab.addClass("active");
                        $this.find(options.tabcont).hide();
                        $this.find(currentTab).fadeIn(200);
                    },150);
                }
                else{
                    $this.find(options.tab).find("li").removeClass("active");
                    $tab.addClass("active");
                    $this.find(options.tabcont).hide();
                    $this.find(currentTab).fadeIn(200);
                }
            });
            if(eventState){
                $this.find(options.tab).find("li").mouseleave(function(){
                    clearTimeout(delayTime);
                })
            };
            //eval组合语句
            var init = eval("$this.find(options.tab).find('li')." + options.mouseevent + "(fn)");
            init;
        });
    };

})(jQuery);


$(function(){
    $(".hasub").each(function(){
        $(this).hover(function() {
            $(".subnav").stop(false,true);
            $(this).find(".subnav").slideDown(50);
            $(this).find(".green1").addClass("on");
        },function(){
            $(this).find(".subnav").hide();
            $(this).find(".green1").removeClass("on");
        });
    });
})