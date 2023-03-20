/**
 * @file : definition.js 상수 값을 모아두는 공통 JS
 * @author :
 * @date :
 */
(function (window, M) {
    var module = {};

    var IS_DEV = true; //true->OT false->MS
    var IS_PROD = !IS_DEV;


    // 앱 환경변수 값
    var ENV = module.ENV = {
        IS_DEV: IS_DEV, // 개발 모드 여부
        SERVER_NAME: IS_PROD ? "MS_SERVER" : "OT_SERVER" //바라볼 서버 이름 (Manifest.xml에 설정되어있는 이름)
        , UPLOAD_URL: IS_PROD ? "http://192.168.0.56:8888/" : "http://192.168.0.56:8888/"
        , INDICATOR: true //서버통신시 indicator 여부
    };

    //서버 전문 요청 목록
    var SERVER_PATH = module.SERVER_PATH = {

        LOGIN: "api/people/login", //로그인
        LOGOUT: "api/people/logout", //로그아웃
        DUPLICATE1: "api/people/duplicate1", //아이디 중복 체크
        DUPLICATE2: "api/people/duplicate2", //이메일 중복 체크
        DUPLICATE3: "api/people/duplicate3", //닉네임 중복 체크
        JOIN: "api/people/join", //회원가입
        JOIN_WITH_IMAGE: "api/people/joinWithImage", //회원가입
        FIND_ID: "api/people/findId", //아이디 찾기
        FIND: "api/people/find", //비밀번호 변경 전 개인정보 확인
        PASSWORD: "api/people/password", //비밀번호 변경
        OUT: "api/people/out", //회원 탈퇴
        INFO: "api/people/info", //회원 정보 조회
        UPDATE: "api/people/update", //회원 정보 수정
        UPDATE_INTRO: "api/people/updateIntro",//회원 소개 수정
        CHECK_PASSWORD: "api/people/chkPwd", //회원 비밀번호 확인
        MY_PAGE : "api/people/myPage",

        //payment
        PAYMENT: "api/payment/insert",//결제
        PAYMENT_LIST: "api/payment/myList", //결제목록
        PAYMENT_DETAIL: "api/payment/detail", //결제 상세
        PAYMENT_CANCEL: "api/payment/cancel", //결제 취소
        //Pro
        PRO_INFO:"api/pro/info", //고수 정보
        PRO_REGIST: "api/pro/regist", //고수 등록
        SEARCH_LICENSE: "api/pro/searchLicense", //자격증 검색

        //request
        REQUEST_WRITE: "api/request/regist", // request 글쓰기
        REQUEST_LIST: "api/request/listSearch", // request list
        REQUEST_DETAIL: "api/request/detail", //요청서 상세
        REQUEST_MYLIST: "api/request/myList", //내 요청서 목록
        REQUEST_CLOSED: "api/request/closed", //요청서 마감

        //estimate
        ESTIMATE_REGIST: "api/estimate/regist",//견적서 등록
        ESTIMATE_LIST: "api/estimate/list", //(내 요청서의)견적서 리스트
        ESTIMATE_DETAIL: "api/estimate/detail", //견적서 상세
        ESTIMATE_MATCHED: "api/estimate/matched", //매칭

        //messageee
        GET_MESSAGE: "api/message/info", // message info
        SET_MESSAGE: "api/message/send", // send message
        GET_MESSAGE_LIST: "api/message/list", //message list
        GET_MESSAGE_NUM : "api/message/setChatRoom",
        //feed
        FEED_REGIST: "api/feed/regist", //피드 등록
        FEED_UPDATE: "api/feed/update", //피드 업데이트
        FEED_LIST: "api/feed/list", //피드 리스트
        FEED_DETAIL: "api/feed/detail", //피드 디테일
        FEED_DELETE: "api/feed/delete", //피드 삭제
        FEED_UPDATE_WITH_IMAGE: "api/feed/updateWithImage",
        FEED_WRITE_WITH_IMAGE: "api/feed/registWithImage",
        FEED_COMMENT_REGIST: "api/feed/commentsRegist", //피드 댓글 작성
        FEED_COMMENT_DETAIL: "api/feed/commentsDetail", //피드 댓글 조회
        FEED_COMMENT_DELETE: "api/feed/commentsDelete", //피드 댓글 삭제
        FEED_LIST_BY_WRITER: "api/feed/proList", //피드 작성자별 리스트

        //estimate
        ESTIMATE_REGIST: "api/estimate/regist", //견적서 등록
        ESTIMATE_LIST: "api/estimate/list",
        ESTIMATE_MYLIST: "api/estimate/myList",
        ESTIMATE_DETAIL: "api/estimate/detail",
        ESTIMATE_MATCH: "api/estimate/matched",

        // qna
        QNA_LIST : "api/inquiry/allList", //qna 리스트 조회
        QNA_DETAIL : "api/inquiry/detail", // qna 디테일
        QNA_UPDATE : "api/inquiry/update",
        QNA_REGIST : "api/inquiry/regist",
        QNA_DELETE : "api/inquiry/delete",
        QNA_MY_LIST : "api/inquiry/personalList",

        //admin

        ADMIN_QNA_DELETE : "api/admin/inquiryDelete",
        ADMIN_QNA_RESPONSE : "api/admin/response/regist",

        //신고하기
        REPORT_REGIST : "api/report/regist",
        REPORT_LIST : "api/admin/reportList",
        REPORT_DETAIL : "api/report/detail",
        REPORT_PROCESS : "api/admin/report", //신고 처리


        REVIEW_REGIST : "api/review/regist",
        REVIEW_MY_LIST : "api/review/list",
        REVIEW_LIST : "api/review/list",
        REVIEW_DETAIL : "api/review/detail",
        REVIEW_RESPONSE : "api/review/response",
        REVIEW_ANSWER : "api/review/response",
        REVIEW_LIST_PRO : "api/review/proList",

    };

    var SERVER_CODE = module.SERVER_CODE = {
        SUCC: '0000', // 성공시
        LOGIN_REQUIRED: '1003' // 로그인 필요시
    }

    // 상수 키 값
    var CONSTANT = module.CONSTANT = {
        AUTO_LOGIN_AUTH: 'AUTO_LOGIN_AUTH'
    }

    // 메시지 문자열 상수
    var MSG = module.MSG = {
        INDICATOR_MSG: "통신중..." //서버통신시 default indicator_msg
        , DEFAULT_ERROR_MSG: "네트워크 통신 중 오류가 발생했습니다."
    };

    // html default form
    var HTML = module.HTML = {
        FEED_CONTENT : "<li>\n" +
            "                        <div class=\"thumbnail-wrap\">\n" +
            "                            <div class=\"thumbnail\">\n" +
            "                                <img src=\"../../img/pro-feed-image.png\" alt=\"\"/>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <div class=\"info-box\">\n" +
            "                            <div class=\"info-box-top\">\n" +
            "                                <strong class=\"ellipsis_1\">\n" +
            "                                    PRO FEED TITLE\n" +
            "                                </strong>\n" +
            "                            </div>\n" +
            "                            <div class=\"info-box-btm\">\n" +
            "                                <p style=\"text-align:left;\" class=\"ellipsis_1\">\n" +
            "                                    PRO FEED CONTENT\n" +
            "                                </p>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                    </li>",
        FEED_COMMENT_HTML: "<ul>\n" +
            "                    <li>\n" +
            "                        <div class=\"comment-writer-info\">\n" +
            "                            <div class=\"comment-writer\">" +
            // "                               김people" +
            "                               </div>\n" +
            "                            <div class=\"comment-write-date\"><span class=\"delete-comment\">X</span></div>\n" +
            "                        </div>\n" +
            "                    </li>\n" +
            "                    <li class='comment-content'>\n" +
            // "                        게시글 잘 보고 갑니다. 제 블로그 놀러오셔서 자격증 정보 알아가세요.\n" +
            "                    </li>\n" +
            "                </ul>",
        MESSAGE_LIST: "<li class=\"chat-container-box\">\n" +
            "                        <div class=\"chat-box-top\">\n" +
            "                            <div class=\"chat-people-info\">\n" +
            "                                <div>\n" +
            "                                    <img src=\"../../img/profile-image.png\">\n" +
            "                                </div>\n" +
            "                                <div class=\"chat-people-info-detail\">\n" +
            "                                   <div class=\"chat-people-sender\">" +
            // "                                       김pro
            "                                   </div>\n" +
            "                                   <div class=\"chat-people-category\">" +
            // "                                                   방송댄스 레슨 | 경기도 부천시
            "                                   </div>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                        <div class=\"chat-box-bottom\">\n" +
            "                            <div class=\"chat-box-content\">\n" +
            "                                <div class=\"chat-box-text\">\n" +
            // "                                안녕하세요. 올려주신 요청서에 대한 견적서 보냅니다! 1:1 또는 소규모로 레슨 진행하고 있습니다. 편안하게 문의 주세요.\n" +
            "                                </div>\n" +
            "                                <div class=\"chat-box-badge\">  \n" +
            "                                    N\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "<div class='is-estimate-box'>" +
            "                            <div class=\"divider\"></div>\n" +
            "                            <div class=\"chat-box-price\">\n" +
            "                                <img src=\"../../img/icon-money.png\"/>\n" +
            // "                                시간당 20000원\n" +
            "                            </div>\n" +
            "                            </div>" +
            "                        </div>\n" +
            "                    </li>",
        MESSAGE_ESTIMATE: "<div class=\"s-message-bubble bubble\">\n" +
            "                            <div class=\"chat-profile\">\n" +
            "                                <img src=\"../../img/profile-image.png\"/>\n" +
            "                            </div>\n" +
            "                            <div class=\"sender-message-box\">\n" +
            "                                <div class=\"message-title\">\n" +
            "                                    <img src=\"../../img/icon-money.png\"/>\n" +
            "                                    견적서가 도착했습니다.\n" +
            "                                </div>\n" +
            "                                <div class=\"message-subtitle\">\n" +
            "                                    <span>김소담</span> 고객님 안녕하세요. 요청서에 따른 예상 금액입니다.\n" +
            "                                </div>\n" +
            "                                <div class=\"divider\"></div>\n" +
            "                                <div class=\"service-info\">\n" +
            "                                    <span class=\"service\">서비스</span>\n" +
            "                                    <span class=\"service-name\">방송댄스 레슨</span>\n" +
            "                                </div>\n" +
            "                                <div class=\"price-info\">\n" +
            "                                    <span class=\"price\">예상금액</span>\n" +
            "                                    <span class=\"price-value\">시간 당 20,000원</span>\n" +
            "                                </div>\n" +
            "                                <div class=\"divider\"></div>\n" +
            "                                <div class=\"guide\">\n" +
            "                                    <div class=\"guide-icon\">\n" +
            "                                        <img src=\"../../img/ico-noti.png\">\n" +
            "                                    </div>\n" +
            "                                    <div class=\"guide-text\">\n" +
            "                                        견적금액에 대해 궁금한 점을 채팅으로 물어보세요!\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "                                <div class=\"message-foot\">\n" +
            "                                    <button class=\"estimate-btn\">\n" +
            "                                        <!--detailEstimate이동-->\n" +
            "                                        견적서 상세보기 <img src=\"../../img/ico-arrow-white.png\">\n" +
            "                                    </button>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                            <div class=\"message-status\">\n" +
            "                                <p class=\"message-time\">\n" +
            "                                    <span>" +
            // "                                       오전 10:49" +
        "                                       </span>\n" +
            "                                </p>\n" +
            "                            </div>\n" +
            "                        </div>",
        MESSAGE_RECEIVE: "<div class=\"s-message-bubble bubble\">\n" +
            "                            <div class=\"chat-profile\">\n" +
            "                                <img src=\"../../img/profile-image.png\"/>\n" +
            "                            </div>\n" +
            "                            <div class=\"sender-message-box\">\n" +
            // "                                안녕못해요?안녕못해요?안녕못해요?안녕못해요?안녕못해요?안녕못해요?안녕못해요?안녕못해요?안녕못해요?안녕못해요?안녕못해요?안녕못해요?\n" +
            "                            </div>\n" +
            "                            <div class=\"message-status\">\n" +
            "                                <p class=\"message-time\">\n" +
            "                                    <span>" +
            // "                                       오전 10:50" +
            "                                   </span>\n" +
            "                                </p>\n" +
            "                            </div>\n" +
            "                        </div>",
        MESSAGE_SEND: "<div class=\"r-message-bubble bubble\">\n" +
            "                            <div class=\"receiver-message-box\">\n" +
            // "                                안녕못해요? 혹시 죄송한데 실례가 되지 않으면 집에 보내주시겠어요?\n" +
            "                            </div>\n" +
            "                            <div class=\"message-status\">\n" +
            "                                <p class=\"message-time\">\n" +
            "                                    <span>" +
            // "                               오전 10:49" +
            "                                   </span>\n" +
            "                                </p>\n" +
            "                            </div>\n" +
            "                        </div>",
        MESSAGE_SEND_ESTIMATE: "<div class=\"r-message-bubble bubble\">\n" +
            "                           <div class=\"sender-message-box\">\n" +
            "                                <div class=\"message-title\">\n" +
            "                                    <img src=\"../../img/icon-money.png\">\n" +
            "                                    견적서를 보냈습니다.\n" +
            "                                </div>\n" +
            "                                <div class=\"message-subtitle\">\n" +
            "                                    <span>doyoung</span> 고객님 안녕하세요. 요청서에 따른 예상 금액입니다.\n" +
            "                                </div>\n" +
            "                                <div class=\"divider\"></div>\n" +
            "                                <div class=\"service-info\">\n" +
            "                                    <span class=\"service\">서비스</span>\n" +
            "                                    <span class=\"service-name\">방송댄스 레슨</span>\n" +
            "                                </div>\n" +
            "                                <div class=\"price-info\">\n" +
            "                                    <span class=\"price\">예상금액</span>\n" +
            "                                    <span class=\"price-value\">시간 당 20,000원</span>\n" +
            "                                </div>\n" +
            "                                <div class=\"divider\"></div>\n" +
            "                                <div class=\"guide\">\n" +
            "                                    <div class=\"guide-icon\">\n" +
            "                                        <img src=\"../../img/ico-noti.png\">\n" +
            "                                    </div>\n" +
            "                                    <div class=\"guide-text\">\n" +
            "                                        견적금액에 대해 궁금한 점을 채팅으로 물어보세요!\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "                                <div class=\"message-foot\">\n" +
            "                                    <button class=\"estimate-btn\">\n" +
            "                                        <!--detailEstimate이동-->\n" +
            "                                        견적서 상세보기 <img src=\"../../img/ico-arrow-white.png\">\n" +
            "                                    </button>\n" +
            "                                </div>\n" +
            "                            </div>" +
            "                            <div class=\"message-status\">\n" +
            "                                <p class=\"message-time\">\n" +
            "                                    <span>" +
            // "                               오전 10:49" +
            "                                   </span>\n" +
            "                                </p>\n" +
            "                            </div>\n" +
            "                        </div>",
        MESSAGE_DATE: "<div class=\"chat-room-messages\">\n" +
            "                    <div class=\"chat-date\">2022년 03월 31일</div>\n" +
            "                </div>",
        REQUEST_LIST: "<li class=\"div-card\">\n" +
            "               <div class=\"head-box\">\n" +
            "                    <h3 class=\"card-title\">요청서 제목</h3>\n" +
            "                    <p class=\"card-day\">2022.03.29</p>\n" +
            "                </div>\n" +
            "                <div class=\"card-body\">\n" +
            "                    <div class=\"request-info\">\n" +
            "                        <p><span class=\"people-id\">심PEOPLE</span>님의 요청서</p>\n" +
            "                        <p class=\"request-content\ id=\"request-content\">프론트엔드 개발 속성 강의</p>\n" +
            "                    </div>\n" +
            "                </div>\n"+
            "            </li>",
        REQUEST_MY_LIST: "<li class=\"div-card\">\n" +
            "                 <div class=\"head-box\">\n" +
            "                       <h3 class=\"card-title\">요청서 제목</h3>\n" +
            "                       <p class=\"card-day\">2022.03.29</p>\n" +
            "                 </div>\n" +
            "                 <div class=\"card-body\">\n" +
            "                    <div class=\"request-info\">\n" +
            "                        <p class=\"request-content\">프론트엔드 개발 속성 강의</p>\n" +
            "                    </div>\n" +
            "                </div>\n"+
            "                    <div class=\"decline-btn-wrap\">" +
            "                        <button type=\"button\" class=\"decline-btn\" id=\"decline-btn\">마감하기</button>" +
            "                    </div>" +
            "               </li>",               
        ESTIMATE_LIST: "<li class=\"div-card\">" +
            "               <div class=\"head-box\">" +
            "                   <h3 class=\"card-title\">앱 개발</h3>" +
            "               </div>" +
            "               <div class=\"card-body\">" +
            "                   내용내용내용내용내용" +
            "               </div>" +
            "               <p class=\"card-estimate\">2022.03.29</p>" +
            "           </li>",
        ESTIMATE_MYLIST: "<div class=\"main-box-04\">" +
            "               <div class=\"request-profile\">" +
            "                   <div class=\"img\"><img src=\"../../img/temp-img-profile.png\"></div>" +
            "                   <div class=\"detail\">" +
            "                       <div class=\"profile-name\">박고수</div>" + 
            "                       <div class=\"info\">" +
            "                       </div>" +
            "                   </div>" +
            "                   </div>" +
            "               </div>",
        QNA_ITEM : "<li class=\"item-container-box\">\n" +
            "                <div class=\"item-box-top\">\n" +
            "                    <div class=\"item-box-l\">답변 전</div>\n" +
            "                    <div class=\"item-box-r\">2022.03.31</div>\n" +
            "                </div>\n" +
            "                <div class=\"item-box-bottom\">\n" +
            "                    <div class=\"item-box-btm-l\">\n" +
            "                        <div class=\"item-box-title\">앱이 개똥이에요</div>\n" +
            "                        <div class=\"item-box-content\">이따구로 만들면 누가 사용하나요?</div>\n" +
            "                    </div>\n" +
            "                    <div class=\"item-box-btm-r\">\n" +
            "                        <div class=\"item-box-status\">\n" +
            "                            상세 보기\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </li>",
        QNA_ANSWER : "<li class=\"item-container-box\">\n" +
            "                <div class=\"item-box-top\">\n" +
            "                    <div class=\"item-box-l\" id='response-number'>답변 번호 : <span>123456789</span></div>\n" +
            "                    <div class=\"item-box-r\" id='response-date'>2022.03.31</div>\n" +
            "                </div>\n" +
            "                <div class=\"item-box-bottom\">\n" +
            "                    <div class=\"item-box-title\" id='answer-title'>RE: 안녕하세여 여쭤볼 게 있는데여</div>\n" +
            "                    <div class=\"item-box-pro\" id='answer-content'>저 카테고리에 다른거 추가해주시면 안댈가요? 저는 레저스포츠 선생님이 필요해요<br/><br/>-------------------------<br/><br/>\n" +
            "                        안녕하세요? TaLan입니다.<br/>레저스포츠 고수는 레슨 카테고리를 이용하시면 됩니다.<br/> 또 다른 문의사항 있으시면 언제든지 문의게시판에 글 남겨주세요.<br/>감사방구.<br/></div>\n" +
            "                </div>\n" +
            "            </li>",
        QNA_QUESTION : " <li class=\"item-container-box\">\n" +
            "                <div class=\"item-box-top\">\n" +
            "                    <div class=\"item-box-l\" id='inquiry-number'>문의 번호 : <span>0</span></div>\n" +
            "                    <div class=\"item-box-r\" id='inquiry-date'>2022.03.31</div>\n" +
            "                </div>\n" +
            "                <div class=\"item-box-top\">\n" +
            "                    <div class=\"item-box-title\" id='subject'>비밀글입니다.</div>\n" +
            "                    <div class=\"item-box-pro\" id='content'>비밀글입니다.</div>\n" +
            "                </div>\n" +
            "            </li>",
        QNA_BUTTON_DELETE : "<div class=\"qna-button\">\n" +
            "                삭제하기\n" +
            "            </div>\n",
        QNA_BUTTON_MODIFY : "<div class=\"qna-button\">\n" +
            "                수정하기\n" +
            "            </div>",
        QNA_BUTTON_ANSWER : "<div class=\"qna-button\">\n" +
            "                답변하기\n" +
            "            </div>",
        PAYMENT_LIST:"<li class=\"item-container-box\">" +
            "            <div class=\"item-box-top\">"+
            "                <div class=\"item-box-l\">2022.04.15</div>" +
            "                <div class=\"item-box-r\">카드</div>" +
            "            </div>" +
            "            <div class=\"item-box-bottom\">" +
            "                <img src=\"../../img/img.png\" />" +
            "                <div class=\"item-box-btm-l\">" +
            "                    <div class=\"item-box-title\">션이 님과의 매칭</div>" +
            "                    <div class=\"item-box-pro\">30000원</div>" +
            "                </div>" +
            "                <div class=\"item-box-btm-r\">" +
            "                    <div class=\"item-box-status\">결제 완료</div>" +
            "                </div>" +
            "            </div>" +
            "        </li>",
        NO_LIST : "<div class=\"no-received-quotes\">\n" +
            "            <article class=\"no-items align-self-center text-center\">\n" +
            "                <img src=\"../../img/folder.png\"/>\n" +
            "                <h3 style=\"font-weight:700;font-size:2rem\" id='title-h3'>견적 내역이 없습니다.</h3>\n" +
            "                <p class=\"help-block p2\">\n" +
            "                    <div class=\"desc\">고수님들이 요청서를 확인하고 있어요. 조금만 기다려주세요!</div>\n" +
            "                </p>\n" +
            "                <!---->\n" +
            "            </article>\n" +
            "        </div>"
    };

    window.__config__ = module;
})(window, M);