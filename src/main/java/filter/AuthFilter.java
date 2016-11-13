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
import java.util.stream.Collectors;

/**
 * Created by MSI on 2016-10-19.
 */
@Provider
//@PreMatching
@webservice.AuthFilter
public class AuthFilter implements ContainerRequestFilter {

    @EJB
    TokensDAO tokensDAO;
    @EJB
    UsersDAO usersDAO;

    String token;

    Header header;
    Payload payload;
    byte[] signature;

    Users user;

    final static Logger LOG = Logger.getLogger(AuthFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String result = new BufferedReader(new InputStreamReader(requestContext.getEntityStream()))
                .lines().collect(Collectors.joining("\n"));
        LOG.info("[Connection] Info - " + result);
        //TODO Fix some issues with finding token in database
        Token jsonToken = ObjectToJsonUtils.convertToObject(result, Token.class);

//        // Split token
//        split(jsonToken.getToken());
//
//        // Build token object
//        auth.Token authToken = new auth.Token.TokenBuilder()
//                .header(header)
//                .payload(payload)
//                .signature(signature)
//                .build();


//        token = jsonToken.getToken();
//
//
//        if (token == null)
//            throw new WebApplicationException(ErrorConfig.UNEXCEPTED_ERROR);
//
//        LOG.info("TOKEN REQUEST: " + token);
//
//        split(token);
//
//        if (oldSignature.equals(newSignature)) {
//            // Signatures are ok, no need new token
//            requestContext.setProperty("token", token);
//        }
//        else {
//            // Need new token
//            token = tokensDAO.generateToken(user);
//            Token oToken = new Token(token);
//
//            // Change token to input stream
//            String jToken = ObjectToJsonUtils.convertToJson(oToken);
//
////            InputStream in = new ByteArrayInputStream(jToken.getBytes("UTF-8"));
////            requestContext.setEntityStream(in);
//            requestContext.setProperty("token", token);
//        }
    }

    private void split(String json) {
        String[] subString = json.split("\\.");

        String sHeader = null;
        String sPayload = null;
        try {
            header = new Header(new String(Base64.decodeBase64(subString[0].getBytes("UTF-8"))));
            payload = new Payload(new String(Base64.decodeBase64(subString[1].getBytes("UTF-8"))));
            signature = Base64.decodeBase64(subString[2].getBytes("UTF-8"));
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
