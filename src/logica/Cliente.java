package logica;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
	   private ObjectOutputStream salida;
	   private ObjectInputStream entrada;
	   private String mensaje = "";
	   private String servidorChat;
	   private Socket cliente;
	
	
	
	public Cliente () {
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
	
	
	public void conectarAServidor() {
		
	}
	
	
	public void obtenerFlujos() {
		
	}
	
	public void procesarConexion() {
		
	}
	
	public void cerrarConexion() {
		
	}
	

}