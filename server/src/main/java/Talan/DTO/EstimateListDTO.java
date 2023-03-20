package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EstimateListDTO {
	String nickname;
	String storeImageName;
	String originImageName;
	String imagePath;
	String requestNumber;
	String category;
	String estimateNumber;
	String proId;
	String predictTime;
	String quotePrice;
	String estimateTitle;
	String estimateContent;
	Date estimateRegisterDate;
	String estimateStatus;
	
	private Map<String, String> categoryList = new HashMap<String, String>() {
		{
			put("lesson", "레슨");
			put("home", "홈/리빙");
			put("event", "이벤트");
			put("business", "비즈니스");
			put("design", "디자인/개발");
			put("part-time-job", "아르바이트");
			put("health", "건강/미용");
			put("etc", "기타");
		}
	};
	
	public Map<String, Object> getEstimateList() {
		Map<String, Object> estimateList = new HashMap<String, Object>();
		
		estimateList.put("nickname", nickname);
		estimateList.put("storeImageName", storeImageName);
		estimateList.put("originImageName", originImageName);
		estimateList.put("imagePath", imagePath);
		estimateList.put("requestNumber", requestNumber);
		estimateList.put("category", getCategory());
		estimateList.put("estimateNumber", estimateNumber);
		estimateList.put("proId", proId);
		estimateList.put("predictTime", predictTime);
		estimateList.put("quotePrice", quotePrice);
		estimateList.put("estimateTitle", estimateTitle);
		estimateList.put("estimateContent", estimateContent);
		estimateList.put("estimateStatus", estimateStatus);
		estimateList.put("estimateRegisterDate", getEstimateRegisterDate());
		
		return estimateList;
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
	public String getRequestNumber() {
		return requestNumber;
	}
	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}
	public String getCategory() {
		String strCateogory = categoryList.get(category).toString();
		return strCateogory;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public String getEstimateStatus() {
		return estimateStatus;
	}
	public void setEstimateStatus(String estimateStatus) {
		this.estimateStatus = estimateStatus;
	}
	
	
}
