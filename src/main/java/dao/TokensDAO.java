package dao;

import auth.Token;
import auth.parts.Header;
import auth.parts.Payload;
import config.ErrorConfig;
import model.Users;
import model.UsersKeys;
import org.apache.log4j.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.ws.rs.WebApplicationException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by KMacioszek on 2016-10-18.
 */
@Stateful
public class TokensDAO {

    @PersistenceContext(name = "NewPersistenceUnit", type = PersistenceContextType.EXTENDED)
    EntityManager em;

    final static Logger LOG = Logger.getLogger(TokensDAO.class);

    private static final int TOKEN_TIME_IN_MIN = 60;

    public String generateToken(Users newUser) {
        // Generate key
        byte[] key = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(1024); // for example
            SecretKey secretKey = keyGen.generateKey();
            key = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage());
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        // Get token for user
        String token = null;
        Date date = new Date(System.currentTimeMillis() + TOKEN_TIME_IN_MIN * 60 * 1000);
        Timestamp dateExpire = new Timestamp(date.getTime());

        try {
            Token tokenO = new Token
                    .TokenBuilder()
                    .header(new Header())
                    .payload(new Payload(
                            dateExpire,
                            newUser.getName(),
                            ("Admin".equals(newUser.getProfiles().getName()) ? "Yes" : "No")
                    ))
                    .signature(key)
                    .build();
            token = tokenO.toString();
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage());
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        // Add key to database
        em.persist(new UsersKeys(newUser, dateExpire, key));

        return token;
    }

}
