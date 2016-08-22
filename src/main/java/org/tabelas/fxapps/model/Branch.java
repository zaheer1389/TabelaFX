package org.tabelas.fxapps.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.tabelas.fxapps.persistence.model.AbstractPojo;

@Entity
@Table(name="Branch")
public class Branch extends AbstractPojo implements Serializable{
	
	@Column(name="BranchID")
	private Long branchId;
	
	@Column(name="BranchName")
	private String branchName;

	public Long getBranchNo() {
		return branchId;
	}

	public void setBranchNo(Long branchNo) {
		this.branchId = branchNo;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

}
