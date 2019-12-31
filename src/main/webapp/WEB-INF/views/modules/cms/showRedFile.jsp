<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<style>

		.htwj_new{
			width: 888px;
			margin: 0 auto;
		}
		.htwj_header .htwj_n_tit{
			color: red;
			font-size: 44px;
			margin-top: 85px;
			line-height: 45px;
			text-align: center;
			font-family: 'SimSun';
			font-weight: bold;
		}
		.htwj_header .htwj_n_line{
			
			margin-top: 55px;
			border-bottom: 3px solid red;
		}
		.htwj_header .htwj_n_qfr{
			position: relative;
			color: #333;
			font-size: 18px;
			margin-top: 25px;
			line-height: 150%;
			text-align: center;
			font-family: 'SimSun';
		}
		.htwj_header .htwj_n_qfr .htwj_n_sign{
			position: absolute;
			right: 0px;
			top: 0px;
			color: #333;
			font-size: 18px;
			font-family: 'SimSun';
			line-height: 150%;
		}
		.htwj_body{
			position: relative;
			padding-bottom: 60px;
		}
		.htwj_body .htwj_n_tit{
			font-family: 'SimHei';
			color: #333;
			font-size: 28px;
			line-height: 200%;
			text-align: center;
			margin-top: 25px;
		}
		.htwj_body .htwj_n_detail {
			position: relative;
			color: #333;
			font-size: 20px!important;
			margin-top: 35px;
			line-height:  26.0pt!important;
			text-align: justify;
			text-justify: inter-ideograph;
			font-family: '仿宋_GB2312!important';
		}
		.htwj_body .htwj_n_detail p span {
			position: relative;
			color: #333;
			font-size: 20px!important;
			margin-top: 35px;
			line-height:  26.0pt!important;
			text-align: justify;
			text-justify: inter-ideograph;
			font-family: '仿宋_GB2312!important';
		}
		.htwj_body .htwj_n_detail ul,
		.htwj_body .htwj_n_detail li,
		.htwj_body .htwj_n_detail ol,
		.htwj_body .htwj_n_detail dl,
		.htwj_body .htwj_n_detail dd,
		.htwj_body .htwj_n_detail dt,
		.htwj_body .htwj_n_detail p,
		.htwj_body .htwj_n_detail h1,
		.htwj_body .htwj_n_detail h2,
		.htwj_body .htwj_n_detail h3,
		.htwj_body .htwj_n_detail h4,
		.htwj_body .htwj_n_detail h5,
		.htwj_body .htwj_n_detail h6,
		.htwj_body .htwj_n_detail form,
		.htwj_body .htwj_n_detail fieldset,
		.htwj_body .htwj_n_detail legend,
		.htwj_body .htwj_n_detail img,
		.htwj_body .htwj_n_detail div {
			position: relative;
			color: #333;
			font-size: 20px!important;
			margin-top: 35px;
			line-height:  26.0pt!important;
			text-align: justify;
			text-justify: inter-ideograph;
			font-family: '仿宋_GB2312!important';
		}
		.htwj_body .htwj_n_detail,
		.htwj_body .htwj_n_detail p,
		.htwj_body .htwj_n_detail div {
			text-align: justify;
			text-justify: inter-ideograph;
			text-indent: 50px;
		}
		.htwj_body .htwj_n_detail img,
		.htwj_body .htwj_n_detail table {
			width: 100%!important;
		}
		.htwj_body .htwj_n_detail p {
			margin: 1.5em 0;
		}
		.htwj_body .htwj_attachment {
			position: relative;
			padding-left: 98px;
			margin-top: 35px;
		}
		.htwj_body .htwj_attachment .htwj_attach_tit {
			position: absolute;
			left: 0px;
			top: 0px;
			height: 28px;
			font-size: 15px;
			color: #333;
			line-height: 200%;
			font-family: '仿宋_GB2312';
			padding-left: 28px;
			background-repeat: no-repeat;
			background-position: left center;
			background-image: url('images/icon_file.png');
		}
		.htwj_body .htwj_attachment .htwj_attach_con > a {
			transition: all 0.05s linear 0s;
			-moz-transition: all 0.05s linear 0s;
			-webkit-transition: all 0.05s linear 0s;
			-o-transition: all 0.05s linear 0s;
			font-size: 18px;
			color: #0059ab;
			line-height: 200%;
			font-family: '仿宋_GB2312';
		}
		.htwj_body .htwj_attachment .htwj_attach_con > a:hover {
			text-decoration: underline;
			color: #006ed4;
		}
		.htwj_body .htwj_n_sign{
			position: absolute;
			right: 0px;
			color: #333;
			width: 200px;
			font-size: 20px;
			margin-top: 2px;
			line-height: 200%;
			text-align: center;
			font-family: '仿宋_GB2312';
		}
		.htwj_n_end{
			margin-top: 125px;
			line-height: 200%;
		}
		.htwj_n_end .htwj_n_key{
			font-size: 20px;
			color: #333;
			font-family: 'SimHei';
		}
		.htwj_n_end .htwj_n_key span{
			margin: 0px 5px;
		}
		.htwj_n_end .htwj_ne_line{
			margin-top: 10px;
			    border-bottom: 1px solid #999;
		}
		.htwj_n_end .htwj_n_respon,.htwj_n_end .htwj_n_send,.htwj_n_end .htwj_n_num{
			position: relative;
			font-size: 18px;
			color: #333;
			line-height: 200%;
			font-family: '仿宋_GB2312';
		}
		.htwj_n_end .htwj_n_respon{
			margin-top: 10px;
		}
		.htwj_n_end .htwj_n_num .all_num{
			position: absolute;
			top: 0px;
			right: 0px;
		}
	</style>
