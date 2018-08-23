package com.board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

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
		
		

		return mapping.findForward("save");
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		return mapping.findForward("list");
	}

}
