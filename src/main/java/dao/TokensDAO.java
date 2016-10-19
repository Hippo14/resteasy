package dao;

import auth.Token;
import auth.parts.Header;
import auth.parts.Payload;
import config.ErrorConfig;
import model.Users;
import model.UsersKeys;
import model.UsersKeys_;
import model.Users_;
import org.apache.log4j.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        // Check if newest token exists
        String token = tokenExists(newUser);
        if (token != null)
            return token;


        // Generate key
        byte[] key = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(1024); // for example
            SecretKey secretKey = keyGen.generateKey();
            key = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e);
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        // Get token for user
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
            LOG.error(e);
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        // Add key to database
        em.persist(new UsersKeys(newUser, dateExpire, key));

        return token;
    }

    private String tokenExists(Users newUser) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsersKeys> q = cb.createQuery(UsersKeys.class);
        Root<UsersKeys> from = q.from(UsersKeys.class);
        Predicate predicate = cb.equal(from.get(UsersKeys_.user), newUser);

        q.select(from).where(predicate);

        UsersKeys key = null;
        try {
            key = em.createQuery(q).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        Date date = new Date(System.currentTimeMillis());
        Timestamp actualDate = new Timestamp(date.getTime());

        if (key.getDateExpire().getTime() < actualDate.getTime())
            return new String(key.getKey());
        else
            return null;
    }

}
