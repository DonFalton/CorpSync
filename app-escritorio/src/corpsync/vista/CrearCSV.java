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
 * Clase CrearCSV : Crea un archivo CSV de perfiles. Tiene dos listas(JList) que
 * muestran todos los perfiles cargados de un archivo CSV una y la otra los
 * perfiles que se guardarán en el nuevo CSV. Tiene dos paneles con campos de
 * texto y un combobox cada uno. Un panel muestra los campos seleccionados en la
 * lista para crea el nuevo archivo CSV permitiendo modificarlos o borrarlos y
 * otro permite introducir los campos para el nuevo perfil que se añade a la
 * lista para crear el archivo CSV. Los registros de perfiles se pueden
 * introducir de nuevas, con este último panel o pasarlos de otro archivo CSV
 * cargado previamente en la lista de carga de CSV
 **/

public class CrearCSV extends JFrame {

	private static final long serialVersionUID = 1L;
	private AccesoADatosBBDD accesoBBDD;
	private Perfil perfilSeleccionadoJListCargarCSV;

	private Perfil perfilSeleccionadoJListCrearCSV;

	// Componentes manipulados :

	private DefaultListModel<Perfil> modelo_jList_PerfilesCrearCSV;
	private DefaultListModel<Perfil> modelo_jList_PerfilesCargarCSV;
	private JList<Perfil> jList_PerfilesCrearCSV;
	private JList<Perfil> jList_PerfilesCargarCSV;
	private JTextField textField_departamento_Sup;
	private JTextField textField_eMail_Sup;
	private JTextField textField_nombre_Sup;

	private JTextField textField_nombre_Inf;
	private JTextField textField_departamento_Inf;

	private JTextField textField_eMail_Inf;

	private JComboBox<String> comboBox_Rol_Inf;
	private JComboBox<String> comboBox_Rol_Sup;
	private JComboBox<Character> comboBox_GuardarCSV;
	JComboBox<Character> comboBox_CrearCSV;
	private JPasswordField passwordField_NuevaContraseña;
	private JPasswordField textField_ContraseñaCifrada_Inf;
	private JPasswordField textField_ContraseñaCifrada_Sup;

