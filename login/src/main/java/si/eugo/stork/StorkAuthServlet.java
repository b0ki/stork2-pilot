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
import java.util.Map;

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
			Map<String, PersonalAttribute> storkAttrs = new HashMap<String, PersonalAttribute>();
			Map<String, List<String>> nameValueMap = new HashMap<String, List<String>>();
			for (PersonalAttribute pa : attrList) {
				log.debug("PersonalAttribute Friendly Name = "
                        + pa.getFriendlyName());
				log.debug("PersonalAttribute Name = " + pa.getName());
				if (pa.getValue() != null) {
					storkAttrs.put(pa.getName(), pa);
					if (pa.getValue() != null)
						nameValueMap.put(pa.getName(), pa.getValue());
				}
			}

            storkUser.setDateOfBirth(getAttribute(nameValueMap, "dateOfBirth"));
            storkUser.setSurname(getAttribute(nameValueMap, "surname"));
            storkUser.setGivenName(getAttribute(nameValueMap, "givenName"));
            storkUser.seteId(getAttribute(nameValueMap, "eIdentifier"));
            storkUser.setUsername(getAttribute(nameValueMap, "eIdentifier"));
            storkUser.setTextResidenceAddress(getAttribute(nameValueMap, "textResidenceAddress"));
            storkUser.setCanonicalResidenceAddress(getAttribute(nameValueMap, "canonicalResidenceAddress"));
            storkUser.setGender(getAttribute(nameValueMap, "gender"));
            storkUser.setNationalityCode(getAttribute(nameValueMap, "nationalityCode"));
            storkUser.setCountryCodeOfBirth(getAttribute(nameValueMap, "countryCodeOfBirth"));

            storkUser.setStorkAttrs(storkAttrs);

            authenticatedUser.login(storkUser);

            //TODO Decide on JAAS
			//req.login(storkUser.getUsername(), storkUser.geteId());
			
			//TODO InitURL
			resp.sendRedirect("main.jsf");
		}
	}

    private String getAttribute(Map<String, List<String>> nameValueMap, String dateOfBirth) {
        List<String> values = nameValueMap.get(dateOfBirth);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
