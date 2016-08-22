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
@Table(name="Animal")
public class Animal extends AbstractPojo implements Serializable{
	
	@JoinColumn(name="BranchId")
	private Branch branch;
	
	@Column(name="AnimalNo")
	private String animalNo;
	
	@Column(name="OwnerName")
	private String ownerName;
	
	@Column(name="PurchasePrice")
	private Double purchasePrice;
	
	@Column(name="PurchaseDate")
	private Timestamp purchaseDate;	
	
	@Column(name="AddedDate")
	private Timestamp addedDate;
	
	@Column(name="Sold")
	private boolean sold;
	
	@Column(name="SoldDate")
	private Timestamp soldDate;
	
	@Column(name="BuyerName")
	private String buyerName;
	
	@Column(name="SoldPrice")
	private Double soldPrice;

	@Transient
	private String strPurchaseDate;
	

	public String getStrPurchaseDate() {
		return new SimpleDateFormat("dd-MMMMMM-yyyy").format(purchaseDate);
	}

	public void setStrPurchaseDate(String strPurchaseDate) {
		this.strPurchaseDate = strPurchaseDate;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getAnimalNo() {
		return animalNo;
	}

	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Timestamp getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Timestamp purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Timestamp getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public Timestamp getSoldDate() {
		return soldDate;
	}

	public void setSoldDate(Timestamp soldDate) {
		this.soldDate = soldDate;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Double getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(Double soldPrice) {
		this.soldPrice = soldPrice;
	}
	
	

}
