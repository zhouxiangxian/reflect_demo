<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
   <form action="dept/insert" method="post" enctype="multipart/form-data">
       部门编号:<input type="text" name="dept.deptno"  id="dept.deptno" value="10000"><br>
       部门名称:<input type="text" name="dept.dname" id="dept.dname"  value="项目四部"><br>
       位置字段:<input type="checkbox" name="dept.loc" id="dept.loc" value="1">第一位置
       <input type="checkbox" name="dept.loc" id="dept.loc" value="2">第二位置
       <input type="checkbox" name="dept.loc" id="dept.loc" value="3">第三位置
       <input type="checkbox" name="dept.loc" id="dept.loc" value="4">第四位置
       <input type="checkbox" name="dept.loc" id="dept.loc" value="5">第五位置<br>
       公司名称:<input type="text" name="dept.company.title" id="dept.company.title" value="梦网科技"><br>
       部门照片：<input type="file" name="photo"><br>
       <input type="submit" value="提交">
       <input type="reset" value="重置">
       <input type="hidden" id="ids" name="ids" value="1|2|3|4">
   </form>
  </body>
</html>
