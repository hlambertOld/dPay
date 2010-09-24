import swp.web.ConfirmApp;
import swp.web.PayApp;
import swp.web.PaymentsApp;
import dk.brics.jwig.WebSite;

/**
 * The dPay website
 */
public class Main extends WebSite {

    @Override
    public void init() {
        add(new PayApp());
        add(new PaymentsApp());
        add(new ConfirmApp());
    }
}
