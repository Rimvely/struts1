/* **********************************************************
    전역 변수
*********************************************************** */
var xmlHttp;           // Ajax 객체

/* **********************************************************
    createXMLHttpRequest() : Ajax 객체를 생성후 반환하는 함수
********************************************************** */
function createXMLHttpRequest() {
    var xmlReq = false;

    if (window.XMLHttpRequest) {    // Non-Microsoft browsers
        xmlReq = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
		try {
			// XMLHttpRequest in later versions of Internet Explorer
			xmlReq = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e1) {
			try {
				// Try version supported by older versions of Internet Explorer
				xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e2) {
				// Unable to create an XMLHttpRequest with ActiveX
			}
		}
	}

	return xmlReq;
}
