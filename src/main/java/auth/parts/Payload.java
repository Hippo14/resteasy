package auth.parts;

import org.apache.commons.codec.binary.Base64;
import utils.ObjectToJsonUtils;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class Payload implements IParts {

    private String iss;
    private long exp;
    private String name;

    private String admin;

    public Payload() { }

    public Payload(Timestamp exp, String name, String admin) {
        this.exp = exp.getTime();
        this.name = name;
        this.admin = admin;
        this.iss = "217.26.2.61";
    }

    public Payload(String payload) {
        Payload temp = ObjectToJsonUtils.convertToObject(payload, Payload.class);
        this.exp = temp.getExp();
        this.name = temp.getName();
        this.admin = temp.getAdmin();
        this.iss = temp.getIss();
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

    public void setIss(String iss) {
        this.iss = iss;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toBase64() throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(ObjectToJsonUtils.convertToJson(this).getBytes("UTF-8")));
    }
}
