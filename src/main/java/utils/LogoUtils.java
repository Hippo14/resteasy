package utils;

import auth.parts.Payload;
import dao.LogoDAO;
import model.UsersLogo;
import org.apache.commons.codec.binary.Base64;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.UnsupportedEncodingException;

/**
 * Created by MSI on 2017-04-30.
 */
@Stateless
public class LogoUtils {

    @EJB
    LogoDAO logoDAO;

    public String get(String token) throws UnsupportedEncodingException {
        String[] subString = token.split("\\.");

        Payload payload = new Payload(new String(Base64.decodeBase64(subString[1].getBytes("UTF-8"))));
        String username = payload.getName();

        UsersLogo usersLogo = logoDAO.getLogoForUser(username);
        byte[] imageB64 = Base64.encodeBase64(usersLogo.getImage());

        return new String(imageB64);
    }

}
