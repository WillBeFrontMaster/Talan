/**
 * @file : receivedEstimateList.js
 * @author : suhyun
 * @date : 2022.04.17
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
            $requestStatus: null,
            $requestContent: null,
            $requestDate: null,
            $myRequest: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$requestStatus = $('#request-status');
            self.els.$requestContent = $('#request-content');
            self.els.$requestDate = $('#request-date');
            self.els.$myRequest = $('#my-request');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            var status = '';
            var requestNumber = M.data.param("requestNumber");
            console.log(requestNumber);
            //위에 정보 띄울 거
            $.sendHttp({
                path: SERVER_PATH.REQUEST_DETAIL,
                data:{
                    requestNumber: requestNumber
                },
                succ: function(data){
                    console.log(data);
                    if(data.requestStatus == 1){
                        status = '요청 마감';
                    }
                    else{
                        status = '요청 진행중';
                    }
                    self.els.$requestStatus.text(status);
                    self.els.$requestContent.text(data.requestTitle);
                    self.els.$requestDate.text(data.requestDate);
                }

            })
            //밑에 목록 띄울 거
            $.sendHttp({
                path: SERVER_PATH.ESTIMATE_LIST,
                data:{
                    requestNumber: M.data.param("requestNumber")
                },
                succ: function(data){
                    console.log(data);
                    if(data.list.length == 0){
                        $('.no-received-quotes').css("display", "block");
                    }
                    else{
                        for(var i = 0; i < data.list.length; i++){
                        //사진 고수닉네임 친절점수 리뷰갯수 견적서이름
                            self.showEstiamteList(data, i);
                        }
                    }
                },
                error: function(data, status){
                    swal("견적서 리스트를 불러오는 데 실패하였습니다.","","error");
                }
            })
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$myRequest.on('click', function(){
                $.movePage({
                    url:"/www/html/people/requestDetail.html",
                    param:{
                        requestNumber: self.els.$requestNumber
                    }
                });
            });
            $(".container").on('click', 'div.request-profile', function(){
                $.movePage({
                    url:"/www/html/pro/estimateDetail.html",
                    param:{
                        estimateNumber: $(this).attr('id')
                    }
                })
            })
        },

        showEstiamteList: function showEstiamteList(data, i){
            $(".container").append(HTML.ESTIMATE_MYLIST);
            $(".request-profile:eq("+i+")").attr('id', data.list[i].estimateNumber);
            $(".img:eq("+i+")").html("<img src=\""+$.imagePath(data.list[i].imagePath,data.list[i].storeImageName,null,null) +"\"/>");
            $(".profile-name:eq("+i+")").html(data.list[i].nickname);
            $(".estimate-title:eq("+i+")").append(data.list[i].estimateTitle);
            $(".info:eq("+i+")").append($.setStar(data.list[i].kindScore, data.list[i].reviewCount)+data.list[i].estimateTitle);
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