import swp.web.ConfirmApp;
import swp.web.PayApp;
import swp.web.PaymentsApp;
import dk.brics.jwig.WebSite;

/**
 * A minimal WebSite implementation that sets up a single web application
 */
public class Main extends WebSite {

    @Override
    public void init() {
        add(new PayApp());
        add(new PaymentsApp());
        add(new ConfirmApp());
    }
}
