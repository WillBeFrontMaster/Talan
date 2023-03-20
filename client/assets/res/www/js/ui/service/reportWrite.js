/**
 * @file : reportWrite.js
 * @author : ParkDoYoung
 * @date : 22.4.15
 */

(function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $reportPeople: null,
            $id: null,
            $chk1: null,
            $chk2: null,
            $chk3: null,
            $chk4: null,
            $chk5: null,
            $content: null,
            $submit: null,
            $cancel: null
        },
        data: {},
        init: function init() {
            var self = this;
            // param
            self.data.reportid = M.data.param('id');
            //
            self.els.$id = $("#report-people");
            self.els.$chk1 = $("#chk1");
            self.els.$chk2 = $("#chk2");
            self.els.$chk3 = $("#chk3");
            self.els.$chk4 = $("#chk4");
            self.els.$chk5 = $("#chk5");
            self.els.$content = $("#content");
            self.els.$submit = $("#submit");
            self.els.$cancel = $("#cancel");
            self.els.$reportPeople = $('#password');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            self.els.$reportPeople.val(self.data.reportid);
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$submit.on('click', function () {
                // 입력값 검증
                var reportid = self.data.reportid;
                var chk1 = "";
                if (self.els.$chk1.is(":checked") === true) {
                    chk1 = self.els.$chk1.val().trim();
                }
                var chk2 = "";
                if (self.els.$chk2.is(":checked") === true) {
                    chk2 = self.els.$chk2.val().trim();
                }
                var chk3 = "";
                if (self.els.$chk3.is(":checked") === true) {
                    chk3 = self.els.$chk3.val().trim();
                }
                var chk4 = "";
                if (self.els.$chk4.is(":checked") === true) {
                    chk4 = self.els.$chk4.val().trim();
                }
                var chk5 = "";
                if (self.els.$chk5.is(":checked") === true) {
                    chk5 = self.els.$chk5.val().trim();
                }
                var reportCode = chk1 + chk2 + chk3 + chk4 + chk5;
                if ($.isEmpty(reportCode)){return swal.fire('한가지는 꼭 선택하셔야합니다','','error');}
                console.log(reportCode);
                var content = self.els.$content.val().trim();
                if ($.isEmpty(content)){return swal.fire('내용을 입력하세요','필수 입력사항입니다.','error');}
                swal.fire({
                    title: '신고 후에는 철회 할 수 없습니다',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '확인',
                    cancelButtonText: '취소'
                }).then((result) =>{
                    console.log(result);
                    if (result.value === true){
                        $.sendHttp({
                            path: SERVER_PATH.REPORT_REGIST,
                            data: {
                                reportTarget: reportid,
                                reportContent: content,
                                reportCode: reportCode
                            },
                            succ : function(data){
                                swal.fire('신고가 정상적으로 처리 도었습니다.').then((result) => {
                                        $.moveBack();
                                    });
                            }
                        });
                    }
                });

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

})(jQuery, M, __page__, window);