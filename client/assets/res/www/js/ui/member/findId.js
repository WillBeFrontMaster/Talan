/**
 * @file :findId.js
 * @author : suhyun
 * @date : 2022.04.08
 */

(function ($, M, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $name: null,
            $email: null,
            $findIdBtn: null,
            $findPwBtn: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$name = $('#user-name');
            self.els.$email = $('#email');
            self.els.$findIdBtn = $('#find-id-btn');
            self.els.$findPwBtn = $('#find-pw');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            this.els.$findIdBtn.on('click', function () {
                self.findId();
            });
            this.els.$findPwBtn.on('click', function () {
                M.page.html('./findPw1.html');
            });
        },

        findId: function findId(){
            var self = this;
            var name = self.els.$name.val().trim();
            var email = self.els.$email.val().trim();
    
            if(name == ''){
                return swal("이름을 입력해주세요.","","warning");
            }
            if(email == ''){
                return swal("이메일을 입력해주세요.","","warning");
            }
    
            $.sendHttp({
                path: SERVER_PATH.FIND_ID,
                data:{
                    name : name,
                    email : email,
                },
                succ: function(data){
                    swal("아이디는 " +data.loginId+" 입니다.");
                },
                error: function(data){
                    swal("등록된 ID가 없습니다.","","error");
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