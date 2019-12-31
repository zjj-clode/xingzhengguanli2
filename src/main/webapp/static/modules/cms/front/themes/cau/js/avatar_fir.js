
(function($,undefined){
	$.fn.avatar = function(options,param){
		var otherArgs = Array.prototype.slice.call(arguments, 1);
		if (typeof options == 'string') {
			var fn = this[0][options];
			if($.isFunction(fn)){
				return fn.apply(this, otherArgs);
			}else{
				throw ("avatar - No such method: " + options);
			}
		}

		return this.each(function(){
			var self = this;  // 保存组件对象
			
			var para = {
					/* 提供给外部的接口方法 */
					onSelect         : function(selectFile){},// 选择文件的回调方法  selectFile:当前选中的文件  allFiles:还没上传的全部文件
					onDelete		 : function(file){},     // 删除一个文件的回调方法 file:当前删除的文件  files:删除之后的文件
			};
			
			this.init = function(){
				this.addEvent();  // 初始化html之后绑定按钮的点击事件
				this.createCorePlug();  // 调用核心js
			};
			
			
			/**
			 * 功能：过滤上传的文件格式等
			 * 参数: files 本次选择的文件
			 * 返回: 通过的文件
			 */
			this.funFilterEligibleFile = function(files){
				var arrFiles = [];  // 替换的文件数组
				for (var i = 0, file; file = files[i]; i++) {
					var ext = ['.gif', '.jpg', '.jpeg', '.png', '.bmp'];
					var s = file.name.toLowerCase();
					var r = false;
					for(var i = 0; i < ext.length; i++){
						if (s.indexOf(ext[i]) > 0){
							r = true;
							break;
						}
					}
					if(!r){
						alert('您这个"'+ file.name +'"文件类型不符（只能为：gif,jpg,jpeg,png,bmp）');
					}else{
						if (file.size >= 51200000) {
							alert('您这个"'+ file.name +'"文件大小过大');	
						} else {
							// 在这里需要判断当前所有文件中
							arrFiles.push(file);	
						}
					}
				}
				return arrFiles;
			};
			
			/**
			 * 功能： 处理参数和格式上的预览html
			 * 参数: files 本次选择的文件
			 * 返回: 预览的html
			 */
			this.funDisposePreviewHtml = function(file, e){
				var html = '<div id="uploadList" class="upload_append_list">';
					html += '	<div class="file_bar" id="file_bar">';
					html += '		<div style="padding:5px;">';
					html += '			<p class="file_name">' + file.name + '</p>';
					html += '           <span class="file_del" id="file_del" title="删除"></span>';
					html += '		</div>';
					html += '	</div>';
					html += '	<a style="height:260px;width:560px;" href="#" class="imgBox">';
					html += '		<div class="uploadImg" style="width:540px;height:240px">';				
					html += '			<img class="upload_image"  src="' + e.target.result + '" />';                                                                 
					html += '		</div>';
					html += '	</a>';
					html += '</div>';
                	
				
				return html;
			};
			
			/**
			 * 功能：调用核心插件
			 * 参数: 无
			 * 返回: 无
			 */
			this.createCorePlug = function(){
				var params = {
					fileInput: $("#fileImage").get(0),
					
					filterFile: function(files) {
						// 过滤合格的文件
						return self.funFilterEligibleFile(files);
					},
					onSelect: function(selectFiles) {
						var html = '', i = 0;
						// 组织预览html
						var funDealtPreviewHtml = function() {
							file = selectFiles[i];
							if (file) {
								var reader = new FileReader()
								reader.onload = function(e) {
									// 处理下配置参数和格式的html
									html += self.funDisposePreviewHtml(file, e);
									
									i++;
									// 再接着调用此方法递归组成可以预览的html
									funDealtPreviewHtml();
								}
								reader.readAsDataURL(file);
							} else {
								// 走到这里说明文件html已经组织完毕，要把html添加到预览区
								funAppendPreviewHtml(html);
							}
						};
						
						// 添加预览html
						var funAppendPreviewHtml = function(html){
							// 添加到添加按钮前
							if(html.length != 0){
								$("#add_upload").hide();
							}
							$("#add_upload").before(html);
							// 绑定删除按钮
							funBindDelEvent();
							funBindHoverEvent();
						};
						
						// 绑定删除按钮事件
						var funBindDelEvent = function(){
							if($("#file_del").length>0){
								// 删除方法
								$("#file_del").click(function() {
									ProcessFile.funDeleteFile();	
									return false;	
								});
							}
						};
						
						// 绑定显示操作栏事件
						var funBindHoverEvent = function(){
							$("#uploadList").hover(
								function (e) {
									$(this).find("#file_bar").addClass("file_hover");
								},function (e) {
									$(this).find("#file_bar").removeClass("file_hover");
								}
							);
						};
						
						funDealtPreviewHtml();					
					},
					onDelete: function(file) {
						// 移除效果
						$("#uploadList").remove();
						$("#fileImage").val("");
						$("#add_upload").show();
						
					},
					onDragOver: function() {alert();
						$(this).addClass("upload_drag_hover");
					},
					onDragLeave: function() {
						$(this).removeClass("upload_drag_hover");
					}

				};
				
				ProcessFile = $.extend(ProcessFile, params);
				ProcessFile.init();
			};
			
			/**
			 * 功能：绑定事件
			 * 参数: 无
			 * 返回: 无
			 */
			this.addEvent = function(){
				// 如果快捷添加文件按钮存在
				if($("#rapidAddImg").length > 0){
					// 绑定添加点击事件
					$("#rapidAddImg").bind("click", function(e){
						$("#fileImage").click();
		            });
				}
			};
			
			
			// 初始化上传控制层插件
			this.init();
		});
	};
})(jQuery);

