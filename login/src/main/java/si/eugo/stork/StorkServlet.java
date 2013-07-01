package si.eugo.stork;

import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;
import eu.stork.peps.auth.commons.STORKAuthnRequest;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StorkServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(StorkServlet.class.getName());

	private String SAMLRequest;

	private static Properties configs;
	private static SpocsStorkConfig xconfigs;

	private static String spId;
	private static String spSector;
	private static String spInstitution;
	private static String spApplication;
	private static String spCountry;

	/* Requested parameters */
	private String pepsUrl;
	private String qaa;
	private String citizen;
	private String returnUrl;

    @Inject private StorkAuthenticator authenticator;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
            // todo: make this portable
			File f = new File(System.getProperty("jboss.server.config.dir"), "sp.properties");
			InputStream is = new FileInputStream(f);
			configs = SPUtil.loadConfigs(is);

		} catch (IOException e) {
			throw new ApplicationSpecificServiceException(
					"Could not load configuration file", e.getMessage());
		}
		log.info("Trying SPUtil.loadConfigs(Constants.SP_PROPERTIES);! SUCCESS");

		configs.getProperty(Constants.SP_URL);

		returnUrl = configs.getProperty(Constants.SP_RETURN);
		qaa = configs.getProperty(Constants.SP_QAALEVEL);
		spId = configs.getProperty(Constants.SP_ID);
		spSector = configs.getProperty(Constants.SP_SECTOR);
		spInstitution = configs.getProperty(Constants.SP_INSTITUTION);
		spApplication = configs.getProperty(Constants.SP_APLICATION);
		spCountry = configs.getProperty(Constants.SP_COUNTRY);
		pepsUrl = configs.getProperty("peps.url");

		citizen = req.getParameter("citizen");
		log.info("req.getParameter(\"citizen\") = " + citizen);

        readXConfigs();

		if (citizen == null || citizen.equals(""))
			throw new ApplicationSpecificServiceException(
					"Citizen country is mandatory", "");

		PersonalAttributeList pAttList = new PersonalAttributeList();
		for (StorkAttribute att : xconfigs.getAttributes()) {
			if (att.isEnabled()) {
				PersonalAttribute patt = new PersonalAttribute();
				patt.setName(att.getName());
				patt.setIsRequired(att.isRequired());
				pAttList.add(patt);
			}
		}


		byte[] token = null;

		STORKAuthnRequest authnRequest = new STORKAuthnRequest();

		authnRequest.setDestination(pepsUrl);
		authnRequest.setProviderName(spId);
		authnRequest.setQaa(Integer.parseInt(qaa));
		authnRequest.setPersonalAttributeList(pAttList);
		authnRequest.setAssertionConsumerServiceURL(returnUrl);

		// news parameters
		authnRequest.setSpSector(spSector);
		authnRequest.setSpInstitution(spInstitution);
		authnRequest.setSpApplication(spApplication);
		authnRequest.setSpCountry(spCountry);

		try {
			STORKSAMLEngine engine = STORKSAMLEngine
					.getInstance(Constants.SP_CONF);
			log.debug("STORKSAMLEngine.getInstance(Constants.SP_CONF); DONE ");
			authnRequest = engine.generateSTORKAuthnRequest(authnRequest);
			log.debug("engine.generateSTORKAuthnRequest(authnRequest); DONE");
		} catch (STORKSAMLEngineException e) {
			throw new ApplicationSpecificServiceException(
					"Could not generate token for Saml Request",
					e.getErrorMessage());
		}

		token = authnRequest.getTokenSaml();

		SAMLRequest = PEPSUtil.encodeSAMLToken(token);

        authenticator.setStorkData(citizen, SAMLRequest);

        log.debug("Redirecting to PEPS post facelet.");

        resp.sendRedirect("storkLogin/stork-redirect.jsf");
	}


    private void readXConfigs()  {
        try {
            JAXBContext ctx = JAXBContext.newInstance(SpocsStorkConfig.class);
            // todo: make this portable
            File f = new File(System.getProperty("jboss.server.config.dir"), "storkconfig.xml");
            xconfigs = (SpocsStorkConfig) ctx.createUnmarshaller().unmarshal(f);
        } catch (Exception e) {
            throw new RuntimeException("Error reding storconfig.xml", e);
        }
    }

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
