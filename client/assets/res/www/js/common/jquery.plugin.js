/**
 * jQuery Custom Plugin
 * @author
 */

(function ($, M, CONFIG) {
    var ENV = CONFIG.ENV;
    var MSG = CONFIG.MSG;
    var CONSTANT = CONFIG.CONSTANT;
    var SERVER_CODE = CONFIG.SERVER_CODE;
    var HTML = CONFIG.HTML;
    /**
     * 함수 여부 확인
     * @param {any} target
     * @returns {boolean}
     */
    $.isFunction = function isFunction(target) {
        return typeof target === 'function';
    }

    /**
     * 문자열 여부 확인
     * @param {any} target
     * @returns {boolean}
     */
    $.isString = function isString(target) {
        return typeof target === 'string';
    }

    /**
     * 값의 존재 여부 확인
     */
    $.isEmpty = function isEmpty(obj) {
        var hasOwnProperty = Object.prototype.hasOwnProperty;
        if (typeof obj == "number") obj = obj + "";

        if (obj == null) return true;
        if (obj === "undefined") return true;
        if (obj.length > 0) return false;
        if (obj.length === 0) return true;

        for (var key in obj) {
            if (hasOwnProperty.call(obj, key)) return false;
        }
        return true;
    };

    /**
     * modal 옵션
     * @param {object} options option 값
     * @param {string} options.title 제목 / 기본값 : '알림'
     * @param {string} options.content 내용 / 기본값 : ''
     * @param {array} options.buttons 문자열 버튼 배열 / 기본값 ['확인']
     * @param {function} options.callback 클릭 이벤트 처리
     */
    $.modal = function modal(options, callback) {
        if ($.isString(options)) options = {content: options};
        if ($.isFunction(callback)) options.callback = callback;
        var _options = {
            title: options.title || '알림',
            message: options.content || '',
            buttons: options.buttons || ['확인'],
            callback: function (idx) {
                if ($.isFunction(options.callback)) {
                    options.callback(idx);
                }
            }
        };
        M.pop.alert(_options);
    }

    /**
     * 토스트 팝업을 띄운다
     * @param {string} msg
     */
    $.toast = function toast(msg) {
        M.pop.instance(msg);
    };

    /**
     * 화면이동에 대한 기본 함수
     * @param {object} options
     * @param {string} options.url 이동할 페이지 값
     * @param {object} options.param 넘길 파라미터 값
     * @param {string} options.actionType 이동시 액션타입 / NEW_SCR(기본), NO_HISTORY, CLEAR_TOP
     */
    $.movePage = function movePage(options) {
        if ($.isString(options)) options = {url: options};
        if ($.isEmpty(options.url)) throw new Error('$.movePage :: url 값은 필수입력입니다.');
        var _options = {
            url: options.url,
            param: options.param || {},
            actionType: options.actionType || 'NEW_SCR'
        };
        M.page.html(_options);
    }

    /**
     * HTTP 통신 모듈
     * @param {object} options
     * @param {string} options.path 호출할 path
     * @param {string} options.method HTTP 매서드 (GET|POST|PUT|DELETE)
     * @param {number} options.timeout 타임아웃 시간
     * @param {object} options.indicator 인디케이터 옵션
     * @param {object} options.data 바디데이터
     * @param {funciton} options.succ 성공시 콜백
     * @param {function} options.error 실패시 콜백
     */
    $.sendHttp = function sendHttp(options) {
        if ($.isEmpty(options.path)) throw new Error('sendHttp :: 옵션의 Path 값은 필수입니다.');
        var _error = null;
        if ($.isEmpty(options.error)) {

        }
        var succFunc = function succFunc(data) {
            console.log('HTTP RESPONE :: ', data);
            if (data.rsltCode == SERVER_CODE.SUCC) {
                if ($.isFunction(options.succ)) {
                    options.succ(data);
                }
            } else if(data.rsltCode == SERVER_CODE.SUCC){ // 로그인 필요시
                swal('로그인')
            }else {
                // 실패
                //$.modal(data.rsltMsg);

                if ($.isFunction(options.error)) {
                    options.error(data);
                }
            }
        };

        var errFunc = function errFunc(code, msg, setting) {
            $.modal(code + '\n' + msg);
            var callback = options.error || function (code, msg, setting) {
                console.log(code + msg + setting);
            };
            callback(code, msg, setting);
        };

        var _options = {
            server: ENV.SERVER_NAME,
            path: options.path, // 필수
            method: options.method || 'POST',
            timeout: options.timeout || 3000,
            indicator: options.indicator || {show: ENV.INDICATOR, message: MSG.INDICATOR_MSG, cancelable: false},
            data: options.data || {},
            success: succFunc,
            error: errFunc
        };

        console.log('HTTP URL :: ' + _options.path);
        M.net.http.send(_options); // 실제로 통신 시작
    }
    $.fileHttpSend = function (options) {
        // body: [
        // { content: "파일업로드", type: "TEXT" },
        // { name: "imgs", content: "test.zip", type: "FILE" },
        // ],
        var fileUploadFinish = function (status, header, body, setting) {
            var _body = null;
            try {
                var _body = JSON.parse(body);
            } catch (e) {
                _body = body;
            }

            if (status == '200' && $.isFunction(options.succ) && _body.rsltCode == SERVER_CODE.SUC) {
                options.succ(_body.body);
            } else if ($.isFunction(options.error)) {
                options.error(status, body)
            }
        }
        var fileUploadProgress = function (total, current) {
            if ($.isFunction(options.progress)) {
                options.progress(total, current)
            }
        }
        var _options = {
            url: ENV.UPLOAD_URL + options.path,
            header: options.header || {},
            params: options.params || {},
            body: options.body || [],
            encoding: "UTF-8",
            finish: fileUploadFinish,
            progress: fileUploadProgress
        }


        M.net.http.upload(_options);
    }
    /**
     * @param {object} options
     * @param {string} options.param 전달할 파라미터
     * @param {Animation Type} options.animation 애니메이션
     * */
    $.moveBack = function (options) {
        console.log('뒤로가기');
        if (options == null) {
            {
                options = {param: ''}
            }
        }
        var _options = {
            param: options.param || {},
            animation: options.animation || "DEFAULT"
        };
        M.page.back();
    }
    // Storage 저장소 관련 모듈
    $.storage = {
        /**
         * 저장된 사용자 로그인 정보를 복호화하여 가져온다.
         * @returns {object|string}
         */
        getAuth: function getAuth() {
            var options = M.data.storage(CONSTANT.AUTO_LOGIN_AUTH);
            var _options = {
                id: $.decrypt(options.id).result,
                pw: $.decrypt(options.pw).result
            };
            console.log(_options.id);
            console.log(_options.pw);
            return _options;
        },

        /**
         * 사용자 로그인 정보를 암호화하여 저장한다.
         * @param {string} id
         * @param {string} pw
         */
        setAuth: function setAuth(id, pw) {
            var encId = $.encrypt(id).result;
            var encPw = $.encrypt(pw).result;
            M.data.storage(CONSTANT.AUTO_LOGIN_AUTH, {id: encId, pw: encPw});
        },
        /**
         * 저장된 사용자 로그인 정보를 삭제한다.
         */
        clearAuth: function clearAuth() {
            M.data.removeStorage(CONSTANT.AUTO_LOGIN_AUTH);
        },
        /**
         * 채팅방 최근 시간 저장
         * @param id
         * @param time
         */
        setMessageTime: function (id, time) {
            var encTime = $.encrypt(time).result;
            M.data.storage(id, encTime);
        },
        /**
         * 채팅방 최근 시간 불러오기
         * @param id
         * @return {string}
         */
        getMessageTime: function (id) {
            var time = M.data.storage(id);
            var decTime = $.decrypt(time).result;
            return decTime;
        }
    }
    /**
     * @param {string} options 암호화할 Hex 문자열
     * returns {object|string} status 변환 성공 여부 (SUCCESS or FAIL)
     * returns {object|string} result 암호화된 문자열
     */
    $.encrypt = function (options) {
        return M.sec.encrypt(options);
    }
    /**
     * @param {string} options 암호화된 Hex 문자열
     * returns {object|string} status 변환 성공 여부 (SUCCESS or FAIL)
     * returns {object|string} result 복호화된 문자열
     */
    $.decrypt = function (options) {
        return M.sec.decrypt(options);
    }
    /**
     * picker
     * @param {object} options
     * @param {string} options.mode SINGLE or MULTI
     * @param {string} options.mediaType 사진(PHOTO) or 동영상(VIDEO) or 오디오(AUDIO) or 갤러리(ALL)
     * @param {string} options.path 파일 경로 (도큐먼트로 부터의 경로, 로컬 웹서버를 통해 파일 접근 가능)
     * @param {int} options.maxCount 최대 선택 갯수
     * @param {int} options.column  미디어 선택 화면 컬럼 수
     * @param {boolean} options.detail 상세 화면 모드
     * @param {boolean} options.zoom 줌 모드
     * @param {Function} options.succ 성공시 콜백
     * @param {Function} options.error 실패시 콜백
     * */
    $.picker = function (options) {
        var callback = function succFunc(status, data) {
            console.log('HTTP RESPONE :: ', status + JSON.stringify(data));
            if (status === "SUCCESS") {
                if ($.isFunction(options.succ)) {
                    options.succ(data);
                }
            } else {
                // 실패
                $.modal(data);
                if ($.isFunction(options.error)) {
                    options.error(data);
                }
            }
        };
        var _options = {
            mode: options.mode || "SINGLE", // SINGLE, MULTI(PHOTO 일때만)
            mediaType: options.mediaType || "PHOTO", //( PHOTO: 사진, VIDEO: 동영상, AUDIO: 오디오, ALL : 갤러리 )
            path: options.path || null,
            maxCount: options.maxCount || 0, //(기본값:0, 0인경우 제한없음)
            column: options.column || 3,
            detail: options.detail || false,
            zoom: options.zoom || false,
            callback: callback,
        }
        M.media.picker(_options);
    }
    /**
     *
     * @param path
     * @return {string|*}
     */
    $.imagePath = function imagePath(path, image,self,id) {
        if (self !== null){
            $(self).on('click',function(){
                $.movePage({
                    url : "/www/html/pro/proInfo.html",
                    param : {
                        proId : id
                    }
                });
            });
        }
        if ($.isEmpty(image)) {
            return "/res/www/img/no-image-found-290180.png"
        } else {
            return path + image;
        }
    }

    $.setStar = function (score, cnt) {
        var start = "<div class=\"pro-feed-info\">\n";
        var one = "<img src=\"../../img/star.png\">\n";
        var half = "<img src=\"../../img/half-star.png\">\n";
        var zero = "<img src=\"../../img/star-null.png\">\n";
        var end = "<span>" + score + "  (" + cnt + ")</span>\n</div>";
        var _STAR_POINT = start + one;
        if (score < 1.5) {
            _STAR_POINT += zero + zero + zero + zero;
        } else if (score < 1.9) {
            _STAR_POINT += half + zero + zero + zero;
        } else if (score < 2) {
            _STAR_POINT += one + zero + zero + zero;
        } else if (score < 2.5) {
            _STAR_POINT += one + zero + zero + zero;
        } else if (score < 2.9) {
            _STAR_POINT += one + half + zero + zero;
        } else if (score < 3) {
            _STAR_POINT += one + one + zero + zero;
        } else if (score < 3.5) {
            _STAR_POINT += one + one + zero + zero;
        } else if (score < 3.9) {
            _STAR_POINT += one + one + half + zero;
        } else if (score < 4) {
            _STAR_POINT += one + one + one + zero;
        } else if (score < 4.5) {
            _STAR_POINT += one + one + one + zero;
        } else if (score < 4.9) {
            _STAR_POINT += one + one + one + half;
        } else if (score < 5) {
            _STAR_POINT += one + one + one + one;
        }
        _STAR_POINT += end;
        return _STAR_POINT;
    }


})(jQuery, M, __config__);