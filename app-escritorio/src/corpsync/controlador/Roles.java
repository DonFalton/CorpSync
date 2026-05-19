package corpsync.controlador;

import javax.swing.JComboBox;

/**
 * Clase Roles: Esta clase refleja y administra los roles correspondientes a los
 * valores permitidos del campo rol de la BBDD. Clase creada para facilitar el
 * mantenimiento ante posibles cambios de estos valores en la BBDD. Tiene un
 * campo estático llamado roles que guarda todos los valores permitidos en el
 * campo roles de la base de datos y se usa para inicializar los componentes
 * combobox de roles. En caso de cambiar estos valores en la base de datos solo
 * haría falta reflejar los cambios aquí.
 **/

public class Roles {

	public final static String[] roles = new String[] { "empleado", "tecnico", "admin" };

	/**
	 * 1. public static boolean comprobarRoles( String rolAComprobar): Comprueba si
	 * el rol pasado como parámetro es admisible. Se usa para comprobar los campos
	 * guardados y extraídos de los archivos CSV y de los que se guardan en la base
	 * de datos.
	 **/
	public static boolean comprobarRoles(String rolAComprobar) {

		boolean resultado = false;
		for (String rol : roles) {

			if (rolAComprobar.equals(rol)) {
				resultado = true;
			}
		}
		return resultado;
	}// Fin comprobarRoles

	/**
	 * public static void inicializarComboBoxDeRoles (JComboBox<String>
	 * comboBox_Roles ): Como su nombre indica se usa para inicializar los valores
	 * de los combobox pasados como parametro.
	 **/
	public static void inicializarComboBoxDeRoles(JComboBox<String> comboBox_Roles) {

		for (String rol : roles) {

			comboBox_Roles.addItem(rol);
		}
	}/// Fin inicializarRoles_ComboBox

}
