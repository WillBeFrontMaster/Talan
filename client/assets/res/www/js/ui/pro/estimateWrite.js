/**
 * @file : estimateWrite.js
 * @author : suhyun
 * @date : 2022.04.14
 */

 (function ($, CONFIG, module, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $timeExpect : null,
            $price : null,
            $content : null,
            $subject : null,
            $write : null,
            $cancel :null,
            $unit : null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$timeExpect = $('#time-expect');
            self.els.$price = $('#price');
            self.els.$content = $('#content');
            self.els.$subject = $('#subject');
            self.els.$write = $('#write');
            self.els.$cancel = $('#cancel');
            self.els.$unit = $('#unit');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            var requestNumber = M.data.param("requestNumber");
            module.onKeyupNum(self.els.$price);
            $(self.els.$write).on('click',function(){
                var timeExpect = self.els.$timeExpect.val().trim();
                var unit = self.els.$unit.val();
                var price = self.els.$price.val().trim();
                var subject = self.els.$subject.val().trim();
                var content = self.els.$content.val().trim();
                if ($.isEmpty(timeExpect)){return alert('예상 소요시간을 입력해주세요');}
                if (unit === '선택'){return alert('단위를 선택해주세요');}
                if ($.isEmpty(price)){return alert('가격을입력해주세요');}
                if ($.isEmpty(subject)){return alert('제목을 입력해주세요');}
                if ($.isEmpty(content)){return alert('내용을 입력해주세요');}
                $.sendHttp({
                    path : SERVER_PATH.ESTIMATE_REGIST,
                    data : {
                        requestNumber : requestNumber,
                        predictTime : timeExpect,
                        quotePrice : price, // unit + price
                        estimateTitle : subject,
                        estimateContent : content,
                    },
                    succ : function (data){
                        console.log(data);
                        swal("견적서 전송이 완료되었습니다!","","success")
                        .then(
                            (result)=>{
                                $.moveBack();
                            }
                        )
                    },
                    error : function (data){
                        swal("견적서 전송 오류","","error");
                    }
                });
            });
        }
    };
    window.__page__ = page;
})(jQuery,__config__, __util__, window);

(function ($, M, pageFunc, window) {

    M.onReady(function () {
        pageFunc.init();
        pageFunc.initView();
        pageFunc.initEvent();
    });

})(jQuery, M, __page__, window);