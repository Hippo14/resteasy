package utils;

import auth.parts.Payload;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;

/**
 * Created by MSI on 2017-05-04.
 */
public class JWTUtils {

    private static final Logger LOG = Logger.getLogger(JWTUtils.class);

    public static String getUsername(String token) throws UnsupportedEncodingException {
        String[] subString = token.split("\\.");

        Payload payload = new Payload(new String(Base64.decodeBase64(subString[1].getBytes("UTF-8"))));
        String username = payload.getName();

        return username;
    }

}
