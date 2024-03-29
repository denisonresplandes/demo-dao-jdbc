package application;

import java.util.Optional;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		Optional<Seller> optional = sellerDao.findById(3);
		optional.ifPresentOrElse(System.out::println, () -> System.out.println("null"));
		
		System.out.println("\n");
		
		sellerDao.findAll().forEach(System.out::println);
	}
}