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
		
		//DB����
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		HttpSession session = request.getSession();
		
		String root = session.getServletContext().getRealPath("/");
		
		String savePath = root + File.separator + "pds" + File.separator + "saveFile";
		
		FileTestForm f = (FileTestForm)form;
		
		//���� ���ε�
		String newFileName = 
				FileManager.doFileUpload(f.getUpload(), savePath);
		
		//DB�� ����
		if(newFileName!=null){
			
			int maxNum = dao.getIntValue("fileTest.maxNum");
			
			f.setNum(maxNum+1);
			f.setSaveFileName(newFileName);
			f.setOriginalFileName(f.getUpload().getFileName());
			//f.getUpload().getFileSize(); ����ũ��
			
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
		
		//������ ��������
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
			
			//�ϷĹ�ȣ ������
			FileTestForm dto = (FileTestForm)it.next();
			
			listNum = totalDataCount - (start + n - 1);
			dto.setListNum(listNum);
			n++;
			
			//���� �ٿ�ε� ���
			str = cp + "/fileTest.do?method=download&num=" + dto.getNum();
			
			dto.setUrlFile(str);
			
		}

		String urlList = cp + "/fileTest.do?method=list";
		
		//�ٿ�ε� Path ����--------------------------------------------
		String downloadUrl = cp + "/fileTest.do?method=download";

		request.setAttribute("downloadUrl", downloadUrl);

		//�ٿ�ε� Path ��---------------------------------------------	
	
		//�̹���path
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
		
		//������ ���� ����(original ���ϳ�������)
		FileManager.doFileDelete(dto.getSaveFileName(), deletePath);
		
		//DB�� ���̺��� ����
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
		
		//�ٿ�ε�� boolean���� �ϱ⶧���� true/false��ȯ�����ִ�
		
		//�ٿ�ε� Ŭ���Ҷ��� num�� �Ѿ���ϱ� num�� �޾ƾ��Ѵ�
		int num = Integer.parseInt(request.getParameter("num"));
		
		FileTestForm dto = (FileTestForm)dao.getReadData("fileTest.readData", num);

		if (dto == null) {
			return mapping.findForward("list");
		}
		
		HttpSession session = request.getSession();
		
		String root = session.getServletContext().getRealPath("/");
		
		String downloadPath = root + File.separator + "pds" + File.separator + "saveFile";
		
		//��ȯ��(true : �ٿ�ε� �Ϸ�)
		boolean flag = 
				FileManager.doFileDownload(dto.getSaveFileName(), dto.getOriginalFileName(), downloadPath, response);
		
		//error message ������ ��� (1.error.jsp ����� ��� 2. ���� ��ũ��Ʈ �ۼ�)
		//2�����
		if(flag==false){
			
			response.setContentType("text/html;charset=UTF-8");
			
			PrintWriter out = response.getWriter();
			
			out.print("<script type='text/javascript'>");
			out.print("alert('download error');");
			out.print("history.back();");
			out.print("</script>");
		}
		
		return mapping.findForward(null);//�ٿ�ε�� ���� �� null �̴�
	}


}
