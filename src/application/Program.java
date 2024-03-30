package application;

import java.util.Optional;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("### TEST 1: seller findById ###");
		Optional<Seller> optional = sellerDao.findById(3);
		optional.ifPresentOrElse(System.out::println, () -> System.out.println("null"));
		
		System.out.println("\n### TEST 2: seller findAll ###");
		sellerDao.findAll().forEach(System.out::println);
		
		System.out.println("\n### TEST 3: seller findByDepartment ###");
		sellerDao.findByDepartment(new Department(2, null))
			.forEach(System.out::println);
	}
}