/**
 * @file : payment.js
 * @author : suhyun
 * @date : 2022.04.17
 */

 (function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $paymentNum: null,
            $estimateDate: null,
            $estimateContent: null,
            $amount: null,
            $paymentType: null,
            $purchase: null,
            $incomeName : null,

            $cardNumber: null,
            $cvc: null,
            $exdate1: null,
            $exdate2: null,
            $accountSelect: null,

        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$paymentNum = $('#payment-num');
            self.els.$estimateDate = $('#estimate-date');
            self.els.$estimateContent = $('#estimate-content');
            self.els.$amount = $('#amount');
            self.els.$paymentType = $('input:radio[name="payment-type"]');
            self.els.$purchase = $('#purchase');
            self.els.$incomeName = $('#income-name');
            self.els.$cardNumber = $('#card-number');
            self.els.$cvc = $('#cvc');
            self.els.$exdate1 = $('#exdate1');
            self.els.$exdate2 = $('#exdate2');
            self.els.$accountSelect = $('#account-select');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            console.log(M.data.param("estimateNumber"));
            var estimateNumber = M.data.param("estimateNumber");
            var self = this;
            $.sendHttp({
                path: SERVER_PATH.ESTIMATE_DETAIL,
                data:{
                    estimateNumber: estimateNumber
                },
                succ: function(data){
                    console.log(data);
                    self.data.Data = data;
                    self.els.$paymentNum.text(data.estimateNumber)
                    self.els.$estimateDate.text(data.estimateRegisterDate);
                    self.els.$estimateContent.text(data.estimateTitle);
                    self.els.$amount.text(data.quotePrice);
                },
            });
            $('#payment-card').css("display", "none");
            $('#payment-account').css("display", "none");
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            console.log(self.data.Data);
            $("input:radio[name='payment-type']").on('click', function(){
                self.changePayment();
            });
            self.els.$purchase.on('click', function(){
                self.goPayment();
            });
        },
        changePayment: function(){
            var self = this;
            console.log($("input:radio[name='payment-type']:checked").val())
            if($("input:radio[name='payment-type']:checked").val() == 'card'){
                console.log("카드");
                self.initView();
                $('#payment-card').css("display", "block");
                    
            }
            if($("input:radio[name='payment-type']:checked").val() == 'account'){
                console.log("무통장입금");
                self.initView();
                $('#payment-account').css("display", "block");
                    
            }
        },
        // requestPay : function () {
        //     var IMP = window.IMP;
        //     var estimateNumber = M.data.param("estimateNumber");
        //     IMP.init("imp22479890");
        //     // IMP.request_pay(param, callback) 결제창 호출
        //     IMP.request_pay({ // param
        //         pg: "html5_inicis", // 이니시스
        //         pay_method: "vbank", //
        //         merchant_uid: paymentNumber,
        //         name: category,
        //         amount: paymentPrice,
        //         buyer_email: email,
        //         buyer_name: nickname,
        //         buyer_tel: phone,
        //     }, function (rsp) { // callback
        //         if (rsp.success) {
        //             // 결제 성공 시 로직, 제가 만든 결제 API를 호출해서 INSERT 해주십셔
        //             $.sendHttp({
        //                 path: SERVER_PATH.PAYMENT,
        //                 data:{
        //                     estimateNumber: estimateNumber,
        //                     paymentPrice: self.data.Data.quotePrice,
        //                     paymentType: self.els.$paymentType.val()
        //                 },
        //                 succ:function(data){
        //                     swal('결제가 완료되었습니다.','','success')
        //                         .then(function(result){
        //                             $.movePage({
        //                                 url:"/www/html/people/paymentList.html",
        //                             })
        //                         })    
        //                 },
        //                 error: function(data, status){
        //                     swal('결제에 실패하였습니다.','','error');
        //                 }
        //             });
                    
        //         } else {
        //             return swal('결제에 실패하였습니다.','','error');
                    
        //         }
        //     });
        // },
        goPayment: function(){
            var self = this;
            var estimateNumber = M.data.param("estimateNumber");
            
            console.log(M.data.param("estimateNumber"));
            console.log();
            console.log(self.els.$paymentType.val());
            var check = $("input:radio[name='payment-type']:checked").val();
            if(!($("input:radio[name='payment-type']").is(':checked'))){return swal('결제방식을 선택하세요.','','error');}
            if(check == 'card'){
                if($.isEmpty(self.els.$cardNumber.val()) || $.isEmpty(self.els.$cvc.val()) || $.isEmpty(self.els.$exdate1.val()) || $.isEmpty(self.els.$exdate2.val())){
                    return swal('카드정보를 입력하세요.','','error');
                }
                if(self.els.$cardNumber.legnth > 16){return swal('올바른 카드번호를 입력하세요.','','error');}
            }
            if(check == 'account'){
                if($.isEmpty(self.els.$accountSelect)){return swal('입금할 계좌를 선택하세요','','error');}
                if($.isEmpty(self.els.$incomeName)){return swal('입금자명을 입력하세요.','','error');}
            }
            console.log(self.data.Data.quotePrice);
            console.log(estimateNumber);
            console.log(self.els.$paymentType.val());
            $.sendHttp({
                path: SERVER_PATH.PAYMENT,
                data:{
                    estimateNumber: estimateNumber,
                    paymentPrice: self.data.Data.quotePrice,
                    paymentType: self.els.$paymentType.val()
                },
                succ:function(data){
                    swal('결제가 완료되었습니다.','','success')
                        .then(function(result){
                            $.movePage({
                                url:"/www/html/people/paymentList.html",
                            });
                            }    
                        );
                },
                error: function(data, status){
                    swal('결제에 실패하였습니다.','','error');
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