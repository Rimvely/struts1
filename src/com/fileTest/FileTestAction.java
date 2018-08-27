package com.fileTest;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.util.FileManager;
import com.util.MyUtil;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;


public class FileTestAction extends DispatchAction {
	
	public ActionForward write(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String pageNum = request.getParameter("pageNum");
		
		request.setAttribute("pageNum", pageNum);
		return mapping.findForward("write");
	}
	
	public ActionForward write_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String pageNum = request.getParameter("pageNum");
		
		//DB연결
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		HttpSession session = request.getSession();
		
		String root = session.getServletContext().getRealPath("/");
		
		String savePath = root + File.separator + "pds" + File.separator + "saveFile";
		
		FileTestForm f = (FileTestForm)form;
		
		//파일 업로드
		String newFileName = 
				FileManager.doFileUpload(f.getUpload(), savePath);
		
		//DB에 저장
		if(newFileName!=null){
			
			int maxNum = dao.getIntValue("fileTest.maxNum");
			
			f.setNum(maxNum+1);
			f.setSaveFileName(newFileName);
			f.setOriginalFileName(f.getUpload().getFileName());
			//f.getUpload().getFileSize(); 파일크기
			
			dao.insertData("fileTest.insertData", f);
		}
		
		session.setAttribute("pageNum", pageNum);
		
		//return mapping.findForward("write_ok");
		
		ActionForward af = new ActionForward();

		af.setRedirect(true);
		af.setPath("/fileTest.do?method=list&pageNum=" + pageNum);

		return af;
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		MyUtil myUtil = new MyUtil();
		
		String cp = request.getContextPath();
		
		int numPerPage = 5;
		int totalPage = 0;
		int totalDataCount = 0;
		
		String pageNum = request.getParameter("pageNum");
		
		int currentPage = 1;
		
		if(pageNum!=null && !pageNum.equals("")){
			currentPage = Integer.parseInt(pageNum);
		}
		
		totalDataCount = dao.getIntValue("fileTest.dataCount");
		
		if(totalDataCount != 0){
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
		}
		
		if(currentPage>totalPage){
			currentPage=totalPage;
		}
		
		//데이터 가져오기
		Map<String, Object> hMap = new HashMap<String, Object>();
		
		int start = (currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;
		
		hMap.put("start", start);
		hMap.put("end", end);
		
		List<Object> lists = dao.getListData("fileTest.listData", hMap);
		
		Iterator<Object> it = lists.iterator();
		int listNum, n=0;
		String str;
		while(it.hasNext()){
			
			//일렬번호 재지정
			FileTestForm dto = (FileTestForm)it.next();
			
			listNum = totalDataCount - (start + n - 1);
			dto.setListNum(listNum);
			n++;
			
			//파일 다운로드 경로
			str = cp + "/fileTest.do?method=download&num=" + dto.getNum();
			
			dto.setUrlFile(str);
			
		}

		String urlList = cp + "/fileTest.do?method=list";
		
		//다운로드 Path 시작--------------------------------------------
		String downloadUrl = cp + "/fileTest.do?method=download";

		request.setAttribute("downloadUrl", downloadUrl);

		//다운로드 Path 끝---------------------------------------------	
	
		//이미지path
		String imagePath = cp + "/pds/saveFile";
		request.setAttribute("imagePath", imagePath);
		
		
		
		
		request.setAttribute("lists", lists);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalDataCount", totalDataCount);
		request.setAttribute("pageIndexList", myUtil.pageIndexList(currentPage, totalPage, urlList));
		
		return mapping.findForward("list");
		
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
		FileTestForm dto = (FileTestForm)dao.getReadData("fileTest.readData", num);
		
		if (dto == null) {
			return mapping.findForward("list");
		}
		
		HttpSession session = request.getSession();
		
		String root = session.getServletContext().getRealPath("/");
		
		String deletePath = root + File.separator + "pds" + File.separator + "saveFile";
		
		//물리적 파일 삭제(original 파일네임으로)
		FileManager.doFileDelete(dto.getSaveFileName(), deletePath);
		
		//DB의 테이블에서 삭제
		dao.deleteData("fileTest.deleteData", num);
		
		
		
		session.setAttribute("pageNum", pageNum);

		//return mapping.findForward("delete_ok");
		
		ActionForward af = new ActionForward();

		af.setRedirect(true);
		af.setPath("/fileTest.do?method=list&pageNum=" + pageNum);

		return af;
		
	}
	
	
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
			
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		//다운로드는 boolean으로 하기때문에 true/false반환값이있다
		
		//다운로드 클릭할때도 num이 넘어오니까 num을 받아야한다
		int num = Integer.parseInt(request.getParameter("num"));
		
		FileTestForm dto = (FileTestForm)dao.getReadData("fileTest.readData", num);

		if (dto == null) {
			return mapping.findForward("list");
		}
		
		HttpSession session = request.getSession();
		
		String root = session.getServletContext().getRealPath("/");
		
		String downloadPath = root + File.separator + "pds" + File.separator + "saveFile";
		
		//반환값(true : 다운로드 완료)
		boolean flag = 
				FileManager.doFileDownload(dto.getSaveFileName(), dto.getOriginalFileName(), downloadPath, response);
		
		//error message 보내는 방법 (1.error.jsp 만드는 방법 2. 직접 스크립트 작성)
		//2번방식
		if(flag==false){
			
			response.setContentType("text/html;charset=UTF-8");
			
			PrintWriter out = response.getWriter();
			
			out.print("<script type='text/javascript'>");
			out.print("alert('download error');");
			out.print("history.back();");
			out.print("</script>");
		}
		
		return mapping.findForward(null);//다운로드는 값이 다 null 이다
	}


}
