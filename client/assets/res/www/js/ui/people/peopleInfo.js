/**
 * @file : peopleInfo.js
 * @author : ParkDoYoung
 * @date : 22.4.12
 */

(function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $goPro : null,
            $chatting : null,
            $report : null,
        },
        data: {},
        init: function init() {
            var self = this;
            // param
            self.data.peopleId = M.data.param("peopleId");
            self.els.$chatting = $("#chatting");
            self.els.$report = $('#report');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            $.sendHttp({
                path : SERVER_PATH.INFO,
                data : {
                    peopleId : self.data.peopleId
                },
                succ : function(data){
                    console.log(data);
                    self.data.proId = self.data.peopleId;
                    self.data.nickname = data.nickname;
                    $("div.pro-feed-name").html(self.data.peopleId);
                    $(".pro-image-img").attr('src',$.imagePath(data.imagePath,data.storeImageName,null,null));
                    $("div.item-info").html(data.intro + "</br>");
                    $("#pro-name").html(self.data.peopleId);
                }
            })
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            // 채팅 이벤트
            $(self.els.$chatting).on('click',function(){
                console.log(self.data.proId);
                $.movePage({
                    url : "/www/html/service/message.html",
                    param : {
                        nickname : self.data.nickname,
                        peopleId : self.data.proId
                    }
                });
            });
            // 신고 이벤트
            $(self.els.$report).on('click',function(){
                $.movePage({
                    url : "/www/html/service/reportWrite.html",
                    param : {
                        id : self.data.proId
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