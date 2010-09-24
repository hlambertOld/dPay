package swp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dk.brics.xact.Element;
import dk.brics.xact.Node;
import dk.brics.xact.NodeList;
import dk.brics.xact.XML;
import dk.brics.xact.operations.XMLPrinter;

import swp.model.AuctionPayment;
import swp.model.PaymentKey;
import swp.web.exception.AuctionPaymentExistException;

/**
 * This class implements the AuctionPaymentService. To maintain persistence the AuctionPaymens 
 * are stored in a XML-file on the hard drive. All methods are synchronized to prevent 
 * corruption of the file. This implementation will not perform well on a large number of
 * AuctionPayments, but is implemented to maintain persistence.  
 */

public class LocalFileSystemAuctionPaymentService implements AuctionPaymentService{
    
    private static final File sourceFile = new File("C:/Users/hans/Desktop/payments.xml");

    public synchronized void create(AuctionPayment payment)
    throws AuctionPaymentExistException {
        Map<PaymentKey, AuctionPayment> payments = loadPayments();
        if(payments.containsKey(payment.getId())) {
            throw new AuctionPaymentExistException("The AuctionPayment already exists");
        }
        payments.put(payment.getId(), payment);
        savePayments(payments);
    }

    public synchronized boolean exists(PaymentKey key) {
        return loadPayments().containsKey(key);
    }

    public synchronized AuctionPayment getPayment(PaymentKey key) {
        return loadPayments().get(key);
    }

    /**
     * Returns a list of payments by the given userName. Note that this will concatenate the payments of a user
     * regardless of the origin of the auction item
     * @param username the user name
     * @return the list of payments
     */
    
    public synchronized List<AuctionPayment> getPaymentsByUser(String username) {
        Map<PaymentKey, AuctionPayment> payments = loadPayments();
        List<AuctionPayment> result = new ArrayList<AuctionPayment>();
        for (AuctionPayment payment : payments.values()) {
            if (payment.getBuyer().equals(username)) {
                result.add(payment);
            }
        }
        return result;
    }

    private void savePayments(Map<PaymentKey, AuctionPayment> map){
        try {
            XML result = getWrapper();
            Iterator<Entry<PaymentKey, AuctionPayment>> iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                result = result.plug("PAYMENT", serializeSingle(iterator.next().getValue())); 
            }
            System.out.println(result.getString());
            FileOutputStream outStream;
            outStream = new FileOutputStream(sourceFile);
            XMLPrinter.print(result, outStream, "UTF-8", false, true);
            outStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private Map<PaymentKey, AuctionPayment> loadPayments() { 
        try {
            Map<PaymentKey, AuctionPayment> result = new HashMap<PaymentKey, AuctionPayment>();
            FileInputStream inStream = new FileInputStream(sourceFile);
            XML.getNamespaceMap().put("p", "https://services.brics.dk/java/courseadmin/SWP/payment");
            XML source = XML.parseDocument(inStream);
            NodeList<Node> list = source.get("//p:payment");
            for(Node node : list){
                Element temp = (Element) node;
                String buyer = temp.getAttribute("buyer");
                URL server = new URL(temp.getAttribute("server"));
                URI id = new URI(temp.getAttribute("item"));
                PaymentKey key = new PaymentKey(server, id);
                AuctionPayment value = new AuctionPayment(key, buyer);
                result.put(key, value);
            }
            inStream.close();
            return result;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private XML getWrapper(){
        XML.getNamespaceMap().put("p", "https://services.brics.dk/java/courseadmin/SWP/payment");
        return XML.parseTemplate("<p:payments><[PAYMENT]></p:payments>");
    }

    private XML serializeSingle(AuctionPayment payment){
        XML result =  XML.parseTemplate("<p:payment buyer=[BUYER] server=[SERVER] item=[ITEM]/>" +
        		"<[PAYMENT]>");
        result = result.plug("BUYER", payment.getBuyer());
        result = result.plug("SERVER", payment.getId().getHost());
        result = result.plug("ITEM", payment.getId().getItemId());
        return result;
    }

}
