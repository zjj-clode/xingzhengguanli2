(function ($) {

    var eCalendar = function (options, object,tips) {
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
            var cNext = "<a href='javascript:void(0)' class='sprite-arrow_right sprite pa'>&nbsp;</a>";
            var cPrevious = "<a href='javascript:void(0)' class='sprite-arrow_left sprite pa'>&nbsp;</a>";
            var cMonth = "<p class='month green1 f24 pl20'>"+dYear+".<span class='f36'>"+(parseInt(dMonth)+1)+"</span></p>";
            $(instance).html("");
            $(instance).append(cPrevious);
            $(instance).append(cNext);
            $(instance).append(cMonth);
            $(".sprite-arrow_left").on('click', previousMonth);
            $(".sprite-arrow_right").on('click', nextMonth);
            //星期
            var cWeekDay = "<ul class='dateHeader bc clear f14'>"+
            "<li class='fl red2'>日</li>" +
            "<li class='fl'>一</li>" +
            "<li class='fl'>二</li>" +
            "<li class='fl'>三</li>" +
            "<li class='fl'>四</li>" +
            "<li class='fl'>五</li>" +
            "<li class='fl red2'>六</li></ul>";
            $(instance).append(cWeekDay);
            var cDays = "<ul class='dateCont bc clear f14'></ul>";
            $(instance).append(cDays);
            var day = 1;
            var dayOfNextMonth = 1;
            var $cDays = $(".dateCont");
            var cDayHtml = "";
            for (var i = 0; i < 42; i++) {
                if (i < dWeekDayOfMonthStart) {
                    cDayHtml += "<li class='fl gray2'><span class='ib'>"+(dLastDayOfPreviousMonth++)+"</span></li>";
                }
                else if (day <= dLastDayOfMonth) {
                    if (day == dDay && adMonth == dMonth && adYear == dYear) {
                        var con = "";
                        for(var j=0;j<tips.length;j++){
                            var obj = tips[j];
                            if(obj.index == day){
                               con = obj.tip;
                            }
                        }
                        if(con){
                            cDayHtml += "<li class='fl thismth today'><span class='ib'>"+(day++)+"</span>"+con+"</li>";
                        }else{
                            cDayHtml += "<li class='fl thismth today'><span class='ib'>"+(day++)+"</span></li>";
                        }
                        
                    }
                    else{
                        var con = "";
                        for(var j=0;j<tips.length;j++){
                            var obj = tips[j];
                            if(obj.index == day){
                               con = obj.tip;
                            }
                        }
                        if(con){
                            cDayHtml += "<li class='fl thismth on'><span class='ib'>"+(day++)+"</span>"+con+"</li>";
                        }else{
                            cDayHtml += "<li class='fl thismth'><span class='ib'>"+(day++)+"</span></li>";
                        }
                    }
                }
                else {
                    cDayHtml += "<li class='fl gray2'><span class='ib'>"+(dayOfNextMonth++)+"</span></li>";
                }
            }
            $cDays.html(cDayHtml);
        }
        return print();
    }

    $.fn.eCalendar = function (oInit,tips) {
        // alert(oInit);
        return this.each(function () {
            return eCalendar(oInit, $(this),tips);
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

}(jQuery));