/**
 * @file : message.js
 * @author : ParkDoYoung
 * @date : 22.4.13
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
            $chatting: null,
            $chatInsert: null,
            $submit: null,
            $payment: null,
            $proInfo: null,
            $report: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$chatting = $('#chatting');
            self.els.$chatInsert = $('#chat-insert');
            self.els.$submit = $('#submit');
            self.els.$payment = $('#payment');
            self.els.$proInfo = $('#pro-info');
            self.els.$report = $('#report');
            // parameter 가져오기  peopleId
            self.data.peopleId = M.data.param('peopleId');
            // 내 정보 가져오기
            self.data.loginInfo = M.data.global("LOGIN_INFO");
            // initial date
            console.log(self.data.loginInfo);
            self.data.date = "1900.01.01";
            // 메세지 보낸사람에 대한 정보 가져오기
            $.sendHttp({
                path: SERVER_PATH.INFO,
                data: {
                    peopleId: self.data.peopleId,
                },
                succ: function (data) {
                    console.log(data);
                    self.data.isProRegisted = data.isProRegisted;
                    self.data.nicknameAdr = data.nickname;
                    self.data.imagepath = $.imagePath(data.imagePath, data.storeImageName);
                    // 채팅방 정보 가져오기
                    console.log(self.data.peopleId);
                    self.initView();
                }
            });
            // 채팅방 정보 가져오기
            $.sendHttp({
                path: SERVER_PATH.GET_MESSAGE_NUM,
                data: {
                    messageSender: self.data.loginInfo.peopleId,
                    messageReceiver: self.data.peopleId,
                },
                succ: function (data) {
                    console.log(data);
                },
                error: function (data) {
                    console.log(data);
                }
            });
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            $("#adversary").html(self.data.nicknameAdr);
            // 채팅방 정보 가져오기
            $.sendHttp({
                path: SERVER_PATH.GET_MESSAGE_NUM,
                data: {
                    messageSender: self.data.loginInfo.peopleId,
                    messageReceiver: self.data.peopleId,
                },
                succ: function (data) {
                    console.log(data);
                    self.data.chatNumber = data.chatNumber;
                    $.sendHttp({
                        path: SERVER_PATH.GET_MESSAGE,
                        data: {
                            chatNumber: self.data.chatNumber
                        },
                        succ: function (data) {
                            console.log(data);
                            console.log(data.Data);
                            $("#chatting").html(" "); // 기존 채팅 내역 지우기
                            if (data.Data.length === 0) {
                                self.noData();
                                return;
                            }
                            for (var i = 0; i < data.Data.length; i++) {
                                // data.date 와 날자가 같으면 안보이게하기
                                $("#chatting").append(HTML.MESSAGE_DATE); // 날짜칸 추가 후 위의 데이터와 날짜가 같으면 안보이게하기
                                if (self.data.date === data.Data[i].messageTime.substr(0, 10)) {
                                    $("div.chat-room-messages:eq(" + i + ")").hide();
                                }
                                self.data.date = data.Data[i].messageTime.substr(0, 10);
                                $("div.chat-date:eq(" + i + ")").html(data.Data[i].messageTime.substr(0, 4) + "년 " + data.Data[i].messageTime.substr(5, 2) + "월 " + data.Data[i].messageTime.substr(8, 2) + "일")
                                // 수신 메세지
                                if (data.Data[i].messageReceiver === self.data.loginInfo.peopleId) {
                                    self.data.adversary = data.Data[i].messageSender;
                                    // 견적서 or 메세지
                                    if (data.Data[i].messageNumber.substr(0, 8) === 'MESSAGES') { // 메시지인 경우
                                        self.receiveMessage(data.Data[i], i);
                                    } else {
                                        self.receiveEstimate(data.Data[i], i);
                                    }
                                } else { // 송신 메세지
                                    if (data.Data[i].messageNumber.substr(0, 8) === 'MESSAGES') { // 메시지인 경우
                                        self.sendMessage(data.Data[i], i);
                                    } else {
                                        self.sendEstimate(data.Data[i], i);
                                    }
                                    self.data.adversary = data.Data[i].messageReceiver;
                                }
                            }
                            // 메시지 시간 저장하기
                            var chatNumber = data.Data[data.Data.length - 1].chatNumber;
                            var time = data.Data[data.Data.length - 1].messageTime;
                            console.log(time);
                            $.storage.setMessageTime(chatNumber, time);
                        },
                        error: function (data) {
                            console.log(data);
                        }
                    });
                }
            });

        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            $("#chat-insert").on("keydown", function (key) {
                if (key.keyCode == 13) {
                    self.sendEvent();
                }
            });
            $("#chat-insert").on("focus", function () {
                var scrollHeight = $(document).height();
                $('body,html').animate({
                    scrollTop: scrollHeight
                }, 800);
            });
            $("#submit").on('click', function () {
                self.sendEvent();
            });
            $(self.els.$proInfo).on('click', function () {
                var id = self.data.peopleId;
                console.log(id);
                if (self.data.isProRegisted === true) {
                    $.movePage({
                        url: "/www/html/pro/proInfo.html",
                        param: {
                            proId: id,
                        }
                    });
                } else {
                    $.movePage({
                        url: "/www/html/people/peopleInfo.html",
                        param: {
                            peopleId: id,
                        }
                    });
                }
            });
            $(self.els.$report).on('click', function () {
                var id = self.data.peopleId;
                $.movePage({
                    url: "/www/html/service/reportWrite.html",
                    param: {
                        id: id,
                    }
                });
            })
        },
        receiveMessage: function receiveMessage(data, idx) {
            var self = this;
            $("#chatting").append(HTML.MESSAGE_RECEIVE);
            $("div.bubble:eq(" + idx + ")").attr('id', data.messageNumber);
            $("div#" + data.messageNumber + "").children(".sender-message-box").html(data.messageContent);
            $("div#" + data.messageNumber + "").children(".message-status").children(".message-time").html("<span>" + data.messageTime.substr(12, 5) + "</span>");
            $("div#" + data.messageNumber + "").children(".chat-profile").html("<img src='" + self.data.imagepath + "'>");
        },
        receiveEstimate: function receiveEstimate(data, idx) {
            var self = this;
            $("#chatting").append(HTML.MESSAGE_ESTIMATE);
            $("div.bubble:eq(" + idx + ")").attr('id', data.messageContent);
            $("div#" + data.messageContent + "").children(".receiver-message-box").html(data.messageContent);
            $("div#" + data.messageContent + "").children(".message-status").children(".message-time").html("<span>" + data.messageTime.substr(12, 5) + "</span>");
            $("div#" + data.messageContent + "").children(".chat-profile").html("<img src='" + self.data.imagepath + "'>");
            $("div.message-subtitle").children("span").html(self.data.loginInfo.peopleId);
            $.sendHttp({
                path: SERVER_PATH.ESTIMATE_DETAIL,
                data: {
                    estimateNumber: data.messageContent
                },
                succ: function (datas) {
                    console.log(datas);
                    self.getRequest(datas.requestNumber, data.messageContent, datas.quotePrice);
                }
            });
            self.setEvent(data.messageContent);
        },
        sendMessage: function sendMessage(data, idx) {
            $("#chatting").append(HTML.MESSAGE_SEND);
            $("div.bubble:eq(" + idx + ")").attr('id', data.messageNumber);
            $("div#" + data.messageNumber + "").children(".message-status").children(".message-time").html("<span>" + data.messageTime.substr(12, 5) + "</span>");
            $("div#" + data.messageNumber + "").children(".receiver-message-box").html(data.messageContent);
        },
        sendEstimate: function sendEstimate(data, idx) {
            var self = this;
            $("#chatting").append(HTML.MESSAGE_SEND_ESTIMATE);
            $("div.bubble:eq(" + idx + ")").attr('id', data.messageContent);
            $("div#" + data.messageContent + "").children(".receiver-message-box").html(data.messageContent);
            $("div#" + data.messageContent + "").children(".message-status").children(".message-time").html("<span>" + data.messageTime.substr(12, 5) + "</span>");
            $("div#" + data.messageContent + "").children(".chat-profile").html("<img src='" + self.data.imagepath + "'>");
            $("div.message-subtitle").children("span").html(data.messageReceiver);
            $.sendHttp({
                path: SERVER_PATH.ESTIMATE_DETAIL,
                data: {
                    estimateNumber: data.messageContent
                },
                succ: function (datas) {
                    console.log(datas);
                    self.getRequest(datas.requestNumber, data.messageContent, datas.quotePrice);
                }
            });
            self.setEvent(data.messageContent);
        },
        sendEvent: function sendEvent() {
            var self = this;
            var value = $(self.els.$chatInsert).val().trim();
            $.sendHttp({
                path: SERVER_PATH.SET_MESSAGE,
                data: {
                    messageSender: self.data.loginInfo.peopleId,
                    messageReceiver: self.data.peopleId,
                    messageContent: value
                },
                succ: function (data) {
                    console.log(data);
                    self.data.chatNumber = data.chatNumber;
                    self.els.$chatInsert.val("");
                    self.initView();
                    var scrollHeight = $(document).height();
                    $('body,html').animate({
                        scrollTop: scrollHeight
                    }, 800);
                }
            });
        },
        getRequest: function getRequest(number, id, price) {
            $.sendHttp({
                path: SERVER_PATH.REQUEST_DETAIL,
                data: {
                    requestNumber: number
                },
                succ: function (data) {
                    console.log(data);
                    console.log(id);
                    $("div#" + id + "").children(".sender-message-box").children(".service-info").children(".service-name").html(data.category);
                    $("div#" + id + "").children(".sender-message-box").children(".price-info").children(".price-value").html(price + '원');
                }
            })
        },
        setEvent: function setEvent(id) {
            $("#" + id).on('click', function () {
                console.log(id);
                $.movePage({
                    url: "/www/html/pro/estimateDetail.html",
                    param: {
                        estimateNumber: id
                    }
                })
            });

        },
        noData: function noData() {
            $("#chatting").append(HTML.NO_LIST);
            $("#title-h3").html("대화 내역이 없습니다.");
            $(".desc").html("채팅을 시작해보세요!");
        },
    };
    window.__page__ = page;
})(jQuery, __config__, window);

(function ($, M, pageFunc, window) {

    M.onReady(function () {
        pageFunc.init();
        // pageFunc.initView();
        pageFunc.initEvent();
    });
    M.onRestore(function () {
        pageFunc.initView();
    });

})(jQuery, M, __page__, window);