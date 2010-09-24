package swp.web;

import dk.brics.jwig.Priority;
import dk.brics.jwig.WebContext;
import swp.service.factory.ServiceFactory;
import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.URLPattern;
import dk.brics.xact.XML;
import swp.model.PaymentKey;


@URLPattern("confirm")
public class ConfirmApp extends DPayAbstractApp{
    
    @URLPattern("")
    @Priority(WebContext.PRE_CACHE)
    public XML execute(String auctionserver, String item) throws BadRequestException {
        PaymentKey id = new PaymentKey(convertURL(auctionserver, "auctionserver"), convertURI(item, "item"));
        boolean paid = ServiceFactory.getInstance().getPaymentService().exists(id);
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