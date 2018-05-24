package logica;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class DiffieHellman {


	public static KeyPair generarKeys(byte[] remotepu) {
		try {
			KeyFactory kf = KeyFactory.getInstance("DH");
			X509EncodedKeySpec encoder = new X509EncodedKeySpec(remotepu);
			PublicKey remotePub = kf.generatePublic(encoder);
			DHParameterSpec dhps = ((DHPublicKey)remotePub).getParams();
			KeyPairGenerator localkpg = KeyPairGenerator.getInstance("DH");
			localkpg.initialize(dhps);
			return localkpg.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static KeyPair generarKeys() {
		KeyPair keyPair = null;
		try {
			//CREAR PARAMETROS DE DIFFIE HELLMAN
			AlgorithmParameterGenerator pGenerator = AlgorithmParameterGenerator.getInstance("DH");
			pGenerator.init(1024);
			AlgorithmParameters pars = pGenerator.generateParameters();
			DHParameterSpec dhPSpec  = (DHParameterSpec) pars.getParameterSpec(DHParameterSpec.class);
			//CREAR KEYPAIR GENERATOR
			KeyPairGenerator localKpg = KeyPairGenerator.getInstance("DH");
			localKpg.initialize(dhPSpec);
			KeyPair localkp = localKpg.generateKeyPair();
			keyPair = localkp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyPair;
	}
	
	public static KeyAgreement generateKeyAgreement(KeyPair localKp) {
		try {
			//CREAR DH AGREEMENT
			KeyAgreement localka = KeyAgreement.getInstance("DH");
			localka.init(localKp.getPrivate());
			return localka;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static SecretKey generarClaveSecretaComun(KeyAgreement localka,PrivateKey miLlavePrivada, byte[] laLlavePublica) {
		SecretKey claveSecreta = null;
		try {
			KeyFactory kf = KeyFactory.getInstance("DH");
			X509EncodedKeySpec encoder = new X509EncodedKeySpec(laLlavePublica);
			PublicKey remoteKey =  kf.generatePublic(encoder);
			localka.doPhase(remoteKey, true);
			claveSecreta = localka.generateSecret("AES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return claveSecreta;
	}
	

	
	
}