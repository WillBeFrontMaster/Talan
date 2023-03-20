package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReviewDTO {
	private String reviewNumber;
	private String paymentNumber;
	private String peopleId;
	private String reviewTitle;
	private String reviewContent;
	private Integer starPoint;
	private String proId;
	private String reviewResponse;
	private Date reviewDate;
	
	public Map<String, Object> getReviewList() {
		Map<String, Object> review = new HashMap<String, Object>();
			review.put("reviewNumber", reviewNumber);
			review.put("paymentNumber", paymentNumber);
			review.put("peopleId", peopleId);
			review.put("reviewTitle", reviewTitle);
			review.put("reviewContent", reviewContent);
			review.put("starPoint", starPoint);
			review.put("proId", proId);
			review.put("reviewResponse", reviewResponse);
			review.put("reviewDate", getReviewDate());
		return review;
	}
	
	public String getReviewNumber() {
		return reviewNumber;
	}
	public void setReviewNumber(String reviewNumber) {
		this.reviewNumber = reviewNumber;
	}
	public String getPaymentNumber() {
		return paymentNumber;
	}
	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}
	public String getPeopleId() {
		return peopleId;
	}
	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}
	public String getReviewTitle() {
		return reviewTitle;
	}
	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	public Integer getStarPoint() {
		return starPoint;
	}
	public void setStarPoint(Integer starPoint) {
		this.starPoint = starPoint;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
	public String getReviewResponse() {
		return reviewResponse;
	}
	public void setReviewResponse(String reviewResponse) {
		this.reviewResponse = reviewResponse;
	}

	public String getReviewDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.");
		String strReviewDate = dateFormat.format(reviewDate);
		return strReviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
	
}
