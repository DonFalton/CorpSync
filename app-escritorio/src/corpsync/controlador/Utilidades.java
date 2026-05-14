package corpsync.controlador;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import corpsync.controlador.Perfil;

public class Utilidades {

	

	
	

	
	/* Limpia los espacios vacios de los atributos de un objeto perfil. Comprueba  que no esten vacios los atributos los 
	 * atributos nombre,  email y contraseña y que el atributos rol coincida con los valores permitidos en la BBDD y reporta en el Frame 
	 * indicado el resultado de la comprobacion, en el frame pasado como parametro, si no son correcto. Devuelve ademas true o false segun esta comprovacion. */
	public static boolean limtiarYComprobarPerfilConContraseña( Perfil perfil , JFrame frameReportar) {
		
		
		perfil.setNombre(perfil.getNombre().strip()) ;
		perfil.setRol(perfil.getRol().strip()) ;
		perfil.setDepartamento(perfil.getDepartamento().strip()) ;
		perfil.setEmail(perfil.getEmail().strip()) ;
		perfil.setContraseña(perfil.getContraseña().strip()) ;
				 
		 if(perfil.getNombre().equals("")) {JOptionPane.showMessageDialog(frameReportar, "En el pefil con nombre:  " + perfil.getNombre() + ", el campo nombre no puede estar vacio"); return false; }
		 if(perfil.getEmail().equals("")) {JOptionPane.showMessageDialog(frameReportar, "En el pefil con nombre:  " + perfil.getNombre() + ", el campo email no puede estar vacio"); return false;}
		 if(perfil.getContraseña().equals("")) {JOptionPane.showMessageDialog(frameReportar, "En el pefil con nombre:  " + perfil.getNombre() + ", el campo contraseña no puede estar vacio"); return false;}
		 if( ! Roles.comprobarRoles(perfil.getRol()) ) {JOptionPane.showMessageDialog(frameReportar, "En el pefil con nombre:  " + perfil.getNombre() + ", el campo rol no esta entre los permitidos"); return false; }
		 
		 return true;
	}
	
	
	/* Limpia los espacios vacios de los atributos de un objeto perfil.Comprueba  que no esten vacios los atributos los 
	 * atributos nombre y email y que el atributos rol coincida con los valores permitidos en la BBDD y reporta en el Frame 
	 * indicado el resultado de la comprobacion, en el frame pasado como parametro, si no son correcto.  A diferencia del anterior permite que el campo contraseña este vacio. 
	 *  Devuelve ademas true o false segun esta comprovacion. */
	public static boolean limtiarYComprobarPerfil( Perfil perfil , JFrame frameReportar) {
		
		
		perfil.setNombre(perfil.getNombre().strip()) ;
		perfil.setRol(perfil.getRol().strip()) ;
		perfil.setDepartamento(perfil.getDepartamento().strip()) ;
		perfil.setEmail(perfil.getEmail().strip()) ;
		perfil.setContraseña(perfil.getContraseña().strip()) ;
				 
		 if(perfil.getNombre().equals("")) {JOptionPane.showMessageDialog(frameReportar, "En el pefil con nombre:  " + perfil.getNombre() + ", el campo nombre no puede estar vacio"); return false; }
		 if(perfil.getEmail().equals("")) {JOptionPane.showMessageDialog(frameReportar, "En el pefil con nombre:  " + perfil.getNombre() + ", el campo email no puede estar vacio"); return false;}
		// if(perfil.getContraseña().equals("")) {JOptionPane.showMessageDialog(frameReportar, "En el pefil con nombre:  " + perfil.getNombre() + ", el campo contraseña no puede estar vacio"); return false;}
		 if( ! Roles.comprobarRoles(perfil.getRol()) ) {JOptionPane.showMessageDialog(frameReportar, "En el pefil con nombre:  " + perfil.getNombre() + ", el campo rol no esta entre los permitidos"); return false; }
		 
		 return true;
	}
	

	
 /* Carga una List de perfiles en una JLists */ 
	public static void cargarListaDePerfilesEnJList(List<Perfil> listaDePerfiles ,DefaultListModel<Perfil> modelo_jListDePerfiles ) {

		//List<Perfil> list_Perfiles = accesoBBDD.obtenerTodosLosPerfiles() ;
		//modelo_jListDePerfiles.clear();
				
		for(Perfil perfil : listaDePerfiles) {
			
			//System.out.println( "Perfil cargado:" + perfil + "--contraseña: " + perfil.getContraseña() + "--" );
			modelo_jListDePerfiles.addElement(perfil);
		}
	}////Fin cargarListaPerfiles() 
	
	
	 /* Carga una List de perfiles en una JLists */ 
		public static void eliminarListaDePerfilesEnJList(List<Perfil> listaDePerfiles ,DefaultListModel<Perfil> modelo_jListDePerfiles ) {

					
			for(Perfil perfil : listaDePerfiles) {
				
				//p.println(perfil);
				modelo_jListDePerfiles.removeElement(perfil); 
			}
		}////Fin cargarListaPerfiles() 
	
	 /* Crea una List de perfiles de una JLists */ 
		public static List<Perfil> crearListaDePerfilesDeJList(DefaultListModel<Perfil> modelo_jListDePerfiles ) {


			List<Perfil> listaPerfiles =  new ArrayList<Perfil>() ;
			for(Object perfil : modelo_jListDePerfiles.toArray() ) {
				
				//p.println(perfil);
				listaPerfiles.add((Perfil) perfil);
			}
			return listaPerfiles;
		}////Fin cargarListaPerfiles() 
	
	
		      
	
	
	
	
	
	
	
	
	
}//--------------------Fin de Clase--------------------------------------------------------------------------------------------------------------------------------


