package webservice.impl;

import config.ErrorConfig;
import dao.UsersDAO;
import model.Users;
import org.apache.log4j.Logger;
import secure.RSA;
import utils.MD5Utils;
import webservice.UsersResource;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by MSI on 2016-10-09.
 */
@Path("user")
public class UsersResourceImpl implements UsersResource {

    final static Logger LOG = Logger.getLogger(UsersResourceImpl.class);

    @EJB
    UsersDAO usersDAO;

    @Override
    public Response getByName(String name) {
        return Response.ok(usersDAO.getByName(name)).build();
    }

    @Override
    //TODO Password must be encrypted with public RSA!!!!
    public String getByEmailAndPassword(String email, String password) {
        String decryptedPassword = null;
        // Decrypt password
        try {
            RSA rsa = new RSA();
            decryptedPassword = rsa.decrypt(password.getBytes("UTF-8"));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        try {
            decryptedPassword = MD5Utils.StringToMD5(decryptedPassword);
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage());
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        return usersDAO.getByEmail(email, decryptedPassword);
    }

    @Override
    public String registerNewUser(Users newUser) {
        return usersDAO.createNewUser(newUser);
    }

}
