package swp.web;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import swp.model.AuctionPayment;
import swp.service.factory.ServiceFactory;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;

@URLPattern("confirm")
public class ConfirmApp extends WebApp{
    
    @URLPattern("")
    public XML execute(String auctionserver, String item) throws BadRequestException {
        try {
            URI id = convertURI(item, "item");
            URL host = convertURL(auctionserver, "auctionserver");
            String status;
            if(paymentExist(host, id)){
                status = "OK";
            } else {
                status = "NO";
            }
            return getWrapper().plug("STATUS_CODE", status);
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getMessage());
        } catch (MalformedURLException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    private boolean paymentExist(URL host, URI id){
        return ServiceFactory.getInstance().getPaymentService().excist(host, id);
    }
    
    private XML getWrapper(){
        XML.getNamespaceMap().put("p", "https://services.brics.dk/java/courseadmin/SWP/payment");
        return XML.parseTemplate(
                "<p:status>" +
                "<[STATUS_CODE]>" +
                "</p:status>");
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

}
