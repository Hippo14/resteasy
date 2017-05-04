package webservice;

import model.Category;
import org.jboss.resteasy.spi.HttpRequest;
import webservice.credentials.Token;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by MSI on 2017-05-01.
 */
@Produces(MediaType.APPLICATION_JSON)
@AuthFilter
public interface CategoryResource {
    @POST
    @Path("")
    List<Category> getAll(@Context HttpRequest request);
}
