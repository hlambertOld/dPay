package swp.web;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import swp.model.AuctionPayment;
import swp.model.AuctionPaymentRequest;
import swp.service.factory.ServiceFactory;
import swp.web.exception.AuctionDoesNotExistException;
import swp.web.exception.AuctionPaymentExistException;
import swp.web.exception.AuctionPaymentSyntaxException;
import dk.brics.jwig.AccessDeniedException;
import dk.brics.jwig.AuthorizationRequiredException;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.JWIGException;
import dk.brics.jwig.SubmitHandler;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.User;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;

// TODO Add caching control. Currently it caches the page, and will display the same page, after the payment has been processed.

@URLPattern("pay")
public class PayApp extends WebApp{
    
        
    @URLPattern("")
    public XML execute(String auctionserver, String item, final String returnurl){
        try {
            URL host = convertURL(auctionserver, "auctionserver");
            URL returnAddress = convertURL(returnurl, "returnurl");
            URI id = convertURI(item, "item");
            User user = getUser();
            AuctionPaymentRequest paymentRequest = ServiceFactory.getInstance().getPaymentRequestService().load(host, id);
            
            if(paymentRequest.getEndDate().after(new Date())){
                return createAuctionHasNotExpired();
            }
            
            validateUser(user, paymentRequest);
            
            if(ServiceFactory.getInstance().getPaymentService().excist(host, id)){
                return createPaymentExist(paymentRequest);
            }
            
            return createPaymentXMLType(paymentRequest, returnAddress);
            
        } catch (MalformedURLException e) {
            throw new BadRequestException(e.getMessage());
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getMessage());
        } catch (AuctionPaymentSyntaxException e) {
            throw new BadRequestException(e.getMessage());
        } catch (AuctionDoesNotExistException e) {
            // TODO Send 404 exception
            e.printStackTrace();
        }
        return null;
    }

    private void validateUser(User user,
            final AuctionPaymentRequest paymentRequest) {
        if(!user.getUsername().equals(paymentRequest.getBuyer())){
            throw new AccessDeniedException("Only the buyer are able to pay the item");
        }
    }

    private URL convertURL(String value, String parameterName) throws MalformedURLException {
        if(value == null){
            throw new BadRequestException("You need to provide the request parameter " + parameterName);
        }
        return new URL(value);
    }
    
    private URI convertURI(String value, String parameterName) throws URISyntaxException {
        if(value == null){
            throw new BadRequestException("You need to provide the request parameter " + parameterName);
        }
        return new URI(value);
    }
    
    public User getUser(){
        User user = super.getUser();
        if(user == null){
            throw new AuthorizationRequiredException("You must login to start the payment"); 
        }
        return user;
    }
    
    private XML createPaymentXMLType(final AuctionPaymentRequest paymentRequest, final URL returnUrl){
        XML result = getWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "You are the winner of the auction for <[ITEM_NAME]>" +
                "<xhtml:p>The price for the item is <xhtml:b><[ITEM_PRICE]></xhtml:b></xhtml:p>" +
                "<xhtml:p>Press the button below to complete the payment. " +
                "The money will be withdrawn from your account</xhtml:p>" +
                "<xhtml:form action=[ACCEPT] method=\"POST\">" +
                    "<xhtml:input  type=\"submit\" value=\"Accept purchase\"/>" +
                "</xhtml:form>"));
        result = result.plug("ITEM_NAME", paymentRequest.getItemName());
        result = result.plug("ITEM_PRICE", paymentRequest.getPrice());
        result = result.plug("TITLE", "Payment Request for " + paymentRequest.getItemName());
        result = result.plug("ACCEPT", getHandler(paymentRequest, returnUrl));
        return result;
    }
    
    private SubmitHandler getHandler(final AuctionPaymentRequest paymentRequest, final URL returnUrl){
        return new SubmitHandler(){
            public XML run(){
                try {
                    AuctionPayment payment = new AuctionPayment(paymentRequest.getHost(), paymentRequest.getId(), paymentRequest.getBuyer());
                    ServiceFactory.getInstance().getPaymentService().create(payment);
                    XML result = createPaymentResponse();
                    result = result.plug("ITEM_NAME", paymentRequest.getItemName());
                    result = result.plug("RETURN_URL", returnUrl);
                    return result;
                } catch (AuctionPaymentExistException e) {
                    //Should not happen
                    throw new JWIGException(e.getMessage());
                }
            }
        };
    }
    
    private XML createPaymentResponse(){
        XML result = getWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "Your payment for \"<[ITEM_NAME]>\" has now been registered." +
                "<xhtml:p>Klick <xhtml:a href=[RETURN_URL]>here</xhtml:a> to return to the auction.</xhtml:p>"));
        return result;
    }
    
    private XML createPaymentExist(AuctionPaymentRequest paymentRequest){
        XML result = getWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "Your payment for \"<[ITEM_NAME]>\" has all ready been processed." +
                "<xhtml:p>Klick <xhtml:a href=[RETURN_URL]>here</xhtml:a> to view your payment summary.</xhtml:p>"));
        result = result.plug("ITEM_NAME", paymentRequest.getItemName());
        result = result.plug("RETURN_URL", "payments/" + paymentRequest.getBuyer());
        return result; 
    }    
    
    private XML createAuctionHasNotExpired(){
        XML result = getWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "The auction has not expired yet"));
        return result; 
    }
    
    private XML getWrapper(){
        XML.getNamespaceMap().put("xhtml", "http://www.w3.org/1999/xhtml");
        return XML.parseTemplate(
                "<xhtml:html>" +
                    "<xhtml:head>" +
                        "<xhtml:title>" +
                            "<[TITLE]>" +
                        "</xhtml:title>" +
                    "</xhtml:head>" +
                    "<xhtml:body>" +
                        "<[BODY]>" +
                    "</xhtml:body>" +
                "</xhtml:html>");
    }
    

}
