package com.recovery.managementsystem.model;


import javax.persistence.*;

@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    @OneToOne(targetEntity = Employee.class)
    @JoinColumn(nullable = false, name = "employee_id")
    private Employee employee;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}


	public PasswordResetToken(String token, Employee employee) {
		this.token = token;
		this.employee = employee;
		
	}

	public PasswordResetToken() {
		super();
		
	}


}