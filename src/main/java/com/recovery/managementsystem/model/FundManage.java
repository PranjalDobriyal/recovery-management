package com.recovery.managementsystem.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
	
	@Enumerated(EnumType.STRING)
	private FundType fundType;
	 
	
	
	@Column
	private String fundPurpose;
	
	

	@Column
	private String approvedBy;
	
	@Column
	private String status;
	
	
	private LocalDate requested;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate approvalDate;
	
	private LocalDate entryDate=LocalDate.now();

	private LocalDateTime createdAt=LocalDateTime.now();
	
	private LocalDateTime updatedAt;
	
	private String paymentType;
	
	private String remarks;
	
	private BigDecimal debitAmount;
	
	private BigDecimal creditAmount;
	
	
	private BigDecimal balance; 
	
	public enum FundType {
		Bonus, Advance, Miscellaneous;
    }
	public FundManage() {
	
	}

	public FundManage(Integer fundId, Employee employee, BigDecimal fundAmount, FundType fundType, String fundPurpose,
			String approvedBy, String status, LocalDate requested, LocalDate approvalDate, LocalDate entryDate,
			LocalDateTime createdAt, LocalDateTime updatedAt, String paymentType, String remarks,
			BigDecimal debitAmount, BigDecimal creditAmount, BigDecimal balance) {
		this.fundId = fundId;
		this.employee = employee;
		this.fundAmount = fundAmount;
		this.fundType = fundType;
		this.fundPurpose = fundPurpose;
		this.approvedBy = approvedBy;
		this.status = status;
		this.requested = requested;
		this.approvalDate = approvalDate;
		this.entryDate = entryDate;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.paymentType = paymentType;
		this.remarks = remarks;
		this.debitAmount = debitAmount;
		this.creditAmount = creditAmount;
		this.balance = balance;
	}

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



	public String getFundPurpose() {
		return fundPurpose;
	}

	public void setFundPurpose(String fundPurpose) {
		this.fundPurpose = fundPurpose;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getRequested() {
		return requested;
	}

	public void setRequested(LocalDate requested) {
		this.requested = requested;
	}

	public LocalDate getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(LocalDate approvalDate) {
		this.approvalDate = approvalDate;
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

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public FundType getFundType() {
		return fundType;
	}

	public void setFundType(FundType fundType) {
		this.fundType = fundType;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	
	
	
	
	
	

}
