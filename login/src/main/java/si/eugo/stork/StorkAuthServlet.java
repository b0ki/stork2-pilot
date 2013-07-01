package si.eugo.stork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StorkAuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;


	private static final Logger log = LoggerFactory.getLogger(StorkAuthServlet.class.getName());


    @Inject
    private AuthenticatedUser authenticatedUser;

    private String successPage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        successPage = config.getInitParameter("success-page");
    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

        authenticatedUser.login(req.getParameter("SAMLResponse"), req.getRemoteHost());
        resp.sendRedirect(successPage);
    }

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
