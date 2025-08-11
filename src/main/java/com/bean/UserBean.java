package com.bean;

public class UserBean {
	private Integer id;
	private String name;
	private String name_hin;
	private String name_guj;
	
	public UserBean() {
		super();
	}

	public UserBean(Integer id, String name, String name_hin, String name_guj) {
		super();
		this.id = id;
		this.name = name;
		this.name_hin = name_hin;
		this.name_guj = name_guj;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName_hin() {
		return name_hin;
	}
	public void setName_hin(String name_hin) {
		this.name_hin = name_hin;
	}
	public String getName_guj() {
		return name_guj;
	}
	public void setName_guj(String name_guj) {
		this.name_guj = name_guj;
	}
	
	
}
