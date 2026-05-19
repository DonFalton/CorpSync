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

/**
 * Clase AccesoADatosBBDD: Tiene todos los métodos de acceso a la BBDD, para
 * obtener, guardar, modificar y borrar perfiles y encriptar contraseñas. Tiene
 * dos campos donde guarda las conexiones que usa. Cada una con permisos de
 * acceso distintos. Tiene un método para cerrar estas conexiónes. Trata y
 * reporta al frame desde el que se invoca los errores de valor de campo unico
 * repetido, campo obligatorio vacio y el de la restricción de valores
 * permitidos del campo rol.
 **/

public class AccesoADatosBBDD {

	private Connection con;
	private Connection con2;

	public AccesoADatosBBDD() {
		super();

		System.out.println("----------Cogiendo conexion normal, desde accesoADatos--------- ");
		con = ConexionBBDD.getConexion();
		con2 = ConexionBBDD.getConexion2();
	}

	public void cerrarConexion() {

		System.out.println("----------Cerrando conexion normal , desde accesoADatos--------- ");
		ConexionBBDD.cerrarConexion();

	}

	public List<Perfil> obtenerTodosLosPerfiles() {

		List<Perfil> perfilesList = new LinkedList<Perfil>();
		String sentencia = "select id, nombre , rol, departamento, email  from perfiles order by nombre";
		String sentencia2 = "select encrypted_password from auth.users where id = ?";
		ResultSet rs = null;
		Perfil perfil;
		try (Statement stm = con.createStatement(); PreparedStatement ps2 = con2.prepareStatement(sentencia2);) {
			rs = stm.executeQuery(sentencia);
			while (rs.next()) {
				perfil = new Perfil();
				UUID id = (UUID) rs.getObject("id");
				String psw = null;
				ps2.setObject(1, id);
				ResultSet rs2 = ps2.executeQuery();
				if (rs2.next()) {
					psw = rs2.getString(1);
				}
				// System.out.println("............encrypted_password : " + psw );

				perfil.setId(id);
				perfil.setNombre(rs.getString("nombre"));
				perfil.setRol(rs.getString("rol"));
				perfil.setDepartamento(rs.getString("departamento"));
				perfil.setEmail(rs.getString("email"));
				perfil.setContraseñaCifrada(psw);

				perfilesList.add(perfil);
			}

			stm.close();
		} catch (SQLException e) {
			ConexionBBDD.muestraErrorSQL(e);
		}

		return perfilesList;
	}