	/*
	 * Solo para pruevas public static void main(String[] args) {
	 * EventQueue.invokeLater(new Runnable() { public void run() { try { CrearCSV
	 * frame = new CrearCSV(); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

// Solo para pruevas
	public CrearCSV() {

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 965, 600);
		setMinimumSize(new Dimension(965, 600));

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

	public CrearCSV(VentanaPrincipal ventanaPrincipal) {

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 965, 600);
		setMinimumSize(new Dimension(965, 600));

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
		panel_central_Bordes_1.setBackground(new Color(0, 128, 192));
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
		JLabel lbl_ParaCrearCSV = new JLabel("Nuevo CSV");
		lbl_ParaCrearCSV.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JLabel lblNewLabel_8 = new JLabel("EMail");
		JLabel lblNewLabel_10 = new JLabel("A\u00F1adir nuevo perfil para crear nuevo CSV desde CSV cargado >>>");
		JLabel lblNewLabel_4 = new JLabel("Nombre");
		JLabel lblNewLabel_9 = new JLabel("<<<    Modificar perfiles de la lista para crear el nuevo CSV");
		JLabel lblNewLabel_5 = new JLabel("Rol");
		JLabel lblNewLabel_6 = new JLabel("Departamento");
		JLabel lblNewLabel_7 = new JLabel("EMail");

		//////////////////////////////////////////////////////////////////

		// ++++++++++++++++++++ Componentes manipulados
		// +++++++++++++++++++++++++++++++++++
		textField_nombre_Inf = new JTextField();

		textField_departamento_Inf = new JTextField();

		textField_eMail_Inf = new JTextField();

		jList_PerfilesCrearCSV = new JList<Perfil>();
		modelo_jList_PerfilesCrearCSV = new DefaultListModel<Perfil>();

		jList_PerfilesCargarCSV = new JList<Perfil>();
		jList_PerfilesCargarCSV.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				manejadorSeleccion_jList_PerfilesCSV();
			}
		});
		modelo_jList_PerfilesCargarCSV = new DefaultListModel<Perfil>();

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

		JLabel etiquetaTitulo = new JLabel("Crear archivos CSV nuevos");
		etiquetaTitulo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(etiquetaTitulo);

		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_Izquierdo.setBackground(new Color(0, 128, 128));

		contentPane.add(panel_Izquierdo, BorderLayout.WEST);

		JScrollPane scrollPane = new JScrollPane();

		JButton btnBorrarlista = new JButton("BorrarLista");
		btnBorrarlista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonBorrarListaPerfilesCrearCSV();// +++++++++++++++++++++++++++++++++++

			}
		});

		JButton boton_CrearCSV = new JButton("Crear CSV");
		boton_CrearCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				manejadorBotonCrearCSV();
			}
		});

		comboBox_CrearCSV = new JComboBox<Character>();
		CSV.inicializarComboBoxDeSeparadores(comboBox_CrearCSV);

		JLabel lblNewLabel_11 = new JLabel("Caracter separador:");

		JButton btnNewButton_1 = new JButton("Ordenar Lista");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_boton_OrdenarListaCSVNuevo();
			}
		});
		GroupLayout gl_panel_Izquierdo = new GroupLayout(panel_Izquierdo);
		gl_panel_Izquierdo.setHorizontalGroup(gl_panel_Izquierdo.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_Izquierdo.createSequentialGroup().addContainerGap().addGroup(gl_panel_Izquierdo
						.createParallelGroup(Alignment.LEADING)
						.addComponent(boton_CrearCSV, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
						.addGroup(gl_panel_Izquierdo.createSequentialGroup().addComponent(btnBorrarlista)
								.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE).addComponent(
										btnNewButton_1, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_Izquierdo.createSequentialGroup().addComponent(lbl_ParaCrearCSV)
								.addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
								.addComponent(lblNewLabel_11, GroupLayout.PREFERRED_SIZE, 105,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(comboBox_CrearCSV,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		gl_panel_Izquierdo.setVerticalGroup(gl_panel_Izquierdo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Izquierdo.createSequentialGroup()
						.addGroup(gl_panel_Izquierdo.createParallelGroup(Alignment.BASELINE)
								.addComponent(lbl_ParaCrearCSV)
								.addComponent(comboBox_CrearCSV, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_11))
						.addGap(16).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(boton_CrearCSV)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_Izquierdo.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnBorrarlista).addComponent(btnNewButton_1))
						.addGap(10)));
		scrollPane.setViewportView(jList_PerfilesCrearCSV);
		jList_PerfilesCrearCSV.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				manejadorSeleccion_jList_PerfilesBBDD(); // +++++++++++++++++++++++++++++++++
			}
		});

		jList_PerfilesCrearCSV.setModel(modelo_jList_PerfilesCrearCSV);// ++++++++
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

		JButton boton_BorrarLista = new JButton("Borrar Lista");
		boton_BorrarLista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				manejador_BotonBorrarListaPerfilesCargaCSV();
			}
		});

		JLabel lblNewLabel_3 = new JLabel("Caracter separador:");

		comboBox_GuardarCSV = new JComboBox<Character>();
		CSV.inicializarComboBoxDeSeparadores(comboBox_GuardarCSV);

		JButton botonANuevoCSV = new JButton("<<< A\u00F1adir seleccionados a CSV");
		botonANuevoCSV.setToolTipText("Carga los perfiles seleccionados en la lista para crear el nuevo archivo CSV.");
		botonANuevoCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_botonANuevoCSV();
			}
		});

		JButton btnNewButton = new JButton("Ordenar Lista");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_boton_OrdenarListaCSVCargado();
			}
		});
		GroupLayout gl_panel_Derecho = new GroupLayout(panel_Derecho);
		gl_panel_Derecho.setHorizontalGroup(gl_panel_Derecho.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_Derecho.createSequentialGroup().addGap(21)
						.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
								.addGroup(gl_panel_Derecho.createSequentialGroup()
										.addComponent(lblNewLabel_CSV, GroupLayout.PREFERRED_SIZE, 29,
												GroupLayout.PREFERRED_SIZE)
										.addGap(42).addComponent(lblNewLabel_3).addGap(18)
										.addComponent(comboBox_GuardarCSV, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(boton_CargarCSV, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
								.addGroup(gl_panel_Derecho.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(boton_BorrarLista, GroupLayout.PREFERRED_SIZE, 87,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
								.addComponent(botonANuevoCSV, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 217,
										Short.MAX_VALUE))
						.addContainerGap()));
		gl_panel_Derecho.setVerticalGroup(gl_panel_Derecho.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Derecho.createSequentialGroup()
						.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_CSV, GroupLayout.PREFERRED_SIZE, 14,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_3).addComponent(comboBox_GuardarCSV,
										GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(botonANuevoCSV)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.LEADING).addComponent(btnNewButton)
								.addComponent(boton_BorrarLista))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(boton_CargarCSV).addGap(5)));

		jList_PerfilesCargarCSV.setModel(modelo_jList_PerfilesCargarCSV);// ++++++++

		scrollPane_1.setRowHeaderView(jList_PerfilesCargarCSV);
		panel_Derecho.setLayout(gl_panel_Derecho);

		contentPane.add(panel_central_Bordes_1, BorderLayout.CENTER);
		panel_central_Bordes_1.setLayout(new BoxLayout(panel_central_Bordes_1, BoxLayout.Y_AXIS));
		panel_box_sup.setBackground(new Color(0, 128, 128));

		panel_central_Bordes_1.add(panel_box_sup);
		panel_box_sup.setLayout(new BoxLayout(panel_box_sup, BoxLayout.Y_AXIS));
		panel_2.setBackground(new Color(0, 128, 128));

		panel_box_sup.add(panel_2);

		panel_2.add(lblNewLabel_9);
		panel_4.setBackground(new Color(128, 128, 0));

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
		panel_7.setBackground(new Color(128, 128, 0));

		panel_box_sup.add(panel_7);

		comboBox_Rol_Sup = new JComboBox<String>();

		Roles.inicializarComboBoxDeRoles(comboBox_Rol_Sup); /// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		JLabel label_Contrasea1 = new JLabel("Contrase\u00F1a");

		textField_ContraseñaCifrada_Sup = new JPasswordField();
		textField_ContraseñaCifrada_Sup.setEditable(false);
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup()
						.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addGap(27).addComponent(comboBox_Rol_Sup, 0, 131, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_Contrasea1)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(textField_ContraseñaCifrada_Sup, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
						.addGap(42)));
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
				.addContainerGap(15, Short.MAX_VALUE)));
		panel_7.setLayout(gl_panel_7);
		panel_8.setBackground(new Color(128, 128, 0));

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
		panel_9.setBackground(new Color(128, 128, 0));

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

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(0, 128, 128));
		panel_box_sup.add(panel_3);

		JLabel lblNewLabel_12 = new JLabel(" Contrase\u00F1a nueva:");

		passwordField_NuevaContraseña = new JPasswordField();

		JButton boton_CambiarContraseña = new JButton("Cambiar contrase\u00F1a");
		boton_CambiarContraseña.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonCambiarContraseña();
			}
		});
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup().addComponent(lblNewLabel_12)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(passwordField_NuevaContraseña, GroupLayout.PREFERRED_SIZE, 113,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(boton_CambiarContraseña).addGap(75)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup().addGap(6)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_12)
								.addComponent(passwordField_NuevaContraseña, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(boton_CambiarContraseña))));
		panel_3.setLayout(gl_panel_3);

		panel_13.setBackground(new Color(0, 128, 128));
		panel_box_sup.add(panel_13);

		JButton boton_BorrarPerfil = new JButton("Borrar Perfil");
		boton_BorrarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonEliminarPerfil();
			}
		});
		JButton botonModificarPerfilCSV = new JButton("Modificar Perfil");
		panel_13.add(botonModificarPerfilCSV);

		botonModificarPerfilCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				manejador_BotonModificarPerfilCSV();// +++++++++++++++++
				// cargarListaPerfiles();

			}
		});
		panel_13.add(boton_BorrarPerfil);
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
						.addComponent(textField_nombre_Inf, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
						.addGap(38)));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_6
				.createSequentialGroup()
				.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(
						textField_nombre_Inf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addContainerGap(16, Short.MAX_VALUE)));
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
						.addGap(43).addComponent(comboBox_Rol_Inf, 0, 131, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_Contrasea2)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textField_ContraseñaCifrada_Inf, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
						.addGap(36)));
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
				.addContainerGap(16, Short.MAX_VALUE)));
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
		panel_12.setBackground(new Color(0, 128, 192));
		panel_box_inf.add(panel_12);

		JButton botonAñadirNuevoPerfilCSV = new JButton("<<< A\u00F1adir Nuevo Perfil al CSV");
		botonAñadirNuevoPerfilCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonAñadirNuevoPerfilCSV();
			}
		});
		panel_12.add(botonAñadirNuevoPerfilCSV);

		manejador_BotonBorrarListaPerfilesCrearCSV();// ++++++++++++++++++++++++
	}/// FIN
		/// inicializarGUI()///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Ordena los perfiles cargados en la JList del mismo panel en orden alfabetico segun el campo nombre de estos **/
	private void manejador_boton_OrdenarListaCSVNuevo() {

		Utils.ordenarJList(jList_PerfilesCrearCSV);

	}

