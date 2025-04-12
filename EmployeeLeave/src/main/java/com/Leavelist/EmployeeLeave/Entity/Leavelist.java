package com.Leavelist.EmployeeLeave.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="leave_request")
public class Leavelist {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="e_Id")
	private int id;
	@Column(name="e_startdate")
	private LocalDate startdate;
	@Column(name="e_enddate")
	private LocalDate enddate;
	@Column(name="e_reason")
	private String reason;
	@Column(name="e_status")
//	@Enumerated(EnumType.STRING)
	private String status;
	@Column(name="e_appliedat")
	private LocalDateTime appliedAt;
	@Column(name="employee_id")
	private Integer employee;
	@Column(name="e_processedat")
	private LocalDateTime processedAt;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getStartdate() {
		return startdate;
	}
	public void setStartdate(LocalDate startdate) {
		this.startdate = startdate;
	}
	public LocalDate getEnddate() {
		return enddate;
	}
	public void setEnddate(LocalDate enddate) {
		this.enddate = enddate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getAppliedAt() {
		return appliedAt;
	}
	public void setAppliedAt(LocalDateTime appliedAt) {
		this.appliedAt = appliedAt;
	}
	public Integer getEmployee() {
		return employee;
	}
	public void setEmployee(Integer employee) {
		this.employee = employee;
	}

	public LocalDateTime getProcessedAt() {
		return processedAt;
	}

	public void setProcessedAt(LocalDateTime processedAt) {
		this.processedAt = processedAt;
	}
	

	



}
