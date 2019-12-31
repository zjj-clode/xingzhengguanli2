(function($){
    //tab切换
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
            var fn = eval(function() {
                var $tab = $(this);
                var currentTab = $tab.attr(options.attribute);
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
                // var currentTop = $this.find(currentTab).offset().top;
             //    $('html,body').animate({
                //     scrollTop: currentTop
                // },
                // 500);
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