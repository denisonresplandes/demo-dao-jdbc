package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection connection;
	
	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Seller department) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(Seller department) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
	}

	@Override
	public Optional<Seller> findById(Integer id) {
		String sql = "SELECT seller.*, department.name as depName "
				+ "FROM seller INNER JOIN department "
				+ "ON seller.departmentId = department.id "
				+ "WHERE seller.id=?";
		ResultSet rs = null;
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			
			rs = statement.executeQuery();
			
			if (rs.next()) {
				Department department = mapDepartment(rs);
				Seller seller = mapSeller(rs, department);
				return Optional.of(seller);
			}
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
		}
		return Optional.empty();
	}


	@Override
	public List<Seller> findAll() {
		String sql = "SELECT seller.*, department.name AS depName FROM seller INNER JOIN department ON "
				+ "seller.departmentId = department.id ORDER BY seller.id";
		
		List<Seller> sellers = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql)) {
			while (rs.next()) {
				Department department = mapDepartment(rs);
				Seller seller = mapSeller(rs, department);
				sellers.add(seller);
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		return sellers;
	}
	
	private Department mapDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department(
			rs.getInt("departmentId"), 
			rs.getString("depName"));
		return dep;
	}
	
	private Seller mapSeller(ResultSet rs, Department department) throws SQLException {
		Seller seller = new Seller(
			rs.getInt("id"), 
			rs.getString("name"),
			rs.getString("email"), 
			rs.getDate("birthDate").toLocalDate(), 
			rs.getDouble("baseSalary"), 
			department);
		return seller;
	}
}
