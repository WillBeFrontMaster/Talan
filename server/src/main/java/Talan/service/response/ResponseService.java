package Talan.service.response;

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

import Talan.DTO.ResponseDTO;

@Service
public class ResponseService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	// 답변 등록
	public int registResponse(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.insert("response.registResponse", param);
			sqlSession.update("inquiry.setInquiryStatus", param);
			transactionManager_sample.commit(status);
			logger.info("========== 답변 등록 완료 : {}", result);
		} catch (Exception e) {
			logger.error("[ERROR] registResponse() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 답변 조회
	public ResponseDTO responseInfo(Map<String, Object> param) {
		ResponseDTO response = new ResponseDTO();
		response = sqlSession.selectOne("response.getResponse", param);
		return response;
	}

	
	
	
	
	

}
