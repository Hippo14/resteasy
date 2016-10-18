import secure.RSA;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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

//        System.out.println("private key: \n" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
//        System.out.println("public key: \n" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));

        RSA rsa = new RSA();
        byte[] password = rsa.encrypt("303delta");
        System.out.println(Base64.getEncoder().encodeToString(password));
        String decodedPassword = rsa.decrypt(password);
        System.out.println(decodedPassword);
    }
}
