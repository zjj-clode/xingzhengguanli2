<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include
	file="/WEB-INF/views/include/taglib.jsp"%>
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
<link rel="stylesheet" href="${ctxStatic}/lte/plugins/kindeditor/themes/simple/simple.css" />
<script src="${ctxStatic}/jquery/j1.js"></script>
<script src="${ctxStatic}/front/robot/js/html5.js"></script>
<script src="${ctxStatic}/pushlet/ajax-pushlet-client.js" type="text/javascript"></script>
<script charset="utf-8" src="${ctxStatic}/lte/plugins/kindeditor/kindeditor-all-min.js"></script>
<script charset="utf-8" src="${ctxStatic}/lte/plugins/kindeditor/lang/zh_CN.js"></script>
<!-- <style type="text/css">
	.small {
		width: 700px; 
		margin-left: -350px
	}
	.sysem_msg {
		text-align: center;
		color:#999;
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
</style> -->
</head>

<body>

<div id="wrapper" class="small robotSer">
	<div class="header pr">
		<span class="robot pa">
			<%--<c:choose>
				 <c:when test="${not empty customPhoto}"><img src="${root}${customPhoto}" class="vt" alt="" width="82" height="97"></c:when> --%>
				<%-- <c:when test="${not empty fns:getUserById(customId).photo}"><img src="${root}${fns:getUserById(customId).photo}" class="vt" alt="" width="82" height="97"></c:when> --%>
				<%-- <c:otherwise> --%><img src="${ctxStatic}/front/robot/images/custom.png" class="vt" alt=""><%-- </c:otherwise>
			</c:choose> --%>
		</span>
		<h1>${fns:getConfig('copyrightUnit')} | 招生在线智能咨询<span>24小时为您提供优质服务</span></h1>
		<div class="tools pa clear">
			<p class="fl tc hand" id="winCtrl"><img src="${ctxStatic}/front/robot/images/ico_expand.png" alt=""></p>
			<p class="fl tc hand" id="winClose"><img src="${ctxStatic}/front/robot/images/ico_close2.png" alt=""></p>
		</div>
		<ul class="options pa clear c2">
			<li class="fl zn" onclick="window.location='${ctx}/robot/forwardRobot'">智能咨询</li>
			<li class="fl rg">人工咨询</li>
		</ul>
	</div>
	<div class="mainBody clear">
		<!-- 左侧部分 -->
		<div class="l_left">
			<!-- 对话内容区 -->
			<div class="chatBody pa">
				<div class="chatInn f14 chatInn1">
					<!-- 动态添加聊天记录区 -->
					<!-- <p class="time">14:40</p> -->
					<!-- <dl class="answer pr mb20 clear">
						<dt class="pa"><img src="${ctxStatic}/front/robot/images/avatar_robot.png" alt=""></dt>
						<dd class="pr fl">
							<img src="${ctxStatic}/front/robot/images/img_b1.png" class="pa jiao" alt="">
							<p class="word">您好，这里是智能答疑便捷咨询平台，如果您有什么要咨询的，请尽量用准确和简洁的语言描述您的问题！</p>
						</dd>
					</dl>
					<dl class="ask pr mb20 clear">
						<dt class="pa"><img src="${ctxStatic}/front/robot/images/avatar_default.png" alt=""></dt>
						<dd class="pr fr">
							<img src="${ctxStatic}/front/robot/images/img_b2.png" class="pa jiao" alt="">
							<p class="word">主要联系方式是什么？</p>
						</dd>
					</dl> -->
				</div>
			</div>
			<!-- 输入内容区 -->
			<div class="askBody pa">
				<div class="list_box" style="display:none;">
					<ul class="keywords_list">
						<!-- <li>请问今年学校<em>招生人数</em>是多少?</li> -->
						<li>请问今年学校<em>招生人数</em>是多请问今年学请问今年学请问今年学请问今年学少?</li>
					</ul>
					<img src="${ctxStatic}/front/robot/images/j_klist.png" class="jiao" alt="">
				</div>
				<div class="askInn" style="padding: 0px 0px;">
					<!-- <span id="historyBtn" onclick="queryHistorys(10)" style="position: absolute;right: 10px;color:#666;top:4px;font-size:10px;cursor:pointer;">消息历史</span> -->
					<textarea name="askText" id="askText" class="f14" placeholder="请输入聊天内容"></textarea>
					<div class="clear bot">
						<p class="fl tip">输入字数不能超过100个汉字</p>
						<p class="btn f16 fr">
							<span id="closeBtn" class="hand close ib mr15" onclick="closeWindow()">关闭</span>
							<a><span id="sendBtn" class="hand send ib">发送</span></a>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!-- 右侧部分 -->
		<div class="l_right">
			<div class="rightPart fr">
				<div>
					<ul class="rightTab clear f16 tc">
						<li class="fl on">常见问题</li>
						<li class="fl">使用帮助</li>
						<li class="fl">投诉建议</li>
					</ul>
					<div class="rightInn f14 clear">
						<p class="tips">
							您好，请咨询学校相关部门，对相关问题进行提问！
						</p>
						<!-- bbb -->
						<div id="list1" class="list">
							<ul>
								<c:set var="page" scope="session" value="${fnc:getQuePageWithAnswer(0,10,false,'')}" />
								<c:forEach items="${page.list}" var="queans">
									<li><div><a href="javascript:void(0);" onclick="rightQuestionLiClick(this)">${queans.content}</a></div></li>
								</c:forEach>
							</ul>
						</div>
	
						<div id="list2" class="list" style="display:none">
							
						</div>
						<div id="list3" class="list" style="display:none">
							<div class="toushu">
								<textarea id="suggestionContent" placeholder="请输入投诉信息4到100个字" maxlength="100"></textarea>
								<div class="f-tips">输入文字不能多于<span style="color:#ff7e00" id="ts">100</span>个字</div>
								<div class="btns">
									<button class="btn_confirm" onclick="suggestionSubmit()">投诉</button>
								</div>
							</div>
						</div>
					</div>
					<div id="huanyipi">
						<span class="fr hand change mt10 change_1" onclick="changePage(${page.pageNo},${page.last})">换一批</span>
					</div>
				</div>
			</div>	
			<div class="yz_logo">
				<a href="#">
					<img draggable="false" src="${ctxStatic}/front/robot/images/yz_logo_big.png"/>
				</a>
				<a href="#" class="suport">
					技术支持：北京云智小橙科技有限公司
				</a>
			</div>		
		</div>
	</div>	
</div>
<div class="footer tc f14">Copyright &copy; 2004-2016 ${fns:getConfig('copyrightUnit')}招生办公室 版权所有</div>

<script type="text/javascript">
	var targetId = "${customId}";
	var targetPhoto = "${customPhoto}";
	var userId = '${userId}';
	var userPhoto = '${userPhoto}';
	
	var editor;
	
	if(targetPhoto=="") {
		targetPhoto = "${ctxStatic}/front/robot/images/custom.png";
	}else {
		targetPhoto = "${root}"+targetPhoto;
	}
	if(userPhoto=="") {
		userPhoto = '${ctxStatic}/front/robot/images/avatar_default.png';
	}else {
		userPhoto = "${root}"+userPhoto;
	}

	function init() {
		PL.joinListen('/onLineCustom');
	}

	function onData(event) {
		try {
			//console.log(event);
			/* var content = decodeURI(event.get('content'));
			content = decodeURI(content);
			console.log(content); */
			var msgId = event.get("msgId");
			$.get("${ctx}/chat/getContent?id="+msgId, function(chatMsg){
				if($("div.chatBody dl.writing").size()<1){
					// 
					appendTargetMsg(chatMsg.content);
				}else{
					// 替换“正在输入中。。。”为对方发过来的真实的内容
					replaceTargetWriting(chatMsg.content);
				}
			});
			
		} catch (e) {
			console.log(e);
		}
	}
	
	function onError(event){
		//console.log("onerror reinit");
		PL._init(); 
		init();
	}

	function sendMsg(content) {
		$.post("${ctx}/chat/sendMsg", {
			type : "1",
			content : content,
			toUserId : targetId
		}, function(result) {
		});
	}
	//提交投诉建议
	function suggestionSubmit() {
			var content = $.trim($("#suggestionContent").val());
			if(content.length<4 || content.length>100){
				alert("请填写咨询内容，4至100个字。");
				$("#content").focus();
				return ;
			}
			$.post("${ctx}/qa/suggestion/save", {"content":content},
		   		function(json){
		     		if(json.success==true){
		     			$("#suggestionContent").val("");
		     			$('#ts').text(100);
		     		}
		     		alert(json.message);
		   		}
			, "json");
	}
	
	function hello() {
		appendTargetMsg('欢迎您使用${fns:getConfig('copyrightUnit')}智能咨询系统，请在输入框中输入您要咨询的问题！');
	}

	//在对话内容区域添加对方消息
	function appendTargetMsg(content, before) {
		var str = '<dl class="answer pr mb20 clear" style="word-wrap:break-word;"><dt class="pa"><img src="' + targetPhoto + '" alt=""></dt><dd class="pr fl"><img src="${ctxStatic}/front/robot/images/img_b1.png" class="pa jiao" alt=""><div class="word" style="max-width: 510px;">';
		str += content + '</div></dd></dl>';
		if(before) {
			$(".chatInn1").prepend(str);
			$('.chatBody').scrollTop(0);
		} else {
			$(".chatInn1").append(str);
			$('.chatBody').scrollTop( $('.chatBody')[0].scrollHeight );
		}
	}
	
	//在对话内容区域添加对方“正在输入。。。”
	function appendTargetWriting(content, before) {
		var str = '<dl class="answer pr mb20 clear writing" style="word-wrap:break-word;"><dt class="pa"><img src="' + targetPhoto + '" alt=""></dt><dd class="pr fl"><img src="${ctxStatic}/front/robot/images/img_b1.png" class="pa jiao" alt=""><div class="word" style="max-width: 510px;">';
		str += content + '</div></dd></dl>';
		if(before) {
			$(".chatInn1").prepend(str);
			$('.chatBody').scrollTop(0);
		} else {
			$(".chatInn1").append(str);
			$('.chatBody').scrollTop( $('.chatBody')[0].scrollHeight );
		}
	}
	
	// 替换“正在输入中。。。”为真实的对方发过来的内容
	function replaceTargetWriting(content) {
		// 找到 “正在输入中。。。”，替换
		$("dl.answer > dd.pr > div.word:last").html(content);
		$("dl.answer").removeClass("writing");
	}

	//在对话内容区域添加己方消息
	function appendSelfMsg(content, before) {
		var str = '<dl class="ask pr mb20 clear" style="word-wrap:break-word;"><dt class="pa"><img src="'+userPhoto+'" alt=""></dt><dd class="pr fr"><img src="${ctxStatic}/front/robot/images/img_b2.png" class="pa jiao" alt=""><div class="word" style="max-width: 510px;">';
		str += content + '</div></dd></dl>';
		if(before) {
			$(".chatInn1").prepend(str);
			$('.chatBody').scrollTop(0);
		} else {
			$(".chatInn1").append(str);
			$('.chatBody').scrollTop( $('.chatBody')[0].scrollHeight );
			editor.html("");
			editor.focus();
		}
	}
	
	//添加系统消息
	function appendSystemMsg(content, before){
		var str = '<div class="sysem_msg">'+ content + '</div>';
		if(before) {
			$(".chatInn1").prepend(str);
			$('.chatBody').scrollTop(0);
		} else {
			$(".chatInn1").append(str);
		}
	}
	
	//控制输入字数
	function checkLetterNum(){
		var len = $.trim(editor.text()).length;
		var maxLen = 100;
		if(len>=maxLen){
			var str = editor.text();
			editor.text("");
			editor.appendHtml(str.substring(0,100));
			$("div.askInn > div > p.tip").html("输入字数不能超过"+maxLen+"个字，<font color='red'>字数已满</font>。");
		}else if(len+10>=maxLen&&len<maxLen){
			$("div.askInn > div > p.tip").html("输入字数不能超过"+maxLen+"个字，还能输入<font color='red'> "+(maxLen-len)+" </font>个字。");
		}else{
			$("div.askInn > div > p.tip").html("输入字数不能超过"+maxLen+"个字");
		}
	}

	//点击发送按钮
	function send() {
		$('.list_box').hide();
		//var content = $.trim(editor.text().replace("<br />", ""));
		var content = $.trim(editor.html());
		if(editor.text() == ""){	
			alert("发送内容不能为空,请输入内容");
		}else{
			appendSelfMsg(content);
			sendMsg(content);
			
			// 显示对方正在输入中。。。
			if($("div.chatBody dl.writing").size()<1){
				setTimeout(function() {
					appendTargetWriting("正在输入中。。。",false);
				}, 500);
			}else{ // 如果对方还未回复，把“正在输入中”挪到最后
				$("div.chatBody dl.writing").remove();
				appendTargetWriting("正在输入中。。。",false);
			}
			
			checkLetterNum();
		}
	}
	
	var topMsgDate = "";
	var hasMoreMsg = true;
	/*
	 * 查询历史消息
	*/
	function queryHistorys(size){
		if(!hasMoreMsg){
			return;
		}
		
		var bottom=$('.chatInn').outerHeight()-$('.chatBody').scrollTop();
		$.getJSON("userMsgs", {toUserId:targetId, topMsgDate: topMsgDate, size: size}, function(msgs){
			$("#historyMsgTag").remove();
			if(msgs.length == 0) {
				hasMoreMsg = false;
				//appendSystemMsg("没有更多消息了", true);
			} else {
				for(var i = 0; i < msgs.length; i++){
					var msg = msgs[i];
					if(i == msgs.length - 1) {
						topMsgDate = msg.createDate;
					}
					if(msg.sender == '1'){
						appendSelfMsg(msg.content, true);
					} else {
						appendTargetMsg(msg.content, true);
					}
				}
				$(".chatInn1").prepend('<div id="historyMsgTag"><span onclick="queryHistorys(10)">查看更多消息</span></div>');
				if(msgs.length < size) {
					hasMoreMsg = false;
					$("#historyMsgTag").remove();
					//appendSystemMsg("没有更多消息了", true);
				}
				
				var val=$('.chatInn').outerHeight()-bottom;
				$('.chatBody').scrollTop(val);
			}
		});
		
	}
	
	/*
	 * 换一批按钮
	 */
	function changePage(pageNo, last) {
		$.post("getQuePageWithAnswer", {
			pageNo : pageNo,
			totalPageNum : last,
			number : 10,
			withAnswer : false
		}, function(data) {
			$("#list1").html('<ul></ul>');
			for (var i = 0; i < data.list.length; i++) {
				var queans = data.list[i];
				$("#list1 ul").append('<li><div><a href="#" onclick="rightQuestionLiClick(this)">'+queans.content+'</a></div></li>');
			}
			$("#huanyipi").html('');
			$("#huanyipi").append('<span class="fr hand change mt10 change_1" onclick="changePage('+data.pageNo+','+data.last+')">换一批</span>');
			//$("#list1").append('<span class="fr hand change mt10" onclick="changePage('+data.pageNo+','+data.last+')">换一批</span>');
		});
		$('.rightInn').scrollTop(0);
	}

	//给右侧提示添加点击事件
	function rightQuestionLiClick(a){
		var askTemp = $(a).text();
		editor.html(askTemp);
		send();
	}
	
	//在Kindeditor中输入文字显示自动提示文字
	function editorKeyup(){
		var keywords = $.trim(editor.html());
		keywords = keywords.replace("<br />",""); // 去掉br
		if(keywords!=null&&keywords!="") {
			$.ajax({
			   type: "POST",
			   url: "${ctx}/robot/userAskTips",
			   data: {"askText":keywords},
			   dataType : "json",
			   success: function(dataObj){
					//console.log(dataObj);
				 	//每次触发事件清空原有的补全信息
					$(".keywords_list").html("");
					if(dataObj.length>0){
						var strs = "";
						$.each(dataObj,function(i,n){
							strs += "<li class='tips'>"+n.title+ "</li>";
						});
						//将自动补全查询的信息显示与自动补全div中
						$(".keywords_list").html(strs);
						$('.list_box').show();
					}else{
						$('.list_box').hide();
					}
			   }
			});
		}
	}
	
    function initKindeditor() {

        KindEditor.ready(function(K) {
            editor = K.create('textarea[name="askText"]', {
                width : '100%',
                resizeType : 0,
                allowPreviewEmoticons : false,
                allowImageUpload : false,
                height:75,
                minHeight:75,
                newlineTag:'br',
                items : [
					'bold','underline','fontname','fontsize','forecolor','hilitecolor','link','emoticons'
				],
				afterCreate : function() {//设置编辑器创建后执行的回调函数
		            var self = this;
				
		            $(self.edit.doc).bind("keydown",function (event) {
		                if (event.keyCode == 13) {
		                	send();
		                }
		            });
		            
		            $(self.edit.doc).bind("keyup",function (event) {
		                editorKeyup();
		            });
		            
		            $(self.edit.doc).bind("input propertychange",function (event) {
		          
		            	checkLetterNum();
		            });
		        }
		        
            });
            
            
        });
    }
    
    function closeWindow(){
			var userAgent = navigator.userAgent;
			if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
				window.location.replace("about:blank");
				window.close();
			}
			else {
				window.opener = null;
				window.open("", "_self");
				window.close();
			}
	}
	    
	$(function(){
		init();
			
		setTimeout(hello, 100);
			
		initKindeditor();
		
		//若无更多消息，把hasMoreMsg设为false
		$.getJSON("userMsgs", {toUserId:targetId, topMsgDate: topMsgDate, size: 1}, function(msgs){
			if(msgs.length == 0) {
				hasMoreMsg = false;
			}
		});
		
		if(hasMoreMsg) {
			$(".chatInn1").prepend('<div id="historyMsgTag"><span onclick="queryHistorys(10)">查看更多消息</span></div>');
		}

		$("#sendBtn").bind('click', function() {
			send();
		});
		
		//自动补全div隐藏
		$('.list_box').hide();
		
		//给右侧提示添加点击事件
		/* $(".rightInn ul").on('click','li',function(){
			var askTemp = $(this).text();
			//alert(askTemp);
			editor.html(askTemp);
			send();
		}); */
		
		//给（关键字提示列表中的列）动态添加的p标签绑定点击事件
		$(".keywords_list").on('click','li',function(){
			var askTemp = $(this).text();
			//$("#askText").val(askTemp);
			editor.html(askTemp);
			$('.list_box').hide();
			send();
		});

		//点击回车键,发送用户输入信息
		/*  $("#askText").keydown(function(e) {
			if (e.keyCode == 13) {
				//e.preventDefault();
				send();
			}
		}); */
	
		// 右侧tab切换
		$('.rightTab li').bind('click',function(){
			var index = $('.rightTab li').index(this);
			$('.rightTab li').removeClass('on');
			$(this).addClass('on');
			if(index != 0){
				$('#huanyipi').hide();
			}else if(index == 0){
				$('#huanyipi').show();
			}
			$('.rightInn .list').hide().eq(index).fadeIn(300);
		});
		// 放大缩小窗口
		$('#winCtrl').bind('click',function(){
			if($('#wrapper').hasClass('small')){
				$('#wrapper').removeClass('small')
				$('#wrapper').addClass('big');
				$(this).find('img').attr('src','${ctxStatic}/front/robot/images/ico_collapse.png');
			}
			else{
				$('#wrapper').removeClass('big')
				$('#wrapper').addClass('small');
				$(this).find('img').attr('src','${ctxStatic}/front/robot/images/ico_expand.png');
			}
		});
		// 关闭窗口
		$('#winClose').bind('click',function(){
			var userAgent = navigator.userAgent;
			if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
				window.location.replace("about:blank");
				window.close();
			}
			else {
				window.opener = null;
				window.open("", "_self");
				window.close();
			}
		});
	})
	
</script>

</body>
</html>