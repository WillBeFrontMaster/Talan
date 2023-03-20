/**
 * @file : main.js
 * @author : ParkDoYoung
 * @date : 22.4.8
 */

(function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $feedList: null,
            $logout: null,
            $popularItem : null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$feedList = $('#feed-list');
            self.els.$logout = $('#logout');
            self.els.$popularItem = $("#popular-item");
            $.sendHttp({
                path: SERVER_PATH.FEED_LIST,
                data: {
                    lastFeedNumber: '0',
                    cnt: '8'
                },
                succ: function (data) {
                    $("string.ellipsis2").html()
                    for (var i = 0; i < data.list.length; i++) {
                        self.addFeedList(data.list[i], i);
                    }
                    for (var i = 0;i<data.popularFeeds.length; i++){
                        $(".item"+i+"").children("img").attr('src',$.imagePath(data.popularFeeds[i].filePath,data.popularFeeds[i].storeFileName,null,null));
                        $(".item"+i+"").children(".popular-info").children(".popular-title").html("<p class=\"popular-title\">"+data.popularFeeds[i].feedTitle+"<img src=\"../img/comment.png\" class=\"comment-img\"><span class=\"comment-count\">"+data.popularFeeds[i].commentsCount+"</span></p>")
                        $(".item"+i+"").attr('name',data.popularFeeds[i].feedNumber);
                    }
                    var owl = $('.owl-carousel');
                    owl.owlCarousel({
                        items: 1,                 // 한번에 보여줄 아이템 수
                        loop: true,               // 반복여부
                        margin: 35,               // 오른쪽 간격
                        autoplay: true,           // 자동재생 여부
                        autoplayTimeout: 2500,    // 재생간격
                        autoplayHoverPause: true  //마우스오버시 멈출지 여부
                    });
                    $('.customNextBtn').on('click', function () {
                        owl.trigger('next.owl.carousel');
                    });

                    $('.customPrevBtn').on('click', function () {
                        owl.trigger('prev.owl.carousel', [300]);
                    });
                }
            });
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터

        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            $('#main-request-box').on('click', 'li', function () {
                var category = $(this).attr('id');
                console.log(category);
                $.movePage({
                    url: "/www/html/pro/requestList.html",
                    param: {
                        category: category
                    }
                })
            });
            $('#feed-list').on('click', 'li', function () {
                var feedNumber = $(this).attr('id');
                console.log(feedNumber);
                $.movePage({
                    url: "/www/html/pro/feedDetail.html",
                    param: {
                        feedNumber: feedNumber
                    }
                });
            });
            self.els.$logout.on('click', function () {
                $.sendHttp({
                    path: SERVER_PATH.LOGOUT,
                    data: {},
                    succ: function () {
                        M.data.removeGlobal("LOGIN_INFO");
                        M.data.removeGlobal("PRO_STATUS");
                        $.storage.clearAuth();
                        console.log("logout");
                        $.movePage({
                            url: "./member/login.html",
                            actionType: "CLEAR_TOP"
                        });
                    }
                });
            });
            $("#popular-item").on('click','.owl-item',function(){
                var feedNumber = $(this).attr('name');
                $.movePage({
                    url: "/www/html/pro/feedDetail.html",
                    param: {
                        feedNumber: feedNumber
                    }
                })
            });
        },
        addFeedList: function addFeedList(feedData, idx) {
            $("strong.ellipsis_1:eq(" + idx + ")").html(feedData.feedTitle);
            $("p.ellipsis_1:eq(" + idx + ")").html(feedData.feedContent);
            $("#feed-list").children("li.feed-li:eq(" + idx + ")").attr('id', feedData.feedNumber);
            if (feedData.filePath === null) {
                $("div.thumbnail:eq(" + idx + ")").html("<img src='/res/www/img/profile-image.png'/>");
            } else {
                $("div.thumbnail:eq(" + idx + ")").html("<img src='" + $.imagePath(feedData.filePath, feedData.storeFileName) + "'/>");
            }
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
    M.onRestore(function () {
        pageFunc.initView();
    });
})(jQuery, M, __page__, window);