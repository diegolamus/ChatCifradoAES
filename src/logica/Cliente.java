package logica;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Cliente {

	// Socket
	private static ObjectOutputStream salida;
	private ObjectInputStream entrada;
	private Socket cliente;

	public Cliente() {
		ejecutarCliente();
	}

	private void ejecutarCliente() {
		try {
			//Iniciar socket cliente
			cliente = new Socket("localhost", 1234);
			

			obtenerFlujos(); // Paso 2: establecer los flujos de entrada y salida
			procesarConexion(); // Paso 3: procesar la conexión
		}
		// el servidor cerró la conexión
		catch (EOFException excepcionEOF) {
			System.err.println("El cliente termino la conexion");
		}
		// procesar los problemas que pueden ocurrir al comunicarse con el servidor
		catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		} finally {
			cerrarConexion(); // Paso 4: cerrar la conexión
		}
	}

	private void conectarAServidor() throws IOException {

	}

	private void obtenerFlujos() throws IOException {
		salida = new ObjectOutputStream(cliente.getOutputStream());// establecer flujo de salida para los objetos
		salida.flush(); // vacíar búfer de salida para enviar información de encabezado
		entrada = new ObjectInputStream(cliente.getInputStream()); // establecer flujo de entrada para los objetos
		System.out.println("\nSe establecieron los flujos de E/S\n");
	}

	private void procesarConexion() throws IOException {
		String mensaje = "Este es el cliente";
		enviarDatos(mensaje);

		do {

			String m = JOptionPane.showInputDialog("Ingrese el mensaje: ");
			enviarDatos(m);

		} while (JOptionPane.showConfirmDialog(null, "Enviar otro mensaje") == JOptionPane.YES_OPTION);

	} // fin del m�todo procesarConexion

	private void cerrarConexion() {
		System.out.println("\nCerrando conexión");

		try {
			salida.close();
			entrada.close();
			cliente.close();
			System.exit(0);
		} catch (IOException excepcionES) {
			excepcionES.printStackTrace();
		}
	}

	private void enviarDatos(String mensaje) {
		// enviar objeto al servidor
		try {
			salida.writeObject("CLIENTE>>> " + mensaje);
			salida.flush();
			// System.out.println( "\nCLIENTE>>> " + mensaje );
		}

		// procesar los problemas que pueden ocurrir al enviar el objeto
		catch (IOException excepcionES) {
			System.out.println("\nError al escribir el objeto");
		}
	}
}
