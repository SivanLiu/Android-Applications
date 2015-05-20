package com.example.bmobtest;

import cn.bmob.v3.BmobObject;

public class FeedBack extends BmobObject{
	private String name;
	private String feedback;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
}
