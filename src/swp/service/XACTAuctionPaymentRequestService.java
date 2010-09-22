package swp.service;

import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.brics.xact.Element;
import dk.brics.xact.Node;
import dk.brics.xact.XML;

import swp.model.AuctionPaymentRequest;


public class XACTAuctionPaymentRequestService implements AuctionPaymentRequestService {

    private static SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    
    // TODO Validate XML before parsing
    // TODO Throw exception to caller
    @Override
    public AuctionPaymentRequest load(URL host, URI id) {
        try {
            XML source = XML.parseDocument(host + "item?item=" + id.toASCIIString());
            source.getNamespaceMap().put("a", "https://services.brics.dk/java/courseadmin/SWP/auction");
            String name = source.getString("//a:name");
            Date endDate = iso8601Format.parse(source.getString("//a:enddate"));
            int maxBidId = getMaxBidId(source);
            Element maxBidElement = source.getElement("//a:bid[" + maxBidId + "]");
            String buyer = maxBidElement.getAttribute("owner");
            int price = new Integer(maxBidElement.getAttribute("price"));
            return new AuctionPaymentRequest(host, id, name, endDate, buyer, price);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    private int getMaxBidId(XML source){
        int i = 0;
        int result = 0;
        int maxPrice = 0;
        for(Node n : source.get("//a:bid")){
            Element e = (Element) n;
            int currentPrice = new Integer(e.getAttribute("price"));
            if(currentPrice > maxPrice){
                maxPrice = currentPrice;
                result = i;
            }
            i++;
        }
        return result;
    }

}
