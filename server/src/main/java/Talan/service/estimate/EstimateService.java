package Talan.service.estimate;

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
import Talan.DTO.EstimateListDTO;
import Talan.DTO.ProDTO;
import Talan.DTO.RequestDTO;
import Talan.service.MessageService;

@Service
public class EstimateService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	@Autowired
	private MessageService service;

	// 견적서 등록
	public int registEstimate(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.insert("estimate.registEstimate", param);
			if (result == 1) {
				service.insertEstimateMessage(param);
			}
			transactionManager_sample.commit(status);
			logger.info("========== 견적서 등록 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] registEstimate() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}
	
	// 내가 보낸 견적서 조회
	public List<Object> estimateMyList(Map<String, Object> param) {
		List<EstimateDTO> estimate = new ArrayList<EstimateDTO>();

		estimate = sqlSession.selectList("estimate.getEstimateMyList", param);

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < estimate.size(); i++) {
			Map<String, Object> preList = new HashMap<String, Object>();
			RequestDTO request = new RequestDTO();
			
			request = sqlSession.selectOne("request.getRequest", estimate.get(i).getRequestNumber());
			
			preList.put("requestStatus", request.getRequestStatus());
			preList.putAll(estimate.get(i).getEstimateList());
			list.add(preList);
		}
		return list;
	}

	// 견적서 리스트 조회
	public List<Object> estimateList(Map<String, Object> param) {
		List<EstimateListDTO> estimate = sqlSession.selectList("estimate.getEstimateList", param);
		List<Object> list = new ArrayList<Object>();
		Map<String, Object> proInfo = new HashMap<String, Object>();
		for (int i = 0; i < estimate.size(); i++) {
			Map<String, Object> preList = new HashMap<String, Object>();
			ProDTO pro = new ProDTO();
			String reviewCount = new String();
			pro = sqlSession.selectOne("pro.getProInfo", estimate.get(i).getProId());
			reviewCount = sqlSession.selectOne("review.getReviewCount", pro.getProId());
			proInfo.put("kindScore", pro.getKindScore());
			proInfo.put("reviewCount", reviewCount);
			preList.putAll(proInfo);;
			preList.putAll(estimate.get(i).getEstimateList());
			list.add(preList);
		}
		return list;
	}
	

	// 견적서 상세 조회
	public EstimateDTO detailEstimate(Map<String, Object> param) {
		EstimateDTO estimate = sqlSession.selectOne("estimate.getEstimate", param);
		return estimate;
	}

	// 견적서 매칭
	public int matchedEstimate(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.update("estimate.setEstimateMatched", param);

			transactionManager_sample.commit(status);
			logger.info("========== 견적서 매칭 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] matchedEstimate() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 견적서 거절
	public int rejectEstimate(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.update("estimate.setEstimateReject", param);

			transactionManager_sample.commit(status);
			logger.info("========== 견적서 거절 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] matchedEstimate() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

}
