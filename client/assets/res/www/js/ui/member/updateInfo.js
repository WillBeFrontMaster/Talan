/**
 * @file : updateInfo.js
 * @author : suhyun
 * @date : 2022.04.13
 */

(function ($, CONFIG, module, window) {
        var ENV = CONFIG.ENV;
        var MSG = CONFIG.MSG;
        var CONSTANT = CONFIG.CONSTANT;
        var SERVER_CODE = CONFIG.SERVER_CODE;
        var SERVER_PATH = CONFIG.SERVER_PATH;
        var originNickname;
        var originEmail;
        var page = {
            els: {
                $name: null,
                $nickname: null,
                $gender: null,
                $year: null,
                $month: null,
                $date: null,
                $city: null,
                $country: null,
                $peopleId: null,
                $email: null,
                $phone: null,
                $account: null,
                $updateBtn: null,
                $dupEmail: null,
                $dupNickname: null,
                $isCheckedEmail: null,
                $isCheckedNickname: null,
            },
            data: {},
            init: function init() {
                var self = this;
                self.els.$name = $('#user-name');
                self.els.$nickname = $('#nickname');
                self.els.$gender = $('#gender');
                self.els.$year = $('#year');
                self.els.$month = $('#month');
                self.els.$date = $('#date');
                self.els.$city = $('#sido');
                self.els.$country = $('#sigungu');
                self.els.$peopleId = $('#people-id');
                self.els.$email = $('#email');
                self.els.$phone = $('#cell-phone');
                self.els.$account = $('#account');
                self.els.$updateBtn = $('#update-btn');
                self.els.$dupEmail = $('#dup-btn2');
                self.els.$dupNickname = $('#dup-btn3');
                self.els.$isCheckedEmail = null;
                self.els.$isCheckedNickname = null;
            },
            initView: function initView() {
                // 화면에서 세팅할 동적데이터
                var self = this;
                var peopleId = M.data.global("LOGIN_INFO.peopleId");
                self.setCityAndCountryData();
                console.log(peopleId);
                $.sendHttp({
                    path: SERVER_PATH.INFO,
                    data: {
                        peopleId: peopleId
                    },
                    succ: function (data) {
                        var gender = '';
                        if (data.gender == 'F') gender = '여자';
                        else gender = '남자';
                        self.els.$name.val(data.name);
                        self.els.$nickname.val(data.nickname);
                        self.els.$gender.val(gender);
                        self.els.$year.val(data.birth.substr(0, 4));
                        self.els.$month.val(data.birth.substr(5, 2));
                        self.els.$date.val(data.birth.substr(8, 2));
                        self.els.$phone.val(data.phone);
                        self.els.$peopleId.val(peopleId);
                        self.els.$email.val(data.email);
                        self.els.$city.val(data.address.split('`')[0]);
                        $("#sigungu").html("<option value='" + data.address.split('`')[1] + "'>" + data.address.split('`')[1] + "</option>");
                        self.els.$account.val(data.account);

                        originNickname = data.nickname;
                        originEmail = data.email;

                    }
                });
            },
            initEvent: function initEvent() {
                // Dom Event 바인딩
                var self = this;
                self.els.$updateBtn.on('click', function () {
                    console.log(originNickname);
                    console.log(originEmail);
                    self.update();
                });
                self.els.$dupEmail.on('click', function () {
                    self.els.$isCheckedEmail = false;
                    self.duplicateEmail();
                });
                self.els.$dupNickname.on('click', function () {
                    self.els.$isCheckedNickname = false;
                    self.duplicateNickname();
                });
            },
            duplicateEmail: function () {
                var self = this;
                var email = this.els.$email.val().trim();
                if (self.checkEmail(email)) {
                    $.sendHttp({
                        path: SERVER_PATH.DUPLICATE2,
                        data: {
                            email: email
                        },
                        succ: function (data) {
                            if (data.existYn == 'Y') {
                                $.toast("중복된 이메일입니다.");
                                return false;
                            } else {
                                $.toast("사용가능한 이메일입니다.");
                                self.els.$isCheckedEmail = true;
                                return true;
                            }
                        }
                    });
                } else {
                    return $.toast("이메일 형식이 잘못되었습니다.");
                }
            },
            checkEmail: function (email) {
                var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
                if (email.length != 0) {
                    if (!reg_email.test(email)) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            },

            duplicateNickname: function () {
                var nickname = this.els.$nickname.val().trim();
                var self = this;

                if (nickname.length > 6 || nickname.length == 0) {
                    return $.toast("닉네임은 6자이하로 입력하세요.");
                } else {
                    $.sendHttp({
                        path: SERVER_PATH.DUPLICATE3,
                        data: {
                            nickname: nickname
                        },
                        succ: function (data) {
                            if (data.existYn == 'Y') {
                                $.toast("중복된 닉네임입니다.");
                                return false;
                            } else {
                                $.toast("사용가능한 닉네임입니다.");
                                self.els.$isCheckedNickname = true;
                                return true;
                            }
                        }
                    });
                }
            },
            setCityAndCountryData: function setCityAndCountryData() {
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
            },
            update: function () {
                var self = this;
                var name = self.els.$name.val().trim();
                var year = self.els.$year.val().trim();
                var month = self.els.$month.val().trim();
                var date = self.els.$date.val().trim();
                var nickname = self.els.$nickname.val().trim();
                var city = self.els.$city.val().trim();
                var country = self.els.$country.val().trim();
                var phone = self.els.$phone.val().trim();
                var email = self.els.$email.val().trim();
                var account = self.els.$account.val().trim();

                
                if($.isEmpty(name)){return swal("이름을 입력하세요.","","warning");}
                if(!module.isBirthday(year,module.digitNum(month),module.digitNum(date))){return swal("올바른 생년월일을 입력하세요.","","warning");}
                if(!module.isCellphone(phone)){return swal("올바른 연락처를 입력하세요.","","warning");}
                if($.isEmpty(email)){return swal("이메일을 입력하세요.","","warning");}
                if(email != originEmail){
                    if(!self.els.$isCheckedEmail){return swal("이메일 중복확인을 해주세요.","","warning");}
                }
                if(city === '시/도'){return swal("시도를 선택하세요.","","warning");}
                if(country === '시/군/구 선택'){return swal("시/군/구를 선택하세요.","","warning");}
                if($.isEmpty(account)){return swal("계좌번호를 입력하세요.","","warning");}
                if($.isEmpty(nickname)){return swal("닉네임을 입력하세요.","","warning");}
                if(nickname != originNickname){
                    if(!self.els.$isCheckedNickname){return swal("닉네임 중복확인을 해주세요.","","warning");}
                }
                $.sendHttp({
                    path: SERVER_PATH.UPDATE,
                    data: {
                        name: name,
                        birth: year + module.digitNum(month) + module.digitNum(date),
                        nickname: nickname,
                        address: city + "`" + country,
                        phone: phone,
                        email: email,
                        account: account
                    },
                    succ: function (data) {
                        swal("정보가 수정되었습니다.","","success")
                        .then(
                            (result)=>{
                                M.page.tab.remove("viewInfo.html");
                                M.page.html({
                                url: "./viewInfo.html",
                                actionType: "CLEAR_TOP"
                            });
                            }
                        )
                        
                    },
                    error: function (status, data) {
                        swal("수정 오류","","warning");
                    }
                });

            }
        
    }; window.__page__ = page;
})(jQuery, __config__, __util__, window);

(function ($, M, pageFunc, window) {

    M.onReady(function () {
        pageFunc.init();
        pageFunc.initView();
        pageFunc.initEvent();
    });

})(jQuery, M, __page__, window);