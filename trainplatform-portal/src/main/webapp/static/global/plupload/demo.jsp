<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<jsp:include page="/common/baseJsp.jsp" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/upload/js/plupload.js"></script>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/common/plupload/jquery.plupload.queue/css/jquery.plupload.queue.css" type="text/css" media="screen" />
	<!-- production -->
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plupload/plupload.full.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plupload//jquery.plupload.queue/jquery.plupload.queue.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath() %>/ck/ckplayer/ckplayer.js"></script>
  	<script type="text/javascript">
		$(function(){
			//iniUploadFile(id,tableName,tableCol,tableId)
			plupload.iniUploadFile("up","tableName","tableCol","321");
// 		var param = "varTableName=RC_Media&varTableCol=media_file&intTableId=36f52ab1-f287-4061-9a27-570aad70ae2c&varFileName=脚本详情-关联素材1.jpg";
// 		var url = appPath + "/rc/upload/downByName?" + param;
// 		$("#validateCodeImg").attr("src", appPath + "/rc/upload/fileByName?" + param);

				
				var flashvars1 = {
					f : '/ResourceCenter/rc/upload/fileByName?varNewFileName=9e64224c8f11462bb48e1040e5ac0a95.mp4',
					s : '0',
					c : '0',
					i : '/ResourceCenter/ck/temp/1.jpg',
					t : '10|10',
					e : '2',
					v : '80',
					p : '0',
					h : '0',
					m : '1',
					ct : '2' 
				};
				var params = {
					bgcolor : '#FFF',
					allowFullScreen : true,
					allowScriptAccess : 'always',
					wmode:'transparent'
				};
				var support = [ 'iPad', 'iPhone', 'ios', 'android+false','msie10+false' ];
				var attributes1 = {
					id : 'ckplayer_a1',
					name : 'ckplayer_a1',
					menu : 'false'
				};
				CKobject.embedSWF('/ResourceCenter/ck/ckplayer/ckplayer.swf', 'video', 'ckplayer_video','100%','100%', flashvars1, params);
		
		});
		//重写此方法
		function backUpCallBack(id, data){
			if(id == "up"){
				console.log(data);
			}
		}
		
	</script>
  </head>
  <body style="width: 1000px;height:1000px;">
		<button id="up">上传</button>
		<img id="validateCodeImg" src="" width="68" height="23"/>
		<div id="video"></div>
  </body>
</html>