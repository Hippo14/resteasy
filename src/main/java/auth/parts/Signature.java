package auth.parts;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class Signature implements IParts {

    private static final String ALGORITHM_SIGNATURE = "HmacSHA256";
    private final String header;
    private final String payload;
    private final byte[] key;

    public Signature(String header, String payload, byte[] key) {
        this.header = header;
        this.payload = payload;
        this.key = key;
    }

    @Override
    public String toBase64() throws UnsupportedEncodingException {
        String encode = null;

        try {
            Mac algorithm = null;
            algorithm = Mac.getInstance(ALGORITHM_SIGNATURE);
            SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM_SIGNATURE);
            algorithm.init(secretKey);
            String header64 = new String(Base64.encodeBase64(header.getBytes("UTF-8")));
            String payload64 = new String(Base64.encodeBase64(payload.getBytes("UTF-8")));
            encode = Hex.encodeHexString(algorithm.doFinal((header64 + payload64).getBytes("UTF-8")));
            return new String(Base64.encodeBase64(encode.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return encode;
    }
}
