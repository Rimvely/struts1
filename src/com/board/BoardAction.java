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
		
		//처음 할일은 연결
		Connection conn = DBCPConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		//create창에서 form을 통해서 5개의 데이터를 가지고 옴
		BoardForm f = (BoardForm)form;
		
		f.setNum(dao.getMaxNum()+1);
		f.setIpAddr(request.getRemoteAddr());
		
		dao.insertData(f);

		return mapping.findForward("save");
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
		
		String param = "";
		String urlArticle = "";
		String urlList = "";
		
		if(!searchValue.equals("")){
			
			searchValue = URLEncoder.encode(searchValue, "UTF-8");
			
			param = "&searchKey=" + searchKey;
			param += "&searchValue=" + searchValue;
			
		}
		
		urlList = cp + "/board.do?method=list" + param;
		urlArticle = cp + "board.do?method=article&pageNum=" + currentPage;
		urlArticle += param;
		
		request.setAttribute("lists", lists);
		request.setAttribute("urlArticle", urlArticle);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("pageIndexList", myUtil.pageIndexList(currentPage, totalPage, urlList));
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalDataCount", totalDataCount);
		
		return mapping.findForward("list");
	}

}
