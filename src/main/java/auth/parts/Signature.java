package auth.parts;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import utils.ObjectToJsonUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class Signature implements IParts {

    private static String ALGORITHM_SIGNATURE = "HmacSHA256";
    private String header;
    private String payload;
    private byte[] key;

    public Signature() { }

    public Signature(String header, String payload, byte[] key) {
        this.header = header;
        this.payload = payload;
        this.key = key;
    }

    public Signature(String signature) {
        Signature temp = ObjectToJsonUtils.convertToObject(signature, Signature.class);
        this.header = temp.getHeader();
        this.payload = temp.getPayload();
        this.key = temp.getKey();
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
            encode = Hex.encodeHexString(algorithm.doFinal((header64 + "." + payload64).getBytes("UTF-8")));
            return new String(Base64.encodeBase64(encode.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return encode;
    }

    public String getHeader() {
        return header;
    }

    public String getPayload() {
        return payload;
    }

    public byte[] getKey() {
        return key;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }
}
