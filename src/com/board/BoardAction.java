package com.board;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.util.DBCPConn;
import com.util.MyUtil;

//그냥 Action은 Servlet으로 갓 넘어온 사람이 if절을 사용해서 쓰기 위한 코딩

//이건 메소드 단위로 만들어서 사용하기위한 다음 버전
public class BoardAction extends DispatchAction {
	
	//ActionForward(메소드)의 이름이 write이기때문에 주소에 http://192.168.16.27:8080/strusts1/board.do?method=write
	//write가 적힘
	public ActionForward write(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		String params = "";
		
		if(searchKey != null){
			params = "&searchKey=" + searchKey;
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		request.setAttribute("params", params);
		request.setAttribute("pageNum", pageNum);

		//창 띄워라 라는 명령어인데 그게 없어서
		//돌아갈때 담아갈 단어하나 정하기(여기선 created)
		//돌아갈 위치는 개인적으로 하는거니까 struts-config_temp.xml를 복사 붙여넣기해서 struts-config_board.xml로 변경
		//web.xml에서 경로 추가하기 servlet configuration에서 /WEB-INF/struts-config_board.xml
		//그리고 서버 재시작
		//그리고 struts-config_board.xml 가서 작업하기
		return mapping.findForward("created");
	}
	
	public ActionForward write_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		//처음 할일은 연결
		Connection conn = DBCPConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		//create창에서 form을 통해서 5개의 데이터를 가지고 옴
		//객채를 생성해서 일일이 다 따로 받을 필요가 없이 ActionForm의 form이 다 가져온다
		BoardForm dto = (BoardForm)form;
		
		dto.setNum(dao.getMaxNum()+1);
		dto.setIpAddr(request.getRemoteAddr());
		
		dao.insertData(dto);

		/*return mapping.findForward("save");*/
		
		String params = "pageNum="+pageNum;
		
		if(searchKey != null){
			params = "&searchKey=" + searchKey;
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		ActionForward af = new ActionForward();

		af.setRedirect(true);
		af.setPath("/board.do?method=list&pageNum=" + pageNum + params);

		return af;
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//처음 할일은 연결
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		
		
		String cp = request.getContextPath();
		
		MyUtil myUtil = new MyUtil();
		
		int numPerPage = 5;
		int totalPage = 0;
		int totalDataCount = 0;
		
		String pageNum = request.getParameter("pageNum");
		
		int currentPage = 1;
		if(pageNum != null)
			currentPage = Integer.parseInt(pageNum);
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchKey==null){
			searchKey = "subject";
			searchValue = "";
	
		}
		
		if(request.getMethod().equalsIgnoreCase("GET")){
			searchValue = URLDecoder.decode(searchValue, "UTF-8");		
		}
		
		totalDataCount = dao.getDataCount(searchKey, searchValue);
		
		if(totalDataCount != 0){
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
		}
		
		if(currentPage > totalPage){
			currentPage = totalPage;
		}
		
		int start = (currentPage -1)*numPerPage+1;
		int end = currentPage*numPerPage;
		
		List<BoardForm> lists = dao.getLists(start, end, searchKey, searchValue);
		
		String params = "";
		String urlArticle = "";
		String urlList = "";
		
		
		if(!searchValue.equals("")){
			
			searchValue = URLEncoder.encode(searchValue, "UTF-8");
			
			params = "&searchKey=" + searchKey;
			params += "&searchValue=" + searchValue;
			
		}
		
		urlList = cp + "/board.do?method=list" + params; //검색후 페이지 정렬하기위해 필요한 것
		urlArticle = cp + "/board.do?method=article&pageNum=" + currentPage;
		urlArticle += params;
		
		request.setAttribute("lists", lists);
		request.setAttribute("urlArticle", urlArticle);
		request.setAttribute("params", params);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("pageIndexList", myUtil.pageIndexList(currentPage, totalPage, urlList));
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalDataCount", totalDataCount);
		
		return mapping.findForward("list");
	}
	
	public ActionForward article(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		//처음 할일은 연결
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		//num을 보냈으니까
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchKey != null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		
		//조회수 증가
		dao.updateHitCount(num);
		
		//데이터 읽기
		BoardForm dto = dao.getReadData(num);
		
		if(dto == null) {
			//경고뜨게하기?
			return mapping.findForward("save");
		}
		
		int lineSu = dto.getContent().split("\n").length;
		
		dto.setContent(dto.getContent().replaceAll("\n", "<br/>"));
		
		String param = "pageNum=" + pageNum;
		if(searchKey != null) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		request.setAttribute("dto", dto);
		request.setAttribute("num", num);
		request.setAttribute("params", param);
		request.setAttribute("lineSu", lineSu);
		request.setAttribute("pageNum", pageNum);
		
		return mapping.findForward("article");
		
	}
	
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		//처음 할일은 연결
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchKey != null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
			
		}
		
		if(searchKey!=null){//검색하면!
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}else{//검색 안하면
				searchKey = "subject";//기본값 넣어줌
				searchValue = "";
		}
		
		BoardForm dto = dao.getReadData(num);
		
		if(dto == null) {
			// /board.do?method=list로 redirect 하기위해 save에 있는걸 적용
			return mapping.findForward("save");
		}
		
		String params = "pageNum="+pageNum;
		
		if(searchKey != null){
			params = "&searchKey=" + searchKey;
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		request.setAttribute("dto", dto);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("params", params);
		request.setAttribute("searchKey", searchKey);
		request.setAttribute("searchValue", searchValue);

		return mapping.findForward("update");
		

		
	}
	
	public ActionForward update_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//처음 할일은 연결
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		BoardForm dto = (BoardForm)form;
		
		dao.updateData(dto);
		
		/*return mapping.findForward("save");*/
		
		String params = "pageNum="+pageNum;
		
		if(searchKey != null){
			params = "&searchKey=" + searchKey;
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		ActionForward af = new ActionForward();

		af.setRedirect(true);
		af.setPath("/board.do?method=list&pageNum=" + pageNum + params);

		return af;
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		//처음 할일은 연결
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		BoardForm dto = (BoardForm)form;
		System.out.println(dto.getNum());
		dao.deleteData(dto.getNum());
		
		String params = "pageNum="+pageNum;
		
		if(searchKey != null){
			params = "&searchKey=" + searchKey;
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		/*	return mapping.findForward("save");*/
		
		ActionForward af = new ActionForward();

		af.setRedirect(true);
		af.setPath("/board.do?method=list&pageNum=" + pageNum + params);


		return af;
	}
	
	
	

}
