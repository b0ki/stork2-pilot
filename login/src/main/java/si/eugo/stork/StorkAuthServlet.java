package si.eugo.stork;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StorkAuthServlet extends HttpServlet {

    @Inject
    private StorkAuthenticator authenticator;

    private String successPage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        successPage = config.getInitParameter("success-page");
    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

        authenticator.login(req.getParameter("SAMLResponse"), req.getRemoteHost());
        resp.sendRedirect(successPage);
    }

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
