package logica;

import javax.crypto.KeyAgreement;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
 


public class DiffieHellman {


    private PrivateKey llavePrivada;
    private PublicKey  llavePublica;
    
    public byte[] generarClaveSecretaComun(PrivateKey miLlavePrivada, PublicKey laLlavePublica) {
    	
    	byte[] claveSecreta=null;
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(miLlavePrivada);
            keyAgreement.doPhase(laLlavePublica, true);

            claveSecreta=keyAgreement.generateSecret();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return claveSecreta;
    }

    /**
     * Genera la llave pública y privada de las personas del chat.
     *
     * 
     */

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



    
    /**
     * Recibe la llave pública de la otra persona en el chat.
     *
     * @param  llave La llave pública de la otra persona en el chat.
     */
//    public void recibirLlavePublicaDe( DiffieHellman llave) {
//
//        llavePublicaRecibida = llave.getLlavePublica();
//    }

        
//    public static void main(String[] args) {
//		DiffieHellman a = new DiffieHellman();
//		
//		a.generarKeys();
//		a.recibirLlavePublicaDe(a);
//		a.generarClaveSecretaComun();
//		try {
//			byte[] msgEn=a.encriptar("Eileen Guerrero".getBytes());
//			System.out.println(new String(msgEn));
//			byte[] msgDes=a.desencriptar((msgEn));
//			a.mensajeSecreto=new String(msgDes);
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		System.out.println(a.mensajeSecreto);
//		
//		
//	}
    
    
    
    }