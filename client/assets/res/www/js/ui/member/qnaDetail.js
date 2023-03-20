/**
 * @file : qnaDetail.js
 * @author : ParkDoYoung
 * @date : 22.4.16
 */

(function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var HTML = CONFIG.HTML;
    var page = {
        els: {
            $list: null,

        },
        data: {},
        init: function init() {
            var self = this;
            // param
            self.data.inquiryNumber = M.data.param("inquiryNumber");
            self.data.loginInfo = M.data.global("LOGIN_INFO");
            //
            self.els.$list = $('#list');

        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            $.sendHttp({
                path: SERVER_PATH.QNA_DETAIL,
                data: {
                    inquiryNumber: self.data.inquiryNumber,
                },
                succ: function (data) {
                    console.log(data);
                    self.data.Data = data;
                    // 비밀번호 확인
                    // 관리자의경우 예외를 어떻게 둘까
                    if (self.data.Data.inquiryPassword !== null) {
                        Swal.fire({
                            title: '패스워드를 입력하세요',
                            icon: 'info',
                            showCancelButton: true,
                            input: 'password',
                            confirmButtonColor: '#3085d6',
                            cancelButtonColor: '#d33',
                            confirmButtonText: '확인',
                            cancelButtonText: '취소'
                        }).then((result) => {
                            if (result.value === data.inquiryPassword || self.data.loginInfo.peopleId === 'admin') {
                                Swal.fire('승인이 완료되었습니다.', 'success');
                                self.drawList(data);
                            } else {
                                Swal.fire('비밀번호가 일치하지 않습니다.', '', 'fail').then(() => {
                                    $.moveBack();
                                });
                            }
                        });
                    } else {
                        self.drawList(data);
                    }
                }
            })
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
        },
        drawList: function drawList(data) {
            var self = this;
            console.log(self.data.Data);
            // 초기화
            $(self.els.$list).html(" ");
            $(self.els.$list).append(HTML.QNA_QUESTION);
            $("#inquiry-number").children("span").html(parseInt(data.inquiryNumber.substr(9, 5)));
            $("#subject").html(data.inquiryTitle);
            $("#content").html(data.inquiryContent);
            // 수정 되었을때
            if (data.inquiryRegisterDate !== data.inquiryModifyDate) {
                $("#inquiry-date").html(data.inquiryModifyDate.substr(0, 10));
                $("#subject").append(" (수정됨)");
            } else {
                $("#inquiry-date").html(data.inquiryRegisterDate.substr(0, 10));
            }
            // 답변 있을때
            if (data.responseStatus === "1") {
                $(self.els.$list).append(HTML.QNA_ANSWER);
                $("#response-number").html(parseInt(data.response.responseNumber.substr(10, 5)));
                $("#response-date").html(data.response.responseRegisterDate.substr(0, 10));
                $("#answer-title").html(data.response.responseTitle);
                $("#answer-content").html(data.response.responseContent)
            }
            // 내가 쓴글일때
            if (data.peopleId === self.data.loginInfo.peopleId) {
                $(self.els.$list).append(HTML.QNA_BUTTON_DELETE);
                $(self.els.$list).append(HTML.QNA_BUTTON_MODIFY);
                $("div.qna-button:eq(0)").attr('id', 'my-delete');
                $("div.qna-button:eq(1)").attr('id', 'my-modify');
                $("#my-delete").on('click', function () {
                    // 답변에 따라 삭제 불가능
                    if (data.responseStatus !== "1") {
                        swal.fire({
                            title: '정말 삭제하시겠습니까?',
                            icon: 'warning',
                            showCancelButton: true,
                            confirmButtonColor: '#3085d6',
                            cancelButtonColor: '#d33',
                            confirmButtonText: '확인',
                            cancelButtonText: '취소'
                        }).then((result) => {
                            if (result.value === true) { // 삭제하기
                                $.sendHttp({ // 확인
                                    path: SERVER_PATH.QNA_DELETE,
                                    data: {
                                        inquiryNumber: self.data.inquiryNumber
                                    }, succ: function () {
                                        $.moveBack();
                                    }
                                });
                            } else { // 취소
                            }
                        });
                    } else {
                        swal.fire('답변이 달린 글은 삭제할 수 없습니다.', '관리자에게 문의 바랍니다.', 'info');
                    }
                });
                $("#my-modify").on('click', function () {
                    if (data.responseStatus !== "1") {
                        $.movePage({
                            url: "/www/html/member/qnaWrite.html",
                            param: {
                                isModify: true,
                                inquiryNumber: self.data.inquiryNumber
                            }
                        });
                    } else {
                        swal.fire('답변이 달린 글은 수정할 수 없습니다.', '관리자에게 문의 바랍니다.', 'info');
                    }
                });
            }
            // 관리자일때
            if (self.data.loginInfo.peopleId === 'admin') {
                $(self.els.$list).append(HTML.QNA_BUTTON_DELETE);
                $("div.qna-button:eq(0)").attr('id', 'admin-delete');
                $("#admin-delete").on('click', function () {
                    swal.fire({
                        title: '정말 삭제하시겠습니까?',
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: '확인',
                        cancelButtonText: '취소'
                    }).then((result) => {
                        if (result.value === true) { // 삭제하기
                            $.sendHttp({ // 확인
                                path: SERVER_PATH.ADMIN_QNA_DELETE,
                                data: {
                                    inquiryNumber: self.data.inquiryNumber
                                },
                                succ: function (data) {
                                    swal('글이 삭제 되었습니다.');
                                    $.moveBack();
                                },
                                error: function (data) {
                                    console.log(data);
                                    swal("글 삭제에 실패 했습니다.");
                                }
                                // 삭제하기
                            });
                        } else { // 취소
                            swal('취소 하셨습니다.');
                        }
                    });
                });

                // 답변하기
                $(self.els.$list).append(HTML.QNA_BUTTON_ANSWER);
                $("div.qna-button:eq(1)").attr('id', 'admin-answer');
                $("#admin-answer").on('click', function () {
                    $.movePage({
                        url: "/www/html/admin/responseWrite.html",
                        param: {
                            inquiryNumber: self.data.inquiryNumber
                        }
                    });
                });
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
    M.onRestore(function () {
        pageFunc.initView();
    });

})(jQuery, M, __page__, window);