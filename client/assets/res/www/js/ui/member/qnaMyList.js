/**
 * @file : qnaMyList.js
 * @author : ParkDoYoung
 * @date : 22.4.19
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

        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$list = $('#list');

        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            $.sendHttp({
                path: SERVER_PATH.QNA_MY_LIST,
                data:{},
                succ: function(data){
                    console.log(data);
                    if(data.list.length === 0){
                        $(".no-received-quotes").css("display","block");
                    }else{
                        for (var i = 0; i < data.list.length; i++){
                            $(self.els.$list).append(HTML.QNA_ITEM);
                            // 게시글 번호 -> li 에 id 값으로 지정
                            $("li:eq("+i+")").attr('id',data.list[i].inquiryNumber);
                            // 게시글 답변 여부
                            if (data.list[i].responseStatus ==="0"){ // 답변 x
                                $(".item-box-l:eq("+i+")").html("답변 전");
                            }else {
                                $(".item-box-l:eq("+i+")").html("답변 완료");
                            }
                            var title = data.list[i].inquiryTitle;
                            // 일시
                            if (data.list[i].inquiryRegisterDate === data.list[i].inquiryModifyDate){ // 수정x
                                var regidate = (data.list[i].inquiryRegisterDate).substr(0,10);
                                $(".item-box-r:eq("+i+")").html(regidate);
                            }else { // 수정됨.
                                var modi = (data.list[i].inquiryModifyDate).substr(0,10);
                                $(".item-box-r:eq("+i+")").html(modi);
                                title +="(수정됨)";
                            }
                            // 비밀글 여부
                            if (data.list[i].SecretStatus === "0"){ // 비밀 글 아닐때
                                // 제목
                                $(".item-box-title:eq("+i+")").html(title);
                                // 내용
                                $(".item-box-content:eq("+i+")").html(data.list[i].inquiryContent);
                            }else { // 비밀 글 일때
                                $(".item-box-title:eq("+i+")").html("비밀글입니다.");
                                $(".item-box-content:eq("+i+")").html("비밀글입니다.");
                            }
                        }
                    }
                },
                error: function(data, status){
                    alert("리스트를 불러오는 데 실패하였습니다.");
                }
            })
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;

            $(self.els.$list).on('click','li',function(){
                var inquiryNumber = $(this).attr('id');
                console.log(inquiryNumber);
                $.movePage({
                    url : "/www/html/member/qnaDetail.html",
                    param : {
                        inquiryNumber : inquiryNumber,
                    }
                });
            });
            $(self.els.$writeBtn).on('click',function(){
                $.movePage({
                    url : "/www/html/member/qnaWrite.html"
                });
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