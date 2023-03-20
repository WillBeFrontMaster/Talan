/**
 * @file :
 * @author :
 * @date :
 */

(function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $approval : null,
            $reject : null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.data.reportNumber = M.data.param('reportNumber');
            console.log(self.data.reportNumber);
            //
            self.els.$approval = $("#approval");
            self.els.$reject = $("#reject");
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            $.sendHttp({
                path : SERVER_PATH.REPORT_DETAIL,
                data : {
                    reportNumber : self.data.reportNumber
                },
                succ : function(data){
                    $("#reportTarget").val(data.reportTarget);
                    $("#reportCode").html(data.reportCode);
                    $("#content").html(data.reportContent);
                }
            })
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$approval.on('click',function(){
                console.log(self.data.reportNumber);
                var reportTarget = $("#reportTarget").val().trim();
                console.log(reportTarget);
                swal.fire({
                    title: '사용자를 추방하시겠습니까?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '추방하기',
                    cancelButtonText: '취소'
                }).then((result) =>{
                    if (result.value === true) { // 삭제하기
                        $.sendHttp({
                            path : SERVER_PATH.REPORT_PROCESS,
                            data : {
                                reportNumber : self.data.reportNumber,
                                reportStatus : "1",
                                reportTarget : reportTarget
                            },
                            succ : function(data){
                                swal.fire('사용자가 추방되었습니다.').then((result)=>{
                                    $.moveBack();
                                });
                            }
                        });
                    }
                    });
            });
            self.els.$reject.on('click',function(){
                var reportTarget = $("#reportTarget").val().trim();
                swal.fire({
                    title: '신고를 보류하시겠습니까?',
                    icon: 'info',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '보류하기',
                    cancelButtonText: '취소'
                }).then((result) =>{
                    if (result.value === true) { // 삭제하기
                        $.sendHttp({
                            path : SERVER_PATH.REPORT_PROCESS,
                            param : {
                                reportNumber : self.data.reportNumber,
                                reportStatus : "-1",
                                reportTarget : reportTarget
                            },
                            succ : function(data){
                                swal.fire('신고를 보류하였습니다.').then((result)=>{
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