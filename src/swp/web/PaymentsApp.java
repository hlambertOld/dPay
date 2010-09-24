package swp.web;

import dk.brics.jwig.AuthorizationRequiredException;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.Priority;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.User;
import dk.brics.jwig.WebContext;
import dk.brics.xact.XML;
import swp.model.AuctionPayment;
import swp.model.BasicAuctionDetails;
import swp.service.factory.ServiceFactory;
import swp.web.exception.AuctionPaymentSyntaxException;
import swp.web.exception.ItemURLReferenceException;

import java.util.List;

@URLPattern("payments")
public class PaymentsApp extends DPayAbstractApp {

    /**
     * Presents a page with the users purchase summary
     * It is required that the user is logged in
     *
     * @param username username
     * @return an XML object containing user purchase history
     */

    @URLPattern("$username")
    @Priority(WebContext.PRE_CACHE)
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
            } catch (AuctionPaymentSyntaxException e) {
                throw new BadRequestException(e.getMessage());
            } catch (ItemURLReferenceException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return result;
    }
}
