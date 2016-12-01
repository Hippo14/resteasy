package webservice.credentials;

import java.io.Serializable;

/**
 * Created by MSI on 2016-10-19.
 */
public class Token implements Serializable{

    String token;

    public Token() {}

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
