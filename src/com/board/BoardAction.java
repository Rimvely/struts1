package com.board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

//�׳� Action�� Servlet���� �� �Ѿ�� ����� if���� ����ؼ� ���� ���� �ڵ�

//�̰� �޼ҵ� ������ ���� ����ϱ����� ���� ����
public class BoardAction extends DispatchAction {
	
	//ActionForward(�޼ҵ�)�� �̸��� write�̱⶧���� �ּҿ� http://192.168.16.27:8080/strusts1/board.do?method=write
	//write�� ����
	public ActionForward write(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		
		

		return mapping.findForward("save");
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		return mapping.findForward("list");
	}

}
