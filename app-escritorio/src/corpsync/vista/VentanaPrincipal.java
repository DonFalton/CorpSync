package corpsync.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import corpsync.controlador.ConexionBBDD;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

/**
 * Simplemente da acceso a las otras ventanas, que son las que tienen la
 * funcionalidad.
 **/
public class VentanaPrincipal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JButton boton_AdministrarPerfilesEnBBDD;
	public JButton boton_GuardarCSVaBBDD;
	public JButton boton_CrearCSV;
	public JButton boton_CrearCSVDesdeBBDD;
	public JButton boton_Autenticacion;

	/*
	 * Solo para pruevas public static void main(String[] args) {
	 * EventQueue.invokeLater(new Runnable() { public void run() { try {
	 * VentanaPrincipal frame = new VentanaPrincipal(); frame.setVisible(true); }
	 * catch (Exception e) { e.printStackTrace(); } } }); }
	 */

	/** Es el constructor usado para crear la clase en produccion.
	 * Crea el manejador de eventos que que trata el evento de cierre de ventana aciendo visible la ventana principal y cerrando esta. ademas, cierra
	 *  la conexión a la base de datos y la aplicación en su conjunto. Fija tambien el tamaño minimo de la ventana.
	 *  Por ultimo ejecuta el método inicializarGUI() que inizializa los componentes visuales**/
	 
	public VentanaPrincipal() {
		setResizable(false);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 436, 395);
		setMinimumSize(new Dimension( 436, 395));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				ConexionBBDD.cerrarConexion();
				dispose();
				System.exit(0);
			}
		});
		inicializarGUI();

	}
	
	/** Crea e inicializa todos los compodentes de la interfaz con sus valores visuales. Crea layouts y fija sus parametros. 
	 * Crea los manejadores de eventos necesarios. **/

	private void inicializarGUI() {

		JPanel contentPane;
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel("Menu principal");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 19));

		boton_AdministrarPerfilesEnBBDD = new JButton("Administrar perfiles en BBDD");
		boton_AdministrarPerfilesEnBBDD.setEnabled(false);
		boton_AdministrarPerfilesEnBBDD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				abrirVentanaAdministrarPerfiles();
			}
		});
		boton_AdministrarPerfilesEnBBDD.setFont(new Font("Tahoma", Font.PLAIN, 15));

		boton_GuardarCSVaBBDD = new JButton("Cuardar perfiles de CSV a la BBDD");
		boton_GuardarCSVaBBDD.setEnabled(false);
		boton_GuardarCSVaBBDD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirVentanaCargarCSVaBBDD();
			}
		});
		boton_GuardarCSVaBBDD.setFont(new Font("Tahoma", Font.PLAIN, 15));

		boton_CrearCSV = new JButton("Crear CSV");
		boton_CrearCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirVentanaCrearCSV();
			}
		});
		boton_CrearCSV.setFont(new Font("Tahoma", Font.PLAIN, 15));

		boton_CrearCSVDesdeBBDD = new JButton("Crear CSV desde BBDD");
		boton_CrearCSVDesdeBBDD.setEnabled(false);
		boton_CrearCSVDesdeBBDD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirVentanaCrearCSVDedeBBDD();
			}
		});
		boton_CrearCSVDesdeBBDD.setFont(new Font("Tahoma", Font.PLAIN, 15));

		boton_Autenticacion = new JButton("Autenticacion");
		boton_Autenticacion.setBackground(new Color(255, 128, 128));
		boton_Autenticacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirVentanaAutenticacionEnSupabase();
			}
		});
		boton_Autenticacion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(121)
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE).addGap(113))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(79).addGroup(gl_contentPane
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(boton_Autenticacion, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 265,
								Short.MAX_VALUE)
						.addComponent(boton_CrearCSVDesdeBBDD, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
						.addComponent(boton_CrearCSV, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
						.addComponent(boton_GuardarCSVaBBDD, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(boton_AdministrarPerfilesEnBBDD, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 265,
								Short.MAX_VALUE))
						.addGap(85)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addComponent(lblNewLabel).addGap(66)
						.addComponent(boton_AdministrarPerfilesEnBBDD).addGap(18).addComponent(boton_GuardarCSVaBBDD)
						.addGap(18).addComponent(boton_CrearCSV).addGap(18).addComponent(boton_CrearCSVDesdeBBDD)
						.addGap(18).addComponent(boton_Autenticacion).addContainerGap(43, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);

	}

	private void abrirVentanaAdministrarPerfiles() {

		// if(!(ventanaAbierta == null)) {return;}
		JFrame ventanaAbierta = new AdministrarPerfiles(this);
		ventanaAbierta.setVisible(true);
		setVisible(false);

	}

	private void abrirVentanaCargarCSVaBBDD() {

		JFrame ventanaAbierta = new GuardarCSVEnBBDD(this);
		ventanaAbierta.setVisible(true);
		setVisible(false);

	}

	private void abrirVentanaCrearCSV() {

		JFrame ventanaAbierta = new CrearCSV(this);
		ventanaAbierta.setVisible(true);
		setVisible(false);

	}

	private void abrirVentanaCrearCSVDedeBBDD() {

		JFrame ventanaAbierta = new CrearCSVDedeBBDD(this);
		ventanaAbierta.setVisible(true);
		setVisible(false);

	}

	private void abrirVentanaAutenticacionEnSupabase() {

		JFrame ventanaAbierta = new Autenticacion(this);
		ventanaAbierta.setVisible(true);
		setVisible(false);

	}

}
