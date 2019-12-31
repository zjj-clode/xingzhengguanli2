/**
 * 日程相关操作
 * 
 * @author roy
 * @version 2017-3-20
 */
$(function(){
	listFcTypes();
	initFcWidth();
	//initDatePicker();
	//initFullCalendar();
	//initLeftCalHeight();
});

/**
 * 请求的地址
 */
var optUrl = {
	list:ctx+"/calendar/list",  //jeesite/calendar/list
	create:ctx+"/calendar/save",  //jeesite/calendar/save
	update:ctx+"/calendar/update", //jeesite/calendar/update
	del:ctx+"/calendar/delete",  //jeesite/calendar/delete
	types:ctx+"/calendar/types"
}

var currentEvent;
var agendaTypes;

/**
 * 初始化fullCalendar的宽度
 * 根据内容部分的宽度，减去日历部分的宽度和边框
 */
function initFcWidth(){
	var fcWidth = $(".content").width() - $(".custom_cal_sm").width()-21;
	$(".custom_cal_fc").width(fcWidth);
}

/**
 * 初始化日历控件部分的高度，保持和fullCalendar一致
 * 由于边框，所以减去2
 */
function initLeftCalHeight(){
	$(".custom_cal_sm").addClass("border").height($(".fc-view-container").height()-2);
}


/**
 * 创建事件所使用的json，日期根据点击具体日期来确定
 */
var createEvent = {
	id:0,
	title:"新建事件...",
	allDay:true
}


function sameDay(d1, d2) {
	if (!d1) {
		return false;
	}
	if (!d2) {
		return false;
	}
	if (d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate()) {
		return true;
	}
	return false;
}

/**
 * 初始化日历控件
 */
function initDatePicker(){
	var firthDay;
	var count = 0;
	var monthEvents=[];
	$('#datepicker').datepicker({
		language:"zh-CN",
		weekStart:0,
		//todayHighlight:true,
		minViewMode: 0,
	    maxViewMode: 0,
	    beforeShowDay: function( date ) {
	    	//从第一天往后查42天，日期不变不会查询
	        if (count == 0 && !sameDay(firthDay, date)) {
	          firthDay = date;
	          var start = moment(date).format("YYYY-MM-DD  HH:mm:ss");
	          var end = moment(date).add(43, 'days').format("YYYY-MM-DD HH:mm:ss");
	    		$.ajax({
	    			url:optUrl.list,
	    			async:false,
	    			data:{
	    				start:start,
	    				end:end
	    			}
	    		}).done(function ( data ) {
	    			monthEvents = data;
	    		});
	        };
	        
	        count++;
	        if (count == 42) {
	          count = 0;
	        };
            if( dayHasEvent(monthEvents, date) ) {
               return {classes: 'fcEvent'};  
            }  
        }
	});
	
	$('#datepicker').datepicker()
    .on("changeDate", function(e) {
    	$("#calendar").fullCalendar('gotoDate', e.format('yyyy-mm-dd')); 
    });
}

function dayHasEvent(monthEvents, date){
	var hasEvent = false;
	for(var i = 0; i < monthEvents.length; i++) {
		if(monthEvents[i].start.indexOf(moment(date).format("YYYY-MM-DD")) != -1){
			return true;
		}
	}
	return hasEvent;
}

/**
 * 初始化FullCalendar
 */
function initFullCalendar(){
	$('#calendar').fullCalendar({
		customButtons: {
	        create: {
	            text: '创建',
	            click: function() {
	            	showCreateModel(moment());
	            }
	        }
	    },
        header: {
            left: 'prev,today,next create',
            center: 'title',
            right: 'month,agendaWeek,agendaDay,listMonth'
        },
        locale: "zh_cn",
        defaultDate: moment().format("YYYY-MM-DD"),
        allDayText:"全天",
        aspectRatio: 1.5,
        editable: false,
        navLinks: true, // can click day/week names to navigate views
        eventLimit: false, // allow "more" link when too many events
        selectable: false,
        selectHelper: true,
        today: ["今天"],
        // fixedWeekCount: false,//是否一直显示6周
        // firstDay: 1, //第一天为周一
        businessHours: true, // display business hours
        buttonText: {
            today: '今天',
            month: '月',
            week: '周',
            day: '日',
            list:'详情',
        },
        viewRender:viewRender,
        dayClick: dayClick,
        events: optUrl.list,
        eventClick: eventClick,
        eventRender: eventRender,
        eventDataTransform:eventDataTransform,
        eventOrder:eventOrder,
        noEventsMessage:"暂无日程",
        loading:function(isLoading, view){
        },
        windowResize:function(){
        	initFcWidth();
        	initLeftCalHeight();
        }
    });
}

