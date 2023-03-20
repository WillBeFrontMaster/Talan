package Talan.DTO;

import java.util.HashMap;
import java.util.Map;

public class MyPageDTO {
	String nickname;
	String storeImageName;
	String originImageName;
	String imagePath;
	String intro;
	String estimate;
	String request;
	String paymentPro;
	String paymentPeople;
	String reviewPro;
	String reviewPeople;
	String inquiry;
	String report;
	
	
	public Map<String, String> getMyPage() {
		Map<String, String> info = new HashMap<String, String>();
		
		info.put("nickname", nickname);
		info.put("storeImageName", storeImageName);
		info.put("originImageName", originImageName);
		info.put("imagePath", imagePath);
		info.put("intro", intro);
		info.put("estimate", estimate);
		info.put("request", request);
		info.put("paymentPro", paymentPro);
		info.put("paymentPeople", paymentPeople);
		info.put("reviewPro", reviewPro);
		info.put("reviewPeople", reviewPeople);
		info.put("inquiry", inquiry);
		info.put("report", report);
		
		return info;
	}
	
	
	
	public String getPaymentPro() {
		return paymentPro;
	}



	public void setPaymentPro(String paymentPro) {
		this.paymentPro = paymentPro;
	}



	public String getPaymentPeople() {
		return paymentPeople;
	}



	public void setPaymentPeople(String paymentPeople) {
		this.paymentPeople = paymentPeople;
	}



	public String getReviewPro() {
		return reviewPro;
	}



	public void setReviewPro(String reviewPro) {
		this.reviewPro = reviewPro;
	}



	public String getReviewPeople() {
		return reviewPeople;
	}



	public void setReviewPeople(String reviewPeople) {
		this.reviewPeople = reviewPeople;
	}



	public String getNickname() {
		return nickname;
	}



	public void setNickname(String nickname) {
		this.nickname = nickname;
	}



	public String getStoreImageName() {
		return storeImageName;
	}



	public void setStoreImageName(String storeImageName) {
		this.storeImageName = storeImageName;
	}



	public String getOriginImageName() {
		return originImageName;
	}



	public void setOriginImageName(String originImageName) {
		this.originImageName = originImageName;
	}



	public String getImagePath() {
		return imagePath;
	}



	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}



	public String getIntro() {
		return intro;
	}



	public void setIntro(String intro) {
		this.intro = intro;
	}



	public String getEstimate() {
		return estimate;
	}
	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getInquiry() {
		return inquiry;
	}
	public void setInquiry(String inquiry) {
		this.inquiry = inquiry;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	
	
}
