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
		onclick="javascript:location.href='<%=cp%>/fileTest.do?method=write&pageNum=${pageNum }';"/>
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
	
	<c:if test="${dto.totalDataCount eq 0 }">
	<tr bgcolor="#ffffff">
		<td align="center" colspan="4">
		등록된 자료가 없습니다.
		</td>
		</tr>
	</c:if>

	<c:forEach var="dto" items="${lists }">
	<tr onmouseover="this.style.backgroundColor='#e4e4e4'"
	onmouseout="this.style.backgroundColor=''" bgcolor="#ffffff">
		
		<td width="50" align="center">${dto.listNum }</td>
		<td width="200" align="center">${dto.subject }</td>
		<td width="200" align="center">
		<a href="${downloadUrl}&num=${dto.num}&pageNum=${pageNum}">
		${dto.originalFileName }
		</a>
		</td>
		<td width="50" align="center"><a href="<%=cp%>/fileTest.do?method=delete&num=${dto.num }&pageNum=${pageNum}">삭제</a></td>
	</tr>
	</c:forEach>
	

	

</table>	

<c:if test="${dto.totalDataCount ne 0 }">
	<table width="600" border="0" cellpadding="0" cellspacing="3" align="center">
		<tr align="center">
			<td align="center" height="30">${pageIndexList }</td>
		</tr>
	</table>	
</c:if>











</body>
</html>
