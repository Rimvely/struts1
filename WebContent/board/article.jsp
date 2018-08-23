<%@ page contentType="text/html; charset=UTF-8"%>

<%
	request.setCharacterEncoding("utf-8");
	String cp = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>게 시 판</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<link rel="stylesheet" href="<%=cp%>/board/css/style.css" type="text/css"/>
<link rel="stylesheet" href="<%=cp%>/board/css/article.css" type="text/css"/>

</head>

<body>

<div id="bbs">
	<div id="bbs_title">
	게 시 판
	</div>
	<div id="bbsArticle">
		<div id="bbsArticle_header">
			게시물 제목
		</div>
		<div class="bbsArticle_bottomLine">
			<dl>
				<dt>작성자</dt>
				<dd>홍길동</dd>
				<dt>줄수</dt>
				<dd>10</dd>
			</dl>
		</div>
		<div class="bbsArticle_bottomLine">
			<dl>
				<dt>등록일</dt>
				<dd>2000-10-10</dd>
				<dt>조회수</dt>
				<dd>10</dd>
			</dl>
		</div>
		<div id="bbsArticle_content">
			<table width="600" border="0">
			<tr><td style="padding: 20px 80px 20px 62px;" valign="top" height="200">
			게시물 내용
			</td></tr>
			</table>
		</div>
		<div class="bbsArticle_bottomLine">
			이전글 : 작업중
		</div>
		<div class="bbsArticle_noLine">
			다음글 : 작업중
		</div>
	</div>
	<div class="bbsArticle_noLine" style="text-align:right">
		    From : 127.0.0.1
	</div>
	<div id="bbsArticle_footer">
		<div id="leftFooter">
               <input type="button" value=" 수정 " class="btn2" onclick=""/>
               <input type="button" value=" 삭제 " class="btn2" onclick=""/>
		</div>
		<div id="rightFooter">
               <input type="button" value=" 리스트 " class="btn2" onclick=""/>
		</div>
	</div>

</div>

<br/>&nbsp;<br/>
</body>

</html>