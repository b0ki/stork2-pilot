package si.eugo.stork;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import java.io.Serializable;

/**
 * @author Matija Mazi <br/>
 * @created 6/13/13 3:12 PM
 */
@SessionScoped
public class AuthenticatedUser implements Serializable {
    private StorkUser user;

    public void login(StorkUser user) {
        this.user = user;
    }

    public void logout() {
        user = null;
    }

    @Produces
    public StorkUser getUser() {
        return user;
    }
}
