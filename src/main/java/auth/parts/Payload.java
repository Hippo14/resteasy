package auth.parts;

import org.apache.commons.codec.binary.Base64;
import utils.ObjectToJsonUtils;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class Payload implements IParts {

    private final String iss;
    private final long exp;
    private final String name;
    private final String admin;

    public Payload(Timestamp exp, String name, String admin) {
        this.exp = exp.getTime();
        this.name = name;
        this.admin = admin;
        this.iss = "217.26.2.61";
    }

    public String getIss() {
        return iss;
    }

    public long getExp() {
        return exp;
    }

    public String getName() {
        return name;
    }

    public String getAdmin() {
        return admin;
    }

    @Override
    public String toBase64() throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(ObjectToJsonUtils.convertToJson(this).getBytes("UTF-8")));
    }
}
