package webservice;

import model.Events;
import model.Marker;
import model.UsersEvents;
import org.jboss.resteasy.spi.HttpRequest;

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
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response registerNewEvent(@Context HttpRequest request);

    @POST
    @Path("/")
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

    @POST
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, Map<String, String>> getEventsByUser(@Context HttpRequest request);

    @POST
    @Path("/list/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<UsersEvents> getUserListEvent(@Context HttpRequest request);

    @POST
    @Path("/user/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, String> addUserToEvent(@Context HttpRequest request);

    @POST
    @Path("/user/count")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, String> getLikedEvents(@Context HttpRequest request);

    @POST
    @Path("/user/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, String> deleteUserFromEvent(@Context HttpRequest request);

    @POST
    @Path("/user/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Boolean getUserStatusEvent(@Context HttpRequest request);

}
