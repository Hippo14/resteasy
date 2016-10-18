package webservice.credentials;

import java.io.Serializable;

/**
 * Created by KMacioszek on 2016-10-18.
 */
public class EmailPassCred implements Serializable {

    String email;
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