	/**
	 * Guarda el perfil pasado como parametro y reporta si hay algun fallo en el
	 * frame tambien pasado como parametro
	 **/
	public boolean guardarPerfilConContraseñaSinCifrar(Perfil perfil, JFrame ventanaOrigen) {

		String sentencia = "insert into perfiles( nombre , rol , departamento , email , password ) values( ? , ? , ? , ? , ? )";
		try (PreparedStatement ps = con.prepareStatement(sentencia);) {

			ps.setString(1, perfil.getNombre());
			ps.setString(2, perfil.getRol());
			ps.setString(3, perfil.getDepartamento());
			ps.setString(4, perfil.getEmail());
			ps.setString(5, perfil.getContraseñaSinCifrar());

			ps.executeUpdate();

		} catch (SQLException e) {

			ConexionBBDD.muestraErrorSQL(e);

			if (e.getSQLState().equals("23505")) {

				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD. El EMail del perfil : '"
						+ perfil + "' ya existe en la base de datos. Pruebe con otro.");
			} else if (e.getSQLState().equals("23502")) {

				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD en perfil : '" + perfil
						+ "'. Ninguno de estos campos puede estar vacio: nombre, rol, email y contraseña.");
			} else if (e.getSQLState().equals("23514")) {

				JOptionPane.showMessageDialog(ventanaOrigen,
						"Error al guardar en BBDD en perfil : '" + perfil + "'. Ese rol no existe.");
			}
			return false;
		}
		return true;
	}// Fin guardarPerfil

	/**
	 * Guarda el perfil pasado como parametro y reporta si hay algun fallo en el
	 * frame tambien pasado como parametro
	 **/
	public boolean guardarPerfilConContraseñaCifrada(Perfil perfil, JFrame ventanaOrigen) {

		int filasGuardadasPerfiles = 0;
		int filasActualizadasUsers = 0;

		String sentencia2 = "UPDATE auth.users " + "SET 	encrypted_password = ? " + "WHERE email = ?";

		String sentencia1 = "insert into perfiles( nombre , rol , departamento , email ) values( ? , ? , ? , ?  )";
		try (PreparedStatement ps = con.prepareStatement(sentencia1);
				PreparedStatement ps2 = con2.prepareStatement(sentencia2);) {
			con.setAutoCommit(false);
			con2.setAutoCommit(false);

			ps.setString(1, perfil.getNombre());
			ps.setString(2, perfil.getRol());
			ps.setString(3, perfil.getDepartamento());
			ps.setString(4, perfil.getEmail());

			filasGuardadasPerfiles = ps.executeUpdate();
			if ((filasGuardadasPerfiles) == 1) {
				con.commit();
			} else {
				con.setAutoCommit(true);
				con2.setAutoCommit(true);
				return false;
			}

			// System.out.println( "+ filas insertadas en perfiles = " +
			// filasGuardadasPerfiles);

			//////////////////////
			ps2.setString(1, perfil.getContraseñaCifrada());
			ps2.setObject(2, perfil.getEmail());

			// System.out.println( "+ !!!!!!!!!!! perfil.getContraseñaCifrada() ===== = " +
			// perfil.getContraseñaCifrada() +"..."+ perfil.getEmail());

			filasActualizadasUsers = ps2.executeUpdate();
			// System.out.println( "+ filas actualizadas en auth.users = " +
			// filasActualizadasUsers);
			/*
			 * //para pruevas if( (filasGuardadasPerfiles + filasActualizadasUsers ) != 2)
			 * {con.rollback(); con2.rollback();
			 * JOptionPane.showMessageDialog(ventanaOrigen,
			 * "Error desconocido al guardar en BBDD el perfil : '"+ perfil );
			 * System.out.println("!! No se pudo guardar el perfil :" + perfil +
			 * "  Sin SQLException lanzada ?? : "); }else{con.commit(); con2.commit();}
			 * //Tiene que actualizar en las dos tablas o en ninguna. Redundante?
			 */
			con2.commit();
			con.setAutoCommit(true);
			con2.setAutoCommit(true);

		} catch (SQLException e) {

			// e.printStackTrace();

			System.out.println("SQL ERROR mensaje: " + e.getMessage());
			System.out.println("-----------SQL codigo especifico: " + e.getSQLState());
			if (e.getSQLState().equals("23505")) {

				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD. El EMail del perfil : '"
						+ perfil + "' ya existe en la base de datos. Pruebe con otro.");
			} else if (e.getSQLState().equals("23502")) {

				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD en perfil : '" + perfil
						+ "'. Ninguno de estos campos puede estar vacio: nombre, rol, email y contraseña.");
			} else if (e.getSQLState().equals("23514")) {

				JOptionPane.showMessageDialog(ventanaOrigen,
						"Error al guardar en BBDD en perfil : '" + perfil + "'. Ese rol no existe.");
			} else {
				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD en perfil : '" + perfil);
			}
			try {
				con.rollback();
				con.setAutoCommit(true);
				con2.rollback();
				con2.setAutoCommit(true);
			} catch (SQLException e1) {
				ConexionBBDD.muestraErrorSQL(e1);
			}
			return false;
		}
		return ((filasGuardadasPerfiles + filasActualizadasUsers) == 2) ? true : false;
	}// Fin guardarPerfilConContraseñaCifrada()

	/*
	 * Actualiza un perfil de usuario y devuelve true o false segun el exito o
	 * fracaso de la operacion
	 */
	public boolean actualizarPerfil(Perfil perfil, JFrame ventanaOrigen) {

		String sentencia1 = "UPDATE perfiles " + "SET nombre = ?, rol = ?, departamento = ?, email = ?  "
				+ "WHERE id = ?";
		String sentencia2 = "UPDATE auth.users " + "SET 	email = ? " + "WHERE id = ?";
		// System.out.println(sentencia);
		int filasActualizadas = 0;

		try (PreparedStatement statement1 = con.prepareStatement(sentencia1);
				PreparedStatement statement2 = con2.prepareStatement(sentencia2);) {

			con.setAutoCommit(false);
			con2.setAutoCommit(false);

			statement1.setString(1, perfil.getNombre());
			statement1.setString(2, perfil.getRol());
			statement1.setString(3, perfil.getDepartamento());
			statement1.setString(4, perfil.getEmail());
			statement1.setObject(5, perfil.getId());
			filasActualizadas += statement1.executeUpdate();
			System.out.println("filasActualizadas en perfiles = " + filasActualizadas);

			statement2.setString(1, perfil.getEmail());
			statement2.setObject(2, perfil.getId());
			filasActualizadas += statement2.executeUpdate();
			System.out.println("+ filasActualizadas en auth.users = " + filasActualizadas);

			if (filasActualizadas != 2) {
				con.rollback();
				con2.rollback();
			} else {
				con.commit();
				con2.commit();
			} // Tiene que actualizar en las dos tablas o en ninguna. Redundante?

			con.setAutoCommit(true);
			con2.setAutoCommit(true);
		} catch (SQLException e) {

			ConexionBBDD.muestraErrorSQL(e);
			if (e.getSQLState().equals("23505")) {

				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD. El EMail del perfil : '"
						+ perfil + "' ya existe en la base de datos. Pruebe con otro.");
			}
			if (e.getSQLState().equals("23502")) {

				JOptionPane.showMessageDialog(ventanaOrigen, "Error al guardar en BBDD en perfil : '" + perfil
						+ "'. Ninguno de estos campos puede estar vacio: nombre, rol, email y contraseña.");
			}

			try {
				con.rollback();
				con.setAutoCommit(true);
				con2.rollback();
				con2.setAutoCommit(true);
			} catch (SQLException e1) {
				ConexionBBDD.muestraErrorSQL(e1);
			}
			return false;
		}
		return (filasActualizadas == 2) ? true : false;
	}// Fin borrarPerfilPorId

	/*
	 * Borra un perfil de usuario segun el id pasado como parametro y devuelve true
	 * o false segun el exito o fracaso de la operacion
	 */
	public boolean borrarPerfil(UUID id, JFrame ventanaOrigen) {

		String sentencia = "DELETE FROM perfiles WHERE id = ?";

		int filasBorradas = 0;

		try (PreparedStatement statement = con.prepareStatement(sentencia);) {

			statement.setObject(1, id);
			filasBorradas += statement.executeUpdate();
			System.out.println("** filasBorradas en perfiles = " + filasBorradas);

		} catch (SQLException e) {
			ConexionBBDD.muestraErrorSQL(e);
			JOptionPane.showMessageDialog(ventanaOrigen, "Error al borrar en BBDD el perfil.");
			return false;
		}
		return true;

	}// Fin borrarPerfilPorId

	public String cifrarContraseña(String contraseñaSinCifrar, JFrame ventanaOrigen) {

		String contraseñaCifrada = null;

		String sentencia2 = "select crypt( ? , gen_salt('bf', 6))";
		try (PreparedStatement ps2 = con2.prepareStatement(sentencia2);) {

			ps2.setString(1, contraseñaSinCifrar);
			ResultSet rs2 = ps2.executeQuery();
			if (rs2.next()) {
				contraseñaCifrada = rs2.getString(1);
			} else {
				JOptionPane.showMessageDialog(ventanaOrigen, "No se pudo oftener la contraseña cifrada.");
			}
			System.out.println(",.,. Contraseña ya Cifrada  : " + contraseñaCifrada);

		} catch (SQLException e) {
			ConexionBBDD.muestraErrorSQL(e);
			JOptionPane.showMessageDialog(ventanaOrigen, "Error al cifrar la contraseña.");
			e.printStackTrace();
		}
		return contraseñaCifrada;
	} // Fin cifrarContraseña()

	public boolean guardarContraseña(UUID id, String contraseñaCifrada, JFrame ventanaOrigen) {

		String sentencia = "UPDATE auth.users " + "SET 	encrypted_password = ? " + "WHERE id = ?";

		int filasActualizadas = 0;

		try (PreparedStatement statement = con2.prepareStatement(sentencia);) {

			statement.setString(1, contraseñaCifrada);
			statement.setObject(2, id);
			filasActualizadas = statement.executeUpdate();
			System.out.println("+ filasActualizadas en auth.users = " + filasActualizadas);

		} catch (SQLException e) {

			ConexionBBDD.muestraErrorSQL(e);
			return false;
		}
		return true;
	}// Fin guardarContraseña()

}// FINNNNNN
