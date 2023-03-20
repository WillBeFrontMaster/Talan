/**
 * @file : login.js
 * @author : suhyun
 * @date : 22.04.08
 */

 (function ($, M, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $peopleId: null,
            $password: null,
            $loginBtn: null,
            $autoLoginChk: null,
            $findIdBtn: null,
            $findPwBtn: null,
            $joinBtn: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$peopleId = $('#login-id');
            self.els.$password = $('#password');
            self.els.$loginBtn = $('#login-btn');
            self.els.$autoLoginChk = $('#auto-login-chk');
            self.els.$findIdBtn = $('#find-id');
            self.els.$findPwBtn = $('#find-pw');
            self.els.$joinBtn = $('#join-btn');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            var id = self.els.$peopleId.val().trim();
            var password = self.els.$password.val().trim();

            this.els.$loginBtn.on('click', function () {
              self.login(id, password);
            });
            this.els.$findIdBtn.on('click', function () {
              M.page.html('./findId.html');
            });
            this.els.$findPwBtn.on('click', function () {
              M.page.html('./findPw1.html');
            });
            this.els.$joinBtn.on('click', function () {
              M.page.html('./join1.html');
            });
        },
        
        //자동로그인
        setAutoLogin: function(id, pw){
            $.storage.setAuth(id,pw);
            // M.data.storage('AUTO_LOGIN_AUTH',{
            //     peopleId: id,
            //     password: pw
            // });
            // console.log(M.data.storage("AUTO_LOGIN_AUTH"));
        },
        unsetAutoLogin: function(){
            // M.data.removeStorage('AUTO_LOGIN_AUTH');
            $.storage.clearAuth();
        },

        login: function () {
            var self = this;
            var id = self.els.$peopleId.val().trim(); // 로그인 아이디 가져오기
            var pw = self.els.$password.val().trim(); // 비밀번호 가져오기
            var isAutoLogin = self.els.$autoLoginChk.prop('checked'); // true / false
      
            if (id == '') {
              return swal('아이디를 입력해주세요',"","warning");
            }
            if (pw == '') {
              return swal('비밀빈호를 입력해주세요',"","warning");
            }
            $.sendHttp({
                path: SERVER_PATH.LOGIN,
                data: {
                    peopleId: id,
                    password: pw
                },
                succ: function (data) {
                    //로그인이 성공했을 때 콜백
                    if (id === 'admin'){ // admin
                        M.data.global({
                            "LOGIN_INFO": {
                                nickname: 'admin',
                                auth: 'admin', // people, pro, admin
                                peopleId : 'admin'
                            }
                        });
                    }else{ // 다른사람들
                        if (isAutoLogin) self.setAutoLogin(id, pw);
                        M.data.global({
                            "LOGIN_INFO": {
                                nickname: data.session.nickname,
                                auth: data.isProRegisted, // people, pro, admin
                                peopleId : id
                            }
                        });
                        if(M.data.global("LOGIN_INFO.auth")){
                            M.data.global({
                                "PRO_STATUS":{
                                    proId:id,
                                    proStatus: true,
                                }
                            });
                        }
                        else{
                            M.data.global({
                                "PRO_STATUS":{
                                    proId: "",
                                    proStatus: false,
                                }
                            });
                        }
                    }

                    M.page.html("../main.html");
                },
                error: function (data) {
                    swal("아이디 혹은 비밀번호가 틀립니다.","","warning")
                    .then(
                        (result)=>{
                            self.els.$peopleId.val('');
                            self.els.$password.val('');
                        }
                    )
                }
            });




        }
    };
    window.__page__ = page;
})(jQuery, M, __config__, window);

(function ($, M, pageFunc, window) {

    M.onReady(function () {
        pageFunc.init();
        pageFunc.initView();
        pageFunc.initEvent();
    });

})(jQuery, M, __page__, window);