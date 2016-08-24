package org.tabelas.fxapps.model;

import java.io.Serializable;
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
@Table(name="AnimalMilkWeight")
public class AnimalMilkWeight extends AbstractPojo implements Serializable{

	@JoinColumn(name="LactationId")
	private Lactation lactation;
	
	@Column(name="WeightDate")
	private Timestamp weightDate;
	
	@Column(name="AddedDate")
	private Timestamp addedDate;
	
	@Column(name="Weight")
	private Double weight;
	
	@Transient
	private String strObservedDate,animalNo;
	
	

	public String getStrObservedDate() {
		return new SimpleDateFormat("dd-MMMM-yyyy").format(new Date(weightDate.getTime()));
	}

	public void setStrObservedDate(String strObservedDate) {
		this.strObservedDate = strObservedDate;
	}

	public String getAnimalNo() {
		return lactation.getAnimal().getAnimalNo();
	}

	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}

	public Lactation getLactation() {
		return lactation;
	}

	public void setLactation(Lactation lactation) {
		this.lactation = lactation;
	}

	public Timestamp getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Timestamp weightDate) {
		this.weightDate = weightDate;
	}

	public Timestamp getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	
	
	
}
