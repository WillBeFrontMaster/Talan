/**
 * @file : paymentDetail.js
 * @author : suhyun
 * @date : 2022.04.18
 */

 (function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $paymentDate: null,
            $paymentNumber: null,
            $paymentStatus: null,
            $paymentType: null,
            $paymentAmount: null,
            $requestTitle: null,
            $requestContent: null,
            $proId: null,
            $estimateTitle: null,
            $estimateContent: null,
            $reviewWrite: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$paymentDate = $('#payment-date');
            self.els.$paymentStatus = $('#payment-status');
            self.els.$paymentType = $('#payment-type');
            self.els.$paymentAmount = $('#payment-amount');
            self.els.$requestTitle = $('#request-title');
            self.els.$requestContent = $('#request-content');
            self.els.$proId = $('#pro-id');
            self.els.$estimateTitle = $('#estimate-title');
            self.els.$estimateContent = $('#estimate-content');
            self.els.$paymentCancel = $('#payment-cancel');
            self.els.$reviewWrite = $('#review-write');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            var paymentNumber = M.data.param("paymentNumber");
            console.log(paymentNumber);
            $.sendHttp({
                path: SERVER_PATH.PAYMENT_DETAIL,
                data:{
                    paymentNumber: paymentNumber
                },
                succ: function(data){
                    self.data.reviewNumber = data.reviewNumber;
                    $(self.els.$paymentDate).html(data.paymentDate);
                    $(self.els.$paymentNumber).html(data.paymentNumber);
                    $(self.els.$paymentStatus).html(data.progressiveStatus);
                    $(self.els.$paymentAmount).html(data.paymentPrice+"원");
                    $(self.els.$requestTitle).html(data.requestTitle);
                    $(self.els.$requestContent).html(data.requestContent);
                    $(self.els.$proId).html(data.nickname+" 님의 견적서");
                    $(self.els.$estimateTitle).html(data.estimateTitle);
                    $(self.els.$estimateContent).html(data.estimateContent);
                    if (data.reviewStatus === "1"){
                        $(self.els.$reviewWrite).html("리뷰 이동");
                        self.data.reviewStatus = "1";
                    }
                }
            })
        },


        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$reviewWrite.on('click', function(){
                if (self.data.reviewStatus === "1"){
                    $.movePage({
                        url:"/www/html/people/reviewDetail.html",
                        param:{
                            reviewNumber: self.data.reviewNumber
                        }
                    });
                }else{
                    $.movePage({
                        url:"/www/html/people/reviewWrite.html",
                        param:{
                            paymentNumber: M.data.param("paymentNumber")
                        }
                    });
                }

            })
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
    M.onRestore(function(){
        pageFunc.initView();
    })

})(jQuery, M, __page__, window);