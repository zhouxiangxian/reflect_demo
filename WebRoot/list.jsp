<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'index.jsp' starting page</title>
<link type="text/css" rel="stylesheet" href="css/mldn.css">
    
</head>

<body>
<div id="splitSearchDiv">
	<jsp:include page="/pages/split_page_plugin_search.jsp"/>
	<br>
</div>
<h1>数据</h1>
<div id="splitBarDiv" style="float:right">
	<jsp:include page="/pages/split_page_plugin_bars.jsp"/>
</div>
</body>
</html>