<style type="text/css" media="print">
	.noprint {
		display: none;
	}
</style>
</head>

<body  onload="setPrintPara(true,1,45);">
	<object id="factory" style="display: none" viewastext classid="clsid:1663ED61-23EB-11D2-B92F-008048FDD814">
	</object>
	<OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id=WebBrowser width=0>
	</OBJECT>
	<div class="wraper">
		<div class="htwj_new">
			<div class="htwj_header" style="margin-top:200px;">
				<!-- <div class="htwj_n_tit">中国政法大学文件</div>
				<div class="htwj_n_line"></div> -->
				<div class="htwj_n_qfr">
					校教字[<span>${article.schoolWord}</span>]第<span>${article.code}</span>号
					<div class="htwj_n_sign">签发：<span>${article.signIssue}</span></div>
				</div>
			</div>
			<div class="htwj_body">
				<div class="htwj_n_tit">${article.title}</div>
				<div class="htwj_n_detail">
					${article.articleData.content}
				</div>
				<!-- <div class="htwj_attachment">
					<div class="htwj_attach_tit">附件：</div>
					<div class="htwj_attach_con">
						<a href="#" download>某某某某某</a><br>
						<a href="#" download>某某某某某</a><br>
						<a href="#" download>某某某某某</a><br>
						<a href="#" download>某某某某某</a><br>
						<a href="#" download>某某某某某</a><br>
					</div>
				</div>
				<div class="htwj_n_sign">
					教务处<br>
					二〇一九年五月十七日
				</div> -->
				<div class="htwj_n_sign">
					${article.section}<br>
					${article.releasetime}
				</div>
			</div>
			<div class="htwj_n_end">
				<div class="htwj_n_key">
					主题词：${article.themeStatement}
				</div>
				<div class="htwj_ne_line"></div>
				<div class="htwj_n_respon">
					报：<span>${article.report}</span>
				</div>
				<div class="htwj_n_send">
					发：<span>${article.send}</span>
				</div>
				<div class="htwj_ne_line"></div>
				<div class="htwj_n_num">
					<div class="save_num">存档<span>2</span>份</div>
					<div class="all_num">共<span>${article.copies}</span>份</div>
				</div>
			</div>
		</div>
	</div>
	<div class="noprint">
	    <div style="text-align: center;"> 
		<input id="btnSubmit" onclick="toPrint()" style="    margin-left: -150px;height: 32px;color: #2196f3;font-size: 16px;-webkit-transition: all .22s linear 0s;margin: 39px 20px;padding-left: 22px;padding-right: 22px;border: 1px solid #2196f3;" class="btn btn-primary noprint" type="button" value="打印"/>&nbsp;
		</div>
	</div>
	<script defer>
//打印机设置
function toPageSetup(){
	if(factory.object)
		factory.printing.PageSetup();
	else
		document.all.WebBrowser.ExecWB(8,1);
}

//打印预览
function toPreview(){
	if(factory.object){
		//页眉 
		factory.printing.header = "";
		//页脚 
		factory.printing.footer = "";
		factory.printing.Preview();
	}else{
		document.all.WebBrowser.ExecWB(7,1);
	}
}


/*======================================
* 功能：打印
* 参数：isShowSetup 是否显示打印设置
======================================*/
function toPrint(isShowSetup){
	if(factory.object){
		factory.printing.Print(isShowSetup);
	}else{
		window.print();		
	}
}

/*=======================================================
* 功能：
* 使用scriptX设置打印属性：页眉、页脚、页边距、纸张方向
* 
* 参数：
* _portrait     纸张方向
* _leftMargin   左边页边距
* _topMargin    上边页边距
========================================================*/
function setPrintPara(_portrait,_leftMargin,_topMargin){
	if (factory.object) {
	 //页眉 
		factory.printing.header = "";
		//页脚 
		factory.printing.footer = "";
		factory.printing.portrait = _portrait;
		factory.printing.leftMargin = _leftMargin;
		factory.printing.topMargin = _topMargin;
		factory.printing.rightMargin = 1;
		factory.printing.bottomMargin = 1;
	}
}
</script>
</body>
</html>