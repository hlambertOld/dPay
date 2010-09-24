package swp.web;

import dk.brics.jwig.AuthorizationRequiredException;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.User;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;
import swp.model.AuctionPayment;
import swp.model.AuctionPaymentRequest;
import swp.model.BasicAuctionDetails;
import swp.service.factory.ServiceFactory;
import swp.web.exception.AuctionPaymentSyntaxException;
import swp.web.exception.ItemURLReferenceException;

import java.util.List;

@URLPattern("payments")
public class PaymentsApp extends DPayAbstractApp {

    @URLPattern("$username")
    public XML execute(String username) {
        User loggedInUser = getUser();
        if (!loggedInUser.getUsername().equals(username)) {
            throw new AuthorizationRequiredException("payments");
        }
        
        XML result = getHtmlWrapper().plug("BODY", XML.parseTemplate("<p>Payments by <b><[USER]></b>:</p><ul><[ITEMS]></ul>"));
        result = result.plug("USER", username);
        List<AuctionPayment> payments = ServiceFactory.getInstance().getPaymentService().getPaymentsByUser(username);
        for (AuctionPayment payment : payments) {
            try {
                BasicAuctionDetails details = ServiceFactory.getInstance().getRemoteAuctionService().loadBasicDetails(payment.getId());
                String host = payment.getId().getHost() + "item?item=" + payment.getId().getItemId();
                result = result.plug("ITEMS", XML.parseTemplate("<li><a href=[HOST]><[ITEM_ID]> - <[ITEM_NAME]> - <[ITEM_PRICE]></a></li><[ITEMS]>").
                        plug("HOST", host).
                        plug("ITEM_ID", details.getId().getItemId()).
                        plug("ITEM_NAME", details.getItemName()).
                        plug("ITEM_PRICE", details.getPrice())
                        );
                addResponseInvalidator(payment);
                update(payment);
            } catch (AuctionPaymentSyntaxException e) {
                // Should not happen. Item all ready validated when payment was processed. Do nothing.
            } catch (ItemURLReferenceException e) {
                // Item not accessible. Do nothing
            }
        }
        return result;
    }
}
