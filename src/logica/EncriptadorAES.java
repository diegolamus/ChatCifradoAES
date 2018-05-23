package logica;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncriptadorAES {

	
	public static String encriptarMensaje(byte[] claveSecreta, String mensaje) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(claveSecreta, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] mensajeEncriptado = cipher.doFinal(mensaje.getBytes());
			return new String(mensajeEncriptado);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String desencriptarMensaje(byte[] claveSecreta, String mensaje) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(claveSecreta, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5no");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			String mensajeDesencriptado = new String(cipher.doFinal(mensaje.getBytes()));
			return mensajeDesencriptado;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String hola = "hola";
		byte[] clave= {3,10,8,25,26,2,3,4,5,6,7,12,34,12,3,4};
		hola = encriptarMensaje(clave, hola);
		System.out.println(hola);
		hola = desencriptarMensaje(clave, hola);
		System.out.println(hola);
	}
}
