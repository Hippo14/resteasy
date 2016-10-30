package webservice.impl;

import config.ErrorConfig;
import dao.UsersDAO;
import model.Users;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import secure.RSA;
import utils.MD5Utils;
import utils.ObjectToJsonUtils;
import webservice.UsersResource;
import webservice.credentials.EmailPassCred;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.IOException;
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
    public Response getByEmailAndPassword(EmailPassCred credentials) {
        LOG.info("[LOGIN USER EVENT - "+ " | email: " + credentials.getEmail() + " | password: " + credentials.getPassword() + " ]");

        String decryptedPassword = decryptPassword(credentials.getPassword());

        // Convert response to json object
        String jsonResponse = ObjectToJsonUtils.convertToJson(usersDAO.getByEmail(credentials.getEmail(), decryptedPassword));

        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }

    @Override
    public Response registerNewUser(Users newUser) {
        LOG.info("[NEW USER EVENT - "+ " | name: " + newUser.getName() + " | email: " + newUser.getEmail() + " | password: " + newUser.getPassword() + " ]");

        String decryptedPassword = decryptPassword(newUser.getPassword());
        newUser.setPassword(decryptedPassword);

        // Add new user
        String jsonResponse = ObjectToJsonUtils.convertToJson(usersDAO.createNewUser(newUser));
        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }

    private String decryptPassword(String password) {
        String decryptedPassword = null;
        // Decrypt password
        try {
            RSA rsa = new RSA();
            decryptedPassword = rsa.decrypt(Base64.decodeBase64(password.getBytes("UTF-8")));
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        try {
            decryptedPassword = MD5Utils.StringToMD5(decryptedPassword);
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e);
            e.printStackTrace();
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        return decryptedPassword;
    }

}
