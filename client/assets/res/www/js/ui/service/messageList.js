/**
 * @file : messageList.js
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
            $chatList: null
        },
        data: {
            loginInfo : {},
            adversary : [],
        },
        init: function init() {
            var self = this;
            self.els.$chatList = $('#chat-list');
            self.data.loginInfo = M.data.global("LOGIN_INFO");
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            var myId = self.data.loginInfo.peopleId;

            console.log(myId);
            $.sendHttp({
                path: SERVER_PATH.GET_MESSAGE_LIST,
                succ: function (data) {
                    console.log(data);
                    $("#chat-list").html(" ");
                    for (var i = 0; i < data.list.length; i++) {
                        $("#chat-list").append(HTML.MESSAGE_LIST);
                        $("li.chat-container-box:eq(" + i + ")").attr('id',data.list[i].chatNumber);
                        $("li.chat-container-box:eq(" + i + ")").attr('name',data.list[i].nickname);
                        if (myId === data.list[i].messageReceiver){
                            $("div.chat-people-sender:eq(" + i + ")").html(data.list[i].messageSender);
                            self.data.adversary[i] = data.list[i].messageSender;
                            $("li.chat-container-box:eq(" + i + ")").attr('peopleId',data.list[i].messageSender);
                        }else{
                            $("div.chat-people-sender:eq(" + i + ")").html(data.list[i].messageReceiver);
                            self.data.adversary[i] = data.list[i].messageReceiver;
                            $("li.chat-container-box:eq(" + i + ")").attr('peopleId',data.list[i].messageReceiver);
                        }
                        $("div.chat-people-category:eq(" + i + ")").html('아무튼 사람에 대한 정보');
                        // 이미지 등록
                        $("div.chat-people-info:eq(" + i + ")").children("div:eq(0)").html("<img src='"+$.imagePath(data.list[i].imagePath,data.list[i].storeImageName)+"'>")
                        // message가 estimate 가 아니면
                        if (data.list[i].messageNumber.substr(0,7) === 'MESSAGE'){ //  message 이면 estimate box 불필요
                            $("div.is-estimate-box:eq(" + i + ")").hide();
                            $("div.chat-box-text:eq(" + i + ")").html(data.list[i].messageContent);
                        }else{
                            self.getEstimate(i,data.list[i].messageContent);
                        }
                        // new 인지 확인
                        var time = $.storage.getMessageTime(data.list[i].chatNumber);
                        if (time === data.list[i].messageTime){
                            $("div.chat-box-badge:eq("+i+")").hide();
                        }
                        // 메세지 보낸사람들의 정보 가져오기
                        self.getUserInfo(i);
                    }
                },
                error: function (data) {
                    console.log(data);
                }
            });
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            $(self.els.$chatList).on('click','li',function(){
                var chatNumber = $(this).attr('id');
                var nickname = $(this).attr('name');
                var peopleId = $(this).attr('peopleId');
                // console.log(chatNumber);

                $.movePage({
                    url : "./message.html",
                    param : {
                        chatNumber : chatNumber,
                        peopleId :peopleId,
                        nickname : nickname
                    }
                });
            });
        },
        getEstimate : function getEstimate(idx,estimateNumber){
            $.sendHttp({
                path : SERVER_PATH.ESTIMATE_DETAIL,
                data : {
                    estimateNumber : estimateNumber,
                },
                succ : function(data){
                    console.log(data);
                    $("div.chat-box-text:eq(" + idx + ")").html(data.estimateTitle);
                    $("div.chat-box-price:eq(" + idx + ")").append(data.quotePrice +'원');
                }
            });
        },
        getUserInfo : function getUserInfo(idx){
            var self = this;
            var adr = self.data.adversary[idx];
            $.sendHttp({
                path : SERVER_PATH.INFO,
                data : {
                    peopleId : adr
                },
                succ : function(data){
                    if (data.isProRegisted === true){
                        self.getUserProInfo(idx,data.address.replace('`',' '));
                    }else{
                        $("div.chat-people-category:eq(" + idx + ")").html(data.address.replace('`',' '));
                    }
                }
            })
        },
        getUserProInfo : function getUserProInfo(idx, param){
            var self = this;
            var adr = self.data.adversary[idx];
            $.sendHttp({
                path : SERVER_PATH.PRO_INFO,
                data : {
                    proId : adr
                },
                succ : function(data){
                    console.log(data);
                    $("div.chat-people-category:eq(" + idx + ")").html(data.category + " | " + param);
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
    M.onRestore(function(){
        pageFunc.initView();
    })
})(jQuery, M, __page__, window);