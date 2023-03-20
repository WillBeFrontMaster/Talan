package Talan.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;

import Talan.DTO.PaymentDetailDTO;
import Talan.DTO.ReviewDTO;
import Talan.service.review.ReviewService;
import kr.msp.constant.Const;

@Controller
public class ReviewController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	private ReviewService service;

	// 리뷰 등록
	@RequestMapping(method = RequestMethod.POST, value = "/api/review/regist")
	public ModelAndView registReview(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			PaymentDetailDTO payment = sqlSession.selectOne("payment.getPayment", reqBodyMap.get("paymentNumber"));
			reqBodyMap.put("peopleId", user.get("loginId"));
			reqBodyMap.put("proId", payment.getProId());

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			int result = service.registReview(reqBodyMap);

			if (result > 0) {
				sqlSession.update("pro.updateProKindScore", reqBodyMap.get("proId"));
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

	// 리뷰 답변
	@RequestMapping(method = RequestMethod.POST, value = "/api/review/response")
	public ModelAndView responseReview(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			int result = service.responseReview(reqBodyMap);

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

	// 리뷰 리스트 (PEOPLE 기준)
	@RequestMapping(method = RequestMethod.POST, value = "/api/review/list")
	public ModelAndView reviewList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			reqBodyMap.put("peopleId", user.get("loginId"));

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			List<Object> review = service.reviewList(reqBodyMap);

			if (!StringUtils.isEmpty(review)) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				responseBodyMap.put("list", review);
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

	// 리뷰 리스트 (PRO 기준)
	@RequestMapping(method = RequestMethod.POST, value = "/api/review/proList")
	public ModelAndView reviewProList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			List<Object> review = service.reviewMyList(reqBodyMap);

			if (!StringUtils.isEmpty(review)) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				responseBodyMap.put("list", review);
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

	// 리뷰 상세 보기
	@RequestMapping(method = RequestMethod.POST, value = "/api/review/detail")
	public ModelAndView reviewDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			ReviewDTO review = service.reviewDetail(reqBodyMap);
			String peopleNickname = sqlSession.selectOne("people.getNickname", review.getPeopleId());
			String proNickname = sqlSession.selectOne("people.getNickname", review.getProId());

			if (!StringUtils.isEmpty(review)) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				responseBodyMap.put("reviewNumber", review.getReviewNumber());
				responseBodyMap.put("paymentNumber", review.getPaymentNumber());
				responseBodyMap.put("peopleId", review.getPeopleId());
				responseBodyMap.put("reviewTitle", review.getReviewTitle());
				responseBodyMap.put("reviewContent", review.getReviewContent());
				responseBodyMap.put("starPoint", review.getStarPoint());
				responseBodyMap.put("proId", review.getProId());
				responseBodyMap.put("reviewResponse", review.getReviewResponse());
				responseBodyMap.put("reviewDate", review.getReviewDate());
				responseBodyMap.put("peopleNickname", peopleNickname);
				responseBodyMap.put("proNickname", proNickname);
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
