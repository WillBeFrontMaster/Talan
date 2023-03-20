/**
 * @file : proRegist.js
 * @author : suhyun
 * @date : 2022.04.14
 */

 (function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $category: null,
            $registContent: null,
            $experiencePeriod: null,
            $license: null,
            $selectedLicense: null,
            $searchBtn: null,
            $searchResult: null,
            $registBtn: null,
            $selectBtn: null,

        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$category = $('#category');
            self.els.$registContent = $('#regist-content');
            self.els.$experiencePeriod = $('#experience-period');
            self.els.$license = $('#license');
            self.els.$licenseList = $('#license-list')
            self.els.$selectedLicense = $('#selected-license');
            self.els.$searchBtn = $('#search-btn');
            self.els.$searchResult = $('#search-result');
            self.els.$registBtn = $('#regist-btn');
            self.els.$selectBtn = $('.select-btn');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            $('#search-result').css("display", "none");
        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            self.els.$searchBtn.on('click', function(){
                self.searchLicenses();
                $("#search-result").css("display", "block");
            });
            $("#license-list").on('click','tr', function(){
               $(self.els.$selectedLicense).val($(this).children("td.license-name").text());
            });
            self.els.$registBtn.on('click', function(){
                var category = self.els.$category.val();
                var registContent = self.els.$registContent.val();
                var experiencePeriod = self.els.$experiencePeriod.val();
                var license = self.els.$selectedLicense.val().trim();
                
                if($.isEmpty(category)){return alert("카테고리를 선택하세요.");}
                if($.isEmpty(registContent)){return alert("세부 내용을 입력하세요.");}
                if($.isEmpty(experiencePeriod)){return alert("기간을 입력하세요.");}
                
                $.sendHttp({
                    path: SERVER_PATH.PRO_REGIST,
                    data:{
                        category: category,
                        license: license,
                        registContent: registContent,
                        experiencePeriod: experiencePeriod
                    },
                    succ: function(data){
                        var _LOGIN_INFO = M.data.global("LOGIN_INFO");
                        _LOGIN_INFO.auth = true;
                        M.data.global("LOGIN_INFO", _LOGIN_INFO);
                        console.log(M.data.global("LOGIN_INFO"));
                        M.data.global({
                            "PRO_STATUS":{
                                proId: _LOGIN_INFO.peopleId,
                                proStatus: true,
                            }
                        });
                        swal.fire({title : 'pro 등록이 완료되었습니다!',icon : 'success'}).then((result)=>{
                            M.page.html({
                                url:"/www/html/member/mypage.html",
                                action:"CLEAR_TOP"
                            });
                        });
                        console.log(M.data.global("PRO_STATUS"));
                    },
                    error: function(data, status){
                        alert("error");
                    }
                })
            });
        },
        
        searchLicenses: function(){
            var self = this;
            var license = self.els.$license.val();
            if(license.length < 2){
                alert("검색어를 2자 이상 입력하세요.");
                $("#search-result").css("display", "none");
            }
            else{
                $.sendHttp({
                    path: SERVER_PATH.SEARCH_LICENSE,
                    data:{
                        searchLicense : license
                    },
                    succ: function(data){
                        console.log(data.licenseList.length);
                        if(data.licenseList.length == 0){
                            alert("검색 결과가 없습니다.");
                            $("#search-result").css("display", "none");
                        }
                        else{
                            for(var i = 0; i < data.licenseList.length; i++){
                                $(".search-license").append("<tr><td>"+data.licenseList[i].licenseType+"</td><td class='license-name'>"+data.licenseList[i].licenseName+"</td></tr>");
                            }
                        }
                    }   
                });
            }

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