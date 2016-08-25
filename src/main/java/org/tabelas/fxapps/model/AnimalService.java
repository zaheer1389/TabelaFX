package org.tabelas.fxapps.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tabelas.fxapps.persistence.model.AbstractPojo;

@Entity
@Table(name="AnimalService")
public class AnimalService extends AbstractPojo implements Serializable{
	
	@JoinColumn(name="LactationId")
	private Lactation lactation;
	
	@Column(name="ServiceDate")
	private Timestamp serviceDate;
	
	@Column(name="AddedDate")
	private Timestamp addedDate;
	
	@Column(name="Result")
	private String result;
	
	@Column(name="Remarks")
	private byte[] remarks;
	
	@Column(name="GenderNo")
	private String genderNo;
	
	@Transient
	private String animalNo,strServiceDate;
	
	

	public String getAnimalNo() {
		return lactation.getAnimal().getAnimalNo();
	}

	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}

	public String getStrServiceDate() {
		return new SimpleDateFormat("dd-MMMM-yyyy").format(new Date(serviceDate.getTime()));
	}

	public void setStrServiceDate(String strServiceDate) {
		this.strServiceDate = strServiceDate;
	}

	public Lactation getLactation() {
		return lactation;
	}

	public void setLactation(Lactation lactation) {
		this.lactation = lactation;
	}

	public Timestamp getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Timestamp serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRemarks() {
		try {
			return new String((remarks!=null?remarks:"".getBytes()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void setRemarks(String remarks) {
		try {
			this.remarks = remarks.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getGenderNo() {
		return genderNo;
	}

	public void setGenderNo(String genderNo) {
		this.genderNo = genderNo;
	}

	public Timestamp getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}
	
	

}
