package logica;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import interfaz.VentanaChat;

public class Cliente implements Runnable {

	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private String IPservidor;
	private Socket cliente;
	private VentanaChat chat;
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private byte[] secret;

	public Cliente(String direccionIp, VentanaChat chat) {
		this.chat=chat;
		IPservidor = direccionIp;
		KeyPair keys = DiffieHellman.generarKeys();
		privateKey = keys.getPrivate();
		publicKey = keys.getPublic();
	}

	public void conectarAServidor() throws UnknownHostException, IOException {
		mostrarMensaje("Intentando realizar conexion\n");
		cliente = new Socket(IPservidor, Servidor.port);
	}

	public void obtenerFlujos() throws IOException {
		salida = new ObjectOutputStream(cliente.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(cliente.getInputStream());
	}

	public void procesarConexion() throws IOException {	
		String mensaje="";
		do {
			try {
				mensaje = (String) entrada.readObject();
				if(mensaje.contains(Servidor.PUBLIC_KEY_LABEL)) {
					enviarDatos(Servidor.PUBLIC_KEY_LABEL+ publicKey.toString());//TODO pasar clave publica en string
					secret = DiffieHellman.generarClaveSecretaComun(privateKey, mensaje.split(" ")[1]);//TODO pasar striung de clave publica en publicKey
					System.out.println(secret);
				}else
					mostrarMensaje(EncriptadorAES.desencriptar(mensaje.getBytes(), secret));
			} catch (ClassNotFoundException excepcionClaseNoEncontrada) {
				excepcionClaseNoEncontrada.printStackTrace();
			}
		} while (!mensaje.equalsIgnoreCase("SERVIDOR -> " + Servidor.TERMINAR));
	}

	public void cerrarConexion() {
		try {
			salida.close();
			entrada.close();
			cliente.close();
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
		} catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mostrarMensaje(String mensaje) {
		chat.mostrarMensaje(mensaje);
	}

	@Override
	public void run() {
		try {
			conectarAServidor();
			obtenerFlujos();
			procesarConexion();
			mostrarMensaje("------Conexion terminada-----");
			chat.resetear();
		}catch(UnknownHostException e) {
			mostrarMensaje("No se encontro el host\n\n" );
		}catch (EOFException excepcionEOF) {
			mostrarMensaje("El servidor termino la conexion\n\n" );
		}catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			cerrarConexion();
		}
	}

}
