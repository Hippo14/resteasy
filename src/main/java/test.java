import secure.RSA;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by KMacioszek on 2016-10-18.
 */
public class test {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidKeySpecException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        System.out.println("private key: \n" + new String(Base64.encodeBase64(privateKey.getEncoded())));
        System.out.println("public key: \n" + new String(Base64.encodeBase64(publicKey.getEncoded())));

        RSA rsa = new RSA();
        byte[] password = rsa.encrypt("303delta");
        System.out.println(new String(Base64.encodeBase64(password)));
        String decodedPassword = rsa.decrypt(password);
        System.out.println(decodedPassword);
    }
}
