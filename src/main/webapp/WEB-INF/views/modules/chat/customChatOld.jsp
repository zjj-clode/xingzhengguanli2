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
<link type="text/css" rel="stylesheet" href="${ctxStatic}/front/robot/css/base.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/front/robot/css/robot.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/front/robot/css/robot-mana.min.css" />
<script src="${ctxStatic}/jquery/j1.js"></script>
<script src="${ctxStatic}/front/robot/js/html5.js"></script>
<script src="${ctxStatic}/pushlet/ajax-pushlet-client.js" type="text/javascript"></script>
<script charset="utf-8" src="${ctxStatic}/lte/plugins/kindeditor/kindeditor-all-min.js"></script>
<script charset="utf-8" src="${ctxStatic}/lte/plugins/kindeditor/lang/zh_CN.js"></script>

<style type="text/css">
.active {
	font-weight: bold;
	color: #4ba9d2 !important;
}

.close-session {
	float: right;
	display: none;
}

#userSessions li {
	padding: 5px 5px 5px 13px;
	cursor: pointer;
}

#userSessions li:hover {
	background-color: #eee;
}

.badge {
	padding-left: 10px;
	margin-left: 10px;
	display: none;
}

.sysem_msg {
	text-align: center;
	color: #999;
}

#chatInnWrap>div {
	display: none;
}

.askInn {
	padding: 0;
}

.ke-container {
	border-left: none !important;
	border-right: none !important;
}

.ke-statusbar {
	display: none;
}
#linkcolor{
	color: white;
}
    .ask .word a{
        color:#fff;
    }
</style>
<script type="text/javascript">
function searchAll(){

	if($("#askText").val() == ""||$("#askText").val().replace(/(^\s*)|(\s*$)/g, "")==""){
		alert("请输入搜索内容");
		return false;
	}else{
		$('#searchForm').submit();
	}
	
}
$(function(){
		$("#askText1").keydown(function(e){
	        if(e.keyCode == 13){
	         	if($("#askText1").val() == ""||$("#askText1").val().replace(/(^\s*)|(\s*$)/g, "")==""){
					alert("请输入搜索内容");
					return false;
				}else{
					$('#searchForm').submit();
				}
			}
	    });
});
</script>
</head>

