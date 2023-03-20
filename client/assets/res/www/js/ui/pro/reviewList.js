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
                path : SERVER_PATH.REVIEW_LIST_PRO,
                succ : function(data){
                    console.log(data);
                    for (var i = 0; i<data.list.length;i++){
                        self.els.$list.append(HTML.QNA_ITEM);
                        $("li.item-container-box:eq("+i+")").attr('id',data.list[i].reviewNumber);
                        $("div.item-box-l:eq("+i+")").html(data.list[i].peopleNickname);
                        $("div.item-box-r:eq("+i+")").html(data.list[i].reviewDate);
                        $("div.item-box-title:eq("+i+")").html(data.list[i].reviewTitle);
                        $("div.item-box-content:eq("+i+")").html(data.list[i].reviewContent)
                    }
                    if (data.list.length === 0){
                        self.els.$list.append(HTML.NO_LIST);
                        $("#title-h3").html("받은 리뷰가 없습니다.");
                        $(".desc").html('후기를 남긴 사용자가 없습니다.');
                    }
                }
            });
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$list.on('click','li',function(){
                console.log($(this).attr('id'));
                var reviewNumber = $(this).attr('id');
                $.movePage({
                    url : "/www/html/people/reviewDetail.html",
                    param : {
                        reviewNumber : reviewNumber
                    }
                });
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

})(jQuery, M, __page__, window);