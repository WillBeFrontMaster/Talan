package Talan.service.people;

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

import Talan.DTO.EstimateDTO;
import Talan.DTO.InquiryDTO;
import Talan.DTO.MessageDTO;
import Talan.DTO.MyPageDTO;
import Talan.DTO.PaymentDTO;
import Talan.DTO.PeopleDTO;
import Talan.DTO.RequestDTO;
import Talan.DTO.ReviewDTO;

@Service
public class PeopleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	@Autowired(required = true)
	@Qualifier("bCrypt")
	private BCrypt bCrypt;

	// 회원정보
	public PeopleDTO getPeopleInfo(Map<String, Object> param) {
		return sqlSession.selectOne("people.getPeopleInfo", param);
	}

	// 회원가입
	public int insertPeople(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		param.replace("password", bCrypt.hashpw(param.get("password").toString(), bCrypt.gensalt()));

		int result = 0;
		try {

			result = sqlSession.insert("people.insertPeople", param);

			transactionManager_sample.commit(status);
			logger.info("========== 유저 등록 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] insertPeople() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 회원가입 (+프로필사진)
	public int insertPeopleWithImage(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		param.replace("password", bCrypt.hashpw(param.get("password").toString(), bCrypt.gensalt()));

		int result = 0;
		try {

			result = sqlSession.insert("people.insertPeopleWithImage", param);

			transactionManager_sample.commit(status);
			logger.info("========== 유저 등록 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] insertPeople() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 회원정보 수정
	public int updatePeople(Map<String, Object> param) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {

			PeopleDTO info = sqlSession.selectOne("people.getPeopleInfo", param);
			result = sqlSession.update("people.updatePeople", param);

			transactionManager_sample.commit(status);
			logger.info("========== 유저 수정 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] updatePeople() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 내 소개 수정
	public int updateIntro(Map<String, Object> param) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.update("people.updateIntro", param);

			transactionManager_sample.commit(status);
			logger.info("========== 유저 소개 수정 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] updateIntro() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 회원탈퇴
	public int deletePeople(Map<String, Object> param) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.delete("people.deletePeople", param);
			try {
				List<MessageDTO> message = sqlSession.selectList("message.getChatList", param);
				sqlSession.delete("estimate.deleteEstimate", param.get("peopleId"));
				sqlSession.delete("request.deleteRequestOutPeople", param.get("peopleId"));
				for (int i = 0; i < message.size(); i++) {
					sqlSession.delete("message.deleteChat", message.get(i).getChatNumber());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			transactionManager_sample.commit(status);
			logger.info("========== 유저 탈퇴 완료 : {}", result);
		} catch (Exception e) {
			logger.error("[ERROR] deletePeople() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 로그인
	public int loginPeople(Map<String, Object> param) {
		int result = 0;
		PeopleDTO info = new PeopleDTO();
		info = sqlSession.selectOne("people.getPeopleInfo", param);
		try {
			if (bCrypt.checkpw(param.get("password").toString(), info.getPassword())) {
				result = 1;
			} else {
				result = -1;
			}
		} catch (Exception e) {
			logger.error("[ERROR] login() Fail : e : {}", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	// 아이디찾기
	public String findId(Map<String, Object> param) {
		return sqlSession.selectOne("people.findId", param);
	}

	// 본인인증
	public int isPeople(Map<String, Object> param) {
		if (sqlSession.selectOne("people.isPeople", param) != null) {
			return 1;
		} else {
			return 0;
		}
	}

	// 비밀번호변경
	public int changePWD(Map<String, Object> param) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);
		param.replace("password", bCrypt.hashpw(param.get("password").toString(), bCrypt.gensalt()));
		int result = 0;
		try {
			result = sqlSession.update("people.changePWD", param);
			transactionManager_sample.commit(status);

		} catch (Exception e) {
			logger.error("[ERROR] changePWD() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 아이디중복확인
	public int duplicatePeople1(Map<String, Object> param) {
		if (sqlSession.selectOne("people.duplicatePeople1", param) != null) {
			return 1;
		} else {
			return 0;
		}
	}

	// 이메일 중복확인
	public int duplicatePeople2(Map<String, Object> param) {
		if (sqlSession.selectOne("people.duplicatePeople2", param) != null) {
			return 1;
		} else {
			return 0;
		}
	}

	// 닉네임 중복확인
	public int duplicatePeople3(Map<String, Object> param) {
		if (sqlSession.selectOne("people.duplicatePeople3", param) != null) {
			return 1;
		} else {
			return 0;
		}
	}

	// 마이페이지
	public MyPageDTO myPage(Map<String, Object> param) {
		MyPageDTO dto = new MyPageDTO();
		PeopleDTO peopleDTO = sqlSession.selectOne("mypage.getInfo", param);
		PaymentDTO paymentPro = sqlSession.selectOne("mypage.getLastPaymentPRO",param);
		PaymentDTO paymentPeople = sqlSession.selectOne("mypage.getLastPaymentPEOPLE",param);
		
		String proPayment = new String();
		if (paymentPro != null) {
			proPayment = sqlSession.selectOne("mypage.getEstimate", paymentPro.getEstimateNumber());
		} else {
			proPayment = null;
		}
		String requestNumber = new String();
		String peoplePayment = new String();
		if (paymentPeople != null) {
		requestNumber = sqlSession.selectOne("mypage.getRequestNumber", paymentPeople.getEstimateNumber());
		peoplePayment = sqlSession.selectOne("mypage.getRequest", param.get("id"));
		} else {
			requestNumber = null;
			peoplePayment = null;
		}
		
		dto.setStoreImageName(peopleDTO.getStoreImageName());
		dto.setOriginImageName(peopleDTO.getOriginImageName());
		dto.setImagePath(peopleDTO.getImagePath());
		dto.setNickname(peopleDTO.getNickname());
		dto.setIntro(peopleDTO.getIntro());
		dto.setPaymentPro(proPayment);
		dto.setPaymentPeople(peoplePayment);
		dto.setEstimate(sqlSession.selectOne("mypage.getLastEstimate", param));
		dto.setRequest(sqlSession.selectOne("mypage.getLastRequest", param));
		dto.setReport(sqlSession.selectOne("mypage.getLastReport", param));
		dto.setReviewPro(sqlSession.selectOne("mypage.getLastReviewPRO", param));
		dto.setReviewPeople(sqlSession.selectOne("mypage.getLastReviewPEOPLE", param));
		dto.setInquiry(sqlSession.selectOne("mypage.getLastInquiry", param));
		
		System.out.println(dto);
		return dto;
	}

	// 프로필 사진 변경
	public int updateProfileImage(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {

			result = sqlSession.update("people.updateProfileImage", param);

			transactionManager_sample.commit(status);
			logger.info("========== 프로필 사진 변경 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] updateProfileImage() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

}
