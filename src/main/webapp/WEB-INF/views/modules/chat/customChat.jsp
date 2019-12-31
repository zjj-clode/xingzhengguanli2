<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="renderer" content="webkit">
<title>智能咨询系统-${fns:getConfig('copyrightUnit')}</title>
<meta name="keywords" content="" />
<meta name="description" content="">
<meta name="viewport" content="width=1440">
<meta http-equiv="X-UA-Compatible" content="edge"/>
<script src="${ctxStatic}/jquery/j1.js"></script>
<!--[if lt IE 9]>
    <script type="text/javascript" src="${ctxStaticFront}/basic/js/html5shiv.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery/j1.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css" href="${ctxStaticFront}/basic/css/robot_zn.min.css"/>
<script src="${ctxStatic}/pushlet/ajax-pushlet-client.js" type="text/javascript"></script>
<script charset="utf-8" src="${ctxStatic}/lte/plugins/kindeditor/kindeditor-all-min.js"></script>
<script charset="utf-8" src="${ctxStatic}/lte/plugins/kindeditor/lang/zh_CN.js"></script>

<body>
    <div id="wrapper">
        <div class="header">
            <div class="logo">
                <img src="${ctxStaticFront}/basic/images/robot/logo.png" alt="" />
                <p class="l_txt">招生在线智能咨询</p>
            </div>
            <div class="h_right">
                <div class="perMsg">
                    <img src="${ctxStaticFront}/basic/images/zn/photo.png" alt="" />
                    <span>${name}</span>
                </div>
            </div>
        </div>
        <div class="zn_main">
            <div class="main_left">
                <div class="tit">会话列表</div>
                <div class="comp_input">
                    <div>
                        <input id="targetName" type="text"/>
                    </div>
                    <i class="icon_search"></i>
                </div>
                <!--<p class="tips">您好，请咨询学校相关部门，对相关问题进行提问！</p>-->
                <div class="lists mainBody">
                    <ul id="userSessions">

                    </ul>
                    <div id="noSession" class="noPerson" style="display: none;">
                    <!-- 暂无人员列表 -->
	                                                                        无匹配人员
	                </div>
                </div>
                
            </div>
            <div class="main_right">
                <div class="r1">
                    <div class="tit">
                        <!-- <p class="person">业务咨询人员一</p> -->
                    </div>
                    <!-- 对话内容区 -->
                    <div class="chatBody">
                        <div class="chatInn chatInn1" id="chatInnWrap">
                            
                        </div>
                    </div>
                    <!--对话结束-->
                    <!-- 输入内容区 -->
                    <div class="askBody ">
                    <!--    <div class="askInn">-->
                            <textarea name="askText" id="askText" placeholder="请简要描述问题，并按Enter提问"></textarea>
                            <div class="btns">
                                <p class="btn"><span class="send" id="sendBtn">发送</span></p>
                            </div>
                        <!--</div>-->
                    </div>
                </div>
                <div class="r2">
                    <ul class="rightTab">
                        <li class="fl on one">常见问题</li>
                        <li class="fl two">问题词典</li>
                    </ul>
                    <ul class="rightCon">
                        <li class="com_quest" style="display: block;">
                            <div class="search_question" id="list1">
                                <ul class="lists">
		                            <c:set var="page" scope="session" value="${fnc:getQuePageWithAnswer(0,10,true,'')}" />
		                            <c:forEach items="${page.list}" var="queans">
		                                <li>
		                                    <a href="javascript:void(0);" data-id="${queans.id}" onclick="answerClick(this)">
			                                    <div class="nr">
			                                        <p class="list_tit">${queans.content }</p>
			                                        <c:forEach items="${queans.answerList}" var="answer">
			                                            <p class="list_con">
			                                            ${fns:abbr(answer.respContent,100)}
			                                            </p>
			                                        </c:forEach>
			                                    </div>
		                                    </a>
		                                </li>
		                            </c:forEach>
		                        </ul>
		                    </div>
		                    <div class="change btn_main_quest" onclick="changePage(${page.pageNo},${page.last},this)">换一批</div>
		                </li>
		                <li class="ques_dic" style="display: none;">
                            <div id="list2" class="search_question" style="display: block;">
                                <div class="search_wrap">
                                    <img src="${ctxStaticFront}/basic/images/zn/al.png" alt="" />
                                    <input id="qaDictionaryInput2" name="askText" type="search" maxlength="10"/><span onclick="qaDictionary($('#qaDictionaryInput2').val())">搜索</span>
                                </div>
                                <div id="qalist">
                                
                                </div>
                            </div>
                            <div class="start_search" style="display: block;">
                                <img src="${ctxStaticFront}/basic/images/zn/zn_bg_03.jpg" alt="" />
                                <div class="center_wrap">
                                     <img src="${ctxStaticFront}/basic/images/zn/search_03.png"/>
                                     <div class="search_wrap">
                                        <img src="${ctxStaticFront}/basic/images/zn/al.png" alt="" />
                                        <input id="qaDictionaryInput1" name="askText" type="search" maxlength="10"/>
                                        <span onclick="qaDictionary($('#qaDictionaryInput1').val())">搜索</span>
                                     </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            
        </div>
    <div>
    <script type="text/javascript" src="${ctxStaticFront}/basic/js/util.js"></script>
    <script type="text/javascript" src="${ctxStaticFront}/basic/js/jquery.placeholder.min.js"></script>
    <script type="text/javascript">
        $(function(){
            $('textarea').placeholder({isUseSpan:true});
        });     
    </script>

	<script type="text/javascript">
		var targetId = "";
		var userId = '${userId}';
		var customPhoto = '${customPhoto}';
		if (customPhoto == "") {
			customPhoto = '${ctxStaticFront}/basic/images/robot/r_icon_06.png';
		}else {
			customPhoto = "${root}"+customPhoto;
		}
		var editor;
		var targetList = [];	//会话目标对象id数组
		var sessionList = [];	//会话id数组
	
		var historyTag = {};
		
		var globalSearchContent = "";
		var globalDictionarySearchContent = "";
	
		function init() {
			PL.joinListen('/onLineCustom');
		}
	
		function onData(event) {
			try {
				//var content = decodeURI(event.get('content'));
				var sessionId = event.get("sessionId");
				var senderId = event.get("senderId");
				var name = decodeURI(event.get("name"));
				var photo = event.get("photo");
				if (photo == null || photo == "") {
					photo = "${ctxStaticFront}/basic/images/robot/r_icon_27.png";
				}else {
					photo = "${root}"+photo;
				}
	
				if (getSessionIndex(senderId) == -1) { //不在会话中,加到会话列表
					var session = addUserSession(sessionId, senderId, name, photo);
					$("#userSessions").append(session);
					$("#userSessions li").hover(function() {
						$(this).find(".close-session").show();
					}, function() {
						$(this).find(".close-session").hide();
					});
				}
				if (targetId == "") {
					$("#userSessions li:eq(0)").trigger("click");
					$("#noSession").hide();
				}
				if (targetId != senderId) {
					var sessionIndex = getSessionIndex(senderId);
					$("#userSessions li").eq(sessionIndex).find(".badge")
						.show();
				}
	
				var msgId = event.get("msgId");
				$.get("${ctx}/chat/getContent?id=" + msgId, function(chatMsg) {
					//appendTargetMsg(content);
					appendTargetMsg(chatMsg.content, false, senderId, photo);
				});
	
				$.get("clearUnreadCount?userId=" + userId + "&targetId=" + targetId);
	
			} catch (e) {
				console.log(e);
			}
		}
	
		function onError(event) {
			//console.log("onerror reinit");
			//PL._init();(暂去，未测试去除是否正确)
			init();
		}
	
		function sendMsg(content) {
			$.post("${ctx}/chat/sendMsg", {
				type : "1",
				content : content,
				toUserId : targetId
			}, function(result) {});
		}
	
		//获取所有会话并添加到会话列表，没有则显示无会话
		function getUserSessions() {
			$.get("userSessions", function(data) {
				var content = "";
				if (data.length == 0) {
					$("#noSession").show();
				} else {
					for (var i = 0; i < data.length; i++) {
						if (data[i].targetUser) {
							var targetPhoto = data[i].targetUser.photo;
							if (targetPhoto == null || targetPhoto == "") {
								targetPhoto = "${ctxStaticFront}/basic/images/robot/r_icon_27.png";
							}else {
								targetPhoto = "${root}"+targetPhoto;
							}
							content += addUserSession(data[i].id, data[i].targetUser.id,
								data[i].targetUser.name, targetPhoto);
						}
					}
					$("#userSessions").html(content);
					if (targetList.length > 0) {
						$("#userSessions li").hover(function() {
							$(this).find(".close-session").show();
						}, function() {
							$(this).find(".close-session").hide();
						});
						$("#userSessions li:eq(0)").trigger("click");
					} else {
						$("#noSession").show();
					}
				}
			});
		}
	
		//添加会话到会话列表
		function addUserSession(sessionId, userId, userName, userPhoto) {
			/* if (userPhoto == null || userPhoto == "") {
				userPhoto = "${ctxStatic}/front/robot/images/avatar_default.png";
			} */
			targetList.push(userId);
			sessionList.push(sessionId);
			historyTag[userId] = {
				topMsgDate : "",
				hasMoreMsg : true
			};
			var myDate = new Date();
			var mytime=myDate.toLocaleTimeString();
			$("#chatInnWrap").append("<div id='" + userId + "'></div>");
			//content：添加会话到会话列表的代码
			//initChatTarget()：初始化聊天对象
			/* var content = '<li onclick="initChatTarget(\''
					+ userId
					+ '\', this)"><a href="javascript:void(0)" class="list-group-item">';
			content += userName;
			content += '<span class="badge"><img src="${ctxStatic}/images/new_msg.gif"></span>';
			content += '</a><a href="javascript:void(0)" onclick="deleteSession(\''
					+ userId + '\', this)" class="close-session">删除</a></li>'; */
	
			var content = '<li  onclick="initChatTarget(\''
				+ userId
				+ '\', this)"><a href="javascript:void(0);"><div class="tx news"><span class="badge" style="display: none;"></span>';
			content += '<img src="'
			content += userPhoto;
			content += '"/></div>';
			content += '<div class="nr"><p class="list_tit">';
			content += userName;
			content += '<span></span></p>';
			content += '<p class="list_con"></p></div><i class="icon_delete"  data-userid='+ userId + '></i>';
			content += '</a></li>';
			return content;
		}
	
		//在对话框中添加目标消息
		function appendTargetMsg(content, before, sessionId, photo) {
			if (photo == null || photo == "") {
				photo = $($("a[class='active']").parents('li')[0]).find("img")[0].src;
			}
			var str = '<dl class="answer link"><dt><img src="'+ photo +'" alt=""></dt><dd><img src="${ctxStaticFront}/basic/images/robot/img_b1.png" class="jiao" alt=""><div class="word">';
			str += content + '</div></dd></dl>';
			if (before) {
				if (sessionId) {
					$("#" + sessionId).prepend(str);
				} else {
					$("#" + targetId).prepend(str);
				}
				$('.chatBody').scrollTop(0);
			} else {
				if (sessionId) {
					$("#" + sessionId).append(str);
				} else {
					$("#" + targetId).append(str);
				}
				$('.chatBody').scrollTop($('.chatBody')[0].scrollHeight);
			}
	
		}
	
		//在对话框中添加自己消息
		function appendSelfMsg(content, before) {
			var str = '<dl class="ask"><dt><img src="'+customPhoto+'" " alt=""></dt><dd><img src="${ctxStaticFront}/basic/images/robot/img_b2.png" class="jiao" alt=""><div class="word">';
			str += content + '</div></dd></dl>';
			if (before) {
				$("#" + targetId).prepend(str);
				$("#" + targetId).scrollTop(0);
			} else {
				$("#" + targetId).append(str);
				$('.chatBody').scrollTop($('.chatBody')[0].scrollHeight);
				editor.html("");
				editor.focus();
			}
		}
	
		//添加系统消息
		function appendSystemMsg(content, before) {
			var str = '<div class="sysem_msg">' + content + '</div>';
			if (before) {
				$("#" + targetId).prepend(str);
				$("#" + targetId).scrollTop(0);
			} else {
				$("#" + targetId).append(str);
			}
		}
	
		//点击发送按钮
		function send() {
			//var content = $.trim(editor.text().replace("<br />", ""));
			var content = $.trim(editor.html());
			if(editor.text() == ""){	
				alert("发送内容不能为空,请输入内容");
			}else{
				if (targetId == "") {
					alert("请选择聊天对象");
					return;
				}
				appendSelfMsg(content);
				sendMsg(content);
				
				checkLetterNum();
			}
		}
	
		function getSessionIndex(targetUserId) {
			var index = -1;
			for (var i = 0; i < targetList.length; i++) {
				if (targetUserId == targetList[i]) {
					index = i;
					break;
				}
			}
			return index;
		}
		$(function(){
			$('body').on('click','.main_left .lists .icon_delete',function(){
				deleteUser($(this).data('userid'),this);
				return false;
			})
		})
	    //移除会话弹窗
	    function deleteUser(id,href){
            var $de = $('<div class="delete_box">'+
                    '<div class="cover"></div>'+
                    '<div class="delete_con">'+
                        '<img class="robot_logo" src="${ctxStaticFront}/basic/images/robot/robot2.png"/>'+
                        '<div class="con">'+
                            '<div class="r_tit">删除联系人</div>'+
                            '<div class="desc">您确定要删除下列联系人？</div>'+
                            '<div class="btns">'+
                                '<button class="btn_confirm">确定</button>'+
                                '<button class="btn_cancle">取消</button>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>').appendTo($('.main_left')).fadeIn();
            $de.find('.btn_confirm').on('click',function(){
                // 确定删除逻辑
                // $de.fadeOut(function(){$(this).remove();});
                deleteSession(id,href);
                $de.fadeOut(function(){$(this).remove();});
            });
            $de.find('.btn_cancle').on('click',function(){
                $de.fadeOut(function(){$(this).remove();});
            });
           
        }
		//移除(删除)某个会话
		function deleteSession(targetUserId, href) {
			$.get("deleteUserSession", {
				toUserId : targetUserId
			}, function() {
				$("#" + targetUserId).remove(); //移除聊天窗口
				$(href).parents('li').eq(0).remove(); //移除会话列表
				if ($("#userSessions li").length == 0) {
					$("#noSession").show();
				}
				var index = getSessionIndex(targetUserId);
				if (index != -1) {
					targetList.splice(index, 1);	//从数组中删除
					sessionList.splice(index, 1);
				}
				historyTag.targetUserId = {};
	
				$("#userSessions li:eq(0)").trigger("click");
			});
		}
	
		/**
		 初始化聊天对象
		 */
		function initChatTarget(id, li) {
			targetId = id;
			$("#userSessions li").removeClass("active");
			$(li).addClass('active');
			$("#chatInnWrap>div").hide();
			$("#" + id).show();
			$(li).find(".badge").hide();
			firstTestHistory();
		}
	
		/*
		 * 查询历史消息
		 */
		function queryHistorys(size) {
			if (targetId == "") {
				return;
			}
			
			var bottom=$('.chatInn').outerHeight()-$('.chatBody').scrollTop();
			var hisInfo = historyTag[targetId];
			if (!hisInfo.hasMoreMsg) {
				return;
			}
	
			$.getJSON("userMsgs", {
				toUserId : targetId,
				topMsgDate : hisInfo.topMsgDate,
				size : size
			}, function(msgs) {
				$("#historyMsgTag").remove();
				if (msgs.length == 0) {
					hisInfo.hasMoreMsg = false;
					//appendSystemMsg("没有更多消息了", true);
				} else {
					for (var i = 0; i < msgs.length; i++) {
						var msg = msgs[i];
						if (i == msgs.length - 1) {
							hisInfo.topMsgDate = msg.createDate;
						}
						if (msg.sender == '1') {
							appendSelfMsg(msg.content, true);
						} else {
							appendTargetMsg(msg.content, true);
						}
					}
					$(".chatInn1").prepend('<div id="historyMsgTag" class="see_more" style="display:block;"><a href="javascript:void(0);" onclick="queryHistorys(10)">查看更多消息</a></div>');
					if (msgs.length < size) {
						hisInfo.hasMoreMsg = false;
						$("#historyMsgTag").remove();
						//appendSystemMsg("没有更多消息了", true);
					}
					
					var val=$('.chatInn').outerHeight()-bottom;
					$('.chatBody').scrollTop(val);
				}
			});
	
			$.get("clearUnreadCount?userId=" + userId + "&targetId=" + targetId);
		}
		
		//页面初始化时测试客服与当前客户有无历史消息记录
		function firstTestHistory() {
			if (targetId == "") {
				return;
			}
	
			$.getJSON("userMsgs", {
				toUserId : targetId,
				topMsgDate : historyTag[targetId].topMsgDate,
				size : 1
			}, function(msgs) {
				if (msgs.length == 0) {
					historyTag[targetId].hasMoreMsg = false;
				}
			});
	
			$.get("clearUnreadCount?userId=" + userId + "&targetId=" + targetId);
			
			if(historyTag[targetId].hasMoreMsg) {
				$(".chatInn1").prepend('<div id="historyMsgTag" class="see_more" style="display:block;"><a href="javascript:void(0);"  onclick="queryHistorys(10)">查看更多消息</a></div>');
			}
		}
	
		/*
		 * 站内搜索和问题词典添加单击事件
		 */
		function searchAllClick(type,content,link,title) {
		//alert("触发"+type+content+link+title);
			 if(type == 'qa'){
				//var answer = content;
				//editor.html(answer);
				//send();
				var id = content;
			  $.ajax({
				   type: "POST",
				   url: "${ctxf}/robot/queryById",
				   data: {"id":id,"type":type},
				   dataType : "json",
			
				   success: function(dataObj){
						if(dataObj.length==1){
							if(dataObj[0].type == "qa"){
								 editor.html(dataObj[0].content)
								   send();
							}	
						}else{
							html = "问题太难啦，我还没有学会呢！您可以使用 &nbsp;<a href='${ctxf}/qa/question' target='question'>在线提问</a>&nbsp;功能！";
							editor.html(html);
							send();
						}
				   }
				});	
			}else if(type == 'article'){
				if(link.indexOf("http") == 0){
					editor.html( "点击文章标题连接原文：</br><a target='_blank' id='linkcolor' style='color:white;' href='"+link+"'>"+title+"</a>");
				}else if(link.indexOf("http") == -1){
					editor.html( "点击文章标题连接原文：</br><a target='_blank' id='linkcolor' style='color:white;' href='${ctxf}/"+link+"'>"+title+"</a>");
				}	
				send();
			}
			
		}
		/*
		 * 给问题列表中的答案添加单击事件
		 */
		function answerClick(a) {
			/* var answer = $(a).text();
			editor.html(answer);
			send(); */
			 var id = $(a).attr("data-id");
			var type = "";
			  $.ajax({
				   type: "POST",
				   url: "${ctxf}/robot/queryById",
				   data: {"id":id,"type":type},
				   dataType : "json",
			
				   success: function(dataObj){
						if(dataObj.length==1){
							if(dataObj[0].type == "qa"){
								 editor.html(dataObj[0].content)
								   send();
							}else if(dataObj[0].type == "article"){
								if(dataObj[0].link.indexOf("http") == 0){
									html = "点击文章标题连接原文：</br><a target='_blank' href='"+dataObj[0].link+"'>"+dataObj[0].title+"</a>";
									editor.html(html)
									   send();
									
								}else if(dataObj[0].link.indexOf("http") == -1){
									html = "点击文章标题连接原文：</br><a target='_blank' href='${ctxf}/"+dataObj[0].link+"'>"+dataObj[0].title+"</a>";
									editor.html(html)
									   send();
								}	
							}	
						}else{
							html = "问题太难啦，我还没有学会呢！您可以使用 &nbsp;<a href='${ctxf}/qa/question' target='question'>在线提问</a>&nbsp;功能！";
							editor.html(html);
							send();
						}
				   }
				});
			//send();
			//queryOne($(a).attr("data-id"),$(a).text(),"");
		}

		/*
		 * 换一批按钮点击
		 */
		/* function changePage1(pageNo, last, span) {
			//alert($(span).parent().attr('id'));
			var listId = $(span).parent().attr('id');
			var searchContent = "";
			if(listId == 'list1') {
				searchContent = $("#searchInput").val();
				if (searchContent == "站内搜索" || searchContent == "") {
					searchContent = globalSearchContent;
					$("#searchInput").val(searchContent);
				}else {
					globalSearchContent = searchContent;
				}
			}else if(listId == 'list2') {
				searchContent = $("#qaDictionaryInput").val();
				if (searchContent == "问题词典" || searchContent == "") {
					searchContent = globalDictionarySearchContent;
					$("#qaDictionaryInput").val(searchContent);
				}else {
					globalDictionarySearchContent = searchContent;
				}
			}
			changePage11(pageNo, searchContent, $(span).parent());
		}
	 */
		/*
		 * 换一批按钮
		 */
		function changePage(pageNo, last, span) {
			$.post("getQuePageWithAnswer", {
				pageNo : pageNo,
				totalPageNum : last,
				number : 10,
				withAnswer : true
			}, function(data) {
				if (data.list == undefined) {
					$('#list1').html('<div id="noRightList" class="noOnline">无查询结果</div>');
				} else {
					$('#list1').html('<ul class="lists"></ul>');
					for (var i = 0; i < data.list.length; i++) {
						var queans = data.list[i];
						var answerList = '';
						for (var j = 0; j < data.list[i].answerList.length; j++) {
							//data.list[i].answerList[j].respContent = 
							var a =data.list[i].answerList[j].respContent.replace(/<[^>]+>/g,"" );/*    /(^\s*)|(\s*$)/g, "" */
							var answer = '<p class="list_con">' + a.substring(0,100) + '</p>';
							answerList += answer;
						}
						$('#list1').find("ul").append('<li><a href="javascript:void(0);" data-id="'+ queans.id +'" onclick="answerClick(this)"><div class="nr"><p class="list_tit">' + queans.content + '</p>' + answerList + '</div></a></li>');
					}
					$('.com_quest div.change').remove();
					$('.com_quest').append('<div class="change btn_main_quest" onclick="changePage(' + data.pageNo + ',' + data.last + ', this)">换一批</div>');
				}
			});
			$('.rightInn').scrollTop(0);
		}
		/*
		 * 问题词典换一批按钮
		 */
		function changePage11(pageNo, searchContent, thisList) {
			$('#qalist').html('');
			$.post("${ctxf}/robot/chatSearchAll", {
				pageNo : pageNo,
				pageSize : 10,
				askText : searchContent
			}, function(data) {
				if (data.list == undefined) {
					$('#qalist').html('');
					$('#qalist').html('<div class="no_data">暂无搜索结果</div>');
					$('.ques_dic').show();
                    $('.start_search').hide();
				} else {
					$('#qalist').html('<ul class="lists"></ul>');
					var str = '';
					for (var i = 0; i < data.list.length; i++) {
						var queans = data.list[i];
						var type = queans.type;
						var content = queans.content;
						var link = queans.link;
						var title = queans.title;
						var id = queans.id;
						if(type == "qa"){
					//	content = $.trim(content);
							var answerList = '<p class="list_con">' + content.substring(0,100) + '</p>';
						} else if(type == "article") {
						title = title.replace('<font color="red">','');
						title = title.replace('</font>','');
							var answerList = '<p class="list_con">' + content.substring(0,35) + '</p>';
						} 
						
						str += '<li><a href="javascript:void(0);" onclick="searchAllClick('+"'"+type+"'"+','+"'"+id+"'"+','+"'"+"'"+','+"'"+"'"+');"><div class="nr"><p class="list_tit">' + queans.title + '</p>' + answerList + '</div></a></li>';
						
					}
					var nextPage = pageNo + 1;
                    var lastPage = pageNo - 1;
					if(pageNo == 1 && pageNo != data.last){
                        str += '<ul class="paging"><li><ul><li class="active"><a href="#">'+ pageNo +'</a></li></ul></li><li><a rel="next" onclick="changePage11('+ nextPage + ',\'' + searchContent +'\', $(\'#qalist\'))"><em>下一页</em></a></li</li></ul>'
                    }
                    if(pageNo > 1 && pageNo != data.last){
                        str += '<ul class="paging"><li><a rel="next" onclick="changePage11('+ lastPage + ',\'' + searchContent +'\',$(\'#qalist\'))"><em>上一页</em></a></li>'+
                        '<li><ul><li class="active"><a href="#">'+ pageNo +'</a></li></ul></li><li><a rel="next" onclick="changePage11('+ nextPage + ',\'' + searchContent +'\',$(\'#qalist\'))"><em>下一页</em></a></li></ul>'
                    }
                    if(pageNo > 1 && pageNo == data.last){
                        str += '<ul class="paging"><li><a rel="next" onclick="changePage11('+ lastPage + ',\'' + searchContent +'\',$(\'#qalist\'))"><em>上一页</em></a></li>'+
                               '<li><ul><li class="active"><a href="#">'+ pageNo +'</a></li></ul></li></ul>';
                    } 
                    $('#qalist').find("ul").append(str);
					//var searchContent = searchContent.replace('"',"'");
					//$('#qalist').append('<span class="fr hand change mt10" onclick="changePage11(' + data.pageNo + ','  + "'" +searchContent + "'" + ', this)">换一批</span>');
				    $('.ques_dic').show();
				    $('.start_search').hide();
				}				
			});
			$('.rightInn').scrollTop(0);
		}
	
		/*
		 * 站内搜索按钮
		 */
		function search() {
			//console.log('22222');
			var searchContent = $("#searchInput").val();
			if (searchContent == "站内搜索") {
				searchContent = "";
			}
			globalSearchContent = searchContent;
			changePage11(1, searchContent, $("#list1"));
			$('.rightTab li one').removeClass('on');
				$('.rightTab li two').addClass('on');
				$('.rightInn .list').hide().eq(index).fadeIn(300);
			/* $("#list1").hide();
			$("#list2").show(); */
		}
		
		/*
		 * 问题词典搜索按钮
		 */
		function qaDictionary(searchContent) {
			if (searchContent == "问题词典" || searchContent == "") {
				$("#qalist").html("");
				return;
			}
			globalDictionarySearchContent = searchContent;
			changePage11(1,searchContent, $("#qalist"));
		}
		
	//控制输入字数
	function checkLetterNum(){
		var len = $.trim(editor.text()).length;
		var maxLen = 100;
		if(len>=maxLen){
			var str = editor.text();
			editor.text("");
			editor.appendHtml(str.substring(0,100));
			//editor.text(editor.text().substr(0,maxLen));
			$("div.askInn > div > p.tip").html("输入字数不能超过"+maxLen+"个字，<font color='red'>字数已满</font>。");
		}else if(len+10>=maxLen&&len<maxLen){
			$("div.askInn > div > p.tip").html("输入字数不能超过"+maxLen+"个字，还能输入<font color='red'> "+(maxLen-len)+" </font>个字。");
		}else{
			$("div.askInn > div > p.tip").html("输入字数不能超过"+maxLen+"个字");
		}
	}
	
		function initKindeditor() {
			KindEditor.ready(function(K) {
				editor = K.create('textarea[name="askText"]', {
					width : '100%',
					resizeType : 0,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					height : 160,
					minHeight : 75,
					newlineTag : 'br',
					items : [ 'bold', 'underline', 'fontname', 'fontsize',
						'forecolor', 'hilitecolor', 'link', 'emoticons' ],
					afterCreate : function() { //设置编辑器创建后执行的回调函数
						var self = this;
	
						$(self.edit.doc).bind("keydown", function(event) {
							if (event.keyCode == 13) {
								//event.preventDefault();
								send();
							}
						});
						
						$(self.edit.doc).bind("input propertychange",function (event) {
		            		checkLetterNum();
		            	});
					}
				});
			});
		}
		
		function closeWindow() {
					var userAgent = navigator.userAgent;
					if (userAgent.indexOf("Firefox") != -1
						|| userAgent.indexOf("Chrome") != -1) {
						window.location.replace("about:blank");
						window.close();
					} else {
						window.opener = null;
						window.open("", "_self");
						window.close();
					}
		}
	
		$(function() {
			init();
	
			getUserSessions();
	
			initKindeditor();
			
			//搜索会话列表
			$(".icon_search").bind('click', function(){
				var name = $("#targetName").val();
				$.get("queryUserSessions", {"name": name}, function(data) {
	                var content = "";
	                if (data.length == 0) {
	                    $("#noSession").show();
	                } else {
	                    for (var i = 0; i < data.length; i++) {
	                    	if (data[i].targetUser) {
	                            var targetPhoto = data[i].targetUser.photo;
	                            if (targetPhoto == null || targetPhoto == "") {
	                                targetPhoto = "${ctxStaticFront}/basic/images/robot/r_icon_27.png";
	                            }else {
	                                targetPhoto = "${root}"+targetPhoto;
	                            }
	                            content += addUserSession(data[i].id, data[i].targetUser.id,
	                                data[i].targetUser.name, targetPhoto);
	                        }
	                    }
	                    $("#userSessions").html(content);
	                    if (targetList.length > 0) {
	                        $("#userSessions li").hover(function() {
	                            $(this).find(".close-session").show();
	                        }, function() {
	                            $(this).find(".close-session").hide();
	                        });
	                        $("#userSessions li:eq(0)").trigger("click");
	                    } else {
	                        $("#noSession").show();
	                    }
	                }
	            });
			});
			
			$("#searchInput").keyup(function(event) {
				if (event.keyCode == '13') {
					search();
				}
			});
			
			$("#qaDictionaryInput").keyup(function(event) {
				if (event.keyCode == '13') {
					qaDictionary();
				}
			});
	
			//发送按钮
			$("#sendBtn").bind('click', function() {
				send();
			});
	
			//关闭会话按钮
			$("#closeBtn").bind('click', function() {
				$("a[class='active']").next().click();
			});
	
			//给右侧提示添加点击事件
			/* $(".rightInn ul").on('click','li',function(){
				var askTemp = $(this).text();
				$("#askText").val(askTemp);
				send();//页面上把发送的信息显示完全，此时并未真正把数据发给后台，调用readyHello为真正发送数据
			}); */
	
			// 右侧tab切换
			$('.rightTab li').bind('click', function() {
				if($(this).hasClass('on')){
                    return;
                }
                $(this).siblings('.on').removeClass('on');
                $('.rightCon>li').hide().eq($(this).addClass('on').index()).fadeIn()
			});
			// 放大缩小窗口
			$('#winCtrl').bind('click', function() {
				if ($('#wrapper').hasClass('small')) {
					$('#wrapper').removeClass('small')
					$('#wrapper').addClass('big');
					$(this).find('img').attr('src', '${ctxStatic}/front/robot/images/ico_collapse.png');
				} else {
					$('#wrapper').removeClass('big')
					$('#wrapper').addClass('small');
					$(this).find('img').attr('src', '${ctxStatic}/front/robot/images/ico_expand.png');
				}
			});
			// 关闭窗口
			$('#winClose').bind(
				'click',
				function() {
					var userAgent = navigator.userAgent;
					if (userAgent.indexOf("Firefox") != -1
						|| userAgent.indexOf("Chrome") != -1) {
						window.location.href = "about:blank";
						window.close();
					} else {
						window.opener = null;
						window.open("", "_self");
						window.close();
					}
				});
	
			$(window).unload(function() {
				PL.leave();
			});
	
			// 人工和智能选择
			$('.options li').click(function() {
				var index = $(this).index();
				if (index == 0) {
					$('.options').removeClass('c2');
				} else if (index == 1 && !$('.options').hasClass('c2')) {
					$('.options').addClass('c2');
				}
			})
		});
		(function($) {
			$.fn.placeholder = function() {
				var txt = $(this).attr('x-placeholder');
				$(this).val(txt);
				$(this).addClass('x-placeholder');
				$(this).on('blur', function() {
					if ($(this).val() == '') {
						$(this).val(txt);
						$(this).addClass('x-placeholder');
					} else {
						$(this).removeClass('x-placeholder');
					}
				});
				$(this).on('focus', function() {
					if ($(this).val() == txt) {
						$(this).val('');
						$(this).removeClass('x-placeholder');
					}
				});
			}
		})($);
		$('input[x-placeholder]').each(function(){
			$(this).placeholder();
		});
		$(function(){
            if($('.comp_input.focus').length > 0){
                $('.comp_input.focus').removeClass('focus');
            }
        });
	</script>
   
</body>
</html>