var _config={
	showConsole: false
}

function noop(){};

window.console = window.console&&_config.showConsole?window.console:(function(){
	var console={},method;
	var methods = [
		'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
		'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
		'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
		'timeStamp', 'trace', 'warn'
	];
	var length = methods.length;
	while ( length-- ) {
		method = methods[ length ];
		console[ method ] = noop;
	}
	return console;
})();

String.prototype.trimAll=function(){
	return this.replace(/\s/gi,'');
};

String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g,"");
};

String.prototype.isPhoneNum=function(){
	var pRex=/^1[34578][0-9]{9}$/;
	return pRex.test(this);
};

String.prototype.isEmail=function(){
	var eRex=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
	return eRex.test(this);
};

function flowController(fn){
	fn=fn||function(e){e.next(true);};
	var then = new flowUnit(fn);
	then.doCt();
	return then;
}

function flowUnit(fn){
	var _this=this;
	this._imDoThen=false;
	this._thenDo=null;
	this._imDoThen=false;
	this.fn=fn;
	this.e={
		next:function(val){
			if(val&&_this._thenDo){
				_this._thenDo.doCt();
			}
			if(val&&!_this._thenDo){
				_this._imDoThen=true;
			}
		}
	};
}

flowUnit.prototype.doCt=function(){
	this.fn(this.e);
};

flowUnit.prototype.then=function(fn){
	this._thenDo= new flowUnit(fn);
	if(this._imDoThen){
		this._thenDo.doCt();
	}
	return this._thenDo;
};

(function($) {
	$.fn.addBack = $.fn.addBack || $.fn.andSelf;
	$.fn.extend({
		actual: function(method, options) {
			// check if the jQuery method exist
			if (!this[method]) {
				throw '$.actual => The jQuery method "' + method + '" you called does not exist';
			}
			var defaults = {
				absolute: false,
				clone: false,
				includeMargin: false
			};
			var configs = $.extend(defaults, options);
			var $target = this.eq(0);
			var fix, restore;
			if (configs.clone === true) {
				fix = function() {
					var style = 'position: absolute !important; top: -1000 !important; ';
					// this is useful with css3pie
					$target = $target.
					clone().
					attr('style', style).
					appendTo('body');
				};
				restore = function() {
					// remove DOM element after getting the width
					$target.remove();
				};
			} else {
				var tmp = [];
				var style = '';
				var $hidden;
				fix = function() {
					// get all hidden parents
					$hidden = $target.parents().addBack().filter(':hidden');
					style += 'visibility: hidden !important; display: block !important; ';
					if (configs.absolute === true) style += 'position: absolute !important; ';
					// save the origin style props
					// set the hidden el css to be got the actual value later
					$hidden.each(function() {
						var $this = $(this);
						// Save original style. If no style was set, attr() returns undefined
						tmp.push($this.attr('style'));
						$this.attr('style', style);
					});
				};
				restore = function() {
					// restore origin style values
					$hidden.each(function(i) {
						var $this = $(this);
						var _tmp = tmp[i];

						if (_tmp === undefined) {
							$this.removeAttr('style');
						} else {
							$this.attr('style', _tmp);
						}
					});
				};
			}
			fix();
			// get the actual value with user specific methed
			// it can be 'width', 'height', 'outerWidth', 'innerWidth'... etc
			// configs.includeMargin only works for 'outerWidth' and 'outerHeight'
			var actual = /(outer)/.test(method) ?
				$target[method](configs.includeMargin) :
				$target[method]();
			restore();
			// IMPORTANT, this plugin only return the value of the first element
			return actual;
		}
	});
})(jQuery);

