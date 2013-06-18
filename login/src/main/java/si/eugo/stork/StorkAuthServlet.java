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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StorkAuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;


	private static final Logger log = LoggerFactory.getLogger(StorkAuthServlet.class.getName());


	private ArrayList<PersonalAttribute> attrList;


    @Inject
    private AuthenticatedUser authenticatedUser;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		STORKAuthnResponse authnResponse = null;
		IPersonalAttributeList personalAttributeList = null;


		byte[] decSamlToken = PEPSUtil.decodeSAMLToken(req
				.getParameter("SAMLResponse"));

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
				log.debug("PersonalAttribute Friendly Name = "
                        + pa.getFriendlyName());
				log.debug("PersonalAttribute Name = " + pa.getName());
				if (pa.getValue() != null) {
					storkAttrs.put(pa.getName(), pa);
					if (pa.getValue() != null)
						storkSessionAttrMap.put(pa.getName(), pa.getValue());
				}

				if (("dateOfBirth").equals(pa.getName())) {
					storkUser.setDateOfBirth(pa.getValue().get(0));
				} else if (("surname").equals(pa.getName())) {
					storkUser.setSurname(pa.getValue().get(0));
				} else if (("givenName").equals(pa.getName())) {
					storkUser.setGivenName(pa.getValue().get(0));
				} else if (("eIdentifier").equals(pa.getName())) {
					storkUser.seteId(pa.getValue().get(0));
					storkUser.setUsername(pa.getValue().get(0));
				} else if ("textResidenceAddress".equals(pa.getName())) {
                    storkUser.setTextResidenceAddress(pa.getValue().get(0));
				} else if ("canonicalResidenceAddress".equals(pa.getName())) {
                    storkUser.setCanonicalResidenceAddress(pa.getValue().get(0));
				} else if ("gender".equals(pa.getName())) {
                    storkUser.setGender(pa.getValue().get(0));
				} else if ("nationalityCode".equals(pa.getName())) {
                    storkUser.setNationalityCode(pa.getValue().get(0));
				} else if ("countryCodeOfBirth".equals(pa.getName())) {
                    storkUser.setCountryCodeOfBirth(pa.getValue().get(0));
                }
			}
			storkUser.setStorkAttrs(storkAttrs);

            authenticatedUser.login(storkUser);

            //TODO Decide on JAAS
			//req.login(storkUser.getUsername(), storkUser.geteId());
			
			//TODO InitURL
			resp.sendRedirect("main.xhtml");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
