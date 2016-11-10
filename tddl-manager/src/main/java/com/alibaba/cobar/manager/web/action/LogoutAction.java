package com.alibaba.cobar.manager.web.action;

import com.alibaba.cobar.manager.web.URLBroker;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wenfeng.cenwf 2011-4-7
 */
public class LogoutAction extends AbstractController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.getSession().removeAttribute("user");
        response.sendRedirect(URLBroker.redirectLogInPage("null"));

        return null;
    }

}
