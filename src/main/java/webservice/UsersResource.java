package webservice;

import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by MSI on 2016-10-09.
 */
@Produces(MediaType.APPLICATION_JSON)
public interface UsersResource {

    @GET
    @Path("/get/{name}")
    Response getByName(@PathParam("name") String name);

    @GET
    @Path("/get/{email}/{password}")
    Response getByEmailAndPassword(@PathParam("email") String email, @PathParam("password") String password);
}
