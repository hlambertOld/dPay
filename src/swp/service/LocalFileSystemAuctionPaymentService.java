package swp.service;

import dk.brics.xact.Element;
import dk.brics.xact.Node;
import dk.brics.xact.NodeList;
import dk.brics.xact.XML;
import dk.brics.xact.operations.XMLPrinter;
import swp.model.AuctionPayment;
import swp.model.PaymentKey;
import swp.service.util.WorkDirectory;
import swp.web.exception.AuctionPaymentExistException;

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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class implements the AuctionPaymentService. To maintain persistence the AuctionPaymens
 * are stored in a XML-file on the hard drive. All methods are synchronized to prevent
 * corruption of the file. This implementation will not perform well on a large number of
 * AuctionPayments, but is implemented to maintain persistence.
 */

public class LocalFileSystemAuctionPaymentService implements AuctionPaymentService {

    private static final File SOURCE_FILE = new File(WorkDirectory.getInstance().getWorkDirectory(), "payments.xml");

    public synchronized void create(AuctionPayment payment)
            throws AuctionPaymentExistException {
        Map<PaymentKey, AuctionPayment> payments = loadPayments();
        if (payments.containsKey(payment.getId())) {
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
     *
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

    private void savePayments(Map<PaymentKey, AuctionPayment> map) {
        try {
            XML result = getWrapper();
            for (Entry<PaymentKey, AuctionPayment> keyAuctionPaymentEntry : map.entrySet()) {
                result = result.plug("PAYMENT", serializeSingle(keyAuctionPaymentEntry.getValue()));
            }
            System.out.println(result.getString());
            FileOutputStream outStream;
            outStream = new FileOutputStream(SOURCE_FILE);
            XMLPrinter.print(result, outStream, "UTF-8", false, true);
            outStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("should never happen", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("should never happen", e);
        } catch (IOException e) {
            throw new RuntimeException("should never happen", e);
        }
    }


    private Map<PaymentKey, AuctionPayment> loadPayments() {
        try {
            Map<PaymentKey, AuctionPayment> result = new HashMap<PaymentKey, AuctionPayment>();
            if (!SOURCE_FILE.exists()) {
                return result;
            }

            FileInputStream inStream = new FileInputStream(SOURCE_FILE);
            XML.getNamespaceMap().put("p", "https://services.brics.dk/java/courseadmin/SWP/payment");
            XML source = XML.parseDocument(inStream);
            NodeList<Node> list = source.get("//p:payment");
            for (Node node : list) {
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
            throw new RuntimeException("should never happen", e);
        } catch (MalformedURLException e) {
            throw new RuntimeException("should never happen", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("should never happen", e);
        } catch (IOException e) {
            throw new RuntimeException("should never happen", e);
        }
    }

    private XML getWrapper() {
        XML.getNamespaceMap().put("p", "https://services.brics.dk/java/courseadmin/SWP/payment");
        return XML.parseTemplate("<p:payments><[PAYMENT]></p:payments>");
    }

    private XML serializeSingle(AuctionPayment payment) {
        XML result = XML.parseTemplate("<p:payment buyer=[BUYER] server=[SERVER] item=[ITEM]/>" +
                "<[PAYMENT]>");
        result = result.plug("BUYER", payment.getBuyer());
        result = result.plug("SERVER", payment.getId().getHost());
        result = result.plug("ITEM", payment.getId().getItemId());
        return result;
    }

}
