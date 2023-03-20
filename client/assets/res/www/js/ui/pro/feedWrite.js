/**
 * @file : feedWrite.js
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
            $feedTitle: null,
            $feedContent: null,
            $submit: null,
            $cancel: null,
            $imgBtn : null,
            $imgName : null,

        },
        data: {
            isUpdate : false,
            feedNumber : "",
            fileName : "",
            filePath : "",
        },
        init: function init() {
            var self = this;
            self.els.$feedTitle = $('#feed-title');
            self.els.$feedContent = $('#feed-content');
            self.els.$cancel = $('#cancel');
            self.els.$submit = $('#submit');
            self.els.$imgBtn = $('#img-btn');
            self.els.$imgName = $('#img-name');
            var feedNumber = M.data.param("feedNumber");
            if (feedNumber !== ""){
                self.data.isUpdate = true;
            }
            self.data.feedNumber = feedNumber;
            console.log(feedNumber);
            if (feedNumber !== ""){
                self.getFeedInfo(feedNumber);
            }
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$submit.on('click',function(){
                console.log(self.data.filePath);
                console.log(self.data.isUpdate);
                if (self.data.filePath === ""){
                    if (self.data.isUpdate === true){
                        self.update();
                        console.log('update');
                    }else{
                        self.write();
                        console.log('write');
                    }
                }else{
                    if (self.data.isUpdate === true){
                        self.updateWithFile();
                        console.log('updateWithFile');
                    }else{
                        self.writeWithFile();
                        console.log('writeWithFile');
                    }
                }
            });
            self.els.$imgBtn.on('click', function(){
                $.picker({
                    succ : function succ(data){
                        $(self.els.$imgName).val(data.name);
                        self.data.filePath = data.path;
                    }
                });
            });
            self.els.$cancel.on('click',function(){
               $.moveBack();
            });
        },
        getFeedInfo : function getFeedInfo(feedNumber){
            var self = this;
            $.sendHttp({
                path: SERVER_PATH.FEED_DETAIL,
                data: {
                    feedNumber: feedNumber
                },
                succ: function (data) {
                    console.log(data);
                    $(self.els.$feedTitle).val(data.feedTitle);
                    $(self.els.$feedContent).html(data.feedContent);
                },
                error : function(data){
                    console.log(data);
                }
            });
            $(self.els.$submit).html('수정하기');
        },
        update : function update(){
            var self = this;
            $.sendHttp({
                path : SERVER_PATH.FEED_UPDATE,
                data : {
                    feedNumber : self.data.feedNumber,
                    feedTitle : self.els.$feedTitle.val().trim(),
                    feedContent : self.els.$feedContent.val().trim()
                },
                succ : function(data){
                    console.log(data);
                    $.moveBack();
                },
                error : function(data){
                    console.log(data);
                }
            });
        },
        write : function write(){
            var self = this;
            $.sendHttp({
                path : SERVER_PATH.FEED_REGIST,
                data : {
                    feedTitle : self.els.$feedTitle.val().trim(),
                    feedContent : self.els.$feedContent.val().trim()
                },
                succ : function(data){
                    console.log(data);
                    $.moveBack();
                },
                error : function(data){
                    console.log(data);
                }
            });
        },
        updateWithFile : function updateWithFile(){
            var self = this;
            var imagePath = self.data.filePath;
            var feedNumber = self.data.feedNumber;
            var feedTitle = self.els.$feedTitle.val().trim();
            var feedContent = self.els.$feedContent.val().trim();
            var body = [
                {name : "image", content :imagePath , type : "FILE"},
                {name : "feedNumber", content : feedNumber, type : "TEXT"},
                {name : "feedTitle", content : feedTitle, type : "TEXT"},
                {name : "feedContent", content : feedContent, type : "TEXT"}
            ];
            $.fileHttpSend({
                path : SERVER_PATH.FEED_UPDATE_WITH_IMAGE,
                body : body,
                succ : function(data){
                    console.log(data);
                    $.moveBack();
                },
                error : function(status,data){
                    console.log(status + JSON.stringify(data));
                },
            });
        },
        writeWithFile : function writeWithFile(){
            var self = this;
            var imagePath = self.data.filePath;
            var feedNumber = self.data.feedNumber;
            var feedTitle = self.els.$feedTitle.val().trim();
            var feedContent = self.els.$feedContent.val().trim();
            var body = [
                {name : "image", content :imagePath , type : "FILE"},
                {name : "feedNumber", content : feedNumber, type : "TEXT"},
                {name : "feedTitle", content : feedTitle, type : "TEXT"},
                {name : "feedContent", content : feedContent, type : "TEXT"}
            ];
            $.fileHttpSend({
                path : SERVER_PATH.FEED_WRITE_WITH_IMAGE,
                body : body,
                succ : function(data){
                    console.log(data);
                    $.moveBack();
                },
                error : function(status,data){
                    console.log(status + JSON.stringify(data));
                },
            });
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

})(jQuery, M, __page__, window);