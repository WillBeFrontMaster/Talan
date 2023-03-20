package com.example.dto;

public class UserDto {

	private String userId;
	private String userName;
	private int age;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String toString() {
		return 	"userId="+userId
			+	"\n userName="+userName
			+	"\n age="+age;
	}
	
}
