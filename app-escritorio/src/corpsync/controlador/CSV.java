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

public class CSV {

	public final static Character[] separadores = new Character[] { ',', ';' };

	// private final static String cabezeraCSV =
	// "nombre,rol,departamento,email\n".toUpperCase() ;

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

	public static void inicializarComboBoxDeSeparadores(JComboBox<Character> comboBoxSeparadores) {

		for (Character separador : separadores) {

			comboBoxSeparadores.addItem(separador);
		}
	}/// Fin inicializarRoles_ComboBox

}
