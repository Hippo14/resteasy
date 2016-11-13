package webservice.impl;

import dao.EventsDAO;
import dao.TokensDAO;
import model.Events;
import model.Users;
import org.jboss.resteasy.spi.HttpRequest;
import webservice.EventsResource;
import webservice.credentials.Token;

import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static model.UsersEvents_.events;

/**
 * Created by MSI on 2016-09-25.
 */
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
public class EventsResourceImpl implements EventsResource {
    
    @EJB
    EventsDAO eventsDAO;
    @EJB
    TokensDAO tokensDAO;

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

    @Override
    public Response registerNewUser(@Context HttpRequest request, Events events) {

        return null;
    }
}
