package com.recovery.managementsystem.model;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "fund_manage")
public class FundManage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fundId;
	
	@ManyToOne
	@JoinColumn(name = "employeee_id" ,referencedColumnName = "employeeId")
	private Employee employee;
	
	@Column
	private BigDecimal fundAmount;
	
	@Column
	private String paymentMode;
	
	@Column
	private String amountType; //credit or debit
	
	@Column
	private String fundPurpose;
	
	@Column
	private String category; 
	
	@Column
	private String givenBy;
	
	@Column
	private String status;
	

	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate entryDate=LocalDate.now();

	private LocalDateTime createdAt=LocalDateTime.now();

	
	private BigDecimal prevBalance=BigDecimal.ZERO;
	
	private BigDecimal newBalance=BigDecimal.ZERO;

	public Integer getFundId() {
		return fundId;
	}

	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public BigDecimal getFundAmount() {
		return fundAmount;
	}

	public void setFundAmount(BigDecimal fundAmount) {
		this.fundAmount = fundAmount;
	}

	public String getAmountType() {
		return amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public String getFundPurpose() {
		return fundPurpose;
	}

	public void setFundPurpose(String fundPurpose) {
		this.fundPurpose = fundPurpose;
	}

	public String getGivenBy() {
		return givenBy;
	}

	public void setGivenBy(String givenBy) {
		this.givenBy = givenBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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





	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public BigDecimal getPrevBalance() {
		return prevBalance;
	}

	public void setPrevBalance(BigDecimal prevBalance) {
		this.prevBalance = prevBalance;
	}

	public BigDecimal getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}

}

		
	