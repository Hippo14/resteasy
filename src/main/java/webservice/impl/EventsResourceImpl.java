package webservice.impl;

import config.ErrorConfig;
import dao.EventsDAO;
import dao.TokensDAO;
import dao.UsersDAO;
import dao.UsersEventsDAO;
import model.Events;
import model.Marker;
import model.Users;
import model.UsersEvents;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.spi.HttpRequest;
import utils.JWTUtils;
import utils.LogoUtils;
import utils.ObjectToJsonUtils;
import webservice.AuthFilter;
import webservice.EventsResource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by MSI on 2016-09-25.
 */
@Path("/events")
@AuthFilter
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class EventsResourceImpl implements EventsResource, Serializable {

    private UUID uuid = java.util.UUID.randomUUID();

    @EJB
    EventsDAO eventsDAO;
    @EJB
    TokensDAO tokensDAO;
    @EJB
    UsersDAO usersDAO;
    @EJB
    LogoUtils logoUtils;
    @EJB
    UsersEventsDAO usersEventsDAO;

    final static Logger LOG = Logger.getLogger(EventsResourceImpl.class);

    @Override
    public Response registerNewEvent(@Context HttpRequest request) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, Object> body = (HashMap<String, Object>) hashMap.get("body");
        ObjectMapper mapper = new ObjectMapper();

        String token = (String) hashMap.get("token");

        try {
            // Get actual user
            Users user = usersDAO.getByName(JWTUtils.getUsername(token));
            body.put("users", mapper.convertValue(user, HashMap.class));
        } catch (UnsupportedEncodingException e) {
            LOG.error(e);
        }

        Events event;
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
        eventsDAO.addUserToEvent(event.getUsers(), event.getLatitude(), event.getLongitude());

        String jsonResponse = ObjectToJsonUtils.convertToJson("Event added");
        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }

    @Override
    public List<Events> getEvents(@Context HttpRequest request) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, Object> body = (HashMap<String, Object>) hashMap.get("body");
        ObjectMapper mapper = new ObjectMapper();

        double latitude = (double) body.get("latitude");
        double longitude = (double) body.get("longitude");
        String cityName = (String) body.get("cityName");

        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

        Date actualDate = new Date();
