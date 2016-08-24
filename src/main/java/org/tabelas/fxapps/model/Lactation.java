package org.tabelas.fxapps.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.tabelas.fxapps.persistence.model.AbstractPojo;

@Entity
@Table(name="Lactation")
public class Lactation extends AbstractPojo implements Serializable{

	@JoinColumn(name="AnimalId")
	private Animal animal;
	
	@Column(name="ArrivalDate")
	private Timestamp arrivalDate;
	
	@Column(name="CowingDate")
	private Timestamp cowingDate;
	
	@Column(name="VehicleNo")
	private String vehicleNo;
	
	@Column(name="DepartureDate")
	private Timestamp departureDate;
	
	@Column(name="DepartureVehicleNo")
	private String departureVehicleNo;
	
	public String getDepartureVehicleNo() {
		return departureVehicleNo;
	}

	public void setDepartureVehicleNo(String departureVehicleNo) {
		this.departureVehicleNo = departureVehicleNo;
	}

	@Column(name="CurrentLactation")
	private boolean currentLactation;
	
	@Transient
	private String animalNo,strArrivalDate,strCowingDate,strDepartureDate;

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Timestamp getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Timestamp arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Timestamp getCowingDate() {
		return cowingDate;
	}

	public void setCowingDate(Timestamp cowingDate) {
		this.cowingDate = cowingDate;
	}

	public Timestamp getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Timestamp departureDate) {
		this.departureDate = departureDate;
	}

	public boolean isCurrentLactation() {
		return currentLactation;
	}

	public void setCurrentLactation(boolean currentLactation) {
		this.currentLactation = currentLactation;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getAnimalNo() {
		return animal.getAnimalNo();
	}

	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}

	public String getStrArrivalDate() {
		return new SimpleDateFormat("dd-MMM-yyyy").format(arrivalDate);
	}

	public void setStrArrivalDate(String strArrivalDate) {
		this.strArrivalDate = strArrivalDate;
	}

	public String getStrCowingDate() {
		return new SimpleDateFormat("dd-MMM-yyyy").format(cowingDate);
	}

	public void setStrCowingDate(String strCowingDate) {
		this.strCowingDate = strCowingDate;
	}

	public String getStrDepartureDate() {
		return (departureDate != null)?new SimpleDateFormat("dd-MMM-yyyy").format(departureDate):"";
	}

	public void setStrDepartureDate(String strDepartureDate) {
		this.strDepartureDate = strDepartureDate;
	}
	
	
	
}
