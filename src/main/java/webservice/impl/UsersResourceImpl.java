package webservice.impl;

import config.ErrorConfig;
import dao.UsersDAO;
import model.Users;
import org.apache.log4j.Logger;
import utils.MD5Utils;
import webservice.UsersResource;

import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;

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
    public String getByEmailAndPassword(String email, String password) {
        try {
            password = MD5Utils.StringToMD5(password);
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }

        return usersDAO.getByEmail(email, password);
    }

    @Override
    public String registerNewUser(Users newUser) {
        return usersDAO.createNewUser(newUser);
    }

}
