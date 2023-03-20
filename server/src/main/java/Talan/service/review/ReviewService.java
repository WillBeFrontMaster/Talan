package Talan.service.review;

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

import Talan.DTO.PeopleDTO;
import Talan.DTO.RequestDTO;
import Talan.DTO.ReviewDTO;

@Service
public class ReviewService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired(required = true)
    @Qualifier("sqlSession_sample")
    private SqlSession sqlSession;
	
	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	// 리뷰 등록
	public int registReview(Map<String, Object> param) {
		//트랜잭션 구현
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager_sample.getTransaction(def);

        int result = 0;
        try{
           
            result = sqlSession.insert("review.registReview", param);
            if (result == 1) {
            	sqlSession.update("payment.setReviewStatus", param);
            }
            transactionManager_sample.commit(status);
            logger.info("========== 리뷰 등록 완료 : {}", result);
            
        }catch(Exception e){
        	logger.error("[ERROR] registReview() Fail : e : {}", e.getMessage());
        	e.printStackTrace();
        	transactionManager_sample.rollback(status);    	
        }
		return result;
	}

	// 리뷰 답변
	public int responseReview(Map<String, Object> param) {
		//트랜잭션 구현
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager_sample.getTransaction(def);

        int result = 0;
        try{   
            result = sqlSession.update("review.responseReview", param);
            transactionManager_sample.commit(status);
            logger.info("========== 리뷰 답변 완료 : {}", result);
            
        }catch(Exception e){
        	logger.error("[ERROR] responseReview() Fail : e : {}", e.getMessage());
        	e.printStackTrace();
        	transactionManager_sample.rollback(status);    	
        }
		return result;
	}

	
	// 리뷰 리스트
	public List<Object> reviewList(Map<String, Object> param) {
		List<ReviewDTO> review = new ArrayList<ReviewDTO>();

		review = sqlSession.selectList("review.getReviewList", param);

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < review.size(); i++) {
			Map<String, Object> preList = new HashMap<String, Object>();
			String peopleNickname = new String();
			String proNickname = new String();
			
			peopleNickname = sqlSession.selectOne("people.getNickname", review.get(i).getPeopleId());
			proNickname = sqlSession.selectOne("people.getNickname", review.get(i).getProId());
			
			preList.put("peopleNickname", peopleNickname);
			preList.put("proNickname", proNickname);
			preList.putAll(review.get(i).getReviewList());
			list.add(preList);
		}
		return list;
	}

	// 리뷰 상세 보기
	public ReviewDTO reviewDetail(Map<String, Object> param) {
		ReviewDTO review = new ReviewDTO();
		review = sqlSession.selectOne("review.getReview", param);
		return review;
	}

	public List<Object> reviewMyList(Map<String, Object> param) {
		List<ReviewDTO> review = new ArrayList<ReviewDTO>();

		review = sqlSession.selectList("review.getReviewListPro", param);

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < review.size(); i++) {
			Map<String, Object> preList = new HashMap<String, Object>();
			String peopleNickname = new String();
			String proNickname = new String();
			
			peopleNickname = sqlSession.selectOne("people.getNickname", review.get(i).getPeopleId());
			proNickname = sqlSession.selectOne("people.getNickname", review.get(i).getProId());
			
			preList.put("peopleNickname", peopleNickname);
			preList.put("proNickname", proNickname);
			preList.putAll(review.get(i).getReviewList());
			list.add(preList);
		}
		return list;
	}

}
