package logica;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
	   private ObjectOutputStream salida;
	   private ObjectInputStream entrada;
	   private String mensaje = "";
	   private String servidorChat;
	   private Socket cliente;
	
	
	
	public Cliente (String direccionIp) {
		servidorChat=direccionIp;
		
		 try {
	         conectarAServidor(); // Paso 1: crear un socket para realizar la conexión
	         obtenerFlujos();      // Paso 2: obtener los flujos de entrada y salida
	         procesarConexion(); // Paso 3: procesar la conexión
	      }
	      // el servidor cerró la conexión
	      catch ( EOFException excepcionEOF ) {
	         System.err.println( "El cliente termino la conexión" );
	      }
	      // procesar los problemas que pueden ocurrir al comunicarse con el servidor
	      catch ( IOException excepcionES ) {
	         excepcionES.printStackTrace();
	      }

	      finally {
	         cerrarConexion(); // Paso 4: cerrar la conexión
	      }
	}
	
	
	public void conectarAServidor() throws UnknownHostException, IOException {
	      cliente = new Socket(InetAddress.getByName( servidorChat ), 12345/*"localhost", 80*/);      

	}
	
	
	/**
	 * obtiene los flujos traidos de la coneccion
	 * @throws IOException
	 */
	public void obtenerFlujos() throws IOException {
		salida = new ObjectOutputStream(cliente.getOutputStream());
		salida.flush();
		entrada = new ObjectInputStream(cliente.getInputStream());
		
	}
	
	public void procesarConexion() {
		
	}
	
	public void cerrarConexion() { 
	      try {
	         salida.close();
	         entrada.close();
	         cliente.close();
	      }
	      catch( IOException excepcionES ) {
	         excepcionES.printStackTrace();
	      }
	}
	

}
