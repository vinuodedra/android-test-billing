package com.android.vending.billing.test;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

import com.android.vending.billing.test.base64.Base64;

public class PurchaseDataSigner {
	/**
	 * @see test-key\android-test-billing.pkcs8
	 */
	private static final String PRIVATE_KEY = ""
		+ "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAIexI/0TV86zdHvm"
		+ "HX8EXYp2t5B3gCO+Bvvp0Cl5H0WZIo7MdtrETGwnok0aCZqcDuORJscrzjqbTlZe"
		+ "0dwXROvi6HEZDfZogXxad7r5RyHSWOjQ6YfsYQ6hz45WsQ8x0epCbdVgtjB9FzDJ"
		+ "W+EpVM6AWK0mLXjOrm9QTZCigjCHAgElAoGANwKf4yOEdmRtflZe/CRdRfjG9WDg"
		+ "6+VBGgTX2XZK8rO0EGC6kApkK9i3a12VMNd7qF1wle8qF8Jr3dNp1cQ+iIdCBRoz"
		+ "NHdQS632GqdSkgKgHxqPVCnkTN8b0w2hIGGNWV6hHUeyLKVisCdYatoh8E15BsF8"
		+ "vFUsMVFHyjPD3EUCQQD4T+gQ11+j/pEoyBtsnZWV98MhJkJPWpqsRYIRhEOIfFPK"
		+ "JdJcyVYajcksq1hKkVg56uUQSo+42iaRjflRR+h1AkEAi+SelGLzN83sQFCRAQAB"
		+ "8aL7GOpCz0rHaM3ymwXADgn+Sph2hkZsQg1w1TzWopHhQxAMwSayIZp5ww6CayP1"
		+ "iwJBAN13s01Kd+VK1H5Rm+tAcKhZhIVZeWlepaCRBU3kqu9aITfA3jcUb2PKkNTQ"
		+ "LCbUqKJb4Q6HsJ3y/83tU/xiupECQDEmz+7yVXR4yKD5tmgimOYyWDkvtpvcAN+i"
		+ "TlIknWy3Zy73TD0D/IXpC/fk8XdjsALOSaulz+JLCC/G0+B0awcCQA6bKznn2bn9"
		+ "f//YiqV2/L6o44BMVE9UYLHM7zEFnDzmvBDDCmMQS6moAIzD2pjRdUl/sP6IoGOh"
		+ "aQxfBD8GJPc=";
	
	public static String sign(String data) { 
		try {
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(PRIVATE_KEY))); 		
			
			Signature signer = Signature.getInstance("SHA1withRSA");
			signer.initSign(privateKey);
			// default charset is : com.ibm.icu4jni.charset.CharsetICU[UTF-8]
			signer.update(data.getBytes("UTF-8"));
			return Base64.encode(signer.sign());
		} catch (Exception ex) {
			return "PurchaseDataSignerFailed";
		}
		
	}
}
