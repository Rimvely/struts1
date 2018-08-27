package com.fileTest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class FileTestForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private int num;
	private String subject;
	private String saveFileName;
	private String originalFileName;
	
//	DTO에는 DB에 없는 변수도 선언 가능	-----------
	
	//write.jsp의 name과 일치해야한다. 
	//여러개 파일을 한번에 올리고싶으면 []배열로 만들면된다
	private FormFile upload; 
	
	private int listNum;//일렬번호 재지정
	private String urlFile;//파일 다운로드 경로
	
//	여기까지가 DB에 없는 변수들---------------------
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSaveFileName() {
		return saveFileName;
	}
	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public FormFile getUpload() {
		return upload;
	}
	public void setUpload(FormFile upload) {
		this.upload = upload;
	}
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public String getUrlFile() {
		return urlFile;
	}
	public void setUrlFile(String urlFile) {
		this.urlFile = urlFile;
	}
	
	
	
	
	
	
}
