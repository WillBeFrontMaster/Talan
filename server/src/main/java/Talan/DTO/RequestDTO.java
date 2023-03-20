package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestDTO {
	private String requestNumber;
	private String peopleId;
	private String category;
	private Date requestDate;
	private String requestTitle;
	private String requestContent;
	private String preference;
	private Date requestRegisterDate;
	private Character requestStatus;
	private String town;
	private String district;
	private Integer taskLevel;
	
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
	
	public Map<String, Object> getRequestList() {
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("requestNumber", requestNumber);
		request.put("peopleId", peopleId);
		request.put("category", getCategory());
		request.put("requestDate", getRequestDate());
		request.put("requestTitle", requestTitle);
		request.put("requestContent", requestContent);
		request.put("preference", preference);
		request.put("requestRegisterDate", getRequestRegisterDate());
		request.put("requestStatus", requestStatus);
		request.put("town", town);
		request.put("district", district);
		request.put("taskLevel", taskLevel);
		return request;
	}
	
	public String getRequestNumber() {
		return requestNumber;
	}
	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}
	public String getPeopleId() {
		return peopleId;
	}
	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}
	public String getCategory() {
		String strCateogory = categoryList.get(category).toString();
		return strCateogory;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getRequestDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.");
		String strRequestDate = dateFormat.format(requestDate);
		return strRequestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
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
	public String getPreference() {
		return preference;
	}
	public void setPreference(String preference) {
		this.preference = preference;
	}
	public String getRequestRegisterDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.");
		String strRequestRegisterDate = dateFormat.format(requestRegisterDate);
		return strRequestRegisterDate;
	}
	public void setRequestRegisterDate(Date requestRegisterDate) {
		this.requestRegisterDate = requestRegisterDate;
	}
	public Character getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(Character requestStatus) {
		this.requestStatus = requestStatus;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public Integer getTaskLevel() {
		return taskLevel;
	}
	public void setTaskLevel(Integer taskLevel) {
		this.taskLevel = taskLevel;
	}
}
