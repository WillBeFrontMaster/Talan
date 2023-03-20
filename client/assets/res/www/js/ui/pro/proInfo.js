/**
 * @file : proInfo.js
 * @author : ParkDoYoung
 * @date : 22.4.10
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
            $chatting : null,
            $report : null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$list = $('#list');
            self.els.$chatting = $('#chatting');
            self.els.$report = $('#report');
            //parameter
            self.data.proId = M.data.param("proId");
            console.log(self.data.proId);
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            self.els.$feedList = $('#feed-list');
            // feed list 가져오기
            $.sendHttp({
                path: SERVER_PATH.FEED_LIST_BY_WRITER,
                data: {
                    proId : self.data.proId,
                    cnt : "0"
                },
                succ: function (data) {
                    // 리스트의 개수에 따라
                    console.log(data);
                    if (data.list.length > 0){
                        $("#list").html(" ");
                        for (var i = 0; i < data.list.length; i++) {
                            self.addFeedList(data.list[i], i);
                        }
                    }else{
                        self.noFeedList();
                    }
                }
            });
            // 프로 정보 가져오기
            $.sendHttp({
                path : SERVER_PATH.PRO_INFO,
                data : {
                    proId : self.data.proId
                },
                succ : function(data){
                    console.log(data);
                    self.data.nickname = data.nickname;
                    $("div.people-info-name").html(data.nickname + "님의 프로필");
                    $("div.people-info-name").append($.setStar(data.kindScore,data.reviewCount));
                    $(".pro-image-img").attr('src',$.imagePath(data.imagePath,data.storeImageName));
                    $(".item-info").html(data.intro + "</br>" +
                    "전문 분야 : " + data.category + "</br>" +
                    "자격 정보 : " + data.license + "</br>" +
                    "경력 : " + data.experiencePeriod);
                }
            });
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
            // 게시물 링크 등록하기
            $(self.els.$list).on('click','li',function(){
                console.log($(this).attr('id'));
                $.movePage({
                    url : "/www/html/pro/feedDetail.html",
                    param : {
                        feedNumber : $(this).attr('id'),
                    }
                });
            });
        },
        addFeedList: function addFeedList(feedData, idx) {
            $("#list").append(HTML.FEED_CONTENT);
            $("strong.ellipsis_1:eq(" + idx + ")").html(feedData.feedTitle);
            $("p.ellipsis_1:eq(" + idx + ")").html(feedData.feedContent);
            $("#list>li:eq(" + idx + ")").attr('id', feedData.feedNumber);
            $(".feed-image:eq(" + idx + ")").attr('src',$.imagePath(feedData.filePath,feedData.storeFileName,null,null));
        },
        noFeedList: function noFeedList(){
            var self = this;
            $(self.els.$list).hide();
            $(self.els.$list).after(HTML.NO_LIST);
        },
    };
    window.__page__ = page;
})(jQuery, __config__, window);

(function ($, M, pageFunc, window) {

    M.onReady(function () {
        pageFunc.init();
        pageFunc.initView();
        pageFunc.initEvent();
    });
    M.onRestore(function () {
        pageFunc.initView();
    });
})(jQuery, M, __page__, window);