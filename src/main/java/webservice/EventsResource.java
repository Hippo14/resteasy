package webservice;


import webservice.credentials.Token;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by KMacioszek on 2016-10-12.
 */
@Produces(MediaType.APPLICATION_JSON)
public interface EventsResource {
    @POST
    @Path("")
    Response getAll(Token token);

    @GET
    @Path("{id}")
    Response getById(@PathParam("id") Integer id);

    @GET
    @Path("{name}")
    Response getByName(@PathParam("name") String name);
}
