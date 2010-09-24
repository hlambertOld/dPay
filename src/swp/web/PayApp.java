package swp.web;

import dk.brics.jwig.AccessDeniedException;
import dk.brics.jwig.AuthorizationRequiredException;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.JWIGException;
import dk.brics.jwig.SubmitHandler;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.User;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;
import swp.model.AuctionPayment;
import swp.model.AuctionPaymentRequest;
import swp.model.PaymentKey;
import swp.service.factory.ServiceFactory;
import swp.web.exception.AuctionPaymentExistException;
import swp.web.exception.AuctionPaymentSyntaxException;
import swp.web.exception.ItemURLReferenceException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

@URLPattern("pay")
public class PayApp extends WebApp {


    @URLPattern("")
    public XML execute(String auctionserver, String item, String returnurl) {
        try {
            URL host = convertURL(auctionserver, "auctionserver");
            URL returnAddress = convertURL(returnurl, "returnurl");
            URI itemId = convertURI(item, "item");
            PaymentKey id = new PaymentKey(host, itemId);

            User user = getUser();

            AuctionPaymentRequest paymentRequest = ServiceFactory.getInstance().getPaymentRequestService().load(id);
            addResponseInvalidator(paymentRequest);

            if (paymentRequest.getEndDate().after(new Date())) {
                return createAuctionHasNotExpired();
            }

            validateUser(user, paymentRequest);

            if (ServiceFactory.getInstance().getPaymentService().exists(id)) {
                return createPaymentExists(paymentRequest);
            }

            return createPaymentXMLType(paymentRequest, returnAddress);

        } catch (MalformedURLException e) {
            throw new BadRequestException(e.getMessage());
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getMessage());
        } catch (AuctionPaymentSyntaxException e) {
            throw new BadRequestException(e.getMessage());
        } catch (ItemURLReferenceException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private void validateUser(User user,
                              final AuctionPaymentRequest paymentRequest) {
        if (!user.getUsername().equals(paymentRequest.getBuyer())) {
            throw new AccessDeniedException("Only the buyer are able to pay the item");
        }
    }

    private URL convertURL(String value, String parameterName) throws MalformedURLException {
        if (value == null) {
            throw new BadRequestException("You need to provide the request parameter " + parameterName);
        }
        return new URL(value);
    }

    private URI convertURI(String value, String parameterName) throws URISyntaxException {
        if (value == null) {
            throw new BadRequestException("You need to provide the request parameter " + parameterName);
        }
        return new URI(value);
    }

    public User getUser() {
        User user = super.getUser();
        if (user == null) {
            throw new AuthorizationRequiredException("pay");
        }
        return user;
    }

    private XML createPaymentXMLType(AuctionPaymentRequest paymentRequest, URL returnUrl) {
        XML result = getWrapper();
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

    private SubmitHandler getHandler(final AuctionPaymentRequest paymentRequest, final URL returnUrl) {
        return new SubmitHandler() {
            public URL run() {
                try {
                    AuctionPayment payment = new AuctionPayment(paymentRequest.getId(), paymentRequest.getBuyer());
                    ServiceFactory.getInstance().getPaymentService().create(payment);
                    update(paymentRequest);
                    return returnUrl;
                } catch (AuctionPaymentExistException e) {
                    throw new JWIGException(e.getMessage(), e);
                }
            }
        };
    }

    private XML createPaymentExists(AuctionPaymentRequest paymentRequest) {
        XML result = getWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "Your payment for \"<[ITEM_NAME]>\" has already been processed." +
                        "<p>Click <a href=[RETURN_URL]>here</a> to view your payment summary.</p>"));
        result = result.plug("ITEM_NAME", paymentRequest.getItemName());
        result = result.plug("RETURN_URL", "payments/" + paymentRequest.getBuyer());
        return result;
    }

    private XML createAuctionHasNotExpired() {
        XML result = getWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "The auction has not expired yet"));
        return result;
    }

    private XML getWrapper() {
        return XML.parseTemplate("<html>" +
                "<head>" +
                "<title>" +
                "<[TITLE]>" +
                "</title>" +
                "</head>" +
                "<body>" +
                "<[BODY]>" +
                "</body>" +
                "</html>");
    }


}