	/** Ordena los perfiles cargados en la JList del mismo panel en orden alfabetico segun el campo nombre de estos **/
	private void manejador_boton_OrdenarListaCSVCargado() {

		Utils.ordenarJList(jList_PerfilesCargarCSV);

	}
	
	/** Cambia y cifra la contraseña del perfil seleccionado. Comprueba si el camco esta vacio o solo tiene espacios con el metodo Utils.comprobarStringNoVacio() que reporta esta situaccion con un JPanel.
	 * Usa el metodo cifrarContraseña() de la clase AccesoADatosBBDD para cifrarla. Este metodo captura cualquier excepcion que ocurriera en el proceso y la reporta en el JFrame desde el que se invoca.
	 *  Limpia el campo contraseña y por ultimo eporta si el cambio fue exitoso.  **/

	private void manejador_BotonCambiarContraseña() {

		if (perfilSeleccionadoJListCrearCSV == null) {
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
		perfilSeleccionadoJListCrearCSV.setContraseñaCifrada(contraseñaCifrada);
		passwordField_NuevaContraseña.setText("");
		JOptionPane.showMessageDialog(this,
				"Se ha cambiado la contraseña del perfil: " + perfilSeleccionadoJListCrearCSV);
		System.out.println("--Cambiada contraseña de perfil: " + perfilSeleccionadoJListCrearCSV);

	}// Fin manejador_BotonCambiarContraseña()

	/**
	 * manejador_botonANuevoCSV() Carga los perfiles seleccionados en la lista para
	 * crear el nuevo archivo CSV Y los elimina de la lista donde estaban.
	 **/
	private void manejador_botonANuevoCSV() {

		if (jList_PerfilesCargarCSV.isSelectionEmpty()) {
			return;
		}
		List<Perfil> perfilesSeleccionados = jList_PerfilesCargarCSV.getSelectedValuesList();
		Utils.cargarListaDePerfilesEnJList(perfilesSeleccionados, modelo_jList_PerfilesCrearCSV);
		Utils.eliminarListaDePerfilesEnJList(perfilesSeleccionados, modelo_jList_PerfilesCargarCSV);

	}

	/**
	 * manejador_BotonModificarPerfilCSV(): Modifica el perfil seleccionado de la
	 * JList de perfiles para crear el nuevo archivo CSV segun los campos
	 * superiores. La contraseña no se cambia aqui
	 **/
	private void manejador_BotonModificarPerfilCSV() {

		String nombre = textField_nombre_Sup.getText();
		String rol = (String) comboBox_Rol_Sup.getSelectedItem();
		String departamento = textField_departamento_Sup.getText();
		String email = textField_eMail_Sup.getText();
		// String contraseña = textField_ContraseñaCifrada_Sup.getText(); // No se
		// modifica aqui.

		Perfil nuevoPerfil = new Perfil(nombre, rol, departamento, email);

		boolean perfilCorrecto = Utils.limtiarYComprobarPerfilSinContraseña(nuevoPerfil, this);
		if (!perfilCorrecto) {
			return;
		}

		perfilSeleccionadoJListCrearCSV.setNombre(nuevoPerfil.getNombre());
		perfilSeleccionadoJListCrearCSV.setRol(nuevoPerfil.getRol());
		perfilSeleccionadoJListCrearCSV.setDepartamento(nuevoPerfil.getDepartamento());
		perfilSeleccionadoJListCrearCSV.setEmail(nuevoPerfil.getEmail());
		// perfilSeleccionadoJListCrearCSV.setContraseña(nuevoPerfil.getContraseña());
		// // Con boton modificar contra

		limpiarCampos_Superiores();
		jList_PerfilesCrearCSV.clearSelection();

	}// Fin manejador_BotonModificarPerfilCSV()

	/**
	 * manejador_BotonEliminarPerfil(): Borra el perfil seleccionado en la JList
	 * para crear el nuevo CSV.
	 **/
	private void manejador_BotonEliminarPerfil() {

		if (perfilSeleccionadoJListCrearCSV == null) {
			return;
		}
		modelo_jList_PerfilesCrearCSV.removeElement(perfilSeleccionadoJListCrearCSV);
		limpiarCampos_Superiores();
		jList_PerfilesCrearCSV.clearSelection();
	}

	/**
	 * manejadorBotonCrearCSV(): Crea el CSV a partir de los perfiles de la JList situada en el mismo panel que el boton que dispara el evento.
	 * Utiliza la clase JFileChooser para crear el objeto File del archivo en el cual se creara el CSV estableciendo primero un filtroDeArchivo (FileNameExtensionFilter)
	 * para solo archivos CSV. Crea una lista de objetos perfil de la JList con el metodo Utils.crearListaDePerfilesDeJList()
	 *  a partir del modelo que esta guarda Y se lo pasa como parametro al método CSV.crearCSV() que crea finalmente el archivo. 
	 *  Este ultimo método reporta cualquier fallo ocurrido en el proceso en el propio JFrame a traves de la referencia de este pasada como parametro.
	 **/
	private void manejadorBotonCrearCSV() {

		JFileChooser selectorArchivos = new JFileChooser();
		FileNameExtensionFilter filtroDeArchivo = new FileNameExtensionFilter("Solo CSVs", "csv");
		selectorArchivos.setFileFilter(filtroDeArchivo);

		int opcionElegida = selectorArchivos.showSaveDialog(null);
		// Si la opcion elegida fue Guardar.
		if (!(opcionElegida == JFileChooser.APPROVE_OPTION)) {
			return;
		}

		File archivoParaGuardar = selectorArchivos.getSelectedFile();
		File archivoParaGuardarConExtension = new File(archivoParaGuardar.getAbsolutePath() + ".csv");

		List<Perfil> listaPerfiles = Utils.crearListaDePerfilesDeJList(modelo_jList_PerfilesCrearCSV);

		CSV.crearCSV(listaPerfiles, archivoParaGuardarConExtension, (Character) comboBox_CrearCSV.getSelectedItem(),
				this);

	}

	/**
	 * manejador_BotonBorrarListaPerfilesCrearCSV():Limpia la lista para crear el nuevo CSV de perfiles.
	* */
	private void manejador_BotonBorrarListaPerfilesCrearCSV() {

		modelo_jList_PerfilesCrearCSV.clear();

	}//// Fin cargarListaPerfiles()
	
	/**  Limpia la lista situada en su mismo panel.
	 */
	private void manejador_BotonBorrarListaPerfilesCargaCSV() {

		modelo_jList_PerfilesCargarCSV.clear();

	}// Fin manejadorBorrarListaCSV()

	/**
	 * manejador_BotonAñadirNuevoPerfilCSV(): Elimina el perfil seleccionado de la
	 * lista de perfiles cargados de archivos CSV y lo añade a la lista para crear
	 * nuevo CSV.
	 */
	private void manejador_BotonAñadirNuevoPerfilCSV() {

		String nombre = textField_nombre_Inf.getText();
		String rol = (String) comboBox_Rol_Inf.getSelectedItem();
		String departamento = textField_departamento_Inf.getText();
		String email = textField_eMail_Inf.getText();
		@SuppressWarnings("deprecation")
		String contraseñaCifrada = textField_ContraseñaCifrada_Inf.getText();

		Perfil nuevoPerfil = new Perfil(nombre, rol, departamento, email);
		nuevoPerfil.setContraseñaCifrada(contraseñaCifrada);

		// Comprueva el perfil sin contraseña porque aqui puede ie vacia.
		boolean perfilCorrecto = Utils.limtiarYComprobarPerfilSinContraseña(nuevoPerfil, this);
		if (!perfilCorrecto) {
			return;
		}

		modelo_jList_PerfilesCrearCSV.addElement(nuevoPerfil);
		modelo_jList_PerfilesCargarCSV.removeElement(jList_PerfilesCargarCSV.getSelectedValue());
		limpiarCampos_Inferiores();

	}// Fin botonEliminarPerfilCSV()
	
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

		// int resultado =
		int opcionElegida = selectorArchivos.showOpenDialog(this);
		if (!(opcionElegida == JFileChooser.APPROVE_OPTION)) {
			return;
		}

		File csv_File = selectorArchivos.getSelectedFile();
		if (csv_File == null) {
			return;
		} // por si no se selecciona nada con JFileChooser

		List<Perfil> perfiles_List = CSV.extraerCSV(csv_File, (Character) comboBox_GuardarCSV.getSelectedItem(), this);

		Utils.cargarListaDePerfilesEnJList(perfiles_List, modelo_jList_PerfilesCargarCSV);

	}

