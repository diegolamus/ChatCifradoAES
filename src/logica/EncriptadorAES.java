package logica;

import java.security.KeyPair;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncriptadorAES {

	private static final String ALGORITMODECIFRADO = "AES";
	
		/**
	     * Encripta el texto escrito
	     *
	     * @param textoPlano El texto a encriptar
	     * @return mensaje encriptado
	     */
	    public static String encriptar(byte[] textoPlano, byte[] llaveSecreta)  throws Exception
	    {
	    	
	        SecretKeySpec SsecretKey = new SecretKeySpec(llaveSecreta, ALGORITMODECIFRADO);
	        Cipher cipher = Cipher.getInstance(ALGORITMODECIFRADO);
	        cipher.init(Cipher.ENCRYPT_MODE, SsecretKey);

	        return new String(cipher.doFinal(textoPlano));
	    }
	    
	    /**
	     * Desencripta el mensaje recibido.
	     *
	     * @param mensajeCifrado El mensaje que ha sido cifrado con anterioridad.
	     * @return retorna el mensaje descifrado, es decir, el original en texto plano.
	     */
	    public static String desencriptar(byte[] mensajeCifrado, byte[] llaveSecreta) throws Exception
	    {
	        SecretKeySpec SsecretKey = new SecretKeySpec(llaveSecreta, ALGORITMODECIFRADO);
	        Cipher cipher = Cipher.getInstance(ALGORITMODECIFRADO);
	        cipher.init(Cipher.DECRYPT_MODE, SsecretKey);
	        return new String(cipher.doFinal(mensajeCifrado));
	    }
	    
	    public static void main(String[] args) {
			String texto = "hola";
			KeyPair keys = DiffieHellman.generarKeys();
			KeyPair keys2 = DiffieHellman.generarKeys();
			byte[] secret = DiffieHellman.generarClaveSecretaComun(keys.getPrivate(), keys2.getPublic());
			String salida = "";
			try {
				String enc = encriptar(texto.getBytes(), secret);
				salida = EncriptadorAES.desencriptar(enc.getBytes(), secret);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(salida);
			
		}
}
