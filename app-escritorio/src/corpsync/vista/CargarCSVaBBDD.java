package corpsync.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
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
import corpsync.controlador.Utilidades;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JScrollPane;
import java.awt.Font;



public class CargarCSVaBBDD extends JFrame {

	
	
	
	
	 private AccesoADatosBBDD accesoBBDD = new AccesoADatosBBDD(); /////////////////////////////////////////////
	 
	 private Perfil perfilSeleccionadoCSV ;
	 

		private Perfil perfilSeleccionadoJListBBDDCopia ;
		
		//private String[] roles = new String[]{"empleado" , "tecnico" , "admin"};
	 
	 
	 // Componentes manipulados :
		
		private DefaultListModel<Perfil> modelo_jList_PerfilesBBDD;
		private DefaultListModel<Perfil> modelo_jList_PerfilesCSV;
		private JList<Perfil> jList_PerfilesBBDD ;
		private JList<Perfil> jList_PerfilesCSV ;
	private  JTextField textField_departamento_Sup ;
	private JTextField textField_eMail_Sup;
	private JTextField textField_nombre_Sup;	
	
	private  JTextField textField_nombre_Inf ;
	private  JTextField textField_departamento_Inf ;

	private JTextField textField_eMail_Inf ;	
	
	private JComboBox<String> comboBox_Rol_Inf;
	private JComboBox<String> comboBox_Rol_Sup;
	private JComboBox<Character> comboBox;

	
	private JTextField textField_Contraseña_Sup;
	private JTextField textField_Contraseña_Inf;


	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CargarCSVaBBDD frame = new CargarCSVaBBDD();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CargarCSVaBBDD() {

		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 945, 600);
		
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
	
	public CargarCSVaBBDD(VentanaPrincipal ventanaPrincipal) {

		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 945, 600);
		
		addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	            	
	            	accesoBBDD.cerrarConexion();
	            	ventanaPrincipal.eliminarReferenciaDeVentanaAbierta();
	            	ventanaPrincipal.setVisible(true);
	                dispose(); 
	                //System.exit(0); 
	            }
	        });
		
		inicializarGUI();
	}
	

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
		
		//++++++++++++++++++++  Componentes manipulados +++++++++++++++++++++++++++++++++++
		textField_nombre_Inf = new JTextField();
		
		
		
		textField_departamento_Inf = new JTextField();

		textField_eMail_Inf = new JTextField();

		jList_PerfilesBBDD = new JList<Perfil>();
		modelo_jList_PerfilesBBDD = new DefaultListModel<Perfil>();
		
		jList_PerfilesCSV = new JList<Perfil>();
		jList_PerfilesCSV.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				manejadorSeleccion_jList_PerfilesCSV() ;
			}
		});
		modelo_jList_PerfilesCSV = new DefaultListModel<Perfil>();
		
		//++++++++++++++++++++ Fin Componentes manipulados +++++++++++++++++++++++++++++++++++

		
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
				recargar_jList_PerfilesBBDD();//+++++++++++++++++++++++++++++++++++
				
			}
		});
		GroupLayout gl_panel_Izquierdo = new GroupLayout(panel_Izquierdo);
		gl_panel_Izquierdo.setHorizontalGroup(
			gl_panel_Izquierdo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Izquierdo.createSequentialGroup()
					.addGroup(gl_panel_Izquierdo.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_Izquierdo.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
						.addGroup(gl_panel_Izquierdo.createSequentialGroup()
							.addGap(67)
							.addComponent(button_Refrescar_Lista))
						.addGroup(gl_panel_Izquierdo.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel_BBDD, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_Izquierdo.setVerticalGroup(
			gl_panel_Izquierdo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Izquierdo.createSequentialGroup()
					.addComponent(lblNewLabel_BBDD)
					.addGap(16)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
					.addGap(8)
					.addComponent(button_Refrescar_Lista))
		);
		scrollPane.setViewportView(jList_PerfilesBBDD);
		jList_PerfilesBBDD.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				manejadorSeleccion_jList_PerfilesBBDD() ; //+++++++++++++++++++++++++++++++++
			}
		});

		jList_PerfilesBBDD.setModel(modelo_jList_PerfilesBBDD);//++++++++
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
		
		JButton botonGuardarListaCSVEnBBDD = new JButton("GuardarEnBBDD");
		botonGuardarListaCSVEnBBDD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_boton_GuardarListaCSVEnBBDD();
			}
		});
		
		JButton boton_BorrarLista = new JButton("Borrar Lista");
		boton_BorrarLista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				manejadorBorrarListaCSV() ;
			}
		});
		
		JLabel lblNewLabel_3 = new JLabel("Caracter separador:");
		
		comboBox = new JComboBox<Character>();
		CSV.inicializarComboBoxDeSeparadores(comboBox );
		GroupLayout gl_panel_Derecho = new GroupLayout(panel_Derecho);
		gl_panel_Derecho.setHorizontalGroup(
			gl_panel_Derecho.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_Derecho.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.LEADING, false)
								.addComponent(scrollPane_1, 0, 0, Short.MAX_VALUE)
								.addGroup(gl_panel_Derecho.createSequentialGroup()
									.addComponent(botonGuardarListaCSVEnBBDD)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(boton_CargarCSV)))
							.addComponent(boton_BorrarLista))
						.addGroup(gl_panel_Derecho.createSequentialGroup()
							.addComponent(lblNewLabel_CSV, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
							.addGap(42)
							.addComponent(lblNewLabel_3)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_Derecho.setVerticalGroup(
			gl_panel_Derecho.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Derecho.createSequentialGroup()
					.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_CSV, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(boton_BorrarLista)
					.addGap(9)
					.addGroup(gl_panel_Derecho.createParallelGroup(Alignment.BASELINE)
						.addComponent(boton_CargarCSV)
						.addComponent(botonGuardarListaCSVEnBBDD)))
		);
		
		
		jList_PerfilesCSV.setModel(modelo_jList_PerfilesCSV);//++++++++
		
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
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addComponent(lblNewLabel_4, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(textField_nombre_Sup, GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
					.addGap(39))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_4)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addContainerGap()
							.addComponent(textField_nombre_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		panel_4.setLayout(gl_panel_4);
		panel_7.setBackground(new Color(255, 128, 0));
		
		panel_box_sup.add(panel_7);
		
		comboBox_Rol_Sup = new JComboBox<String>();
		
		Roles.inicializarComboBoxDeRoles(comboBox_Rol_Sup );  ///++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		JLabel label_Contrasea1 = new JLabel("Contrase\u00F1a");
		//Por seguridad el campo contraseña y su etiqueta, de momento, se oculta. Se deja por posibles cambios.
		label_Contrasea1.setVisible(false);
		
		textField_Contraseña_Sup = new JTextField();
		//Por seguridad el campo contraseña y su etiqueta, de momento, se oculta. Se deja por posibles cambios.
		textField_Contraseña_Sup.setVisible(false);
		textField_Contraseña_Sup.setColumns(10);
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addComponent(comboBox_Rol_Sup, 0, 127, Short.MAX_VALUE)
					.addGap(39)
					.addComponent(label_Contrasea1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_Contraseña_Sup, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_7.setVerticalGroup(
			gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_7.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBox_Rol_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_Contraseña_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(label_Contrasea1)
						.addComponent(lblNewLabel_5))
					.addContainerGap(28, Short.MAX_VALUE))
		);
		panel_7.setLayout(gl_panel_7);
		panel_8.setBackground(new Color(255, 128, 0));
		
		panel_box_sup.add(panel_8);
		

		
		textField_departamento_Sup = new JTextField();
		textField_departamento_Sup.setColumns(10);
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addComponent(lblNewLabel_6)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_departamento_Sup, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
					.addGap(40))
		);
		gl_panel_8.setVerticalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_6)
						.addGroup(gl_panel_8.createSequentialGroup()
							.addGap(11)
							.addComponent(textField_departamento_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_8.setLayout(gl_panel_8);
		panel_9.setBackground(new Color(255, 128, 0));
		
		panel_box_sup.add(panel_9);
		

		
		textField_eMail_Sup = new JTextField();
		textField_eMail_Sup.setColumns(10);
		GroupLayout gl_panel_9 = new GroupLayout(panel_9);
		gl_panel_9.setHorizontalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addComponent(lblNewLabel_7, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(textField_eMail_Sup, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
					.addGap(40))
		);
		gl_panel_9.setVerticalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_7)
						.addGroup(gl_panel_9.createSequentialGroup()
							.addContainerGap()
							.addComponent(textField_eMail_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
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
		panel_box_inf.setBackground(new Color(192, 192, 192));
		
		panel_central_Bordes_1.add(panel_box_inf);
		panel_box_inf.setLayout(new BoxLayout(panel_box_inf, BoxLayout.Y_AXIS));
		panel_5.setBackground(new Color(0, 128, 192));
		
		panel_box_inf.add(panel_5);
		
		
		panel_5.add(lblNewLabel_10);
		panel_6.setBackground(new Color(0, 128, 255));
		
		panel_box_inf.add(panel_6);
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addComponent(lblNewLabel)
					.addGap(33)
					.addComponent(textField_nombre_Inf, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
					.addGap(38))
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGap(11)
							.addComponent(textField_nombre_Inf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_6.setLayout(gl_panel_6);
		panel_10.setBackground(new Color(0, 128, 255));
		
		panel_box_inf.add(panel_10);
		
		
		
		comboBox_Rol_Inf = new JComboBox<String>();		
		Roles.inicializarComboBoxDeRoles(comboBox_Rol_Inf );  ///++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		JLabel label_Contrasea2 = new JLabel("Contrase\u00F1a");
		
		textField_Contraseña_Inf = new JTextField();
		textField_Contraseña_Inf.setColumns(10);
		
		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10.setHorizontalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_10.createSequentialGroup()
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addGap(43)
					.addComponent(comboBox_Rol_Inf, 0, 155, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label_Contrasea2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_Contraseña_Inf, GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_10.setVerticalGroup(
			gl_panel_10.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_10.createSequentialGroup()
					.addGroup(gl_panel_10.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1)
						.addGroup(gl_panel_10.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_10.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBox_Rol_Inf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_Contraseña_Inf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(label_Contrasea2))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_10.setLayout(gl_panel_10);
		panel_11.setBackground(new Color(0, 128, 255));
		
		panel_box_inf.add(panel_11);
		//comboBox.addItem(nombre);
		
		GroupLayout gl_panel_11 = new GroupLayout(panel_11);
		gl_panel_11.setHorizontalGroup(
			gl_panel_11.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_11.createSequentialGroup()
					.addComponent(lblNewLabel_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_departamento_Inf, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
					.addGap(37))
		);
		gl_panel_11.setVerticalGroup(
			gl_panel_11.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_11.createSequentialGroup()
					.addGroup(gl_panel_11.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_2)
						.addGroup(gl_panel_11.createSequentialGroup()
							.addGap(11)
							.addComponent(textField_departamento_Inf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_11.setLayout(gl_panel_11);
		panel_14.setBackground(new Color(0, 128, 255));
		
		panel_box_inf.add(panel_14);
		GroupLayout gl_panel_14 = new GroupLayout(panel_14);
		gl_panel_14.setHorizontalGroup(
			gl_panel_14.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_14.createSequentialGroup()
					.addComponent(lblNewLabel_8)
					.addGap(51)
					.addComponent(textField_eMail_Inf, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
					.addGap(36))
		);
		gl_panel_14.setVerticalGroup(
			gl_panel_14.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_14.createSequentialGroup()
					.addGroup(gl_panel_14.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_8)
						.addGroup(gl_panel_14.createSequentialGroup()
							.addGap(10)
							.addComponent(textField_eMail_Inf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		panel_14.setLayout(gl_panel_14);
		panel_12.setBackground(new Color(0, 128, 192));		
		panel_box_inf.add(panel_12);
				
		botonModificarPerfilCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				manejador_BotonModificarPerfilCSV();//+++++++++++++++++
				//cargarListaPerfiles();
				
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
		
		recargar_jList_PerfilesBBDD() ;//++++++++++++++++++++++++
	}/// FIN inicializarGUI()///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	

	
	public void manejadorBorrarListaCSV() {

		modelo_jList_PerfilesCSV.clear();		
			
	}// Fin manejadorBorrarListaCSV()
	
	
	/* Elimina el perfil seleccionado de la lista de perfiles cargados de archivos CSV */
	public void manejador_BotonEliminarPerfilCSV() {
		

		
		if(perfilSeleccionadoCSV == null ){ return;    }
		modelo_jList_PerfilesCSV.removeElement(perfilSeleccionadoCSV);
		limpiarCampos_Inferiores ();
		jList_PerfilesCSV.clearSelection();		
		
	
	}// Fin botonEliminarPerfilCSV()
	
	
	
	/* Modifica el perfil seleccionado de la lista de perfiles cargados de archivos CSV segun los campos inferiores */
	public void manejador_BotonModificarPerfilCSV() {

		 String nombre = textField_nombre_Inf.getText();
		 String rol =(String) comboBox_Rol_Inf.getSelectedItem();		  
		  String departamento =  textField_departamento_Inf.getText();
		 String email = textField_eMail_Inf.getText();
		 String contraseña =   textField_Contraseña_Inf.getText();
	 
		 Perfil nuevoPerfil = new Perfil( nombre , rol , departamento , email, contraseña) ;

		 boolean perfilCorrecto = Utilidades.limtiarYComprobarPerfilConContraseña( nuevoPerfil , this);
		 if(! perfilCorrecto ) { return; }
 
			perfilSeleccionadoCSV.setNombre(nuevoPerfil.getNombre());
			perfilSeleccionadoCSV.setRol(nuevoPerfil.getRol());
			perfilSeleccionadoCSV.setDepartamento(nuevoPerfil.getDepartamento());
			perfilSeleccionadoCSV.setEmail(nuevoPerfil.getEmail());
			perfilSeleccionadoCSV.setContraseña(nuevoPerfil.getContraseña());
		 
				limpiarCampos_Inferiores ();
				jList_PerfilesCSV.clearSelection();
	
	}// Fin manejador_BotonModificarPerfilCSV()
	
	

	public void manejador_boton_GuardarListaCSVEnBBDD() {
		
		for (Object perfil: modelo_jList_PerfilesCSV.toArray()) {
		    //Object perfil = modelo_jList_PerfilesCSV.getElementAt(i);
		    System.out.println(perfil);
		    
			 boolean perfilCorrecto = Utilidades.limtiarYComprobarPerfilConContraseña( (Perfil) perfil , this);
			 if(! perfilCorrecto ) { continue; }
		    
		    boolean exitoAlGuardar = accesoBBDD.guardarPerfil((Perfil) perfil, this) ;
		    if(exitoAlGuardar) { modelo_jList_PerfilesCSV.removeElement(perfil);}
		    
		}
		recargar_jList_PerfilesBBDD() ;
		
	}//  Fin manejador_boton_GuardarListaCSVEnBBDD()
	
	public void manejador_boton_CargarCSV() {
		

		JFileChooser selectorArchivos = new JFileChooser();
		selectorArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filtroDeArchivo = new FileNameExtensionFilter( "Solo CSVs","csv");
        selectorArchivos.setFileFilter(filtroDeArchivo);

		// int resultado =
        int opcionElegida = selectorArchivos.showOpenDialog(this);
        if (!(opcionElegida == JFileChooser.APPROVE_OPTION)) { return;  }
        
		File csv_File = selectorArchivos.getSelectedFile();
		if(csv_File == null) { return; } //por si no se selecciona nada con JFileChooser
		
		List<Perfil>  perfiles_List = CSV.extraerCSV(csv_File ,(Character)comboBox.getSelectedItem(), this);
		
		Utilidades.cargarListaDePerfilesEnJList(perfiles_List, modelo_jList_PerfilesCSV);
		
	
	}

	  
	public void manejadorSeleccion_jList_PerfilesCSV() {
		  perfilSeleccionadoCSV = (Perfil) jList_PerfilesCSV.getSelectedValue();
		   if(perfilSeleccionadoCSV == null ) {return;  } // Si no puede dar error
		//perfilSeleccionadoJListBBDDCopia = perfilSeleccionado.crearCopia() ;
		 
		 textField_nombre_Inf.setText(perfilSeleccionadoCSV.getNombre());
		 comboBox_Rol_Inf.setSelectedItem(perfilSeleccionadoCSV.getRol());		 
		 textField_departamento_Inf.setText(perfilSeleccionadoCSV.getDepartamento());
		 textField_eMail_Inf.setText(  perfilSeleccionadoCSV.getEmail() );
		 textField_Contraseña_Inf.setText(  perfilSeleccionadoCSV.getContraseña() );
	   }
	
	public void manejadorSeleccion_jList_PerfilesBBDD() {
		   Perfil perfilSeleccionado = (Perfil) jList_PerfilesBBDD.getSelectedValue();
		   if(perfilSeleccionado == null ) {return;  } // Si no puede dar error
		perfilSeleccionadoJListBBDDCopia = perfilSeleccionado.crearCopia() ;
		 
		 textField_nombre_Sup.setText(perfilSeleccionadoJListBBDDCopia.getNombre());
		 comboBox_Rol_Sup.setSelectedItem(perfilSeleccionadoJListBBDDCopia.getRol());		 
		 //textField_rol.setText(   perfilSeleccionado.getRol() );
		 textField_departamento_Sup.setText(perfilSeleccionadoJListBBDDCopia.getDepartamento());
		 textField_eMail_Sup.setText(  perfilSeleccionadoJListBBDDCopia.getEmail() );
		 textField_Contraseña_Sup.setText(  perfilSeleccionadoJListBBDDCopia.getContraseña() );
	   }
	
	
	
	/** manejador_Boton_Actualizar_Perfil(): Actualiza en la BBDD los datos del perfil seleccionado en la JList cargada de la BBDD **/
	public void manejador_Boton_ActualizarPerfil() {
		   
				
		 if(perfilSeleccionadoJListBBDDCopia==null ) {return;  } // Si no puede dar error
		 System.out.println("--perfilSeleccionadoJListBBDDCopia : "  + perfilSeleccionadoJListBBDDCopia);
		 
		 perfilSeleccionadoJListBBDDCopia.setNombre(textField_nombre_Sup.getText() ); 
		   perfilSeleccionadoJListBBDDCopia.setRol((String) comboBox_Rol_Sup.getSelectedItem() ); 
		 perfilSeleccionadoJListBBDDCopia.setDepartamento(textField_departamento_Sup.getText() );
		  perfilSeleccionadoJListBBDDCopia.setEmail(textField_eMail_Sup.getText() );  
		  perfilSeleccionadoJListBBDDCopia.setContraseña(textField_Contraseña_Sup.getText() ); 
		  
		  
		  
			 boolean perfilCorrecto = Utilidades.limtiarYComprobarPerfil( perfilSeleccionadoJListBBDDCopia , this);
			 if(! perfilCorrecto ) { return; }
			 boolean exitoActualizar =  accesoBBDD.actualizarPerfil(perfilSeleccionadoJListBBDDCopia , this );
			 if(exitoActualizar ) {
					recargar_jList_PerfilesBBDD();
					limpiarCampos_Superiores() ;			 			 
			 }
	   } // Fin manejador_Boton_Actualizar_Perfil() 


	/* borrarPerfil(): Borra el perfil seleccionado en la JList cargada de desde la BBDD segun el valor del atributo id. */
	
	public void manejador_BotonBorrarPerfil() {
		
		// Perfil perfilSeleccionado =  (Perfil) jList_Perfiles.getSelectedValue();
		 if(perfilSeleccionadoJListBBDDCopia==null ) {return;  } // Si no puede dar error cuando se ha recargado la lista.
		 
		UUID id = perfilSeleccionadoJListBBDDCopia.getId();
		 
		accesoBBDD.borrarPerfil(id , this)  ;
		System.out.println("--Borrado usuario con ID: " + id);
	
		recargar_jList_PerfilesBBDD() ;
		//limpiarCampos_Superiores();
}

	
	//  Fin manejadores de eventos -------------------------------------------------------------------------------------------------------------------------------------------------------
	
	
	
	/* cargarListaPerfiles():Refresca la lista de perfiles. Carga todos los registros de la tabla "perfiles"
	 * (devueltos por el metodo accesoBBDD.obtenerTodosLosPerfiles()) en la JList jList_Perfiles despues de limpiarla.  */
	public void recargar_jList_PerfilesBBDD() {

		List<Perfil> list_Perfiles = accesoBBDD.obtenerTodosLosPerfiles() ;
		modelo_jList_PerfilesBBDD.clear();
		Utilidades.cargarListaDePerfilesEnJList(list_Perfiles, modelo_jList_PerfilesBBDD);
		limpiarCampos_Superiores();
				
	}////Fin cargarListaPerfiles() 
	
	

	/* Limpia los campos de texto del panel superior*/
	private void limpiarCampos_Superiores() {
		
		textField_nombre_Sup.setText("");
		comboBox_Rol_Sup.setSelectedItem(Roles.roles[0]);
		 textField_departamento_Sup.setText("");
		  textField_eMail_Sup.setText("");
		textField_Contraseña_Sup.setText("");
		
	}///Fin limpiarCampos_Superiores ()
	
	/* Limpia los campos de texto del panel inferior*/
	private void limpiarCampos_Inferiores () {
		
		textField_nombre_Inf.setText("");
		comboBox_Rol_Inf.setSelectedItem(Roles.roles[0]);
		 textField_departamento_Inf.setText("");
		  textField_eMail_Inf.setText("");
		textField_Contraseña_Inf.setText("");
		
	}///Fin limpiarCampos_Inferiores 	
}//////Fin de clase----------------------