function eventOrder(a, b){
	if(a.id > b.id){
		return 1;
	} else {
		return -1;
	}
}

function eventDataTransform(eventData){
	if(eventData.allDay === '0'){
		eventData.allDay = false;
	} else {
		eventData.allDay = true;
	}
	var color = getTypeValue(eventData.type);
	eventData.backgroundColor = color;
	eventData.borderColor = color;
	return eventData;
}

function dayClick(date, allDay, jsEvent, view ,event) {
	$("#calendar").fullCalendar('removeEvents',[0]); //移除添加事件
	createEvent.start = $.fullCalendar.formatDate(date, "YYYY-MM-DD");
	$("#calendar").fullCalendar('renderEvent',createEvent, true); 
	
	$(".fc-day").removeClass("active");
	$(this).addClass("active");
}

/**
 * fullCalendar的ViewRender函数
 * 在绘制视图的时候，展示和隐藏日历控件和调整日历控件的时间
 * 
 */
function viewRender(view, element){
	if(view.name == 'month' || view.name == 'agendaWeek' || view.name == 'listMonth') {//不显示小日历
		$(".fc-view-container").addClass("noSmall");
	} else {
		$(".fc-view-container").removeClass("noSmall");
	}
	if(view.name == 'agendaDay'){
		var start = $.fullCalendar.formatDate(view.intervalStart, "YYYY-MM-DD");
		$('#datepicker').datepicker('update', start);
	}
}


/**
 * 根据当前视图的开始和结束时间来查询日程
 */
function listAgendas(view, element){
	$("#calendar").fullCalendar('removeEvents');
	var start = $.fullCalendar.formatDate(view.start, "YYYY-MM-DD HH:mm:ss");
	var end = $.fullCalendar.formatDate(view.end, "YYYY-MM-DD HH:mm:ss");
	$.post(optUrl.list,{start:start,end:end}, function(agendas){
		$("#calendar").fullCalendar('renderEvents', agendas, true);
	});
}

/**
 * 事件渲染
 * 
 */
function eventRender(event, element) {
    if(event.id === 0) {
    	$(element).addClass("create-event");
    	$(element).find("fc-title").text("创建事件");
    } else {
    	$(element).addClass("real-event");
    }
    $(element).attr("href","javascript:void(0)");
}

function eventClick(calEvent, jsEvent, view) {
	if(calEvent.id === 0) {
		showCreateModel(calEvent.start);
	} else {
		showEventDetail(calEvent);
	}
}

function showEventDetail(calEvent){
	currentEvent = calEvent;
	$("#eventDetailModal .title").html(calEvent.title);
	var time = "时间：";
	if(calEvent.allDay) {
		time += $.fullCalendar.formatDate(calEvent.start, "YYYY-MM-DD");
	} else {
		var start = $.fullCalendar.formatDate(calEvent.start, "YYYY-MM-DD HH:mm:ss");
		var end = $.fullCalendar.formatDate(calEvent.end, "YYYY-MM-DD HH:mm:ss");
		time += start + " ~ " + end;
	}
	$("#eventDetailModal .time").html(time);
	$("#eventDetailModal .type").html("类型："+getTypeLabel(calEvent.type));
	if(calEvent.place){
		$("#eventDetailModal .place").html("地点："+calEvent.place);
	} else {
		$("#eventDetailModal .place").html("");
	}
	$("#eventDetailModal .remark").html(calEvent.remarks);
	$("#eventDetailModal").modal('show');
}

/**
 * 显示创建事件Modal
 */
function showCreateModel(start){
	$("#eventId").val("");
	$("#iptEventTitle").val("");
	$("#iptEventTitle").removeClass("required");
	$("#allDay").prop("checked", false);
	$("#emailNotify").prop("checked", false);
	toogleMoreItem(0);
	var start = $.fullCalendar.formatDate(start, "YYYY-MM-DD");
	$("#startTime").val(start+" 09:00:00");
	$("#endTime").val(start+" 09:30:00");
	$("#notifyTimeSelect").val("10");
	$("#place").val("");
	$("#remarks").val("");
	$("#createEventModal").modal('show');
}

/**
 * 调用接口创建事件
 */
