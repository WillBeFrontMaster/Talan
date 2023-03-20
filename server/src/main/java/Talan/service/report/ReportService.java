package Talan.service.report;

import java.util.ArrayList;
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

import Talan.DTO.ReportDTO;

@Service
public class ReportService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	// 신고하기
	public int resportRegist(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.insert("report.registReport", param);

			transactionManager_sample.commit(status);
			logger.info("========== 신고 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] resportRegist() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 신고 리스트
	public List<Object> reportMyList(Map<String, Object> param) {
		List<ReportDTO> report = new ArrayList<ReportDTO>();

		report = sqlSession.selectList("report.getReportMyList", param);

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < report.size(); i++) {
			list.add(report.get(i).getReportList());
		}
		return list;
	}

	// 신고 상세 내역
	public ReportDTO reportDetail(Map<String, Object> param) {
		ReportDTO dto = new ReportDTO();
		dto = sqlSession.selectOne("report.getReport", param);
		return dto;
	}

}
