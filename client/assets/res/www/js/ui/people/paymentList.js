/**
 * @file : paymentList.js
 * @author : suhyun
 * @date : 2022.04.17
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
            
        },
        data: {},
        init: function init() {
            var self = this;
            self.data.auth = M.data.param('auth');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            $.sendHttp({
                path: SERVER_PATH.PAYMENT_LIST,
                data:{},
                succ: function(data){
                    if(data.list.length == 0){
                        $('.no-received-quotes').css("display", "block");
                    }
                    else{
                        for(var i = 0; i < data.list.length; i++){
                            self.showPaymentList(data, i);
                        }
                    }
                },
                error: function(data, status){
                    console.log(data);
                    swal('결제내역을 불러오지 못했습니다','','error');
                }
            })
            
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            $(".container").on('click','li.item-container-box', function(){
                $.movePage({
                    url:"/www/html/people/paymentDetail.html",
                    param:{
                        paymentNumber: $(this).attr('id')
                    }
                })
            })
        },
        showPaymentList: function(data, i){
            $("ul.page-list").append(HTML.PAYMENT_LIST);
            $("li.item-container-box:eq("+i+")").attr('id',data.list[i].paymentNumber);
            $("div.item-box-l:eq("+i+")").html(data.list[i].paymentDate);
            $("div.item-box-r:eq("+i+")").html(data.list[i].paymentType);
            $(".pro-info-img").attr('src',$.imagePath(data.list[i].imagePath,data.list[i].storeImageName));
            $("div.item-box-title:eq("+i+")").html(data.list[i].nickname+" 님과의 매칭");
            $("div.item-box-pro:eq("+i+")").html(data.list[i].paymentPrice+"원");
            $("div.item-box-status:eq("+i+")").html(data.list[i].progressiveStatus);
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