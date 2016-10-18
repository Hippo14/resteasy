package secure;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by KMacioszek on 2016-10-18.
 */
public class RSA {

    //TODO Key must be in database!!!!
    // Keys stored in Base64
    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuvL5AN/ih53OwmP/1UUEVtw50DKh2GGVFVP9/bWIQ461L6otAytfHAZ5wiVcbng4m4v0+RyYV6vWHldrHG/i++yf7AyTvYJP2mm8O1J9/Yf1eSlkg5HpRM90q5em0EDYa+5jqCqMxxVCO0v4HJQyoFSyoBYO1A/QfWzarcQSRawIDAQAB";
    private static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK68vkA3+KHnc7CY//VRQRW3DnQMqHYYZUVU/39tYhDjrUvqi0DK18cBnnCJVxueDibi/T5HJhXq9YeV2scb+L77J/sDJO9gk/aabw7Un39h/V5KWSDkelEz3Srl6bQQNhr7mOoKozHFUI7S/gclDKgVLKgFg7UD9B9bNqtxBJFrAgMBAAECgYBbhBbQY4Agq6v3zeogfZHs4Zb8SLTiRRylF0NJZex6lAPrQsf0A0jHvZ1Rq1yn5jvbOf8QYwsbZ5d4j4+pwIih/cvPbM8Doo3tDucet5O5BMkO7c3FMv8OAMhuvMNNuOhuIlU9sEmXq9hZyZjJVb6sPaWyfOW+kvV2oqqxRcrv4QJBAPUYe7qG/WO5Wv655bjvE1xo/nkUMlnnmoI1phcE5Nvm9tHOsillFv53v66152KSIC2DbYXrZ/S7yF6zDUEgN98CQQC2gunQXtCIZxiS73Ffz3G3V067Pz22mvCw8zWmwLFHp6n5nRwu39Td0YSDlIFip2KrptdA2oj1pxG+ANvZWAf1AkEA8HVRxmFzregG/0wQtgxQOWSE7PNEpvHTo5XQ2lqh0aDaXGLmZjhYg9WmSFkMpqdoTF7aD/4xomitH0OZaGCcjwJAXIKhB3h31RwEzb3Gxty8dEUx6CGXI28H33l30W1MrthihN7JAU19dW4U8CkfKCVWE1G/O+VJlNK5YIjI5zMC3QJBAIblqI8NrjB6NmlHkVQamAZueEyJwQb+6K+2dZnpR8gWHPUlu/4MUAaHiMKwxa067tQ/z0niI4Bl7Hdh4dyOTYc=";
    PublicKey publicKeyRSA;
    PrivateKey privateKeyRSA;

    public RSA() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        publicKeyRSA = loadPublicKey();
        privateKeyRSA = loadPrivateKey();
    }

    private PublicKey loadPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(Base64.decodeBase64(publicKey.getBytes("UTF-8")));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpecPublic);
    }

    private PrivateKey loadPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes("UTF-8")));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpecPrivate);
    }

    public byte[] encrypt (String text) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher rsa;
        rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.ENCRYPT_MODE, publicKeyRSA);
        return rsa.doFinal(text.getBytes("UTF-8"));
    }

    public String decrypt (byte[] buffer) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher rsa;
        rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.DECRYPT_MODE, privateKeyRSA);
        byte[] utf8 = rsa.doFinal(buffer);
        return new String(utf8, "UTF-8");
    }

}
