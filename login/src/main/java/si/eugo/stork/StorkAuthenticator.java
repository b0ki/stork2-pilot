package si.eugo.stork;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matija Mazi <br/>
 * @created 7/1/13 1:54 PM
 *
 * todo: test that injecting StorkAuthenticator into servlets works as expected.
 */
@Stateless
public class StorkAuthenticator {

    private static final Logger log = LoggerFactory.getLogger(StorkAuthenticator.class);

    @Inject private AuthenticatedUser authenticatedUser;

    @Inject private StorkPostData storkPostData;

    public void login(String samlResponse, String remoteHost) {
        STORKAuthnResponse authnResponse;
        IPersonalAttributeList personalAttributeList;

        byte[] decSamlToken = PEPSUtil.decodeSAMLToken(samlResponse);

        STORKSAMLEngine engine = STORKSAMLEngine.getInstance(Constants.SP_CONF);

        try {
            // validate SAML Token
            authnResponse = engine.validateSTORKAuthnResponse(decSamlToken, remoteHost);
        } catch (STORKSAMLEngineException e) {
            throw new ApplicationSpecificServiceException("Could not validate token for Saml Response", e.toString());
        }

        if (authnResponse.isFail()) {
            throw new ApplicationSpecificServiceException("Saml Response is fail", authnResponse.getMessage());
        }
        // Get attributes
        personalAttributeList = authnResponse.getPersonalAttributeList();
        ArrayList<PersonalAttribute> attrList = new ArrayList<PersonalAttribute>(
                personalAttributeList.values());
        Map<String, PersonalAttribute> storkAttrs = new HashMap<String, PersonalAttribute>();
        Map<String, List<String>> nameValueMap = new HashMap<String, List<String>>();
        for (PersonalAttribute pa : attrList) {
            log.debug("PersonalAttribute Friendly Name = " + pa.getFriendlyName());
            log.debug("PersonalAttribute Name = " + pa.getName());
            if (pa.getValue() != null) {
                storkAttrs.put(pa.getName(), pa);
                if (pa.getValue() != null) {
                    nameValueMap.put(pa.getName(), pa.getValue());
                }
            }
        }

        StorkUser storkUser = new StorkUser(
                getAttribute(nameValueMap, "givenName"),
                getAttribute(nameValueMap, "surname"),
                getAttribute(nameValueMap, "dateOfBirth"),
                getAttribute(nameValueMap, "countryCodeOfBirth"),
                getAttribute(nameValueMap, "nationalityCode"),
                getAttribute(nameValueMap, "gender"),
                getAttribute(nameValueMap, "canonicalResidenceAddress"),
                getAttribute(nameValueMap, "textResidenceAddress"));
        storkUser.seteId(getAttribute(nameValueMap, "eIdentifier"));
        storkUser.setUsername(getAttribute(nameValueMap, "eIdentifier"));

        storkUser.setStorkAttrs(storkAttrs);

        log.debug("Logged in: {}", storkUser);
        authenticatedUser.login(storkUser);
    }

    private static String getAttribute(Map<String, List<String>> nameValueMap, String key) {
        List<String> values = nameValueMap.get(key);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    public void setStorkData(String citizen, String samlRequest, String pepsUrl) {
        log.debug("Setting stork data.");
        storkPostData.setCitizen(citizen);
        storkPostData.setSAMLRequest(samlRequest);
        storkPostData.setPepsUrl(pepsUrl);
    }

}
