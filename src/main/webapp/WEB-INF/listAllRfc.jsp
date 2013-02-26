<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basepath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Rfc Doc</title>
</head>
<body>
<%@include file="/WEB-INF/include/menu.jsp" %>
<table border="1">
<tbody>
<tr>
<th>RFC</th><th>Title</th><th>author</th><th>original infomation</th>
</tr>
<c:forEach items="${list }" var="item">
<tr>
<td><a href="rfc/${item.id }">RFC${item.id }</a></td><td>${item.title }</td><td>${item.author }</td><td>${item.origInfo }</td>
</tr>
</c:forEach>
</tbody>
</table>
<c:choose><c:when test="${curpage > 1 }"><a href="rfc/page/${curpage - 1 }">上一页</a></c:when><c:otherwise>上一页</c:otherwise></c:choose>
<c:choose><c:when test="${curpage < maxpage }">&nbsp;&nbsp;<a href="rfc/page/${curpage + 1 }">下一页</a></c:when><c:otherwise>下一页</c:otherwise></c:choose>
当前为第${curpage }页,共${maxpage }页<br>
共有${total }个RFC文档<br/>
</body>
</html>