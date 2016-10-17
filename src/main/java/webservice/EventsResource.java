package webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by KMacioszek on 2016-10-12.
 */
@Produces(MediaType.APPLICATION_JSON)
public interface EventsResource {

    Response getAll();

    @GET
    @Path("{id}")
    Response getById(@PathParam("id") Integer id);

    @GET
    @Path("{name}")
    Response getByName(@PathParam("name") String name);
}
