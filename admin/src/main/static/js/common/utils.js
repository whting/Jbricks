(function (global) {
    if (global.util) {
        return;
    }

    var util = global.util = {
        version: "1.0.0"
    }

    util.cookieUtil = {
        getCookie: function (c_name) {

            if(document.cookie.length>0){
                var c_start = document.cookie.indexOf(c_name+"=");
                if(c_start!=-1){
                    c_start=c_start+c_name.length+1;
                    var c_end=document.cookie.indexOf(";",c_start);
                    if(c_end == -1){
                        c_end = document.cookie.length;
                    }
                    return unescape(document.cookie.substring(c_start,c_end));
                }
            }
        },
        setCookie : function (c_name,c_value,expiredays){
            if(!expiredays){
                expiredays=365;
            }
            var exdate=new Date();
            exdate.setDate(exdate.getDate()+expiredays);
            document.cookie = c_name+"="+c_value+";expires="+exdate.toGMTString()+";path=/";
        }
    }


})(this)