import utils.MessageUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by MSI on 2016-10-14.
 */
@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
//        StringBuilder response = new StringBuilder("{");
//        response.append("\"status\":\"ERROR\",");
//        response.append("\"message\":\"" + e.getMessage() + "\"");
//        response.append("}");

        String response = e.getStackTrace().toString();

        return Response.serverError().entity(response.toString()).type(MediaType.APPLICATION_JSON).build();
    }
}
