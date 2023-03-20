/**
 * @file : reviewDetail.js
 * @author : ParkDoYoung
 * @date : 22.4.19
 */

(function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $comment: null,
            $submit: null,

        },
        data: {},
        init: function init() {
            var self = this;
            self.data.reviewNumber = M.data.param('reviewNumber');
            console.log(self.data.reviewNumber);
            self.els.$comment = $("#comment");
            self.els.$submit = $("#submit");
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            // 리뷰 보기
            $.sendHttp({
                path: SERVER_PATH.REVIEW_DETAIL,
                data: {
                    reviewNumber: self.data.reviewNumber
                },
                succ: function (data) {
                    $("#subject").html(data.reviewTitle);
                    $("#content").html(data.reviewContent);
                    $("#star-point").html(" ");
                    for (var i = 0; i < data.starPoint; i++) {
                        $("#star-point").append("⭐");
                    }
                    $("#pro-id").html(data.proId);
                    $("#pro-review").html(data.reviewResponse);
                }
            });
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$submit.on('click', function () {
                var data1 = self.els.$comment.val().trim();
                if ($.isEmpty(data1)) {
                    return swal.fire('값을 입력하세요', '입력값은 필수입니다', 'error');
                }
                $.sendHttp({
                    path: SERVER_PATH.REVIEW_ANSWER,
                    data: {
                        reviewNumber: self.data.reviewNumber,
                        reviewResponse: data1
                    },
                    succ: function (data) {
                        console.log(data);
                        swal('답변을 등록했습니다.', '', 'success').then((result) => {
                            $("#comment").val(" ");
                            self.initView();
                            }
                        );
                    }
                });
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