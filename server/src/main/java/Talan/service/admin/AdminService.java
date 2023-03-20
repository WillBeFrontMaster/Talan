package Talan.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mindrot.bcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import Talan.DTO.AdminDTO;
import Talan.DTO.MessageDTO;
import Talan.DTO.ReportDTO;

@Service
public class AdminService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	@Autowired(required = true)
	@Qualifier("bCrypt")
	private BCrypt bCrypt;
	
	public int registAdmin(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		param.replace("adminPassword", bCrypt.hashpw(param.get("adminPassword").toString(), bCrypt.gensalt()));

		int result = 0;
		try {
			result = sqlSession.insert("admin.registAdmin", param);

			transactionManager_sample.commit(status);
			logger.info("========== 관리자 등록 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] registAdmin() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 관리자 로그인
	public int loginAdmin(Map<String, Object> param) {
		int result = 0;
		AdminDTO dto = sqlSession.selectOne("admin.loginAdmin", param.get("peopleId"));

		try {
			if (bCrypt.checkpw(param.get("password").toString(), dto.getAdminPassword())) {
				result = 1;
			} else {
				result = -1;
			}
		} catch (Exception e) {
			logger.error("[ERROR] loginAdmin() Fail : e : {}", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	// 신고 리스트
	public List<Object> reportList(Map<String, Object> param) {
		List<ReportDTO> report = new ArrayList<ReportDTO>();

		report = sqlSession.selectList("report.getReportList", param);

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < report.size(); i++) {
			list.add(report.get(i).getReportList());
		}
		return list;
	}

	// 문의 삭제
	public int deleteInquiry(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.update("inquiry.deleteInquiryAdmin", param);
			transactionManager_sample.commit(status);
			logger.info("========== 문의 삭제 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] deleteInquiry() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 요청서 삭제
	public int deleteRequest(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.update("request.deleteRequest", param);
			transactionManager_sample.commit(status);
			logger.info("========== 요청서 삭제 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] deleteInquiry() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 신고처리
	public int reportProcess(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.update("report.reportProcess", param);
			if (result == 1 && param.get("reportStatus").toString().equals("1")) {
				sqlSession.delete("admin.banishPeople", param);
				List<MessageDTO> message = sqlSession.selectList("message.getChatList", param.get("reportTarget"));
				sqlSession.delete("estimate.deleteEstimate", param.get("reportTarget"));
				sqlSession.delete("request.deleteRequestOutPeople", param.get("reportTarget"));
				for (int i = 0; i < message.size(); i++) {
				sqlSession.delete("message.deleteChat", message.get(i).getChatNumber());
				}
					
			}
			transactionManager_sample.commit(status);
			logger.info("========== 신고 처리 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] deleteInquiry() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 리뷰 숨기기
	public int deleteReview(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.delete("admin.deleteReview", param);
			if (result == 1) {
				sqlSession.update("admin.updateProKindScore", param);
			}
			transactionManager_sample.commit(status);
			logger.info("========== 신고 처리 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] deleteInquiry() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

}
