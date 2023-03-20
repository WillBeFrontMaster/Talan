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

import Talan.DTO.InquiryDTO;
import Talan.DTO.ResponseDTO;
import Talan.service.inquiry.InquiryService;
import Talan.service.response.ResponseService;
import kr.msp.constant.Const;

@Controller
public class InquiryController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	private InquiryService service;

	@Autowired(required = true)
	private ResponseService responseService;

	// 문의하기
	@RequestMapping(method = RequestMethod.POST, value = "/api/inquiry/regist")
	public ModelAndView registInquiry(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			int result = service.registInquiry(reqBodyMap);

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

	// 문의글 전체 조회
	@RequestMapping(method = RequestMethod.POST, value = "/api/inquiry/allList")
	public ModelAndView inquiryListAll(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		List<Object> list = service.inquiryListAll(reqBodyMap);

		Map<String, Object> lastInquiry = new HashMap<String, Object>();
		lastInquiry = (Map<String, Object>) list.get(Integer.parseInt(reqBodyMap.get("cnt").toString()) - 1);

		if (!StringUtils.isEmpty(list)) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("lastInquiryNumber", lastInquiry.get("inquiryNumber"));
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

	// 나의 문의 내역
	@RequestMapping(method = RequestMethod.POST, value = "/api/inquiry/personalList")
	public ModelAndView personalInquiryList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");

		if (user != null) {
			reqBodyMap.put("peopleId", user.get("loginId"));

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			List<Object> list = service.personalList(reqBodyMap);

			if (!StringUtils.isEmpty(list)) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				responseBodyMap.put("inquiryCount", sqlSession.selectOne("inquiry.getPersonalInquiryCnt", reqBodyMap));
				responseBodyMap.put("list", list);
			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		} else {
			responseBodyMap.put("rsltCode", "1003");
			responseBodyMap.put("rsltMsg", "login required.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 문의글 상세 조회
	@RequestMapping(method = RequestMethod.POST, value = "/api/inquiry/detail")
	public ModelAndView detailInquiry(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		InquiryDTO inquiry = service.detailInquiry(reqBodyMap);
		ResponseDTO responseDTO = responseService.responseInfo(reqBodyMap);

		if (!StringUtils.isEmpty(inquiry)) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("inquiryNumber", inquiry.getInquiryNumber());
			responseBodyMap.put("peopleId", inquiry.getPeopleId());
			responseBodyMap.put("inquiryTitle", inquiry.getInquiryTitle());
			responseBodyMap.put("inquiryContent", inquiry.getInquiryContent());
			responseBodyMap.put("inquiryRegisterDate", inquiry.getInquiryRegisterDate());
			responseBodyMap.put("inquiryModifyDate", inquiry.getInquiryModifyDate());
			responseBodyMap.put("inquirySecretStatus", inquiry.getSecretStatus());
			responseBodyMap.put("inquiryPassword", inquiry.getInquiryPassword());
			responseBodyMap.put("responseStatus", inquiry.getResponseStatus());

			if (!StringUtils.isEmpty(responseDTO)) {
				responseBodyMap.put("response", responseDTO);
			}
		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 문의글 수정
	@RequestMapping(method = RequestMethod.POST, value = "/api/inquiry/update")
	public ModelAndView updateInquiry(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			int result = service.updateInquiry(reqBodyMap);

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

	// 문의글 삭제
	@RequestMapping(method = RequestMethod.POST, value = "/api/inquiry/delete")
	public ModelAndView deleteInquiry(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			int result = service.deleteInquiry(reqBodyMap);

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
