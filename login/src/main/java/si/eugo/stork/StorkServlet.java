package si.eugo.stork;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;
import eu.stork.peps.auth.commons.STORKAuthnRequest;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;

public class StorkServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(StorkServlet.class.getName());

	private String SAMLRequest;
	private String samlRequestXML;

	private static Properties configs;
	private static SpocsStorkConfig xconfigs;
	private static ArrayList<Country> countries;
	private static ArrayList<String> attributeNameList;
	private static ArrayList<PersonalAttribute> attributeList;

	private static String spId;
	private static String spSector;
	private static String spInstitution;
	private static String spApplication;
	private static String spCountry;

	/* Requested parameters */
	private String pepsUrl;
	private String pepsUrl2;
	private String qaa;
	private String citizen;
	private String returnUrl;
	private String service;

	private String spUrl;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		//
		// StorkThreadLocal.setStorkUser(new StorkUser("ROOT"));
		//
		// log.info("Success");
		//
		// req.login("root","gtn");
		// super.doGet(req, resp);
		// LOG.info("[execute] Start");
		try {
//			log.info("Trying SPUtil.loadConfigs(Constants.SP_PROPERTIES);! chgd at 12:47");
//			configs = SPUtil.loadConfigs(Constants.SP_PROPERTIES);
			
			File f = new File(System.getProperty("jboss.server.config.dir"), "sp.properties");
			InputStream is = new FileInputStream(f);
			configs = SPUtil.loadConfigs(is);

		} catch (IOException e) {
			throw new ApplicationSpecificServiceException(
					"Could not load configuration file", e.getMessage());
		}
		log.info("Trying SPUtil.loadConfigs(Constants.SP_PROPERTIES);! SUCCESS");

		spUrl = configs.getProperty(Constants.SP_URL);

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

		try {
			JAXBContext ctx = JAXBContext.newInstance(SpocsStorkConfig.class);
			File f = new File(System.getProperty("jboss.server.config.dir"), "storkconfig.xml");
			InputStream is = SPUtil.loadConfInputStream("storkconfig.xml");// this.getClass().getClassLoader().getResourceAsStream("storkconfig.xml");
			xconfigs = (SpocsStorkConfig) ctx.createUnmarshaller().unmarshal(f);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

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

		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("eIdentifier");
		// att.setIsRequired(true);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("givenName");
		// att.setIsRequired(true);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("surname");
		// att.setIsRequired(true);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("eMail");
		// att.setIsRequired(true);
		// pAttList.add(att);
		// }
		// // if (service.equals("display")) {
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("countryCodeOfBirth");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("nationalityCode");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("gender");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("dateOfBirth");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		//
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("inheritedFamilyName");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		//
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("adoptedFamilyName");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("maritalStatus");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("textResidenceAddress");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("canonicalResidenceAddress");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("title");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("residencePermit");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("pseudonym");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("age");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }

		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("citizenQAALevel");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("fiscalNumber");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }
		// {
		// PersonalAttribute att = new PersonalAttribute();
		// att.setName("citizen509Certificate");
		// att.setIsRequired(false);
		// pAttList.add(att);
		// }

		// }

		// request.getSession().setAttribute("service", service);
		// request.getSession().setAttribute("citizen", citizen);

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
			System.out
					.println("STORKSAMLEngine.getInstance(Constants.SP_CONF); DONE ");
			authnRequest = engine.generateSTORKAuthnRequest(authnRequest);
			System.out
					.println("engine.generateSTORKAuthnRequest(authnRequest); DONE");
		} catch (STORKSAMLEngineException e) {
			throw new ApplicationSpecificServiceException(
					"Could not generate token for Saml Request",
					e.getErrorMessage());
		}

		token = authnRequest.getTokenSaml();

		SAMLRequest = PEPSUtil.encodeSAMLToken(token);
		samlRequestXML = new String(token);

		resp.setContentType("text/html; charset=UTF-8");

		PrintWriter out = resp.getWriter();
		out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
		out.write("<html>");
		out.write("<head>");
		out.write("<title>peps-redirect</title>");
		out.write("</head>");
		out.write("<body onload=\"document.redirectForm.submit();\">"); //onload=\"document.redirectForm.submit();\"
		out.write("<form name=\"redirectForm\"  action=\"https://peps-test.mju.gov.si/PEPS/ServiceProvider\" method=\"post\" target=\"_self\" >");
		out.write("<input type=\"hidden\" name=\"SAMLRequest\" value=\""
				+ SAMLRequest + "\"/>");
		out.write("<input type=hidden name=\"country\" value=\"" + citizen
				+ "\"/>");
		//out.write("<input type=\"submit\"/>");
		out.write("</form>");
		out.write("</body>");
		out.write("</html>");

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
