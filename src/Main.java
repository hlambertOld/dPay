import dk.brics.jwig.WebSite;
import swp.service.util.WorkDirectory;
import swp.web.ConfirmApp;
import swp.web.PayApp;
import swp.web.PaymentsApp;

import java.io.File;

/**
 * The dPay website
 */
public class Main extends WebSite {

    @Override
    public void init() {
        WorkDirectory.getInstance().setWorkDirectory(new File((String) getProperty("workdirectory")));
        add(new PayApp());
        add(new PaymentsApp());
        add(new ConfirmApp());
    }
}
