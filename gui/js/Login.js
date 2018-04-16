//
// ログインクラス
//
(function (){
"use strict";

    //
    // コンストラクタ
    //
    MSF.Login = function(){
        this.user = {};
    };


    //
    // 認証処理
    //
    MSF.Login.prototype.Authentication = function(user, pass){
        var isOk = false;

        for (var i = 0; i < MSF.AccountList.length; i++) {
            var ul = MSF.AccountList[i];
            if (user == ul.user && pass == ul.pass) {
                isOk = true;
                this.user = ul;
                break;
            }
        }

        return isOk;
    };
})();


$(function() {
    //
    // ログイン-クリックイベントハンドラー
    //
    $("#login").click(function() {

        var user = $("#userName").val();
        var password = $("#password").val();

        // ログインクラス
        var login = new MSF.Login();

        // 認証処理
        var isOk = login.Authentication(user, password);
        if (isOk) {
            // 画面遷移
            window.location.href = 'MSFMain.html?user=' + login.user.user + '&authority=' + login.user.authority;
        } else {
            //var msg = MSF.MessageInfo.Login.login.AllError;
            //$("#message").text(msg);
            $("#message").text(i18nx("Login.LoginError", "Username or password is incorrect."));
        }
    });

    // Enterキーでログイン
    $("#password").keypress( function ( e ) {
        if ( e.which == 13 ) $("#login").click();
    });

});
