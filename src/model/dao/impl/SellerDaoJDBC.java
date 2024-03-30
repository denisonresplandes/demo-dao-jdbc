package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
	public void insert(Seller seller) {
		String sql = "INSERT INTO seller (name, email, birthDate, baseSalary, departmentId) "
				+ "VALUES (?,?,?,?,?)";
		try (PreparedStatement statement = connection.prepareStatement(sql, 
				Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, seller.getName());
			statement.setString(2, seller.getEmail());
			statement.setDate(3, Date.valueOf(seller.getBirthDate()));
			statement.setDouble(4, seller.getBaseSalary());
			statement.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = statement.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
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
	public List<Seller> findByDepartment(Department department) {
		String sql = "SELECT seller.*, department.name AS depName FROM seller "
				+ "INNER JOIN department ON seller.departmentId = department.id "
				+ "WHERE departmentId = ? ORDER BY name";
		
		ResultSet rs = null;
		List<Seller> sellers = new ArrayList<>();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, department.getId());
			
			rs = statement.executeQuery();

			Map<Integer, Department> map = new HashMap<>();
			while (rs.next()) {
				Department dep = map.get(rs.getInt("departmentId"));
				if (Objects.isNull(dep)) {
					dep = mapDepartment(rs);
					map.put(rs.getInt("departmentId"), dep);
				}
				Seller seller = mapSeller(rs, dep);
				sellers.add(seller);
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
		}
		return sellers;
	}


	@Override
	public List<Seller> findAll() {
		String sql = "SELECT seller.*, department.name AS depName FROM seller "
				+ "INNER JOIN department ON seller.departmentId = department.id "
				+ "ORDER BY seller.id";
		
		List<Seller> sellers = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql)) {
			
			Map<Integer, Department> map = new HashMap<>();
			while (rs.next()) {
				Department department = map.get(rs.getInt("departmentId")); 
				if (Objects.isNull(department)) {
					department = mapDepartment(rs);
					map.put(rs.getInt("departmentId"), department);
				}
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
