package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentDTO {
	private String paymentNumber;
	private String estimateNumber;
	private Integer paymentPrice;
	private Character paymentStatus;
	private Date paymentDate;
	private Date paymentCancelDate;
	private String paymentType;
	private String progressiveStatus;
	private String peopleId;
	private String proId;
	private Character reviewStatus;
	
	private Map<String, String> paymentTypeList = new HashMap<String, String>() {
		{
			put("card", "카드");
			put("account", "무통장입금");
		}
	};
	
	public Map<String, Object> getPaymentList() {
		Map<String, Object> payment = new HashMap<String, Object>();
			
		payment.put("paymentNumber", paymentNumber);
		payment.put("estimateNumber", estimateNumber);
		payment.put("paymentPrice", paymentPrice);
		payment.put("paymentStatus", paymentStatus);
		payment.put("paymentDate", getPaymentDate());
		payment.put("paymentCancleDate", getPaymentCancelDate());
		payment.put("paymentType", getPaymentType());
		payment.put("progressiveStatus", progressiveStatus);
		payment.put("peopleId", peopleId);
		payment.put("proId", proId);
		payment.put("reviewStatus", reviewStatus);
		return payment;
	}
	
	
	public Character getReviewStatus() {
		return reviewStatus;
	}


	public void setReviewStatus(Character reviewStatus) {
		this.reviewStatus = reviewStatus;
	}


	public String getPeopleId() {
		return peopleId;
	}
	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getPaymentNumber() {
		return paymentNumber;
	}
	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}
	public String getEstimateNumber() {
		return estimateNumber;
	}
	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}
	public Integer getPaymentPrice() {
		return paymentPrice;
	}
	public void setPaymentPrice(Integer paymentPrice) {
		this.paymentPrice = paymentPrice;
	}
	public Character getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(Character paymentStatus) {
		this.paymentStatus = paymentStatus;
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
		String strPaymentCancelDate = dateFormat.format(paymentCancelDate);
		return strPaymentCancelDate;
	}
	public void setPaymentCancelDate(Date paymentCancelDate) {
		this.paymentCancelDate = paymentCancelDate;
	}
	public String getPaymentType() {
		String strPaymentType = paymentTypeList.get(paymentType).toString();
		return strPaymentType;
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
	
}
