package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection connection;
	
	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Department department) {
		String sql = "INSERT INTO department (name) VALUES (?)";
		
		try (PreparedStatement statement = connection.prepareStatement(sql, 
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, department.getName());
			
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet rs = statement.getGeneratedKeys()) {
					if (rs.next()) {
						int id = rs.getInt(1);
						department.setId(id);
					}
				}
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
	public void update(Department department) {
		String sql = "UPDATE department SET name=? WHERE id=?";
		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, department.getName());
			statement.setInt(2, department.getId());
			
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public void deleteById(Integer id) {
		String sql = "DELETE FROM department WHERE id=?";
		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public Optional<Department> findById(Integer id) {
		String sql = "SELECT * FROM department WHERE id=?";
		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapDepartment(rs));
				}
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		return Optional.empty();
	}

	@Override
	public List<Department> findAll() {
		String sql = "SELECT * FROM department";
		
		List<Department> departments = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql)) {
			while (rs.next()) {
				departments.add(mapDepartment(rs));
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		return departments;
	}

	private Department mapDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department(
			rs.getInt("id"), 
			rs.getString("name"));
		return dep;
	}
}