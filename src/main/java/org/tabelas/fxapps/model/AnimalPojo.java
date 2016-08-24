package org.tabelas.fxapps.model;

public class AnimalPojo {
	
	String animalNo,arrivalDate,departureDate,serviceDate,expectedArrivalDate;
	
	public AnimalPojo(String animalNo, String arrivalDate, String departureDate
			,String serviceDate,String expectedArrivalDate){
		this.animalNo = animalNo;
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
		this.serviceDate = serviceDate;
		this.expectedArrivalDate = expectedArrivalDate;
	}

	public String getAnimalNo() {
		return animalNo;
	}

	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getExpectedArrivalDate() {
		return expectedArrivalDate;
	}

	public void setExpectedArrivalDate(String expectedArrivalDate) {
		this.expectedArrivalDate = expectedArrivalDate;
	}
	
	
	
}
