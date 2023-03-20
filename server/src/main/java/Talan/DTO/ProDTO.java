package Talan.DTO;

import java.util.HashMap;
import java.util.Map;

public class ProDTO {

	private String proId;
	private Integer kindScore;
	private String category;
	private String license;
	private String content;
	private String experiencePeriod;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExperiencePeriod() {
		return experiencePeriod;
	}

	public void setExperiencePeriod(String experiencePeriod) {
		this.experiencePeriod = experiencePeriod;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public Integer getKindScore() {
		return kindScore;
	}

	public void setKindScore(Integer kindScore) {
		this.kindScore = kindScore;
	}

	public String getCategory() {
		String strCateogory = categoryList.get(category).toString();
		return strCateogory;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

}
