/**
 * @file : reportList.js
 * @author : ParkDoYoung
 * @date : 22.4.18
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
            $list : null,

        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$list = $("#list");
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            $.sendHttp({
                path : SERVER_PATH.REPORT_LIST,
                succ : function(data){
                    for (var i = 0; i < data.list.length; i++){
                        self.els.$list.append(HTML.QNA_ITEM);
                        $("li.item-container-box:eq("+i+")").attr('id',data.list[i].reportNumber);
                        $("div.item-box-l:eq("+i+")").html(data.list[i].reportNumber);
                        if (data.list[i].reportStatus==="0") {
                            $("div.item-box-r:eq(" + i + ")").html("처리 대기중");
                        }else {
                            $("div.item-box-r:eq(" + i + ")").html("처리 완료");
                        }
                        $("div.item-box-title:eq("+i+")").html(data.list[i].reportPeople);
                        $("div.item-box-content:eq("+i+")").html(data.list[i].reportContent);
                    }
                }
            });

        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            $(self.els.$list).on('click','li',function(){
                var reportNumber = $(this).attr('id');
               $.movePage({
                   url : "/www/html/admin/reportDetail.html",
                   param : {
                       reportNumber : reportNumber
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