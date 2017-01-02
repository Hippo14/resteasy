package webservice;

import model.Events;
import model.Marker;
import org.jboss.resteasy.spi.HttpRequest;
import webservice.credentials.Token;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by KMacioszek on 2016-10-12.
 */
@Produces(MediaType.APPLICATION_JSON)
@AuthFilter
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

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response registerNewEvent(@Context HttpRequest request);

    @POST
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<Events> getEvents(@Context HttpRequest request);

    @POST
    @Path("/marker")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Marker getMarkerDetails(@Context HttpRequest request);

    @POST
    @Path("/details")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Events getEventDetails(@Context HttpRequest request);

    @POST
    @Path("/board")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, Map<String, String>> getBoard(@Context HttpRequest request);

}
