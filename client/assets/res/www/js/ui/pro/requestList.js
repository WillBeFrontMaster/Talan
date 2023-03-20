/**
 * @file : requestList.js
 * @author : shyun
 * @date : 2022.04.15
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
            $category: null,
            $requestList: null,
            $writeBtn: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$writeBtn = $('#write-btn');
            self.els.$requestList = $('#request-list');
            self.els.$category = $('#category');
            //
            self.data.category = M.data.param('category');
        },
        initView: function initView() {
            var self = this;
            $('#category').val(self.data.category);
            self.changeCategory(M.data.param("category"));
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            $('#category').on('change', function(){
                var target = $('#category option:selected').val();
                console.log(target);
                self.changeCategory(target);
            });
            self.els.$requestList.on('click', 'li', function(){
                var requestNumber = $(this).attr('id');
                $.movePage({
                    url: "/www/html/people/requestDetail.html",
                    param:{
                        requestNumber : requestNumber
                    }
                });
            })
            self.els.$writeBtn.on('click', function(){
                $.movePage({
                    url: "/www/html/people/requestWrite.html"
                });
            })
        },
        changeCategory: function changeCategory(target){
            var self = this;
            $.sendHttp({
                path: SERVER_PATH.REQUEST_LIST,
                data:{
                    town: "전체",
                    district: "전체",
                    category: target,
                    lastRequestNumber: "0",
                    cnt: "0"
                },
                succ: function(data){
                    console.log(data);
                    $("#request-list").html(" ");
                    for(var i = 0; i < data.list.length; i++){
                        self.showRequestList(data, i);
                    }
                },
                error: function(status, data){
                    swal("요청서 리스트를 불러오는 데 실패하였습니다","","error");
                }
            });
        },
        showRequestList: function showRequestList(data, i){
            $("#request-list").append(HTML.REQUEST_LIST);
            $("li.div-card:eq("+i+")").attr('id', data.list[i].requestNumber);
            $("h3.card-title:eq("+i+")").html(data.list[i].requestTitle);
            $("p.card-day:eq("+i+")").html(data.list[i].requestDate);
            $("span.people-id:eq("+i+")").html(data.list[i].nickname);
            $("p.request-content:eq("+i+")").html(data.list[i].requestContent);
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