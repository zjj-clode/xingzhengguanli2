(function ($) {
  var eCalendar = function (options, object,url,path) {
        var adDay = new Date().getDate();
        var adMonth = new Date().getMonth();
        var adYear = new Date().getFullYear();
        var dDay = adDay;
        var dMonth = adMonth;
        var dYear = adYear;
        var instance = object;
        var settings = $.extend({}, $.fn.eCalendar.defaults, options);

        var nextMonth = function () {
            if (dMonth < 11) {
                dMonth++;
            } else {
                dMonth = 0;
                dYear++;
            }
            print();
        };
        var previousMonth = function () {
            if (dMonth > 0) {
                dMonth--;
            } else {
                dMonth = 11;
                dYear--;
            }
            print();
        };

        function print() {
            var dWeekDayOfMonthStart = new Date(dYear, dMonth, 1).getDay();
            var dLastDayOfMonth = new Date(dYear, dMonth + 1, 0).getDate();
            var dLastDayOfPreviousMonth = new Date(dYear, dMonth + 1, 0).getDate() - dWeekDayOfMonthStart + 1;
            var tit = "";
            var cPrevious = "<div class='rl_tit'><a class='rl_prev'><img src='"+path+"/images/q.png' alt=''></a>";
            tit += cPrevious;
            var cMonth = "";
            if((parseInt(dMonth)+1) < 10){
               cMonth = dYear+".0"+(parseInt(dMonth)+1);
            }else{
               cMonth = dYear+"."+(parseInt(dMonth)+1);
            }
            tit += cMonth;
            var cNext = "<a class='rl_next'><img src='"+path+"/images/h.png' alt=''></a></div>";
            tit += cNext;
            $(instance).html("");
            $(instance).append(tit);
            
            $(".rl_prev").on('click', previousMonth);
            $(".rl_next").on('click', nextMonth);
            
            //星期
            var cWeekDay = "<table cellpadding='0' cellspacing='0' class='tab01'></table>";
            $(instance).append(cWeekDay);
            var $cWeekDay = $(instance).find(".tab01");
            var weekTr = "<tr><th>日</th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th>六</th></tr>";
            $cWeekDay.append(weekTr);
            var day = 1;
            var j= 0;
            var dayOfNextMonth = 1;
            var cDayHtml = "<tr>";
            for (var i = 0; i < 35; i++) {
              if (i < dWeekDayOfMonthStart) {
                cDayHtml += "<td><span class='qy'>"+(dLastDayOfPreviousMonth++)+"</span></td>";
              }
              else if (day <= dLastDayOfMonth) {
                if (day == dDay && adMonth == dMonth && adYear == dYear) {
                  cDayHtml += "<td class='calendarTd"+day+"'><span class='dy_dt'>"+(day++)+"</span></td>";
                }else{
                  cDayHtml += "<td class='calendarTd"+day+"'><span class='dy'>"+(day++)+"</span></td>";	
                }
              }
              else {
                cDayHtml += "<td><span class='qy'>"+(dayOfNextMonth++)+"</span></td>";
              }
              if((i+1)%7 == 0){
            	  cDayHtml += "</tr>";
            	  $cWeekDay.append(cDayHtml);
            	  cDayHtml = "<tr>";
              }
              
            }
            $(instance).append($cWeekDay);
            
            if(url){
	            if((parseInt(dMonth)+1) < 10){
	               getCalendarData(url,dYear+"-0"+(parseInt(dMonth)+1),dWeekDayOfMonthStart);
	            }else{
	               getCalendarData(url,dYear+"-"+(parseInt(dMonth)+1),dWeekDayOfMonthStart);
	            }
            }
        }
        
        return print();
    }

    $.fn.eCalendar = function (oInit,url,path) {
        // alert(oInit);
        return this.each(function () {
            return eCalendar(oInit, $(this),url,path);
        });
    };

    // plugin defaults
    // $.fn.eCalendar.defaults = {
    //     months: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
    //     textArrows: {previous: '<', next: '>'},
    //     eventTitle: 'Eventos',
    //     url: '',
    //     events: [

    //     ]
    // };
    function getCalendarData(url,time,startNum){
        $.ajax({
	        //提交数据的类型 POST
	        type:"POST",
	        async:false,
	        //提交的网址
	        url:url+"?time="+time + "&startNum=" + startNum,
	        //提交的数据
	        data:"",
	        success:function(data){
	            if(data !=null && data != ""){
	            	eval(data);
	            }
	        },
	        //调用出错执行的函数
	        error: function(){
	        }        
	    });
    }
}(jQuery));