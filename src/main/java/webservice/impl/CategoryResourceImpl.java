package webservice.impl;

import dao.CategoryDAO;
import model.Category;
import webservice.AuthFilter;
import webservice.CategoryResource;
import webservice.credentials.Token;

import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by MSI on 2017-05-01.
 */
@Path("/category")
@AuthFilter
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResourceImpl implements CategoryResource {

    @EJB
    CategoryDAO categoryDAO;

    @Override
    public Response getAll(Token token) {
        List<Category> categoryList = categoryDAO.getAll();
        if (categoryList == null)
            return Response.serverError().build();

        GenericEntity<List<Category>> ge = new GenericEntity<List<Category>>(categoryList) {};
        return Response.ok(ge).build();
    }

}
