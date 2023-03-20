/**
 * @file : feedDetail.js
 * @author : ParkDoYoung
 * @date : 22.4.12
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
            $commentList: null,
            $isMyFeed: null,
            $update: null,
            $delete: null,
            $commentContent: null,
            $commentSubmit: null,
            $feedImage: null,
        },
        data: {
            feedWriter: "",
            feedCnt: 0,
            myId: "",
            myNickname: "",
        },
        init: function init() {
            var self = this;
            self.els.$commentList = $('#comment-list');
            self.els.$isMyFeed = $('#is-my-feed');
            self.els.$update = $('#update');
            self.els.$delete = $('#delete');
            self.els.$commentContent = $('#comment-content');
            self.els.$commentSubmit = $('#comment-submit');
            self.els.$feedImage = $('#feed-image');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            // nick name get
            self.data.loginInfo = M.data.global("LOGIN_INFO");
            self.data.myNickname = self.data.loginInfo.nickname;
            var feedNumber = M.data.param("feedNumber");
            //
            $.sendHttp({
                path: SERVER_PATH.FEED_DETAIL,
                data: {
                    feedNumber: feedNumber,
                },
                succ: function succ(data) {
                    $("div.feed-title").html(data.feedTitle);
                    $("div.feed-text").html(data.feedContent);
                    $("div.feed-writer").html(data.feedWriterNickname);
                    $("div.feed-write-date").html(data.feedRegisterDate);

                    $("div#feed-image").html("<img src='" + $.imagePath(data.filePath, data.storeFileName, null, null) + "'/>");
                    self.data.feedWriter = data.feedWriterId;
                    self.getWriterInfo();
                },
                error: function errir(data) {
                    console.log(data);
                }
            });
            $.sendHttp({
                path: SERVER_PATH.FEED_COMMENT_DETAIL,
                data: {
                    feedNumber: feedNumber
                },
                succ: function succ(data) {
                    console.log(data);
                    $(self.els.$commentList).html("");
                    for (var i = 0; i < data.list.length; i++) {
                        $(self.els.$commentList).append(HTML.FEED_COMMENT_HTML);
                        $("div.comment-writer:eq(" + i + ")").html(data.list[i].nickname);
                        // x 버튼 글쓴이 == l일때만 보이기 관리자일 경우 처리 가능까지
                        if (self.data.myNickname === data.list[i].nickname || self.data.loginInfo.peopleId === 'admin') {
                            $("div.comment-write-date:eq(" + i + ")").append(data.list[i].feedCommentsRegisterDate);
                        } else {
                            $("div.comment-write-date:eq(" + i + ")").html(data.list[i].feedCommentsRegisterDate);
                        }
                        $("span.delete-comment:eq(" + i + ")").attr('id', data.list[i].feedCommentsNumber);
                        $("li.comment-content:eq(" + i + ")").html(data.list[i].feedCommentsContent);
                    }
                    self.checkWriter();
                }
            });
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            var feedNumber = M.data.param("feedNumber");
            self.els.$update.on('click', function () {
                $.movePage({
                    url: "/www/html/pro/feedWrite.html",
                    param: {
                        feedNumber: feedNumber
                    }
                });
            });
            self.els.$delete.on('click', function () {
                var result = confirm("정말로 삭제하시겠습니까?");
                if (result === false) {
                    return;
                }
                $.sendHttp({
                    path: SERVER_PATH.FEED_DELETE,
                    data: {
                        feedNumber: feedNumber,
                    },
                    succ: function (data) {
                        console.log(data);
                    }
                });
                $.movePage({
                    url: "/www/html/pro/proInfo.html",
                    actionType: "NO_HISTORY"
                });
            });
            $("#comment-list").on('click', 'span.delete-comment', function () {
                var self2 = this;
                if (self.data.loginInfo.peopleId === 'admin') {
                    swal.fire({
                        title: '정말 삭제하시겠습니까?!',
                        icon: 'info',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: '확인',
                        cancelButtonText: '취소'
                    }).then((result) => {
                        console.log(result);
                        if (result.value === true) {
                            $.sendHttp({
                                path: SERVER_PATH.FEED_COMMENT_DELETE,
                                data: {
                                    feedCommentsNumber: $(self2).attr('id')
                                },
                                succ: function (data) {
                                    swal('삭제되었습니다!');
                                    self.initView();
                                }
                            });
                        }
                    });
                } else {
                    swal.fire({
                        title: '정말 삭제하시겠습니까?!',
                        icon: 'info',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: '확인',
                        cancelButtonText: '취소'
                    }).then((result) => {
                        if (result.value === true) {
                            $.sendHttp({
                                path: SERVER_PATH.FEED_COMMENT_DELETE,
                                data: {
                                    feedCommentsNumber: $(self2).attr('id')
                                },
                                succ: function (data) {
                                    swal.fire('삭제되었습니다!');
                                    self.initView();
                                }
                            });
                        }
                    });
                }
            })
            self.els.$commentSubmit.on('click', function () {
                var comments = self.els.$commentContent.val().trim();
                if (comments !== "") {
                    console.log(comments);
                    $.sendHttp({
                        path: SERVER_PATH.FEED_COMMENT_REGIST,
                        data: {
                            feedNumber: feedNumber,
                            feedCommentsContent: comments
                        },
                        succ: function (data) {
                            alert('댓글작성에 성공했습니다!');
                            $(self.els.$commentContent).val("");
                            self.initView();
                        },
                        error: function (data) {
                            console.log(data);
                            alert('댓글 작성에 실패했습니다');
                        }
                    })
                }
            })
        },
        checkWriter: function checkWriter() {
            var self = this;
            var loginInfo = M.data.global("LOGIN_INFO");
            console.log(self.data.feedWriter);
            console.log(loginInfo.peopleId);
            if (self.data.feedWriter !== loginInfo.peopleId) {
                document.getElementById("is-my-feed").style.display = "none";
            }
        },
        getWriterInfo: function getWriterInfo() {
            var self = this;
            var proId = self.data.feedWriter;
            $.sendHttp({
                path: SERVER_PATH.PRO_INFO,
                data: {
                    proId: proId
                },
                succ: function (data) {
                    $("#feed-writer-image").html("<img src='" + $.imagePath(data.imagePath, data.storeImageName, "img.feed-link", proId) + "'>")

                }
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
    M.onRestore(function () {
        pageFunc.initView();
    });
})(jQuery, M, __page__, window);