//        try {
//            actualDate = df.parse((String) body.get("actualDate"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        List<Events> eventsList = eventsDAO.getByLocation(cityName, latitude, longitude, new Timestamp(actualDate.getTime()));

        StringBuilder eventString = new StringBuilder("");
        for (Events event : eventsList) {
            eventString.append(event.getName() + " " + event.getId() + "\n");
        }

        LOG.info("[GET EVENTS - " + eventString.toString() + " latitude: " + latitude + " longitude:" + longitude + " cityName: " + "");

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

    @Override
    public Events getEventDetails(@Context HttpRequest request) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, Object> body = (HashMap<String, Object>) hashMap.get("body");
        ObjectMapper mapper = new ObjectMapper();

        double latitude = (double) body.get("latitude");
        double longitude = (double) body.get("longitude");

        Events event = eventsDAO.getByLocation(latitude, longitude);

        LOG.info("[GET EVENTS DETAILS- " + event + " latitude: " + latitude + " longitude:" + longitude);

        return event;
    }

    @Override
    public Map<String, Map<String, String>> getBoard(@Context HttpRequest request) {
        HashMap<String, Object> requestMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, String> body = (HashMap<String, String>) requestMap.get("body");
        ObjectMapper mapper = new ObjectMapper();

        String token = (String) requestMap.get("token");

        double latitude = Double.parseDouble(body.get("latitude"));
        double longitude = Double.parseDouble(body.get("longitude"));

        List<Events> boardList = eventsDAO.getTopEvents(new Timestamp(new Date().getTime()), latitude, longitude, 10);

        Map<String, Map<String, String>> response = new HashMap<>();
        int i = 0;
        for (Events event : boardList) {
            Map<String, String> map = new HashMap<>();
            map.put("name", event.getName());
            map.put("description", event.getDescription());
            map.put("username", event.getUsers().getName());
            map.put("image", logoUtils.get(event.getUsers()));
            map.put("latitude", Double.toString(event.getLatitude()));
            map.put("longitude", Double.toString(event.getLongitude()));

            response.put(Integer.toString(i++), map);
        }

        LOG.info("[GET BOARD -  latitude: " + latitude + " longitude:" + longitude + " response" + response);

        return response;
    }

    @Override
    public Map<String, Map<String, String>> getEventsByUser(@Context HttpRequest request) {
        HashMap<String, Object> requestMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, String> body = (HashMap<String, String>) requestMap.get("body");
        ObjectMapper mapper = new ObjectMapper();

        String token = (String) requestMap.get("token");
        Map<String, Map<String, String>> response = new HashMap<>();

        try {
            Users user = usersDAO.getByName(JWTUtils.getUsername(token));

            List<Events> eventsList = usersEventsDAO.getByUser(user);

            int i = 0;
            for (Events event : eventsList) {
                Map<String, String> map = new HashMap<>();
                map.put("name", event.getName());
                map.put("description", event.getDescription());
                map.put("latitude", Double.toString(event.getLatitude()));
                map.put("longitude", Double.toString(event.getLongitude()));
                response.put(Integer.toString(i++), map);
            }
            LOG.info("[GET EVENTS BY USER - username" + JWTUtils.getUsername(token) + " response" + response);
        } catch (UnsupportedEncodingException e) {
            LOG.info("[GET EVENTS BY USER - error  response - " + response + " e - " + e.getMessage());

            e.printStackTrace();
        }

        return response;
    }

    @Override
    public List<UsersEvents> getUserListEvent(@Context HttpRequest request) {
        HashMap<String, Object> requestMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, String> body = (HashMap<String, String>) requestMap.get(("body"));

        Double latitude = Double.parseDouble(body.get("latitude"));
        Double longitude = Double.parseDouble(body.get("longitude"));

        List<UsersEvents> userList = eventsDAO.getUserListEvent(latitude, longitude);

//        Map<String, Map<String, String>> response = new HashMap<>();
//        for (String elem : userList) {
//            Map<String, String> map = new HashMap<>();
//
//            map.put("user", elem);
//        }

        return userList;
    }

    @Override
    public Map<String, String> addUserToEvent(@Context HttpRequest request) {
        HashMap<String, Object> requestMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, String> body = (HashMap<String, String>) requestMap.get(("body"));
        Map<String, String> response = new HashMap<>();

        String token = (String) requestMap.get("token");

        Double latitude = Double.parseDouble(body.get("latitude"));
        Double longitude = Double.parseDouble(body.get("longitude"));
        Users user = null;

        try {
            user = usersDAO.getByName(JWTUtils.getUsername(token));
        } catch (UnsupportedEncodingException e) {
            LOG.info("[GET USER BY TOKEN - error  response - " + " e - " + e.getMessage());

            e.printStackTrace();
        }

        user = eventsDAO.addUserToEvent(user, latitude, longitude);

        if (user == null) {
            throw new WebApplicationException(ErrorConfig.USER_EXISTS_IN_EVENT);
        }

        response.put("result", "User added!");

        return response;
    }

    @Override
    public Map<String, String> getLikedEvents(@Context HttpRequest request) {
        HashMap<String, Object> requestMap = (HashMap<String, Object>) request.getAttribute("request");
        String token = (String) requestMap.get("token");

        Map<String, String> result = new HashMap<>();

        try {
            result.put("likedEvents", Long.toString(usersEventsDAO.getLikedEvents(JWTUtils.getUsername(token))));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, String> deleteUserFromEvent(@Context HttpRequest request) {
        HashMap<String, Object> requestMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, String> body = (HashMap<String, String>) requestMap.get(("body"));
        Map<String, String> response = new HashMap<>();

        String token = (String) requestMap.get("token");

        Double latitude = Double.parseDouble(body.get("latitude"));
        Double longitude = Double.parseDouble(body.get("longitude"));
        Users user = null;

        try {
            user = usersDAO.getByName(JWTUtils.getUsername(token));
        } catch (UnsupportedEncodingException e) {
            LOG.info("[GET USER BY TOKEN - error  response - " + " e - " + e.getMessage());

            e.printStackTrace();
        }

        user = eventsDAO.removeUserFromEvent(user, latitude, longitude);

        if (user == null) {
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        response.put("result", "Deleted!");

        return response;
    }

    @Override
    public Boolean getUserStatusEvent(@Context HttpRequest request) {
        HashMap<String, Object> requestMap = (HashMap<String, Object>) request.getAttribute("request");
        HashMap<String, String> body = (HashMap<String, String>) requestMap.get(("body"));
        Map<String, String> response = new HashMap<>();

        String token = (String) requestMap.get("token");

        Double latitude = Double.parseDouble(body.get("latitude"));
        Double longitude = Double.parseDouble(body.get("longitude"));
        Users user = null;

        try {
            user = usersDAO.getByName(JWTUtils.getUsername(token));
        } catch (UnsupportedEncodingException e) {
            LOG.info("[GET USER BY TOKEN - error  response - " + " e - " + e.getMessage());

            e.printStackTrace();
        }

        return eventsDAO.getUserStatusEvent(user, latitude, longitude);
    }

}