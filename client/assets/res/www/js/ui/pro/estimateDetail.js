/**
 * @file : estimateDetail.js
 * @author : suhyun
 * @date : 2022.04.14
 */

 (function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $name : null,
            $timeExpect : null,
            $price : null,
            $subject : null,
            $content : null,
            $message : null,
            $purchase : null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$name = $('#pro-id');
            self.els.$timeExpect = $('#time-expect');
            self.els.$price = $('#price');
            self.els.$subject = $('#subject');
            self.els.$content = $('#content');
            self.els.$message = $('#message');
            self.els.$purchase = $('#purchase');
            self.data.loginInfo = M.data.global("LOGIN_INFO");
            self.data.estimateNumber = M.data.param('estimateNumber');
            console.log(self.data.estimateNumber);
            self.data.proId = M.data.param('proId');

        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            console.log(self.data.loginInfo);

            $.sendHttp({
                path : SERVER_PATH.ESTIMATE_DETAIL,
                data : {
                    estimateNumber : self.data.estimateNumber
                },
                succ : function(data){
                    console.log(data);
                    self.data.proId = data.proId;
                    $(self.els.$name).html(data.proId);
                    $(self.els.$timeExpect).val(data.predictTime);
                    $(self.els.$price).val(data.quotePrice+'원');
                    $(self.els.$subject).val(data.estimateTitle);
                    $(self.els.$content).html(data.estimateContent);

                    if (self.data.proId === self.data.loginInfo.peopleId){
                        $(".footer").css("display", "none");
                    }
                }
            });
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            $(self.els.$message).on('click',function(){
                console.log(self.data.proId);
                $.movePage({
                    url : "/www/html/service/message.html",
                    param : {
                        peopleId : self.data.proId
                    }
                });
            });
            $(self.els.$purchase).on('click', function(){
                console.log(self.data.estimateNumber);
                $.movePage({
                    url:"/www/html/people/payment.html",
                    param:{
                        estimateNumber : self.data.estimateNumber,
                    }
                })
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