package swp.service;

import java.util.List;

import swp.model.AuctionPayment;

public interface AuctionPaymentService {
    
    public boolean excist(AuctionPayment payment);
    
    public void create(AuctionPayment payment);
    
    public List<AuctionPayment> getPaymentsByUser(String username);    

}
