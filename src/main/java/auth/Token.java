package auth;

import auth.parts.Header;
import auth.parts.Payload;
import auth.parts.Signature;
import model.Users;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class Token implements IToken{

    private static final int TOKEN_TIME_IN_MIN = 60;

    public static String getTokenToJson(Users users, byte[] key) throws NoSuchAlgorithmException {

        Date date = new Date(System.currentTimeMillis() + TOKEN_TIME_IN_MIN * 60 * 1000);

        // Create header
        Header header = new Header();

        // Create payload
        Payload payload = new Payload(
                new Timestamp(date.getTime()),
                users.getName(),
                ("Admin".equals(users.getProfiles().getName()) ? "Yes" : "No")
        );

        // Create signature
        Signature signature = new Signature(
                header.toBase64(),
                payload.toBase64(),
                key
        );

        return
                header.toBase64() + "." +
                payload.toBase64() + "." +
                signature.toBase64();
    }


}
