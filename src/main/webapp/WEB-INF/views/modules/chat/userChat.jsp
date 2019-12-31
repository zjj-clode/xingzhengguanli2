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
<meta name="viewport" content="width=1440">
<meta http-equiv="X-UA-Compatible" content="edge"/>
<link rel="stylesheet" href="${ctxStatic}/lte/plugins/kindeditor/themes/simple/simple.css" />
<link rel="stylesheet" type="text/css" href="${ctxStaticFront}/basic/css/custom.min.css"/>
<script src="${ctxStatic}/pushlet/ajax-pushlet-client.js" type="text/javascript"></script>
<script charset="utf-8" src="${ctxStatic}/lte/plugins/kindeditor/kindeditor-all-min.js"></script>
<script charset="utf-8" src="${ctxStatic}/lte/plugins/kindeditor/lang/zh_CN.js"></script>
<script src="${ctxStatic}/jquery/j1.js"></script>
<!--[if lt IE 9]>
    <script type="text/javascript" src="${ctxStaticFront}/js/html5shiv.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery/j1.js"></script>
<![endif]-->
</head>

<body>

<div id="wrapper">
        <div class="header">
            <div class="l_con">
                <div class="logo">
                    <a href="${fns:getConfig('custmor.site')}">
	                    <img src="${ctxStaticFront}/basic/images/robot/logo.png" alt="" />
                    </a>
                    <p class="l_txt">招生在线智能咨询</p>
                </div>
                <ul class="options">
                    <li class="zn" onclick="window.location='${ctxf}/robot/forwardRobot'">智能咨询</li>
                    <li class="rg active">人工咨询</li>
                </ul>
            </div>
        </div>
        <div class="robotWrap">
            <div class="r_left">
                <div class="l_header">
                    <img src="${ctxStaticFront}/basic/images/robot/robot.png" alt="" class="robot" />
                    <p class="service">${fns:getConfig('robot.name')}</p>
                </div>
                <!-- 对话内容区 -->
                <div class="chatBody">
                    <div class="chatInn chatInn1">
                        
                    </div>
                </div>
                <!--对话结束-->
                <!-- 输入内容区 -->
                <div class="askBody">
                    <div class="askInn">
                        <textarea  class="chat_input" id="askText" name="askText"></textarea>
                        <div class="btns">
                            <p class="btn">
                                <span class="send" id="sendBtn">发送</span>
                            </p>
                        </div>
                    </div>
                </div>              
            </div>
            <div class="r_right">
                <ul class="rightTab">
                    <li class="fl on">常见问题</li>
                    <li class="fl">投诉建议</li>
                </ul>
                <div class="rightInn">
                    <div class="tab_con main_quest">
                        <p class="tips">您好，请咨询学校相关部门，对相关问题进行提问！</p>
                        <ul class="list" id="list1">
                            <c:set var="page" scope="session" value="${fnc:getQuePage(10,'')}" />
	                        <c:forEach items="${page.list}" var="queans">
	                            <li><a href="javascript:void(0);" data-id="${queans.id}" onclick="rightQuestionLiClick(this)">${queans.content }</a></li>
	                        </c:forEach>
                        </ul>
                    </div>
                    <div class="tab_con sugg" style="display:none;">
                        <p class="tips">您好，请咨询学校相关部门，对相关问题进行提问！</p>
                        <div>
                            <textarea id="suggestionContent" placeholder="请输入投诉信息（4到100字）"></textarea>
                            <div class="sugg_tip">输入文字不能多于<span id="ts">100</span>个字</div>
                            <button class="btn_sugg" onclick="suggestionSubmit()">投诉</button>
                        </div>
                    </div>
                </div>
                <div class="change btn_main_quest" id="huanyipi"><span onclick="changePage(${page.pageNo},${page.last})">换一批</span></div>
                <div class="yz_logo">
                    <a href="#">
                        <img draggable="false" src="${ctxStaticFront}/basic/images/robot/logo_o.png">
                    </a>
                    <a href="#" class="suport">
                                                                  技术支持：北京云智小橙科技有限公司
                    </a>
                </div>
            </div>
        </div>
    <div>
    <script type="text/javascript" src="${ctxStaticFront}/basic/js/jquery.placeholder.min.js"></script>
    <script type="text/javascript">
        $(function(){
            $('textarea').placeholder({isUseSpan:true});
        });     
    </script>
    <script type="text/javascript">
        // 右侧tab切换
        $('.rightTab li').bind('click',function(){
            var index = $('.rightTab li').index(this);
            if(index === 0){
                $('.btn_main_quest.change').show();
            }else{
                $('.btn_main_quest.change').hide();
            }
            $('.rightTab li').removeClass('on');
            $(this).addClass('on');
            $('.rightInn .tab_con').hide().eq(index).fadeIn(400);
        });
    </script>

<script type="text/javascript">
	var targetId = "${customId}";
	var targetPhoto = "${customPhoto}";
	var userId = '${userId}';
	var userPhoto = '${userPhoto}';
	
	var editor;
	
	if(targetPhoto=="") {
		targetPhoto = '${ctxStaticFront}/basic/images/robot/r_icon_06.png';
	}else {
		targetPhoto = "${root}"+targetPhoto;
	}
	if(userPhoto=="") {
		userPhoto = '${ctxStaticFront}/basic/images/robot/r_icon_27.png';
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
			$.post("${ctxf}/qa/suggestion/save", {"content":content},
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
		var str = '<dl class="answer link"><dt><img src="' + targetPhoto + '" alt=""></dt><dd><img src="${ctxStaticFront}/basic/images/robot/img_b1.png" class="jiao" alt=""><div class="word">';
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
		var str = '<dl class="answer link writing"><dt><img src="' + targetPhoto + '" alt=""></dt><dd><img src="${ctxStaticFront}/basic/images/robot/img_b1.png" class="jiao" alt=""><div class="word">';
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
		$("dl.answer > dd > div.word:last").html(content);
		$("dl.answer").removeClass("writing");
	}

	//在对话内容区域添加己方消息
	function appendSelfMsg(content, before) {
		var str = '<dl class="ask"><dt><img src="'+userPhoto+'" " alt=""></dt><dd><img src="${ctxStaticFront}/basic/images/robot/img_b2.png" class="jiao" alt=""><div class="word">';
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
				$(".chatInn1").prepend('<div id="historyMsgTag" class="see_more"><a href="javascript:void(0);" onclick="queryHistorys(10)">查看更多消息</a></div>');
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
			$("#huanyipi").append('<span onclick="changePage('+data.pageNo+','+data.last+')">换一批</span>');
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
			   url: "${ctxf}/robot/userAskTips",
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
                height:160,
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
			
		//setTimeout(hello, 100);
			
		initKindeditor();
		
		//若无更多消息，把hasMoreMsg设为false
		$.getJSON("userMsgs", {toUserId:targetId, topMsgDate: topMsgDate, size: 1}, function(msgs){
			if(msgs.length == 0) {
				hasMoreMsg = false;
			}
		});
		
		if(hasMoreMsg) {
			$(".chatInn1").prepend('<div id="historyMsgTag" class="see_more"><a href="javascript:void(0);" onclick="queryHistorys(10)">查看更多消息</a></div></div>');
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