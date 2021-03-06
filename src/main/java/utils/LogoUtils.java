package utils;

import auth.parts.Payload;
import dao.LogoDAO;
import model.Users;
import model.UsersLogo;
import org.apache.commons.codec.binary.Base64;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by MSI on 2017-04-30.
 */
@Stateful
public class LogoUtils implements Serializable {

    private UUID uuid = java.util.UUID.randomUUID();

    @EJB
    LogoDAO logoDAO;

    @Deprecated
    public String get(String token) throws UnsupportedEncodingException {
        String[] subString = token.split("\\.");

        Payload payload = new Payload(new String(Base64.decodeBase64(subString[1].getBytes("UTF-8"))));
        String username = payload.getName();

        UsersLogo usersLogo = logoDAO.getLogoForUser(username);
        byte[] imageB64 = Base64.encodeBase64(usersLogo.getImage());

        return new String(imageB64);
    }

    public String get(Users users) {
        UsersLogo usersLogo = users.getUsersLogo();
        byte[] imageB64 = Base64.encodeBase64(usersLogo.getImage());

        return new String(imageB64);
    }
}
