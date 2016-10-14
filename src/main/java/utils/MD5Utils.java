package utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by MSI on 2016-10-14.
 */
public class MD5Utils {

    public static String StringToMD5(String password) throws NoSuchAlgorithmException {
        MessageDigest mdEnc = MessageDigest.getInstance("MD5");
        mdEnc.update(password.getBytes(), 0, password.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);

        return md5;
    }

}
