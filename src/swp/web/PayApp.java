package swp.web;

import dk.brics.jwig.AccessDeniedException;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.JWIGException;
import dk.brics.jwig.SubmitHandler;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.User;
import dk.brics.xact.XML;
import swp.model.AuctionPayment;
import swp.model.AuctionPaymentRequest;
import swp.model.PaymentKey;
import swp.service.factory.ServiceFactory;
import swp.web.exception.AuctionNotExpiredException;
import swp.web.exception.AuctionPaymentExistException;
import swp.web.exception.AuctionPaymentSyntaxException;
import swp.web.exception.ItemURLReferenceException;

import java.net.MalformedURLException;
import java.net.URL;

@URLPattern("pay")
public class PayApp extends DPayAbstractApp {


    /**
     * Presents a page where the user can accept the payment of an item.
     * If the payment has been processed all ready, the user will be told and
     * given a link to his payment overview.
     * It is required that the logged in user is the buyer
     *
     * @param auctionserver the server that holds the auction
     * @param item          the auction id
     * @param returnurl     the URL that the user will be return to
     *                      after the processing of the payment
     * @return an XML object containing the given page.
     */

    @URLPattern("")
    public XML execute(String auctionserver, String item, String returnurl) {
        try {
            URL host = convertURL(auctionserver, "auctionserver");
            String itemId = check(item, "item");
            PaymentKey id = new PaymentKey(host, itemId);

            User user = getUser();

            AuctionPaymentRequest paymentRequest = ServiceFactory.getInstance().getRemoteAuctionService().loadPaymentRequest(id);
            addResponseInvalidator(paymentRequest);

            validateUser(user, paymentRequest);

            if (ServiceFactory.getInstance().getPaymentService().exists(id)) {
                return createPaymentExists(paymentRequest);
            }

            return createPaymentXMLType(paymentRequest, returnurl);

        } catch (AuctionPaymentSyntaxException e) {
            throw new BadRequestException(e.getMessage());
        } catch (ItemURLReferenceException e) {
            throw new BadRequestException(e.getMessage());
        } catch (AuctionNotExpiredException e) {
            return createAuctionHasNotExpired();
        }
    }

    private void validateUser(User user, AuctionPaymentRequest paymentRequest) {
        if (!user.getUsername().equals(paymentRequest.getBuyer())) {
            throw new AccessDeniedException("Only the buyer are able to pay the item");
        }
    }

    private XML createPaymentXMLType(AuctionPaymentRequest paymentRequest, String returnUrl) {
        XML result = getHtmlWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "You are the winner of the auction for <[ITEM_NAME]>" +
                        "<p>The price for the item is <b><[ITEM_PRICE]></b></p>" +
                        "<p>Press the button below to complete the payment. " +
                        "The money will be withdrawn from your account</p>" +
                        "<form action=[ACCEPT] method=\"POST\">" +
                        "<input  type=\"submit\" value=\"Accept purchase\"/>" +
                        "</form>"));
        result = result.plug("ITEM_NAME", paymentRequest.getItemName());
        result = result.plug("ITEM_PRICE", paymentRequest.getPrice());
        result = result.plug("TITLE", "Payment Request for " + paymentRequest.getItemName());
        result = result.plug("ACCEPT", getHandler(paymentRequest, returnUrl));
        return result;
    }

    private SubmitHandler getHandler(final AuctionPaymentRequest paymentRequest, final String returnUrl) {
        return new SubmitHandler() {
            public URL run() {
                try {
                    AuctionPayment payment = new AuctionPayment(paymentRequest.getId(), paymentRequest.getBuyer());
                    ServiceFactory.getInstance().getPaymentService().create(payment);
                    update(paymentRequest);
                    return new URL(returnUrl);
                } catch (AuctionPaymentExistException e) {
                    throw new JWIGException(e.getMessage(), e);
                } catch (MalformedURLException e) {
                    throw new JWIGException(e.getMessage(), e);
                }
            }
        };
    }

    private XML createPaymentExists(AuctionPaymentRequest paymentRequest) {
        XML result = getHtmlWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "Your payment for \"<[ITEM_NAME]>\" has already been processed." +
                        "<p>Click <a href=[RETURN_URL]>here</a> to view your payment summary.</p>"));
        result = result.plug("ITEM_NAME", paymentRequest.getItemName());
        result = result.plug("RETURN_URL", "payments/" + paymentRequest.getBuyer());
        return result;
    }

    private XML createAuctionHasNotExpired() {
        XML result = getHtmlWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "The auction has not expired yet"));
        return result;
    }
}
