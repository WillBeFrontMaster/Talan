/**
 * @file : revirewWrite.js
 * @author : ParkDoYoung
 * @date : 22.4.18
 */

(function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $paymentNumber : null,
            $subject : null,
            $content : null,
            $starPoint : null,
            $submit : null,
            $cancel : null,
        },
        data: {},
        init: function init() {
            var self = this;
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            self.data.paymentNumber = M.data.param("paymentNumber");
            console.log(self.data.paymentNumber);
            self.els.$subject = $("#subject");
            self.els.$content = $("#content");
            self.els.$starPoint = $("#star-point");
            self.els.$submit = $("#submit");
            self.els.$cancel = $("#cancel");
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            $("#payment-number").val(self.data.paymentNumber);
            self.els.$submit.on('click',function(){
                var paymentNumber = self.data.paymentNumber;
                var subject = self.els.$subject.val().trim();
                var content = self.els.$content.val().trim();
                var starPoint = self.els.$starPoint.val().trim();
                if ($.isEmpty(subject)){return swal.fire('제목을 입력하세요','필수 입력사항입니다.','error');}
                if ($.isEmpty(content)){return swal.fire('내용을 입력하세요','필수 입력사항입니다.','error');}
                $.sendHttp({
                    path : SERVER_PATH.REVIEW_REGIST,
                    data : {
                        paymentNumber : paymentNumber,
                        reviewTitle : subject,
                        reviewContent : content,
                        starPoint : starPoint
                    },
                    succ : function(data){
                        console.log(data);
                        swal.fire('리뷰 작성을 완료했습니다','','success')
                            .then((result)=>{
                                $.moveBack();
                            });
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