	/** Rellena los campos inferiores con los valores del perfil selecionado. **/
	private void manejadorSeleccion_jList_PerfilesCSV() {
		perfilSeleccionadoJListCargarCSV = (Perfil) jList_PerfilesCargarCSV.getSelectedValue();
		if (perfilSeleccionadoJListCargarCSV == null) {
			return;
		} // Si no puede dar error
			// perfilSeleccionadoJListBBDDCopia = perfilSeleccionado.crearCopia() ;

		textField_nombre_Inf.setText(perfilSeleccionadoJListCargarCSV.getNombre());
		comboBox_Rol_Inf.setSelectedItem(perfilSeleccionadoJListCargarCSV.getRol());
		textField_departamento_Inf.setText(perfilSeleccionadoJListCargarCSV.getDepartamento());
		textField_eMail_Inf.setText(perfilSeleccionadoJListCargarCSV.getEmail());
		textField_ContraseñaCifrada_Inf.setText(perfilSeleccionadoJListCargarCSV.getContraseñaCifrada());
	}

	/** Rellena los campos superiores con los valores del perfil selecionado. **/
	private void manejadorSeleccion_jList_PerfilesBBDD() {
		perfilSeleccionadoJListCrearCSV = (Perfil) jList_PerfilesCrearCSV.getSelectedValue();
		if (perfilSeleccionadoJListCrearCSV == null) {
			return;
		} // Si no puede dar error

		textField_nombre_Sup.setText(perfilSeleccionadoJListCrearCSV.getNombre());
		comboBox_Rol_Sup.setSelectedItem(perfilSeleccionadoJListCrearCSV.getRol());
		// textField_rol.setText( perfilSeleccionado.getRol() );
		textField_departamento_Sup.setText(perfilSeleccionadoJListCrearCSV.getDepartamento());
		textField_eMail_Sup.setText(perfilSeleccionadoJListCrearCSV.getEmail());
		textField_ContraseñaCifrada_Sup.setText(perfilSeleccionadoJListCrearCSV.getContraseñaCifrada());
	}

	// Fin manejadores de eventos
	// -------------------------------------------------------------------------------------------------------------------------------------------------------

	/** Limpia los campos de texto del panel superior */
	private void limpiarCampos_Superiores() {

		textField_nombre_Sup.setText("");
		comboBox_Rol_Sup.setSelectedItem(Roles.roles[0]);
		textField_departamento_Sup.setText("");
		textField_eMail_Sup.setText("");
		textField_ContraseñaCifrada_Sup.setText("");

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
