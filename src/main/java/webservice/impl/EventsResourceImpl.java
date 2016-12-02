package webservice.impl;

import auth.parts.Payload;
import dao.EventsDAO;
import dao.TokensDAO;
import dao.UsersDAO;
import model.Events;
import model.Marker;
import model.Users;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.spi.HttpRequest;
import utils.ObjectToJsonUtils;
import webservice.AuthFilter;
import webservice.EventsResource;
import webservice.credentials.Token;

import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MSI on 2016-09-25.
 */
@Path("/events")
@AuthFilter
@Produces(MediaType.APPLICATION_JSON)
public class EventsResourceImpl implements EventsResource {
    
    @EJB
    EventsDAO eventsDAO;
    @EJB
    TokensDAO tokensDAO;
    @EJB
    UsersDAO usersDAO;

    final static Logger LOG = Logger.getLogger(EventsResourceImpl.class);

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
    public Response registerNewEvent(@Context HttpRequest request) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, Object> body = (HashMap<String, Object>) hashMap.get("body");
        ObjectMapper mapper = new ObjectMapper();

        String token = (String) hashMap.get("token");

        try {
            Payload payload = new Payload(new String(Base64.decodeBase64(token.split("\\.")[1].getBytes("UTF-8"))));

            // Get actual user
            Users user = usersDAO.getByName(payload.getName());

            body.put("users", mapper.convertValue(user, HashMap.class));
        } catch (UnsupportedEncodingException e) {
            LOG.error(e);
        }

        Events event = null;
        try {
            event = mapper.convertValue(body, Events.class);
        } catch (Exception e) {
            LOG.error(e);
            throw new WebApplicationException("Cannot parse to object!");
        }

        // Check if event exists
        Events eventDb = eventsDAO.getByName(event.getName());
        if (eventDb != null) {
            throw new WebApplicationException("Event exists!");
        }
        eventsDAO.add(event);

        String jsonResponse = ObjectToJsonUtils.convertToJson("Event added");
        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }

    @Override
    public List<Events> getEvents(@Context HttpRequest request) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, Object> body = (HashMap<String, Object>) hashMap.get("body");
        ObjectMapper mapper = new ObjectMapper();

        String cityName = (String) body.get("cityName");
        double latitude = (double) body.get("latitude");
        double longitude = (double) body.get("longitude");

        List<Events> eventsList = eventsDAO.getByLocation(cityName, latitude, longitude);

        LOG.info("[GET EVENTS - " + eventsList + " latitude: " + latitude + " longitude:" + longitude + " cityName" + cityName);

        return eventsList;
    }

    @Override
    public Marker getMarkerDetails(@Context HttpRequest request) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, Object> body = (HashMap<String, Object>) hashMap.get("body");
        ObjectMapper mapper = new ObjectMapper();

        String cityName = (String) body.get("cityName");
        double latitude = (double) body.get("latitude");
        double longitude = (double) body.get("longitude");

        Marker marker = eventsDAO.getMarkerDetails(cityName, latitude, longitude);

        LOG.info("[GET MARKER DETAILS - " + marker + " latitude: " + latitude + " longitude:" + longitude + " cityName" + cityName);

        return marker;
    }

}
