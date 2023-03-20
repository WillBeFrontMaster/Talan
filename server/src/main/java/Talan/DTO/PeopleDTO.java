package Talan.DTO;

import java.sql.Date;

public class PeopleDTO {
	
	private String peopleId;
	private String password;
	private String name;
	private Date birth;
	private String nickname;
	private String address;
	private String phone;
	private String intro;
	private String gender;
	private String email;
	private String account;
	private String originImageName;
	private String storeImageName;
	private String imagePath;
	
	public String toString() {
		return 	"peopleId="+peopleId
			+	"\n password="+password
			+	"\n name="+name
			+	"\n birth="+birth
			+	"\n nickname="+nickname
			+	"\n address="+address
			+	"\n phone="+phone
			+	"\n intro="+intro
			+	"\n gender="+gender
			+	"\n email="+email
			+	"\n account="+account
			+	"\n originImageName="+originImageName
			+	"\n storeImageName="+storeImageName
			+	"\n imagePath="+imagePath;
	}

	public String getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOriginImageName() {
		return originImageName;
	}

	public void setOriginImageName(String originImageName) {
		this.originImageName = originImageName;
	}

	public String getStoreImageName() {
		return storeImageName;
	}

	public void setStoreImageName(String storeImageName) {
		this.storeImageName = storeImageName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	
	
}
