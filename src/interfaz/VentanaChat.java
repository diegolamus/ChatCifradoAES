package interfaz;

import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import logica.Cliente;
import logica.Servidor;

public class VentanaChat extends JFrame {

	private static final long serialVersionUID = -5070643191804052892L;
	
	// Cliente y servidor
	private Cliente cliente;
	private Servidor servidor;
	
	// Conexion
	private JTextField direccionIP;
	private JTextField puerto;
	private JButton btnConectar;
	private JButton iniciarServidor;

	// Mensajes
	private JTextArea textArea;
	private JTextField mensaje;
	private JButton btnEnviar;

	public VentanaChat() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(500, 500));

		// Agregar campo para direccion IP
		JLabel dirIP = new JLabel("Dirección IP destino:");
		dirIP.setBounds(10, 10, 150, 30);
		this.add(dirIP);
		direccionIP = new JTextField();
		direccionIP.setBounds(190, 10, 200, 25);
		this.add(direccionIP);

		// Agregar campo para puerto
		JLabel port = new JLabel("Puerto:");
		port.setBounds(10, 40, 150, 30);
		this.add(port);
		puerto = new JTextField();
		puerto.setBounds(190, 40, 100, 25);
		this.add(puerto);

		// agregar boton conectar
		btnConectar = new JButton("Conectar");
		btnConectar.setBounds(10, 70, 150, 25);
		btnConectar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.add(btnConectar);
		
		// agregar boton conectar
		iniciarServidor = new JButton("Iniciar Servidor");
		iniciarServidor.setBounds(170, 70, 150, 25);
		iniciarServidor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnConectar.setEnabled(false);
				servidor = new Servidor(getChat());
				//servidor.ejecutarServidor();
			}
		});
		this.add(iniciarServidor);

		// Agregar campo mensaje
		mensaje = new JTextField();
		mensaje.setBounds(10, 380, 360, 25);
		this.add(mensaje);

		// Agregar boton enviar
		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(390, 380, 100, 25);
		btnEnviar.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.add(btnEnviar);
		this.pack();
		
		//Agregar text area
		textArea = new JTextArea();
		JScrollPane pane = new JScrollPane(textArea);
		pane.setBounds(10,120,480,250);
		this.add(pane);

	}
	
	private VentanaChat getChat() {
		return this;
	}
	
	public void mostrarMensaje(String mensaje) {
		
	}

	public static void main(String[] args) {
		VentanaChat chat = new VentanaChat();
		chat.setVisible(true);
	}
}
