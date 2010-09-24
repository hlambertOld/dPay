package swp.web;

import dk.brics.jwig.AuthorizationRequiredException;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.User;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;
import swp.model.AuctionPayment;
import swp.model.AuctionPaymentRequest;
import swp.service.factory.ServiceFactory;

import java.util.List;

@URLPattern("payments")
public class PaymentsApp extends WebApp {

    @URLPattern("$username")
    public XML execute(String username) {
        User loggedInUser = getUser();
        if (loggedInUser == null || !loggedInUser.getUsername().equals(username)) {
            throw new AuthorizationRequiredException("payments");
        }
        
        XML result = getWrapper().plug("USER", username);
        List<AuctionPayment> payments = ServiceFactory.getInstance().getPaymentService().getPaymentsByUser(username);
        for (AuctionPayment payment : payments) {
            // You might wonder why we don't plug the host. The reason is that putting <[HOST]> in the href tag will mean that the template
            // is not valid XML. Can't see a way out of concatenation.
            String host = payment.getId().getHost() + "item?item=" + payment.getId().getItemId();
            result = result.plug("ITEMS", XML.parseTemplate("<li><a href=\"" + host + "\"><[ITEMID]></a></li><[ITEMS]>").
                    plug("ITEMID", payment.getId().getItemId()));
            addResponseInvalidator(payment);
            update(payment);
        }
        return result;
    }

    private XML getWrapper() {
        return XML.parseTemplate("<html>" +
                "<head>" +
                "<title>dPay</title>" +
                "</head>" +
                "<body>" +
                "<p>Payments by <b><[USER]></b>:</p>" +
                "<ul><[ITEMS]></ul>" +
                "</body>" +
                "</html>");
    }
}
