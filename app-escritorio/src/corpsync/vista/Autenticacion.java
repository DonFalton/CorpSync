package corpsync.vista;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import corpsync.controlador.ConexionBBDD;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * Clase Autenticación: Esta clase autentica al usuario de la aplicación según
 * su identificador y contraseña. Si son correctos, habilita los botones de la
 * clase VentanaPrincipal que dan acceso a la base de datos y cambia el color
 * del botón de autenticación para reflejar que la autenticación ha sido
 * correcta. Si es así, además crea la conexión necesaria a la base de datos.
 **/
public class Autenticacion extends JFrame {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	// private AccesoADatosBBDD accesoBBDD = new AccesoADatosBBDD();
	// /////////////////////////////////////////////
	private JTextField textField_Usuariorio;

	private VentanaPrincipal ventanaPrin;
	private JPasswordField passwordField;

	/*
	 * Solo para pruevas public static void main(String[] args) {
	 * EventQueue.invokeLater(new Runnable() { public void run() { try {
	 * Autenticacion frame = new Autenticacion(); frame.setVisible(true); } catch
	 * (Exception e) { e.printStackTrace(); } } }); }
	 */

	// Constructor Solo para pruevas
	public Autenticacion() {
		setResizable(false);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
	

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				dispose();
				System.exit(0);
			}
		});

		inicializarGUI();
	}
	
	/** Es el constructor usado para crear la clase en produccion.
	 * crea el manejador de eventos que que trata el evento de cierre de ventana aciendo visible la ventana principal y cerrando esta.
	 *  Recibe como parametro una referancia a la ventana principal para poderla hacer visible de nuevo. Fija tambien el tamaño minimo de la ventana.
	 *  Por ultimo ejecuta el método inicializarGUI() que inizializa los componentes visuales**/

	public Autenticacion(VentanaPrincipal ventanaPrincipal) {

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setMinimumSize(new Dimension(450, 300));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				ventanaPrincipal.setVisible(true);
				dispose();
			}
		});
		ventanaPrin = ventanaPrincipal;
		inicializarGUI();
	}

	/** Crea e inicializa todos los compodentes de la interfaz con sus valores visuales. Crea layouts y fija sus parametros. 
	 * Crea los manejadores de eventos necesarios . **/
	private void inicializarGUI() {

		JPanel contentPane;
		contentPane = new JPanel();
		contentPane.setBackground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel("Autenticaci\u00F3n para conexi\u00F3n");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

		textField_Usuariorio = new JTextField();
		textField_Usuariorio.setColumns(10);

		JButton boton_Autenticar = new JButton("Autenticar en Supabase");
		boton_Autenticar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				controlador_botonConectarABBDD();
			}
		});

		passwordField = new JPasswordField();

		JLabel lblNewLabel_1 = new JLabel("Contrase\u00F1a");

		JLabel lblNewLabel_2 = new JLabel("Usuario");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(146)
						.addComponent(boton_Autenticar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGap(131))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(84)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPane
										.createSequentialGroup()
										.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 93,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_contentPane
												.createSequentialGroup()
												.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 70,
														GroupLayout.PREFERRED_SIZE)
												.addContainerGap())
										.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
												.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
														.addComponent(textField_Usuariorio, Alignment.LEADING,
																GroupLayout.PREFERRED_SIZE, 284,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(passwordField, Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
														.addComponent(lblNewLabel, Alignment.LEADING,
																GroupLayout.PREFERRED_SIZE, 284,
																GroupLayout.PREFERRED_SIZE))
												.addGap(56))))));
		gl_contentPane
				.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addComponent(lblNewLabel)
								.addGap(28).addComponent(lblNewLabel_2).addGap(18)
								.addComponent(textField_Usuariorio, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(27).addComponent(lblNewLabel_1).addGap(18)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
								.addComponent(boton_Autenticar).addContainerGap()));
		contentPane.setLayout(gl_contentPane);

	}// Fin inicializarGUI()

	private void controlador_botonConectarABBDD() {

		String usuario = textField_Usuariorio.getText();
		@SuppressWarnings("deprecation")
		String pwd = passwordField.getText();

		Connection conexion = ConexionBBDD.conectar(usuario, pwd);
		if (!(conexion == null)) {
			habilitarBotonesDeVentanas();
			JOptionPane.showMessageDialog( this ,"Autenticacion correcta");
		}

	}

	private void habilitarBotonesDeVentanas() {

		ventanaPrin.boton_AdministrarPerfilesEnBBDD.setEnabled(true);
		ventanaPrin.boton_GuardarCSVaBBDD.setEnabled(true);
		ventanaPrin.boton_CrearCSVDesdeBBDD.setEnabled(true);
		ventanaPrin.boton_Autenticacion.setBackground(new Color(0, 128, 128));

	}

}// FIN de clase