<body>
	<div id="wrapper" class="big">
		<div class="header pr">
			<span class="robot pa"><img src="${ctxStatic}/front/robot/images/custom.png" class="vt" alt=""></span>
			<h1>
				${fns:getConfig('copyrightUnit')} | 管理员页面<span>小时为您提供优质服务</span>
			</h1>
			<div class="tools pa clear">
				<!-- <p class="fl tc hand" id="winCtrl">
					<img src="${ctxStatic}/front/robot/images/ico_expand.png" alt="">
				</p> -->
				<p class="fl tc hand" id="winClose">
					<img src="${ctxStatic}/front/robot/images/ico_close2.png" alt="">
				</p>
			</div>
			<ul class="options pa clear">
			 <form:form action="${ctx}/searchAll" method="post" id="searchForm" target="_blank">
				<input type="text" id="askText1" name="askText" placeholder="站内搜索" class="font_15"/>
				<!-- <button type="submit" id="shousuo" style="background-color: #818492;border: 0px;"> -->
					<img class="icon-search"  onclick="searchAll();return false;" src="${ctxStatic}/front/robot/images/icon_search.png">
				<!-- </button> -->
			</form:form>
				<%-- <input id="searchInput" x-placeholder="站内搜索" />
				<img class="icon-search" src="${ctxStatic}/front/robot/images/icon_search.png" > --%>
			</ul>
		</div>
		<div class="mainBody clear">
			<div class="l_left">
				<!-- 左侧会话列表 -->
				<div class="perBody">
					<ul id="userSessions">
						<!-- 动态添加会话 -->
					</ul>
					<div id="noSession" class="noOnline" style="display: none">暂无会话</div>
				</div>
			</div>
			
			<div class="l_right">
				<div class="lr_left">
					<!-- 对话内容区 -->
					<div class="chatBody pa">
						<div class="chatInn f14 chatInn1" id="chatInnWrap">
							<!-- 动态添加聊天记录区 -->
						</div>
					</div>
					
					<!-- 输入内容区 -->
					<div class="askBody pa">
						<div class="askInn">
							<!-- <span id="historyBtn" onclick="queryHistorys(10)" style="position: absolute;right: 10px;color:#666;top:4px;font-size:10px;cursor:pointer;">消息历史</span> -->
							<textarea name="askText" id="askText" class="f14" placeholder="请输入聊天内容" maxlength="100"></textarea>
							<div class="clear bot">
								<p class="fl tip">输入字数不能超过100个汉字</p>
								<p class="btn f16 fr">
									<span id="closeBtn" class="hand close ib mr15" onclick="closeWindow()">关闭</span> <span id="sendBtn" class="hand send ib">发送</span>
								</p>
							</div>
						</div>
					</div>
				</div>
				
				<div class="lr_right">
					<!-- 右侧部分 -->
					<div class="rightPart fr">
						<ul class="rightTab clear f16 tc">
							<li class="fl on one">常见问题</li>
							<li class="fl two">问题词典</li>
						</ul>
						<div class="rightInn f14 clear">
							<div class="list">
								<p class="tips">您好，请咨询学校相关部门，对相关问题进行提问！</p>
								<div id="list1">
									<ul>
										<c:set var="page" scope="session" value="${fnc:getQuePageWithAnswer(0,10,true,'')}" />
										<c:forEach items="${page.list}" var="queans">
											<li style="font-size:15px;color: #333;">
												<div>
													${queans.content }
													<c:forEach items="${queans.answerList}" var="answer">
														<div>
															<%-- <a style="font-size:15px;color: #666;" href="javascript:void(0);" onclick="answerClick(this)">${answer.respContent}</a> --%>
														<a style="font-size:15px;color: #666;" href="javascript:void(0);" data-id="${queans.id}" onclick="answerClick(this)">${fns:abbr(answer.respContent,100)}</a>
														</div>
													</c:forEach>
												</div>
											</li>
										</c:forEach>
									</ul>
									<span class="fr hand change mt10" onclick="changePage(${page.pageNo},${page.last},this)">换一批</span>
								</div>
							</div>
							<div class="list" style="display:none">
								<div class="t-c">
									<div class="q-ser">
										<input id="qaDictionaryInput" name="askText" x-placeholder="问题词典"/>
										<img class="icon-search" src="${ctxStatic}/front/robot/images/q-ser.png" onclick="qaDictionary()">
										<%-- <form:form action="${ctx}/searchAll" method="post" id="searchForm">
											<input type="text" name="askText" placeholder="站内搜索" class="font_15"/>
											<img class="icon-search" src="${ctxStatic}/front/robot/images/q-ser.png" onclick="qaDictionary()">
										</form:form> --%>
									
									</div>
								</div>
								<div id="list2">
									<!-- <ul>
										<li>
											<div>
												<a href="#">主要联系方式是什么？</a>
												<div>本校2017年计划招生200人，其中在山东理工类招生20人....</div>
											</div>
										</li>
									</ul>
									<span class="fr hand change mt10" onclick="changePage(${page.pageNo},${page.last},this)">换一批</span> -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="footer tc f14">Copyright &copy; 2004-2016 ${fns:getConfig('copyrightUnit')}招生办公室 版权所有</div>

	<script type="text/javascript">
		var targetId = "";
		var userId = '${userId}';
		var customPhoto = '${customPhoto}';
		if (customPhoto == "") {
			customPhoto = '${ctxStatic}/front/robot/images/custom.png';
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
					photo = "${ctxStatic}/front/robot/images/avatar_default.png";
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
								targetPhoto = "${ctxStatic}/front/robot/images/avatar_default.png";
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
				+ '\', this)" class="active"><div class="sticker">';
			content += '<img src="'
			content += userPhoto;
			content += '"/></div>';
			content += '<div class="infor"><div class="name"><a href="javascript:void(0)">';
			content += userName;
			content += '<span class="badge"><img src="${ctxStatic}/images/new_msg.gif"></span>';
			content += '</a><a href="javascript:void(0)" onclick="deleteSession(\''
				+ userId + '\', this)" class="close-session">删除</a></div>';
			content += '<div class="desc">';
			content += '对方已成功接收文件';
			content += '</div>';
			content += '</div></li>';
			return content;
		}
	
		//在对话框中添加目标消息
		function appendTargetMsg(content, before, sessionId, photo) {
			if (photo == null || photo == "") {
				photo = $($("a[class='active']").parents('li')[0]).find("img")[0].src;
			}
			var str = '<dl class="answer pr mb20 clear" style="word-wrap:break-word;"><dt class="pa"><img src="' + photo + '" alt=""></dt><dd class="pr fl"><img src="${ctxStatic}/front/robot/images/img_b1.png" class="pa jiao" alt=""><div class="word" style="max-width: 500px;">';
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
			var str = '<dl class="ask pr mb20 clear" style="word-wrap:break-word;"><dt class="pa"><img src="' + customPhoto + '" alt=""></dt><dd class="pr fr"><img src="${ctxStatic}/front/robot/images/img_b2.png" class="pa jiao" alt=""><div class="word" style="max-width: 500px;">';
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
	
		//移除(删除)某个会话
		function deleteSession(targetUserId, href) {
			$.get("deleteUserSession", {
				toUserId : targetUserId
			}, function() {
				$("#" + targetUserId).remove(); //移除聊天窗口
				$(href).parent().parent().parent().remove(); //移除会话列表
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
			$("#userSessions a").removeClass("active");
			$(li).find("a:eq(0)").addClass('active');
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
					$(".chatInn1").prepend('<div id="historyMsgTag" style="display:block;"><span onclick="queryHistorys(10)">查看更多消息</span></div>');
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
				$(".chatInn1").prepend('<div id="historyMsgTag" style="display:block;"><span onclick="queryHistorys(10)">查看更多消息</span></div>');
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
							html = "问题太难啦，我还没有学会呢！您可以使用 &nbsp;<a href='${ctx}/qa/question' target='question'>在线提问</a>&nbsp;功能！";
							editor.html(html);
							send();
						}
				   }
				});	
			}else if(type == 'article'){
				if(link.indexOf("http") == 0){
					editor.html( "点击文章标题连接原文：</br><a target='_blank' id='linkcolor' style='color:white;' href='"+link+"'>"+title+"</a>");
				}else if(link.indexOf("http") == -1){
					editor.html( "点击文章标题连接原文：</br><a target='_blank' id='linkcolor' style='color:white;' href='${ctx}/"+link+"'>"+title+"</a>");
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
									html = "点击文章标题连接原文：</br><a target='_blank' href='${ctx}/"+dataObj[0].link+"'>"+dataObj[0].title+"</a>";
									editor.html(html)
									   send();
								}	
							}	
						}else{
							html = "问题太难啦，我还没有学会呢！您可以使用 &nbsp;<a href='${ctx}/qa/question' target='question'>在线提问</a>&nbsp;功能！";
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
					$('#list1').html('<ul></ul>');
					for (var i = 0; i < data.list.length; i++) {
						var queans = data.list[i];
						var answerList = '';
						for (var j = 0; j < data.list[i].answerList.length; j++) {
							//data.list[i].answerList[j].respContent = 
							var a =data.list[i].answerList[j].respContent.replace(/<[^>]+>/g,"" );/*    /(^\s*)|(\s*$)/g, "" */
							var answer = '<div><a style="font-size:15px;color: #666;" data-id="'+queans.id+'" href="javascript:void(0);" onclick="answerClick(this)">' + a.substring(0,100) + '</a></div>';
							answerList += answer;
						}
						$('#list1').find("ul").append('<li style="font-size:15px;color: #333;"><div>' + queans.content + answerList + '</div></li>');
					}
					$('#list1').append('<span class="fr hand change mt10" onclick="changePage(' + data.pageNo + ',' + data.last + ', this)">换一批</span>');
				}
			});
			$('.rightInn').scrollTop(0);
		}
		
		/*
		 * 问题词典换一批按钮
		 */
		function changePage11(pageNo, searchContent, thisList) {
			$('#list2').html('');
			$.post("${ctxf}/robot/chatSearchAll", {
				pageNo : pageNo,
				pageSize : 10,
				askText : searchContent
			}, function(data) {
				if (data.list == undefined) {
					$('#list2').html('');
					$('#list2').html('<div id="noRightList" class="noOnline">无查询结果</div>');
				} else {
					$('#list2').html('<ul></ul>');
					for (var i = 0; i < data.list.length; i++) {
						var queans = data.list[i];
						var type = queans.type;
						var content = queans.content;
						var link = queans.link;
						var title = queans.title;
						var id = queans.id;
						if(type == "qa"){
					//	content = $.trim(content);
							var answerList = '<div><a style="font-size:15px;color: #666;" href="javascript:void(0);" onclick="searchAllClick('+"'"+type+"'"+','+"'"+id+"'"+','+"'"+"'"+','+"'"+"'"+');">' + content.substring(0,100) + '</a></div>';
						}else if(type == "article") {
						title = title.replace('<font color="red">','');
						title = title.replace('</font>','');
							var answerList = '<div><a style="font-size:15px;color: #666;" href="javascript:void(0);" onclick="searchAllClick('+"'"+type+"'"+','+"'"+''+"'"+','+"'"+link+"'"+','+"'"+title+"'"+');">' + content.substring(0,35) + '</a></div>';
						}
						
						$('#list2').find("ul").append('<li style="font-size:15px;color: #333;"><div>' + queans.title + answerList + '</div></li>');
					}
					//var searchContent = searchContent.replace('"',"'");
					$('#list2').append('<span class="fr hand change mt10" onclick="changePage11(' + data.pageNo + ','  + "'" +searchContent + "'" + ', this)">换一批</span>');
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
		function qaDictionary() {
			var searchContent = $("#qaDictionaryInput").val();
			if (searchContent == "问题词典" || searchContent == "") {
				$("#list2").html("");
				return;
			}
			globalDictionarySearchContent = searchContent;
			changePage11(1,searchContent, $("#list2"));
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
					height : 75,
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
				var index = $('.rightTab li').index(this);
				$('.rightTab li').removeClass('on');
				$(this).addClass('on');
				$('.rightInn .list').hide().eq(index).fadeIn(300);
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
	</script>

</body>
</html>