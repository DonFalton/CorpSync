package corpsync.controlador;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class Provar {

	public static void main(String[] args) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		// TODO Auto-generated method stub
		
		
		
		p1();

	       

	}
	
	
	public static  void p2() {
		
		System.out.println(Roles.comprobarRoles( "empleado" ) );
		

		
	}
	
	
	
	public static  void p1() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
		  List<Perfil> listaPerfiles = Arrays.asList(
	                new Perfil("nombre1",  "rol1", "departamento1", "email1", "contraseña1"),
	                new Perfil("nombre2",  "rol2", "departamento2", "email2", "contraseña2"),
	                new Perfil("nombre3",  "rol3", "departamento3", "email3", "contraseña3"),
	                new Perfil("nombre4",  "rol4", "departamento4", "email4", "contraseña4")
	                
	        );
		   
		   //File file = new File("C:\\300 pruevas\\perfiles.csv");
		   File file = new File("C:\\Users\\ger\\Documents\\300 pruevas\\perfiles.csv");
		   
		   JFrame fr = new JFrame();
		   

	       // FileWriter writerDelCSV = new FileWriter(file);  Utilidades.crearCSV( listaPerfiles , writerDelCSV , fr  );
	        

	        
	        //FileReader readerDelCSV = new FileReader(file);   Utilidades.extraerCSV( file , fr    );
		
		
	}
	
	

}
