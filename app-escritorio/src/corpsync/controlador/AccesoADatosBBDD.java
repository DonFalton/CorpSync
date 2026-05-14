package corpsync.controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class AccesoADatosBBDD {

	//static PrintStream p = System.out;

	////// depurar

	private Connection con = ConexionBBDD.conexion();
	public AccesoADatosBBDD bbdd = this; ////////////
	static int ERROR_SQL = -1;/////////////
	static int ERROR_NOMBRE_REPETIDO = -2;//////////////////
	
	
	
	
	
	public void cerrarConexion()  {
		try {
	 		  if (con != null && !con.isClosed()) {
				 con.close();
	                System.out.println("Conexión cerrada");
	            }			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	}
	
	

	public AccesoADatosBBDD getBbdd() {
		return bbdd;
	}
	
	
	public List<Perfil> obtenerTodosLosPerfiles() {

		//Connection con = ConexionBBDD.conexion();/////--------prov
		
		List<Perfil> perfilesList = new LinkedList<Perfil>();
		String sentencia = "select id, nombre , rol, departamento, email  from perfiles";
		ResultSet rs = null;
		Perfil perfil;
		try (Statement stm = con.createStatement()) {
			// stm = con.createStatement();
			rs = stm.executeQuery(sentencia);
			while (rs.next()) {
				perfil = new Perfil();

				perfil.setId((UUID) rs.getObject("id")); 
				perfil.setNombre(rs.getString("nombre"));
				perfil.setRol(rs.getString("rol"));
				perfil.setDepartamento (rs.getString("departamento"));
				perfil.setEmail(rs.getString("email"));
			
				 //p.println("e - " + perfil);
				 

				perfilesList.add(perfil);
			}

			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*finally{try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	}*/
		
		return perfilesList;
	}
	
	
	public boolean guardarPerfil(Perfil perfil, JFrame ventanaOrigen) {

	
		//Connection con = ConexionBBDD.conexion();/////--------prov
		
		String sentencia = "insert into perfiles( nombre , rol , departamento , email , password ) values( ? , ? , ? , ? , ? )";
		try (/*Connection con = ConexionBBDD.conexion();*/ PreparedStatement ps = con.prepareStatement(sentencia);) {
			
			ps.setString(1, perfil.getNombre());
			ps.setString(2, perfil.getRol ());
			ps.setString(3, perfil.getDepartamento());
			ps.setString(4, perfil.getEmail());
			ps.setString(5, perfil.getContraseña());

			ps.executeUpdate();
			// ps.close();
		} catch (SQLException e) {
			
			//e.printStackTrace();
			
			System.out.println("SQL ERROR mensaje: " + e.getMessage());
			System.out.println("-----------SQL codigo especifico: " + e.getSQLState());
			if (e.getSQLState().equals("23505") ) {
				
				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD. El EMail del perfil : '"+ perfil +"' ya existe en la base de datos. Pruebe con otro.");
			}else if  (e.getSQLState().equals("23502") ) {
				
				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD en perfil : '" + perfil + "'. Ninguno de estos campos puede estar vacio: nombre, rol, email y contraseña.");
			}else if  (e.getSQLState().equals("23514") ) {
				
				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD en perfil : '" + perfil  + "'. Ese rol no existe.");
			}
			return false ;
		}
		return true ;
	}//Fin guardarPerfil
	
	
	/*Actualiza un perfil de usuario y devuelve true o false segun el exito o fracaso de la operacion  */
	public  boolean actualizarPerfil(Perfil perfil, JFrame ventanaOrigen) {

		//Connection con = ConexionBBDD.conexion();/////--------prov
		
		//delete FROM perfiles where id =  '6f45e840-2aeb-4037-86b7-c4d658794930';
		String sentencia1 = "UPDATE perfiles "
							+ "SET nombre = ?, rol = ?, departamento = ?, email = ?  "
							+ "WHERE id = ?";
		String sentencia2 = "UPDATE auth.users "
							+ "SET 	email = ? "
							+ "WHERE id = ?";
		//System.out.println(sentencia);
		int filasActualizadas = 0;
		
		try (PreparedStatement statement1 = con.prepareStatement(sentencia1);
				PreparedStatement statement2 = con.prepareStatement(sentencia2);){
			
			con.setAutoCommit(false);
			
			statement1.setString(1, perfil.getNombre());
			statement1.setString(2, perfil.getRol());
			statement1.setString(3, perfil.getDepartamento());
			statement1.setString(4, perfil.getEmail());
			statement1.setObject(5, perfil.getId());
			filasActualizadas += statement1.executeUpdate();
			System.out.println( "filasActualizadas en perfiles = " +  filasActualizadas);
			
			statement2.setString(1, perfil.getEmail());
			statement2.setObject(2, perfil.getId());
			filasActualizadas += statement2.executeUpdate();
			System.out.println( "+ filasActualizadas en auth.users = " +  filasActualizadas);	
			
			if(filasActualizadas != 2) {con.rollback();}else{con.commit();} //Tiene que borrar en las dos tablas o en ninguna. Redundante?
			
			con.setAutoCommit(true);		 
		} catch (SQLException e) {
			
			//e.printStackTrace();
			
			System.out.println("SQL ERROR mensaje: " + e.getMessage());
			System.out.println("-----------SQL codigo especifico: " + e.getSQLState());
			if (e.getSQLState().equals("23505") ) {
				
				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD. El EMail del perfil : '"+ perfil +"' ya existe en la base de datos. Pruebe con otro.");
			}
			if (e.getSQLState().equals("23502") ) {
				
				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD en perfil : '" + perfil  + "'. Ninguno de estos campos puede estar vacio: nombre, rol, email y contraseña.");
			}
			
			try {
				con.rollback();
				con.setAutoCommit(true);
				return  false;	
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		
			return (filasActualizadas == 2) ? true : false;		
		}// Fin borrarPerfilPorId
	





/*Borra un perfil de usuario segun el id pasado como parametro y devuelve true o false segun el exito o fracaso de la operacion  */
public  boolean borrarPerfil(UUID id , JFrame ventanaOrigen ) {

	String sentencia = "DELETE FROM perfiles WHERE id = ?";

	int filasBorradas = 0;
	
	try (PreparedStatement statement = con.prepareStatement(sentencia);){
		
		statement.setObject(1, id);
		filasBorradas += statement.executeUpdate();
		System.out.println( "filasBorradas en perfiles = " +  filasBorradas);
		 
	} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ventanaOrigen, "Error al borrar en BBDD el perfil ");
			return  false;	
		}
	return  true;
	
	}// Fin borrarPerfilPorId









}// FINNNNNN


