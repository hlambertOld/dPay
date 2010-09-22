package swp.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.brics.xact.Element;
import dk.brics.xact.Node;
import dk.brics.xact.XML;
import dk.brics.xact.operations.XMLValidator;

import swp.model.AuctionPaymentRequest;
import swp.web.exception.AuctionDoesNotExistException;
import swp.web.exception.AuctionPaymentSyntaxException;


public class XACTAuctionPaymentRequestService implements AuctionPaymentRequestService {

    private static SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static final String ITEM_RNG = "item.rng";


    // TODO Validate XML before parsing
    // TODO Check if auction is expired
    @Override
    public AuctionPaymentRequest load(URL host, URI id) throws AuctionPaymentSyntaxException, AuctionDoesNotExistException {
        try {
            URL path = new URL(host + "item?item=" + id.toASCIIString());
            if(resourceAvaliable(path)){
                XML source = XML.parseDocument(new URL(host + "item?item=" + id.toASCIIString()));
                if(validate(source)){
                    source.getNamespaceMap().put("a", "https://services.brics.dk/java/courseadmin/SWP/auction");
                    String name = source.getString("//a:name");
                    Date endDate = iso8601Format.parse(source.getString("//a:enddate"));
                    int maxBidId = getMaxBidId(source);
                    Element maxBidElement = source.getElement("//a:bid[" + maxBidId + "]");
                    String buyer = maxBidElement.getAttribute("owner");
                    int price = new Integer(maxBidElement.getAttribute("price"));
                    return new AuctionPaymentRequest(host, id, name, endDate, buyer, price);
                } else {
                    throw new AuctionPaymentSyntaxException("The auction could not be validated");
                }
            } else {
                throw new AuctionDoesNotExistException("The item is not avaliable");
            }
        } catch (ParseException e) {
            throw new AuctionPaymentSyntaxException(e);
        } catch (MalformedURLException e) {
            throw new AuctionPaymentSyntaxException(e);
        } catch (IOException e) {
            throw new AuctionDoesNotExistException(e);
        } 
    }

    private boolean resourceAvaliable(URL resource) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) resource.openConnection();
        return (connection.getResponseCode() == 200);
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
        result++;
        return result;
    }

    private boolean validate(XML source){
        //        XMLValidator.loadXMLSchema(this.getClass().getResource("/item.rng"));
        return true;
    }

}
