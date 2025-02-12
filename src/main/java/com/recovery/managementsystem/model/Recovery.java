package com.recovery.managementsystem.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "recovery")
public class Recovery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recoveryId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "employee_id",referencedColumnName = "employeeId")
    private Employee employee;


    private String recoveryStage;

    private BigDecimal amountToRecover;

    private BigDecimal recoveredAmount = BigDecimal.ZERO;

    private String recoveryType;

    private String notes;

    private LocalDate startDate;

    private LocalDate dueDate;

    private LocalDate lastFollowUpDate;

    private LocalDate nextFollowUpDate;

    private Integer followUpCount = 0;

    private String actionTaken;

   
    private String communicationMode;

    private Boolean agreementSigned = false;

    private Boolean legalActionRequired = false;

    private String documentReference;

   
    private String escalationLevel ;


    private String recoveryPriority;

    private Integer assignedBy;

    private LocalDate createdAt = LocalDate.now();

    private LocalDate updatedAt = LocalDate.now();

	public Integer getRecoveryId() {
		return recoveryId;
	}

	public void setRecoveryId(Integer recoveryId) {
		this.recoveryId = recoveryId;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getRecoveryStage() {
		return recoveryStage;
	}

	public void setRecoveryStage(String recoveryStage) {
		this.recoveryStage = recoveryStage;
	}

	public BigDecimal getAmountToRecover() {
		return amountToRecover;
	}

	public void setAmountToRecover(BigDecimal amountToRecover) {
		this.amountToRecover = amountToRecover;
	}

	public BigDecimal getRecoveredAmount() {
		return recoveredAmount;
	}

	public void setRecoveredAmount(BigDecimal recoveredAmount) {
		this.recoveredAmount = recoveredAmount;
	}

	public String getRecoveryType() {
		return recoveryType;
	}

	public void setRecoveryType(String recoveryType) {
		this.recoveryType = recoveryType;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getLastFollowUpDate() {
		return lastFollowUpDate;
	}

	public void setLastFollowUpDate(LocalDate lastFollowUpDate) {
		this.lastFollowUpDate = lastFollowUpDate;
	}

	public LocalDate getNextFollowUpDate() {
		return nextFollowUpDate;
	}

	public void setNextFollowUpDate(LocalDate nextFollowUpDate) {
		this.nextFollowUpDate = nextFollowUpDate;
	}

	public Integer getFollowUpCount() {
		return followUpCount;
	}

	public void setFollowUpCount(Integer followUpCount) {
		this.followUpCount = followUpCount;
	}

	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getCommunicationMode() {
		return communicationMode;
	}

	public void setCommunicationMode(String communicationMode) {
		this.communicationMode = communicationMode;
	}

	public Boolean getAgreementSigned() {
		return agreementSigned;
	}

	public void setAgreementSigned(Boolean agreementSigned) {
		this.agreementSigned = agreementSigned;
	}

	public Boolean getLegalActionRequired() {
		return legalActionRequired;
	}

	public void setLegalActionRequired(Boolean legalActionRequired) {
		this.legalActionRequired = legalActionRequired;
	}

	public String getDocumentReference() {
		return documentReference;
	}

	public void setDocumentReference(String documentReference) {
		this.documentReference = documentReference;
	}

	public String getEscalationLevel() {
		return escalationLevel;
	}

	public void setEscalationLevel(String escalationLevel) {
		this.escalationLevel = escalationLevel;
	}

	public String getRecoveryPriority() {
		return recoveryPriority;
	}

	public void setRecoveryPriority(String recoveryPriority) {
		this.recoveryPriority = recoveryPriority;
	}

	public Integer getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(Integer assignedBy) {
		this.assignedBy = assignedBy;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}