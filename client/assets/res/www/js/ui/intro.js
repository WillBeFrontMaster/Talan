/**
 * @file : intro.js
 * @author : suhyun
 * @date : 2022.04.08
 */

// 페이지 단위 모듈
(function ($, M, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $percent: null,
            $progressBar: null
        },
        data: {},
        init: function init() {
            this.els.$percent = $('#percent');
            this.els.$progressBar = $('#progress-bar');
        },
        startProgress: function startProgress(succCallBack) {
            var $percent = this.els.$percent;
            var $progressBar = this.els.$progressBar;
            var count = 0;                              

            var interval = setInterval(function () {
                count += 10;
                $percent.html(count);
                $progressBar.css('width', count + '%')
                if (count == 100) {
                    clearInterval(interval); // 반복 실행을 멈춘다.
                    succCallBack();
                }
            }, 50); // 반복적으로 함수를 실행 1ms
        },

        moveLoginPage: function moveLoginPage() {
            M.page.html({
                url: "./member/login.html",
                actionType: "CLEAR_TOP"
            });
        },
        moveMainPage: function moveMainPage() {
            $.movePage({
              url: "./main.html",
              actionType: "CLEAR_TOP"
            });
          },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            var existLoginData = $.storage.getAuth();
            console.log(existLoginData);
            if (existLoginData) {
                this.startProgress(function () {
                    $.sendHttp({
                        path: SERVER_PATH.LOGIN,
                        data: {
                            peopleId: existLoginData.id,
                            password: existLoginData.pw
                        },
                        succ: function (data) {
                            //로그인이 성공했을 때 콜백
                            M.data.global({
                                "LOGIN_INFO": {
                                    nickname: data.session.nickname,
                                    auth: data.isProRegisted, // people, pro, admin
                                    peopleId : existLoginData.id
                                }
                            });
                            if("LOFIN_INFO.auth"){
                                M.data.global({
                                    "PRO_STATUS":{
                                        proId: existLoginData.id,
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
                            self.moveMainPage();
                        },
                        error: function () {
                            self.moveLoginPage();
                        }
                    });
                });
            } else {
                this.startProgress(this.moveLoginPage);
            }
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
        }
    };
    window.__page__ = page;
})(jQuery, M, __config__, window);

// 해당 페이지에서 실제 호출
(function ($, M, pageFunc, window) {

    // 화면에 리소스가 로딩을 끝내고 정상적으로 동작할 수 있는 시점에 대한 콜백
    // window.onload 와 비슷함.
    M.onReady(function () {
        pageFunc.init(); // 최초 화면 초기화
        pageFunc.initView();
        pageFunc.initEvent();
    });

})(jQuery, M, __page__, window);