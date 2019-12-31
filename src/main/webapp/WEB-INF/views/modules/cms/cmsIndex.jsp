<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>内容管理</title>
<meta name="decorator" content="lte" />
<!-- ztree -->
<link rel="stylesheet" href="${ctxStaticLTE}/plugins/ztree/zTreeStyle.css">
<script src="${ctxStaticLTE}/plugins/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
<style type="text/css">
.ztree {
	padding: 0
}

.ztree * {
	font-size: 14px
}

.ztree li ul {
	margin: 0;
	padding: 0
}

.ztree li {
	line-height: 40px;
}

.ztree li a {
	width: 180px;
	height: 40px;
	padding-top: 0px;
	text-indent: 10px;
}

.ztree li a:hover {
	text-decoration: none;
	background-color: #f4f6f8;
}

.ztree li a span.button.switch {
	visibility: hidden
}

.ztree.showIcon li a span.button.switch {
	visibility: visible
}

.ztree li a.curSelectedNode {
	background-color: #FFF;
	border: 0;
	height: 40px;
}

.ztree li span {
	line-height: 40px;
}

.ztree li span.button {
	margin-top: -2px;
}

.ztree li span.button.switch {
	width: 18px;
	height: 18px;
}

.ztree li span.button {
	background-image: url("${ctxStaticLTE}/dist/img/left_menuForOutLook.png");
}

.ztree li span.button.switch.level0 {
	width: 20px;
	height: 20px
}

.ztree li span.button.switch.level1 {
	width: 20px;
	height: 20px
}

.ztree li span.button.noline_open {
	background-position: 0 0;
}

.ztree li span.button.noline_close {
	background-position: -18px 0;
}

.viewbar {
	position: absolute;
	bottom: 51px;
	height: auto;
}
</style>

</head>
<body>
	<div id="content" class="row-fluid">
		<div id="left">
			<div class="viewbar" style="width: 180px;">
				<h3>我的栏目</h3>
				<ul id="tree" class="ztree showIcon"></ul>
			</div>
		</div>
		<div class="viewbar-collapse open">
			<i class="fa fa-outdent"></i> <i class="fa fa-indent"></i>
		</div>
		<div class="viewbody" style="min-height: 561px; margin-left: 180px;">
			<iframe id="iframepage" name="iframepage" src="${frameUrl }" class="framebody" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
		</div>
	</div>

	<%@include file="/WEB-INF/views/include/jsfile.jsp"%>

	<script type="text/javascript">
	
		$(document).ready(function(){
			
			var setting = {
		            view: {
		                showLine: false,
		                showIcon: false,
		                selectedMulti: false,
		                dblClickExpand: false,
		                addDiyDom: function (treeId, treeNode) {
		                    var spaceWidth = 10;
		                    var switchObj = $("#" + treeNode.tId + "_switch"),
		                        icoObj = $("#" + treeNode.tId + "_ico");
		                    switchObj.remove();
		                    icoObj.before(switchObj);

		                    if (treeNode.level > 0) {
		                        var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level) + "px'></span>";
		                        switchObj.before(spaceStr);
		                    }
		                }
		            },
		            data: {
		            	simpleData: {
							enable: true
						}
		            },
		            callback: {
		                onClick: function (event, treeId, treeNode) {
		                    $("#iframepage").attr("src",treeNode.liUrl);
		                }
		            }
		        };
			
			var zNodes=[<c:forEach items="${categoryList}" var="tpl">
						{id:'${tpl.id}', pId:'${not empty tpl.parent?tpl.parent.id:0}', name:"${tpl.name}", liUrl:"${ctx}/cms/${not empty tpl.module?tpl.module:'none'}/?category.id=${tpl.id}"},
		            	</c:forEach>];

			// 初始化树结构
			var tree = $.fn.zTree.init($("#tree"), setting, zNodes);
			tree.expandAll(true); 
			var nodes = tree.getNodes();
			
			
            /* //父节点不能选择
            var nodes = tree.transformToArray(tree.getNodes());
            for (var i=0, length=nodes.length; i < length; i++) {
              if (nodes[i].isParent){
            	  tree.setChkDisabled(nodes[i], true);
              } 
            } */

			
			var currCatId = '${categoryId}'; //编辑后跳转回来
			if(currCatId == ''){
				tree.selectNode(nodes[0]);
				$("#iframepage").attr("src",nodes[0].liUrl);
			} else {
				var node = tree.getNodeByParam("id", currCatId, null);
				tree.selectNode(node);
			}
		});
	</script>

</body>
</html>