package Talan.DTO;

public class AdminDTO {
	private String adminId;
	private String adminPassword;
	private Character authority;
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	public Character getAuthority() {
		return authority;
	}
	public void setAuthority(Character authority) {
		this.authority = authority;
	}
	
}
