package application;

import java.time.LocalDate;
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
		
//		System.out.println("\n### TEST 4: seller insert ###");
//		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", LocalDate.now(), 
//				4000.00, new Department(2, null));
//		sellerDao.insert(newSeller);
//		System.out.println("Inserted! New id = " + newSeller.getId());
		
		System.out.println("\n### TEST 5: seller update ###");
		sellerDao.findById(1).ifPresentOrElse(s -> {
			s.setName("Martha Waine");
			sellerDao.update(s);
			System.out.println(s);
		}, () -> System.out.println("There is no seller with an informed id"));
	}
}