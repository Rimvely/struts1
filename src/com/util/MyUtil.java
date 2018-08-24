package com.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MyUtil {
	// 문자열의 내용중 원하는 문자열을 다른 문자열로 변환
	// String str = replaceAll(str, "\r\n", "<br>"); // 엔터를 <br>로 변환
	public String replaceAll(String str, String oldStr, String newStr) {
		if(str == null || str.equals(""))
			return "";
		
		Pattern p = Pattern.compile(oldStr);
		Matcher m = p.matcher(str);
		
		StringBuffer sb = new StringBuffer();
		
		while(m.find()) {
			m.appendReplacement(sb, newStr);
		}
		m.appendTail(sb);
		
		return sb.toString();
	}

 // *******************************************************************************
    // 페이지 수 구하기
	public int getPageCount(int numPerPage, int dataCount) {
		int pageCount = 0;
		int remain;

		 // 총 페이지 수를 구하기 위한 나머지 계산
		remain = dataCount % numPerPage;
		if(remain == 0)
			pageCount = dataCount / numPerPage;
		else
			pageCount = dataCount / numPerPage + 1;

		return pageCount;
	}
    
// Get 방식에 의한 페이지 처리 메서드 *************************************
    public String pageIndexList(int current_page, int total_page, String list_url) {
        int numPerBlock = 10;   // 리스트에 나타낼 페이지 수
        int currentPageSetUp;
        int n;
        int page;
        StringBuffer strList = new StringBuffer();
        
        if(current_page == 0)
        	return "";
        
        if(list_url.indexOf("?") != -1)
        	list_url = list_url + "&";
        else
        	list_url = list_url +"?";

        // 표시할 첫 페이지
        currentPageSetUp = (current_page / numPerBlock) * numPerBlock;
        if (current_page % numPerBlock == 0)
            currentPageSetUp = currentPageSetUp - numPerBlock;

        // 1 페이지
        if ((total_page > numPerBlock) && (currentPageSetUp > 0)) {
           strList.append("<a href='"+list_url+"pageNum=1'>1</a> ");
        }

        // [Prev] : 총 페이지수가 numPerBlock 이상인 경우 이전 numPerBlock 보여줌
        n = current_page - numPerBlock;
        if (total_page > numPerBlock && currentPageSetUp > 0) {
           strList.append("[<a href='"+list_url+"pageNum="+n+"'>Prev</a>] ");
        }

        // 바로가기 페이지 구현
        page = currentPageSetUp + 1;
        while((page <= total_page) && (page <= currentPageSetUp + numPerBlock)) {
           if(page == current_page) {
             strList.append("<font color='Fuchsia'>"+page+" </font>");
           }
           else {
             strList.append("<a href='"+list_url+"pageNum="+page+"'>"+page+"</a> ");
           }
           page++;
        }
        
        // [Next] : 총 페이지수가 numPerBlock 페이지 이상인 경우 다음 numPerBlock 페이지를 보여줌
        // n = currentPageSetUp + numPerBlock + 1;
        n = current_page + numPerBlock;
        if (total_page - currentPageSetUp > numPerBlock) {
			strList.append("[<a href='"+list_url+"pageNum="+n+"'>Next</a>] ");
        }

        // 마지막 페이지
        if ((total_page > numPerBlock) && (currentPageSetUp + numPerBlock < total_page)) {
			strList.append("<a href='"+list_url+"pageNum="+total_page+"'>"+total_page+"</a>");
        }

        return strList.toString();
    }
    
    // 자바 스크립트(listPage 함수)에 의한 페이지 처리 메서드(prototype 등) ***********************
    public String pageIndexList(int current_page, int total_page) {
        int numPerBlock = 10;   // 리스트에 나타낼 페이지 수
        int currentPageSetUp;
        int n;
        int page;
        String strList="";
        
        if(current_page == 0)
        	return "";

        // 표시할 첫 페이지
        currentPageSetUp = (current_page / numPerBlock) * numPerBlock;
        if (current_page % numPerBlock == 0)
            currentPageSetUp = currentPageSetUp - numPerBlock;

        // 1 페이지
        if ((total_page > numPerBlock) && (currentPageSetUp > 0)) {
            strList = "<a onclick='listPage(1);'>1</a> ";
        }

        // [Prev] : 총 페이지수가 numPerBlock 이상인 경우 이전 numPerBlock 보여줌
        n = current_page - numPerBlock;
        if (total_page > numPerBlock && currentPageSetUp > 0) {
            strList = strList + "[<a onclick='listPage("+n+");'>Prev</a>] ";
        }

        // 바로가기 페이지 구현
        page = currentPageSetUp + 1;
        while((page <= total_page) && (page <= currentPageSetUp + numPerBlock)) {
           if(page == current_page) {
               strList = strList + "<font color='Fuchsia'>"+page+" </font>";
           }
           else {
               strList = strList +"<a onclick='listPage("+page+");'>"+page+"</a> ";
           }
           page++;
        }

        // [Next] : 총 페이지수가 numPerBlock 페이지 이상인 경우 다음 numPerBlock 페이지를 보여줌
        // n = currentPageSetUp + numPerBlock + 1;
        n = current_page + numPerBlock;
        if (total_page - currentPageSetUp > numPerBlock) {
            strList = strList + "[<a onclick='listPage("+n+");'>Next</a>] ";
        }

        // 마지막 페이지
        if ((total_page > numPerBlock) && (currentPageSetUp + numPerBlock < total_page)) {
            strList = strList + "<a onclick='listPage("+total_page+");'>"+total_page+"</a>";
        }

        return strList;
    }
// *******************************************************************************
    
 // Ajax(Dojo) 페이지 처리 메서드 *************************************
    public String ajaxPageIndexList(int current_page, int total_page, String list_url, String targets) {
        int numPerBlock = 10;   // 리스트에 나타낼 페이지 수
        int currentPageSetUp;
        int n;
        int page;
        StringBuffer strList = new StringBuffer();
        
        if(current_page == 0)
        	return "";
        
        if(list_url.indexOf("?") != -1)
        	list_url = list_url + "&";
        else
        	list_url = list_url +"?";

        // 표시할 첫 페이지
        currentPageSetUp = (current_page / numPerBlock) * numPerBlock;
        if (current_page % numPerBlock == 0)
            currentPageSetUp = currentPageSetUp - numPerBlock;

        // 1 페이지
        if ((total_page > numPerBlock) && (currentPageSetUp > 0)) {
           strList.append("<a dojoType='struts:BindAnchor' href='"+list_url+"pageNo=1' targets='"+targets+"' showError='true'>1</a> ");
        }

        // [Prev] : 총 페이지수가 numPerBlock 이상인 경우 이전 numPerBlock 보여줌
        n = current_page - numPerBlock;
        if (total_page > numPerBlock && currentPageSetUp > 0) {
           strList.append("[<a dojoType='struts:BindAnchor' href='"+list_url+"pageNo="+n+"' targets='"+targets+"' showError='true'>Prev</a>] ");
        }

        // 바로가기 페이지 구현
        page = currentPageSetUp + 1;
        while((page <= total_page) && (page <= currentPageSetUp + numPerBlock)) {
           if(page == current_page) {
             strList.append("<font color='Fuchsia'>"+page+" </font>");
           }
           else {
             strList.append("<a dojoType='struts:BindAnchor' href='"+list_url+"pageNo="+page+"' targets='"+targets+"' showError='true'>"+page+"</a> ");
           }
           page++;
        }

        // [Next] : 총 페이지수가 numPerBlock 페이지 이상인 경우 다음 numPerBlock 페이지를 보여줌
        n = current_page + numPerBlock;
        if (total_page - currentPageSetUp > numPerBlock) {
			strList.append("[<a dojoType='struts:BindAnchor' href='"+list_url+"pageNo="+n+"' targets='"+targets+"' showError='true'>Next</a>] ");
        }

        // 마지막 페이지
        if ((total_page > numPerBlock) && (currentPageSetUp + numPerBlock < total_page)) {
			strList.append("<a dojoType='struts:BindAnchor' href='"+list_url+"pageNo="+total_page+"' targets='"+targets+"' showError='true'>"+total_page+"</a>");
        }

        return strList.toString();
    }
// *******************************************************************************
    
// *******************************************************************************
	// E-Mail 검사
    public boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches(
            "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", 
            email.trim());
        return b;
    }

	// NULL 인 경우 ""로 
    public String checkNull(String str) {
        String strTmp;
        if (str == null)
            strTmp = "";
        else
            strTmp = str;
        return strTmp;
    }
    
    // 8859_1 를 euc-kr로
    public String isoToEuc(String str) {
        String convStr = null;
        try {
            if(str == null)
                return "";

            convStr = new String(str.getBytes("8859_1"),"euc-kr");
        } catch (UnsupportedEncodingException e) {
        }
        return convStr;
    }

    // 8859_1 를 utf-8로
    public String isoToUtf(String str) {
        String convStr = null;
        try {
            if(str == null)
                return "";

            convStr = new String(str.getBytes("8859_1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return convStr;
    }

    // euc-kr 를 ISO-8859-1 로
    public String eucToIso(String str) {
        String convStr = null;
        try {
            if(str == null)
                return "";

            convStr = new String(str.getBytes("euc-kr"),"8859_1");
        } catch (UnsupportedEncodingException e) {
        }
        return convStr;
    }
    
    // KSC56O1 를 8859_1로
    public String fromKorean(String str) {
        String convStr = null;
        try {
            if(str == null)
                return "";

            convStr = new String(str.getBytes("KSC5601"),"8859_1");
        } catch (UnsupportedEncodingException e) {
        }
        return convStr;
    }
    
    // euc-kr 를 KSC5601 로
    public String eucToKsc(String str) {
        String convStr = null;
        try {
            if(str == null)
                return "";

            convStr = new String(str.getBytes("euc-kr"),"8859_1");
            convStr = new String(convStr.getBytes("8859_1"),"KSC5601");
        } catch (UnsupportedEncodingException e) {
        }
        return convStr;
    }
}
