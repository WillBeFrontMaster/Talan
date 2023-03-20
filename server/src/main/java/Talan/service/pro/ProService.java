package Talan.service.pro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import Talan.DTO.ProDTO;

@Service
public class ProService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	@Qualifier("transactionManager_sample")
	private DataSourceTransactionManager transactionManager_sample;

	// PRO 등록
	public int registPro(Map<String, Object> param) {
		// 트랜잭션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);

		int result = 0;
		try {
			result = sqlSession.insert("pro.registPro", param);

			transactionManager_sample.commit(status);
			logger.info("========== 고수 등록 완료 : {}", result);

		} catch (Exception e) {
			logger.error("[ERROR] registPro() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);
		}
		return result;
	}

	// PRO 확인
	public int isProRegisted(String peopleId) {
		int result = sqlSession.selectOne("pro.isProRegisted", peopleId);
		return result;
	}

	// 고수 조회
	public ProDTO getProInfo(Map<String, Object> param) {
		ProDTO info = sqlSession.selectOne("pro.getProInfo", param);

		return info;
	}

	// 자격증 검색
	public List<Object> searchLicense(Map<String, Object> param) {
		String key = "agGt5F7wZcUG8s4DRqYg2ZiZ8tsxY53FMKGJ2PKkpUx9ovoAFj6Q8PxDpE7OX7qyd2VT4bPZGRvR5VitFckRHw%3D%3D";
		List<Object> searchList = new ArrayList<Object>();

		try {
			URL url = new URL(
					"http://api.odcloud.kr/api/15082998/v1/uddi:c357330a-6fe8-406c-9a50-c34e18cca518?page=1&perPage=634&serviceKey="
							+ key);

			String line = "";
			String result = "";

			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((line = br.readLine()) != null) {
				result = result.concat(line);
			}

			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(result);

			JSONArray data = (JSONArray) obj.get("data");
			
			for (int i = 0; i < data.size(); i++) {
				JSONObject list = (JSONObject) data.get(i);
				String type = (String) list.get("계열명");
				String name = (String) list.get("종목명");

				if (name.contains(param.get("searchLicense").toString())) {
					Map<String, String> license = new HashMap<String, String>();
					license.put("licenseType", type);
					license.put("licenseName", name);
					searchList.add(license);
				}
			}
			br.close();
		} catch (Exception e) { e.printStackTrace(); }

		return searchList;
	}
}
