package si.eugo.stork;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matija Mazi <br/>
 * @created 6/13/13 3:12 PM
 */
@SessionScoped
public class AuthenticatedUser implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(AuthenticatedUser.class);

    private StorkUser user;

    private static String getAttribute(Map<String, List<String>> nameValueMap, String key) {
        List<String> values = nameValueMap.get(key);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    public void logout() {
        user = null;
    }

    @Produces @Authenticated @Named("storkUser")
    public StorkUser getUser() {
        return user;
    }

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
        StorkUser storkUser = new StorkUser();
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

        log.debug("Logged in: {}", storkUser);
        this.user = storkUser;
    }
}
