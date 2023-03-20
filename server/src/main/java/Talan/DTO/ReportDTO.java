package Talan.DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportDTO {
	private String reportNumber;
	private String reportPeople;
	private String reportTarget;
	private String reportContent;
	private String reportCode;
	private Date reportDate;
	private Character reportStatus;

	public Map<String, Object> getReportList() {
		Map<String, Object> report = new HashMap<String, Object>();
		report.put("reportNumber", reportNumber);
		report.put("reportPeople", reportPeople);
		report.put("reportTarget", reportTarget);
		report.put("reportContent", reportContent);
		report.put("reportCode", reportCode);
		report.put("reportDate", getReportDate());
		report.put("reportStatus", reportStatus);
		return report;
	}
	
	public Character getReportStatus() {
		return reportStatus;
	}


	public void setReportStatus(Character reportStatus) {
		this.reportStatus = reportStatus;
	}

	
	public String getReportDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.");
		String strReportDate = dateFormat.format(reportDate);
		return strReportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportNumber() {
		return reportNumber;
	}
	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}
	public String getReportPeople() {
		return reportPeople;
	}
	public void setReportPeople(String reportPeople) {
		this.reportPeople = reportPeople;
	}
	public String getReportTarget() {
		return reportTarget;
	}
	public void setReportTarget(String reportTarget) {
		this.reportTarget = reportTarget;
	}
	public String getReportContent() {
		return reportContent;
	}
	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	
}
