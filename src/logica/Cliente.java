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

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

import interfaz.VentanaChat;

public class Cliente implements Runnable {

	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private String IPservidor;
	private Socket cliente;
	private VentanaChat chat;
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private SecretKey secret;
	private KeyAgreement agreement; 
	

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
		byte[] mensaje=null;
		Object recibido=null;
		String mensajeDec ="";

			do {
				try {
					recibido = entrada.readObject();
					mensaje = (byte[]) recibido;
					mensajeDec = EncriptadorAES.desencriptar(mensaje, secret.getEncoded());
					mostrarMensaje(mensajeDec);
				} catch (ClassNotFoundException excepcionClaseNoEncontrada) {
					excepcionClaseNoEncontrada.printStackTrace();
				} 
				catch(ClassCastException e) {
					KeyPair keys = DiffieHellman.generarKeys(((PublicKey)recibido).getEncoded());
					privateKey = keys.getPrivate();
					publicKey = keys.getPublic();
					agreement = DiffieHellman.generateKeyAgreement(keys);
					secret= DiffieHellman.generarClaveSecretaComun(agreement,privateKey, ((PublicKey)recibido).getEncoded());
					mostrarMensaje("HASHCODE CLAVE SECRETA: "+secret.hashCode()+"\n");
					enviarClavePublica(publicKey);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			} while (!mensajeDec.equalsIgnoreCase("SERVIDOR -> " + Servidor.TERMINAR));
		
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

	public void enviarClavePublica(PublicKey key) {
		try {
			mostrarMensaje("Enviado clave pública a servidor\n");
			salida.writeObject(key);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void enviarDatos(String mensaje) {
		String mensajeParaEnviar = "CLIENTE ->  " + mensaje+ "\n";
		try {
			byte[] mensajeEncriptado = EncriptadorAES.encriptar(mensajeParaEnviar.getBytes(), secret.getEncoded());
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
