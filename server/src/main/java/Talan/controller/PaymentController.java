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

import Talan.DTO.EstimateDTO;
import Talan.DTO.PaymentDetailDTO;
import Talan.service.payment.PaymentService;
import Talan.service.pro.ProService;
import kr.msp.constant.Const;

@Controller
public class PaymentController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	private PaymentService service;
	
	@Autowired(required = true)
	private ProService proService;

	// 결제
	@RequestMapping(method = RequestMethod.POST, value = "/api/payment/insert")
	public ModelAndView paymentInsert(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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
			EstimateDTO estimate = sqlSession.selectOne("estimate.getEstimate", reqBodyMap.get("estimateNumber"));
			reqBodyMap.put("proId", estimate.getProId());
			
			
			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			int result = service.paymentInsert(reqBodyMap);

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
	
	// 결제 취소
	@RequestMapping(method = RequestMethod.POST, value = "/api/payment/cancel")
	public ModelAndView paymenCancel(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			int result = service.paymenCancel(reqBodyMap);

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
	
	// 결제리스트 조회
	@RequestMapping(method = RequestMethod.POST, value = "/api/payment/myList")
	public ModelAndView paymentList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			List<Object> list = service.paymentList(reqBodyMap);

			if (!StringUtils.isEmpty(list)) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				responseBodyMap.put("list", list);
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
	
	
	// 결제 상세 조회
	@RequestMapping(method = RequestMethod.POST, value = "/api/payment/detail")
	public ModelAndView paymentDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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
			String id =user.get("loginId").toString();
			
			int proRegisted = proService.isProRegisted(id);
			boolean isPro = false;

			if (proRegisted == 1) {
				isPro = true;
			}
			
			if (isPro == true) {
				reqBodyMap.put("proId", user.get("loginId"));
			} else {
			reqBodyMap.put("peopleId", user.get("loginId"));
			}
			
			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			PaymentDetailDTO dto = service.paymentDetail(reqBodyMap);

			if (!StringUtils.isEmpty(dto)) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				responseBodyMap.put("paymentNumber", dto.getPaymentNumber());
				responseBodyMap.put("estimateNumber", dto.getEstimateNumber());
				responseBodyMap.put("paymentPrice", dto.getPaymentPrice());
				responseBodyMap.put("paymentDate", dto.getPaymentDate());
				responseBodyMap.put("paymentCancleDate", dto.getPaymentCancelDate());
				responseBodyMap.put("paymentType", dto.getPaymentType());
				responseBodyMap.put("progressiveStatus", dto.getProgressiveStatus());
				responseBodyMap.put("estimateTitle", dto.getEstimateTitle());
				responseBodyMap.put("estimateContent", dto.getEstimateContent());
				responseBodyMap.put("requestTitle", dto.getRequestTitle());
				responseBodyMap.put("requestContent", dto.getRequestContent());
				responseBodyMap.put("nickname", dto.getNickname());
				responseBodyMap.put("reviewStatus", dto.getReviewStatus());
				responseBodyMap.put("reviewNumber", dto.getReviewNumber());
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
