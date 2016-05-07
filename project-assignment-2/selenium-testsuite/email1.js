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

//Get new email address from the guerrillamail api via sonp.afeld.me proxy to remove limitations due to lacking Cross-Site Request Forgery token
ajaxGet(
    'https://jsonp.afeld.me/?url=http%3A%2F%2Fapi.guerrillamail.com%2Fajax.php%3Ff%3Dget_email_address%26ip%3D127.0.0.1%26agent%3DMozilla_foo_bar',
    function (response) {
        response = JSON.parse(response);
        if (!response)
            return;

        //Save token in cookie
        document.cookie = "phpsid="+response.sid_token;
        //Fill email feild with the email from guerrillamail
        document.getElementsByName('customer_info[Customer][Email]')[0].value = response.email_addr;
});
