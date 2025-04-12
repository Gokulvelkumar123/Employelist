package com.Leavelist.EmployeeLeave.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.Leavelist.EmployeeLeave.Entity.Employee;
import com.Leavelist.EmployeeLeave.Entity.Leavelist;
import com.Leavelist.EmployeeLeave.repository.Employeerepository;
import com.Leavelist.EmployeeLeave.repository.Leavelistrepository;

@RestController
public class EmployeelistController {
	@Autowired
	Employeerepository repo;
	@Autowired
	Leavelistrepository repo1;

	@GetMapping("/listemployee")
	public List<Employee> getall() {
		List<Employee> employee = repo.findAll();
		return employee;
	}

	@PostMapping("/addemployee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Employee addEmployee(@RequestBody Employee employee) {
		repo.save(employee);
		return employee;
	}

	@PostMapping("/applyleave")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<Object> addList(@RequestBody Leavelist leavelist) {

	    if (leavelist.getStartdate().isBefore(LocalDate.now())) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Cannot apply for leave in the past. Please select a future start date.");
	    }
	    List<Leavelist> existingLeaves = repo1.findByEmployee(leavelist.getEmployee());
	    for (Leavelist existingLeave : existingLeaves) {
	        if (existingLeave.getStatus().equalsIgnoreCase("APPROVED") &&
	            leavelist.getStartdate().isBefore(existingLeave.getEnddate()) &&
	            leavelist.getEnddate().isAfter(existingLeave.getStartdate())) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot apply for leave as dates overlap with an existing approved leave.");
	        }
	    }
	    leavelist.setStatus("PENDING");
	    leavelist.setAppliedAt(LocalDateTime.now());
	    repo1.save(leavelist);
	    return ResponseEntity.ok(leavelist);
	}

	@GetMapping({"/listleaverequest", "/listleaverequest/{status}"})
	public ResponseEntity<List<Leavelist>> getLeaveRequests(
			@PathVariable(required = false) String status,
			@RequestParam(required = false) Integer employeeId) {
		try {
			List<Leavelist> leaves;
			if (status != null) {
				leaves = repo1.findByStatus(status);
			} else {
				leaves = repo1.findAll();
			}
			if (employeeId != null) {
				leaves.removeIf(leave -> !employeeId.equals(leave.getEmployee()));
			}
			return ResponseEntity.ok(leaves);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/approveorreject/{id}")
	public ResponseEntity<Leavelist> updateLeaveRequest(
			@PathVariable int id,
			@RequestParam String action,
			@RequestBody(required = false) Leavelist leavelist) {
		try {
			Leavelist existingLeave = repo1.findById(id)
				.orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
			
			String newStatus = action.toUpperCase();
			if (newStatus.equals("APPROVED") || newStatus.equals("REJECTED")) {
				existingLeave.setStatus(newStatus); 
				existingLeave.setProcessedAt(LocalDateTime.now());
			} else {
				return ResponseEntity.badRequest().build();
			}
			
			if (leavelist != null && leavelist.getReason() != null) {
				existingLeave.setReason(leavelist.getReason());
			}
			
			Leavelist updatedLeave = repo1.save(existingLeave);
			return ResponseEntity.ok(updatedLeave);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@GetMapping("/listemployeeleavedetails/{id}")
	public ResponseEntity<List<Employee>> getEmployeeLeaves(@PathVariable int id) {
		try {
			List<Employee> leaves = repo.findById(id);
			return ResponseEntity.ok(leaves);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/employeeWithLeaves/{employeeId}")
	public ResponseEntity<Map<String, Object>> getEmployeeWithLeaves(@PathVariable int employeeId) {
		List<Employee>employee=repo.findById(employeeId);
		if (employee == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", "Employee not found with ID: " + employeeId));
		}
		List<Leavelist> leaveLists = repo1.findByEmployee(employeeId);
		Map<String, Object> response = new HashMap<>();
		response.put("employee", employee);
		response.put("leaveRecords", leaveLists);
		return ResponseEntity.ok(response);
	}
}

