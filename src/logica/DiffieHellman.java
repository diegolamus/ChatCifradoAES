package logica;

import javax.crypto.KeyAgreement;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class DiffieHellman {

	public static byte[] generarClaveSecretaComun(PrivateKey miLlavePrivada, PublicKey laLlavePublica) {

		byte[] claveSecreta = null;
		try {
			KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
			keyAgreement.init(miLlavePrivada);
			keyAgreement.doPhase(laLlavePublica, true);

			claveSecreta = keyAgreement.generateSecret();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return claveSecreta;
	}

	public static KeyPair generarKeys() {
		KeyPair keyPair = null;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
			keyPairGenerator.initialize(1024);

			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyPair;
	}
	

	
	
}