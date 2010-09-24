package swp.service;

import java.util.List;

import swp.model.AuctionPayment;
import swp.model.PaymentKey;
import swp.web.exception.AuctionPaymentExistException;

public interface AuctionPaymentService {
    
    boolean exists(PaymentKey key);
    
    void create(AuctionPayment payment) throws AuctionPaymentExistException;
    
    AuctionPayment getPayment(PaymentKey key);
    
    List<AuctionPayment> getPaymentsByUser(String username);    

}
