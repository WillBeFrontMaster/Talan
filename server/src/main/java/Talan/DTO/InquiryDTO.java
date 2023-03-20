package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InquiryDTO {
	private String inquiryNumber;
	private String peopleId;
	private String inquiryTitle;
	private String inquiryContent;
	private Character SecretStatus;
	private String inquiryPassword;
	private Date inquiryRegisterDate;
	private Date inquiryModifyDate;
	private Character responseStatus;
	
	public Map<String, Object> getInquiryList() {
		Map<String, Object> inquiry = new HashMap<String, Object>();
		inquiry.put("inquiryNumber", inquiryNumber);
		inquiry.put("peopleId", peopleId);
		inquiry.put("inquiryTitle", inquiryTitle);
		inquiry.put("inquiryContent", inquiryContent);
		inquiry.put("SecretStatus", SecretStatus);
		inquiry.put("inquiryRegisterDate", getInquiryRegisterDate());
		inquiry.put("inquiryModifyDate", getInquiryModifyDate());
		inquiry.put("responseStatus", responseStatus);
		return inquiry;
	}
	
	public String getInquiryNumber() {
		return inquiryNumber;
	}
	public void setInquiryNumber(String inquiryNumber) {
		this.inquiryNumber = inquiryNumber;
	}
	public String getPeopleId() {
		return peopleId;
	}
	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}
	public String getInquiryTitle() {
		return inquiryTitle;
	}
	public void setInquiryTitle(String inquiryTitle) {
		this.inquiryTitle = inquiryTitle;
	}
	public String getInquiryContent() {
		return inquiryContent;
	}
	public void setInquiryContent(String inquiryContent) {
		this.inquiryContent = inquiryContent;
	}
	public Character getSecretStatus() {
		return SecretStatus;
	}
	public void setSecretStatus(Character secretStatus) {
		SecretStatus = secretStatus;
	}
	public String getInquiryPassword() {
		return inquiryPassword;
	}
	public void setInquiryPassword(String inquiryPassword) {
		this.inquiryPassword = inquiryPassword;
	}
	public String getInquiryRegisterDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String strInquiryRegisterDate = dateFormat.format(inquiryRegisterDate);
		return strInquiryRegisterDate;
	}
	public void setInquiryRegisterDate(Date inquiryRegisterDate) {
		this.inquiryRegisterDate = inquiryRegisterDate;
	}
	public String getInquiryModifyDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String strInquiryModifyDate = dateFormat.format(inquiryModifyDate);
		return strInquiryModifyDate;

	}
	public void setInquiryModifyDate(Date inquiryModifyDate) {
		this.inquiryModifyDate = inquiryModifyDate;
	}
	public Character getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(Character responseStatus) {
		this.responseStatus = responseStatus;
	}
}

