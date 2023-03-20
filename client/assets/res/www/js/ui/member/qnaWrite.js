/**
 * @file : qnaWrite.js
 * @author : ParkDoYoung
 * @date : 22.4.17
 */

(function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $head: null,
            $inquiryNumber: null,
            $subject: null,
            $content: null,
            $chk1: null,
            $password: null,
            $submit: null,
            $cancel: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$head = $("#head");
            self.els.$inquiryNumber = $('#inquiry-number');
            self.els.$subject = $('#subject');
            self.els.$content = $('#content');
            self.els.$chk1 = $('#chk1');
            self.els.$password = $('#password');
            self.els.$submit = $('#submit');
            self.els.$cancel = $('#cancel');
            $("#password").attr("disabled", true);
            // 수정인지?
            if ($.isEmpty(M.data.param('isModify'))) { // 수정아닐때
                self.data.isModify = false;
            } else { // 수정일때
                self.data.inquiryNumber = M.data.param("inquiryNumber");
                self.data.isModify = true;
                $.sendHttp({
                    path: SERVER_PATH.QNA_DETAIL,
                    data: {
                        inquiryNumber: self.data.inquiryNumber
                    },
                    succ: function (data) {
                        console.log(data);
                        $("#subject").val(data.inquiryTitle);
                        $("#content").html(data.inquiryContent);
                        if (data.inquirySecretStatus === "1") {
                            $("#chk1").prop("checked", true);
                        }
                        $("#password").attr("disabled", true);
                        $("#submit").html("수정하기");
                        $("#chk1").attr('disabled', true);
                    }
                });
            }

        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            // check event
            $(self.els.$chk1).change(function () {
                if ($(self.els.$chk1).is(":checked")) {
                    $("#password").attr("disabled", false);
                    $("#password").val("");
                } else {
                    $("#password").attr("disabled", true);
                }
            });
            $(self.els.$submit).on('click', function () {
                var inquiryTitle = self.els.$subject.val().trim();
                var inquiryContent = self.els.$content.val().trim();
                if ($.isEmpty(inquiryTitle)) {
                    return swal.fire('제목을 입력하세요', '필수 입력사항입니다.', 'error')
                }
                ;
                if ($.isEmpty(inquiryContent)) {
                    return swal.fire('내용을 입력하세요', '필수 입력사항입니다.', 'error')
                }
                ;
                if (self.data.isModify === true) { // 수정하기
                    var inquiryNumber = self.data.inquiryNumber;

                    $.sendHttp({
                        path: SERVER_PATH.QNA_UPDATE,
                        data: {
                            inquiryNumber: inquiryNumber,
                            inquiryTitle: inquiryTitle,
                            inquiryContent: inquiryContent
                        },
                        succ: function (data) {
                            console.log(data);
                            swal.fire('수정에 성공했습니다.')
                                .then((result) => {
                                    self.initView();
                                });
                        }
                    });
                } else { // 등록하기
                    var status = "0";
                    var password = self.els.$password.val().trim();
                    if ($("#chk1").is(":checked")) {
                        status = "1";
                        if ($.isEmpty(password)) {
                            return swal.fire('패스워드를 입력하세요', '패스워드는 필수 입력사항입니다.', 'error');
                        }
                    }

                    if (status === 0) { // 비밀글 일떄
                        password = null;

                    }

                    $.sendHttp({
                        path: SERVER_PATH.QNA_REGIST,
                        data: {
                            inquiryTitle: inquiryTitle,
                            inquiryContent: inquiryContent,
                            secretStatus: status,
                            inquiryPassword: password
                        },
                        succ: function (data) {
                            console.log(data);
                            swal.fire({
                                title: '등록에 성공했습니다!',
                                icon: 'success'
                            }).then((result) => {
                                console.log(result);
                                $.moveBack();
                            });
                        }
                    });
                }
            });
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