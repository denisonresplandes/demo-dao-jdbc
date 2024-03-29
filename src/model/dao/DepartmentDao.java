package model.dao;

import java.util.List;
import java.util.Optional;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department department);
	
	void update(Department department);
	
	void deleteById(Integer id);
	
	Optional<Department> findById(Integer id);
	
	List<Department> findAll();
}
