package model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Seller implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String email;
	private LocalDate birthDate;
	private Double baseSalary;
	
	private Department department;
	
	public Seller() { }
	
	public Seller(Integer id, String name, String email, LocalDate birthDate, Double baseSalary,
			Department department) {
		//TODO validations
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthDate = birthDate;
		this.baseSalary = baseSalary;
		this.department = department;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	
	public void setName(String name) {
		// TODO validations.
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public Double getBaseSalary() {
		return baseSalary;
	}
	
	public void setBaseSalary(Double baseSalary) {
		// TODO validation
		this.baseSalary = baseSalary;
	}
	
	public Department getDepartment() {
		return department;
	}
	
	public void setDepartment(Department department) {
		Objects.nonNull(department);
		this.department = department;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seller other = (Seller) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return String.format("Seller -> [id=%s, name=%s, email=%s, bithDate=%s, baseSalary=%s, %s]", 
				getId(),
				getName(),
				getEmail(),
				getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
				getBaseSalary(),
				getDepartment());
	}
}
