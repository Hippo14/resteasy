package webservice.impl;

import dao.EventsDAO;
import model.Events;
import webservice.EventsResource;
import webservice.credentials.Token;

import javax.ejb.EJB;
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
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
public class EventsResourceImpl implements EventsResource {
    
    @EJB
    EventsDAO eventsDAO;

    @Override
    public Response getAll(Token token) {
        List<Events> events = eventsDAO.getAll();
        if (events == null)
            return Response.serverError().build();

        GenericEntity<List<Events>> ge = new GenericEntity<List<Events>>(events){};
        return Response.ok(ge).build();
    }

    @Override
    @Path("{id}")
    public Response getById(@PathParam("id") Integer id) {
        return Response.ok(eventsDAO.getById(id)).build();
    }

    @Override
    @Path("{name}")
    public Response getByName(@PathParam("name") String name) {
        return Response.ok(eventsDAO.getByName(name)).build();
    }
}
