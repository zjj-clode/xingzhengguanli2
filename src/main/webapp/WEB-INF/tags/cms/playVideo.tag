<%-- 视频播放tag，集成ckplayer播放器 --%>
<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" body-content="empty"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="false" description="视频播放div的id，默认为video，如果会冲突请指定。"%>
<%@ attribute name="style" type="java.lang.String" required="false" description="视频播放div的style"%>
<%@ attribute name="videoUrl" type="java.lang.String" required="true" description="视频地址"%>
<%@ attribute name="poster" type="java.lang.String" required="false" description="视频封面图片地址"%>
<%@ attribute name="loop" type="java.lang.Boolean" required="false" description="是否循环播放，默认false"%>
<%@ attribute name="autoplay" type="java.lang.Boolean" required="false" description="是否自动播放，默认false"%>
<c:if test="${not empty videoUrl and not fns:startsWith(videoUrl,'http://') and not fns:startsWith(videoUrl,'https://') and not fns:startsWith(videoUrl,ctxPath)}">
	<c:set var="videoUrl" value="${ctxPath.concat(videoUrl)}"/>
</c:if>
<div id="${empty id ? 'video' : id}" style="width:100%;height:100%;${style}"></div>
<script type="text/javascript" src="${ctxStatic}/ckplayer/ckplayer.min.js" charset="UTF-8"></script>
<script type="text/javascript">
	var player = new ckplayer({
		container: '#${empty id ? 'video' : id}',
		variable: 'player',
		loop: ${empty loop ? false : loop},
		autoplay: ${empty autoplay ? false : autoplay},
<c:if test="${not empty poster}">poster: '${movie.image}',</c:if>
		video:'${videoUrl}'
	});
	function changeVideo(videoUrl,posterUrl) {
		if(player == null) {
			return;
		}
		var newVideoObject = {
			container: '#${empty id ? 'video' : id}',
			variable: 'player',
			loop: ${empty loop ? false : loop},
			autoplay: ${empty autoplay ? false : autoplay},
			loaded: 'loadedHandler', //当播放器加载后执行的函数
			video: videoUrl,
			poster: posterUrl
		}
		//判断是需要重新加载播放器还是直接换新地址
		if(player.playerType == 'html5video') {
			if(player.getFileExt(videoUrl) == '.flv' || player.getFileExt(videoUrl) == '.m3u8' || player.getFileExt(videoUrl) == '.f4v' || videoUrl.substr(0, 4) == 'rtmp') {
				player.removeChild();
				player = null;
				player = new ckplayer();
				player.embed(newVideoObject);
			} else {
				player.newVideo(newVideoObject);
			}
		} else {
			if(player.getFileExt(videoUrl) == '.mp4' || player.getFileExt(videoUrl) == '.webm' || player.getFileExt(videoUrl) == '.ogg') {
				player = null;
				player = new ckplayer();
				player.embed(newVideoObject);
			} else {
				player.newVideo(newVideoObject);
			}
		}
	}
</script>