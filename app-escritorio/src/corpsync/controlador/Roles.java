package corpsync.controlador;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/* Clase que refleja y administra los roles correspondientes a los valores permitidos del campo rol de la BBDD.
 * Clase creada para facifitar el mantenimiento ante posibles cambios de estos valores en la BBDD. */

public class Roles {
	
	public final static String[] roles = new String[]{"empleado" , "tecnico" , "admin"};
	
	
public static boolean comprobarRoles( String rolAComprobar ) {
	
	boolean resultado = false;
	for(String rol : roles) {
		
		if(rolAComprobar.equals(rol)) {  resultado = true; }
	}		
		 return resultado ;
	}// Fin comprobarRoles
	
public static void inicializarComboBoxDeRoles (JComboBox<String> comboBox_Roles ) {
	
	for (String rol : roles ) {
		
		comboBox_Roles.addItem(rol);
	}
}///Fin inicializarRoles_ComboBox


}
