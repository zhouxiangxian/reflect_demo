<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://www.mldn.cn/c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/" ;
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>微商城综合实战</title>
    <link type="text/css" rel="stylesheet" href="css/mldn.css">
    <script type="text/javascript" src="js/mldn.js"></script>
</head>
<body>
<h1>对不起，程序出现了错误，请与管理员联系！</h1>
<c:if test="${errors != null}">
	<ul>
		<c:forEach items="${errors}" var="err">
			<li>${err.key} = ${err.value}</li>
		</c:forEach>
	</ul>
</c:if>
<h1><a href="<%=basePath%>index.jsp">回到首页</a>
</html>
