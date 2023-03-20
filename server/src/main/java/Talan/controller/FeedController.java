package Talan.controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import Talan.DTO.FeedDTO;
import Talan.DTO.PeopleDTO;
import Talan.service.feed.FeedService;
import kr.msp.constant.Const;

@Controller
public class FeedController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	private FeedService service;

	// FEED 등록
	@RequestMapping(method = RequestMethod.POST, value = "/api/feed/regist")
	public ModelAndView registFeed(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		if (session.getAttribute("user") != null) {
			Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");

			PeopleDTO peopleDTO = sqlSession.selectOne("people.getPeopleInfo", user.get("loginId"));

			reqBodyMap.put("proId", user.get("loginId"));
			reqBodyMap.put("feedWriterNickname", peopleDTO.getNickname());

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			int result = service.registFeed(reqBodyMap);

			if (result > 0) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		} else if (session.getAttribute("user") == null) {
			responseBodyMap.put("rsltCode", "1003");
			responseBodyMap.put("rsltMsg", "Login required.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// FEED 등록 (with Image)
	@RequestMapping(method = RequestMethod.POST, value = "/api/feed/registWithImage")
	public ModelAndView registFeedWithImage(MultipartHttpServletRequest request, HttpServletResponse response,
			HttpSession session) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = new HashMap<String, Object>();
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		if (session.getAttribute("user") != null) {
			Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");

			PeopleDTO peopleDTO = sqlSession.selectOne("people.getPeopleInfo", user.get("loginId"));
			System.out.println();

			reqBodyMap.put("proId", user.get("loginId"));
			reqBodyMap.put("feedWriterNickname", peopleDTO.getNickname());
			reqBodyMap.put("feedTitle", request.getParameter("feedTitle"));
			reqBodyMap.put("feedContent", request.getParameter("feedContent"));

			//////////////////////////// IMAGE UPLOAD////////////////////////////
			
			InetAddress addr = null;
			try {
				addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			String ip = addr.getHostAddress();
			String http = "http://";
			String port = ":8888";
			String server = http + ip + port;

			String fileDir = "/image/feedImage/";
			String filePath = request.getServletContext().getRealPath(fileDir);
			System.out.println(filePath);
			MultipartFile image = request.getFile("image");
			String originalFile = image.getOriginalFilename();

			// .png
			String extension = originalFile.substring(originalFile.lastIndexOf("."));

			// 7b2582aca35e4525b4a579d84e8b6c9d
			String storeName = UUID.randomUUID().toString().replace("-", "");

			String storeFileName = storeName + extension;

			File file = new File(filePath + storeFileName);
			try {
				image.transferTo(file); // 파일을 저장
			} catch (Exception e) {
				e.printStackTrace();
			}

			reqBodyMap.put("storeFileName", storeFileName);
			reqBodyMap.put("originFileName", originalFile);
			reqBodyMap.put("filePath", server+fileDir);

			//////////////////////////// IMAGE UPLOAD////////////////////////////

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			int result = service.registFeedWithImage(reqBodyMap);

			if (result > 0) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		} else if (session.getAttribute("user") == null) {
			responseBodyMap.put("rsltCode", "1003");
			responseBodyMap.put("rsltMsg", "Login required.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// FEED 전체 조회
	@RequestMapping(method = RequestMethod.POST, value = "/api/feed/list")
	public ModelAndView feedList(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		/*
		 * "body" : { "lastFeedNumber" : "첫 번째에 보여질 피드 번호 , 0이면 최신순 ", "cnt" :
		 * "가져올 피드의 갯수, 0이거나 전체피드 개수보다 큰 값이 입력되면 전체 피드를 가져온다"
		 */

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		List<Object> list = service.feedList(reqBodyMap);

		Map<String, Object> lastFeed = new HashMap<String, Object>();
		lastFeed = (Map<String, Object>) list.get(Integer.parseInt(reqBodyMap.get("cnt").toString()) - 1);

		if (!StringUtils.isEmpty(list)) {
			List<Object> popularFeeds = service.popularFeeds();
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("lastFeedNumber", lastFeed.get("feedNumber"));
			responseBodyMap.put("popularFeeds", popularFeeds);
			responseBodyMap.put("list", list);
		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// PRO 개인 피드 조회
	@RequestMapping(method = RequestMethod.POST, value = "/api/feed/proList")
	public ModelAndView feedProList(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		List<Object> list = service.feedProList(reqBodyMap);

		if (!StringUtils.isEmpty(list)) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("proFeedCount", sqlSession.selectOne("feed.getFeedProCnt", reqBodyMap));
			responseBodyMap.put("list", list);
		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// FEED 상세 조회
	@RequestMapping(method = RequestMethod.POST, value = "/api/feed/detail")
	public ModelAndView detailFeed(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		FeedDTO feed = service.detailFeed(reqBodyMap);

		if (!StringUtils.isEmpty(feed)) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("feedNumber", feed.getFeedNumber());
			responseBodyMap.put("feedTitle", feed.getFeedTitle());
			responseBodyMap.put("feedContent", feed.getFeedContent());
			responseBodyMap.put("feedWriterNickname", feed.getFeedWriterNickname());
			responseBodyMap.put("feedRegisterDate", feed.getFeedRegisterDate());
			responseBodyMap.put("storeFileName", feed.getStoreFileName());
			responseBodyMap.put("originFileName", feed.getOriginFileName());
			responseBodyMap.put("filePath", feed.getFilePath());
			responseBodyMap.put("feedWriterId", feed.getProId());

		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// FEED 수정
	@RequestMapping(method = RequestMethod.POST, value = "/api/feed/update")
	public ModelAndView updateFeed(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		if (session.getAttribute("user") != null) {

			Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");

			reqBodyMap.put("proId", user.get("loginId"));

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			/*
			 * body에 들어가는 값은 proId, feedNumber, feedTitle, feedContent proId는 세션으로 미리 put
			 * 해주었고, feedNumber는 프론트에서 버튼 처리할 때 data.param으로 넘겨주도록 합시다.
			 */

			int result = service.updateFeed(reqBodyMap);

			if (result > 0) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		} else if (session.getAttribute("user") == null) {
			responseBodyMap.put("rsltCode", "1003");
			responseBodyMap.put("rsltMsg", "Login required.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// FEED 수정 (with Image)
	@RequestMapping(method = RequestMethod.POST, value = "/api/feed/updateWithImage")
	public ModelAndView updateFeedWithImage(MultipartHttpServletRequest request, HttpServletResponse response,
			HttpSession session) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = new HashMap<String, Object>();
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		if (session.getAttribute("user") != null) {
			Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");

			reqBodyMap.put("proId", user.get("loginId"));
			reqBodyMap.put("feedNumber", request.getParameter("feedNumber"));
			reqBodyMap.put("feedTitle", request.getParameter("feedTitle"));
			reqBodyMap.put("feedContent", request.getParameter("feedContent"));
			
			System.out.println("BODY ::::::::: " + reqBodyMap);

			//////////////////////////// IMAGE UPLOAD////////////////////////////
			
			InetAddress addr = null;
			try {
				addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			String ip = addr.getHostAddress();
			String http = "http://";
			String port = ":8888";
			String server = http + ip + port;

			String fileDir = "/image/feedImage/";
			String filePath = request.getServletContext().getRealPath(fileDir);
			System.out.println(filePath);
			MultipartFile image = request.getFile("image");
			String originalFile = image.getOriginalFilename();

			// .png
			String extension = originalFile.substring(originalFile.lastIndexOf("."));

			// 7b2582aca35e4525b4a579d84e8b6c9d
			String storeName = UUID.randomUUID().toString().replace("-", "");

			String storeFileName = storeName + extension;

			File file = new File(filePath + storeFileName);
			try {
				image.transferTo(file); // 파일을 저장
			} catch (Exception e) {
				e.printStackTrace();
			}

			reqBodyMap.put("storeFileName", storeFileName);
			reqBodyMap.put("originFileName", originalFile);
			reqBodyMap.put("filePath", server+fileDir);

			//////////////////////////// IMAGE UPLOAD////////////////////////////

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			/*
			 * body에 들어가는 값은 proId, feedNumber, feedTitle, feedContent proId는 세션으로 미리 put
			 * 해주었고, feedNumber는 프론트에서 버튼 처리할 때 data.param으로 넘겨주도록 합시다.
			 */

			int result = service.updateFeedWithImage(reqBodyMap);

			if (result > 0) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		} else if (session.getAttribute("user") == null) {
			responseBodyMap.put("rsltCode", "1003");
			responseBodyMap.put("rsltMsg", "Login required.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// FEED 삭제
	@RequestMapping(method = RequestMethod.POST, value = "/api/feed/delete")
	public ModelAndView deleteFeed(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");
		
		if (session.getAttribute("user") != null) {
			

			reqBodyMap.put("proId", user.get("loginId"));

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			/*
			 * feedNumber는 프론트에서 버튼 처리할 때 data.param으로 넘겨주도록 합시다.
			 */

			int result = service.deleteFeed(reqBodyMap);

			if (result > 0) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		} else if (session.getAttribute("user") == null) {
			responseBodyMap.put("rsltCode", "1003");
			responseBodyMap.put("rsltMsg", "Login required.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}
}