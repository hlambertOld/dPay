package swp.service;

import dk.brics.xact.Element;
import dk.brics.xact.Node;
import dk.brics.xact.NodeList;
import dk.brics.xact.XML;
import dk.brics.xact.XMLException;
import swp.model.AuctionPaymentRequest;
import swp.model.BasicAuctionDetails;
import swp.model.PaymentKey;
import swp.web.exception.AuctionNotExpiredException;
import swp.web.exception.ItemURLReferenceException;
import swp.web.exception.AuctionPaymentSyntaxException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class XACTAuctionPaymentRequestService implements RemoteAuctionService {

    private static final SimpleDateFormat ISO8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


    /**
     * Loads an XML document from a remote location specified by the key
     * @param key the key identifying the location
     * @return the payment request, serialized from the received XML
     * @throws AuctionPaymentSyntaxException the XML was not well formed, the item was not expired, or no bids were found
     * @throws ItemURLReferenceException could not fetch the XML
     * @throws AuctionNotExpiredException the auction is not expired and not payable
     */

    public AuctionPaymentRequest loadPaymentRequest(PaymentKey key) throws AuctionPaymentSyntaxException, ItemURLReferenceException, AuctionNotExpiredException {
        try {
            XML source = getSource(key);
            String name = source.getString("//a:name");
            Date endDate = ISO8601_FORMAT.parse(source.getString("//a:enddate"));
            if (endDate.after(new Date())) {
                throw new AuctionNotExpiredException("The auction will not expire until " + ISO8601_FORMAT.format(endDate));
            }
            int maxBidId = getMaxBidId(source);
            Element maxBidElement = source.getElement("//a:bid[" + maxBidId + "]");
            String buyer = maxBidElement.getAttribute("owner");
            int price = new Integer(maxBidElement.getAttribute("price"));
            return new AuctionPaymentRequest(key, buyer, name, endDate, price);
        } catch (ParseException e) {
            throw new AuctionPaymentSyntaxException(e);
        } 

    }

    /**
     * Loads an XML document from a remote location specified by the key
     * @param key the key identifying the location
     * @return the basic auction details, serialized from the received XML
     * @throws AuctionPaymentSyntaxException the XML was not well formed, the item was not expired, or no bids were found
     * @throws ItemURLReferenceException could not fetch the XML
     */
    
    public BasicAuctionDetails loadBasicDetails(PaymentKey key) throws AuctionPaymentSyntaxException, ItemURLReferenceException {
        XML source = getSource(key);
        String name = source.getString("//a:name");
        int maxBidId = getMaxBidId(source);
        Element maxBidElement = source.getElement("//a:bid[" + maxBidId + "]");
        int price = new Integer(maxBidElement.getAttribute("price"));
        return new BasicAuctionDetails(key, name, price);
    } 


    private XML getSource(PaymentKey key) throws ItemURLReferenceException, AuctionPaymentSyntaxException{
        try{
            URL host = key.getHost();
            URI id = key.getItemId();
            XML result = XML.parseDocument(new URL(host + "item?item=" + id));
            XML.getNamespaceMap().put("a", "https://services.brics.dk/java/courseadmin/SWP/auction");
            return result;
        } catch (XMLException e) {
            throw new ItemURLReferenceException("Error fetching Item XML: " + e.getMessage(), e);
        } catch (MalformedURLException e) {
            throw new AuctionPaymentSyntaxException(e);
        }
    }

    private int getMaxBidId(XML source) throws AuctionPaymentSyntaxException {
        NodeList<Node> list = source.get("//a:bid");
        if (list.isEmpty())
            throw new AuctionPaymentSyntaxException("There are no bids on the auction");
        int i = 0;
        int result = -1;
        int maxPrice = 0;
        for (Node n : source.get("//a:bid")) {
            Element e = (Element) n;
            int currentPrice = new Integer(e.getAttribute("price"));
            if (currentPrice > maxPrice) {
                maxPrice = currentPrice;
                result = i;
            }
            i++;
        }
        result++;
        return result;
    }
}
