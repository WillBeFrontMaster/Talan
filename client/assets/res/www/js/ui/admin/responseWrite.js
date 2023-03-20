/**
 * @file : responseWrite.js
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
            $answer: null,
            $cancel: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.data.inquiryNumber = M.data.param('inquiryNumber');
            self.els.$answer = $("#answer");
            self.els.$cancel = $("#cancel");
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            $.sendHttp({
                path: SERVER_PATH.QNA_DETAIL,
                data: {
                    inquiryNumber: self.data.inquiryNumber
                },
                succ: function (data) {
                    console.log(data);
                    $("#inquiry-number").html(data.inquiryNumber);
                    $("#subject").html("RE: " + data.inquiryTitle);
                    $("#content").val(data.inquiryContent + "&#10;&#10;=====================&#10;&#10;");
                }
            })
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$answer.on('click', function () {
                var content = $("#content").val();
                content = content.replace(/(\r\n|\n|\r)/gm, "</br>");
                console.log(content);
                $.sendHttp({
                    path: SERVER_PATH.ADMIN_QNA_RESPONSE,
                    data: {
                        responseTitle: $("#subject").val().trim(),
                        responseContent: $("#content").val().trim(),
                        inquiryNumber: self.data.inquiryNumber
                    },
                    succ: function (data) {
                        console.log(data);
                        $.moveBack();
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