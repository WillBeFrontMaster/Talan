package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseDTO {
	private String responseNumber;
	private String adminId;
	private String responseTitle;
	private String responseContent;
	private Date responseRegisterDate;
	private Date responseModifyDate;
	private String inquiryNumber;
	
	public String getResponseNumber() {
		return responseNumber;
	}
	public void setResponseNumber(String responseNumber) {
		this.responseNumber = responseNumber;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getResponseTitle() {
		return responseTitle;
	}
	public void setResponseTitle(String responseTitle) {
		this.responseTitle = responseTitle;
	}
	public String getResponseContent() {
		return responseContent;
	}
	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
	public String getResponseRegisterDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String strResponseRegisterDate = dateFormat.format(responseRegisterDate);
		return strResponseRegisterDate;
	}
	public void setResponseRegisterDate(Date responseRegisterDate) {
		this.responseRegisterDate = responseRegisterDate;
	}
	public String getResponseModifyDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String strResponseModifyDate = dateFormat.format(responseModifyDate);
		return strResponseModifyDate;
	}
	public void setResponseModifyDate(Date responseModifyDate) {
		this.responseModifyDate = responseModifyDate;
	}
	public String getInquiryNumber() {
		return inquiryNumber;
	}
	public void setInquiryNumber(String inquiryNumber) {
		this.inquiryNumber = inquiryNumber;
	}
	
}
