package swp.web;

import java.net.URI;
import java.net.URISyntaxException;

import swp.model.AuctionPayment;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;

@URLPattern("confirm")
public class ConfirmApp extends WebApp{
    
    @URLPattern("")
    public XML execute(String auctionserver, String item) throws BadRequestException {
        try {
            URI id = new URI(item);
            String status;
            AuctionPayment payment = getPayment(auctionserver, id);
            if(payment != null){
                status = "OK";
            } else {
                status = "NO";
            }
            return getWrapper().plug("STATUS_CODE", status);
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    private AuctionPayment getPayment(String host, URI id){
        return null;
    }
    
    private XML getWrapper(){
        XML.getNamespaceMap().put("p", "https://services.brics.dk/java/courseadmin/SWP/payment");
        return XML.parseTemplate(
                "<p:status>" +
                "<[STATUS_CODE]>" +
                "</p:status>");
    }

}
