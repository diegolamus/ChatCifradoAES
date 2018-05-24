package logica;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import interfaz.VentanaChat;

public class Servidor implements Runnable {

	public static final String PUBLIC_KEY_LABEL = "PUBLIC_KEY_SHARE: ";
	public static final int port = 1234;
	public static final int backlog = 100;
	public static final String TERMINAR = "terminar";

	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private ServerSocket servidor;
	private Socket conexion;

	private VentanaChat chat;

	private PrivateKey privateKey;
	private PublicKey publicKey;
	private byte[] secret;

	public Servidor(VentanaChat chat) {
		this.chat = chat;
		try {
			servidor = new ServerSocket(port, backlog);
		} catch (IOException e) {
			e.printStackTrace();
		}
		KeyPair keys = DiffieHellman.generarKeys();
		privateKey = keys.getPrivate();
		publicKey = keys.getPublic();
	}

	private void esperarConexion() throws IOException {
		mostrarMensaje("Esperando al clinte\n");
		conexion = servidor.accept();
	}

	private void obtenerFlujos() throws IOException {
		salida = new ObjectOutputStream(conexion.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(conexion.getInputStream());
	}

	private void procesarConexion() throws IOException {
		String mensaje = "Conexion exitosa con:" + conexion.getInetAddress().getHostName() + "\n";
		enviarDatos(mensaje);
		enviarDatos(PUBLIC_KEY_LABEL + publicKey);//TODO pasar clave publica en string
		do {
			try {
				mensaje = (String) entrada.readObject();
				if (mensaje.contains(PUBLIC_KEY_LABEL)) {
					secret = DiffieHellman.generarClaveSecretaComun(privateKey, mensaje.split(" ")[1]);//TODO pasar striung de clave publica en publicKey
					System.out.println(secret);
				} else {
					mostrarMensaje(EncriptadorAES.desencriptar(mensaje.getBytes(), secret));
				}
			} catch (ClassNotFoundException excepcionClaseNoEncontrada) {
				excepcionClaseNoEncontrada.printStackTrace();
			}
		} while (!mensaje.equalsIgnoreCase("CLIENTE ->" + TERMINAR));
	}

	private void cerrarConexion() {
		try {
			salida.close();
			entrada.close();
			conexion.close();
		} catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		}
	}

	public void enviarDatos(String mensaje) {
		String mensajeParaEnviar = "CLIENTE ->  " + mensaje+ "\n";
		try {
			String mensajeEncriptado = EncriptadorAES.encriptar(mensajeParaEnviar.getBytes(), secret);
			salida.writeObject(mensajeEncriptado);
			salida.flush();
			mostrarMensaje(mensajeParaEnviar);
		} catch (Exception e) {
			mostrarMensaje("Error al enviar mensaje, verifique que el cliente se encuentre conectado.\n");
		}
	}

	public void mostrarMensaje(String mensaje) {
		chat.mostrarMensaje(mensaje);
	}

	@Override
	public void run() {
		try {
			while (true) {
				try {
					esperarConexion();
					obtenerFlujos();
					procesarConexion();
				} catch (EOFException excepcionEOF) {
					mostrarMensaje("El cliente termino la conexion\n\n");
				} finally {
					cerrarConexion();
				}
			}
		} catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		}

	}

}
