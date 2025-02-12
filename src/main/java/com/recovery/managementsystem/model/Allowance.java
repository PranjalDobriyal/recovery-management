package com.recovery.managementsystem.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;





@Entity
@Table(name = "allowance")
public class Allowance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer allowanceId;
	
	@Column(unique = true)
	private String allowanceName;
	
	@Column(unique = true)
	private String allowanceShort;
	
	@Enumerated(EnumType.STRING)
    private AllowanceType allowanceType; //amount ya fir percentage
	
	@Column
	private String status;
	
	
	@Column
	private LocalDate entryDate=LocalDate.now();
	
	@Column
	private LocalDateTime createdAt=LocalDateTime.now();
	    
	@OneToMany(mappedBy = "allowance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<EmployeeAllowance> employeeAllowances = new HashSet<>();

	  public enum AllowanceType {
	        AMOUNT, PERCENTAGE
	    }

	public Integer getAllowanceId() {
		return allowanceId;
	}

	public void setAllowanceId(Integer allowanceId) {
		this.allowanceId = allowanceId;
	}

	public String getAllowanceName() {
		return allowanceName;
	}

	public void setAllowanceName(String allowanceName) {
		this.allowanceName = allowanceName;
	}

	public String getAllowanceShort() {
		return allowanceShort;
	}

	public void setAllowanceShort(String allowanceShort) {
		this.allowanceShort = allowanceShort;
	}

	public AllowanceType getAllowanceType() {
		return allowanceType;
	}

	public void setAllowanceType(AllowanceType allowanceType) {
		this.allowanceType = allowanceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public Set<EmployeeAllowance> getEmployeeAllowances() {
		return employeeAllowances;
	}

	public void setEmployeeAllowances(Set<EmployeeAllowance> employeeAllowances) {
		this.employeeAllowances = employeeAllowances;
	}

	public LocalDate getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	

}
