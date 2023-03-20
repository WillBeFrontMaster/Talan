package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedCommentsDTO {
	private String feedCommentsNumber;
	private String feedNumber;
	private String peopleId;
	private String feedCommentsContent;
	private Date feedCommentsRegisterDate;
	private String nickname;
	
	public Map<String, String> getFeedCommentsList() {
		Map<String, String> feedComments = new HashMap<String, String>();
		feedComments.put("feedCommentsNumber", feedCommentsNumber);
		feedComments.put("feedNumber", feedNumber);
		feedComments.put("peopleId", peopleId);
		feedComments.put("nickname", nickname);
		feedComments.put("feedCommentsContent", feedCommentsContent);
		feedComments.put("feedCommentsRegisterDate", getFeedCommentsRegisterDate());
		return feedComments;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getFeedCommentsNumber() {
		return feedCommentsNumber;
	}
	public void setFeedCommentsNumber(String feedCommentsNumber) {
		this.feedCommentsNumber = feedCommentsNumber;
	}
	public String getFeedNumber() {
		return feedNumber;
	}
	public void setFeedNumber(String feedNumber) {
		this.feedNumber = feedNumber;
	}
	public String getPeopleId() {
		return peopleId;
	}
	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}
	public String getFeedCommentsContent() {
		return feedCommentsContent;
	}
	public void setFeedCommentsContent(String feedCommentsContent) {
		this.feedCommentsContent = feedCommentsContent;
	}
	public String getFeedCommentsRegisterDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String strFeedCommentsRegisterDate = dateFormat.format(feedCommentsRegisterDate);
		return strFeedCommentsRegisterDate;
	}
	public void setFeedCommentsRegisterDate(Date feedCommentsRegisterDate) {
		this.feedCommentsRegisterDate = feedCommentsRegisterDate;
	}
	
}
