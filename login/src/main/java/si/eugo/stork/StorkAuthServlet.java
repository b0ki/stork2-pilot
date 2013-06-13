package si.eugo.stork;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class StorkAuthServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(StorkAuthServlet.class.getName());

	private String SAMLResponse;
	private String samlResponseXML;

	private ArrayList<PersonalAttribute> attrList;

	private static Properties configs;
	private static String spId;
	private static String spUrl;
	private String service;
	private String citizen;

    @Inject
    private AuthenticatedUser authenticatedUser;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		STORKAuthnResponse authnResponse = null;
		IPersonalAttributeList personalAttributeList = null;

		// LOG.info("[execute] Start");
		try {
			configs = SPUtil.loadConfigs(Constants.SP_PROPERTIES);
		} catch (IOException e) {
			throw new ApplicationSpecificServiceException(
					"Could not load configuration file", e.getMessage());
		}

		spId = configs.getProperty(Constants.SP_ID);
		spUrl = configs.getProperty(Constants.SP_URL);

		// setService( (String)request.getSession().getAttribute("service"));
		// setCitizen( (String)request.getSession().getAttribute("citizen"));

		byte[] decSamlToken = PEPSUtil.decodeSAMLToken(req
				.getParameter("SAMLResponse"));
		samlResponseXML = new String(decSamlToken);

		STORKSAMLEngine engine = STORKSAMLEngine.getInstance(Constants.SP_CONF);

		try {
			// validate SAML Token
			authnResponse = engine.validateSTORKAuthnResponse(decSamlToken,
					(String) req.getRemoteHost());
		} catch (STORKSAMLEngineException e) {
			throw new ApplicationSpecificServiceException(
					"Could not validate token for Saml Response",
					e.getErrorMessage());
		}

		if (authnResponse.isFail()) {
			throw new ApplicationSpecificServiceException(
					"Saml Response is fail", authnResponse.getMessage());
		} else {

			StorkUser storkUser = new StorkUser("");
			// Get attributes
			personalAttributeList = authnResponse.getPersonalAttributeList();
			attrList = new ArrayList<PersonalAttribute>(
					personalAttributeList.values());
			HashMap<String, PersonalAttribute> storkAttrs = new HashMap<String, PersonalAttribute>();
			HashMap<String, List<String>> storkSessionAttrMap = new HashMap<String, List<String>>();
			for (PersonalAttribute pa : attrList) {
				System.out.println("PersonalAttribute Friendly Name = "
						+ pa.getFriendlyName());
				System.out.println("PersonalAttribute Name = " + pa.getName());
				// System.out.println("PersonalAttribute Value = " +
				// pa.getValue().get(0));
				if (pa.getValue() != null) {
					storkAttrs.put(pa.getName(), pa);
					if (pa.getValue() != null)
						storkSessionAttrMap.put(pa.getName(), pa.getValue());
				}

				if (("dateOfBirth").equals(pa.getName())) {
					storkUser.setDateOfBirth(pa.getValue().get(0));
				}

				if (("surname").equals(pa.getName())) {
					storkUser.setSurname(pa.getValue().get(0));
				}

				if (("givenName").equals(pa.getName())) {
					storkUser.setGivenName(pa.getValue().get(0));
				}

				if (("eIdentifier").equals(pa.getName())) {
					storkUser.seteId(pa.getValue().get(0));
					storkUser.setUsername(pa.getValue().get(0));
				}
			}
			storkUser.setStorkAttrs(storkAttrs);

            authenticatedUser.login(storkUser);

			log.info("Calling Servlet Login!");

			req.login(storkUser.getUsername(), storkUser.geteId());
			
			log.info("Programatic login finished!\n");
			HttpSession session = req.getSession(false);
			if (session != null) {
				log.info("HttpSession != null\n");
				log.info("Authenticated user = " + req.getRemoteUser());
				if (req.getRemoteUser() != null)
					session.setAttribute("storkAttributeMap",
							storkSessionAttrMap);
			}else{
				log.info("ŠIT HttpSession == null\n");
			}
			String rUrl = (String) req.getSession().getAttribute("initialURI");
			if(rUrl==null)
				rUrl = "/spocs/pages/holder.jsf";
			resp.sendRedirect(rUrl);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
