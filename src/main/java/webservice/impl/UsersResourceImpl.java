package webservice.impl;

import dao.UsersDAO;
import webservice.UsersResource;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by MSI on 2016-10-09.
 */
@Path("user")
public class UsersResourceImpl implements UsersResource {

    @EJB
    UsersDAO usersDAO;

    @Override
    public Response getByName(String name) {
        return Response.ok(usersDAO.getByName(name)).build();
    }

}
