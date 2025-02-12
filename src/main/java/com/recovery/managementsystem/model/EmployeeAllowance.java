package com.recovery.managementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_allowance")
public class EmployeeAllowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id",referencedColumnName = "employeeId" ,nullable = false)
    @JsonIgnore
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allowance_id", nullable = false)
    @JsonIgnore
    private Allowance allowance;
    
    
    private LocalDateTime createdAt=LocalDateTime.now();
    
    private BigDecimal amount;
    
    private Double percentage;

    
    private LocalDate dateGranted = LocalDate.now();
	
	
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public Allowance getAllowance() {
		return allowance;
	}
	public void setAllowance(Allowance allowance) {
		this.allowance = allowance;
	}

	public LocalDate getDateGranted() {
		return dateGranted;
	}
	public void setDateGranted(LocalDate dateGranted) {
		this.dateGranted = dateGranted;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

   
}
