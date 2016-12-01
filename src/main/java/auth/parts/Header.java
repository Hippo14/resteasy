package auth.parts;

import utils.ObjectToJsonUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class Header implements IParts {

    private String alg;
    private String typ;

    public Header () {
    }

    public Header(String alg, String typ) {
        this.alg = "HS256";
        this.typ = "JWT";
    }

    public Header(String header) {
        Header temp = ObjectToJsonUtils.convertToObject(header, Header.class);
        this.alg = temp.getAlg();
        this.typ = temp.getTyp();
    }

    public String getAlg() {
        return alg;
    }

    public String getTyp() {
        return typ;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    @Override
    public String toBase64() throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(ObjectToJsonUtils.convertToJson(this).getBytes("UTF-8")));
    }
}
