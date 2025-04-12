package com.Leavelist.EmployeeLeave.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Leavelist.EmployeeLeave.Entity.Employee;
import com.Leavelist.EmployeeLeave.Entity.Leavelist;

public interface Employeerepository extends JpaRepository<Employee, Long>{

	void save(Leavelist leave_list);

	List<Employee> findById(Integer employeeId);
	

}
