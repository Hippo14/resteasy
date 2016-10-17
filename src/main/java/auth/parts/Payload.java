package auth.parts;

import utils.ObjectToJsonUtils;

import java.sql.Timestamp;

/**
 * Created by KMacioszek on 2016-10-17.
 */
public class Payload implements IParts {

    private final String iss;
    private final Timestamp exp;
    private final String name;
    private final String admin;

    public Payload(Timestamp exp, String name, String admin) {
        this.exp = exp;
        this.name = name;
        this.admin = admin;
        this.iss = "217.26.2.61";
    }

    @Override
    public String toBase64() {
        return ObjectToJsonUtils.convertToJson(this);
    }
}
