package swp.web;

import dk.brics.jwig.BadRequestException;
import dk.brics.jwig.Priority;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.WebContext;
import dk.brics.xact.XML;
import swp.model.PaymentKey;
import swp.service.factory.ServiceFactory;


@URLPattern("confirm")
public class ConfirmApp extends DPayAbstractApp {

    /**
     * Returns a status code "OK" if the item has been paid or "NO" if it has not.
     * The method will not check if the auction exists on the server.
     *
     * @param auctionserver the server that holds the auction
     * @param item          the auction id
     * @return an XML object containing the given status.
     * @throws BadRequestException if the parameters are not valid URI and URL
     */

    @URLPattern("")
    @Priority(WebContext.PRE_CACHE)
    public XML execute(String auctionserver, String item) throws BadRequestException {
        PaymentKey id = new PaymentKey(convertURL(auctionserver, "auctionserver"), convertURI(item, "item"));
        boolean paid = ServiceFactory.getInstance().getPaymentService().exists(id);
        return getWrapper().plug("STATUS_CODE", paid ? "OK" : "NO");
    }

    private XML getWrapper() {
        XML.getNamespaceMap().put("s", "https://services.brics.dk/java/courseadmin/SWP/payment");
        return XML.parseTemplate(
                "<s:status>" +
                        "<[STATUS_CODE]>" +
                        "</s:status>");
    }


}