package Talan.controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
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

import Talan.DTO.AdminDTO;
import Talan.DTO.MyPageDTO;
import Talan.DTO.PeopleDTO;
import Talan.service.admin.AdminService;
import Talan.service.people.PeopleService;
import Talan.service.pro.ProService;
import kr.msp.constant.Const;

@Controller
public class PeopleController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = true)
	@Qualifier("sqlSession_sample")
	private SqlSession sqlSession;

	@Autowired(required = true)
	private PeopleService service;

	@Autowired(required = true)
	private ProService proService;

	@Autowired(required = true)
	private AdminService adminService;

	// 회원정보
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/info")
	public ModelAndView getpeopleInfo(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		PeopleDTO info = service.getPeopleInfo(reqBodyMap);

		int proRegisted = proService.isProRegisted(info.getPeopleId());
		boolean isPro = false;

		if (proRegisted == 1) {
			isPro = true;
		}

		if (!StringUtils.isEmpty(info)) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("name", info.getName());
			responseBodyMap.put("nickname", info.getNickname());
			responseBodyMap.put("address", info.getAddress());
			responseBodyMap.put("phone", info.getPhone());
			responseBodyMap.put("birth", info.getBirth());
			responseBodyMap.put("gender", info.getGender());
			responseBodyMap.put("intro", info.getIntro());
			responseBodyMap.put("email", info.getEmail());
			responseBodyMap.put("account", info.getAccount());
			responseBodyMap.put("originImageName", info.getOriginImageName());
			responseBodyMap.put("storeImageName", info.getStoreImageName());
			responseBodyMap.put("imagePath", info.getImagePath());
			responseBodyMap.put("isProRegisted", isPro);

		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 회원가입 (이미지X)
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/join")
	public ModelAndView regPeople(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		int result = service.insertPeople(reqBodyMap);

		if (result > 0) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
		} else {

			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);
		return mv;
	}

	// 회원가입 (이미지)
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/joinWithImage")
	public ModelAndView regPeople(MultipartHttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = new HashMap<String, Object>();
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		reqBodyMap.put("peopleId", request.getParameter("peopleId"));
		reqBodyMap.put("password", request.getParameter("password"));
		reqBodyMap.put("name", request.getParameter("name"));
		reqBodyMap.put("birth", request.getParameter("birth"));
		reqBodyMap.put("nickname", request.getParameter("nickname"));
		reqBodyMap.put("address", request.getParameter("address"));
		reqBodyMap.put("phone", request.getParameter("phone"));
		reqBodyMap.put("intro", request.getParameter("intro"));
		reqBodyMap.put("gender", request.getParameter("gender"));
		reqBodyMap.put("email", request.getParameter("email"));
		reqBodyMap.put("account", request.getParameter("account"));

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

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

		String fileDir = "/image/profileImage/";
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

		reqBodyMap.put("storeImageName", storeFileName);
		reqBodyMap.put("originImageName", originalFile);
		reqBodyMap.put("imagePath", server + fileDir);

		//////////////////////////// IMAGE UPLOAD////////////////////////////

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		int result = service.insertPeopleWithImage(reqBodyMap);

		if (result > 0) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 회원정보수정
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/update")
	public ModelAndView updatePeople(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

			int result = service.updatePeople(reqBodyMap);

			if (result > 0) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		} else {
			responseBodyMap.put("rsltCode", "1003");
			responseBodyMap.put("rsltMsg", "Login required.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 프로필 사진 변경
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/updateProfileImage")
		public ModelAndView updateProfileImage(MultipartHttpServletRequest request, HttpServletResponse response,
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

				reqBodyMap.put("peopleId", user.get("loginId"));
				
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

				String fileDir = "/image/profileImage/";
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

				reqBodyMap.put("storeImageName", storeFileName);
				reqBodyMap.put("originImageName", originalFile);
				reqBodyMap.put("imagePath", server + fileDir);

				//////////////////////////// IMAGE UPLOAD////////////////////////////

				logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

				/*
				 * body에 들어가는 값은 proId, feedNumber, feedTitle, feedContent proId는 세션으로 미리 put
				 * 해주었고, feedNumber는 프론트에서 버튼 처리할 때 data.param으로 넘겨주도록 합시다.
				 */

				int result = service.updateProfileImage(reqBodyMap);

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

	// 내 소개 수정
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/updateIntro")
	public ModelAndView updateIntro(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");

		user = (Map<String, Object>) session.getAttribute("user");

		if (user != null) {

			reqBodyMap.put("peopleId", user.get("loginId").toString());

			logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

			int result = service.updateIntro(reqBodyMap);

			if (result == 1) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
			} else {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				responseBodyMap.put("existYn", "N");
			}
		} else {
			responseBodyMap.put("rsltCode", "1003");
			responseBodyMap.put("rsltMsg", "Login required.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;

	}

	// 회원탈퇴
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/out")
	public ModelAndView deletepeople(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);
		
		Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");
		System.out.println(user);
		reqBodyMap.put("id", user.get("loginId").toString());

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		int result = service.deletePeople(reqBodyMap);

		if (result > 0) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			session.removeAttribute("user");
			session.invalidate();
		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 로그인
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/login")
	public ModelAndView loginpeople(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		Map<String, String> user = new HashMap<String, String>();

		if (reqBodyMap.get("peopleId").toString().equals("admin")) {
			int result = adminService.loginAdmin(reqBodyMap);
			if (result == 1) {
				AdminDTO DTO = sqlSession.selectOne("admin.loginAdmin", reqBodyMap);
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				responseBodyMap.put("session", DTO);
				user.put("loginId", DTO.getAdminId());
				user.put("password", DTO.getAdminPassword());
				session.setAttribute("user", user);
			} else if (result == -1) {
				responseBodyMap.put("rsltCode", "2001");
				responseBodyMap.put("rsltMsg", "ID or PWD not correct");
			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		} else {
			PeopleDTO DTO = sqlSession.selectOne("people.getPeopleInfo", reqBodyMap);

			int result = service.loginPeople(reqBodyMap);
			int proRegisted = proService.isProRegisted(DTO.getPeopleId());
			boolean isPro = false;

			if (proRegisted == 1) {
				isPro = true;
			}

			if (result == 1) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				user.put("loginId", DTO.getPeopleId());
				user.put("password", DTO.getPassword());
				session.setAttribute("user", user);
				responseBodyMap.put("session", DTO);
				responseBodyMap.put("isProRegisted", isPro);

			} else if (result == -1) {
				responseBodyMap.put("rsltCode", "2001");
				responseBodyMap.put("rsltMsg", "ID or PWD not correct");
			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		}
		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;

	}

	// 로그아웃
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/logout")
	public ModelAndView logoutPeople(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
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
			responseBodyMap.put("logout", user.get("loginId").toString() + ", 로그아웃 성공");
			session.removeAttribute("user");
			session.invalidate();
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 아이디찾기
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/findId")
	public ModelAndView findId(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		String info = service.findId(reqBodyMap);

		if (!StringUtils.isEmpty(info)) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("loginId", info);
		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 본인인증
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/find")
	public ModelAndView ispeople(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		Integer info = service.isPeople(reqBodyMap);

		if (info == 1) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("existYn", "Y");
		} else {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("existYn", "N");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;

	}

	// 비밀번호변경
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/password")
	public ModelAndView changePWD(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		int result = service.changePWD(reqBodyMap);

		if (result > 0) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
		} else {
			responseBodyMap.put("rsltCode", "2003");
			responseBodyMap.put("rsltMsg", "Data not found.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 아이디중복확인
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/duplicate1")
	public ModelAndView duplicatepeople1(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		int result = service.duplicatePeople1(reqBodyMap);

		if (result > 0) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("existYn", "Y");
		} else {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("existYn", "N");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 이메일 중복확인
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/duplicate2")
	public ModelAndView duplicatepeople2(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		int result = service.duplicatePeople2(reqBodyMap);

		if (result > 0) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("existYn", "Y");
		} else {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("existYn", "N");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 닉네임 중복확인
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/duplicate3")
	public ModelAndView duplicatepeople3(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		int result = service.duplicatePeople3(reqBodyMap);

		if (result > 0) {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("existYn", "Y");
		} else {
			responseBodyMap.put("rsltCode", "0000");
			responseBodyMap.put("rsltMsg", "Success");
			responseBodyMap.put("existYn", "N");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

	// 마이 페이지
	@RequestMapping(method = RequestMethod.POST, value = "/api/people/myPage")
	public ModelAndView myPage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> reqHeadMap = (Map<String, Object>) request.getAttribute(Const.HEAD);
		Map<String, Object> reqBodyMap = (Map<String, Object>) request.getAttribute(Const.BODY);
		Map<String, Object> responseBodyMap = new HashMap<String, Object>();

		if (reqHeadMap == null) {
			reqHeadMap = new HashMap<String, Object>();
		}

		reqHeadMap.put(Const.RESULT_CODE, Const.OK);
		reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS);

		Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");

		reqBodyMap.put("id", user.get("loginId"));
		reqBodyMap.put("isPro", proService.isProRegisted(user.get("loginId").toString()));

		logger.info("======================= reqBodyMap : {}", reqBodyMap.toString());

		if (session.getAttribute("user") != null) {
			MyPageDTO dto = service.myPage(reqBodyMap);

			if (!StringUtils.isEmpty(dto)) {
				responseBodyMap.put("rsltCode", "0000");
				responseBodyMap.put("rsltMsg", "Success");
				responseBodyMap.putAll(dto.getMyPage());

			} else {
				responseBodyMap.put("rsltCode", "2003");
				responseBodyMap.put("rsltMsg", "Data not found.");
			}
		} else {
			responseBodyMap.put("rsltCode", "1003");
			responseBodyMap.put("rsltMsg", "Login required.");
		}

		ModelAndView mv = new ModelAndView("defaultJsonView");
		mv.addObject(Const.HEAD, reqHeadMap);
		mv.addObject(Const.BODY, responseBodyMap);

		return mv;
	}

}
