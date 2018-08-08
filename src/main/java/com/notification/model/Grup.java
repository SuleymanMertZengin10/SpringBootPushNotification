package com.notification.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Grup {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(unique=true)
	private String grupName;

	@ManyToMany()
	@JoinTable(name = "device_grup", joinColumns = @JoinColumn(name = "grup_id"), inverseJoinColumns = @JoinColumn(name = "device_mail"))
	private List<Device> devices = new ArrayList<>();
	
	public String getGrupName() {
		return grupName;
	}

	public void setGrupName(String grupName) {
		this.grupName = grupName;
	}

	@JsonIgnore	
	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	@Override
	public String toString() {
		return "Grup [id=" + id + ", grupName=" + grupName + ", devices=" + devices + "]";
	}



	
	
 
	
	
}
