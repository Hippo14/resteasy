package webservice.impl;

import dao.CategoryDAO;
import model.Category;
import org.apache.log4j.Logger;
import org.jboss.resteasy.spi.HttpRequest;
import webservice.AuthFilter;
import webservice.CategoryResource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by MSI on 2017-05-01.
 */
@Path("/category")
@AuthFilter
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class CategoryResourceImpl implements CategoryResource, Serializable {

    private UUID uuid = java.util.UUID.randomUUID();

    @EJB
    CategoryDAO categoryDAO;

    final static Logger LOG = Logger.getLogger(CategoryResourceImpl.class);

    @Override
    public List<Category> getAll(@Context HttpRequest request) {
        List<Category> categoryList = categoryDAO.getAll();

        return categoryList;
    }

}
