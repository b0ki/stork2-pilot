package si.gov.mnz.blp.taxid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.eugo.stork.Authenticated;
import si.eugo.stork.StorkUser;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

/**
 * @author Matija Mazi <br/>
 * @created 6/13/13 3:17 PM
 */
@Model
public class MainPage {
    private static final Logger log = LoggerFactory.getLogger(MainPage.class);

    @Inject @Authenticated
    private StorkUser storkUser;

    public void requestTaxId() {
        log.debug("MainPage.requestTaxId");
    }
}
