package webservice;

import model.Users;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/**
 * Created by MSI on 2016-10-09.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UsersResource {

    @GET
    @Path("/get/{name}")
    Response getByName(@PathParam("name") String name);

    @GET
    @Path("/get/{email}/{password}")
    String getByEmailAndPassword(@PathParam("email") String email, @PathParam("password") String password);

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    String registerNewUser(Users newUser);
}
