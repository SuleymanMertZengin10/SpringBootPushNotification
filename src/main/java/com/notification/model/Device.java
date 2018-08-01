package com.notification.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Device {
	
	@Id
	private String token;
	
	@Column(unique=true)
	private String mail;
	
	@ManyToMany(mappedBy="devices",fetch=FetchType.LAZY)
	private List<Grup> grups=new ArrayList<>();


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	
	 @JsonIgnore	
    public List<Grup> getGrups() {
		
		return grups;
	}


	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	
}
