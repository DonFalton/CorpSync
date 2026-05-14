package corpsync.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {


	private JFrame ventanaAbierta;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
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
	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 436, 395);
		
		
		inicializarGUI() ;

	}
	
	
	private void inicializarGUI() {
		
		JPanel contentPane;		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Menu principal");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 19));
		
		JButton boton_AdministrarPerfilesEnBBDD = new JButton("Administrar perfiles en BBDD");
		boton_AdministrarPerfilesEnBBDD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				abrirVentanaAdministrarPerfiles();
			}
		});
		boton_AdministrarPerfilesEnBBDD.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnNewButton_1 = new JButton("Cargar perfiles dede CSV a la BBDD");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirVentanaCargarCSVaBBDD();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnNewButton_2 = new JButton("Crear CSV");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirVentanaCrearCSV ();
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnNewButton_3 = new JButton("Crear CSV desde BBDD");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirVentanaCrearCSVDedeBBDD();			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(121)
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
					.addGap(113))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(79)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnNewButton_3, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
						.addComponent(btnNewButton_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
						.addComponent(btnNewButton_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(boton_AdministrarPerfilesEnBBDD, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
					.addGap(85))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(66)
					.addComponent(boton_AdministrarPerfilesEnBBDD)
					.addGap(18)
					.addComponent(btnNewButton_1)
					.addGap(18)
					.addComponent(btnNewButton_2)
					.addGap(18)
					.addComponent(btnNewButton_3)
					.addContainerGap(84, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
	}
	
	
	public void abrirVentanaAdministrarPerfiles() {
		
		//if(!(ventanaAbierta == null)) {return;}
		JFrame ventanaAbierta = new AdministrarPerfiles(this);
		ventanaAbierta.setVisible(true);
		setVisible(false);
		
	}
	
	
	public void abrirVentanaCargarCSVaBBDD() {
		
		JFrame ventanaAbierta = new  CargarCSVaBBDD(this);
		ventanaAbierta.setVisible(true);
		setVisible(false);

	}
	
	public void abrirVentanaCrearCSV () {
		
		
		JFrame ventanaAbierta = new CrearCSV (this);
		ventanaAbierta.setVisible(true);
		setVisible(false);
		
	}
	
	public void abrirVentanaCrearCSVDedeBBDD() {
		
		JFrame ventanaAbierta = new CrearCSVDedeBBDD (this);
		ventanaAbierta.setVisible(true);
		setVisible(false);
		
	}
	
	public void eliminarReferenciaDeVentanaAbierta() {
		
		ventanaAbierta = null;
		
	}
	
	
	
	
}
