package cn.mldn.vo;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Company implements Serializable {
	private String title;
	private String number[];
	
	public String[] getNumber() {
		return number;
	}
	public void setNumber(String[] number) {
		this.number = number;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public Company(){
		
	}
	@Override
	public String toString() {
		return "Company [title=" + title + ", number="
				+ Arrays.toString(number) + "]";
	}
}
