package corpsync.controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


/** Clase CSVs: Clase creada para manejar CSVs y todo lo relacionado con ellos. Usa la librería OpenCSV 
 * para extraer y guardar los registros de perfiles a y desde los archivos CSV. Concretamente usa 
 * las clases CsvToBeanBuilder y StatefulBeanToCsvBuilder para este proceso. Tiene un atributo llamado separadores 
 * con un array de objetos caracter que contiene los posibles caracteres separadores que la aplicación permite usar para crear y cargar archivos CSV.
 *  En caso de querer ampliar el numero de caracteres separador solo haria falta modificar este atributo de clase.  **/


public class CSV {

	public final static Character[] separadores = new Character[] { ',', ';' };

/** 1. List<Perfil> extraerCSV(File foloFile, char separador, JFrame frameReportar) : Extrae los registros de un csv y los  devuelve en una lista de perfiles.
 *  Reporta cualquier error capturado en el proceso en el JFrame pasado como parametro mediante un mensaje de diálogo de un JOptionPane. **/
	public static List<Perfil> extraerCSV(File foloFile, char separador, JFrame frameReportar) { // FileReader
																									// readerDelCSV

		FileReader readerDelCSV;
		List<Perfil> perfiles = null;
		try {
			readerDelCSV = new FileReader(foloFile);

			CsvToBean<Perfil> csvToBean = new CsvToBeanBuilder<Perfil>(readerDelCSV).withType(Perfil.class)
					.withIgnoreLeadingWhiteSpace(true).withSeparator(separador).build();

			perfiles = csvToBean.parse();

			for (Perfil perfil : perfiles) {
				System.out.println("--perfil extraido->" + " -Nombre =" + perfil.getNombre() + " -Departamento ="
						+ perfil.getDepartamento() + " -Rol =" + perfil.getRol() + " -Email =" + perfil.getEmail());
			}
			readerDelCSV.close();

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frameReportar, "Error: Archivo CSV no encontrado.");
			e.printStackTrace();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(frameReportar, "No se pudo cerrar el archivo.");
			e.printStackTrace();
		}

		return perfiles;

	}//// Fin extraerCSV

	/** void crearCSV(List<Perfil> listaPerfiles, File fileDelCSV, char separador, JFrame frameReportar) : Crea el CSV a partir de la lista pasada como parámetro.
	 *  Reporta cualquier error capturado en el proceso en el JFrame pasado como parametro mediante un mensaje de diálogo de un JOptionPane. **/
	public static void crearCSV(List<Perfil> listaPerfiles, File fileDelCSV, char separador, JFrame frameReportar) {

		try(FileWriter writerDelCSV = new FileWriter(fileDelCSV);) {

			
			// writerDelCSV.write(cabezeraCSV);

			StatefulBeanToCsv<Perfil> beanToCsv = new StatefulBeanToCsvBuilder<Perfil>(writerDelCSV)
					.withApplyQuotesToAll(false).withSeparator(separador).build();

			beanToCsv.write(listaPerfiles);
			//writerDelCSV.close();

		} catch (CsvDataTypeMismatchException e) {
			JOptionPane.showMessageDialog(frameReportar,
					"Error: El valor de un campo no es del formato esperado segun su tipo en CSV.");
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			JOptionPane.showMessageDialog(frameReportar, "Error: Campo requerido vacio en CSV.");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frameReportar, "Error al guardar el archivo CSV.");
			e.printStackTrace();
		}

		System.out.println("CSV generado");

	}//// Fin crearCSV
	
	
/**  void inicializarComboBoxDeSeparadores(JComboBox<Character> comboBoxSeparadores) : Carga los combobox usados para  seleccionar 
 * el carácter separador en la creación o carga de CSV. Usa el citado array para hacerlo.  **/
	public static void inicializarComboBoxDeSeparadores(JComboBox<Character> comboBoxSeparadores) {

		for (Character separador : separadores) {

			comboBoxSeparadores.addItem(separador);
		}
	}/// Fin inicializarRoles_ComboBox

}
