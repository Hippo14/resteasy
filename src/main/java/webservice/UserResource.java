package webservice;

import model.Users;
import org.jboss.resteasy.spi.HttpRequest;
import webservice.credentials.EmailPassCred;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by MSI on 2016-10-09.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserResource {

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response getByEmailAndPassword(EmailPassCred credentials);

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response registerNewUser(Users newUser);

    @POST
    @AuthFilter
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Users getUserByToken(@Context HttpRequest request);

    @POST
    @AuthFilter
    @Path("/logo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, String> getUserLogo(@Context HttpRequest request);

    @POST
    @AuthFilter
    @Path("/logo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Boolean setUserLogo(@Context HttpRequest request);

}
