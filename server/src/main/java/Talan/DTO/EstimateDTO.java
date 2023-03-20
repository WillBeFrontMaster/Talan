package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EstimateDTO {
	private String estimateNumber;
	private String proId;
	private String predictTime;
	private String quotePrice;
	private String estimateTitle;
	private String estimateContent;
	private Date estimateRegisterDate;
	private Character estimateStatus;
	private String requestNumber;
	
	public Map<String, Object> getEstimateList() {
		Map<String, Object> estimate = new HashMap<String, Object>();
		estimate.put("estimateNumber", getEstimateNumber());
		estimate.put("proId", getProId());
		estimate.put("predictTime", getPredictTime());
		estimate.put("quotePrice", getQuotePrice());
		estimate.put("estimateTitle", getEstimateTitle());
		estimate.put("estimateContent", getEstimateContent());
		estimate.put("estimateRegisterDate", getEstimateRegisterDate());
		estimate.put("estimateStatus", getEstimateStatus());
		estimate.put("requestNumber", getRequestNumber());
		return estimate;
	}
	
	public String getEstimateNumber() {
		return estimateNumber;
	}
	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getPredictTime() {
		return predictTime;
	}
	public void setPredictTime(String predictTime) {
		this.predictTime = predictTime;
	}
	public String getQuotePrice() {
		return quotePrice;
	}
	public void setQuotePrice(String quotePrice) {
		this.quotePrice = quotePrice;
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
	public String getEstimateRegisterDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.");
		String strEstimateRegisterDate = dateFormat.format(estimateRegisterDate);
		return strEstimateRegisterDate;
	}
	public void setEstimateRegisterDate(Date estimateRegisterDate) {
		this.estimateRegisterDate = estimateRegisterDate;
	}
	public Character getEstimateStatus() {
		return estimateStatus;
	}
	public void setEstimateStatus(Character estimateStatus) {
		this.estimateStatus = estimateStatus;
	}
	public String getRequestNumber() {
		return requestNumber;
	}
	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}
}
