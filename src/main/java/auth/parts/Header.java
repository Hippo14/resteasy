package auth.parts;

import utils.ObjectToJsonUtils;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class Header implements IToken {

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
    public String toBase64() {
        return ObjectToJsonUtils.convertToJson(this);
    }
}
