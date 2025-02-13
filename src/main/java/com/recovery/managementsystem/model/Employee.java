package com.recovery.managementsystem.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee implements Serializable{
	
	 private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	private Integer id;
	
	@Column
	private String employeeName;
	
	@Column(unique = true, nullable = false)
    private String employeeId;
	
	@Column
	@Size(min =8 ,message= "The password should be minimum 8 characters")
	private String password;
	
	@Column
	@Size(max = 10,min = 10,message = "The contact number cannot be less than 10")
	private String contactNumber;
	
	@Column
	private String email;
     
	@Column
	private String panCard;
	
	@Column
	private String adhaarCard;
	
	@Column
	private String bankName;
	
	@Column
	private String ifscCode;
	
	@Column
	private BigDecimal walletBalance=BigDecimal.ZERO;
	
	@Column
	private String accountNumber;
	
	@Column
	@Enumerated(EnumType.STRING)
	private EmployeeType employeeType;
	
	private Long address;
	
	private String city;
	
	@Past(message = "The Date of Birth should be from the past")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	
	private String status;
	
	private String role;
	
	
	
	@Column
	private LocalDate entryDate=LocalDate.now();
	
	  private LocalDateTime createdAt = LocalDateTime.now();
	
	  private String designation;
	  
	  private BigDecimal salary;
	  
	  @Enumerated(EnumType.STRING)
	    private FOSType fosType; // Permanent, Contract dono mein se ek

	    @Enumerated(EnumType.STRING)
	    private PaymentType paymentType; // Commission, Salaried dono mein se ek

	    private Boolean hasIncentive;
	    
	    private BigDecimal incentiveAmount;
	    
	    private BigDecimal totalSales; // Applicable for commission-based FOS employees
	    	    
	    private Double commissionPercentage;
	    
	    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	    private PasswordResetToken passwordResetToken;
	    
	    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private Set<EmployeeAllowance> employeeAllowances = new HashSet<>();
	    
	    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	    private List<Recovery> recoveries;
	    
	    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	    private List<FundManage> funds;
  
	  public enum FOSType {
	        Permanent, Contract
	    }

	    public enum PaymentType {
	        Commission, Salaried
	    }

	public String getEmployeeName() {
		return employeeName;
	}


	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	@Column(unique = true)
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	
	public enum EmployeeType {
	    FOS, Office
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPanCard() {
		return panCard;
	}


	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}


	public String getAdhaarCard() {
		return adhaarCard;
	}


	public void setAdhaarCard(String adhaarCard) {
		this.adhaarCard = adhaarCard;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getIfscCode() {
		return ifscCode;
	}


	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}


	public String getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public EmployeeType getEmployeeType() {
		return employeeType;
	}


	public void setEmployeeType(EmployeeType employeeType) {
		this.employeeType = employeeType;
	}


	public Long getAddress() {
		return address;
	}


	public void setAddress(Long address) {
		this.address = address;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public LocalDate getDob() {
		return dob;
	}


	public void setDob(LocalDate dob) {
		this.dob = dob;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}


	public String getDesignation() {
		return designation;
	}


	public void setDesignation(String designation) {
		this.designation = designation;
	}


	public BigDecimal getSalary() {
		return salary;
	}


	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}


	public FOSType getFosType() {
		return fosType;
	}


	public void setFosType(FOSType fosType) {
		this.fosType = fosType;
	}


	public PaymentType getPaymentType() {
		return paymentType;
	}


	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}


	public Boolean getHasIncentive() {
		return hasIncentive;
	}


	public void setHasIncentive(Boolean hasIncentive) {
		this.hasIncentive = hasIncentive;
	}


	public BigDecimal getIncentiveAmount() {
		return incentiveAmount;
	}


	public void setIncentiveAmount(BigDecimal incentiveAmount) {
		this.incentiveAmount = incentiveAmount;
	}


	public BigDecimal getTotalSales() {
		return totalSales;
	}


	public void setTotalSales(BigDecimal totalSales) {
		this.totalSales = totalSales;
	}


	public Double getCommissionPercentage() {
		return commissionPercentage;
	}


	public void setCommissionPercentage(Double commisionPercentage) {
		this.commissionPercentage = commisionPercentage;
	}


	public PasswordResetToken getPasswordResetToken() {
		return passwordResetToken;
	}


	public void setPasswordResetToken(PasswordResetToken passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}


	public Set<EmployeeAllowance> getEmployeeAllowances() {
		return employeeAllowances;
	}


	public void setEmployeeAllowances(Set<EmployeeAllowance> employeeAllowances) {
		this.employeeAllowances = employeeAllowances;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public LocalDate getEntryDate() {
		return entryDate;
	}


	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}


	public List<Recovery> getRecoveries() {
		return recoveries;
	}


	public void setRecoveries(List<Recovery> recoveries) {
		this.recoveries = recoveries;
	}


	

	public List<FundManage> getFunds() {
		return funds;
	}


	public void setFunds(List<FundManage> funds) {
		this.funds = funds;
	}


	public BigDecimal getWalletBalance() {
		return walletBalance;
	}


	public void setWalletBalance(BigDecimal walletBalance) {
		this.walletBalance = walletBalance;
	}
	

}
