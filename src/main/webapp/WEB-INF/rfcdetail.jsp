<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basepath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Rfc${rfc.id }</title>
</head>
<body>
<%@include file="/WEB-INF/include/menu.jsp" %>
查看RFC文档&nbsp;>>&nbsp;RFC${rfc.id }<br/>
<table border="1">
<tr><td>RFC编号</td><td>RFC${rfc.id }</td></tr>
<tr><td>标题</td><td>${rfc.title }</td></tr>
<tr><td>作者</td><td>${rfc.author }</td></tr>
<tr><td>原始信息</td><td>${rfc.origInfo }</td></tr>
</table>
<a href="rfc/${rfc.id }/1">查看文档</a>
</body>
</html>