package Talan.service.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import Talan.DTO.EstimateDTO;
import Talan.DTO.PaymentDTO;
import Talan.DTO.PaymentDetailDTO;
import Talan.DTO.PeopleDTO;
import Talan.DTO.ReviewDTO;

@Service
public class PaymentService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired(required = true)
    @Qualifier("sqlSession_sample")
    private SqlSession sqlSession;
	
	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	// 결제
	public int paymentInsert(Map<String, Object> param) {
		//트랜잭션 구현
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager_sample.getTransaction(def);

        int result = 0;
        try{
            result = sqlSession.insert("payment.paymentInsert", param);
            if (result == 1) {
            	EstimateDTO estimate = sqlSession.selectOne("estimate.getEstimate",param);
            	sqlSession.update("request.setRequestClosed", estimate.getRequestNumber());
            }
            transactionManager_sample.commit(status);
            logger.info("========== 결제 완료 : {}", result);
            
        }catch(Exception e){
        	logger.error("[ERROR] paymentInsert() Fail : e : {}", e.getMessage());
        	e.printStackTrace();
        	transactionManager_sample.rollback(status);    	
        }
		return result;
	}

	// 결제 취소
	public int paymenCancel(Map<String, Object> param) {
		//트랜잭션 구현
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager_sample.getTransaction(def);

        int result = 0;
        try{
            result = sqlSession.insert("payment.paymentCancel", param);
            
            transactionManager_sample.commit(status);
            logger.info("========== 결제 취소 : {}", result);
            
        }catch(Exception e){
        	logger.error("[ERROR] paymenCancel() Fail : e : {}", e.getMessage());
        	e.printStackTrace();
        	transactionManager_sample.rollback(status);    	
        }
		return result;
	}

	// 결제 조회
	public List<Object> paymentList(Map<String, Object> param) {
		List<PaymentDTO> payment = new ArrayList<PaymentDTO>();

		payment = sqlSession.selectList("payment.getPaymentList", param);

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < payment.size(); i++) {
			PeopleDTO peopleInfo = new PeopleDTO();
			Map<String, Object> preList = new HashMap<String, Object>();
			peopleInfo = sqlSession.selectOne("people.getPeopleInfo", payment.get(i).getProId());
			preList.put("nickname", peopleInfo.getNickname());
			preList.putAll(payment.get(i).getPaymentList());
			list.add(preList);
		}
		return list;
	}

	// 결제 상세 조회
	public PaymentDetailDTO paymentDetail(Map<String, Object> param) {
		PaymentDetailDTO payment = new PaymentDetailDTO();
		payment = sqlSession.selectOne("payment.getPayment", param);
		
		if (payment.getReviewStatus() == '1') {
			String reviewNumber = sqlSession.selectOne("review.getReviewNumber", param);
			payment.setReviewNumber(reviewNumber);
		}
		return payment;
	}
	
	
}
