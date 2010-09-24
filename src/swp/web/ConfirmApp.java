package swp.web;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import swp.service.factory.ServiceFactory;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;
import swp.model.PaymentKey;


@URLPattern("confirm")
public class ConfirmApp extends DPayAbstractApp{
    
    @URLPattern("")
    public XML execute(String auctionserver, String item) throws BadRequestException {
        PaymentKey id = new PaymentKey(convertURL(auctionserver, "auctionserver"), convertURI(item, "item"));
        boolean paid = ServiceFactory.getInstance().getPaymentService().exists(id);
        addResponseInvalidator(id);
        update(id);
        return getWrapper().plug("STATUS_CODE", paid ? "OK" : "NO");
    }
    
    private XML getWrapper(){
        XML.getNamespaceMap().put("s", "https://services.brics.dk/java/courseadmin/SWP/payment");
        return XML.parseTemplate(
                "<s:status>" +
                "<[STATUS_CODE]>" +
                "</s:status>");
    }
    
    

}