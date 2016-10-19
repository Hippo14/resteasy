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
    private static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApqMjhQPflyFqCjo5D+8AoyEkvUqZK8se436nvMob9O9s2homE1TN6pT0+tO6OjLWsryXrc1zTtDqjljbqhXm73v2B7j+hQSGsHep8wmIQyCg8l8/coBArm9jycR88c18vP2l7pwXnb2OlyYBa/GjWQZgAzn4CpaGXskXhE5LQfU3H+1ZjikAkIIz4oyLibU6e6D7TqR3c4YO9rzMR+opATCBbQKp250rUUcLL3cJz+pnoo1k4RM+nam+PRYB0eqrLhFVxXq5a6QvpU7OBfHgnRb5V5XH1mVGsdTLQzMgtyxjlpEDyww1+GjCsQODUnFWNZlbqe5ozbmGQU9yGklXdwIDAQAB";
    private static final String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCmoyOFA9+XIWoKOjkP7wCjISS9Spkryx7jfqe8yhv072zaGiYTVM3qlPT607o6MtayvJetzXNO0OqOWNuqFebve/YHuP6FBIawd6nzCYhDIKDyXz9ygECub2PJxHzxzXy8/aXunBedvY6XJgFr8aNZBmADOfgKloZeyReETktB9Tcf7VmOKQCQgjPijIuJtTp7oPtOpHdzhg72vMxH6ikBMIFtAqnbnStRRwsvdwnP6meijWThEz6dqb49FgHR6qsuEVXFerlrpC+lTs4F8eCdFvlXlcfWZUax1MtDMyC3LGOWkQPLDDX4aMKxA4NScVY1mVup7mjNuYZBT3IaSVd3AgMBAAECggEAFeEjv8pTaEeV4cPlTCdIgSLP7R7HX82qv5oYshwjcZSiSTzXtQAUjXGDfQ+hyxRzxkIm9EYelsGtmoUmSn3XtagkFL2AhbyjZrY/lU6yGoZFO62v6fvXAlBT3OE0upcNgs8XI25RV3/VIhl2ZFL07LJ+ls3cuJ8tA3MJ6HBTR1SRdWEeqeZ+dSg0J2qit07ruIbdgswJmCqJJsMCJ0inlc5n5ZHl528CAvB4w7pB/sWPZBQQX/8Ghhr+CpeN2W2LPhS5RiTHtfXYpTORu1FVgjSjWusltTYk/+JTttFs/y7I/g/q4WUvsrim0uTwB/tF2WfOSL6hCYnNSaqTT8Bh8QKBgQDrZvSgEgdlCYiXVFXmlfeaJIi6Fe1HpGIPERlDCA2KRjLkiqz1zRxcSf+og1Sbm97gJnlrka/KIq7AsdVqanZvPJVIumLj00W+gPG0gxOt7bwjSQ5fHtW2LU2G5zsXlslacvMUVtL25Mrw5oskLV1gyDeKXVvjAmcF1/AiIoXJKQKBgQC1N9a4BsrBlotgxN7oP6TnMRYclFRnlajTFZ78pD6XVrQFEeGxhJ0luNA3FR9BZhF5dA/LnLJ8VJ38sY2DHXQnmvjfO2fd1/znKNHe914ZWf9O2wQ41p/U4fbAteZAJWvOiS/FtIqcOiHsdeXLf00VgeuCrs9WoqD817CexCgPnwKBgQCLFPRZku1di+lxB7oNF/QH1agW0he0aXIqVr7nNN3TIbC2CR0xyTMFcA8mQBoAKnLfWrwIizYipqU7YpamXiqFhHTrWHxPASfG66ZjMcKx07GB44sjNjOshE228+2c/VEQKLRoAvh4OSuSc5QoFsmCAimWjZxZnrTB5hiSAnWrKQKBgB/PXXGQP6cbtfuWk0pRYbDzfN4na2mDa88hzHasnhASuCbrrKJslOk6N9JzcrkYIZkgNmWXa/1HHTRuNlj9opYRLf6BfwoCD/LDx5NL+Miac6Zz65nOkt/bimTpiTFZYEMw8aHSv0INMkc/qsa94doUpDKcXOjoQzoe4nH8HMwhAoGBANDygmTkSlOn7ihFCryWWOKHFThNVfVnIY+ObxZrI5Fd/WwO9mVwS+0BG6DoRmCzyxa/AA7oyTw1FdCq62Z8n6ApXnYbClmvangdgj8JcufscNFNopT0J2Oh8aRgWWtqSTfRKvGYVVa7D6PfSTf5zu+wKg8w1VJnbDYlIyXly8R7";
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