function createFcEvent(){
	var eventData = {};
	var title = $.trim($("#iptEventTitle").val());
	if(title == ""){
		$("#iptEventTitle").addClass("required");
		return;
	}
	eventData.id = $("#eventId").val();
	eventData.title = title;
	if($("#allDay").prop("checked")){
		var start = $("#startTime").val(); //yyyy-MM-dd HH:mm:ss
		eventData.allDay = "1";
		eventData.start = start.substring(0, 10);
		eventData.end = start.substring(0, 10);
	} else {
		eventData.allDay = "0";
		eventData.start =  $("#startTime").val();
		eventData.end =  $("#endTime").val();
	}
	if($("#emailNotify").prop("checked")) {
		eventData.emailNotify="1";
	} else {
		eventData.emailNotify="0";
	}
	eventData.notifyTime = getNotifyDate(eventData.start);
	eventData.place = $("#place").val();
	eventData.remarks = $("#remarks").val();
	eventData.type = $("#calendarTypeSelect").val();
	var isCreate = true;
	var url = optUrl.create;
	if(eventData.id !== ""){
		url = optUrl.update;
		isCreate = false;
	}
	
	$.post(url, eventData, function(result){
		$("#createEventModal").modal('hide');
		if(result && result.id) {
			if(isCreate){
				$("#calendar").fullCalendar('removeEvents',[0]);
				$("#calendar").fullCalendar('renderEvent',result, true); 
			} else {
				$("#calendar").fullCalendar('removeEvents',[result.id]);
				$("#calendar").fullCalendar('renderEvent',result, true);
			}
		}
	});
}

function getNotifyDate(timeStr){
	var before = $("#notifyTimeSelect").val();
	var time = $.fullCalendar.moment(timeStr).subtract(before, "minute");
	return $.fullCalendar.formatDate(time, "YYYY-MM-DD HH:mm:ss");
}


function deleteFcEvent(){
	$.get(optUrl.del,{id:currentEvent.id},function(){
		$("#eventDetailModal").modal('hide');
		$("#calendar").fullCalendar('removeEvents',[currentEvent.id]);
	});
}

function toogleMoreItem(type){
	if(type == 1) {
		$("#createEventModal .more-item").show();
		$("#createEventModal .more-down").hide();
		$("#createEventModal .more-up").show();
	} else {
		$("#createEventModal .more-item").hide();
		$("#createEventModal .more-down").show();
		$("#createEventModal .more-up").hide();
	}
}

function editFcEvent(){
	$("#eventDetailModal").modal('hide');
	
	$("#eventId").val(currentEvent.id);
	$("#iptEventTitle").val(currentEvent.title);
	$("#iptEventTitle").removeClass("required");
	$("#allDay").prop("checked", currentEvent.allDay);
	$("#emailNotify").prop("checked", currentEvent.emailNotify==="1");
	toogleMoreItem(0);
	$("#startTime").val($.fullCalendar.formatDate(currentEvent.start, "YYYY-MM-DD HH:mm:ss"));
	if(currentEvent.end){
		$("#endTime").val($.fullCalendar.formatDate(currentEvent.end, "YYYY-MM-DD HH:mm:ss"));
	} else {
		$("#endTime").val($.fullCalendar.formatDate(currentEvent.start, "YYYY-MM-DD HH:mm:ss"));
	}
	var startStr = currentEvent.start._i;
	if(currentEvent.allDay) {
		startStr = startStr +" 00:00:00";
	}
	var st = $.fullCalendar.moment(startStr);
	var nd = $.fullCalendar.moment(currentEvent.notifyTime);
	var diff = st.diff(nd, "minute");
	$("#notifyTimeSelect").val(diff);
	$("#place").val(currentEvent.place);
	$("#remarks").val(currentEvent.remarks);
	$("#calendarTypeSelect").val(currentEvent.type);
	$("#createEventModal").modal('show');
}


function listFcTypes(){
	$.get(optUrl.types, function(types){
		agendaTypes = types;
		var typestr = "";
		for(var i = 0; i < types.length; i++) {
			typestr += "<option value='"+types[i].id+"'>"+types[i].label+"</option>";
		}
		$("#calendarTypeSelect").append(typestr);
		initDatePicker();
		initFullCalendar();
		initLeftCalHeight();
	});
}

function getTypeLabel(id){
	for(var i = 0; i < agendaTypes.length; i++) {
		if(agendaTypes[i].id===id){
			return agendaTypes[i].label;
		}
	}
}

function getTypeValue(id){
	for(var i = 0; i < agendaTypes.length; i++) {
		if(agendaTypes[i].id===id){
			return agendaTypes[i].value;
		}
	}
}