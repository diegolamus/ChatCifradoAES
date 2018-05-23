package logica;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import interfaz.VentanaChat;

public class Servidor {

	public static final int port = 1234;
	public static final int backlog = 100;

	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private ServerSocket servidor;
	private Socket conexion;
	
	private VentanaChat chat;

	public Servidor(VentanaChat chat) {
		try {
			servidor = new ServerSocket(port, backlog);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ejecutarServidor() {
		try {
			// Paso 1: crear un objeto ServerSocket.
			while (true) {

				try {
					esperarConexion(); // Paso 2: esperar una conexión.
					obtenerFlujos(); // Paso 3: obtener flujos de entrada y salida.
					procesarConexion(); // Paso 4: procesar la conexión.
				}
				// procesar excepción EOFException cuando el cliente cierre la conexión
				catch (EOFException excepcionEOF) {
					System.err.println("El servidor terminó la conexión");
				} finally {
					cerrarConexion(); // Paso 5: cerrar la conexión.
				}
			}
		} // fin del bloque try

		// procesar problemas con E/S
		catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		}
	} 

	private void esperarConexion() throws IOException {
		conexion = servidor.accept();
	}

	private void obtenerFlujos() throws IOException {
		salida = new ObjectOutputStream(conexion.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(conexion.getInputStream());
	}

	private void procesarConexion() throws IOException {
		String mensaje = "Conexión exitosa";
		enviarDatos(mensaje);
		// establecerCampoTextoEditable( true );
		do {
			try {
				mensaje = (String) entrada.readObject();
				mostrarMensaje("\n" + mensaje);
			} catch (ClassNotFoundException excepcionClaseNoEncontrada) {

			}
		} while (!mensaje.equals("CLIENTE>>> TERMINAR"));
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

	private void enviarDatos(String mensaje) {
		try {
			salida.writeObject("SERVIDOR --> " + mensaje);
			salida.flush();
		} catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		}
	}
	
	public void mostrarMensaje(String mensaje) {
		chat.mostrarMensaje(mensaje);
	}

}
