/**
 * @file : requestDetail.js
 * @author : suhyun
 * @date : 2022.04.15
 */

 (function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $peopleId: null,
            $category: null,
            $date: null,
            $title: null,
            $content: null,
            $preference: null,
            $address: null,
            $workIntense: null,
            $writeBtn: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$peopleId = $('#people-id');
            self.els.$category = $('#category');
            self.els.$date = $('#date');
            self.els.$title = $('#title');
            self.els.$content = $('#content');
            self.els.$preference = $('#preference');
            self.els.$address = $('#address');
            self.els.$workIntense = $('#work-intense');
            self.els.$writeBtn = $('#write-btn');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            var requestNumber = M.data.param("requestNumber");
            $.sendHttp({
                path: SERVER_PATH.REQUEST_DETAIL,
                data: {
                    requestNumber : requestNumber
                },
                succ: function(data){
                    console.log(data.category);
                    self.els.$peopleId.text(data.nickname);
                    self.els.$category.val(data.category);
                    self.els.$date.val(data.requestRegisterDate);
                    self.els.$title.val(data.requestTitle);
                    self.els.$content.text(data.requestContent);
                    self.els.$preference.val(data.preference);
                    self.els.$address.val(data.town+" "+data.district);
                    self.els.$workIntense.val(data.taskLevel);
                    self.estimateRight(data.category);
                },
                error: function(status, data){
                    swal("견적서 상세내용을 불러오는 데 실패하였습니다","","warning");
                }
            });
            
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            var requestNumber = M.data.param("requestNumber");
            self.els.$writeBtn.on('click', function(){
                $.movePage({
                    url: "/www/html/pro/estimateWrite.html",
                    param:{
                        requestNumber : requestNumber
                    }
                })
            });
        },
        estimateRight: function estimateRight(category){
            var proCategory = '';
            var loginInfo = M.data.global("LOGIN_INFO");
            console.log(loginInfo)
            console.log(loginInfo.auth);
            if(loginInfo.auth){
                var proStatus = M.data.global("PRO_STATUS");
                console.log(proStatus);
                console.log(proStatus.proId);
                console.log(proStatus.proStatus);

                $.sendHttp({
                    path: SERVER_PATH.PRO_INFO,
                    data:{
                        proId: proStatus.proId,
                    },
                    succ: function(data){
                        proCategory = data.category
                        if(proCategory == category){
                            $('.btn-wrap').css("display", "block");
                        }
                    },
                    error: function(data, status){
                        alert("error");
                    }
                });
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

})(jQuery, M, __page__, window);