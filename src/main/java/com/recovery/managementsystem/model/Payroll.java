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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Payroll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "employee_id", referencedColumnName = "employeeId")
	private Employee employee;

	@Column
	private String allowanceName;

	@Column
	private String allowanceShort;

	@Column
	private BigDecimal allowanceAmount;

	@Column
	private BigDecimal deductionAmount;

	@Column
	@Enumerated(EnumType.STRING)
	private Month month;	

	@Column
	private int year;

	@Column
	private String status;

	@Column
	private LocalDate entryDate = LocalDate.now();

	@Column
	private LocalDateTime createdAt = LocalDateTime.now();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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

	public BigDecimal getAllowanceAmount() {
		return allowanceAmount;
	}

	public void setAllowanceAmount(BigDecimal allowanceAmount) {
		this.allowanceAmount = allowanceAmount;
	}

	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
