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

import org.apache.commons.collections4.functors.AndPredicate;

import corpsync.controlador.AccesoADatosBBDD;
import corpsync.controlador.Perfil;
import corpsync.controlador.Roles;
import corpsync.controlador.Utilidades;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JComboBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JScrollPane;



public class AdministrarPerfiles extends JFrame {

	
	
	
	
	 private AccesoADatosBBDD accesoBBDD = new AccesoADatosBBDD(); /////////////////////////////////////////////
	 
	 
	 
		private DefaultListModel<Perfil> modelo_jList_Perfiles;
		private Perfil perfilSeleccionadoJListBBDDCopia ;
		
		//private String[] roles = new String[]{"empleado" , "tecnico" , "admin"};
	 
	 
	 


	private  JTextField textField_departamento_Sup ;
	private JTextField textField_eMail_Sup;
	private JTextField textField_nombre_Sup;	
	
	private  JTextField textField_nombre_Inf ;
	private  JTextField textField_departamento_Inf ;

	private JTextField textField_eMail_Inf ;	
	
	private JComboBox<String> comboBox_Rol_Inf;
	private JComboBox<String> comboBox_Rol_Sup;	

	private JList<Perfil> jList_Perfiles ;
	private JTextField textField_Contraseña_Sup;
	private JTextField textField_Contraseña_Inf;
	private JTextField textField_ContraseñaNueva_Sup;



	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdministrarPerfiles frame = new AdministrarPerfiles();
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
	public AdministrarPerfiles() {

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
	
	public AdministrarPerfiles(VentanaPrincipal ventanaPrincipal) {

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
		
		//++++++++++++++++++++  Componentes manipulados +++++++++++++++++++++++++++++++++++
		textField_nombre_Inf = new JTextField();
		
		
		
		textField_departamento_Inf = new JTextField();

		textField_eMail_Inf = new JTextField();
		



		jList_Perfiles = new JList<Perfil>();
		
		//++++++++++++++++++++ Fin Componentes manipulados +++++++++++++++++++++++++++++++++++

		
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
							.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_Izquierdo.setVerticalGroup(
			gl_panel_Izquierdo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_Izquierdo.createSequentialGroup()
					.addComponent(lblNewLabel_3)
					.addGap(16)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
					.addGap(8)
					.addComponent(button_Refrescar_Lista))
		);
		scrollPane.setViewportView(jList_Perfiles);
		jList_Perfiles.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				manejador_jList_Perfiles_Seleccion() ; //+++++++++++++++++++++++++++++++++
			}
		});
		
		modelo_jList_Perfiles = new DefaultListModel<Perfil>();
		jList_Perfiles.setModel(modelo_jList_Perfiles);//++++++++
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
		Roles.inicializarComboBoxDeRoles(comboBox_Rol_Sup) ;
		
		JLabel label_Contrasea1 = new JLabel("Contrase\u00F1a");
		//Por seguridad el campo contraseña y su etiqueta, de momento, se oculta. Se deja por posibles cambios.
		label_Contrasea1.setVisible(false);
		textField_Contraseña_Sup = new JTextField();
		textField_Contraseña_Sup.setEditable(false);
		textField_Contraseña_Sup.setColumns(10);
		 //Por seguridad este campo, de momento, se oculta. Se deja por posibles cambios.
		textField_Contraseña_Sup.setVisible(false);
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addComponent(comboBox_Rol_Sup, 0, 240, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label_Contrasea1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_Contraseña_Sup, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
					.addGap(48))
		);
		gl_panel_7.setVerticalGroup(
			gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_5)
						.addGroup(gl_panel_7.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBox_Rol_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_Contraseña_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(label_Contrasea1))
					.addContainerGap(16, Short.MAX_VALUE))
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
		
		textField_ContraseñaNueva_Sup = new JTextField();
		textField_ContraseñaNueva_Sup.setColumns(10);
		
		JButton boton_CambiarContraseña = new JButton("Cambiar Contrase\u00F1a");
		boton_CambiarContraseña.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manejador_BotonCambiarContraseña();
			}
		});
		GroupLayout gl_panel_13 = new GroupLayout(panel_13);
		gl_panel_13.setHorizontalGroup(
			gl_panel_13.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_13.createSequentialGroup()
					.addGap(2)
					.addComponent(boton_BorrarPerfil)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(boton_ActualizarPerfil)
					.addPreferredGap(ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
					.addComponent(lblNewLabel_12)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_ContraseñaNueva_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(boton_CambiarContraseña)
					.addGap(6))
		);
		gl_panel_13.setVerticalGroup(
			gl_panel_13.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_13.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panel_13.createParallelGroup(Alignment.BASELINE)
						.addComponent(boton_BorrarPerfil)
						.addComponent(boton_ActualizarPerfil)
						.addComponent(textField_ContraseñaNueva_Sup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_12)
						.addComponent(boton_CambiarContraseña)))
		);
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
		panel_10.setBackground(new Color(253, 197, 66));
		
		panel_box_inf.add(panel_10);
		
		
		
		comboBox_Rol_Inf = new JComboBox<String>();		
		Roles.inicializarComboBoxDeRoles(comboBox_Rol_Inf );  ///++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		JLabel label_Contrasea2 = new JLabel("Contrase\u00F1a");
		
		textField_Contraseña_Inf = new JTextField();
		textField_Contraseña_Inf.setColumns(15);
		
		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10.setHorizontalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_10.createSequentialGroup()
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addGap(43)
					.addComponent(comboBox_Rol_Inf, 0, 240, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label_Contrasea2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_Contraseña_Inf, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addGap(38))
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
					.addContainerGap(16, Short.MAX_VALUE))
		);
		panel_10.setLayout(gl_panel_10);
		panel_11.setBackground(new Color(253, 197, 66));
		
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
		panel_14.setBackground(new Color(253, 197, 66));
		
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
		panel_12.setBackground(new Color(198, 159, 4));		
		panel_box_inf.add(panel_12);
				
		buttonGuardarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				manejador_GuardarPerfil();//+++++++++++++++++
				//cargarListaPerfiles();
				
			}
		});
		
		panel_12.add(buttonGuardarPerfil);
		
		recargar_jList_PerfilesBBDD() ;//++++++++++++++++++++++++
	}/// FIN inicializarGUI()//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void manejador_BotonCambiarContraseña() {
		
		// Perfil perfilSeleccionado =  (Perfil) jList_Perfiles.getSelectedValue();
		 if(perfilSeleccionadoJListBBDDCopia==null ) {return;  } // Si no puede dar error cuando se ha recargado la lista.
		 
		UUID id = perfilSeleccionadoJListBBDDCopia.getId();
		// Comprobacion de contraseña y lipiado espacios de campos
		String contraseña = textField_ContraseñaNueva_Sup.getText().strip();
		if(contraseña.equals("")) {  
			JOptionPane.showMessageDialog(this,  "En el pefil con nombre:  " + perfilSeleccionadoJListBBDDCopia.getNombre() + ", el campo contraseña no puede estar vacio");
			return;
		}

		perfilSeleccionadoJListBBDDCopia.setContraseña(contraseña);

		boolean exitoBorrar =  accesoBBDD.borrarPerfil(id , this)  ;
		 if(! exitoBorrar ) { return; }
		 perfilSeleccionadoJListBBDDCopia.setContraseña(textField_ContraseñaNueva_Sup.getText());
		 boolean exitoGuardar =  accesoBBDD.guardarPerfil(perfilSeleccionadoJListBBDDCopia, this);
		 if(exitoGuardar ) {
				recargar_jList_PerfilesBBDD();
				System.out.println("--Cambiada contraseña de perfil: " + perfilSeleccionadoJListBBDDCopia);						 			 
		 }
		}// Fin manejador_BotonCambiarContraseña()
	
	
	
	public void manejador_jList_Perfiles_Seleccion() {
		   Perfil perfilSeleccionado = (Perfil) jList_Perfiles.getSelectedValue();
		   if(perfilSeleccionado == null ) {return;  } // Si no puede dar error
		perfilSeleccionadoJListBBDDCopia = perfilSeleccionado.crearCopia() ;
		 
		 textField_nombre_Sup.setText(perfilSeleccionadoJListBBDDCopia.getNombre());
		 comboBox_Rol_Sup.setSelectedItem(perfilSeleccionadoJListBBDDCopia.getRol());		 
		 textField_departamento_Sup.setText(perfilSeleccionadoJListBBDDCopia.getDepartamento());
		 textField_eMail_Sup.setText(  perfilSeleccionadoJListBBDDCopia.getEmail() );
		 textField_Contraseña_Sup.setText(  perfilSeleccionadoJListBBDDCopia.getContraseña() );
	   }


	/** manejador_Boton_Actualizar_Perfil(): Actualiza en la BBDD los datos del perfil seleccionado en la JList cargada de la BBDD **/
	public void manejador_Boton_Actualizar_Perfil() {
		   
				
		 if(perfilSeleccionadoJListBBDDCopia==null ) {return;  } // Si no puede dar error
		 System.out.println("----"  + perfilSeleccionadoJListBBDDCopia);
		 
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
		limpiarCampos_Superiores();
}

	
	/* borrarPerfil(): Borra un perfil segun el valor del campo de texto textField_eMail, 
	 * el cual se usa para ojear los correos de los registros en la BBDD cargados en la JList jList_Perfiles. */
	/*
	public void manejador_BotonBorrarPerfil_Por EMail() {
		
		String eMail = textField_eMail.getText();
		System.out.println("------EMail====" + eMail);
		if (eMail != "") { accesoBBDD.borrarPerfilPorEMail(eMail) ; 	}
		cargarListaPerfiles() ;
}*/

		

		
	public void manejador_GuardarPerfil() {
		
		 String nombre = textField_nombre_Inf.getText();
		 String rol =(String) comboBox_Rol_Inf.getSelectedItem();		  
		  String departamento =  textField_departamento_Inf.getText();
		 String email = textField_eMail_Inf.getText();
		 String contraseña =   textField_Contraseña_Inf.getText();

	 
		 Perfil nuevoPerfil = new Perfil( nombre , rol , departamento , email, contraseña) ;
		 
		 boolean perfilCorrecto = Utilidades.limtiarYComprobarPerfilConContraseña( nuevoPerfil , this);
		 if(! perfilCorrecto ) { return; }
		 boolean exitoGuardar =  accesoBBDD.guardarPerfil(nuevoPerfil, this);
		 if(exitoGuardar ) {
				recargar_jList_PerfilesBBDD();
				limpiarCampos_Inferiores ();
				System.out.println("--Guardado el perfil: " + nuevoPerfil);		 			 
		 }

	}// Fin manejador_GuardarPerfil()
	
	
	
	/* cargarListaPerfiles():Refresca la lista de perfiles. Carga todos los registros de la tabla "perfiles"
	 * (devueltos por el metodo accesoBBDD.obtenerTodosLosPerfiles()) en la JList jList_Perfiles despues de limpiarla.  */
	public void recargar_jList_PerfilesBBDD() {

		List<Perfil> list_Perfiles = accesoBBDD.obtenerTodosLosPerfiles() ;
		modelo_jList_Perfiles.clear();
		limpiarCampos_Superiores();
		Utilidades.cargarListaDePerfilesEnJList(list_Perfiles, modelo_jList_Perfiles);
				
	}////Fin cargarListaPerfiles() 
	
	

	/* Limpia los campos de texto del panel inferior*/
	private void limpiarCampos_Inferiores() {
		
		textField_nombre_Inf.setText("");
		comboBox_Rol_Inf.setSelectedItem(Roles.roles[0]);
		 textField_departamento_Inf.setText("");
		  textField_eMail_Inf.setText("");
		textField_Contraseña_Inf.setText("");
		
	}///Fin limpiarCampos_Inferiores 	
	
	
	/* Limpia los campos de texto del panel superior*/
	private void limpiarCampos_Superiores() {
		
		textField_nombre_Sup.setText("");
		comboBox_Rol_Sup.setSelectedItem(Roles.roles[0]);
		 textField_departamento_Sup.setText("");
		  textField_eMail_Sup.setText("");
		textField_Contraseña_Sup.setText("");
		textField_ContraseñaNueva_Sup.setText("");
		
	}///Fin limpiarCampos_Superiores ()
}//////Fin de clase----------------------
