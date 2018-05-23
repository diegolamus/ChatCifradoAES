package logica;



import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
 


public class DiffieHellman {


    private PrivateKey llavePrivada;
    private PublicKey  llavePublica;
    private PublicKey  llavePublicaRecibida;
    private byte[]     llaveSecreta;
    private String     mensajeSecreto;
    

    private static final String ALGORITMODECIFRADO = "AES";

    
    public void generarClaveSecreta() {

        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(llavePrivada);
            keyAgreement.doPhase(llavePublicaRecibida, true);

            llaveSecreta = keyAgreement.generateSecret();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void generarKeys() {

        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(1024);

            final KeyPair keyPair = keyPairGenerator.generateKeyPair();

            llavePrivada = keyPair.getPrivate();
            llavePublica  = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PublicKey getLlavePublica() {

        return llavePublica;
    }


    
    /**
     * In a real life example you must serialize the public key for transferring.
     *
     * @param  person
     */
    public void recibirLlavePublicaDe( DiffieHellman llave) {

        llavePublicaRecibida = llave.getLlavePublica();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * 1024 bit symmetric key size is so big for DES so we must shorten the key size. You can get first 8 longKey of the
     * byte array or can use a key factory
     *
     * @param   longKey
     *
     * @return
     */

    
    
    public static void main(String[] args) {
		DiffieHellman a = new DiffieHellman();
		
		a.generarKeys();
		a.recibirLlavePublicaDe(a);
		a.generarClaveSecreta();
		try {
			byte[] msgEn=a.encriptar("Eileen".getBytes());
			System.out.println(new String(msgEn));
			byte[] msgDes=a.desencriptar((msgEn));
			a.mensajeSecreto=new String(msgDes);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		System.out.println(a.mensajeSecreto);
		
		
	}
    /**
     * Encripta el texto escrito
     *
     * @param textoPlano El texto a encriptar
     * @return mensaje encriptado
     */
    public byte[] encriptar(byte[] textoPlano) throws Exception
    {
    	llaveSecreta = new byte [16];
        SecretKeySpec SsecretKey = new SecretKeySpec(llaveSecreta, ALGORITMODECIFRADO);
        System.out.println(llaveSecreta.length);
        Cipher cipher = Cipher.getInstance(ALGORITMODECIFRADO);
        cipher.init(Cipher.ENCRYPT_MODE, SsecretKey);

        return cipher.doFinal(textoPlano);
    }
    
    /**
     * Des
     *
     * @param cipherText The data to decrypt
     */
    public byte[] desencriptar(byte[] cipherText) throws Exception
    {
        SecretKeySpec SsecretKey = new SecretKeySpec(llaveSecreta, ALGORITMODECIFRADO);
        Cipher cipher = Cipher.getInstance(ALGORITMODECIFRADO);
        cipher.init(Cipher.DECRYPT_MODE, SsecretKey);

        return cipher.doFinal(cipherText);
    }
    
    
    }