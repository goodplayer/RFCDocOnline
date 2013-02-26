<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basepath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RFC${id }</title>
</head>
<body>
<%@include file="/WEB-INF/include/menu.jsp" %>
查看RFC文档&nbsp;>>&nbsp;<a href="rfc/${id }">RFC${id }</a>&nbsp;>>&nbsp;Page&nbsp;${curpage }<br/>
<br/>
<hr>
<pre>
${content }
</pre>
<hr>
<br/>
<c:choose><c:when test="${curpage > 1 }"><a href="rfc/${id }/${curpage - 1 }">上一页</a></c:when><c:otherwise>上一页</c:otherwise></c:choose>
<c:choose><c:when test="${curpage < maxpage }">&nbsp;&nbsp;<a href="rfc/${id }/${curpage + 1 }">下一页</a></c:when><c:otherwise>下一页</c:otherwise></c:choose>
当前为第${curpage }页,共${maxpage }页<br>
</body>
</html>