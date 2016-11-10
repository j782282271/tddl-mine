package com.alibaba.cobar.manager.web.commons;

import com.alibaba.cobar.manager.dataobject.xml.UserDO;
import com.alibaba.cobar.manager.util.ConstantDefine;
import com.alibaba.cobar.manager.web.PermissionInterceptor;
import org.junit.Assert;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author <a href="mailto:shuo.qius@alibaba-inc.com">QIU Shuo</a>
 * @author haiqing.zhuhq 2011-9-6
 */
public class PermissionInterceptorTest extends AbstractDependencyInjectionSpringContextTests {

    private PermissionInterceptor permissionInterceptor;

    public PermissionInterceptorTest() {
        super();
    }

    public void setPermissionInterceptor(PermissionInterceptor permissionInterceptor) {
        this.permissionInterceptor = permissionInterceptor;
    }

    @Override
    protected String[] getConfigPaths() {
        return new String[]{"/WEB-INF/cobarManager-servlet.xml"};
    }

    public void testPermissionInter() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/cobarDetail.htm");
        MockHttpSession session = new MockHttpSession();
        UserDO user = new UserDO();
        user.setStatus(ConstantDefine.NORMAL);
        user.setUser_role(ConstantDefine.SYSTEM_ADMIN);
        session.setAttribute("user", user);
        request.setSession(session);

        boolean rst = permissionInterceptor.preHandle(request, new MockHttpServletResponse(), null);
        Assert.assertTrue(rst);
    }

}
