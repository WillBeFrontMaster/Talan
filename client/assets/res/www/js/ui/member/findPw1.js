/**
 * @file : findPw1.js
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
            $loginId: null,
            $userNm: null,
            $email: null,
            $findPwBtn: null
        },
        data: {},
        init: function init() {
            this.els.$loginId = $('#login-id');
            this.els.$userNm = $('#user-name');
            this.els.$email = $('#email');
            this.els.$findPwBtn = $('#find-pw-btn');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            this.els.$findPwBtn.on('click', function(){
                self.findPw();
            });
        },

        findPw: function(){
            var self = this;
            var id = this.els.$loginId.val().trim();
            var name = this.els.$userNm.val().trim();
            var email = this.els.$email.val().trim();

            if(id == ''){
                return swal("아이디를 입력해주세요.","","warning");
            }
            if(name == ''){
                return swal("이름을 입력해주세요.","","warning");
            }
            if(email == ''){
                return swal("이메일을 입력해주세요.","","warning");
            }

            $.sendHttp({
                path: SERVER_PATH.FIND,
                data:{
                    peopleId: id,
                    name: name,
                    email: email
                },
                succ: function(data){
                    if(data.existYn == 'Y'){
                        swal("본인인증에 성공했습니다.")
                        .then(
                            (result)=>{
                                M.page.html({
                                    path:"./findPw2.html",
                                    param: {
                                        "peopleId" : id
                                    }
                                });
                            }
                        )
                        
                    }
                    else{
                        return swal("일치하는 정보가 없습니다.","","error");
                    }
                },
                error: function(data){
                    swal("본인인증에 실패했습니다.","","warning");
                }
            })
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