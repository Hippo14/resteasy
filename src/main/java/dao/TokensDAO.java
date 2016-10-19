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
import org.hibernate.Criteria;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ejb.Stateful;
import javax.persistence.*;
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
    private static final int TOKEN_TIME = TOKEN_TIME_IN_MIN * 60 * 1000;

    public String generateToken(Users newUser) {
        UsersKeys usersKeys = tokenExists(newUser);
        byte[] key = null;
        Timestamp dateExpire = null;

        // Check if newest token exists
        if (usersKeys != null) {
            key = usersKeys.getKey();
            dateExpire = usersKeys.getDateExpire();
        }
        else {
            // Generate key
            key = generateKey();
            // Calculate expire date
            Date date = new Date(System.currentTimeMillis() + TOKEN_TIME);
            dateExpire = new Timestamp(date.getTime());
        }

        String token = null;

        // Get token for user
        token = getTokenForUser(newUser, key, dateExpire);

        if (usersKeys == null)
            // Add key to database
            em.persist(new UsersKeys(newUser, dateExpire, key));

        return token;
    }

    private UsersKeys tokenExists(Users newUser) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsersKeys> q = cb.createQuery(UsersKeys.class);
        Root<UsersKeys> from = q.from(UsersKeys.class);
        Predicate predicate = cb.equal(from.get(UsersKeys_.user), newUser);

        q.select(from).where(predicate).orderBy(cb.desc(from.get(UsersKeys_.dateExpire)));

        UsersKeys key = null;
        try {
            key = em.createQuery(q).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            key = em.createQuery(q).setMaxResults(1).getResultList().get(0);
        }

        Date date = new Date(System.currentTimeMillis());
        Timestamp actualDate = new Timestamp(date.getTime());

        if (key.getDateExpire().getTime() > actualDate.getTime())
            return key;
        else
            return null;
    }

    private byte[] generateKey() {
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
        return key;
    }

    public String getTokenForUser(Users newUser, byte[] key, Timestamp dateExpire) {
        String token = null;

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
        return token;
    }
}
