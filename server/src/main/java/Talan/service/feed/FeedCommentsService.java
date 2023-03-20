package Talan.service.feed;

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

import Talan.DTO.FeedCommentsDTO;

@Service
public class FeedCommentsService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired(required = true)
    @Qualifier("sqlSession_sample")
    private SqlSession sqlSession;
	
	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	// Feed 댓글 등록
	public int registFeedComments(Map<String, Object> param) {
		//트랜잭션 구현
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager_sample.getTransaction(def);

        int result = 0;
        try{
            result = sqlSession.insert("feedComments.registFeedComments", param);
            
            transactionManager_sample.commit(status);
            logger.info("========== 피드 댓글 등록 완료 : {}", result);
            
        }catch(Exception e){
        	logger.error("[ERROR] registFeedComments() Fail : e : {}", e.getMessage());
        	e.printStackTrace();
        	transactionManager_sample.rollback(status);    	
        }
		return result;
	}

	// Feed 댓글 조회
	public List<Object> detailFeedCommentsList(Map<String, Object> param) {
		List<FeedCommentsDTO> feedComments = new ArrayList<FeedCommentsDTO>();
		feedComments = sqlSession.selectList("feedComments.getFeedCommentsList", param);
		List<Object> list = new ArrayList<Object>();
		for(int i = 0; i < feedComments.size(); i++) {
			feedComments.get(i).setNickname(sqlSession.selectOne("people.getNickname", feedComments.get(i).getPeopleId()));
			list.add(feedComments.get(i).getFeedCommentsList());
		}
		return list;
	}

	// Feed 댓글 삭제
	public int deleteFeedComment(Map<String, Object> param) {
		//트랜잭션 구현
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager_sample.getTransaction(def);

        int result = 0;
        try{
            result = sqlSession.delete("feedComments.deleteFeedComment", param);
            
            transactionManager_sample.commit(status);
            logger.info("========== 피드 댓글 삭제 완료 : {}", result);
            
        }catch(Exception e){
        	logger.error("[ERROR] registFeedComments() Fail : e : {}", e.getMessage());
        	e.printStackTrace();
        	transactionManager_sample.rollback(status);    	
        }
		return result;
	}

	

}
