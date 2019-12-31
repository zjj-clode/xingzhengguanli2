<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!doctype html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>升级您的浏览器</title>
<link rel="stylesheet" type="text/css" href="browser.css" />
</head>
<body style="background-color:#f6f1ee;">
	<div id="wrapper" style="height:100%;">
		<div class="no-exist-top">
			<div class="mid-box">
				<h2>
					<img src="nt.jpg" />浏览器版本太低
				</h2>
				<p class="tip">为保障正常操作和浏览效果，请升级您的浏览器</p>
				<a href="https://support.microsoft.com/en-us/help/17621/internet-explorer-downloads"><img src="ie.jpg" />
					<p>IE浏览器</p></a> <a href="http://www.google.cn/chrome/browser/desktop/index.html"><img src="chrome.jpg" />
					<p>谷歌浏览器</p></a> <a href="https://browser.360.cn/se/"><img src="360.jpg" />
					<p>360浏览器</p></a> <a href="https://www.mozilla.org/zh-CN/firefox/new/?utm_medium=referral&utm_source=firefox-com"><img src="firefox.jpg" />
					<p>火狐浏览器</p></a>
			</div>
		</div>
		<div class="more-browser">
			<a href="https://browsehappy.com">更多浏览器下载</a>
		</div>
		<div class="footer">
			<img src="logo_cp.jpg" /><a href="">版权所有：${fns:getConfig('copyrightUnit')}</a><a href="">技术支持：北京云智小橙科技有限公司</a>
		</div>
	</div>
	</body>
</html>
