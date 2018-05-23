package logica;

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
	    public byte[] encriptar(byte[] textoPlano, byte[] llaveSecreta)  throws Exception
	    {
	    	llaveSecreta = new byte [16];
	        SecretKeySpec SsecretKey = new SecretKeySpec(llaveSecreta, ALGORITMODECIFRADO);
	        System.out.println(llaveSecreta.length);
	        Cipher cipher = Cipher.getInstance(ALGORITMODECIFRADO);
	        cipher.init(Cipher.ENCRYPT_MODE, SsecretKey);

	        return cipher.doFinal(textoPlano);
	    }
	    
	    /**
	     * Desencripta el mensaje recibido.
	     *
	     * @param mensajeCifrado El mensaje que ha sido cifrado con anterioridad.
	     * @return retorna el mensaje descifrado, es decir, el original en texto plano.
	     */
	    public byte[] desencriptar(byte[] mensajeCifrado, byte[] llaveSecreta) throws Exception
	    {
	        SecretKeySpec SsecretKey = new SecretKeySpec(llaveSecreta, ALGORITMODECIFRADO);
	        Cipher cipher = Cipher.getInstance(ALGORITMODECIFRADO);
	        cipher.init(Cipher.DECRYPT_MODE, SsecretKey);

	        return cipher.doFinal(mensajeCifrado);
	    }
}
