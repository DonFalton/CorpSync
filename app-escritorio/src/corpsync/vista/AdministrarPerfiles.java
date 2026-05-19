package corpsync.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;

import corpsync.controlador.AccesoADatosBBDD;
import corpsync.controlador.Perfil;
import corpsync.controlador.Roles;
import corpsync.controlador.Utils;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JComboBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;
import javax.swing.JPasswordField;

/**
 * Clase AdministrarPerfiles : Permite crear nuevos perfiles, modificarlos y
 * borrarlos. Tiene una lista(JList) que muestra todos los perfiles de la base
 * de datos. Dos paneles con campos de texto y un combobox cada uno. Un panel
 * muestra los campos seleccionados en la lista para permitir modificarlos o
 * borrarlos y otro permite introducir loscampos para el nuevo perfil.
 **/

public class AdministrarPerfiles extends JFrame {


	private static final long serialVersionUID = 1L;

	private AccesoADatosBBDD accesoBBDD;

	private DefaultListModel<Perfil> modelo_jList_Perfiles;
	private Perfil perfilSeleccionadoJListBBDDCopia;

	// private String[] roles = new String[]{"empleado" , "tecnico" , "admin"};

	private JTextField textField_departamento_Sup;
	private JTextField textField_eMail_Sup;
	private JTextField textField_nombre_Sup;

	private JTextField textField_nombre_Inf;
	private JTextField textField_departamento_Inf;

	private JTextField textField_eMail_Inf;

	private JComboBox<String> comboBox_Rol_Inf;
	private JComboBox<String> comboBox_Rol_Sup;

	private JList<Perfil> jList_Perfiles;
	private JPasswordField passwordField_NuevaContraseña_Sup;
	private JPasswordField textField_ContraseñaCifrada_Sup;
	private JPasswordField textField_ContraseñaSinCifrar_Inf;

	/*
	 * Solo para pruevas public static void main(String[] args) {
	 * EventQueue.invokeLater(new Runnable() { public void run() { try {
	 * AdministrarPerfiles frame = new AdministrarPerfiles();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */

