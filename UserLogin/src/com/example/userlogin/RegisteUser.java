package com.example.userlogin;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class RegisteUser extends BmobUser{


	private String number;
	private String sex;
	private int age;
	private String address;
	public String getConfpassword() {
		return confpassword;
	}
	public void setConfpassword(String confpassword) {
		this.confpassword = confpassword;
	}
	private String confpassword;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
