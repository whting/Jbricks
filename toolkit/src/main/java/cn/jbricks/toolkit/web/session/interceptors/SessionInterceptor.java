package cn.jbricks.toolkit.web.session.interceptors;

import cn.jbricks.toolkit.web.session.Session;
import cn.jbricks.toolkit.web.session.SessionHolder;
import cn.jbricks.toolkit.web.session.exception.SessionInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kuiyuexiang on 15/11/17.
 */
public abstract class SessionInterceptor implements HandlerInterceptor {

    protected static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Session session = SessionHolder.getSession();
        if (request instanceof MultipartHttpServletRequest) {
            session.reloadSession(request);
        }

        if (session == null || !session.isValid()) {
            logger.error("[preHandle]session is valid,sessionId=" + session.getSessionId() + ",token=" + session.getToken()
                    + ",userId=" + session.getUserId() + ",signature=" + session.getSignature());
            throw new SessionInvalidException("SessionInvalidException,session is valid");
        }

        fillSession(session);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("[postHandle]");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("[afterCompletion]");
    }

    public abstract void fillSession(Session session) throws Exception;
}
