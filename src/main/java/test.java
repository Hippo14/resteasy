import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by KMacioszek on 2016-10-18.
 */
public class test {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);

        byte[] publicKey = keyGen.genKeyPair().getPublic().getEncoded();
        StringBuffer retString1 = new StringBuffer();
        retString1.append("[");

        for (int puk = 0; puk < publicKey.length; ++puk) {
            retString1.append(publicKey[puk]);
            // retString1.append(", ");
        }
        retString1 = retString1.delete(retString1.length()-2,retString1.length());
        retString1.append("]");
        System.out.println(retString1);

        byte[] privateKey = keyGen.genKeyPair().getPrivate().getEncoded();
        StringBuffer retString2 = new StringBuffer();
        retString2.append("[");

        for (int pri = 0; pri < privateKey.length; ++pri) {
            retString2.append(privateKey[pri]);
            // retString2.append(", ");
        }
        retString2 = retString2.delete(retString2.length()-2,retString2.length());
        retString2.append("]");
        System.out.println(retString2);
    }
}
