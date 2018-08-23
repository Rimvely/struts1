// 이메일 검사
function isValidEmail(email) {
	var format = /^((\w|[\-\.])+)@((\w|[\-\.])+)\.([A-Za-z]+)$/;
    if (email.search(format) != -1)
        return true; //올바른 포맷 형식
    return false;
}

// 내용의 값의 빈공백을 trim(앞/뒤)
String.prototype.trim = function() {
		var TRIM_PATTERN = /(^\s*)|(\s*$)/g;
		return this.replace(TRIM_PATTERN, "");
};

// 한글 필터링
function isValidKorean(data){
     // UTF-8 코드 중 AC00부터 D7A3 값이지 검사
	var format = /^[\uac00-\ud7a3]*$/g;
    if (data.search(format) != -1)
        return true; //올바른 포맷 형식
    return false;
}

//***********************************************************************
// 날짜 검사(문자)
function isValidDate(syear, smonth, sday) {
	var temp, year, month, day;
	var days = new Array (31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);

	syear = syear.trim();
	smonth = smonth.trim();
	sday = sday.trim();
    if(syear.length != 4 ||smonth.length!=2||sday.length!=2 )
		return false;

    year = parseInt(syear);

	temp = smonth;
    if(temp.charAt(0) == '0')
        temp = temp.charAt(1);
    month = parseInt(temp);

    temp = sday;
    if(temp.charAt(0) == '0')
        temp = temp.charAt(1);
    day = parseInt(temp);

    // 날짜 검사
	if(year%4==0 && year%100 !=0 || year%400==0)
		days[1]=29;
	else
		days[1]=28;

	if(month < 1 || month > 12)
		return false;

	if(day > days[month-1] || day < 1)
		return false;
	return true;
}

//***********************************************************************
// 주민등록 번호 검사
function isValidResidentNO(ssn1, ssn2) {
		var days = new Array (31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);

		var check = new Array (2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5);
		var ssn = new Array(13);
		var temp, year, month, day, tot, chkNum, i;

		if((ssn1.length != 6) || (ssn2.length != 7))
			return false;

		for(i = 0; i< 13; i++) {
			if(i < 6)
				ssn[i] = parseInt(ssn1.charAt(i));
			else
				ssn[i] = parseInt(ssn2.charAt(i-6));
		}

		temp = ssn1.substr(0, 2);
		if(temp.charAt(0) == '0')
			temp = temp.charAt(1);
		year = parseInt(temp);

		if(ssn[6] == 1 || ssn[6] == 2)
			year = year + 1900;
		else
			year = year + 2000;

		temp = ssn1.substr(2, 2);
		if(temp.charAt(0) == '0')
			temp = temp.charAt(1);
		month = parseInt(temp);

		if(ssn[6] < 1 || ssn[6] > 4)
			return false;
   
		temp = ssn1.substr(4, 2);
		if(temp.charAt(0) == '0')
			temp = temp.charAt(1);
		day = parseInt(temp);

		// *********************************************************
		if(year%4==0 && year%100 !=0 || year%400==0)
			days[1]=29;
		else
			days[1]=28;

		if(month < 1 || month > 12)
			return false;

		if(day > days[month-1] || day < 1)
			return false;
		// *********************************************************

		tot=0;
		for(i=0; i<12; i++)
			tot = tot + ssn[i] * check[i];
		chkNum = 11 - tot % 11;
		chkNum = chkNum % 10;
  
		if(chkNum != ssn[12])
			return false;
		return true;
}

//***********************************************************************
// 숫자와 영문자만 입력 허용
function onlyAlpNum() {
     if((event.keyCode < 48) || 
         ((event.keyCode > 57) && (event.keyCode < 65)) || 
         ((event.keyCode > 90) && (event.keyCode < 97)) || 
         (event.keyCode > 122))
        event.returnValue = false;
}

//***********************************************************************
// 숫자만 입력 허용
function onlyNum() {
     if((event.keyCode < 48) || (event.keyCode > 57))
        event.returnValue = false;
}

//***********************************************************************
// 한글만 입력 허용
function onlyHangul() {
		if((event.keyCode < 12592) || (event.keyCode > 12687))
			event.returnValue = false;
}

//***********************************************************************
// 영문, 숫자 인지 확인
 function isValidEngNum(str) {
		for(var i=0;i<str.length;i++) {
			achar = str.charCodeAt(i);                 
			if( achar > 255 )
				return false;
		}
		return true; 
}

//***********************************************************************
//팝업 윈도우즈
function winOpen(theURL, winName) {
	var flag;
	flag = "left=10, ";
	flag += "top=10, ";
	flag += "width=372, ";
	flag += "height=466, ";
	flag += "toolbar=no, ";
	flag += "menubar=no, ";
	flag += "status=no, ";
	flag += "scrollbars=no, ";
	flag += "resizable=no";
    //features = "scrollbars=no,resizable=no,width=220,height=230";

     window.open(theURL, winName, flag);

     return;
}

//***********************************************************************
// 전화번호 형식(숫자-숫자-숫자)인지 체크
function isValidPhone(tel) {
	var format = /^(\d+)-(\d+)-(\d+)$/;

    if (tel.search(format) != -1)
        return true; //올바른 포맷 형식
    return false;
}

//***********************************************************************
// 문자열에 특수문자(",  ',  <,  > ) 검사
function isValidSpecialChar(str){
    if(str.value.search(/[\",\',<,>]/g) >= 0)
		return true; // 존재 하면
	return false;
}
