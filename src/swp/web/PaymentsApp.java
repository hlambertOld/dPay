package swp.web;

import java.util.List;

import swp.model.AuctionPaymentRequest;

import dk.brics.jwig.URLPattern;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;

@URLPattern("payments")
public class PaymentsApp extends WebApp{
    
    @URLPattern("$username")
    public XML execute(String username){
        return getWrapper().plug("TITLE", "This is the username").plug("BODY", username);
    }
    
    
    private XML createList(String username){
        XML result = getWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "You have purchased the following items: <[ITEM]>"));
        for(AuctionPaymentRequest item : getPurchasedItems(username)){
            result = addItem(result, item);
        }
        return result;
    }
    
    private XML addItem(XML xml, AuctionPaymentRequest item){
        return xml.plug("ITEM", "" +
        		"<xhtml:p>" +
        		    "Name: " + item.getItemName() + ", Price: " /*item.getMaximumBid()*/ +
    		    "</xhtml:p>" +
    		    "<[ITEM]>");
    }
    
    private List<AuctionPaymentRequest> getPurchasedItems(String username){
        return null;
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
