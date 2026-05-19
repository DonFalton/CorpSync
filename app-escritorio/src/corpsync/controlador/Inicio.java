package corpsync.controlador;

import java.awt.EventQueue;

import corpsync.vista.VentanaPrincipal;


/** clase Inicio: Clase de comienzo de la aplicacion. Carga la ventana principal. **/
public class Inicio {

	public static void main(String[] args) {

		        EventQueue.invokeLater(() -> {
		            new VentanaPrincipal().setVisible(true);
		        });
		        
		    }			
}
