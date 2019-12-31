/**验证*/
(function($,window){
	$.validate=function(tips,configRules){
		tips=tips||{};
		configRules=configRules||{};
		var _vald=new validate(configRules);
		_vald.changeTips(tips);
		return _vald;
	}

	function validate(configRules){
		var baseRules={
			required:{
				tip:'此项为必填项，请填写',
				filter:function(e){
					var rslt=e.val.replace(/\s/gi,'')!=='';
					e.next(rslt);
				}
			},
			tel:{
				tip:'请输入正确的固定电话号码,注意填写区号',
				filter:function(e){
					var rslt=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(e.val);
					e.next(rslt);
				}
			},
			phone:{
				tip:'请输入有效的手机号码',
				filter:function(e){
					var rslt=/^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[3,5-8])|(166)|(18[0-9]))\d{8}$/.test(e.val);
					e.next(rslt);
				}
			},
			email:{
				tip:"请输入正确的邮箱格式",
				filter:function(e){
					var rslt=/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/.test(e.val.toLowerCase());
					e.next(rslt);
				}
			},
			maxlength:{
				tip:"输入格式已经超出{0}位||13",
				filter:function(e){
					var max=$.number(e.params?e.params[0]||13:13);
					var rslt=e.val.length<=max;
					e.next(rslt);
				}
			},
			minlength:{
				tip:"输入长度不足{0}||6",
				filter:function(e){
					var min=$.number(e.params?e.params[0]||6:6);
					var rslt=e.val.length>=min;
					e.next(rslt);
				}
			},
			equals:{
				tip:"输入参数与原参数不等",
				filter:function(e){
					if(e.params&&typeof e.params[0]==='string'){
						var rslt=e.val===e.params[0];
						e.next(rslt);
					}else{
						console.log('error:equals>>>比较参数格式不对或者不存在');
					}
				}
			},
			number:{
				tip:"请输入数字",
				filter:function(e){
					if($.number(e.val)!=false){
						e.next(true);
					};
				}
			},
			url:{
				tip:"请输入正确的网站",
				filter:function(e){
					var rslt=/(http[s]?|ftp):\/\/[^\/\.]+?\..+\w$/i.test(e.val);
					e.next(rslt);
				}
			}
		}
		this.rules=$.extend(true,{},baseRules,configRules||{});
	}
	validate.prototype.run=function(val,opt,error,succ){
		var rslt=true;
		var rules=this.rules;
		
		var flow=flowController();
		$.each(opt,function(key,item){
			var rule=rules[item.key];
			if(rule){
				flow=flow.then(function(e){
					var vald={};
					vald=analyseValidateParams(rule.tip);
					if(typeof item.value==='string'&&item.value.trimAll()!==''){
						var domVald=analyseValidateParams(item.value);
						vald=$.extend({},vald,domVald);
					}
					var event={
						next:function(isRight,tip){
							e.next(isRight);
							if(!isRight){
								rslt={};
								rslt.type=item.key;
								if(tip){
									rslt.tip=tip;
								}else{
									rslt.tip=dyAnalyseTip(vald['tip'],vald['params']);
								}
								error(rslt);
							}
						},
						val:val,
						params:vald['params']
					}
					rule['filter'](event);
				});
			}
		});
		flow.then(function(e){
			var e={};
			succ(e);
		});
	}
	validate.prototype.changeTips=function(){
		if(typeof arguments[0]==='string'&&this.rules[arguments[0]]&&typeof arguments[1]==='string'){
			this.rules[arguments[0]].tip=arguments[1];
		}
		if($.isJson(arguments[0])){
			var tips=arguments[0];
			for(var key in tips){
				if(this.rules[key]){
					this.rules[key].tip=tips[key];
				}
			};
		}
	}
	validate.prototype.changeFilters=function(){
		if(typeof arguments[0]==='string'&&this.rules[arguments[0]]&&typeof arguments[1]==='function'){
			this.rules[arguments[0]]=arguments[1];
		}
		if($.isJson()){
			var filters=arguments[0];
			for(var key in filters){
				if(this.rules[key]){
					this.rules[key].filter=filters[key];
				}
			};
		}
	}
	validate.prototype.addRules=function(rules){
		if(typeof arguments[0]==='string'&&$.isJson(arguments[1])){
			if(typeof arguments[1].tip==='string'&&typeof arguments[1].filter==='function'){
				this.rules[arguments[0]]=arguments[1];
			}else{
				console.log('添加的规则格式有误');
			}
		}
		if($.isJson(arguments[0])){
			this.rules=$.extend(true,{},this.rules,arguments[0]);
		}
	}

	function dyAnalyseTip(tip,params){
		if(tip&&$.isArray(params)){
			for(var i=0;i<params.length;i++){
				tip=tip.replace('{'+i+'}',params[i]);
			}
		}
		return tip||'';
	}
	function analyseValidateParams(val){
		if(typeof val==='string'){
			var vald={};
			if(/^\(.+\)$/.test(val)){
				vald.params=val.replace(/^\(/,'').replace(/\)$/,'').split(',');
				for(var i=0;i<vald.params.length;i++){
					vald.params[i]=excJsCode(vald.params[i]);
				}
				return vald;
			}
			if(val.indexOf('||')>=0){
				var args=val.split('||');
				vald.tip=args[0];
				vald.params=args[1].split(',');
				for(var i=0;i<vald.params.length;i++){
					vald.params[i]=excJsCode(vald.params[i]);
				}
			}else{
				vald.tip=val;
			}
			return vald;
		}
		return false;
	}
	function excJsCode(val){
		if(/^js:/.test(val)){
			var newVal;
			try{
				newVal=eval(val.replace(/^js:/,''));
			}catch(e){
				newVal='';
			}
			return newVal||'';
		}else{
			return val;
		}
	}
})($,window);

