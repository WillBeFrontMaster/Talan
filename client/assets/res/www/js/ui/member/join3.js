/**
 * @file : join3.js
 * @author : suhyun
 * @date : 2022.04.11
 */

 (function ($,M,window){
    var page = {
      els:{
          $loginBtn: null
      },
      data: {},
      init: function init(){
          this.els.$loginBtn = $('#login-btn');
      },
      initView: function initView(){
        // 화면에서 세팅할 동적 데이터
      },
      initEvent: function initEvent(){
        // DOM Event 바인딩
        var self = this;
        this.els.$loginBtn.on('click', function(){
            self.login();
        })
      },
      
      login: function(){
          M.page.html({
              url: "./login.html",
              actionType: "CLEAR_TOP"
          })
      }
  //    method: {},
    };
    window.__page__ = page;
  })(jQuery,M,window);
  
  (function($,M,pageFunc,window){
    M.onReady(function(){
      pageFunc.init(); // 최초 화면 초기화
      pageFunc.initView();
      pageFunc.initEvent();
    });
    
  // 해당 페이지에서 실제 호출
  })(jQuery, M,__page__,window);