/**
 * @file : mypage.js
 * @author : suhyun
 * @date : 2022.04.11
 */

(function ($, M, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var proStatus = M.data.global("PRO_STATUS.proStatus");
    var HTML = CONFIG.HTML;
    var page = {
        els: {
            $nickname: null,
            $intro: null,
            $profileImgBtn: null,
            $modifyInfo: null,
            $modifyIntro: null,
            $paymentInfo: null,
            $latestPayment: null,
            $latestRequest: null,
            $latestEstimate:null,
            $latestReviewWrite: null,
            $latestReviewReceive: null,
            $latestInquiry: null,
            $proRegist: null,
            $goPro: null,
            $goPeople: null,
            $feedWriteBtn: null,

            $paymentList: null,
            $requestList: null,
            $estimateList: null,
            $reviewList: null,
            $inquiryList: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$nickname = $('#nickname');
            self.els.$intro = $('#intro');
            self.els.$profileImgBtn = $('#profile-img-btn');
            self.els.$modifyInfo = $('#modify-info');
            self.els.$modifyIntro = $('#modify-intro');
            self.els.$paymentInfo = $('#payment-info');
            self.els.$latestPaymentPeople = $('#latest-payment-people');
            self.els.$latestPaymentPro = $('#latest-payment-pro');
            self.els.$latestRequest = $('#latest-request');
            self.els.$latestEstimate = $('#latest-estimate');
            self.els.$latestReviewWrite = $('#latest-review-write');
            self.els.$latestReviewReceive = $('#latest-review-receive');
            self.els.$latestInquiry = $('#latest-inquiry');
            self.els.$proRegist = $('#pro-register');
            self.els.$goPro = $('#go-pro');
            self.els.$goPeople = $('#go-people');
            self.els.$feedWriteBtn = $('#pro-mypage3');

            self.els.$paymentListPeople = $('#latest-payment-people');
            self.els.$paymentListPro = $('#latest-payment-pro');
            self.els.$requestList = $('#request-list');
            self.els.$estimateList = $('#estimate-list');
            self.els.$reviewList = $('#review-list');
            self.els.$inquiryList = $('#inquiry-list');

            self.data.loginInfo = M.data.global("LOGIN_INFO");
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            var peopleId = M.data.global("LOGIN_INFO.peopleId");
            var nickname = M.data.global("LOGIN_INFO.nickname");
            var auth = M.data.global("LOGIN_INFO.auth");
            $.sendHttp({
                path: SERVER_PATH.INFO,
                data: {
                    peopleId: peopleId
                },
                succ: function (data) {
                    self.els.$nickname.text(nickname);
                    self.els.$intro.text(data.intro);
                    document.getElementById("profile-img-btn").src=($.imagePath(data.imagePath, data.storeImageName, null, null));
                    if (auth) {
                        //pro인증이 된 회원
                        $('#pro-register').css("display", "none");
                        self.proOn();
                    } else {
                        //pro인증이 되지 않은 회원. pro가입하기 띄움'
                        $("#pro-register").css("display", "block");
                        self.peopleOn();
                        $("#people-mypage").css("display", "none");
                        $("#pro-mypage").css("display", "none");
                    }
                },
                error: function () {
                    alert("ㅇㅔㄹㅓ")
                }
            });
            $.sendHttp({
                path : SERVER_PATH.MY_PAGE,
                succ : function(data){
                    $("#latest-payment-pro").html(data.paymentPro);
                    $("#latest-payment-people").html(data.paymentPeople);
                    $("#latest-request").html(data.request);
                    $("#latest-estimate").html(data.estimate)
                    $("#latest-review").html(data.review);
                    $("#latest-inquiry").html(data.inquiry);
                    $("#latest-review-receive").html(data.reviewPro);
                }
            });

            $.sendHttp({
                path : SERVER_PATH.FEED_LIST_BY_WRITER,
                data : {
                    proId : self.data.loginInfo.peopleId
                },
                succ : function(data){
                    if (data.list.length > 0){
                        $("#list").html(" ");
                        for (var i = 0; i < data.list.length; i++) {
                            self.addFeedList(data.list[i], i);
                        }
                    }else{
                        self.noFeedList();
                    }
                }
            })
        },
        initEvent: function initEvent() {
            var self = this;
            self.els.$paymentListPeople.on('click',function(){
                $.movePage({
                    url:"/www/html/people/paymentList.html",
                    param : {
                        auth : 'people'
                    }
                });
            });
            self.els.$paymentListPro.on('click',function(){
                $.movePage({
                    url:"/www/html/people/paymentList.html",
                    param : {
                        auth : 'pro'
                    }
                });
            });

            self.els.$requestList.on('click', function(){
                $.movePage({
                    url:"/www/html/people/requestMyList.html"
                });
            });
            self.els.$estimateList.on('click', function(){
                $.movePage({
                    url:"/www/html/pro/estimateMyList.html"
                })
            });
            self.els.$latestReviewWrite.on('click', function(){
                $.movePage({
                    url:"/www/html/people/reviewMyList.html"
                });
            });
            self.els.$latestReviewReceive.on('click', function(){
                $.movePage({
                    url:"/www/html/pro/reviewList.html"
                });
            });
            self.els.$inquiryList.on('click', function(){
                $.movePage({
                    url:"/www/html/member/qnaMyList.html"
                });
            });
            self.els.$goPro.on('click', function () {
                self.goPro();
            });
            self.els.$goPeople.on('click', function(){
                self.goPeople();
            });
            self.els.$modifyInfo.on('click', function(){
                M.page.html("./viewInfo.html");
            })
            self.els.$modifyIntro.on('click', function(){
                var intro = '';
                swal("수정할 소개를 입력하세요", {
                    content: "input",
                  })
                  .then((value) => {
                    intro = value;
                    $.sendHttp({
                        path: SERVER_PATH.UPDATE_INTRO,
                        data: {
                            intro: intro
                        },
                        succ: function(){
                            swal(`소개가 수정되었습니다.`,'','success');
                            console.log(intro);
                            self.initView();
                        },
                        error: function(){
                            alert("소개 수정 오류");
                        }
                    });
                  });
                
            });
            self.els.$proRegist.on('click', function () {
                M.page.html("../pro/proRegist.html");
            });
            self.els.$feedWriteBtn.on('click', function(){
                M.page.html("../pro/feedWrite.html")
            });
            self.els.$profileImgBtn.on('click', function(){
                self.updateImage();
            });
            
        },
        goPro: function goPro(){
            var self = this;
            self.proOn();
        },
        goPeople: function goPeople(){
            var self = this;
            self.peopleOn();
        },
        proOn: function proOn() {
            //people -> pro
            $("#pro-mypage1").css("display", "block");
            $("#pro-mypage2").css("display", "block");
            $("#pro-mypage3").css("display", "block");
            $("#pro-mypage4").css("display", "block");
            $("#pro-mypage5").css("display", "block");
            $("#people-mypage1").css("display", "none");
            $("#people-mypage2").css("display", "none");
            $("#people-mypage3").css("display", "none");
            $("#people-mypage4").css("display", "none");
            $("#people-mypage5").css("display", "none");
            M.data.removeGlobal("PRO_STATUS.proStatus");
            console.log(M.data.global("PRO_STATUS.proStatus"));
            M.data.global("PRO_STATUS.proStatus", true);
            //people로 전환하는 버튼
            $("#people-mypage").css("display", "block");
            $("#pro-mypage").css("display", "none");
            console.log(M.data.global("PRO_STATUS.proStatus"));

        },
        peopleOn: function peopleOn(){
            //pro -> people
            $("#pro-mypage1").css("display","none");
            $("#pro-mypage2").css("display","none");
            $("#pro-mypage3").css("display", "none");
            $("#pro-mypage4").css("display", "none");
            $("#pro-mypage5").css("display", "none");
            $("#people-mypage1").css("display", "block");
            $("#people-mypage2").css("display", "block");
            $("#people-mypage3").css("display", "block");
            $("#people-mypage4").css("display", "block");
            $("#people-mypage5").css("display", "block");
            //pro로 전환
            $("#pro-mypage").css("display", "block");
            $("#people-mypage").css("display","none");
            M.data.removeGlobal("PRO_STATUS.proStatus");
            console.log(M.data.global("PRO_STATUS.proStatus"));
            M.data.global("PRO_STATUS.proStatus", false);
            console.log(M.data.global("PRO_STATUS.proStatus"));

        },
        updateImage: function updateImage(){
            //프로필 이미지 수정
        },
        addFeedList: function addFeedList(feedData, idx) {
            $("#list").append(HTML.FEED_CONTENT);
            $("strong.ellipsis_1:eq(" + idx + ")").html(feedData.feedTitle);
            $("p.ellipsis_1:eq(" + idx + ")").html(feedData.feedContent);
            $("#list>li:eq(" + idx + ")").attr('id', feedData.feedNumber);
            $(".feed-image:eq(" + idx + ")").attr('src',$.imagePath(feedData.filePath,feedData.storeFileName,null,null));
        },
        noFeedList: function noFeedList(){
            var self = this;
            $(self.els.$list).hide();
            $(self.els.$list).after(HTML.NO_LIST);
        },
        
    };
    window.__page__ = page;
})(jQuery, M, __config__, window);

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