package swp.service;

import java.util.List;

import swp.model.AuctionPayment;
import swp.model.PaymentKey;
import swp.web.exception.AuctionPaymentExistException;

public interface AuctionPaymentService {
    
    public boolean exists(PaymentKey key);
    
    public void create(AuctionPayment payment) throws AuctionPaymentExistException;
    
    public AuctionPayment getPayment(PaymentKey key);
    
    public List<AuctionPayment> getPaymentsByUser(String username);    

}
