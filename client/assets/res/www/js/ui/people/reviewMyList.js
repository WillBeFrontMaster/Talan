/**
 * @file : reviewMyList.js
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
            $lsit : null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$list = $("#list");
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            self.els.$list.html(" ");
            $.sendHttp({
                path : SERVER_PATH.REVIEW_MY_LIST,
                succ : function(data){
                    console.log(data);
                    for (var i = 0; i<data.list.length;i++){
                        self.els.$list.append(HTML.QNA_ITEM);
                        $("div.item-box-l:eq("+i+")").html(data.list[i].proNickname);
                        $("div.item-box-r:eq("+i+")").html(datal.list[i].reviewDate);
                        $("div.item-box-title:eq("+i+")").html(data.list[i].reviewTitle);
                    }
                    if (data.list.length === 0){
                        self.els.$list.append(HTML.NO_LIST);
                        $("#title-h3").html("작성한 리뷰가 없습니다.");
                        $(".desc").html('리뷰를 작성해보세요.');
                    }
                }
            });
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
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