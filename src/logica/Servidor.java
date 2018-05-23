package logica;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import interfaz.VentanaChat;

public class Servidor implements Runnable {

	public static final int port = 1234;
	public static final int backlog = 100;
	public static final String TERMINAR = "Terminar";

	private ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private ServerSocket servidor;
	private Socket conexion;

	private VentanaChat chat;

	public Servidor(VentanaChat chat) {
		this.chat = chat;
		try {
			servidor = new ServerSocket(port, backlog);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void esperarConexion() throws IOException {
		mostrarMensaje("Esperando al clinte");
		conexion = servidor.accept();
	}

	private void obtenerFlujos() throws IOException {
		salida = new ObjectOutputStream(conexion.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(conexion.getInputStream());
	}

	private void procesarConexion() throws IOException {
		String mensaje = "Conexion exitosa con:" + conexion.getInetAddress().getHostName();
		enviarDatos(mensaje);
		do {
			try {
				mensaje = (String) entrada.readObject();
				mostrarMensaje("\n" + mensaje);
			} catch (ClassNotFoundException excepcionClaseNoEncontrada) {

			}
		} while (!mensaje.equals("CLIENTE ->" + TERMINAR));
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

	@Override
	public void run() {
		try {
			while (true) {
				try {
					esperarConexion();
					obtenerFlujos();
					procesarConexion();
				} catch (EOFException excepcionEOF) {
					mostrarMensaje("El cliente termino la conexion");
				} finally {
					cerrarConexion();
				}
			}
		} catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		}

	}

}
