/**
 * @file : findPw2.js
 * @author : suhyun
 * @date : 2022.04.10
 */

 (function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: { 
            $peopleId: null,
            $password: null,
            $repassword: null,
            $changePwBtn: null
        },
        data: {},
        init: function init() {
            this.els.$peopleId = $('#login-id');
            this.els.$password = $('#password');
            this.els.$repassword = $('#repassword');
            this.els.$changePwBtn = $('#change-pw-btn');
        },
        initView: function initView() {
            this.els.$peopleId.val(M.data.global("LOGIN_INFO.peopleId"));
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$changePwBtn.on('click', function(){
                self.changePw();
            })
        },

        changePw: function(){
            var self = this;
            var id = this.els.$peopleId.val().trim();
            var password = this.els.$password.val().trim();
            var repassword = this.els.$repassword.val().trim();

            if (password == '') {
                return swal("비밀번호를 입력해 주세요.","","warning");
            }
            if (repassword == '') {
                return swal("비밀번호 확인을 입력해 주세요.","","warning");
            }
            if (password != repassword) {
               return swal("비밀번호와 비밀번호 확인이 다릅니다.","","warning");
            }
        
            if(self.checkPw(password)){
                $.sendHttp({
                  path: SERVER_PATH.PASSWORD,
                  data: {
                    peopleId: id,
                    password: password
                  },
                  succ: function (data) {
                    swal("비밀번호가 성공적으로 변경되었습니다.\n로그인 페이지로 이동합니다.")
                    .then(
                      (result)=>{
                        M.page.html({
                          url: "./login.html",
                          actionType: "CLEAR_TOP"
                        });
                      }
                    )
                  }
                });
              }
            else{
                return swal("비밀번호는 8~20자 사이의 문자, 숫자, 특수문자를 한 개 이상 포함해야 합니다.","","warning");
            }
            
        
            },

            checkPw: function (password) {
              var reg_pw = /^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$/;
              if (!reg_pw.test(password)) {
                return false;
              }
              else {
                return true;
              }
            }

    };
    window.__page__ = page;
})(jQuery, __config__, window);

(function ($, M, pageFunc, window) {

    M.onReady(function () {
        pageFunc.init();
        pageFunc.initView();
        pageFunc.initEvent();
    });

})(jQuery, M, __page__, window);