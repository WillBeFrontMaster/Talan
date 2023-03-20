/**
 * @file : requestWrite.js
 * @author : ParkDoYoung
 * @date : 22.4.8
 */

// 페이지 단위 모듈
(function ($, CONFIG, window) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var SERVER_PATH = CONFIG.SERVER_PATH;
    var page = {
        els: {
            $category: null,
            $operationDate: null,
            $subject: null,
            $content: null,
            $preferentialTreatment: null,
            $workIntensity: null,
            $city: null,
            $country: null,
            $write: null,
            $cancel: null,
        },
        data: {},
        init: function init() {
            var self = this;
            self.els.$category = $('#category');
            self.els.$operationDate = $('#operation-date');
            self.els.$subject = $('#subject');
            self.els.$content = $('#content');
            self.els.$preferentialTreatment = $('#preferential-treatment');
            self.els.$workIntensity = $('#work-intensity');
            self.els.$city = $('#city');
            self.els.$country = $('#country');
            self.els.$write = $('#write');
            self.els.$cancel = $('#cancel');
        },
        initView: function initView() {
            // 화면에서 세팅할 동적데이터
            var self = this;
            self.setCityAndCountryData();
            document.getElementById('operation-date').value = new Date().toISOString().substring(0, 10);

        },
        initEvent: function initEvent() {
            // Dom Event 바인딩
            var self = this;
            $(self.els.$write).on('click',function(){
                var category = self.els.$category.val().trim();
                var operationDate = self.els.$operationDate.val().trim();
                var subject = self.els.$subject.val().trim();
                var content = self.els.$content.val().trim();
                var preferentialTreatment = self.els.$preferentialTreatment.val().trim();
                var city = self.els.$city.val();
                var country = self.els.$country.val();
                var workIntensity = self.els.$workIntensity.val();
                var date = operationDate.substr(0,4) + operationDate.substr(5,2) + operationDate.substr(8,2);

                if (category === '선택'){return alert('카테고리를 선택해주세요');}
                if (operationDate === '') {return alert(('날자를 선택해주세요'));}
                if (subject === ''){return alert('제목을 입력해주세요');}
                if (content===''){return alert('내용을 입력해주세요');}
                // if (preferentialTreatment === ''){return alert('우대사항을 입력해주세요');}  필수 아님
                if (city === '시도' ){return alert('시/도를 선택해주세요');}
                if (country === '시/군/구 선택'){return alert('시/군/구를 정확하게 선택해주세요');}
                $.sendHttp({
                    path : SERVER_PATH.REQUEST_WRITE,
                    data : {
                        category : category,
                        requestDate : date,
                        requestTitle : subject,
                        requestContent: content,
                        preference : preferentialTreatment,
                        town : city,
                        district : country,
                        taskLevel : workIntensity
                    },
                    succ : function(data){
                        console.log(data);
                        $.movePage({
                           url : "/www/html/pro/requestList.html",
                           action: "CLEAR_TOP",
                            param : {
                               category : category
                            }
                        });
                    },
                    error : function(){
                        alert('error');
                    }
                })
            });
            $(self.els.$cancel).on('click',function(){
               $.moveBack();
            });
        },

        setCityAndCountryData: function setCityAndCountryData(){
            // city 선택에 따른 country 데이터 셋팅
            var area0 = ["서울특별시", "인천광역시", "대전광역시", "광주광역시", "대구광역시", "울산광역시", "부산광역시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주도"];
            var area1 = ["시/군/구 선택", "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"];
            var area2 = ["시/군/구 선택", "계양구", "남구", "남동구", "동구", "부평구", "서구", "연수구", "중구", "강화군", "옹진군"];
            var area3 = ["시/군/구 선택", "대덕구", "동구", "서구", "유성구", "중구"];
            var area4 = ["시/군/구 선택", "광산구", "남구", "동구", "북구", "서구"];
            var area5 = ["시/군/구 선택", "남구", "달서구", "동구", "북구", "서구", "수성구", "중구", "달성군"];
            var area6 = ["시/군/구 선택", "남구", "동구", "북구", "중구", "울주군"];
            var area7 = ["시/군/구 선택", "강서구", "금정구", "남구", "동구", "동래구", "부산진구", "북구", "사상구", "사하구", "서구", "수영구", "연제구", "영도구", "중구", "해운대구", "기장군"];
            var area8 = ["시/군/구 선택", "고양시", "과천시", "광명시", "광주시", "구리시", "군포시", "김포시", "남양주시", "동두천시", "부천시", "성남시", "수원시", "시흥시", "안산시", "안성시", "안양시", "양주시", "오산시", "용인시", "의왕시", "의정부시", "이천시", "파주시", "평택시", "포천시", "하남시", "화성시", "가평군", "양평군", "여주군", "연천군"];
            var area9 = ["시/군/구 선택", "강릉시", "동해시", "삼척시", "속초시", "원주시", "춘천시", "태백시", "고성군", "양구군", "양양군", "영월군", "인제군", "정선군", "철원군", "평창군", "홍천군", "화천군", "횡성군"];
            var area10 = ["시/군/구 선택", "제천시", "청주시", "충주시", "괴산군", "단양군", "보은군", "영동군", "옥천군", "음성군", "증평군", "진천군", "청원군"];
            var area11 = ["시/군/구 선택", "계룡시", "공주시", "논산시", "보령시", "서산시", "아산시", "천안시", "금산군", "당진군", "부여군", "서천군", "연기군", "예산군", "청양군", "태안군", "홍성군"];
            var area12 = ["시/군/구 선택", "군산시", "김제시", "남원시", "익산시", "전주시", "정읍시", "고창군", "무주군", "부안군", "순창군", "완주군", "임실군", "장수군", "진안군"];
            var area13 = ["시/군/구 선택", "광양시", "나주시", "목포시", "순천시", "여수시", "강진군", "고흥군", "곡성군", "구례군", "담양군", "무안군", "보성군", "신안군", "영광군", "영암군", "완도군", "장성군", "장흥군", "진도군", "함평군", "해남군", "화순군"];
            var area14 = ["시/군/구 선택", "경산시", "경주시", "구미시", "김천시", "문경시", "상주시", "안동시", "영주시", "영천시", "포항시", "고령군", "군위군", "봉화군", "성주군", "영덕군", "영양군", "예천군", "울릉군", "울진군", "의성군", "청도군", "청송군", "칠곡군"];
            var area15 = ["시/군/구 선택", "거제시", "김해시", "마산시", "밀양시", "사천시", "양산시", "진주시", "진해시", "창원시", "통영시", "거창군", "고성군", "남해군", "산청군", "의령군", "창녕군", "하동군", "함안군", "함양군", "합천군"];
            var area16 = ["시/군/구 선택", "서귀포시", "제주시", "남제주군", "북제주군"];

            var self = this;
            $(self.els.$city).each(function () {
                $selectedCity = $(this);
                $.each(eval(area0), function () {
                    $selectedCity.append("<option value='" + this + "'>" + this + "</option>");
                });
                $selectedCity.next().append("<option value=''>시/군/구 선택</option>");
            });
            $(self.els.$city).change(function () {
                var area = "area" + $("option", $(this)).index($("option:selected", $(this)));
                var $selectedCountry = $(this).next(); // 선택영역 군구 객체
                $("option", $selectedCountry).remove(); // 구군 초기화

                if (area == "area0")
                    $selectedCountry.append("<option value=''>구/군 선택</option>");
                else {
                    $.each(eval(area), function () {
                        $selectedCountry.append("<option value='" + this + "'>" + this + "</option>");
                    });
                }
            });
        }
    };
    window.__page__ = page;
})(jQuery, __config__, window);

// 해당 페이지에서 실제 호출
(function ($, M, pageFunc, window) {

    // 화면에 리소스가 로딩을 끝내고 정상적으로 동작할 수 있는 시점에 대한 콜백
    // window.onload 와 비슷함.
    M.onReady(function () {
        pageFunc.init(); // 최초 화면 초기화
        pageFunc.initView();
        pageFunc.initEvent();
    });

})(jQuery, M, __page__, window);