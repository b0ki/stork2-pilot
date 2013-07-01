package si.eugo.stork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Matija Mazi <br/>
 * @created 6/13/13 3:12 PM
 */
@SessionScoped
public class AuthenticatedUser implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(AuthenticatedUser.class);

    private StorkUser user;

    public void login(StorkUser storkUser) {
        this.user = storkUser;
    }

    public void logout() {
        user = null;
    }

    @Produces @Authenticated @Named("storkUser")
    public StorkUser getUser() {
        return user;
    }
}
