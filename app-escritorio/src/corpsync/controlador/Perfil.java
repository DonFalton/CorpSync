package corpsync.controlador;

import java.util.UUID;

import com.opencsv.bean.CsvBindByName;


/**  Clase Perfil: Entidad que representa un perfil de la base de datos. Es una JavaBean. Publica, con el Constructor vacío (sin argumentos)
 *  y atributos privados con sus getter y setter públicos correspondientes. También tiene otros constructores necesarios para el resto de clases. 
 *  Tiene sobreescrito el método toString() para reflejar los campos más relevantes, nombre y email, cuando se cargan en una Jlist y cuando se roportan errores. 
 *  Esta entidad se usa en el proceso de carga y guardado por medio de la libreria OpenCSV y sus campos están marcados con la etiqueta @CsvBindByName(column = "nombre del campo"),
 *   necesaria para este proceso. Tambien la usan los métodos de persistencia en base de datos y los de comprobarción de la clase Utils. **/
public class Perfil {
	
	
	public UUID id;
	 @CsvBindByName(column = "nombre")
	public String nombre;
	 @CsvBindByName(column = "rol")
	public String rol;
	 @CsvBindByName(column = "departamento")
	public String departamento;
	 @CsvBindByName(column = "email")
	public String email;
	public String contraseñaSinCifrar; 
	 @CsvBindByName(column = "contraseña")
	public String contraseñaCifrada; 
	
	
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
		this.contraseñaCifrada = contraseña;
	}

	public Perfil(String nombre, String rol, String departamento, String email, String contraseña) {
		super();
		this.nombre = nombre;
		this.rol = rol;
		this.departamento = departamento;
		this.email = email;
		this.contraseñaCifrada = contraseña;
	}

	public Perfil( String nombre, String rol, String departamento, String email) {
		super();		
		this.nombre = nombre;
		this.rol = rol;
		this.departamento = departamento;
		this.email = email;
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
	public String getContraseñaCifrada() {
		return contraseñaCifrada;
	}

	public void setContraseñaCifrada(String contraseñaCifrada) {
		this.contraseñaCifrada = contraseñaCifrada;
	}
	
	public String getContraseñaSinCifrar() {
		return contraseñaSinCifrar;
	}
	public void setContraseñaSinCifrar(String contraseña) {
		this.contraseñaSinCifrar = contraseña;
	}
	
	
	@Override
	public String toString() {
		return  nombre + " -- " + email ;
	}
	
	
	/** Hace una copia de este objeto perfil. **/
	public Perfil crearCopia() {
		
		//UUID id = new UUID(this.getId().getMostSignificantBits() ,this.getId().getLeastSignificantBits() );
		
		Perfil perfilCopia = new Perfil(nombre, rol , departamento, email);
		perfilCopia.setId(id);
		perfilCopia.setContraseñaCifrada(contraseñaCifrada);
		return perfilCopia ;

	}	
	
}
