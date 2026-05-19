package corpsync.controlador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;


/**
 * Clase Utils: Tiene una variedad de métodos con funcionalidades que se usan
 * repetidamente en las otras clases, desacoplando estas funcionalidades de las
 * clases que las usan y facilitando así su mantenimiento y reusabilidad.
 **/

public class Utils {

	/**
	 * static boolean limtiarYComprobarPerfilConContraseñaSinCifrar( Perfil perfil ,
	 * JFrame frameReportar): Limpia los espacios vacíos de los atributos de un
	 * objeto perfil. Comprueba que no estén vacíos los atributos nombre, email y
	 * ContraseñaSinCifrar y que el atributo rol coincida con los valores permitidos
	 * en la BBDD. Reporta en el Frame indicado el resultado de la comprobación si
	 * no son correctos. Devuelve además true o false según esta comprobación.
	 **/
	public static boolean limtiarYComprobarPerfilConContraseñaSinCifrar(Perfil perfil, JFrame frameReportar) {

		perfil.setNombre(perfil.getNombre().strip());
		perfil.setRol(perfil.getRol().strip());
		perfil.setDepartamento(perfil.getDepartamento().strip());
		perfil.setEmail(perfil.getEmail().strip());
		perfil.setContraseñaSinCifrar(perfil.getContraseñaSinCifrar().strip());

		if (perfil.getNombre().equals("")) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo nombre no puede estar vacio");
			return false;
		}
		if (perfil.getEmail().equals("")) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo email no puede estar vacio");
			return false;
		}
		if (perfil.getContraseñaSinCifrar().equals("")) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre >:  " + perfil.getNombre() + ", el campo contraseña no puede estar vacio");
			return false;
		}
		if (!Roles.comprobarRoles(perfil.getRol())) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo rol no esta entre los permitidos");
			return false;
		}

		return true;
	}

	/**
	 * static boolean limtiarYComprobarPerfilConContraseñaSinCifrar( Perfil perfil ,
	 * JFrame frameReportar): Limpia los espacios vacíos de los atributos de un
	 * objeto perfil. Comprueba que no estén vacíos los atributos nombre, email y
	 * ContraseñaCifrada y que el atributo rol coincida con los valores permitidos
	 * en la BBDD. Reporta en el Frame indicado el resultado de la comprobación si
	 * no son correctos. Devuelve además true o false según esta comprobación.
	 **/
	public static boolean limtiarYComprobarPerfilConContraseñaCifrada(Perfil perfil, JFrame frameReportar) {

		perfil.setNombre(perfil.getNombre().strip());
		perfil.setRol(perfil.getRol().strip());
		perfil.setDepartamento(perfil.getDepartamento().strip());
		perfil.setEmail(perfil.getEmail().strip());
		perfil.setContraseñaCifrada(perfil.getContraseñaCifrada().strip());

		if (perfil.getNombre().equals("")) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo nombre no puede estar vacio");
			return false;
		}
		if (perfil.getEmail().equals("")) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo email no puede estar vacio");
			return false;
		}
		if (perfil.getContraseñaCifrada().equals("")) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo contraseña no puede estar vacio");
			return false;
		}
		if (!Roles.comprobarRoles(perfil.getRol())) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo rol no esta entre los permitidos");
			return false;
		}

		return true;
	}

	/**
	 * static boolean limtiarYComprobarPerfilSinContraseña( Perfil perfil , JFrame
	 * frameReportar): Limpia los espacios vacíos de los atributos de un objeto
	 * perfil. Comprueba que no estén vacíos los atributos nombre y email y que el
	 * atributo rol coincida con los valores permitidos en la BBDD. Reporta en el
	 * Frame indicado el resultado de la comprobación si no son correctos. Devuelve
	 * además true o false según esta comprobación.
	 **/
	public static boolean limtiarYComprobarPerfilSinContraseña(Perfil perfil, JFrame frameReportar) {

		perfil.setNombre(perfil.getNombre().strip());
		perfil.setRol(perfil.getRol().strip());
		perfil.setDepartamento(perfil.getDepartamento().strip());
		perfil.setEmail(perfil.getEmail().strip());

		if (perfil.getNombre().equals("")) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo nombre no puede estar vacio");
			return false;
		}
		if (perfil.getEmail().equals("")) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo email no puede estar vacio");
			return false;
		}
		if (!Roles.comprobarRoles(perfil.getRol())) {
			JOptionPane.showMessageDialog(frameReportar,
					"En el pefil con nombre:  " + perfil.getNombre() + ", el campo rol no esta entre los permitidos");
			return false;
		}

		return true;
	}

	/**
	 * void cargarListaDePerfilesEnJList(List<Perfil> listaDePerfiles
	 * ,DefaultListModel<Perfil> modelo_jListDePerfiles ) : Carga una List de
	 * perfiles, pasada como parametro en una JLists.
	 **/
	public static void cargarListaDePerfilesEnJList(List<Perfil> listaDePerfiles,
			DefaultListModel<Perfil> modelo_jListDePerfiles) {

		// List<Perfil> list_Perfiles = accesoBBDD.obtenerTodosLosPerfiles() ;
		// modelo_jListDePerfiles.clear();

		for (Perfil perfil : listaDePerfiles) {

			// System.out.println( "Perfil cargado:" + perfil + "--contraseña: " +
			// perfil.getContraseña() + "--" );
			modelo_jListDePerfiles.addElement(perfil);
		}
	}//// Fin cargarListaPerfiles()

	/**
	 * void eliminarListaDePerfilesEnJList(List<Perfil> listaDePerfiles
	 * ,DefaultListModel<Perfil> modelo_jListDePerfiles ):Elimina los perfiles
	 * pasados en una lista de perfiles de una JLists.
	 **/
	public static void eliminarListaDePerfilesEnJList(List<Perfil> listaDePerfiles,
			DefaultListModel<Perfil> modelo_jListDePerfiles) {

		for (Perfil perfil : listaDePerfiles) {

			// p.println(perfil);
			modelo_jListDePerfiles.removeElement(perfil);
		}
	}//// Fin cargarListaPerfiles()

	/**
	 * 1. List<Perfil> crearListaDePerfilesDeJList(DefaultListModel<Perfil>
	 * modelo_jListDePerfiles ): Crea una lista de perfiles de la JLists del modelo
	 * que se pasa como parametro.
	 **/
	public static List<Perfil> crearListaDePerfilesDeJList(DefaultListModel<Perfil> modelo_jListDePerfiles) {

		List<Perfil> listaPerfiles = new ArrayList<Perfil>();
		for (Object perfil : modelo_jListDePerfiles.toArray()) {

			// p.println(perfil);
			listaPerfiles.add((Perfil) perfil);
		}
		return listaPerfiles;
	}//// Fin cargarListaPerfiles()

	/**
	 * static void ordenarJList( JList<Perfil> jListDesordenada ) : Ordena la JList
	 * de perfiles pasada como parametro segun el valor del atributo nombre.
	 **/

	public static void ordenarJList(JList<Perfil> jListDesordenada) {

		DefaultListModel<Perfil> modelo = (DefaultListModel<Perfil>) jListDesordenada.getModel();

		List<Perfil> lista = crearListaDePerfilesDeJList(modelo);
		// List<Perfil> listaOrdenada =
		lista.sort(Comparator.comparing(Perfil::getNombre));
		modelo.clear();
		cargarListaDePerfilesEnJList(lista, modelo);

	}//// Fin ordenarJList()

	/**
	 * static boolean comprobarSiContraseñaCifradaFormatoCorrecto( String
	 * contraseñaCifrada , JFrame frameReportar) : Comprueba si la contraseña pasada
	 * como paramero tiene el formato que se usa en la dase de datos.
	 **/
	public static boolean comprobarSiContraseñaCifradaFormatoCorrecto(String contraseñaCifrada, JFrame frameReportar) {

		boolean contraseñaCorrecta = contraseñaCifrada.matches("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");
		if (!contraseñaCorrecta) {
			JOptionPane.showMessageDialog(frameReportar, "El formato de contraseña no es correcto.");
		}

		return contraseñaCorrecta;
	}

	/**
	 * static boolean comprobarStringNoVacio( String cadena , JFrame frameReportar)
	 * : Comprueba si el String pasada como paramero no esta vacio o si es una
	 * cadena solo de espacios.
	 **/
	public static boolean comprobarStringNoVacio(String cadena, JFrame frameReportar) {

		if ((cadena.strip()).equals("")) {
			JOptionPane.showMessageDialog(frameReportar, "El campo no puede estar vacio");
			return false;
		} else {
			return true;
		}
	}// Fin comprobarStringNoVacio( )

}// --------------------Fin de
	// Clase--------------------------------------------------------------------------------------------------------------------------------
