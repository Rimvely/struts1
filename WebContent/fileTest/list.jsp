<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	//한글을 3바이트로 받아줘서 안깨지게해주는것
	request.setCharacterEncoding("UTF-8");
//  cp = ~/study 까지 경로
	String cp = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body style="font-size: 12pt;">

<table width="500" align="center">
	<tr height="30">
		<td align="right">
		<input type="button" value="파일 올리기" 
		onclick="javascript:location.href='<%=cp%>/fileTest.do?method=write';"/>
		</td>	
	</tr>
</table>

<table width="500" align="center" border="1" style="font-size: 12pt;">
	<tr height="30">
		<td width="50">번호</td>
		<td width="200">제목</td>
		<td width="200">파일</td>
		<td width="50">삭제</td>
	</tr>
</table>







<br/><br/><br/><br/><br/><br/>
<br/><br/><br/><br/><br/><br/>
<br/><br/><br/><br/><br/><br/>
<br/><br/><br/><br/><br/><br/>
</body>
</html>
