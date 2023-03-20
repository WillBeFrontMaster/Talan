/**
 * @file : viewInfo.js
 * @author : suhyun
 * @date : 2022.04.12
 */

 (function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $name: null,
            $nickname: null,
            $birth: null,
            $sido: null,
            $email: null,
            $phone: null,
            $modifyBtn: null,
            $deleteBtn: null,
            $changePassword: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$name = $('#name');
            self.els.$nickname = $('#nickname');
            self.els.$birth = $('#birth');
            self.els.$address = $('#address');
            self.els.$email = $('#email');
            self.els.$phone = $('#phone');
            self.els.$modifyBtn = $('#modify-info');
            self.els.$deleteBtn = $('#delete-member');
            self.els.$changePassword = $('#change-password');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self =this;
            var peopleId = M.data.global("LOGIN_INFO.peopleId");
            console.log("LOGIN_INFO.peopleId");
            console.log(peopleId);
            
            $.sendHttp({
                path: SERVER_PATH.INFO,
                data:{
                    peopleId: peopleId
                },
                succ: function(data){
                    console.log(data.birth);
                    self.els.$name.text(data.name);
                    self.els.$nickname.text(data.nickname);
                    self.els.$birth.text(data.birth);
                    self.els.$address.text(data.address.split('`')[0]+" "+data.address.split('`')[1]);
                    self.els.$email.text(data.email);
                    self.els.$phone.text(data.phone);
                }
            });
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            var peopleId = M.data.global("LOGIN_INFO.peopleId");
            self.els.$modifyBtn.on('click', function(){
                M.page.html("./updateInfo.html")
            });
            self.els.$deleteBtn.on('click', function(){
                self.drop(peopleId);
            });
            self.els.$changePassword.on('click', function(){
                M.page.html("./findPw2.html")
            });
        },
        drop: function(){
            var peopleId = M.data.global("LOGIN_INFO.peopleId");
            swal({
                title: "탈퇴하시겠습니까?",
                text: "탈퇴하신 정보는 되돌릴 수 없습니다.",
                icon: "warning",
                buttons: true,
                dangerMode: true,
              })
              .then((willDelete) => {
                if (willDelete) {
                    $.sendHttp({
                        path: SERVER_PATH.OUT,
                        data: {
                          peopleId: peopleId
                        },
                        succ: function(data){
                            swal("탈퇴되었습니다","","success")
                            .then(
                                (result)=>{
                                    M.page.html({
                                        url: "./login.html",
                                        actionType: "CLEAR_TOP"
                                      });
                                }
                            )
                        },
                        error: function(status, data){
                            swal("탈퇴 오류","","warning")
                        }
                    })
                } else {
                }
              });
            
            
            
            // M.pop.alert({
            //     title: '탈퇴확인',
            //     message: '탈퇴하시겠습니까?',
            //     buttons: ['예', '아니오'],
            //     callback: function (index) {
            //       if (index == 0) {
            //         //탈퇴후 로그인페이지로 이동
            //         $.sendHttp({
            //           path: SERVER_PATH.OUT,
            //           data: {
            //             peopleId: peopleId
            //           },
            //           succ: function (data) {
            //             alert("정상적으로 탈퇴되었습니다.");
            //             M.page.html({
            //               url: "./login.html",
            //               actionType: "CLEAR_TOP"
            //             });
            //           },
            //           error: function (status, data){
            //               console.log(peopleId);
            //               alert("탈퇴 오류");
            //           }
            //         });
            //       } else {
            //         return false;
            //       }
            //     }
            //   });
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