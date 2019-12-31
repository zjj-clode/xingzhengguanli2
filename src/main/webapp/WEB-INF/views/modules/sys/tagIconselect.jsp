<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>图标选择</title>
<meta name="decorator" content="lteblank" />
<style type="text/css"> 
  .highlight {background-color:green;color:#fff;} 
</style> 
<script type="text/javascript">
	$(document).ready(function() {
		$("#icons div").click(function() {
			$("#icons div").removeClass("active");
			$(this).addClass("active");
			$("#icon").val($(this).text());
		});
		$("#icons div").each(function() {
			if ($(this).text() == "${value}") {
				$(this).click();
			}
		});
		$('.fontawesome-icon-list .fa-hover').dblclick(function() {
			top.$.jBox.getBox().find("button[value='ok']").trigger("click");
		});
	});
</script>
</head>
<body class="hold-transition skin-purple sidebar-mini">
	<div class="pad15">

		<input type="hidden" id="icon" value="${value}" />

		<div class="form-group">
			<label for="">关键词：</label> 
			<input type="text" class="form-control" id="key" placeholder="请输入关键词搜索" autocomplete="off" style="width:300px;display:inline-block;">
		</div>

		<div class="row fontawesome-icon-list" id="icons">

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-address-book" aria-hidden="true"></i> fa-address-book
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-address-book-o" aria-hidden="true"></i> fa-address-book-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-address-card" aria-hidden="true"></i> fa-address-card
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-address-card-o" aria-hidden="true"></i> fa-address-card-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-adjust" aria-hidden="true"></i> fa-adjust
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-american-sign-language-interpreting" aria-hidden="true"></i> fa-american-sign-language-interpreting
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-anchor" aria-hidden="true"></i> fa-anchor
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-archive" aria-hidden="true"></i> fa-archive
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-area-chart" aria-hidden="true"></i> fa-area-chart
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-arrows" aria-hidden="true"></i> fa-arrows
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-arrows-h" aria-hidden="true"></i> fa-arrows-h
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-arrows-v" aria-hidden="true"></i> fa-arrows-v
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-asl-interpreting" aria-hidden="true"></i> fa-asl-interpreting <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-assistive-listening-systems" aria-hidden="true"></i> fa-assistive-listening-systems
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-asterisk" aria-hidden="true"></i> fa-asterisk
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-at" aria-hidden="true"></i> fa-at
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-audio-description" aria-hidden="true"></i> fa-audio-description
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-automobile" aria-hidden="true"></i> fa-automobile <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-balance-scale" aria-hidden="true"></i> fa-balance-scale
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-ban" aria-hidden="true"></i> fa-ban
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bank" aria-hidden="true"></i> fa-bank <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bar-chart" aria-hidden="true"></i> fa-bar-chart
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bar-chart-o" aria-hidden="true"></i> fa-bar-chart-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-barcode" aria-hidden="true"></i> fa-barcode
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bars" aria-hidden="true"></i> fa-bars
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bath" aria-hidden="true"></i> fa-bath
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bathtub" aria-hidden="true"></i> fa-bathtub <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery" aria-hidden="true"></i> fa-battery <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-0" aria-hidden="true"></i> fa-battery-0 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-1" aria-hidden="true"></i> fa-battery-1 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-2" aria-hidden="true"></i> fa-battery-2 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-3" aria-hidden="true"></i> fa-battery-3 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-4" aria-hidden="true"></i> fa-battery-4 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-empty" aria-hidden="true"></i> fa-battery-empty
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-full" aria-hidden="true"></i> fa-battery-full
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-half" aria-hidden="true"></i> fa-battery-half
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-quarter" aria-hidden="true"></i> fa-battery-quarter
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-battery-three-quarters" aria-hidden="true"></i> fa-battery-three-quarters
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bed" aria-hidden="true"></i> fa-bed
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-beer" aria-hidden="true"></i> fa-beer
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bell" aria-hidden="true"></i> fa-bell
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bell-o" aria-hidden="true"></i> fa-bell-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bell-slash" aria-hidden="true"></i> fa-bell-slash
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bell-slash-o" aria-hidden="true"></i> fa-bell-slash-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bicycle" aria-hidden="true"></i> fa-bicycle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-binoculars" aria-hidden="true"></i> fa-binoculars
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-birthday-cake" aria-hidden="true"></i> fa-birthday-cake
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-blind" aria-hidden="true"></i> fa-blind
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bluetooth" aria-hidden="true"></i> fa-bluetooth
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bluetooth-b" aria-hidden="true"></i> fa-bluetooth-b
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bolt" aria-hidden="true"></i> fa-bolt
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bomb" aria-hidden="true"></i> fa-bomb
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-book" aria-hidden="true"></i> fa-book
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bookmark" aria-hidden="true"></i> fa-bookmark
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bookmark-o" aria-hidden="true"></i> fa-bookmark-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-braille" aria-hidden="true"></i> fa-braille
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-briefcase" aria-hidden="true"></i> fa-briefcase
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bug" aria-hidden="true"></i> fa-bug
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-building" aria-hidden="true"></i> fa-building
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-building-o" aria-hidden="true"></i> fa-building-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bullhorn" aria-hidden="true"></i> fa-bullhorn
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bullseye" aria-hidden="true"></i> fa-bullseye
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bus" aria-hidden="true"></i> fa-bus
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cab" aria-hidden="true"></i> fa-cab <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-calculator" aria-hidden="true"></i> fa-calculator
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-calendar" aria-hidden="true"></i> fa-calendar
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-calendar-check-o" aria-hidden="true"></i> fa-calendar-check-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-calendar-minus-o" aria-hidden="true"></i> fa-calendar-minus-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-calendar-o" aria-hidden="true"></i> fa-calendar-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-calendar-plus-o" aria-hidden="true"></i> fa-calendar-plus-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-calendar-times-o" aria-hidden="true"></i> fa-calendar-times-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-camera" aria-hidden="true"></i> fa-camera
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-camera-retro" aria-hidden="true"></i> fa-camera-retro
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-car" aria-hidden="true"></i> fa-car
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-caret-square-o-down" aria-hidden="true"></i> fa-caret-square-o-down
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-caret-square-o-left" aria-hidden="true"></i> fa-caret-square-o-left
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-caret-square-o-right" aria-hidden="true"></i> fa-caret-square-o-right
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-caret-square-o-up" aria-hidden="true"></i> fa-caret-square-o-up
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cart-arrow-down" aria-hidden="true"></i> fa-cart-arrow-down
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cart-plus" aria-hidden="true"></i> fa-cart-plus
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cc" aria-hidden="true"></i> fa-cc
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-certificate" aria-hidden="true"></i> fa-certificate
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-check" aria-hidden="true"></i> fa-check
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-check-circle" aria-hidden="true"></i> fa-check-circle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-check-circle-o" aria-hidden="true"></i> fa-check-circle-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-check-square" aria-hidden="true"></i> fa-check-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-check-square-o" aria-hidden="true"></i> fa-check-square-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-child" aria-hidden="true"></i> fa-child
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-circle" aria-hidden="true"></i> fa-circle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-circle-o" aria-hidden="true"></i> fa-circle-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-circle-o-notch" aria-hidden="true"></i> fa-circle-o-notch
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-circle-thin" aria-hidden="true"></i> fa-circle-thin
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-clock-o" aria-hidden="true"></i> fa-clock-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-clone" aria-hidden="true"></i> fa-clone
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-close" aria-hidden="true"></i> fa-close <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cloud" aria-hidden="true"></i> fa-cloud
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cloud-download" aria-hidden="true"></i> fa-cloud-download
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cloud-upload" aria-hidden="true"></i> fa-cloud-upload
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-code" aria-hidden="true"></i> fa-code
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-code-fork" aria-hidden="true"></i> fa-code-fork
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-coffee" aria-hidden="true"></i> fa-coffee
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cog" aria-hidden="true"></i> fa-cog
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cogs" aria-hidden="true"></i> fa-cogs
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-comment" aria-hidden="true"></i> fa-comment
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-comment-o" aria-hidden="true"></i> fa-comment-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-commenting" aria-hidden="true"></i> fa-commenting
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-commenting-o" aria-hidden="true"></i> fa-commenting-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-comments" aria-hidden="true"></i> fa-comments
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-comments-o" aria-hidden="true"></i> fa-comments-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-compass" aria-hidden="true"></i> fa-compass
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-copyright" aria-hidden="true"></i> fa-copyright
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-creative-commons" aria-hidden="true"></i> fa-creative-commons
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-credit-card" aria-hidden="true"></i> fa-credit-card
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-credit-card-alt" aria-hidden="true"></i> fa-credit-card-alt
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-crop" aria-hidden="true"></i> fa-crop
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-crosshairs" aria-hidden="true"></i> fa-crosshairs
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cube" aria-hidden="true"></i> fa-cube
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cubes" aria-hidden="true"></i> fa-cubes
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cutlery" aria-hidden="true"></i> fa-cutlery
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-dashboard" aria-hidden="true"></i> fa-dashboard <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-database" aria-hidden="true"></i> fa-database
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-deaf" aria-hidden="true"></i> fa-deaf
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-deafness" aria-hidden="true"></i> fa-deafness <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-desktop" aria-hidden="true"></i> fa-desktop
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-diamond" aria-hidden="true"></i> fa-diamond
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-dot-circle-o" aria-hidden="true"></i> fa-dot-circle-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-download" aria-hidden="true"></i> fa-download
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-drivers-license" aria-hidden="true"></i> fa-drivers-license <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-drivers-license-o" aria-hidden="true"></i> fa-drivers-license-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-edit" aria-hidden="true"></i> fa-edit <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-ellipsis-h" aria-hidden="true"></i> fa-ellipsis-h
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-ellipsis-v" aria-hidden="true"></i> fa-ellipsis-v
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-envelope" aria-hidden="true"></i> fa-envelope
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-envelope-o" aria-hidden="true"></i> fa-envelope-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-envelope-open" aria-hidden="true"></i> fa-envelope-open
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-envelope-open-o" aria-hidden="true"></i> fa-envelope-open-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-envelope-square" aria-hidden="true"></i> fa-envelope-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-eraser" aria-hidden="true"></i> fa-eraser
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-exchange" aria-hidden="true"></i> fa-exchange
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-exclamation" aria-hidden="true"></i> fa-exclamation
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-exclamation-circle" aria-hidden="true"></i> fa-exclamation-circle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-exclamation-triangle" aria-hidden="true"></i> fa-exclamation-triangle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-external-link" aria-hidden="true"></i> fa-external-link
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-external-link-square" aria-hidden="true"></i> fa-external-link-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-eye" aria-hidden="true"></i> fa-eye
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-eye-slash" aria-hidden="true"></i> fa-eye-slash
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-eyedropper" aria-hidden="true"></i> fa-eyedropper
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-fax" aria-hidden="true"></i> fa-fax
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-feed" aria-hidden="true"></i> fa-feed <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-female" aria-hidden="true"></i> fa-female
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-fighter-jet" aria-hidden="true"></i> fa-fighter-jet
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-archive-o" aria-hidden="true"></i> fa-file-archive-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-audio-o" aria-hidden="true"></i> fa-file-audio-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-code-o" aria-hidden="true"></i> fa-file-code-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-excel-o" aria-hidden="true"></i> fa-file-excel-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-image-o" aria-hidden="true"></i> fa-file-image-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-movie-o" aria-hidden="true"></i> fa-file-movie-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-pdf-o" aria-hidden="true"></i> fa-file-pdf-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-photo-o" aria-hidden="true"></i> fa-file-photo-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-picture-o" aria-hidden="true"></i> fa-file-picture-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-powerpoint-o" aria-hidden="true"></i> fa-file-powerpoint-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-sound-o" aria-hidden="true"></i> fa-file-sound-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-video-o" aria-hidden="true"></i> fa-file-video-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-word-o" aria-hidden="true"></i> fa-file-word-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-zip-o" aria-hidden="true"></i> fa-file-zip-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-film" aria-hidden="true"></i> fa-film
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-filter" aria-hidden="true"></i> fa-filter
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-fire" aria-hidden="true"></i> fa-fire
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-fire-extinguisher" aria-hidden="true"></i> fa-fire-extinguisher
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-flag" aria-hidden="true"></i> fa-flag
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-flag-checkered" aria-hidden="true"></i> fa-flag-checkered
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-flag-o" aria-hidden="true"></i> fa-flag-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-flash" aria-hidden="true"></i> fa-flash <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-flask" aria-hidden="true"></i> fa-flask
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-folder" aria-hidden="true"></i> fa-folder
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-folder-o" aria-hidden="true"></i> fa-folder-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-folder-open" aria-hidden="true"></i> fa-folder-open
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-folder-open-o" aria-hidden="true"></i> fa-folder-open-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-frown-o" aria-hidden="true"></i> fa-frown-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-futbol-o" aria-hidden="true"></i> fa-futbol-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-gamepad" aria-hidden="true"></i> fa-gamepad
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-gavel" aria-hidden="true"></i> fa-gavel
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-gear" aria-hidden="true"></i> fa-gear <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-gears" aria-hidden="true"></i> fa-gears <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-gift" aria-hidden="true"></i> fa-gift
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-glass" aria-hidden="true"></i> fa-glass
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-globe" aria-hidden="true"></i> fa-globe
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-graduation-cap" aria-hidden="true"></i> fa-graduation-cap
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-group" aria-hidden="true"></i> fa-group <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hand-grab-o" aria-hidden="true"></i> fa-hand-grab-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hand-lizard-o" aria-hidden="true"></i> fa-hand-lizard-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hand-paper-o" aria-hidden="true"></i> fa-hand-paper-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hand-peace-o" aria-hidden="true"></i> fa-hand-peace-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hand-pointer-o" aria-hidden="true"></i> fa-hand-pointer-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hand-rock-o" aria-hidden="true"></i> fa-hand-rock-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hand-scissors-o" aria-hidden="true"></i> fa-hand-scissors-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hand-spock-o" aria-hidden="true"></i> fa-hand-spock-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hand-stop-o" aria-hidden="true"></i> fa-hand-stop-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-handshake-o" aria-hidden="true"></i> fa-handshake-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hard-of-hearing" aria-hidden="true"></i> fa-hard-of-hearing <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hashtag" aria-hidden="true"></i> fa-hashtag
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hdd-o" aria-hidden="true"></i> fa-hdd-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-headphones" aria-hidden="true"></i> fa-headphones
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-heart" aria-hidden="true"></i> fa-heart
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-heart-o" aria-hidden="true"></i> fa-heart-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-heartbeat" aria-hidden="true"></i> fa-heartbeat
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-history" aria-hidden="true"></i> fa-history
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-home" aria-hidden="true"></i> fa-home
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hotel" aria-hidden="true"></i> fa-hotel <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hourglass" aria-hidden="true"></i> fa-hourglass
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hourglass-1" aria-hidden="true"></i> fa-hourglass-1 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hourglass-2" aria-hidden="true"></i> fa-hourglass-2 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hourglass-3" aria-hidden="true"></i> fa-hourglass-3 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hourglass-end" aria-hidden="true"></i> fa-hourglass-end
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hourglass-half" aria-hidden="true"></i> fa-hourglass-half
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hourglass-o" aria-hidden="true"></i> fa-hourglass-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hourglass-start" aria-hidden="true"></i> fa-hourglass-start
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-i-cursor" aria-hidden="true"></i> fa-i-cursor
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-id-badge" aria-hidden="true"></i> fa-id-badge
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-id-card" aria-hidden="true"></i> fa-id-card
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-id-card-o" aria-hidden="true"></i> fa-id-card-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-image" aria-hidden="true"></i> fa-image <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-inbox" aria-hidden="true"></i> fa-inbox
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-industry" aria-hidden="true"></i> fa-industry
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-info" aria-hidden="true"></i> fa-info
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-info-circle" aria-hidden="true"></i> fa-info-circle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-institution" aria-hidden="true"></i> fa-institution <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-key" aria-hidden="true"></i> fa-key
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-keyboard-o" aria-hidden="true"></i> fa-keyboard-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-language" aria-hidden="true"></i> fa-language
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-laptop" aria-hidden="true"></i> fa-laptop
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-leaf" aria-hidden="true"></i> fa-leaf
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-legal" aria-hidden="true"></i> fa-legal <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-lemon-o" aria-hidden="true"></i> fa-lemon-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-level-down" aria-hidden="true"></i> fa-level-down
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-level-up" aria-hidden="true"></i> fa-level-up
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-life-bouy" aria-hidden="true"></i> fa-life-bouy <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-life-buoy" aria-hidden="true"></i> fa-life-buoy <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-life-ring" aria-hidden="true"></i> fa-life-ring
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-life-saver" aria-hidden="true"></i> fa-life-saver <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-lightbulb-o" aria-hidden="true"></i> fa-lightbulb-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-line-chart" aria-hidden="true"></i> fa-line-chart
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-location-arrow" aria-hidden="true"></i> fa-location-arrow
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-lock" aria-hidden="true"></i> fa-lock
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-low-vision" aria-hidden="true"></i> fa-low-vision
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-magic" aria-hidden="true"></i> fa-magic
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-magnet" aria-hidden="true"></i> fa-magnet
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-mail-forward" aria-hidden="true"></i> fa-mail-forward <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-mail-reply" aria-hidden="true"></i> fa-mail-reply <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-mail-reply-all" aria-hidden="true"></i> fa-mail-reply-all <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-male" aria-hidden="true"></i> fa-male
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-map" aria-hidden="true"></i> fa-map
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-map-marker" aria-hidden="true"></i> fa-map-marker
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-map-o" aria-hidden="true"></i> fa-map-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-map-pin" aria-hidden="true"></i> fa-map-pin
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-map-signs" aria-hidden="true"></i> fa-map-signs
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-meh-o" aria-hidden="true"></i> fa-meh-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-microchip" aria-hidden="true"></i> fa-microchip
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-microphone" aria-hidden="true"></i> fa-microphone
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-microphone-slash" aria-hidden="true"></i> fa-microphone-slash
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-minus" aria-hidden="true"></i> fa-minus
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-minus-circle" aria-hidden="true"></i> fa-minus-circle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-minus-square" aria-hidden="true"></i> fa-minus-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-minus-square-o" aria-hidden="true"></i> fa-minus-square-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-mobile" aria-hidden="true"></i> fa-mobile
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-mobile-phone" aria-hidden="true"></i> fa-mobile-phone <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-money" aria-hidden="true"></i> fa-money
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-moon-o" aria-hidden="true"></i> fa-moon-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-mortar-board" aria-hidden="true"></i> fa-mortar-board <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-motorcycle" aria-hidden="true"></i> fa-motorcycle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-mouse-pointer" aria-hidden="true"></i> fa-mouse-pointer
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-music" aria-hidden="true"></i> fa-music
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-navicon" aria-hidden="true"></i> fa-navicon <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-newspaper-o" aria-hidden="true"></i> fa-newspaper-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-object-group" aria-hidden="true"></i> fa-object-group
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-object-ungroup" aria-hidden="true"></i> fa-object-ungroup
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-paint-brush" aria-hidden="true"></i> fa-paint-brush
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-paper-plane" aria-hidden="true"></i> fa-paper-plane
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-paper-plane-o" aria-hidden="true"></i> fa-paper-plane-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-paw" aria-hidden="true"></i> fa-paw
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-pencil" aria-hidden="true"></i> fa-pencil
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-pencil-square" aria-hidden="true"></i> fa-pencil-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-pencil-square-o" aria-hidden="true"></i> fa-pencil-square-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-percent" aria-hidden="true"></i> fa-percent
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-phone" aria-hidden="true"></i> fa-phone
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-phone-square" aria-hidden="true"></i> fa-phone-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-photo" aria-hidden="true"></i> fa-photo <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-picture-o" aria-hidden="true"></i> fa-picture-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-pie-chart" aria-hidden="true"></i> fa-pie-chart
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-plane" aria-hidden="true"></i> fa-plane
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-plug" aria-hidden="true"></i> fa-plug
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-plus" aria-hidden="true"></i> fa-plus
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-plus-circle" aria-hidden="true"></i> fa-plus-circle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-plus-square" aria-hidden="true"></i> fa-plus-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-plus-square-o" aria-hidden="true"></i> fa-plus-square-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-podcast" aria-hidden="true"></i> fa-podcast
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-power-off" aria-hidden="true"></i> fa-power-off
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-print" aria-hidden="true"></i> fa-print
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-puzzle-piece" aria-hidden="true"></i> fa-puzzle-piece
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-qrcode" aria-hidden="true"></i> fa-qrcode
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-question" aria-hidden="true"></i> fa-question
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-question-circle" aria-hidden="true"></i> fa-question-circle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-question-circle-o" aria-hidden="true"></i> fa-question-circle-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-quote-left" aria-hidden="true"></i> fa-quote-left
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-quote-right" aria-hidden="true"></i> fa-quote-right
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-random" aria-hidden="true"></i> fa-random
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-recycle" aria-hidden="true"></i> fa-recycle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-refresh" aria-hidden="true"></i> fa-refresh
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-registered" aria-hidden="true"></i> fa-registered
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-remove" aria-hidden="true"></i> fa-remove <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-reorder" aria-hidden="true"></i> fa-reorder <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-reply" aria-hidden="true"></i> fa-reply
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-reply-all" aria-hidden="true"></i> fa-reply-all
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-retweet" aria-hidden="true"></i> fa-retweet
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-road" aria-hidden="true"></i> fa-road
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-rocket" aria-hidden="true"></i> fa-rocket
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-rss" aria-hidden="true"></i> fa-rss
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-rss-square" aria-hidden="true"></i> fa-rss-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-s15" aria-hidden="true"></i> fa-s15 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-search" aria-hidden="true"></i> fa-search
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-search-minus" aria-hidden="true"></i> fa-search-minus
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-search-plus" aria-hidden="true"></i> fa-search-plus
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-send" aria-hidden="true"></i> fa-send <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-send-o" aria-hidden="true"></i> fa-send-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-server" aria-hidden="true"></i> fa-server
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-share" aria-hidden="true"></i> fa-share
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-share-alt" aria-hidden="true"></i> fa-share-alt
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-share-alt-square" aria-hidden="true"></i> fa-share-alt-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-share-square" aria-hidden="true"></i> fa-share-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-share-square-o" aria-hidden="true"></i> fa-share-square-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-shield" aria-hidden="true"></i> fa-shield
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-ship" aria-hidden="true"></i> fa-ship
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-shopping-bag" aria-hidden="true"></i> fa-shopping-bag
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-shopping-basket" aria-hidden="true"></i> fa-shopping-basket
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-shopping-cart" aria-hidden="true"></i> fa-shopping-cart
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-shower" aria-hidden="true"></i> fa-shower
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sign-in" aria-hidden="true"></i> fa-sign-in
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sign-language" aria-hidden="true"></i> fa-sign-language
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sign-out" aria-hidden="true"></i> fa-sign-out
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-signal" aria-hidden="true"></i> fa-signal
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-signing" aria-hidden="true"></i> fa-signing <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sitemap" aria-hidden="true"></i> fa-sitemap
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sliders" aria-hidden="true"></i> fa-sliders
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-smile-o" aria-hidden="true"></i> fa-smile-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-snowflake-o" aria-hidden="true"></i> fa-snowflake-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-soccer-ball-o" aria-hidden="true"></i> fa-soccer-ball-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort" aria-hidden="true"></i> fa-sort
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-alpha-asc" aria-hidden="true"></i> fa-sort-alpha-asc
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-alpha-desc" aria-hidden="true"></i> fa-sort-alpha-desc
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-amount-asc" aria-hidden="true"></i> fa-sort-amount-asc
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-amount-desc" aria-hidden="true"></i> fa-sort-amount-desc
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-asc" aria-hidden="true"></i> fa-sort-asc
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-desc" aria-hidden="true"></i> fa-sort-desc
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-down" aria-hidden="true"></i> fa-sort-down <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-numeric-asc" aria-hidden="true"></i> fa-sort-numeric-asc
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-numeric-desc" aria-hidden="true"></i> fa-sort-numeric-desc
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sort-up" aria-hidden="true"></i> fa-sort-up <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-space-shuttle" aria-hidden="true"></i> fa-space-shuttle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-spinner" aria-hidden="true"></i> fa-spinner
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-spoon" aria-hidden="true"></i> fa-spoon
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-square" aria-hidden="true"></i> fa-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-square-o" aria-hidden="true"></i> fa-square-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-star" aria-hidden="true"></i> fa-star
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-star-half" aria-hidden="true"></i> fa-star-half
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-star-half-empty" aria-hidden="true"></i> fa-star-half-empty <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-star-half-full" aria-hidden="true"></i> fa-star-half-full <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-star-half-o" aria-hidden="true"></i> fa-star-half-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-star-o" aria-hidden="true"></i> fa-star-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sticky-note" aria-hidden="true"></i> fa-sticky-note
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sticky-note-o" aria-hidden="true"></i> fa-sticky-note-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-street-view" aria-hidden="true"></i> fa-street-view
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-suitcase" aria-hidden="true"></i> fa-suitcase
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-sun-o" aria-hidden="true"></i> fa-sun-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-support" aria-hidden="true"></i> fa-support <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-tablet" aria-hidden="true"></i> fa-tablet
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-tachometer" aria-hidden="true"></i> fa-tachometer
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-tag" aria-hidden="true"></i> fa-tag
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-tags" aria-hidden="true"></i> fa-tags
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-tasks" aria-hidden="true"></i> fa-tasks
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-taxi" aria-hidden="true"></i> fa-taxi
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-television" aria-hidden="true"></i> fa-television
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-terminal" aria-hidden="true"></i> fa-terminal
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer" aria-hidden="true"></i> fa-thermometer <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-0" aria-hidden="true"></i> fa-thermometer-0 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-1" aria-hidden="true"></i> fa-thermometer-1 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-2" aria-hidden="true"></i> fa-thermometer-2 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-3" aria-hidden="true"></i> fa-thermometer-3 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-4" aria-hidden="true"></i> fa-thermometer-4 <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-empty" aria-hidden="true"></i> fa-thermometer-empty
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-full" aria-hidden="true"></i> fa-thermometer-full
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-half" aria-hidden="true"></i> fa-thermometer-half
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-quarter" aria-hidden="true"></i> fa-thermometer-quarter
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thermometer-three-quarters" aria-hidden="true"></i> fa-thermometer-three-quarters
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thumb-tack" aria-hidden="true"></i> fa-thumb-tack
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thumbs-down" aria-hidden="true"></i> fa-thumbs-down
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thumbs-o-down" aria-hidden="true"></i> fa-thumbs-o-down
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thumbs-o-up" aria-hidden="true"></i> fa-thumbs-o-up
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-thumbs-up" aria-hidden="true"></i> fa-thumbs-up
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-ticket" aria-hidden="true"></i> fa-ticket
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-times" aria-hidden="true"></i> fa-times
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-times-circle" aria-hidden="true"></i> fa-times-circle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-times-circle-o" aria-hidden="true"></i> fa-times-circle-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-times-rectangle" aria-hidden="true"></i> fa-times-rectangle <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-times-rectangle-o" aria-hidden="true"></i> fa-times-rectangle-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-tint" aria-hidden="true"></i> fa-tint
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-toggle-down" aria-hidden="true"></i> fa-toggle-down <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-toggle-left" aria-hidden="true"></i> fa-toggle-left <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-toggle-off" aria-hidden="true"></i> fa-toggle-off
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-toggle-on" aria-hidden="true"></i> fa-toggle-on
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-toggle-right" aria-hidden="true"></i> fa-toggle-right <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-toggle-up" aria-hidden="true"></i> fa-toggle-up <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-trademark" aria-hidden="true"></i> fa-trademark
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-trash" aria-hidden="true"></i> fa-trash
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-trash-o" aria-hidden="true"></i> fa-trash-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-tree" aria-hidden="true"></i> fa-tree
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-trophy" aria-hidden="true"></i> fa-trophy
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-truck" aria-hidden="true"></i> fa-truck
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-tty" aria-hidden="true"></i> fa-tty
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-tv" aria-hidden="true"></i> fa-tv <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-umbrella" aria-hidden="true"></i> fa-umbrella
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-universal-access" aria-hidden="true"></i> fa-universal-access
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-university" aria-hidden="true"></i> fa-university
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-unlock" aria-hidden="true"></i> fa-unlock
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-unlock-alt" aria-hidden="true"></i> fa-unlock-alt
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-unsorted" aria-hidden="true"></i> fa-unsorted <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-upload" aria-hidden="true"></i> fa-upload
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-user" aria-hidden="true"></i> fa-user
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-user-circle" aria-hidden="true"></i> fa-user-circle
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-user-circle-o" aria-hidden="true"></i> fa-user-circle-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-user-o" aria-hidden="true"></i> fa-user-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-user-plus" aria-hidden="true"></i> fa-user-plus
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-user-secret" aria-hidden="true"></i> fa-user-secret
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-user-times" aria-hidden="true"></i> fa-user-times
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-users" aria-hidden="true"></i> fa-users
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-vcard" aria-hidden="true"></i> fa-vcard <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-vcard-o" aria-hidden="true"></i> fa-vcard-o <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-video-camera" aria-hidden="true"></i> fa-video-camera
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-volume-control-phone" aria-hidden="true"></i> fa-volume-control-phone
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-volume-down" aria-hidden="true"></i> fa-volume-down
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-volume-off" aria-hidden="true"></i> fa-volume-off
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-volume-up" aria-hidden="true"></i> fa-volume-up
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-warning" aria-hidden="true"></i> fa-warning <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-wheelchair" aria-hidden="true"></i> fa-wheelchair
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-wheelchair-alt" aria-hidden="true"></i> fa-wheelchair-alt
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-wifi" aria-hidden="true"></i> fa-wifi
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-window-close" aria-hidden="true"></i> fa-window-close
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-window-close-o" aria-hidden="true"></i> fa-window-close-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-window-maximize" aria-hidden="true"></i> fa-window-maximize
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-window-minimize" aria-hidden="true"></i> fa-window-minimize
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-window-restore" aria-hidden="true"></i> fa-window-restore
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-wrench" aria-hidden="true"></i> fa-wrench
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-align-center" aria-hidden="true"></i> fa-align-center
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-align-justify" aria-hidden="true"></i> fa-align-justify
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-align-left" aria-hidden="true"></i> fa-align-left
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-align-right" aria-hidden="true"></i> fa-align-right
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-bold" aria-hidden="true"></i> fa-bold
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-chain" aria-hidden="true"></i> fa-chain <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-chain-broken" aria-hidden="true"></i> fa-chain-broken
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-clipboard" aria-hidden="true"></i> fa-clipboard
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-columns" aria-hidden="true"></i> fa-columns
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-copy" aria-hidden="true"></i> fa-copy <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-cut" aria-hidden="true"></i> fa-cut <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-dedent" aria-hidden="true"></i> fa-dedent <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-eraser" aria-hidden="true"></i> fa-eraser
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file" aria-hidden="true"></i> fa-file
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-o" aria-hidden="true"></i> fa-file-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-text" aria-hidden="true"></i> fa-file-text
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-file-text-o" aria-hidden="true"></i> fa-file-text-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-files-o" aria-hidden="true"></i> fa-files-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-floppy-o" aria-hidden="true"></i> fa-floppy-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-font" aria-hidden="true"></i> fa-font
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-header" aria-hidden="true"></i> fa-header
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-indent" aria-hidden="true"></i> fa-indent
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-italic" aria-hidden="true"></i> fa-italic
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-link" aria-hidden="true"></i> fa-link
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-list" aria-hidden="true"></i> fa-list
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-list-alt" aria-hidden="true"></i> fa-list-alt
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-list-ol" aria-hidden="true"></i> fa-list-ol
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-list-ul" aria-hidden="true"></i> fa-list-ul
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-outdent" aria-hidden="true"></i> fa-outdent
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-paperclip" aria-hidden="true"></i> fa-paperclip
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-paragraph" aria-hidden="true"></i> fa-paragraph
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-paste" aria-hidden="true"></i> fa-paste <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-repeat" aria-hidden="true"></i> fa-repeat
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-rotate-left" aria-hidden="true"></i> fa-rotate-left <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-rotate-right" aria-hidden="true"></i> fa-rotate-right <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-save" aria-hidden="true"></i> fa-save <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-scissors" aria-hidden="true"></i> fa-scissors
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-strikethrough" aria-hidden="true"></i> fa-strikethrough
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-subscript" aria-hidden="true"></i> fa-subscript
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-superscript" aria-hidden="true"></i> fa-superscript
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-table" aria-hidden="true"></i> fa-table
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-text-height" aria-hidden="true"></i> fa-text-height
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-text-width" aria-hidden="true"></i> fa-text-width
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-th" aria-hidden="true"></i> fa-th
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-th-large" aria-hidden="true"></i> fa-th-large
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-th-list" aria-hidden="true"></i> fa-th-list
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-underline" aria-hidden="true"></i> fa-underline
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-undo" aria-hidden="true"></i> fa-undo
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-unlink" aria-hidden="true"></i> fa-unlink <span class="text-muted">(alias)</span>
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-ambulance" aria-hidden="true"></i> fa-ambulance
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-h-square" aria-hidden="true"></i> fa-h-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-heart" aria-hidden="true"></i> fa-heart
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-heart-o" aria-hidden="true"></i> fa-heart-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-heartbeat" aria-hidden="true"></i> fa-heartbeat
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-hospital-o" aria-hidden="true"></i> fa-hospital-o
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-medkit" aria-hidden="true"></i> fa-medkit
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-plus-square" aria-hidden="true"></i> fa-plus-square
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-stethoscope" aria-hidden="true"></i> fa-stethoscope
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-user-md" aria-hidden="true"></i> fa-user-md
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-wheelchair" aria-hidden="true"></i> fa-wheelchair
			</div>

			<div class="fa-hover col-md-3 col-sm-4">
				<i class="fa fa-wheelchair-alt" aria-hidden="true"></i> fa-wheelchair-alt
			</div>

		</div>
		<script type="text/javascript">
			$(function() {
				$('#key').bind('input propertychange', function() {
					if($(this).val()!=""){
						highlight();
					}else{
						clearHighlight();
					}
				}); 
		
			});
			function highlight() {
				clearHighlight();
				var key = $('#key').val();
				var regExp = new RegExp(key, 'g'); //全局搜索
				$('#icons div').each(function() {
					var text = $(this).text();
					var newHtml = text.replace(regExp, '<span class="highlight">' + key + '</span>'); //加上span.highlight包裹
					$(this).html($(this).find("i").prop("outerHTML") + newHtml); //html更换
				});
				
				// 
				$("#icons div").hide();
				$(".highlight").parent().show();
			}
			function clearHighlight() {
				$('div').each(function() {
					$(this).find('.highlight').each(function() {
						$(this).replaceWith($(this).html()); //去掉span.highlight包裹
					});
				});
			}
		</script>
	</div>
</body>