(function($,window){
	$(function(){
		var vald=window.validate=$.validate(window.errorTips,window.configRules);
	});

	$.fn.validate=function(_opt){
		// var _defaultVs=['v-required','v-tel','v-phone','v-email','v-maxLenght','v-minLenght','v-equals','v-error','v-succ'];
		var _default={
			way:window.validate,
			filter:function(){},
			val:function(){
				return $(this).val();
			},
			progress:function(e){return true},
			error:function(e){
				console.log(e.type+'>>>>'+e.tip);
			},
			succ:function(e){
				console.log('-----succ----');
			},
			rslt:function(e){
				console.log('----last--result-----');
			}
		};
		var _config=$.extend({},_default,_opt||{});
		var $vs=$(this);
		var $vItems=$(this).add($(this).find('*')).filter(function(){
						var isTarget=false;
						var _attrs=this.attributes;
						for(var i=0;i<_attrs.length;i++){
							var attr=_attrs[i].nodeName;
							if(/^v-/.test(attr)){
								isTarget=true;
								break;
							}
						}
						return isTarget;
					});
		var vItemLength=$vItems.length;

		var sumDo=sumThenDo(vItemLength,function(e){
			_config.rslt.call($vs,e);
		});
		$vItems.each(function(e){
			var $t=$(this);
			var needVil=_config.filter.call($t);
			if(needVil===false){
				sumDo();
			}else{
				var val=_getVal($t);
				var e={};
				e.val=val;
				_progress(e,$t);
			}
		});

		function _getVsWay(){
			return _config.way;
		}

		function _getVal($t){
			var val=_config.val.call($t);
			if(val==undefined){
				val=$t.val();
			}
			if(val==undefined){
				val='';
			}
			return ''+val;
		}
		function _progress(e,$t){
			var valiWay=_config.way;
			e.$target=$t;
			e.addRules=function(rules){
				valiWay.addRules(rules);
			}
			e.getVerifyItems=function(){
				var vItems=[];
				var _attrs=$t[0].attributes;
				for(var i=0;i<_attrs.length;i++){
					var item={};
					var attr=_attrs[i].nodeName;
					if(/^v-/.test(attr)&&attr!=='v-error'&&attr!='v-succ'){
						var index=99999999;
						if(/\[\d{1,}\]$/.test(attr)){
							var _str_index=attr.match(/\[\d{1,}\]$/)[0];
							index=parseInt(_str_index.substring(1,_str_index.length-1));
						};
						item.index=index;
						item.key=attr.replace(/^v-/,'').replace(/\[\d{1,}\]$/,'');
						item.value=$t.attr(attr);
						vItems.push(item);
					}
				}
				vItems.sort(function(a,b){
					return a.index>b.index?1:-1;
				})
				return vItems;
			}
			e.getDomError=function(){
				return _getErrorFn($t);
			}
			e.getDomSucc=function(){
				return _getSuccFn($t);
			}
			e.run=function(val,item,error,succ){
				valiWay.run(val,item,function(e){
					_error(e,$t,error);
				},function(e){
					_succ(e,$t,succ);
				});
			}
			var _needDo=_config.progress.call($t,e);
			
			if(_needDo||_needDo===undefined){
				var vItems=e.getVerifyItems();
				valiWay.run(e.val,vItems,function(e){
					_error(e,$t);
				},function(e){
					_succ(e,$t);
				});
			}
		}

		function _getFn($t,fnName){
			return (function(e){
				if(_config[fnName]){
					_config[fnName].call($t,e);
				}else{
					try{
						eval(fnName).call($t,e);
					}catch(e){
						console.log('----未找到错误回调----');
					}
				}
			});
		}

		function _getErrorFn($t){
			if($t.hasAttr('v-error')){
				return _getFn($t,$t.attr('v-error'));
			}else{
				return function(e){
					_config.error.call($t,e);
				}
			}
		}

		function _getSuccFn($t){
			if($t.hasAttr('v-succ')){
				return _getFn($t,$t.attr('v-succ'));
			}else{
				return function(e){
					_config.succ.call($t,e);
				}
			}
		}
		function _error(e,$t,error){
			if(!error){
				_getErrorFn($t)(e);
			}else{
				error.call($t,e);
			}
			e.$target=$t;
			sumDo('error',e);
		}
		function _succ(e,$t,succ){
			if(!succ){
				_getSuccFn($t)(e);
			}else{
				succ.call($t,e);
			}
			e.$target=$t;
			sumDo('right',e);
		}


		function sumThenDo(num,fn){
			var e={};
			e.$error=[];
			e.$right=[];
			e.isAllVia=true;
			return function(key,val){
				num--;
				if(key==='error'){
					e.$error.push(val);
					e.isAllVia=false;
				}else if(key==='succ'){
					e.$right.push(val);
				}
				if(num===0){
					fn(e);
				}
			}
		}
	}
})($,window);

