/**
 * Created by Administrator on 2017/10/9.
 * 浏览器版本检测
 */

function checkBrowser() {
    var userAgent = navigator.userAgent;
    //return false;
    if(userAgent.indexOf("MSIE")!=-1 || userAgent.indexOf('Trident/7.0') !== -1) {
        return false;
    }
    return true;
}

// ie 9.0以下或者360浏览器为ie模式 提示用户
if(!checkBrowser()) {
    $('body').addClass('ie');
}

