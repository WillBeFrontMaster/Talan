//카카오 결제 API
var IMP = window.IMP; // 생략가능
IMP.init('impxxxxxxxx');  // 가맹점 식별코드
// IMP.request_pay(param, callback) 결제창 호출
IMP.request_pay({
    pg : 'kakaopay', //pg사 선택 (kakao, kakaopay 둘다 가능)
    pay_method: 'card',
    merchant_uid : 'merchant_' + new Date().getTime(), //주문번호
    name : 'Bunddeuk', // 상품명
    amount : amount,
    //customer_uid 파라메터가 있어야 빌링키 발급을 시도함
    customer_uid : buyer_name + new Date().getTime(),
    buyer_email : email,
    buyer_name : buyer_name,
    buyer_tel : hp,
    buyer_addr : addr,
}, function(rsp) { //callback
    if ( rsp.success ) {
      console.log('빌링키 발급 성공', rsp)
      //빌링키 발급이 완료되었으므로, 서버에 결제 요청
      alert('예약 결제가 완료되었습니다!');
    } else {
      var msg = '결제에 실패하였습니다.\n';
      msg += rsp.error_msg;
      alert(msg);
      return false;
    }
    $("#final-support-submit").submit();
});