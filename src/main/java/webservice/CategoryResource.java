package webservice;

import webservice.credentials.Token;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by MSI on 2017-05-01.
 */
@Produces(MediaType.APPLICATION_JSON)
@AuthFilter
public interface CategoryResource {

    @POST
    @Path("")
    Response getAll(Token token);

}
