package com.example.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.soap.providers.com.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.example.dto.SampleUserDto;
import com.example.dto.UserDto;

@Service
public class TestService {

	private Logger logger = LoggerFactory.getLogger(TestService.class);
	
	@Autowired(required=true)
    @Qualifier("sqlSession_sample")  ///WEB-INF/config/context/sample-mybatis-context.xml파일에서 설정한 DB 연결세션
    private SqlSession sqlSession;

    @Autowired(required=true)
    @Qualifier("transactionManager_sample")
    private DataSourceTransactionManager transactionManager_sample;
	
    // 리스트조회
	public List<UserDto> getUserList( Map<String,Object> paramMap ){
		
		List<UserDto> list = sqlSession.selectList("Test.getUserList", paramMap);
		logger.info("================= list.size() : {}", list.size() );
		return list;
	}
	
	// 상세조회
	public UserDto getUserInfo( Map<String,Object> paramMap ){		
		UserDto info = sqlSession.selectOne("Test.getUserInfo", paramMap);
//		logger.info("================= info.toString() : {}", info.toString() );
		return info;
	}
	
	// 유저 등록
	public int insertUser( Map<String,Object> paramMap ){		
		//트렌젝션 구현
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager_sample.getTransaction(def);

        int result = 0;
        try{
           
            UserDto userDto = new UserDto();
            userDto.setUserId( (String) paramMap.get("userId") );
            userDto.setUserName( (String) paramMap.get("userName") );
            userDto.setAge( (int) paramMap.get("age") );
            result = sqlSession.insert("Test.insertUser", userDto);
            
            transactionManager_sample.commit(status);

            logger.info("========== 유저 등록 완료 : {}", result);
            
        }catch(Exception e){
        	logger.error("[ERROR] insertUser() Fail : e : {}", e.getMessage());
        	e.printStackTrace();
        	transactionManager_sample.rollback(status);    	
        }
		return result;
	}
	
	// 유저정보 수정
	public int updateUser( Map<String,Object> paramMap ){		
		//트렌젝션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);
		
		int result = 0;
		try{
			
			UserDto userDto = new UserDto();
			userDto.setUserId( (String) paramMap.get("userId") );
			userDto.setUserName( (String) paramMap.get("userName") );
			userDto.setAge( (int) paramMap.get("age") );
			result = sqlSession.update("Test.updateUser", userDto);
			
			transactionManager_sample.commit(status);
			
			logger.info("========== 유저 수정 완료 : {}", result);
			
		}catch(Exception e){
			logger.error("[ERROR] updateUser() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);    	
		}
		return result;
	}
	
	// 유저정보 삭제
	public int deleteUser( Map<String,Object> paramMap ){		
		//트렌젝션 구현
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager_sample.getTransaction(def);
		
		int result = 0;
		try{
			
			UserDto userDto = new UserDto();
			userDto.setUserId( (String) paramMap.get("userId") );
			result = sqlSession.delete("Test.deleteUser", userDto);
			
			transactionManager_sample.commit(status);
			
			logger.info("========== 유저 삭제 완료 : {}", result);
			
		}catch(Exception e){
			logger.error("[ERROR] deleteUser() Fail : e : {}", e.getMessage());
			e.printStackTrace();
			transactionManager_sample.rollback(status);    	
		}
		return result;
	}
	
}
