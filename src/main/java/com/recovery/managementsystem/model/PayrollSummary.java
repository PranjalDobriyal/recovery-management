package com.recovery.managementsystem.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
  public class PayrollSummary {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String employeeId;
	
	private String employeeName;
	
	private BigDecimal totalAllowance;
	
	private BigDecimal totalDeduction;
	
	private BigDecimal netSalary;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Month month;	

	@Column
	private int year;
	@Column
	private LocalDate entryDate=LocalDate.now();
	
	
	@Column
	private LocalDateTime createdAt=LocalDateTime.now();
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public BigDecimal getTotalAllowance() {
		return totalAllowance;
	}

	public void setTotalAllowance(BigDecimal totalAllowance) {
		this.totalAllowance = totalAllowance;
	}

	public BigDecimal getTotalDeduction() {
		return totalDeduction;
	}

	public void setTotalDeduction(BigDecimal totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	public BigDecimal getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(BigDecimal netSalary) {
		this.netSalary = netSalary;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	
	
 }
 