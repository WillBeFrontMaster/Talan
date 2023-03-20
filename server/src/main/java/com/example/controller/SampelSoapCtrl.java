/*
 * package com.example.controller;
 * 
 * import kr.msp.constant.Const; import kr.msp.legacy.parser.SoapParser; import
 * org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.MessageSource; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.servlet.ModelAndView;
 * 
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse; import javax.xml.soap.*; import
 * java.util.HashMap; import java.util.Locale; import java.util.Map;
 * 
 *//**
	 * Created by Y.B.H(mium2) on 17. 4. 11..
	 *
	 * �꽕紐� : SOAP �꽌踰꾧� WSDL�쓣 �젣怨듯빐 以� 寃쎌슦�뒗 �븘�옒�쓽 諛⑸쾿�쓣 �궗�슜�븯吏� �븡�뒗�떎.
	 * $JAVA_HOME/bin/wsimport -s ./ http://soap�꽌踰꾩＜�냼/wsdl寃쎈줈?WSDL �씠�씪怨� 移섎㈃ �뿰寃�
	 * 諛� �궗�슜 �븷 �닔 �엳�뒗 Bean �뙆�씪�뱾�씠 �깮湲곌퀬 �빐�떦 �뙆�씪�쓣 �씠�슜�븯�뿬 鍮꾩쫰�땲�뒪 濡쒖쭅�쓣
	 * �닔�뻾 �븯硫대맂�떎.
	 */
/*
 * @Controller public class SampelSoapCtrl{ private Logger logger =
 * LoggerFactory.getLogger(this.getClass().getName());
 * 
 * @Autowired(required=true) private MessageSource messageSource;
 * 
 * @RequestMapping(value="/api/soapSample",produces =
 * "application/json; charset=utf8") public ModelAndView
 * sampleUseConfig(HttpServletRequest request, HttpServletResponse response){
 * /////////////////////////////////////////////////////////////////////////////
 * //////////////////////////////////////// // �겢�씪�씠�뼵�듃�뿉�꽌 �꽆�뼱�삩 request 媛�
 * map�쑝濡� 由ы꽩�빐以� (諛섎뱶�떆 �룷�븿)
 * /////////////////////////////////////////////////////////////////////////////
 * //////////////////////////////////////// //rest濡� �꽆�뼱�삩 URI Path VARIABLES
 * ATTRIBUTE 留듭젙蹂� Map<String,Object> uriPathVal =
 * (Map<String,Object>)request.getAttribute(Const.REST_URI_PATH_VAL);
 * //�겢�씪�씠�뼵�듃�뿉�꽌 �꽆�뼱�삩 request(HEAD+BODY) 紐⑤뱺�젙蹂� Map<String,Object> reqMap
 * = (Map<String,Object>)request.getAttribute(Const.HTTP_BODY); //�겢�씪�씠�뼵�듃�뿉�꽌
 * �꽆�뼱�삩 怨듯넻 �뿤�뜑 留듭젙蹂� Map<String,Object> reqHeadMap =
 * (Map<String,Object>)request.getAttribute(Const.HEAD); //�겢�씪�씠�뼵�듃�뿉�꽌 �꽆湲�
 * �뙆�씪誘명꽣 留듭젙蹂� Map<String,Object> reqBodyMap =
 * (Map<String,Object>)request.getAttribute(Const.BODY); //�겢�씪�씠�뼵�듃�뿉�꽌 �꽆湲�
 * Response 留� �꽭�똿 Map<String,Object> responseMap = new HashMap<String,
 * Object>(); Map<String, Object> responseBodyMap= new HashMap<String,
 * Object>(); if(reqHeadMap==null){ reqHeadMap = new HashMap<String, Object>();
 * } reqHeadMap.put(Const.RESULT_CODE, Const.OK);
 * reqHeadMap.put(Const.RESULT_MESSAGE, Const.SUCCESS); try{
 *//**************************************************************************************************
	 * �씠 遺�遺꾩뿉 鍮꾩쫰�땲�뒪 濡쒖쭅�쓣 肄붾뵫�븳�떎. 留뚯빟, �겢�씪�씠�뼵�듃�뿉 �뿉�윭泥섎━瑜� �븯怨� �떢�떎硫�
	 * responseMap.setResultCode(Const.EXCEPTION_ERROR); �궗�슜
	 **************************************************************************************************/
