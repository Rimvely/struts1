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

//�׳� Action�� Servlet���� �� �Ѿ�� ����� if���� ����ؼ� ���� ���� �ڵ�

//�̰� �޼ҵ� ������ ���� ����ϱ����� ���� ����
public class BoardAction extends DispatchAction {
	
	//ActionForward(�޼ҵ�)�� �̸��� write�̱⶧���� �ּҿ� http://192.168.16.27:8080/strusts1/board.do?method=write
	//write�� ����
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

		//â ����� ��� ��ɾ��ε� �װ� ���
		//���ư��� ��ư� �ܾ��ϳ� ���ϱ�(���⼱ created)
		//���ư� ��ġ�� ���������� �ϴ°Ŵϱ� struts-config_temp.xml�� ���� �ٿ��ֱ��ؼ� struts-config_board.xml�� ����
		//web.xml���� ��� �߰��ϱ� servlet configuration���� /WEB-INF/struts-config_board.xml
		//�׸��� ���� �����
		//�׸��� struts-config_board.xml ���� �۾��ϱ�
		return mapping.findForward("created");
	}
	
	public ActionForward write_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		//ó�� ������ ����
		Connection conn = DBCPConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		//createâ���� form�� ���ؼ� 5���� �����͸� ������ ��
		//��ä�� �����ؼ� ������ �� ���� ���� �ʿ䰡 ���� ActionForm�� form�� �� �����´�
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
		
		//ó�� ������ ����
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
		
		urlList = cp + "/board.do?method=list" + params; //�˻��� ������ �����ϱ����� �ʿ��� ��
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

		//ó�� ������ ����
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		//num�� �������ϱ�
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchKey != null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		
		//��ȸ�� ����
		dao.updateHitCount(num);
		
		//������ �б�
		BoardForm dto = dao.getReadData(num);
		
		if(dto == null) {
			//���߰��ϱ�?
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

		//ó�� ������ ����
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchKey != null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
			
		}
		
		if(searchKey!=null){//�˻��ϸ�!
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}else{//�˻� ���ϸ�
				searchKey = "subject";//�⺻�� �־���
				searchValue = "";
		}
		
		BoardForm dto = dao.getReadData(num);
		
		if(dto == null) {
			// /board.do?method=list�� redirect �ϱ����� save�� �ִ°� ����
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
		
		//ó�� ������ ����
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
		
		//ó�� ������ ����
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
