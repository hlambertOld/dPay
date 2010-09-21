package swp.web;

import swp.model.AuctionItem;
import dk.brics.jwig.URLPattern;
import dk.brics.jwig.WebApp;
import dk.brics.xact.XML;

@URLPattern("pay")
public class PayApp extends WebApp{
    
    @URLPattern("")
    public XML execute(String auctionserver, String item, String returnurl){
        XML result = testMakePaymentSummary();
        result = result.plug("AUCTION_SERVER", auctionserver);
        result = result.plug("ITEM_ID", item);
        result = result.plug("RETURN_URL", returnurl);
        result = result.plug("TITLE", "BLAH");
        return result.close();
    }
    
    private XML makePaymentSummary(AuctionItem item){
        return null;
    }
    
    private XML testMakePaymentSummary(){
        XML result = createPaymentXMLType();
        result = result.plug("ITEM_NAME", "Bar");
        result = result.plug("ITEM_PRICE", "100");
        return result.plug("BUYER", "HANS");
    }
    
    private XML createPaymentXMLType(){
        XML result = getWrapper();
        result = result.plug("BODY", XML.parseTemplate(
                "You are the winner of the auction for <[ITEM_NAME]>" +
                "<xhtml:p>The price for the item is <xhtml:b><[ITEM_PRICE]></xhtml:b></xhtml:p>" +
                "<xhtml:p>Press the button below to complete the payment. " +
                "The money will be withdrawn from your account</xhtml:p>" +
                "<xhtml:form action=\"payItem\" method=\"POST\">" +
                    "<xhtml:input type=\"hidden\" name=\"item\" value=[ITEM_ID]/>" +
                    "<xhtml:input type=\"hidden\" name=\"auctionserver\" value=[AUCTION_SERVER]/>" +
                    "<xhtml:input type=\"hidden\" name=\"returnurl\" value=[RETURN_URL]/>" +
                    "<xhtml:input type=\"hidden\" name=\"buyer\" value=[BUYER]/>" +
                    "<xhtml:input  type=\"submit\" value=\"Accept purchase\"/>" +
                "</xhtml:form>"));
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