(function($){
	$.formBlurValid=function(_t){
		return new formBlurValid(_t);
	}
	function formBlurValid(_t){
		this.$form=$(_t);
		this.initBlurEvent=function(){
			this.$form.find('[vs-blur]').each(function(){
				if($(this).data('vs-blur-event')){
					return;
				}
				$(this).data('vs-blur-event',true);
				if($(this).hasClass('comp_select')){
					$(this).selectComp('closeSelectBefore',function(e){
						blurVilade.call(this);
						e.canClose();
					});
				}else if($(this).hasClass('comp_input')){
					var $t=$(this);
					$(this).find('input,textarea').on('blur',function(){
						blurVilade.call($t);
					});
				}else if($(this).attr('redefine')==='allVule'||$(this).attr('redefine')==="onlyOneVal"){
					var $t=$(this);
					$(this).find('input').on('blur',function(){
						blurVilade.call($t);
					});
				}else{
					$(this).on('blur',function(){
						blurVilade.call(this);
					});
				}
			});
		}
		this.initBlurEvent();
		function blurVilade(){
			$(this).validate({
				filter:function(){
					return commonFilter.call(this);
				},
				val:function(){
					return getValiVal.call(this);
				},
				error:function(e){
					addViError.call(this,e);
					addRemoveError.call(this);
				},
				succ:function(e){
				}
			});
		}
	}

	$(function(){
		$('form').each(function(){
			var obj=$.formBlurValid($(this));
			$(this).data('vs-blur',obj);
		})
	})
})($);