(function($){
	/**获取图片路径*/
	$.getImagesUrl=function(){
		var imagesUrl=window.imagesUrl||'../images/';
		if(!arguments[0]){
			return imagesUrl;
		}
		if(arguments[0]&&!arguments[1]){
			return imagesUrl+arguments[0];
		}
		/**添加时间戳，待续。。。。*/
		// if(arguments[0]&&arguments[1]){
		// 	return imagesUrl+arguments[0]
		// }
	}

	/**
	 * [isRegExp 判断一个变量是不是正则表达式]
	 * @param  {[type]}  p [description]
	 * @return {Boolean}   [true 是 false 不是]
	 */
	$.isRegExp = function(p){
		if(p==null){
			return false;
		}
		try{
			var str='';
			p.test(str);
			return true;
		}catch(e){
			return false;
		}
	};

	/**
	 * [getClass 获取某个元素上的类列表,也可以输入一个正则表达式]
	 * @return {[]} [返回一个数组]
	 */
	$.fn.getClass = function(){
		var c = [];
		var cStr = $(this).attr('class');
		if(cStr!=null){
			cStr = cStr.trim();
			c = cStr.split(/\s+/);
		}
		if(arguments.length != 0&&$.isRegExp(arguments[0])){
			var sc = [];
			for(var i= 0 ,len = c.length;i<len;i++){
				if(arguments[0].test(c[i])){
					sc.push(c[i]);
				}
			}
			return sc;
		}
		return c;
	};

	/**
	 * [hasClass 重新写了jquery的hasClass的方法，也可输入一个正则表达式]
	 * @return {Boolean} [description]
	 */
	var _hasClass = $.fn.hasClass;
	$.fn.hasClass = function(){
		if(arguments.length == 0){
			return false;
		}
		if($.isRegExp(arguments[0])){
			var c = $(this).getClass(arguments[0]);
			if(c.length>0){
				return true;
			}else{
				return false;
			}
		}else{
			return _hasClass.apply(this,[arguments[0]]);
		};
	};

	var _removeClass = $.fn.removeClass;

	/**
	 * [removeClass 修改jquery的方法，可以使用正则表达式]
	 * @return {[type]} [description]
	 */
	$.fn.removeClass = function(){
		if(arguments.length == 0){
			return false;
		}
		if($.isRegExp(arguments[0])){
			var c = $(this).getClass(arguments[0]);
			for(var i = 0,len = c.length;i<len;i++){
				_removeClass.apply(this,[c[i]]);
			}
		}else{
			return _removeClass.apply(this,[arguments[0]]);
		};
	};

	/**判断jquery是不是同一个对象*/
	$.fn.isEqual=function($dom){
		var len = $(this).length , dlen = $dom.length ;
		if(len == 0 && dlen == 0){
			return true;
		}
		if(dlen !== len){
			return false;
		}
		var isEqual = true ;
		for(var i = 0; i < len ; i++){
			if($(this)[i] !== $dom[i]){
				isEqual = false;
				break;
			}
		}
		return isEqual
	};

	/**判断一个对象是不是另外一个对象的子集*/
	$.fn.isMembers=function($dom){
		var len = $(this).length , dlen = $dom.length;
		if(len.length == 0){
			console.log('调用对象isMembers 的长度为空')
			return false;
		}
		if(len > dlen){
			return false;
		}
		var isMember = false ;
		for (var i = 0 ; i < len ; i++ ){
			for(var j = 0 ; j < dlen ; j++ ){
				if($dom[j] == $(this)[i]){
					isMember = true ;
					break;
				}
			}
			if(!isMember){
				return false;
			}
			isMember = false;
		}
		return true;
	};

	/**判断一个对象是否存在*/
	$.fn.isExist=function(){
		if($(this).length>0){
			return true;
		}else{
			return false;
		}
	}

	/**判断dom上有没有该属性*/
	$.fn.hasAttr=function(val){
		return !(typeof $(this).attr(val)==='undefined');
	}
	
	/**判断是不是dom对象*/
	$.isDom=( typeof HTMLElement === 'object' )?
				function(obj){
					return obj instanceof HTMLElement;
				} :
				function(obj){
					return obj && typeof obj === 'object' && obj.nodeType === 1 && typeof obj.nodeName === 'string';
				}

	/**判断是不是jquery对象*/
	$.isJquery=function(obj){
		return obj instanceof $;
	}

	/**判断是不是array对象*/
	$.isArray=function(obj){
		return Object.prototype.toString.call(obj) === '[object Array]';
	}

	$.isJson=function(obj){
		return typeof(obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length
	}

	/**转换为数字*/
	$.number=function(val){
		var n = Number(val);
		if (!isNaN(n)){
			return n;
		}
		return false;
	}

	var IE=function(){};

	IE.prototype.isIE=function(){
		if (!!window.ActiveXObject || "ActiveXObject" in window)
			return true;
		else
			return false;
	};

	IE.prototype.getVersion=function(ua){
		ua =  ua || navigator.userAgent;
		if(/msie/i.test(ua)){
			return ua.match(/msie (\d+\.\d+)/i)[1];
		}else if(~ua.toLowerCase().indexOf('trident') && ~ua.indexOf('rv')){
			return ua.match(/rv:(\d+\.\d+)/)[1];
		}
		return false;
	};

	IE.prototype.ltDo=function(val,clk){
		var v=this.getVersion();
		if(this.isIE()){
			if(Number(v)<val){
				clk();
			}
		}
	}

	$.IE=new IE();
})($);

/**模态框*/
(function($){
	
	/**提示框*/
	$.tipComp=function(val1,tipDiv){
		var tim=700,animateTime=200;
		var $tipDiv=$('<div class="comp_tip"></div>');
		if(typeof tipDiv==='string'){
			$tipDiv.html(tipDiv);
		};
		if($.isDom(tipDiv)){
			$tipDiv.append($(tipDiv));
		}
		if($.isJquery(tipDiv)){
			$tipDiv.append(tipDiv);
		}
		var ct=new tipComp($tipDiv,animateTime);
		if(typeof val1==='boolean'&&val1){
			return ct;
		}else{
			tim=val1||tim;
			tim=tim<500?500:tim;
			setTimeout(function(){
				ct.close();
			},tim-animateTime);
		}
		function tipComp($tipDiv,animateTime){
			$tipDiv.css('display','none');
			$('body').append($tipDiv);
			var h=$tipDiv.outerHeight();
			var w=$tipDiv.outerWidth();
			$tipDiv.css({
				'margin-left':-w/2,
				'margin-top':-h/2
			});
			$tipDiv.fadeIn();
			this.close=function(){
				$tipDiv.fadeOut(animateTime,function(){
					$tipDiv.remove();
				});
			}
		}
	}

	/**特殊提示封装：成功提示*/
	$.successTip=function(val1,val2){
		var imagesUrl=window.imagesUrl||'';
		var $rTip=$('<div class="comp_rightTip"><img src="'+$.getImagesUrl('ico/comp_success.png')+'"><div class="comp_txt"></div></div>');
		val2=val2||'成功';
		if(typeof val1==='string'){
			$rTip.find('.comp_txt').html(val1);
			$.tipComp(null,$rTip);
		}else{
			$rTip.find('.comp_txt').html(val2);
			$.tipComp(val1,$rTip);
		}
	}

	$.noticeTip=function(val1,val2){
		var imagesUrl=window.imagesUrl||'';
		var $rTip=$('<div class="comp_noticeTip"><img src="'+$.getImagesUrl('ico/comp_notice.png')+'"><div class="comp_txt"></div></div>');
		val2=val2||'注意';
		if(typeof val1==='string'){
			$rTip.find('.comp_txt').html(val1);
			$.tipComp(null,$rTip);
		}else{
			$rTip.find('.comp_txt').html(val2);
			$.tipComp(val1,$rTip);
		}
	}

	/**特殊提示封装：失败提示*/
	$.errorTip=function(val1,val2){
		var $eTip=$('<div class="comp_errorTip"><img src="'+$.getImagesUrl('ico/comp_error.png')+'"><div class="comp_txt"></div></div>');
		val2=val2||'失败';
		if(typeof val1==='string'){
			$eTip.find('.comp_txt').html(val1);
			$.tipComp(null,$eTip);
		}else{
			$eTip.find('.comp_txt').html(val2);
			$.tipComp(val1,$eTip);
		}
	}

	/**加载动画*/
	$.loading = function($where){
		return new loading($where);
	}
	function loading($where){
		this.$where = $where || $('body');
		var $spin = $('<div class="spinner">'+
							'<div class="spinner-container container1">'+
								'<div class="circle1"></div>'+
								'<div class="circle2"></div>'+
								'<div class="circle3"></div>'+
								'<div class="circle4"></div>'+
							'</div>'+
							'<div class="spinner-container container2">'+
								'<div class="circle1"></div>'+
								'<div class="circle2"></div>'+
								'<div class="circle3"></div>'+
								'<div class="circle4"></div>'+
							'</div>'+
							'<div class="spinner-container container3">'+
								'<div class="circle1"></div>'+
								'<div class="circle2"></div>'+
								'<div class="circle3"></div>'+
								'<div class="circle4"></div>'+
							'</div>'+
						'</div>');
		// $spin = $('<img class="spin_img" src="'+$.getImagesUrl('com/loading.gif')+'">');
		var style = '';
		$.IE.ltDo(10,function(){
			style = 'visibility:hidden;';
		});
		this.$loading = $('<div class="comp_loading" style='+style+'>'+
							+'</div>')
						.append($spin)
						.appendTo(this.$where)
						.fadeIn();
	}
	loading.prototype.remove = function(){
		this.$loading.fadeOut();
	}
	
})($);


/**
 *简洁的输入提示框
*/
(function($){
	$.fn.comfirmbox = function(config){
		$(this).each(function(){
			var _default = {
				confirmCal: function(){},
				cancelCal: function(){}
			}
			var _config = $.extend({},_default,config);
			var $el = $(this).clone().appendTo('body').fadeIn();
			var e = {};
			e.close = function(){
				$el.fadeOut(function(){
					$(this).remove();
				});
			}
			$el.on('click','[fun_type="confirm"]',function(){
				_config.confirmCal.call($el,e)
			});
			$el.on('click','[fun_type="cancel"]',function(){
				_config.cancelCal.call($el,e);
			});

		});
	};
})($);