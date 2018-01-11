/**
 * Created by Administrator on 2017/8/10.
 */

//操作cookie相关
function getCookie(name){
    var mycookie=document.cookie;
    var idx=mycookie.indexOf(name+'=');
    var value=null;
    if(idx==-1){
        return null;
    }else{
        var start=mycookie.indexOf('=',idx)+1;
        var end=mycookie.indexOf(';',start);
        if(end==-1){
            end=mycookie.length;
        }
        value=unescape(mycookie.substring(start,end));
    }
    return value;
}
function setCookie(name,value){
    var Days = 2;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires="+ exp.toGMTString() + ";path=/";
}

function delCookie(name){
    // var value=getCookie(name);
    // if(value){
    //     var expireDate=new Date();
    //     expireDate.setTime(expireDate.getTime()-1);
    //     document.cookie=name+'="";expires='+expireDate.toGMTString();
    // }
    setCookie(name, "");
}
