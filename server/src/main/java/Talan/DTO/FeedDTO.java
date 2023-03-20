package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedDTO {
	
	private String feedNumber;
	private String proId;
	private Date feedRegisterDate;
	private String feedTitle;
	private String feedContent;
	private String feedWriterNickname;
	private String storeFileName;
	private String originFileName;
	private String filePath;
	
	public Map<String, String> getFeedList() {
		Map<String, String> feed = new HashMap<String, String>();
		feed.put("feedNumber", feedNumber);
		feed.put("proId", proId);
		feed.put("feedRegisterDate", getFeedRegisterDate());
		feed.put("feedTitle", feedTitle);
		feed.put("feedContent", feedContent);
		feed.put("feedWriterNickname", feedWriterNickname);
		feed.put("storeFileName", getStoreFileName());
		feed.put("originFileName", getOriginFileName());
		feed.put("filePath", getFilePath());
		return feed;
	}

	public String getFeedNumber() {
		return feedNumber;
	}
	public void setFeedNumber(String feedNumber) {
		this.feedNumber = feedNumber;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getFeedRegisterDate() {
		// Date 타입으로 그냥 가져오면 날짜가 박살나니까 String 타입으로 바꾸고 Format해줬슴다. - 소담 -
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
		String strFeedRegisterDate = dateFormat.format(feedRegisterDate);
		return strFeedRegisterDate;
	}
	public void setFeedRegisterDate(Date feedRegisterDate) {
		this.feedRegisterDate = feedRegisterDate;
	}
	public String getFeedTitle() {
		return feedTitle;
	}
	public void setFeedTitle(String feedTitle) {
		this.feedTitle = feedTitle;
	}
	public String getFeedContent() {
		return feedContent;
	}
	public void setFeedContent(String feedContent) {
		this.feedContent = feedContent;
	}
	public String getFeedWriterNickname() {
		return feedWriterNickname;
	}
	public void setFeedWriterNickname(String feedWriterNickname) {
		this.feedWriterNickname = feedWriterNickname;
	}
	public String getStoreFileName() {
		return storeFileName;
	}
	public void setStoreFileName(String storeFileName) {
		this.storeFileName = storeFileName;
	}
	public String getOriginFileName() {
		return originFileName;
	}
	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
