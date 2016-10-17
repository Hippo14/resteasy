package auth.parts;

import utils.ObjectToJsonUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class Header implements IParts {

    private final String alg;
    private final String typ;

    public Header () {
        this.alg = "HS256";
        this.typ = "JWT";
    }

    public String getAlg() {
        return alg;
    }

    public String getTyp() {
        return typ;
    }

    @Override
    public String toBase64() throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(ObjectToJsonUtils.convertToJson(this).getBytes("UTF-8")));
    }
}