function formSubMitVilade(_t,succ_clk,error_clk){
	$(_t).parents('form').validate({
		filter:function(){
			return commonFilter.call(this);
		},
		val:function(){
			return getValiVal.call(this);
		},
		error:function(e){
			addViError.call(this,e);
			addRemoveError.call(this);
		},
		succ:function(e){
			console.log('222');
		},
		rslt:function(e){
			// clk(e);
			if(!e.isAllVia){
				if(typeof error_clk==='function'){
					error_clk.call(this);
				}else{
					var top=parseInt($(e.$error[0].$target).offset().top);
					var h=parseInt($(window).height());
					top=top-h/3;
					$('html,body').animate({scrollTop:top>0?top:0},350);
				}
			}else{
				if(typeof succ_clk==='function'){
					succ_clk.call(this);
				}else{
					$(this).submit();
				}
			}
		}
	});
}
function commonFilter(){
	if($(this).css('display')==='none'||$(this).parents(':hidden').length>0){
		return false;
	}
}
function getValiVal(){
	if($(this).hasClass('comp_select')){
		return $(this).children('input').val();
	}
	if($(this).hasClass('comp_input')){
		var $in=$(this).find('input');
		var $tx=$(this).find('textarea');
		return $in.length===1?$in.val():$tx.length===1?$tx.val():'';
	}
	if($(this).hasClass('comp_radio')){
		return $(this).find('input:checked').val();
	}
	if($(this).hasAttr('redefine')){
		var type=$(this).attr('redefine');
		if(type==='allVule'){
			var val='';
			$(this).find('input').each(function(){
				if(val===''){
					val=$(this).val();
				}else{
					val+='-'+$(this).val();
				}
			});
			return val;
		}
		if(type==='onlyOneVal'){
			var val='';
			$(this).find('input').each(function(){
				if($(this).val()!==''){
					val=$(this).val();
				}
			});
			return val;
		}
	}
}

function addViError(e){
	var $p = $(this).parent();
	$p.addClass('error');
	var $erTxt = $p.find('.error_txt');
	if($erTxt.isExist()){
		$erTxt.html(e.tip);
	}else{
		$(this).after('<div class="error_txt">'+e.tip+'</div>');
		// var l=$(this).offset().left;
		// var t=$(this).offset().top;
		// var w=$(this).outerWidth(true);
		// var $errorLogo=$('<img class="error_logo" src="'+$.getImagesUrl('ico/error_logo.png')+'">');
		// $(this).after($errorLogo);
		// $errorLogo.offset({
		// 	left:l+w+5,
		// 	top:t+5
		// })
	}
}

function addRemoveError(){
	var $p = $(this).parent();
	if($(this).hasClass('comp_select')){
		$(this).selectComp('openSelect',function(){
			$p.removeClass('error');
			$p.find('.error_txt').remove();
		});
	}else if($(this).attr('redefine')==='allVule'){
		$p.find('input').on('focus',removeState);
		function removeState(){
			$p.removeClass('error');
			$p.find('.error_txt').remove();
			$p.find('input').off('focus',removeState);
		}
	}else if($(this).hasClass('comp_input')){
		$(this).find('input,textarea').one('focus',function(){
			$p.removeClass('error');
			$p.find('.error_txt').remove();
		});

	}else{
		$(this).one('focus',function(){
			$p.removeClass('error');
			$p.find('.error_txt').remove();
		});
	}
}