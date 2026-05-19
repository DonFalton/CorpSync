package corpsync.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import corpsync.controlador.AccesoADatosBBDD;
import corpsync.controlador.CSV;
import corpsync.controlador.Perfil;
import corpsync.controlador.Roles;
import corpsync.controlador.Utils;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JPasswordField;

/**
 * Clase GuardarCSVEnBBDD : Guarda los registos elegidos de un archivo CSV en la
 * base de datos. Tiene dos listas(JList) que muestrantodos los perfiles de la
 * base de datos una y la otra los perfiles del archivo CSV desde el cual se
 * extraerán los perfiles para guardar en la base de datos. Tiene dos paneles
 * con campos de texto y un combobox cada uno. Un panel muestra los campos
 * seleccionados en la lista de la base de datos para permitir modificarlos o
 * borrarlos, por si se cuela algún registro no deseado o por si se quisiera
 * cambiar algo tras introducirlos, otro muestra los campos del perfil
 * seleccionado en la lista del CSV cargado y también permite modificarlos o
 * borrarlos antes de pasarlos a la base de datos.
 **/

public class GuardarCSVEnBBDD extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AccesoADatosBBDD accesoBBDD;

	private Perfil perfilSeleccionadoCSV;

	private Perfil perfilSeleccionadoJListBBDDCopia;

	// Componentes manipulados :

	private DefaultListModel<Perfil> modelo_jList_PerfilesBBDD;
	private DefaultListModel<Perfil> modelo_jList_PerfilesCSV;
	private JList<Perfil> jList_PerfilesBBDD;
	private JList<Perfil> jList_PerfilesCSV;
	private JTextField textField_departamento_Sup;
	private JTextField textField_eMail_Sup;
	private JTextField textField_nombre_Sup;

	private JTextField textField_nombre_Inf;
	private JTextField textField_departamento_Inf;

	private JTextField textField_eMail_Inf;

	private JComboBox<String> comboBox_Rol_Inf;
	private JComboBox<String> comboBox_Rol_Sup;
	private JComboBox<Character> comboBox;
	private JPasswordField textField_ContraseñaCifrada_Inf;
	private JPasswordField textField_ContraseñaCifrada_Sup;
	private JPasswordField passwordField_NuevaContraseña;

	/*
	 * Solo para pruevas public static void main(String[] args) {
	 * EventQueue.invokeLater(new Runnable() { public void run() { try {
	 * GuardarCSVEnBBDD frame = new GuardarCSVEnBBDD(); frame.setVisible(true); }
	 * catch (Exception e) { e.printStackTrace(); } } }); }
	 */

	// Constructor Solo para pruevas
	public GuardarCSVEnBBDD() {

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 985, 650);
		setMinimumSize(new Dimension(1020, 650));
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

	public GuardarCSVEnBBDD(VentanaPrincipal ventanaPrincipal) {

		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(1020, 650));

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
		panel_Derecho.setBackground(new Color(0, 128, 192));
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
		JLabel lblNewLabel_BBDD = new JLabel("BBDD");
		lblNewLabel_BBDD.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JLabel lblNewLabel_8 = new JLabel("EMail");
		JLabel lblNewLabel_10 = new JLabel("Modificar y eliminar perfil de la lista de CSV    >>>");
		JLabel lblNewLabel_4 = new JLabel(" Nombre");
		JLabel lblNewLabel_9 = new JLabel("<<<   Modificar y borrar perfil en base de datos");
		JLabel lblNewLabel_5 = new JLabel(" Rol");
		JLabel lblNewLabel_6 = new JLabel(" Departamento");
		JLabel lblNewLabel_7 = new JLabel(" EMail");
		JButton botonModificarPerfilCSV = new JButton("Modificar Perfil");

		//////////////////////////////////////////////////////////////////

		// ++++++++++++++++++++ Componentes manipulados
		// +++++++++++++++++++++++++++++++++++
		textField_nombre_Inf = new JTextField();

		textField_departamento_Inf = new JTextField();

		textField_eMail_Inf = new JTextField();

		jList_PerfilesBBDD = new JList<Perfil>();
		modelo_jList_PerfilesBBDD = new DefaultListModel<Perfil>();

		jList_PerfilesCSV = new JList<Perfil>();
		jList_PerfilesCSV.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				manejadorSeleccion_jList_PerfilesCSV();
			}
		});
		modelo_jList_PerfilesCSV = new DefaultListModel<Perfil>();

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

		JLabel etiquetaTitulo = new JLabel("Cargar CSV y guardar en Base de Datos");
		panel.add(etiquetaTitulo);

		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_Izquierdo.setBackground(new Color(215, 137, 21));

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
								.addGroup(gl_panel_Izquierdo.createSequentialGroup().addContainerGap().addComponent(
										lblNewLabel_BBDD, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_Izquierdo.createSequentialGroup().addContainerGap()
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
								.addGroup(gl_panel_Izquierdo.createSequentialGroup().addGap(67)
										.addComponent(button_Refrescar_Lista)))
						.addContainerGap()));
		gl_panel_Izquierdo.setVerticalGroup(gl_panel_Izquierdo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Izquierdo.createSequentialGroup().addComponent(lblNewLabel_BBDD).addGap(16)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(button_Refrescar_Lista).addGap(5)));
		scrollPane.setViewportView(jList_PerfilesBBDD);
		jList_PerfilesBBDD.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				manejadorSeleccion_jList_PerfilesBBDD(); // +++++++++++++++++++++++++++++++++
			}
		});

		jList_PerfilesBBDD.setModel(modelo_jList_PerfilesBBDD);// ++++++++
		panel_Izquierdo.setLayout(gl_panel_Izquierdo);

		contentPane.add(panel_Derecho, BorderLayout.EAST);

		JLabel lblNewLabel_CSV = new JLabel("CSV");
		lblNewLabel_CSV.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JScrollPane scrollPane_1 = new JScrollPane();

		JButton boton_CargarCSV = new JButton("Cargar CSV");
		boton_CargarCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_boton_CargarCSV();
			}
		});

		JButton botonGuardarCSVEnBBDD = new JButton("Guardar En BBDD");
		botonGuardarCSVEnBBDD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_boton_GuardarListaCSVEnBBDD();
			}
		});

		JButton boton_BorrarLista = new JButton("Borrar Lista");
		boton_BorrarLista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				manejadorBorrarListaCSV();
			}
		});

		JLabel lblNewLabel_3 = new JLabel("Caracter separador:");

		comboBox = new JComboBox<Character>();
		CSV.inicializarComboBoxDeSeparadores(comboBox);

		JButton boton_OrdenarListaCSVCargado = new JButton("Ordenar Lista");
		boton_OrdenarListaCSVCargado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_boton_OrdenarListaCSVCargado();
			}

		});
		GroupLayout gl_panel_Derecho = new GroupLayout(panel_Derecho);
		gl_panel_Derecho.setHorizontalGroup(gl_panel_Derecho.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Derecho.createSequentialGroup().addContainerGap().addGroup(gl_panel_Derecho
						.createParallelGroup(Alignment.TRAILING).addComponent(scrollPane_1, 0, 0, Short.MAX_VALUE)
						.addGroup(gl_panel_Derecho.createSequentialGroup()
								.addComponent(lblNewLabel_CSV, GroupLayout.PREFERRED_SIZE, 29,
										GroupLayout.PREFERRED_SIZE)
								.addGap(42).addComponent(lblNewLabel_3).addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_Derecho.createSequentialGroup().addGap(10).addGroup(gl_panel_Derecho
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_Derecho.createSequentialGroup().addComponent(boton_BorrarLista)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(boton_OrdenarListaCSVCargado))
								.addComponent(botonGuardarCSVEnBBDD, GroupLayout.PREFERRED_SIZE, 202,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(boton_CargarCSV, GroupLayout.PREFERRED_SIZE, 202,
										GroupLayout.PREFERRED_SIZE))
								.addGap(5)))
						.addContainerGap()));
		gl_panel_Derecho.setVerticalGroup(gl_panel_Derecho.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Derecho.createSequentialGroup()
						.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_CSV, GroupLayout.PREFERRED_SIZE, 14,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_3)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addGap(18).addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(botonGuardarCSVEnBBDD)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.BASELINE)
								.addComponent(boton_BorrarLista).addComponent(boton_OrdenarListaCSVCargado))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(boton_CargarCSV).addGap(7)));

		jList_PerfilesCSV.setModel(modelo_jList_PerfilesCSV);// ++++++++

		scrollPane_1.setViewportView(jList_PerfilesCSV);
		panel_Derecho.setLayout(gl_panel_Derecho);

		contentPane.add(panel_central_Bordes_1, BorderLayout.CENTER);
		panel_central_Bordes_1.setLayout(new BoxLayout(panel_central_Bordes_1, BoxLayout.Y_AXIS));
		panel_box_sup.setBackground(new Color(64, 128, 128));

		panel_central_Bordes_1.add(panel_box_sup);
		panel_box_sup.setLayout(new BoxLayout(panel_box_sup, BoxLayout.Y_AXIS));
		panel_2.setBackground(new Color(215, 137, 21));

		panel_box_sup.add(panel_2);

		panel_2.add(lblNewLabel_9);
		panel_4.setBackground(new Color(255, 128, 0));

		panel_box_sup.add(panel_4);

		textField_nombre_Sup = new JTextField();
		textField_nombre_Sup.setColumns(10);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
						.addComponent(lblNewLabel_4, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addGap(26).addComponent(textField_nombre_Sup, GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
						.addGap(39)));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
						.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_4)
								.addGroup(gl_panel_4.createSequentialGroup().addContainerGap().addComponent(
										textField_nombre_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(16, Short.MAX_VALUE)));
		panel_4.setLayout(gl_panel_4);
		panel_7.setBackground(new Color(255, 128, 0));

		panel_box_sup.add(panel_7);

		comboBox_Rol_Sup = new JComboBox<String>();

		Roles.inicializarComboBoxDeRoles(comboBox_Rol_Sup); /// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		JLabel label_Contrasea1 = new JLabel("Contrase\u00F1a");
		// Por seguridad el campo contraseña y su etiqueta, de momento, se oculta. Se
		// deja por posibles cambios.
		label_Contrasea1.setVisible(true);

		textField_ContraseñaCifrada_Sup = new JPasswordField();
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup()
						.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_7.createSequentialGroup()
										.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)
										.addGap(25))
								.addGroup(gl_panel_7.createSequentialGroup().addGap(28)
										.addComponent(comboBox_Rol_Sup, 0, 187, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)))
						.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_7.createSequentialGroup().addGap(43)
										.addComponent(textField_ContraseñaCifrada_Sup, GroupLayout.DEFAULT_SIZE, 124,
												Short.MAX_VALUE)
										.addGap(43))
								.addGroup(gl_panel_7.createSequentialGroup().addGap(33).addComponent(label_Contrasea1)
										.addContainerGap()))));
		gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_7.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
						.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_7.createSequentialGroup().addComponent(lblNewLabel_5)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(comboBox_Rol_Sup,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_7.createSequentialGroup().addGap(1).addComponent(label_Contrasea1)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_ContraseñaCifrada_Sup, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGap(31)));
		panel_7.setLayout(gl_panel_7);
		panel_8.setBackground(new Color(255, 128, 0));

		panel_box_sup.add(panel_8);

		textField_departamento_Sup = new JTextField();
		textField_departamento_Sup.setColumns(10);
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(gl_panel_8.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_8
				.createSequentialGroup().addComponent(lblNewLabel_6).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(textField_departamento_Sup, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE).addGap(40)));
		gl_panel_8.setVerticalGroup(gl_panel_8.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_8
				.createSequentialGroup()
				.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_6)
						.addGroup(gl_panel_8.createSequentialGroup().addGap(11).addComponent(textField_departamento_Sup,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_8.setLayout(gl_panel_8);
		panel_9.setBackground(new Color(255, 128, 0));

		panel_box_sup.add(panel_9);

		textField_eMail_Sup = new JTextField();
		textField_eMail_Sup.setColumns(10);
		GroupLayout gl_panel_9 = new GroupLayout(panel_9);
		gl_panel_9.setHorizontalGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
						.addComponent(lblNewLabel_7, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addGap(26).addComponent(textField_eMail_Sup, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
						.addGap(40)));
		gl_panel_9.setVerticalGroup(gl_panel_9.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_9
				.createSequentialGroup()
				.addGroup(gl_panel_9.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_7)
						.addGroup(gl_panel_9.createSequentialGroup().addContainerGap().addComponent(textField_eMail_Sup,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_9.setLayout(gl_panel_9);

		panel_13.setBackground(new Color(215, 137, 21));
		panel_box_sup.add(panel_13);

		JButton boton_BorrarPerfil = new JButton("Borrar Perfil");
		boton_BorrarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonBorrarPerfil();
			}
		});
		panel_13.add(boton_BorrarPerfil);

		JButton boton_ActualizarPerfil = new JButton("Actualizar Perfil");
		boton_ActualizarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_Boton_ActualizarPerfil();
			}
		});
		panel_13.add(boton_ActualizarPerfil);
		panel_box_inf.setBackground(new Color(0, 128, 192));

		panel_central_Bordes_1.add(panel_box_inf);
		panel_box_inf.setLayout(new BoxLayout(panel_box_inf, BoxLayout.Y_AXIS));
		panel_5.setBackground(new Color(0, 128, 192));

		panel_box_inf.add(panel_5);

		panel_5.add(lblNewLabel_10);
		panel_6.setBackground(new Color(0, 128, 255));

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
		panel_10.setBackground(new Color(0, 128, 255));

		panel_box_inf.add(panel_10);

		comboBox_Rol_Inf = new JComboBox<String>();
		Roles.inicializarComboBoxDeRoles(comboBox_Rol_Inf); /// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		JLabel label_Contrasea2 = new JLabel("Contrase\u00F1a");

		textField_ContraseñaCifrada_Inf = new JPasswordField();
		textField_ContraseñaCifrada_Inf.setEditable(false);

		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10.setHorizontalGroup(gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_10.createSequentialGroup()
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addGap(43).addComponent(comboBox_Rol_Inf, 0, 126, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_Contrasea2)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textField_ContraseñaCifrada_Inf, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
						.addGap(38)));
		gl_panel_10.setVerticalGroup(gl_panel_10.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_10
				.createSequentialGroup()
				.addGroup(gl_panel_10.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_1)
						.addGroup(gl_panel_10.createSequentialGroup().addContainerGap()
								.addGroup(gl_panel_10.createParallelGroup(Alignment.BASELINE)
										.addComponent(comboBox_Rol_Inf, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(textField_ContraseñaCifrada_Inf, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(label_Contrasea2))
				.addContainerGap(15, Short.MAX_VALUE)));
		panel_10.setLayout(gl_panel_10);
		panel_11.setBackground(new Color(0, 128, 255));

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
		panel_14.setBackground(new Color(0, 128, 255));

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

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(0, 128, 255));
		panel_box_inf.add(panel_3);

		JLabel lblNewLabel_11 = new JLabel("Contrase\u00F1a nueva:");

		passwordField_NuevaContraseña = new JPasswordField();

		JButton boton_CambiarContraseña = new JButton("Cambiar contrase\u00F1a");
		boton_CambiarContraseña.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonCambiarContraseña();
			}
		});
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3
				.createSequentialGroup().addComponent(lblNewLabel_11).addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(passwordField_NuevaContraseña, GroupLayout.PREFERRED_SIZE, 155,
						GroupLayout.PREFERRED_SIZE)
				.addGap(18)
				.addComponent(boton_CambiarContraseña, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(70, Short.MAX_VALUE)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_11)
								.addComponent(passwordField_NuevaContraseña, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(boton_CambiarContraseña))
						.addContainerGap(19, Short.MAX_VALUE)));
		panel_3.setLayout(gl_panel_3);
		panel_12.setBackground(new Color(0, 128, 192));
		panel_box_inf.add(panel_12);

		botonModificarPerfilCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				manejador_BotonModificarPerfilCSV();// +++++++++++++++++
				// cargarListaPerfiles();

			}
		});

		JButton botonEliminarPerfilCSV = new JButton("Eliminar Perfil");
		botonEliminarPerfilCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonEliminarPerfilCSV();
			}
		});
		panel_12.add(botonEliminarPerfilCSV);

		panel_12.add(botonModificarPerfilCSV);

		recargar_jList_PerfilesBBDD();// ++++++++++++++++++++++++
	}/// FIN
		/// inicializarGUI()///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Ordena los perfiles cargados en la JList del mismo panel en orden alfabetico segun el campo nombre de estos **/
	private void manejador_boton_OrdenarListaCSVCargado() {

		Utils.ordenarJList(jList_PerfilesCSV);

	}
	
	/** Cambia y cifra la contraseña del perfil seleccionado. Comprueba si el camco esta vacio o solo tiene espacios con el metodo Utils.comprobarStringNoVacio() que reporta esta situaccion con un JPanel.
	 * Usa el metodo cifrarContraseña() de la clase AccesoADatosBBDD para cifrarla. Este metodo captura cualquier excepcion que ocurriera en el proceso y la reporta en el JFrame desde el que se invoca.
	 *  Limpia el campo contraseña y por ultimo eporta si el cambio fue exitoso.  **/
	private void manejador_BotonCambiarContraseña() {

		if (perfilSeleccionadoCSV == null) {
			JOptionPane.showMessageDialog(this, "Seleccione un perfil de usuario.");
			return;
		} // Si no puede dar error cuando se ha recargado la lista.
			// UUID id = perfilSeleccionadoCSV.getId();
			// Comprobacion de contraseña y lipiado espacios de campos
		@SuppressWarnings("deprecation")
		String contraseñaSinCifrar = passwordField_NuevaContraseña.getText().strip();
		if (!Utils.comprobarStringNoVacio(contraseñaSinCifrar, this)) {
			return;
		}
		String contraseñaCifrada = accesoBBDD.cifrarContraseña(contraseñaSinCifrar, this);
		perfilSeleccionadoCSV.setContraseñaCifrada(contraseñaCifrada);
		passwordField_NuevaContraseña.setText("");
		JOptionPane.showMessageDialog(this, "Se ha cambiado la contraseña del perfil: " + perfilSeleccionadoCSV);

		System.out.println("--Cambiada contraseña de perfil: " + perfilSeleccionadoCSV);
	}// Fin manejador_BotonCambiarContraseña()

	private void manejadorBorrarListaCSV() {

		modelo_jList_PerfilesCSV.clear();

	}// Fin manejadorBorrarListaCSV()

	/**
	 * Elimina el perfil seleccionado de la lista de perfiles cargados de archivos
	 * CSV
	 */
	private void manejador_BotonEliminarPerfilCSV() {

		if (perfilSeleccionadoCSV == null) {
			return;
		}
		modelo_jList_PerfilesCSV.removeElement(perfilSeleccionadoCSV);
		limpiarCampos_Inferiores();
		jList_PerfilesCSV.clearSelection();

	}// Fin botonEliminarPerfilCSV()

	/**
	 * Modifica el perfil seleccionado de la lista de perfiles cargados de archivos
	 * CSV segun los campos inferiores. El campo Contraseña no se modifica aqui
	 */
	private void manejador_BotonModificarPerfilCSV() {

		// modifica campos no contra

		String nombre = textField_nombre_Inf.getText();
		String rol = (String) comboBox_Rol_Inf.getSelectedItem();
		String departamento = textField_departamento_Inf.getText();
		String email = textField_eMail_Inf.getText();

		Perfil nuevoPerfil = new Perfil(nombre, rol, departamento, email);

		boolean perfilCorrecto = Utils.limtiarYComprobarPerfilSinContraseña(nuevoPerfil, this);
		if (!perfilCorrecto) {
			return;
		}

		perfilSeleccionadoCSV.setNombre(nuevoPerfil.getNombre());
		perfilSeleccionadoCSV.setRol(nuevoPerfil.getRol());
		perfilSeleccionadoCSV.setDepartamento(nuevoPerfil.getDepartamento());
		perfilSeleccionadoCSV.setEmail(nuevoPerfil.getEmail());
		// perfilSeleccionadoCSV.setContraseñaSinCifrar(nuevoPerfil.getContraseñaSinCifrar());

		limpiarCampos_Inferiores();
		jList_PerfilesCSV.clearSelection();

	}// Fin manejador_BotonModificarPerfilCSV()

	/** Guarda los objetos de la lista en la BBDD. Previamente limpia y comprueva que los campos no esten vacios y comprueva que la contraseña este cifrada en el formato correcto. **/
	private void manejador_boton_GuardarListaCSVEnBBDD() {

		for (Object perfil : modelo_jList_PerfilesCSV.toArray()) {
			// Object perfil = modelo_jList_PerfilesCSV.getElementAt(i);
			System.out.println(perfil);

			boolean perfilCorrecto = Utils.limtiarYComprobarPerfilConContraseñaCifrada((Perfil) perfil, this);
			if (!perfilCorrecto) {
				continue;
			}
			boolean contraseñaCorrecta = Utils
					.comprobarSiContraseñaCifradaFormatoCorrecto(((Perfil) perfil).getContraseñaCifrada(), this);
			if (!contraseñaCorrecta) {
				continue;
			}

			boolean exitoAlGuardar = accesoBBDD.guardarPerfilConContraseñaCifrada((Perfil) perfil, this);
			if (exitoAlGuardar) {
				modelo_jList_PerfilesCSV.removeElement(perfil);
			}

		}
		recargar_jList_PerfilesBBDD();

	}// Fin manejador_boton_GuardarListaCSVEnBBDD()

	/** Carga los valores del archivo CSV seleccionado con JFileChooser en la lista situada en su mismo panel.
	 * Para esto invoca el método CSV.extraerCSV pasandole el objeto File del archivo seleccionado , 
	 * el caracter de separacion establecido en el ComBox pertinente y una referencia al propio JFrame para que 
	 * el citado metodo reporte en el los posibles fallos al cargar el CSV. Por ultimo invoca el metodo Utils.cargarListaDePerfilesEnJList()
	 * para cargar en la lista situada en su mismo panel los perfiles extraidos del CSV en una List. **/
	private void manejador_boton_CargarCSV() {

		JFileChooser selectorArchivos = new JFileChooser();
		selectorArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filtroDeArchivo = new FileNameExtensionFilter("Solo CSVs", "csv");
		selectorArchivos.setFileFilter(filtroDeArchivo);

		int opcionElegida = selectorArchivos.showOpenDialog(this);
		if (!(opcionElegida == JFileChooser.APPROVE_OPTION)) {
			return;
		}

		File csv_File = selectorArchivos.getSelectedFile();
		// por si no se selecciona nada con JFileChooser
		if (csv_File == null) {
			return;
		}

		List<Perfil> perfiles_List = CSV.extraerCSV(csv_File, (Character) comboBox.getSelectedItem(), this);

		Utils.cargarListaDePerfilesEnJList(perfiles_List, modelo_jList_PerfilesCSV);

	}

	/** Rellena los campos superiores con los valores del perfil selecionado. **/
	private void manejadorSeleccion_jList_PerfilesCSV() {
		perfilSeleccionadoCSV = (Perfil) jList_PerfilesCSV.getSelectedValue();
		if (perfilSeleccionadoCSV == null) {
			return;
		} // Si no puede dar error
			// perfilSeleccionadoJListBBDDCopia = perfilSeleccionado.crearCopia() ;

		textField_nombre_Inf.setText(perfilSeleccionadoCSV.getNombre());
		comboBox_Rol_Inf.setSelectedItem(perfilSeleccionadoCSV.getRol());
		textField_departamento_Inf.setText(perfilSeleccionadoCSV.getDepartamento());
		textField_eMail_Inf.setText(perfilSeleccionadoCSV.getEmail());
		textField_ContraseñaCifrada_Inf.setText(perfilSeleccionadoCSV.getContraseñaCifrada());
	}

	/** Rellena los campos superiores con los valores del perfil selecionado. **/
	private void manejadorSeleccion_jList_PerfilesBBDD() {
		Perfil perfilSeleccionado = (Perfil) jList_PerfilesBBDD.getSelectedValue();
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
	 * perfil seleccionado en la JList cargada de la BBDD
	 **/
	@SuppressWarnings("deprecation")
	private void manejador_Boton_ActualizarPerfil() {

		if (perfilSeleccionadoJListBBDDCopia == null) {
			return;
		} // Si no puede dar error
		System.out.println("--perfilSeleccionadoJListBBDDCopia : " + perfilSeleccionadoJListBBDDCopia);

		perfilSeleccionadoJListBBDDCopia.setNombre(textField_nombre_Sup.getText());
		perfilSeleccionadoJListBBDDCopia.setRol((String) comboBox_Rol_Sup.getSelectedItem());
		perfilSeleccionadoJListBBDDCopia.setDepartamento(textField_departamento_Sup.getText());
		perfilSeleccionadoJListBBDDCopia.setEmail(textField_eMail_Sup.getText());
		perfilSeleccionadoJListBBDDCopia.setContraseñaSinCifrar(textField_ContraseñaCifrada_Sup.getText());

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

		accesoBBDD.borrarPerfil(id, this);
		System.out.println("--Borrado usuario con ID: " + id);

		recargar_jList_PerfilesBBDD();
		// limpiarCampos_Superiores();
	}

	// Fin manejadores de eventos
	// -------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * cargarListaPerfiles():Refresca la lista de perfiles. Carga todos los
	 * registros de la tabla "perfiles" (devueltos por el metodo
	 * accesoBBDD.obtenerTodosLosPerfiles()) en la JList jList_Perfiles despues de
	 * limpiarla.
	 */
	private void recargar_jList_PerfilesBBDD() {

		List<Perfil> list_Perfiles = accesoBBDD.obtenerTodosLosPerfiles();
		modelo_jList_PerfilesBBDD.clear();
		Utils.cargarListaDePerfilesEnJList(list_Perfiles, modelo_jList_PerfilesBBDD);
		limpiarCampos_Superiores();

	}//// Fin cargarListaPerfiles()

	/** Limpia los campos de texto del panel superior */
	private void limpiarCampos_Superiores() {

		textField_nombre_Sup.setText("");
		comboBox_Rol_Sup.setSelectedItem(Roles.roles[0]);
		textField_departamento_Sup.setText("");
		textField_eMail_Sup.setText("");

	}/// Fin limpiarCampos_Superiores ()

	/** Limpia los campos de texto del panel inferior */
	private void limpiarCampos_Inferiores() {

		textField_nombre_Inf.setText("");
		comboBox_Rol_Inf.setSelectedItem(Roles.roles[0]);
		textField_departamento_Inf.setText("");
		textField_eMail_Inf.setText("");
		textField_ContraseñaCifrada_Inf.setText("");

	}/// Fin limpiarCampos_Inferiores
}////// Fin de clase----------------------
