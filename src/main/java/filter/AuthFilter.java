package filter;


import auth.parts.Header;
import auth.parts.Payload;
import auth.parts.Signature;
import config.ErrorConfig;
import dao.TokensDAO;
import dao.UsersDAO;
import model.Users;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import utils.ObjectToJsonUtils;
import webservice.credentials.Token;

import javax.ejb.EJB;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by MSI on 2016-10-19.
 */
@Provider
@webservice.AuthFilter
public class AuthFilter implements ContainerRequestFilter {

    @EJB
    TokensDAO tokensDAO;
    @EJB
    UsersDAO usersDAO;

    Header header;
    Payload payload;
    String signature;


    final static Logger LOG = Logger.getLogger(AuthFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String result = new BufferedReader(new InputStreamReader(requestContext.getEntityStream()))
                .lines().collect(Collectors.joining("\n"));
        LOG.info("[CONNECTION EVENT: INFO - " + result + "]");

        // Request to hashmap
//        Map<String, String> request = new ObjectMapper().readValue(result, TypeFactory.mapType(HashMap.class, String.class, String.class));
        HashMap<String, Object> request = new ObjectMapper().readValue(result, HashMap.class);

        // Find token in request
        String token = (String) request.get("token");

        if (token == null) {
            LOG.error("[CONNECTION EVENT: ERROR - " + "Token is empty!" + "]");
            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
        }

        // Split token
        split(token);

        //TODO Fix some issues with finding token in database
        // Get user from payload
        Users user = usersDAO.getByName(payload.getName());

        String oldSignature = signature;
        String newSignature = new Signature(header.toBase64(), payload.toBase64(), tokensDAO.getKey(user)).toBase64();

        if (oldSignature.equals(newSignature)) {
            // Signatures are ok
        }
        else {
            // Need new token
            token = tokensDAO.generateToken(user);
            request.put("token", token);

//            String newBody = ObjectToJsonUtils.convertToJson(request);

            // Change to new body
//            InputStream in = new ByteArrayInputStream(newBody.getBytes("UTF-8"));
//            requestContext.setEntityStream(in);
        }
        requestContext.setProperty("request", request);

        LOG.info("");
    }

    private void split(String json) {
        String[] subString = json.split("\\.");

        try {
            header = new Header(new String(Base64.decodeBase64(subString[0].getBytes("UTF-8"))));
            payload = new Payload(new String(Base64.decodeBase64(subString[1].getBytes("UTF-8"))));
//            signature = Base64.decodeBase64(subString[2].getBytes("UTF-8"));
            signature = subString[2];
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
//
//    @Override
//    public void destroy() {
//
//    }

//    public class MyRequestWrapper extends HttpServletRequestWrapper {
//        private final String body;
//        public MyRequestWrapper(HttpServletRequest request) throws IOException {
//            super(request);
//            StringBuilder stringBuilder = new StringBuilder();
//            BufferedReader bufferedReader = null;
//            try {
//                InputStream inputStream = request.getInputStream();
//                if (inputStream != null) {
//                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                    char[] charBuffer = new char[128];
//                    int bytesRead = -1;
//                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                        stringBuilder.append(charBuffer, 0, bytesRead);
//                    }
//                } else {
//                    stringBuilder.append("");
//                }
//            } catch (IOException ex) {
//                throw ex;
//            } finally {
//                if (bufferedReader != null) {
//                    try {
//                        bufferedReader.close();
//                    } catch (IOException ex) {
//                        throw ex;
//                    }
//                }
//            }
//            body = stringBuilder.toString();
//        }
//
//        @Override
//        public ServletInputStream getInputStream() throws IOException {
//            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
//            ServletInputStream servletInputStream = new ServletInputStream() {
//                public int read() throws IOException {
//                    return byteArrayInputStream.read();
//                }
//            };
//            return servletInputStream;
//        }
//
//        @Override
//        public BufferedReader getReader() throws IOException {
//            return new BufferedReader(new InputStreamReader(this.getInputStream()));
//        }
//
//        public String getBody() {
//            return this.body;
//        }
//    }

}
