package webservice;

import dao.EventsDAO;
import model.Events;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by MSI on 2016-09-25.
 */
@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventsResource {
    
    @EJB
    EventsDAO eventsDAO;
    
    @Path("")
    @GET
    public Response getAll() {
        List<Events> events = eventsDAO.getAll();
        if (events == null)
            return Response.serverError().build();

        GenericEntity<List<Events>> ge = new GenericEntity<List<Events>>(events){};
        return Response.ok(ge).build();
    }

    @Path("{id}")
    @GET
    public Response getByName(@PathParam("id") Integer id) {
        return Response.ok(eventsDAO.getByName(id)).build();
    }
    
}
