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

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

import interfaz.VentanaChat;

public class Servidor implements Runnable {

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
	private SecretKey secret;
	private KeyAgreement agreement;

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
		agreement = DiffieHellman.generateKeyAgreement(keys);
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
		mostrarMensaje("Conexion exitosa con:" + conexion.getInetAddress().getHostName() + "\n");
		byte[] mensaje = null;
		String mensajeDec = "";
		enviarClavePublica(publicKey);
		Object recibido = null;
		do {
			try {
				recibido = entrada.readObject();
				mensaje = (byte[]) recibido;
				mensajeDec = EncriptadorAES.desencriptar(mensaje, secret.getEncoded());
				mostrarMensaje(mensajeDec);
			} catch (ClassNotFoundException excepcionClaseNoEncontrada) {
				excepcionClaseNoEncontrada.printStackTrace();
			} catch (ClassCastException e) {
				secret = DiffieHellman.generarClaveSecretaComun(agreement, privateKey,
						((PublicKey) recibido).getEncoded());
				mostrarMensaje("HASHCODE CLAVE SECRETA: " + secret.hashCode() + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (!mensajeDec.equalsIgnoreCase("CLIENTE -> " + Servidor.TERMINAR));

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

	public void enviarClavePublica(PublicKey key) {
		try {
			mostrarMensaje("Enviado clave pública al cliente\n");
			salida.writeObject(key);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void enviarDatos(String mensaje) {

		String mensajeParaEnviar = "SERVIDOR ->  " + mensaje + "\n";
		try {
			byte[] mensajeEncriptado = EncriptadorAES.encriptar(mensajeParaEnviar.getBytes(), secret.getEncoded());
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
