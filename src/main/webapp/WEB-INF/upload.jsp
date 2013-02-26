<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload Rfc</title>
</head>
<body>
<%@include file="/WEB-INF/include/menu.jsp" %>
<c:if test="${result != null }">
<div style="color: #FF0000;font-weight: bolder;">${result }</div>
</c:if>
<form action="rfcUpload" method="post" enctype="multipart/form-data">
RFC<input name="id" /><br />
标题<input name="title"><br />
文件<input type="file" name="rfcfile"><br />
<input type="submit" value="提交" /><br />
</form>
</body>
</html>