package si.eugo.stork;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Matija Mazi <br/>
 * @created 7/1/13 12:28 PM
 */
@SessionScoped
@Named
public class StorkPostData implements Serializable {
    private String citizen;
    private String SAMLRequest;

    public String getCitizen() {
        return citizen;
    }

    public void setCitizen(String citizen) {
        this.citizen = citizen;
    }

    public String getSAMLRequest() {
        return SAMLRequest;
    }

    public void setSAMLRequest(String SAMLRequest) {
        this.SAMLRequest = SAMLRequest;
    }
}
