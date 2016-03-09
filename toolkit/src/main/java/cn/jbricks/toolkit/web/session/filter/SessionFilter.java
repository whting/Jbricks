package cn.jbricks.toolkit.web.session.filter;

import cn.jbricks.toolkit.web.session.Session;
import cn.jbricks.toolkit.web.session.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kelen
 */
public class SessionFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    private BeanFactory beanfactory;

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("[init]");
        this.filterConfig = filterConfig;
        beanfactory = (WebApplicationContext) filterConfig.getServletContext().
                getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.debug("[doFilter]");
        try {
            Session session = new Session((HttpServletRequest) request, (HttpServletResponse) response);
            if (!session.signatureValid()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("[doFilter]session signature is valid");
                }
                session.clearSession();
            }

            SessionHolder.setSession(session);
            chain.doFilter(request, response);
        } finally {
            SessionHolder.clearSession();
        }
    }


    @Override
    public void destroy() {
        return;
    }
}
