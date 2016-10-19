package filter;

import auth.Token;
import dao.TokensDAO;
import dao.UsersDAO;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * Created by MSI on 2016-10-19.
 */
public class AuthFilter implements Filter {

    @EJB
    TokensDAO tokensDAO;
    @EJB
    UsersDAO usersDAO;

    final static Logger LOG = Logger.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String json = servletRequest.getParameter("token");

        LOG.info("TOKEN REQUEST: " + json);

        Token token = new Token(json, tokensDAO, usersDAO);
    }

    @Override
    public void destroy() {

    }

    private static class ResettableStreamHttpServletRequest extends
            HttpServletRequestWrapper {

        private byte[] rawData;
        private HttpServletRequest request;
        private ResettableServletInputStream servletStream;

        public ResettableStreamHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.request = request;
            this.servletStream = new ResettableServletInputStream();
        }


        public void resetInputStream() {
            servletStream.stream = new ByteArrayInputStream(rawData);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return servletStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return new BufferedReader(new InputStreamReader(servletStream));
        }


        private class ResettableServletInputStream extends ServletInputStream {

            private InputStream stream;

            @Override
            public int read() throws IOException {
                return stream.read();
            }
        }
    }

}
