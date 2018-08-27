package com.fileTest;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.util.FileManager;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;

public class FileTestAction extends DispatchAction {
	
	public ActionForward write(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return mapping.findForward("write");
	}
	
	public ActionForward write_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		
		return mapping.findForward("write_ok");
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		
		return mapping.findForward("list");
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		
		return mapping.findForward("delete_ok");
	}
	
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		
		return mapping.findForward(null);//�ٿ�ε�� ���� �� null �̴�
	}


}
