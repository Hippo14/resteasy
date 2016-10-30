import org.apache.commons.codec.language.Soundex;
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

//        System.out.println("private key: \n" + new String(Base64.encodeBase64(privateKey.getEncoded())));
//        System.out.println("public key: \n" + new String(Base64.encodeBase64(publicKey.getEncoded())));

        RSA rsa = new RSA();
        byte[] password = rsa.encrypt("mraumrau");
        System.out.println(new String(Base64.encodeBase64(password)));
        String decodedPassword = rsa.decrypt(password);
        System.out.println(decodedPassword);


        String base64 = "LXD9VF1VXiBxuIHCx1wfkqLaJmHF/lFeKPP4oEBAIEnX/0XKmHYaGvww/2u33XV6i9VTo2sVwmi8+xvMBX4F/OaSh4H8Fr0QKuv2oe0MAOCTPqdHMMrqlPN8c+5l6XoGBU8KUJ7Eaqyui8b/p9b/3Ym+33ChXagxOmG8oiVNtqzLO9lcDbvgO56HuwKDKlC00GbH5EMPqpo0VDUQbHe5zCey3+Fteyv0tCU46ZCEcmgOYPDvWgXef45l63Oye2jRkVJGeHEWMnwFxi4E1UiMjL1xQcYN/x3r3urB3mXOIZ2T8viMUx4f+0+RO3/E+SZjG6xUvF6wBDf4TtcJ4Pgtcg==";
        byte[] decodedBase64 = Base64.decodeBase64(base64);
        decodedPassword = rsa.decrypt(decodedBase64);
        System.out.println(decodedPassword);
    }
}
