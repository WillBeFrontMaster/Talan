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

import Talan.DTO.FeedDTO;

@Service
public class FeedService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	// Feed 등록
	public int registFeed(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.insert("feed.registFeed", param);

			transactionManager_sample.commit(status);
			logger.info("========== 피드 등록 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] registFeed() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// Feed 등록 (with Image)
	public int registFeedWithImage(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.insert("feed.registFeedWithImage", param);

			transactionManager_sample.commit(status);
			logger.info("========== 피드 등록 (이미지 포함) 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] registFeed() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// FEED 수정
	public int updateFeed(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.update("feed.updateFeed", param);

			transactionManager_sample.commit(status);
			logger.info("========== 피드 수정 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] updateFeed() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// Feed 수정 (with Image)
	public int updateFeedWithImage(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.update("feed.updateFeedWithImage", param);

			transactionManager_sample.commit(status);
			logger.info("========== 피드 수정(이미지) 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] updateFeedWithImage() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// FEED 전체 조회
	public List<Object> feedList(Map<String, Object> param) {
		List<FeedDTO> feed = new ArrayList<FeedDTO>();

		if (param.get("lastFeedNumber").equals("0")) {
			param.replace("lastFeedNumber", "0", sqlSession.selectOne("feed.getLastFeedNumber"));
		} else {
			String strLastFeedNumber = param.get("lastFeedNumber").toString();
			int lastFeedNumber = Integer.parseInt(strLastFeedNumber.substring(4)) - 1;
			param.replace("lastFeedNumber", "FEED" + lastFeedNumber);
		}

		int feedCnt = sqlSession.selectOne("feed.getFeedCnt");
		if ((Integer.parseInt(param.get("cnt").toString()) > feedCnt)
				|| Integer.parseInt(param.get("cnt").toString()) == 0) {
			param.replace("cnt", feedCnt);
		}

		feed = sqlSession.selectList("feed.getFeedList", param);

		int cnt = Integer.parseInt(param.get("cnt").toString()) - 1;

		
		
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i <= cnt; i++) {
			list.add(feed.get(i).getFeedList());
		}
		return list;
	}

	// PRO 개인 피드 조회
	public List<Object> feedProList(Map<String, Object> param) {
		List<FeedDTO> feed = new ArrayList<FeedDTO>();

		feed = sqlSession.selectList("feed.getFeedProList", param);

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < feed.size(); i++) {
			list.add(feed.get(i).getFeedList());
		}
		return list;
	}

	// FEED 상세 조회
	public FeedDTO detailFeed(Map<String, Object> param) {
		FeedDTO feed = sqlSession.selectOne("feed.getFeed", param);
		return feed;
	}

	// FEED 삭제
	public int deleteFeed(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			if (param.get("proId").equals("admin")) {
				result = sqlSession.delete("feed.adminDeleteFeed", param);
			} else {
				result = sqlSession.delete("feed.deleteFeed", param);
			}
			if (result > 0) {
				sqlSession.delete("feed.deleteFeedComments", param);
			}
			transactionManager_sample.commit(status);
			logger.info("========== 피드 삭제 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] deleteFeed() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// 인기 피드 가져오기
	public List<Object> popularFeeds() {
		List<Object> popularFeeds = new ArrayList<Object>();
		List<String> feedNumber = new ArrayList<>();
		feedNumber = sqlSession.selectList("feedComments.getPopularFeed");
		System.out.println("::::::::::::::::::::"+feedNumber);
		for (int i = 0; i < feedNumber.size(); i++) {
			Map<String, Object> popularFeed = new HashMap<String, Object>();
			FeedDTO dto = new FeedDTO();
			dto = sqlSession.selectOne("feed.getFeed", feedNumber.get(i));
			System.out.println(":::::::::::::"+feedNumber.get(i));
			popularFeed.put("feedNumber", feedNumber.get(i));
			popularFeed.put("feedTitle", dto.getFeedTitle());
			popularFeed.put("commentsCount", sqlSession.selectOne("feedComments.getCommentsCount", feedNumber.get(i)));
			popularFeed.put("storeFileName", dto.getStoreFileName());
			popularFeed.put("originFileName", dto.getOriginFileName());
			popularFeed.put("filePath", dto.getFilePath());
			popularFeeds.add(i, popularFeed);
		}
		return popularFeeds;
	}

}
