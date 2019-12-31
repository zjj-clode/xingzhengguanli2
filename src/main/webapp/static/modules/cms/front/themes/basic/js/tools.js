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

    //全屏跟随导航
    /* --------------------------
        调用方法：
        $(".sideMenu").followNav({
            scrollSpeed: 300
        });
    ----------------------------*/
    $.fn.followNav = function(options){
        var defaults = {
            scrollSpeed: 500,
            currentClass: 'current',
            scrollThreshold: 0.5,
            startHeight: 100
        };
        var options = $.extend(defaults, options);
        this.each(function(){
            var $this = $(this);
            //定义对象，存放各个区域到达屏幕顶部的距离
            var sections = {};
            var didScroll = true;
            function getHash($link){
                return $link.attr('href').split('#')[1];
            }
            //根据滚动位置判断当前区域id
            function getSection(windowPos) {
                var returnValue = null;
                //区域滚动到屏幕的该高度时，成为当前
                var Threshold_height = Math.round($(window).height() * options.scrollThreshold);
                for(var section in sections) {
                    if((sections[section] - Threshold_height) < windowPos) {
                        returnValue = section;
                    }
                }
                return returnValue;
            };
            //切换当前导航样式
            function adjustNav(navli){
                $this.find("li").removeClass(options.currentClass);
                navli.addClass(options.currentClass);
            }
            function scrollTo(target) {
                var offset = $(target).offset().top;
                didScroll = false;
                $('html, body').animate({
                    scrollTop: offset
                },options.scrollSpeed,function(){
                    didScroll = true;
                });
            };
            //将各个区域距离文档顶部的高度存入sections对象
            $this.find("a").each(function() {
                linkHref = getHash($(this));
                $target = $("#"+linkHref);
                if($target.length) {
                    topPos = $target.offset().top;
                    sections[linkHref] = Math.round(topPos);
                }
            });
            //屏幕滚动，导航切换当前
            function scrollChange(){
                var window_top=$(window).scrollTop();
                window_top > options.startHeight ? $this.fadeIn(100) : $this.fadeOut(100)
                if(window_top>options.startHeight){
                    $this.fadeIn(100);
                }
                var position = getSection(window_top);
                if(didScroll){
                    if(position !== null) {
                        $parent_li = $this.find('a[href$="#' + position + '"]').parent();
                        if(!$parent_li.hasClass(options.currentClass)) {
                            adjustNav($parent_li);
                        }
                    }
                }
            };
            $(window).scroll(function(){
                scrollChange();
            });

            //点击锚地平滑滚动到对应区域
            $this.find("a").click(function(event){
                var self = $(this);
                var $link = $(event.currentTarget);
                var $parent = $link.parent();
                var newLoc = '#' + getHash($link);
                if(!$parent.hasClass(options.currentClass)) {
                    adjustNav($parent);
                    scrollTo(newLoc);
                }
                event.preventDefault();
            });
        });
    };


    //内容展开收起
    /* --------------------------
        调用方法：
        $(".sideMenu").fold({
            scrollSpeed: 300
        });
    ----------------------------*/
    $.fn.fold = function(options){
        var defaults = {
            closeClass: 'close',
            openClass: 'open',
            defaultState: 'close', //值为open || close
            btnElm: '.ctrlBtn',
            areaElm: '.ctrlArea',
            speed: 200
        };
        var options = $.extend(defaults, options);
        this.each(function(){
            var $this = $(this);
            var btnElm = $(options.btnElm);
            var areaElm = $(options.areaElm);
            function open(){
                btnElm.addClass(options.openClass);
                areaElm.show();
            }
            function close(){
                btnElm.addClass(options.closeClass);
                areaElm.hide();
            }
            options.defaultState=='open' ? open() : close();
            $this.find(options.btnElm).click(function(){
                console.log(1);
                var thisBtn = $(this);
                if(thisBtn.hasClass(options.openClass)){
                    thisBtn.removeClass(options.openClass).addClass(options.closeClass);
                    thisBtn.siblings(options.areaElm).slideUp(options.speed);
                }
                else{
                    thisBtn.removeClass(options.closeClass).addClass(options.openClass);
                    thisBtn.siblings(options.areaElm).slideDown(options.speed);
                }
            })
        });
    };


})(jQuery);