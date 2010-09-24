package swp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swp.model.AuctionPayment;
import swp.model.PaymentKey;
import swp.web.exception.AuctionPaymentExistException;

public class InMemoryAuctionPaymentService implements AuctionPaymentService {

    private Map<PaymentKey, AuctionPayment> payments = new HashMap<PaymentKey, AuctionPayment>();

    @Override
    public boolean exists(PaymentKey key) {
        return payments.containsKey(key);
    }

    public void create(AuctionPayment payment) throws AuctionPaymentExistException{
        if(payments.containsKey(payment.getId())) {
            throw new AuctionPaymentExistException("The AuctionPayment already exists");
        }
        payments.put(payment.getId(), payment);
    }

    @Override
    public AuctionPayment getPayment(PaymentKey key) {
        return payments.get(key);
    }

    /**
     * Returns a list of payments by the given userName. Note that this will concatenate the payments of a user
     * regardless of the origin of the auction item
     * @param username the user name
     * @return the list of payments
     */
    @Override
    public List<AuctionPayment> getPaymentsByUser(String username) {
        List<AuctionPayment> result = new ArrayList<AuctionPayment>();
        for (AuctionPayment payment : payments.values()) {
            if (payment.getUser().equals(username)) {
                result.add(payment);
            }
        }
        return result;
    }
}
