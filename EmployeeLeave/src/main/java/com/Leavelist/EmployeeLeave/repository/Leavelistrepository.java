package com.Leavelist.EmployeeLeave.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Leavelist.EmployeeLeave.Entity.Leavelist;

public interface Leavelistrepository extends JpaRepository<Leavelist, Integer> {

	List<Leavelist> findByStatus(String status);
	List<Leavelist> findByEmployee(Integer integer);
	


	

}
