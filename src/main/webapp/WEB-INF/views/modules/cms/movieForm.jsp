<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>视频管理</title>
<meta name="decorator" content="lte" />
<style type="text/css">
.form-information .btn-default {
	height: 34px;
	padding: 5px 12px;
	font-size: 14px;
}

.form-information .btn label {
	margin: 0;
}

.form-information .chbox {
	margin: 0;
	vertical-align: middle
}

.zdInn {
	position: relative;
	padding-left: 150px;
}

.zdInn .zdtxt {
	position: absolute;
	left: 15px;
	top: 7px;
}

.zdInn .zdtxt label {
	margin-right: 10px;
}
</style>
</head>
<body>
	<section class="content-header">
		<h1>
			视频管理 <small>视频发布</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="${ctx }"><i class="fa fa-dashboard"></i> 首页</a></li>
			<li><a href="${ctx}/cms/movie">视频信息</a></li>
			<li class="active">视频发布</li>
		</ol>
	</section>

	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info" style="padding-right:20px;padding-top:20px;">
					<form:form id="inputForm" modelAttribute="movie" action="${ctx}/cms/movie/save" method="post" class="form-horizontal form-information form-lie">
						<form:hidden path="id" />
						<sys:message content="${message}" />
						<div class="box-body pad">
							<div class="form-group">
								<label class="col-sm-1 col-sm-1 control-label"><span class="text-red fm">*</span> 归属栏目:</label>
								<div class="col-sm-2">
									<sys:treeselect id="category" name="category.id" value="${movie.category.id}" labelName="category.name" labelValue="${movie.category.name}" title="栏目" url="/cms/category/treeData"
										module="movie" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="form-control required" />
									&nbsp;
								</div>

							</div>
							<div class="form-group">
								<label for="" class="col-sm-1 control-label"><span class="text-red fm">*</span> 视频名称：</label>
								<div class="col-sm-4">
									<form:input path="title" htmlEscape="false" maxlength="200" class="form-control required has-ruler required" id="cp3text" style="color: ${movie.color}" />
								</div>
								<div class="col-sm-1">
									<input id="cp3" name="cp3" type="text" class="input_colorpicker" readonly="" style="background-color: rgb(0, 0, 0);z-index: 9999">
									<form:hidden path="color" htmlEscape="false" maxlength="200" class="form-control required has-ruler required" id="cp3textcolor" />
								</div>
							</div>


							<%-- 
							<div class="form-group">
								<label class="col-sm-1 control-label">视频上传:</label>
								<div class="col-sm-4">
									<input type="hidden" id="link" name="link" value="${movie.link}" />
									<sys:ckfinder input="link" type="files" uploadPath="/files" selectMultiple="false" maxWidth="100" maxHeight="100" />
								</div>
							</div> 
							--%>


							<div class="form-group">
								<label class="col-sm-1 control-label">视频上传:</label>
								<div class="col-sm-6">
									<input type="hidden" id="link" name="link" value="${movie.link}" />
									<cms:playVideo videoUrl="${movie.link}" style="width:500px;height:300px;" />
									<div id="uploader" class="wu-example" style="margin-top:10px;">
										<div id="thelist" class="uploader-list"></div>
										<div class="btns" style="">
											<div id="picker" style="width:150px;"></div>
											<button id="ctlBtn" type="button" class="btn btn-danger" style="padding:10px 15px;border-radius:3px;">开始上传</button>
										</div>
									</div>
									<div class="text-muted" style="clear:left;margin-top:10px;">
										<p class="">提示： 1、上传文件类型为：${fns:getSysConfigWithDefaultValue("cms.movie.fileType", "mp4")}；上传文件大小不超过${fns:getSysConfigWithDefaultValue("cms.movie.fileSize", 500)}M。
											2、选择文件之后，需要点击“开始上传”按钮才会正式开始上传。如果上传过程出现中断，刷新页面再次选择文件续传即可。</p>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label">视频截图:</label>
								<div class="col-sm-4">
									<input type="hidden" id="image" name="image" value="${movie.image}" />
									<sys:ckfinder input="image" type="images" uploadPath="/video/poster" selectMultiple="false" />
								</div>
							</div>



							<div class="form-group">
								<label class="col-sm-1 control-label">权重:</label>
								<div class="col-sm-1">
									<form:input path="weight" id="weight" htmlEscape="false" maxlength="200" class="form-control required" />
								</div>
								<div class="col-sm-3 zdInn">
									<span class="zdtxt"> <label> <input type="checkbox" id="weightTop" class="chbox" onclick="$('#weight').val(this.checked?'999':'0')">置顶
									</label> <label for="">过期时间：</label>
									</span>
									<div class="input-group date">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input id="weightDate" name="weightDate" type="text" maxlength="20" class="form-control pull-left" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});">
									</div>
								</div>
								<div class="col-sm-5 text-muted">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</div>
							</div>


							<shiro:hasPermission name="cms:movie:audit">
								<div class="form-group">
									<label class="col-sm-1 control-label">发布状态:</label>
									<div class="col-sm-7 radio">
										<c:forEach items="${fns:getDictList('cms_del_flag')}" var="dic" varStatus="status">
											<label style="margin-right:10px"> <input type="radio" name="delFlag" id="delFlag" value="${dic.value}" <c:if test="${dic.value == article.delFlag }">checked="true"</c:if>
												htmlEscape="false" class="required">${dic.label}
											</label>
										</c:forEach>
									</div>
								</div>
							</shiro:hasPermission>

							<div class="form-group">
								<div class="col-sm-offset-1 col-sm-11">
									<shiro:hasPermission name="cms:movie:edit">
										<input id="btnSubmit" class="btn btn-primary" style="width:90px;margin-right:10px" type="submit" value="保 存" />&nbsp;</shiro:hasPermission>
									<input id="btnCancel" class="btn btn-default" type="button" value="返 回" onclick="history.go(-1)" />
								</div>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
	<!-- /.content-wrapper -->
	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>
	<script type="text/javascript">
		$(function() {
			// 按钮鼠标停留效果
			$('.form-information button').tooltip();
			$(".select2").select2();
			if ($("#movie").val()) {
				$('#movieBody').show();
				$('#url').attr("checked", true);
			}
			$("#title").focus();
			$("#inputForm").validate({
				submitHandler : function(form) {
					if ($("#categoryId").val() == "") {
						$("#categoryName").focus();
						top.$.jBox.tip('请选择归属栏目', 'warning');
					} else if (CKEDITOR.instances.content.getData() == "" && $("#movie").val().trim() == "") {
						top.$.jBox.tip('请填写正文', 'warning');
					} else {
						loading('正在提交，请稍等...');
						form.submit();
					}
				},
				errorContainer : "#messageBox",
				errorPlacement : function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
	
			//颜色选择器
			$('#cp3').colpick({
				colorScheme : 'dark',
				layout : 'rgbhex',
				color : '000000',
				onSubmit : function(hsb, hex, rgb, el) {
					$(el).css('background-color', '#' + hex);
					$(el).colpickHide();
					$("#cp3text").css("color", '#' + hex);
					$("#cp3textcolor").val('#' + hex);
				}
			});
		});
	</script>












	<link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/css/webuploader.css">
	<script type="text/javascript" src="${ctxStatic}/webuploader/js/webuploader.min.js"></script>
	<script>
	    var $btn = $('#ctlBtn');
	    var $thelist = $('#thelist');
	    
	    var chunkSize = ${fns:getSysConfigWithDefaultValue("chunkUpload.chunk.size",5242880)};
	    var checkFileMd5Url = '${ctx}/cms/movie/chunkupload/checkFile';
	    var fileUploadUrl = '${ctx}/cms/movie/chunkupload/fileUpload';
	    var fileNumLimit = 1;
	    var sizeLimit = ${fns:getSysConfigWithDefaultValue("cms.movie.fileSize", 500)};
	    var fileSizeLimit = sizeLimit * 1024 * 1024;
	    var fileSingleSizeLimit = sizeLimit * 1024 * 1024;
		var extensions = '${fns:getSysConfigWithDefaultValue("cms.movie.fileType", "mp4")}';
	    // 
	    // 这个必须要再uploader实例化前面
	    WebUploader.Uploader.register({
	        'before-send-file': 'beforeSendFile',
	        'before-send': 'beforeSend'
	    }, {
	        beforeSendFile: function (file) {
	            var task = new $.Deferred();
	            uploader.md5File(file).progress(function (percentage) {   // 及时显示进度
	                getProgressBar(file, percentage, "MD5", "校验文件进度");
	            }).then(function (val) { 
	            	tipSuccess("文件校验完成，开始上传！",500);
	            	setTimeout(function(){
	            		fadeOutProgress(file,"MD5");
	    	        },3000);
	                file.md5 = val;
	                file.uid = "${fns:getUser().id}";
	                file.mid = "${movie.id}";
	                // 进行md5判断
	                $.post(checkFileMd5Url, {uid: file.uid, md5: file.md5},function (ret) {
	                	if (ret.state && ret.state == 1) {// 成功
	                		var data = ret.data;
		                     var status = data.uploadStatus;
		                     if (status == "NONE") {
		                         // 文件不存在，那就正常流程
		                    	 task.resolve();
		                     } else if (status == "ALL") {
		                    	// 文件已上传过时
		                    	// 1、重新上传
			                	task.resolve();
		                    	/*
		                    	// 2、不再上传
		                    	tipSuccess("您已经上传过此文件，不再上传！",3000);
			                	uploader.skipFile(file);
		                        file.pass = true;
		                        // 删除队列中已上传的文件
	        				    $('#' + file.id).remove();
	        				    uploader.removeFile(uploader.getFile(file.id));
	        				    task.reject();  
	        				    */
		                     } else if (status == "HALF") {
		                         // 部分已经上传到服务器了，但是差几个模块。
		                         file.missChunks = data.missChunks;
		                         task.resolve();
		                     }
	        			} else {// 失败
	        				if (ret.msg != null && ret.msg != "") {
	        					//提示错误信息
	        					alertError(ret.msg, function(){
	        						// 删除队列中已上传的文件
	        				        $('#' + file.id).remove();
	        				        uploader.removeFile(uploader.getFile(file.id));
	        					}); 
	        				}
	        				task.reject();
	        			}
	            	});
	            });
	            return $.when(task);
	        },
	        beforeSend: function (block) {
	            var task = new $.Deferred();
	            var file = block.file;
	            var missChunks = file.missChunks;
	            var blockChunk = block.chunk;
	            if (missChunks !== null && missChunks !== undefined && missChunks !== '') {
	                var flag = true;
	                for (var i = 0; i < missChunks.length; i++) {
	                    if (blockChunk == missChunks[i]) {
	                        flag = false;
	                        break;
	                    }
	                }
	                if (flag) {
	                    task.reject();
	                } else {
	                    task.resolve();
	                }
	            } else {
	                task.resolve();
	            }
	            return $.when(task);
	        }
	    });
	
	    // 实例化
	    var uploader = WebUploader.create({
	        pick: {
	            id: '#picker',
	            multiple:false, 
	            label: '选择文件。。。'
	        },
	        formData: {
	            uid: '',
	            md5: '',
	            chunkSize: chunkSize,
	            mid : ''
	        },
	        //dnd: '#dndArea',
	        //paste: '#uploader',
	        swf: 'js/Uploader.swf',
	        chunked: true,
	        chunkSize: chunkSize, // 分块大小，单位：字节 
	        chunkRetry: 10,
	        threads: 1,
	        server: fileUploadUrl,
	        auto: false,
	        duplicate :true,
	        accept: {
                title: '视频文件',
               	mimeTypes: 'video/mp4,video/avi,video/quicktime',
                extensions: extensions
            },
	        // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
	        disableGlobalDnd: true,
	        fileNumLimit: fileNumLimit, // 文件数量限制
	        fileSizeLimit: fileSizeLimit,    // 文件大小限制
	        fileSingleSizeLimit: fileSingleSizeLimit // 单个文件大小限制
	    });
	    
	    // 当文件被加入队列之前触发
	    uploader.on('beforeFileQueued', function (file) {
	        // 上传队列只保留最后一个待上传文件
	        if(uploader.getFiles().length > 0 ){ 
	        	uploader.reset();
	        	$thelist.empty();
	        }
	        return true;
	    });
	    
	    // 当有文件被添加进队列的时候 
	    uploader.on('fileQueued', function (file) {
	        $thelist.append('<div id="' + file.id + '" class="item">' +
	                '<h5 class="info">已选择文件：' + file.name + '</h5>' +
	                '<p class="state text-green">点击“开始上传”按钮上传文件</p>' +
	                '</div>');
	    });
	
	    //当某个文件的分块在发送前触发，主要用来询问是否要添加附带参数，大文件在开起分片上传的前提下此事件可能会触发多次。
	    uploader.onUploadBeforeSend = function (obj, data) {
	        var file = obj.file;
	        data.md5 = file.md5 || '';
	        data.uid = file.uid;
	        data.min = file.mid;
	    };
	    // 上传中
	    uploader.on('uploadProgress', function (file, percentage) {
	        getProgressBar(file, percentage, "FILE", "上传进度");
	    });
	    // 上传返回结果
	    uploader.on('uploadSuccess', function (file, response) {
	        var text = '已上传';
	        if (file.pass) {
	            text = "文件妙传功能，文件已上传。"
	        }
	        $('#' + file.id).find('p.state').text(text);
	        var data = response.data;
	        if(data && data.videoUrl){
		        tipSuccess("文件上传成功！");
	        }
	        // 删除队列中已上传的文件
	        $('#' + file.id).remove();
	        uploader.removeFile(uploader.getFile(file.id));
	        //
	        if(data && data.videoUrl){
		        $("#link").val(data.videoUrl);
		        $("#image").val(data.posterUrl);
		        changeVideo('${ctxPath}'+data.videoUrl,'${ctxPath}'+data.posterUrl);
		        $("#imagePreview img").attr("src",'${ctxPath}'+data.posterUrl).attr("url",'${ctxPath}'+data.posterUrl);
	        }
	    });
	    uploader.on('uploadError', function (file, reason) {
	       // console.log("uploadError , reason : " + reason);
	    	$('#' + file.id).find('p.state').text('上传出错');
	    });
	    // 不管成功或者失败，文件上传完成时触发。
	    uploader.on('uploadComplete', function (file) {
	        // 隐藏进度条
	        setTimeout(function(){
		        fadeOutProgress(file, 'FILE');
	        },3000);
	    });
	    // 不管成功或者失败，文件上传完成时触发。
	    uploader.on('error', function (type) {
	    	if("Q_EXCEED_NUM_LIMIT"==type){
	    		alertError("文件数量超过限制，最多："+fileNumLimit+"个文件");
	    	}else if("Q_EXCEED_SIZE_LIMIT"==type){
	    		alertError("文件大小超过限制，最大："+sizeLimit+"M");
	    	}else if("Q_TYPE_DENIED"==type){
	    		alertError("文件类型不符合，要求："+extensions);
	    	}
	    });
	    
	    uploader.on("uploadAccept", function(object, ret){
	    	if (ret.state && ret.state == 1) {// 成功
				return true;
			} else {// 失败
				if (ret.msg != null && ret.msg != "") {
					//提示错误信息
					alertError(ret.msg, function(){
						// 删除队列中已上传的文件
				        $('#' + object.file.id).remove();
				        uploader.removeFile(uploader.getFile(object.file.id));
					}); 
				}
				return false;
			}
	  	}); 
	    
	    // 文件上传
	    $btn.on('click', function () {
	    	
	        uploader.upload();
	    });
	
	    /**
	     *  生成进度条封装方法
	     * @param file 文件
	     * @param percentage 进度值
	     * @param id_Prefix id前缀
	     * @param titleName 标题名
	     */
	    function getProgressBar(file, percentage, id_Prefix, titleName) {
	        var $li = $('#' + file.id), $percent = $li.find('#' + id_Prefix + '-progress-bar');
	        // 避免重复创建
	        if (!$percent.length) {
	            $percent = $('<div id="' + id_Prefix + '-progress" class="progress progress-striped active">' +
	                    '<div id="' + id_Prefix + '-progress-bar" class="progress-bar" role="progressbar" style="width: 0%">' +
	                    '</div>' +
	                    '</div>'
	            ).appendTo($li).find('#' + id_Prefix + '-progress-bar');
	        }
	        var progressPercentage = parseInt(percentage * 100) + '%';
	        $percent.css('width', progressPercentage);
	        $percent.html(titleName + '：' + progressPercentage);
	    }
	
	    /**
	     * 隐藏进度条
	     * @param file 文件对象
	     * @param id_Prefix id前缀
	     */
	    function fadeOutProgress(file, id_Prefix) {
	        $('#' + file.id).find('#' + id_Prefix + '-progress').fadeOut();
	    }
	     
	</script>
</body>
</html>