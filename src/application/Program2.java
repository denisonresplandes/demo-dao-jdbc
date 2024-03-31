package application;

import java.util.Optional;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		System.out.println("### TEST 1: department findAll ###");
		DepartmentDao depDao = DaoFactory.createDepartmentDao();
		depDao.findAll().forEach(System.out::println);
		
		System.out.println("\n### TEST 2: department findById ###");
		Optional<Department> optional = depDao.findById(2);
		optional.ifPresentOrElse(System.out::println, () -> System.out.println("null"));
	
		System.out.println("\n### TEST 3: department insert ###");
		Department newDepartment = new Department(null, "Test");
		depDao.insert(newDepartment);
		System.out.println("Inserted! New id = " + newDepartment.getId());
		
		System.out.println("\n### TEST 4: department update ###");
		depDao.findById(7).ifPresentOrElse(d -> {
			d.setName("Test update");
			depDao.update(d);
			System.out.println(d);
		}, () -> System.out.println("There is no department with specified id"));
		
		System.out.println("\n### TEST 5: department deleteById ###");
		depDao.deleteById(8);
	}
}