/*
 * // Create SOAP Connection SOAPConnectionFactory soapConnectionFactory =
 * SOAPConnectionFactory.newInstance(); SOAPConnection soapConnection =
 * soapConnectionFactory.createConnection();
 * 
 * // Send SOAP Message to SOAP Server String url =
 * "http://xxx.xxx.xxx.xxx/soapSample.do"; SOAPMessage soapResponse =
 * soapConnection.call(createSOAPRequest(), url);
 * 
 * // print SOAP Response System.out.print("Response SOAP Message:");
 * soapResponse.writeTo(System.out);
 * 
 * soapConnection.close();
 *//**************************************************************************************************
	 * �씠 遺�遺꾩뿉 鍮꾩쫰�땲�뒪 濡쒖쭅 留덉묠.
	 *************************************************************************************************//*
																										 * } catch
																										 * (Exception e)
																										 * { reqHeadMap.
																										 * put(Const.
																										 * RESULT_CODE,
																										 * Const.
																										 * EXCEPTION_ERROR
																										 * ); if(e.
																										 * getMessage()
																										 * != null){
																										 * reqHeadMap.
																										 * put(Const.
																										 * RESULT_MESSAGE
																										 * ,e.getMessage
																										 * ()); } else {
																										 * reqHeadMap.
																										 * put(Const.
																										 * RESULT_MESSAGE
																										 * ,
																										 * messageSource
																										 * .getMessage(
																										 * "500.error",
																										 * null ,
																										 * Locale.
																										 * getDefault().
																										 * ENGLISH )); }
																										 * }
																										 * responseMap.
																										 * put(Const.
																										 * HEAD,
																										 * reqHeadMap);
																										 * responseMap.
																										 * put(Const.
																										 * BODY,
																										 * responseBodyMap
																										 * );
																										 * ModelAndView
																										 * mv = new
																										 * ModelAndView(
																										 * "defaultJsonView"
																										 * ); mv.
																										 * addAllObjects
																										 * (responseMap)
																										 * ; return mv;
																										 * }
																										 * 
																										 * private
																										 * SOAPMessage
																										 * createSOAPRequest
																										 * () throws
																										 * Exception {
																										 * MessageFactory
																										 * messageFactory
																										 * =
																										 * MessageFactory
																										 * .newInstance(
																										 * );
																										 * SOAPMessage
																										 * soapMessage =
																										 * messageFactory
																										 * .
																										 * createMessage
																										 * (); SOAPPart
																										 * soapPart =
																										 * soapMessage.
																										 * getSOAPPart()
																										 * ;
																										 * 
																										 * String
																										 * serverURI =
																										 * "http://xxx.xxx.xxx.xxx/";
																										 * 
																										 * // SOAP
																										 * Envelope
																										 * SOAPEnvelope
																										 * envelope =
																										 * soapPart.
																										 * getEnvelope()
																										 * ; envelope.
																										 * addNamespaceDeclaration
																										 * ("example",
																										 * serverURI);
																										 * 
																										 * 
																										 * // �씤利� �뿰寃곗떆
																										 * �삁�젣
																										 * Constructed
																										 * SOAP Request
																										 * Message:
																										 * <SOAP-ENV:
																										 * Envelope
																										 * xmlns:SOAP-
																										 * ENV=
																										 * "http://schemas.xmlsoap.org/soap/envelope/"
																										 * xmlns:example
																										 * =
																										 * "http://xxx.xxx.xxx.xxx/">
																										 * <SOAP-ENV:
																										 * Header/>
																										 * <SOAP-ENV:
																										 * Body>
																										 * <example:
																										 * VerifyEmail>
																										 * <example:
																										 * email>
																										 * mutantninja@
																										 * gmail.com</
																										 * example:
																										 * email>
																										 * <example:
																										 * LicenseKey>
																										 * 123</example:
																										 * LicenseKey>
																										 * </example:
																										 * VerifyEmail>
																										 * </SOAP-ENV:
																										 * Body>
																										 * </SOAP-ENV:
																										 * Envelope>
																										 * 
																										 * 
																										 * // SOAP Body
																										 * SOAPBody
																										 * soapBody =
																										 * envelope.
																										 * getBody();
																										 * SoapParser
																										 * soapParser =
																										 * new
																										 * SoapParser(
																										 * null); //
																										 * SOAP Body
																										 * �젙蹂� 媛��졇�삤湲�
																										 * �삁�젣
																										 * SOAPElement
																										 * soapBodyElem
																										 * = soapBody.
																										 * addChildElement
																										 * (
																										 * "VerifyEmail",
																										 * "example");
																										 * SOAPElement
																										 * soapBodyElem1
																										 * =
																										 * soapBodyElem.
																										 * addChildElement
																										 * ("email",
																										 * "example");
																										 * soapBodyElem1
																										 * .addTextNode(
																										 * "test@gmail.com"
																										 * );
																										 * SOAPElement
																										 * soapBodyElem2
																										 * =
																										 * soapBodyElem.
																										 * addChildElement
																										 * (
																										 * "LicenseKey",
																										 * "example");
																										 * soapBodyElem2
																										 * .addTextNode(
																										 * "123");
																										 * 
																										 * MimeHeaders
																										 * headers =
																										 * soapMessage.
																										 * getMimeHeaders
																										 * (); headers.
																										 * addHeader(
																										 * "SOAPAction",
																										 * serverURI +
																										 * "VerifyEmail"
																										 * );
																										 * 
																										 * soapMessage.
																										 * saveChanges()
																										 * ;
																										 * 
																										 * Print the
																										 * request
																										 * message
																										 * System.out.
																										 * print("Request SOAP Message:"
																										 * );
																										 * soapMessage.
																										 * writeTo(
																										 * System.out);
																										 * System.out.
																										 * println();
																										 * 
																										 * return
																										 * soapMessage;
																										 * }
																										 * 
																										 * }
																										 */