	// Constructor Solo para pruevas
	public AdministrarPerfiles() {


		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 945, 600);
		setMinimumSize(new Dimension(945, 600));
		accesoBBDD = new AccesoADatosBBDD(); // +++
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				accesoBBDD.cerrarConexion();
				dispose();
				System.exit(0);
			}
		});

		inicializarGUI();
	}

	/** Es el constructor usado para crear la clase en produccion. Crea una instancia de la clase AccesoADatosBBDD para usar sus métodos de persistencia en base de datos.
	 * crea el manejador de eventos que que trata el evento de cierre de ventana aciendo visible la ventana principal y cerrando esta.
	 *  Recibe como parametro una referancia a la ventana principal para poderla hacer visible de nuevo. Fija tambien el tamaño minimo de la ventana.
	 *  Por ultimo ejecuta el método inicializarGUI() que inizializa los componentes visuales**/
	public AdministrarPerfiles(VentanaPrincipal ventanaPrincipal) {

		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 945, 600);
		setMinimumSize(new Dimension(945, 600));

		accesoBBDD = new AccesoADatosBBDD(); // +++

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {


				ventanaPrincipal.setVisible(true);
				dispose();

			}
		});

		inicializarGUI();
	}

	/** Crea e inicializa todos los compodentes de la interfaz con sus valores visuales. Crea layouts y fija sus parametros. 
	 * Crea los manejadores de eventos necesarios y carga los valores de inicio de JList y ComboBox. **/
	private void inicializarGUI() {

		////////////////////////////////////////////////////////////
		JPanel contentPane;
		JPanel panel = new JPanel();
		panel.setBackground(new Color(192, 192, 192));
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(192, 192, 192));
		JPanel panel_Izquierdo = new JPanel();
		JPanel panel_Derecho = new JPanel();
		panel_Derecho.setBackground(new Color(192, 192, 192));
		JPanel panel_central_Bordes_1 = new JPanel();
		JPanel panel_box_sup = new JPanel();
		JPanel panel_box_inf = new JPanel();
		JPanel panel_2 = new JPanel();
		JPanel panel_4 = new JPanel();
		JPanel panel_5 = new JPanel();
		JPanel panel_6 = new JPanel();
		JPanel panel_7 = new JPanel();
		JPanel panel_8 = new JPanel();
		JPanel panel_9 = new JPanel();
		JPanel panel_10 = new JPanel();
		JPanel panel_11 = new JPanel();
		JPanel panel_12 = new JPanel();
		JPanel panel_13 = new JPanel();
		JPanel panel_14 = new JPanel();
		JLabel lblNewLabel = new JLabel("Nombre");
		JLabel lblNewLabel_1 = new JLabel("Rol");
		JLabel lblNewLabel_2 = new JLabel("Departamento");
		JLabel lblNewLabel_3 = new JLabel("Base de datos");
		JLabel lblNewLabel_8 = new JLabel("EMail");
		JLabel lblNewLabel_10 = new JLabel("Crear nuevo perfil en base de datos");
		JLabel lblNewLabel_4 = new JLabel("Nombre");
		JLabel lblNewLabel_9 = new JLabel("Modificar y borrar perfil en base de datos");
		JLabel lblNewLabel_5 = new JLabel("Rol");
		JLabel lblNewLabel_6 = new JLabel("Departamento");
		JLabel lblNewLabel_7 = new JLabel("EMail");
		JButton buttonGuardarPerfil = new JButton("Guardar Perfil");

		//////////////////////////////////////////////////////////////////

		// ++++++++++++++++++++ Componentes manipulados
		// +++++++++++++++++++++++++++++++++++
		textField_nombre_Inf = new JTextField();

		textField_departamento_Inf = new JTextField();

		textField_eMail_Inf = new JTextField();

		jList_Perfiles = new JList<Perfil>();

		// ++++++++++++++++++++ Fin Componentes manipulados
		// +++++++++++++++++++++++++++++++++++

		textField_eMail_Inf.setColumns(10);
		textField_departamento_Inf.setColumns(10);
		textField_nombre_Inf.setColumns(10);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		contentPane.add(panel, BorderLayout.NORTH);

		JLabel lblNewLabel_11 = new JLabel("Administrar perfiles en la base de datos");
		panel.add(lblNewLabel_11);

		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_Izquierdo.setBackground(new Color(241, 184, 99));

		contentPane.add(panel_Izquierdo, BorderLayout.WEST);

		JScrollPane scrollPane = new JScrollPane();

		JButton button_Refrescar_Lista = new JButton("Refrescar Lista");
		button_Refrescar_Lista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recargar_jList_PerfilesBBDD();// +++++++++++++++++++++++++++++++++++

			}
		});
		GroupLayout gl_panel_Izquierdo = new GroupLayout(panel_Izquierdo);
		gl_panel_Izquierdo.setHorizontalGroup(gl_panel_Izquierdo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Izquierdo.createSequentialGroup()
						.addGroup(gl_panel_Izquierdo.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_Izquierdo.createSequentialGroup().addContainerGap()
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
								.addGroup(gl_panel_Izquierdo.createSequentialGroup().addGap(67)
										.addComponent(button_Refrescar_Lista))
								.addGroup(gl_panel_Izquierdo.createSequentialGroup().addContainerGap().addComponent(
										lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		gl_panel_Izquierdo.setVerticalGroup(gl_panel_Izquierdo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Izquierdo.createSequentialGroup().addComponent(lblNewLabel_3).addGap(16)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE).addGap(8)
						.addComponent(button_Refrescar_Lista)));
		scrollPane.setViewportView(jList_Perfiles);
		jList_Perfiles.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				manejador_jList_Perfiles_Seleccion(); // +++++++++++++++++++++++++++++++++
			}
		});

		modelo_jList_Perfiles = new DefaultListModel<Perfil>();
		jList_Perfiles.setModel(modelo_jList_Perfiles);// ++++++++
		panel_Izquierdo.setLayout(gl_panel_Izquierdo);

		contentPane.add(panel_Derecho, BorderLayout.EAST);
		panel_Derecho.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		contentPane.add(panel_central_Bordes_1, BorderLayout.CENTER);
		panel_central_Bordes_1.setLayout(new BoxLayout(panel_central_Bordes_1, BoxLayout.Y_AXIS));
		panel_box_sup.setBackground(new Color(64, 128, 128));

		panel_central_Bordes_1.add(panel_box_sup);
		panel_box_sup.setLayout(new BoxLayout(panel_box_sup, BoxLayout.Y_AXIS));
		panel_2.setBackground(new Color(206, 134, 0));

		panel_box_sup.add(panel_2);

		panel_2.add(lblNewLabel_9);
		panel_4.setBackground(new Color(255, 128, 0));

		panel_box_sup.add(panel_4);

		textField_nombre_Sup = new JTextField();
		textField_nombre_Sup.setColumns(10);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
						.addComponent(lblNewLabel_4, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(textField_nombre_Sup, GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
						.addGap(39)));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup().addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup().addGap(11).addComponent(textField_nombre_Sup,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblNewLabel_4)).addContainerGap(23, Short.MAX_VALUE)));
		panel_4.setLayout(gl_panel_4);
		panel_7.setBackground(new Color(255, 128, 0));

		panel_box_sup.add(panel_7);

		comboBox_Rol_Sup = new JComboBox<String>();
		Roles.inicializarComboBoxDeRoles(comboBox_Rol_Sup);

		JLabel label_Contrasea1 = new JLabel("Contrase\u00F1a");

		label_Contrasea1.setVisible(true);

		textField_ContraseñaCifrada_Sup = new JPasswordField();
		textField_ContraseñaCifrada_Sup.setEditable(false);
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup()
						.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addGap(27).addComponent(comboBox_Rol_Sup, 0, 240, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_Contrasea1).addGap(32)
						.addComponent(textField_ContraseñaCifrada_Sup, GroupLayout.PREFERRED_SIZE, 214,
								GroupLayout.PREFERRED_SIZE)
						.addGap(43)));
		gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_7
				.createSequentialGroup()
				.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_5)
						.addGroup(gl_panel_7.createSequentialGroup().addContainerGap()
								.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE)
										.addComponent(comboBox_Rol_Sup, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(textField_ContraseñaCifrada_Sup, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(label_Contrasea1))
				.addContainerGap(18, Short.MAX_VALUE)));
		panel_7.setLayout(gl_panel_7);
		panel_8.setBackground(new Color(255, 128, 0));

		panel_box_sup.add(panel_8);

		textField_departamento_Sup = new JTextField();
		textField_departamento_Sup.setColumns(10);
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(gl_panel_8.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_8
				.createSequentialGroup().addComponent(lblNewLabel_6).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(textField_departamento_Sup, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE).addGap(40)));
		gl_panel_8.setVerticalGroup(gl_panel_8.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_8
				.createSequentialGroup()
				.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_6)
						.addGroup(gl_panel_8.createSequentialGroup().addGap(11).addComponent(textField_departamento_Sup,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(18, Short.MAX_VALUE)));
		panel_8.setLayout(gl_panel_8);
		panel_9.setBackground(new Color(255, 128, 0));

		panel_box_sup.add(panel_9);

		textField_eMail_Sup = new JTextField();
		textField_eMail_Sup.setColumns(10);
		GroupLayout gl_panel_9 = new GroupLayout(panel_9);
		gl_panel_9.setHorizontalGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
						.addComponent(lblNewLabel_7, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(textField_eMail_Sup, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE).addGap(40)));
		gl_panel_9.setVerticalGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup().addGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_9.createSequentialGroup().addGap(11).addComponent(textField_eMail_Sup,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblNewLabel_7)).addContainerGap(23, Short.MAX_VALUE)));
		panel_9.setLayout(gl_panel_9);

		panel_13.setBackground(new Color(206, 134, 0));
		panel_box_sup.add(panel_13);

		JButton boton_BorrarPerfil = new JButton("Borrar Perfil");
		boton_BorrarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonBorrarPerfil();
			}
		});

		JButton boton_ActualizarPerfil = new JButton("Actualizar Perfil");
		boton_ActualizarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_Boton_Actualizar_Perfil();
			}
		});

		JLabel lblNewLabel_12 = new JLabel(" Nueva contrase\u00F1a :");

		JButton boton_CambiarContraseña = new JButton("Cambiar Contrase\u00F1a");
		boton_CambiarContraseña.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonCambiarContraseña();
			}
		});

		passwordField_NuevaContraseña_Sup = new JPasswordField();
		GroupLayout gl_panel_13 = new GroupLayout(panel_13);
		gl_panel_13.setHorizontalGroup(gl_panel_13.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_13.createSequentialGroup().addGap(2).addComponent(boton_BorrarPerfil)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(boton_ActualizarPerfil)
						.addPreferredGap(ComponentPlacement.RELATED, 116, Short.MAX_VALUE).addComponent(lblNewLabel_12)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(passwordField_NuevaContraseña_Sup, GroupLayout.PREFERRED_SIZE, 96,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(boton_CambiarContraseña).addGap(6)));
		gl_panel_13.setVerticalGroup(gl_panel_13.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_13
				.createSequentialGroup().addGap(5)
				.addGroup(gl_panel_13.createParallelGroup(Alignment.BASELINE).addComponent(boton_BorrarPerfil)
						.addComponent(boton_ActualizarPerfil).addComponent(boton_CambiarContraseña)
						.addComponent(lblNewLabel_12).addComponent(passwordField_NuevaContraseña_Sup,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))));
		panel_13.setLayout(gl_panel_13);
		panel_box_inf.setBackground(new Color(192, 192, 192));

		panel_central_Bordes_1.add(panel_box_inf);
		panel_box_inf.setLayout(new BoxLayout(panel_box_inf, BoxLayout.Y_AXIS));
		panel_5.setBackground(new Color(198, 159, 4));

		panel_box_inf.add(panel_5);

		panel_5.add(lblNewLabel_10);
		panel_6.setBackground(new Color(253, 197, 66));

		panel_box_inf.add(panel_6);
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup().addComponent(lblNewLabel).addGap(33)
						.addComponent(textField_nombre_Inf, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
						.addGap(38)));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_6
				.createSequentialGroup()
				.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel)
						.addGroup(gl_panel_6.createSequentialGroup().addGap(11).addComponent(textField_nombre_Inf,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_6.setLayout(gl_panel_6);
		panel_10.setBackground(new Color(253, 197, 66));

		panel_box_inf.add(panel_10);

		comboBox_Rol_Inf = new JComboBox<String>();
		Roles.inicializarComboBoxDeRoles(comboBox_Rol_Inf); /// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		JLabel label_Contrasea2 = new JLabel("Contrase\u00F1a");

		textField_ContraseñaSinCifrar_Inf = new JPasswordField();

		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10.setHorizontalGroup(gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_10.createSequentialGroup()
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addGap(43).addComponent(comboBox_Rol_Inf, 0, 240, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_Contrasea2)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(textField_ContraseñaSinCifrar_Inf, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
						.addGap(38)));
		gl_panel_10.setVerticalGroup(gl_panel_10.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_10
				.createSequentialGroup()
				.addGroup(gl_panel_10.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_1)
						.addGroup(gl_panel_10.createSequentialGroup().addContainerGap()
								.addGroup(gl_panel_10.createParallelGroup(Alignment.BASELINE)
										.addComponent(comboBox_Rol_Inf, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(textField_ContraseñaSinCifrar_Inf, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(label_Contrasea2))
				.addContainerGap(18, Short.MAX_VALUE)));
		panel_10.setLayout(gl_panel_10);
		panel_11.setBackground(new Color(253, 197, 66));

		panel_box_inf.add(panel_11);
		// comboBox.addItem(nombre);

		GroupLayout gl_panel_11 = new GroupLayout(panel_11);
		gl_panel_11.setHorizontalGroup(gl_panel_11.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_11
				.createSequentialGroup().addComponent(lblNewLabel_2).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(textField_departamento_Inf, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE).addGap(37)));
		gl_panel_11.setVerticalGroup(gl_panel_11.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_11.createSequentialGroup()
						.addGroup(gl_panel_11.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_2)
								.addGroup(gl_panel_11.createSequentialGroup().addGap(11).addComponent(
										textField_departamento_Inf, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_11.setLayout(gl_panel_11);
		panel_14.setBackground(new Color(253, 197, 66));

		panel_box_inf.add(panel_14);
		GroupLayout gl_panel_14 = new GroupLayout(panel_14);
		gl_panel_14.setHorizontalGroup(gl_panel_14.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_14.createSequentialGroup().addComponent(lblNewLabel_8).addGap(51)
						.addComponent(textField_eMail_Inf, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE).addGap(36)));
		gl_panel_14.setVerticalGroup(gl_panel_14.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_14
				.createSequentialGroup()
				.addGroup(gl_panel_14.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_8)
						.addGroup(gl_panel_14.createSequentialGroup().addGap(10).addComponent(textField_eMail_Inf,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap()));
		panel_14.setLayout(gl_panel_14);
		panel_12.setBackground(new Color(198, 159, 4));
		panel_box_inf.add(panel_12);

		buttonGuardarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				manejador_GuardarPerfil();// +++++++++++++++++
				// cargarListaPerfiles();

			}
		});

		panel_12.add(buttonGuardarPerfil);

		recargar_jList_PerfilesBBDD();// ++++++++++++++++++++++++
	}/// FIN
		/// inicializarGUI()//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Cambia y cifra la contraseña del perfil seleccionado. Comprueba si el camco esta vacio o solo tiene espacios y reporta esta situaccion con un JPanel.
	 * Reporta tambien si el cambio fue exitoso. **/
	private void manejador_BotonCambiarContraseña() {

		if (perfilSeleccionadoJListBBDDCopia == null) {
			JOptionPane.showMessageDialog(this, "Seleccione un perfil de usuario.");
			return;
		} // Si no puede dar error cuando se ha recargado la lista.
		UUID id = perfilSeleccionadoJListBBDDCopia.getId();
		// Comprobacion de contraseña y lipiado espacios de campos
		@SuppressWarnings("deprecation")
		String contraseñaSinCifrar = passwordField_NuevaContraseña_Sup.getText().strip();
		if (contraseñaSinCifrar.equals("")) {
			JOptionPane.showMessageDialog(this,
					"Para el pefil con nombre:  " + perfilSeleccionadoJListBBDDCopia.getNombre()
							+ ", el campo 'Nueva contraseña' no puede estar vacio.");
			return;
		}
		String contraseñaCifrada = accesoBBDD.cifrarContraseña(contraseñaSinCifrar, this);
		boolean exitoGuardar = accesoBBDD.guardarContraseña(id, contraseñaCifrada, this);

		if (exitoGuardar) {
			passwordField_NuevaContraseña_Sup.setText("");
			recargar_jList_PerfilesBBDD();
			System.out.println("--Cambiada contraseña de perfil: " + perfilSeleccionadoJListBBDDCopia);
		}
	}// Fin manejador_BotonCambiarContraseña()

	/** Rellena los campos superiores con los valores del perfil selecionado. **/
	private void manejador_jList_Perfiles_Seleccion() {
		Perfil perfilSeleccionado = (Perfil) jList_Perfiles.getSelectedValue();
		if (perfilSeleccionado == null) {
			return;
		} // Si no puede dar error
		perfilSeleccionadoJListBBDDCopia = perfilSeleccionado.crearCopia();

		textField_nombre_Sup.setText(perfilSeleccionadoJListBBDDCopia.getNombre());
		comboBox_Rol_Sup.setSelectedItem(perfilSeleccionadoJListBBDDCopia.getRol());
		textField_departamento_Sup.setText(perfilSeleccionadoJListBBDDCopia.getDepartamento());
		textField_eMail_Sup.setText(perfilSeleccionadoJListBBDDCopia.getEmail());
		textField_ContraseñaCifrada_Sup.setText(perfilSeleccionadoJListBBDDCopia.getContraseñaCifrada());
	}

	/**
	 * manejador_Boton_Actualizar_Perfil(): Actualiza en la BBDD los datos del
	 * perfil seleccionado en la JList cargada de la BBDD. Todos excepto la contraseña.
	 **/
	private void manejador_Boton_Actualizar_Perfil() {

		if (perfilSeleccionadoJListBBDDCopia == null) {
			return;
		} // Si no puede dar error
		System.out.println("----" + perfilSeleccionadoJListBBDDCopia);

		perfilSeleccionadoJListBBDDCopia.setNombre(textField_nombre_Sup.getText());
		perfilSeleccionadoJListBBDDCopia.setRol((String) comboBox_Rol_Sup.getSelectedItem());
		perfilSeleccionadoJListBBDDCopia.setDepartamento(textField_departamento_Sup.getText());
		perfilSeleccionadoJListBBDDCopia.setEmail(textField_eMail_Sup.getText());

		boolean perfilCorrecto = Utils.limtiarYComprobarPerfilSinContraseña(perfilSeleccionadoJListBBDDCopia, this);
		if (!perfilCorrecto) {
			return;
		}
		boolean exitoActualizar = accesoBBDD.actualizarPerfil(perfilSeleccionadoJListBBDDCopia, this);
		if (exitoActualizar) {
			recargar_jList_PerfilesBBDD();
			limpiarCampos_Superiores();
		}
	} // Fin manejador_Boton_Actualizar_Perfil()

	/**
	 * borrarPerfil(): Borra , de la base de datos, el perfil seleccionado en la JList cargada de desde la
	 * BBDD segun el valor del atributo id. Reporta el fallo si no se consigue borrar por alguna excepcion SQL.
	 */

	private void manejador_BotonBorrarPerfil() {

		if (perfilSeleccionadoJListBBDDCopia == null) {
			return;
		} // Si no puede dar error cuando se ha recargado la lista.

		UUID id = perfilSeleccionadoJListBBDDCopia.getId();

		boolean exitoBorrar = accesoBBDD.borrarPerfil(id, this);
		if (exitoBorrar) {
			System.out.println("--Borrado el usuario con ID: " + id);
		} else {
			System.out.println("--No se pudo borrar el  usuario con ID: " + id);
		}

		recargar_jList_PerfilesBBDD();
		limpiarCampos_Superiores();
	}

/** Guarda el perfil seleccionado en la base de datos. Comprueba si hay campos vacios o solo con espacios y lo notifica con un JPanel.
 * Tambien reporta excepciones SQL si no se pudo guardar. Recarga la lista de perfiles de la base de datos y limpia los campos superiores. **/
 void manejador_GuardarPerfil() {

		String nombre = textField_nombre_Inf.getText();
		String rol = (String) comboBox_Rol_Inf.getSelectedItem();
		String departamento = textField_departamento_Inf.getText();
		String email = textField_eMail_Inf.getText();
		@SuppressWarnings("deprecation")
		String contraseña = textField_ContraseñaSinCifrar_Inf.getText();

		System.out.println("--textField_ContraseñaSinCifrar_InftextField_ContraseñaSinCifrar_Inf::::::: " + contraseña);
		Perfil nuevoPerfil = new Perfil(nombre, rol, departamento, email);
		nuevoPerfil.setContraseñaSinCifrar(contraseña);
		boolean perfilCorrecto = Utils.limtiarYComprobarPerfilConContraseñaSinCifrar(nuevoPerfil, this);
		if (!perfilCorrecto) {
			return;
		}
		boolean exitoGuardar = accesoBBDD.guardarPerfilConContraseñaSinCifrar(nuevoPerfil, this);
		if (exitoGuardar) {
			recargar_jList_PerfilesBBDD();
			limpiarCampos_Inferiores();
			System.out.println("--Guardado el perfil: " + nuevoPerfil);
		}

	}// Fin manejador_GuardarPerfil()

	/**
	 * recargar_jList_PerfilesBBDD(): Refresca la lista de perfiles. Carga todos los
	 * registros de la tabla "perfiles" (devueltos por el metodo
	 * accesoBBDD.obtenerTodosLosPerfiles()) en la JList jList_Perfiles despues de limpiarla.
	 * Tambien limpia los campos del panel superior.
	 */
	private void recargar_jList_PerfilesBBDD() {

		List<Perfil> list_Perfiles = accesoBBDD.obtenerTodosLosPerfiles();
		modelo_jList_Perfiles.clear();
		limpiarCampos_Superiores();
		Utils.cargarListaDePerfilesEnJList(list_Perfiles, modelo_jList_Perfiles);

	}//// Fin cargarListaPerfiles()

	/** Limpia los campos de texto del panel inferior */
	private void limpiarCampos_Inferiores() {

		textField_nombre_Inf.setText("");
		comboBox_Rol_Inf.setSelectedItem(Roles.roles[0]);
		textField_departamento_Inf.setText("");
		textField_eMail_Inf.setText("");
		textField_ContraseñaSinCifrar_Inf.setText("");

	}/// Fin limpiarCampos_Inferiores

	/** Limpia los campos de texto del panel superior */
	private void limpiarCampos_Superiores() {

		textField_nombre_Sup.setText("");
		comboBox_Rol_Sup.setSelectedItem(Roles.roles[0]);
		textField_departamento_Sup.setText("");
		textField_eMail_Sup.setText("");

	}/// Fin limpiarCampos_Superiores ()
}////// Fin de clase----------------------
