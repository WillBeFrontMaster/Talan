package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentDetailDTO {
	private String paymentNumber;
	private Integer paymentPrice;
	private Date paymentDate;
	private Date paymentCancelDate;
	private String paymentType;
	private String progressiveStatus;
	private String estimateNumber;
	private String estimateTitle;
	private String estimateContent;
	private String requestTitle;
	private String requestContent;
	private String nickname;
	private String proId;
	private Character reviewStatus;
	private String reviewNumber;
	
	

	public String getReviewNumber() {
		return reviewNumber;
	}

	public void setReviewNumber(String reviewNumber) {
		this.reviewNumber = reviewNumber;
	}

	public Character getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Character reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public void setPaymentPrice(Integer paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public Integer getPaymentPrice() {
		return paymentPrice;
	}

	public void setPaymentPrice(int paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	public String getPaymentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String strPaymentDate = dateFormat.format(paymentDate);
		return strPaymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentCancelDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String strPaymentCancleDate = dateFormat.format(paymentCancelDate);
		return strPaymentCancleDate;
	}

	public void setPaymentCancelDate(Date paymentCancelDate) {
		this.paymentCancelDate = paymentCancelDate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getProgressiveStatus() {
		return progressiveStatus;
	}

	public void setProgressiveStatus(String progressiveStatus) {
		this.progressiveStatus = progressiveStatus;
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public String getEstimateTitle() {
		return estimateTitle;
	}

	public void setEstimateTitle(String estimateTitle) {
		this.estimateTitle = estimateTitle;
	}

	public String getEstimateContent() {
		return estimateContent;
	}

	public void setEstimateContent(String estimateContent) {
		this.estimateContent = estimateContent;
	}

	public String getRequestTitle() {
		return requestTitle;
	}

	public void setRequestTitle(String requestTitle) {
		this.requestTitle = requestTitle;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getProId() {
		return proId;
	}
	
	public void setProId(String proId) {
		this.proId = proId;
	}
	
	private Map<String, String> paymentTypeList = new HashMap<String, String>() {
		{
			put("card", "카드");
			put("account", "무통장입금");
		}
	};
}
