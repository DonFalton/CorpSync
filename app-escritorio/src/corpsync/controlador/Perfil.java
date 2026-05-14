package corpsync.controlador;

import java.util.UUID;

import com.opencsv.bean.CsvBindByName;


public class Perfil {
	
	
	public UUID id;
	 @CsvBindByName(column = "nombre")
	 //@CsvBindByPosition(position = 0)
	public String nombre;
	 @CsvBindByName(column = "rol")
	 //@CsvBindByPosition(position = 1)
	public String rol;
	 @CsvBindByName(column = "departamento")
	 //@CsvBindByPosition(position = 2)
	public String departamento;
	 @CsvBindByName(column = "email")
	 //@CsvBindByPosition(position = 3)
	public String email;
	 @CsvBindByName(column = "contraseña")
	public String contraseña; //= "????????????????";
	
	
	public Perfil() {
		super();
	}

	public Perfil(UUID id, String nombre, String rol, String departamento, String email, String contraseña) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.rol = rol;
		this.departamento = departamento;
		this.email = email;
		this.contraseña = contraseña;
	}

	public Perfil(String nombre, String rol, String departamento, String email, String contraseña) {
		super();
		this.nombre = nombre;
		this.rol = rol;
		this.departamento = departamento;
		this.email = email;
		this.contraseña = contraseña;
	}






	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	
	public String getContraseña() {
		return contraseña;
	}






	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}






	@Override
	public String toString() {
		return  nombre + " -- " + email ;
	}
	
	
	/* Hace una copia de este objeto perfil*/
	public Perfil crearCopia() {
		
		//UUID id = new UUID(this.getId().getMostSignificantBits() ,this.getId().getLeastSignificantBits() );
		
		Perfil perfilCopia = new Perfil(this.id, this.nombre, this.rol , this.departamento, this.email, this.contraseña);
		
		return perfilCopia ;

	}
	
	
	
	
	
	
	
}
