package swp.service;

import java.net.URI;
import java.net.URL;
import java.util.List;

import swp.model.AuctionPayment;
import swp.web.exception.AuctionPaymentExistException;

public interface AuctionPaymentService {
    
    public boolean excist(URL host, URI id);
    
    public void create(AuctionPayment payment) throws AuctionPaymentExistException;
    
    public AuctionPayment getPayment(URL host, URI id);
    
    public List<AuctionPayment> getPaymentsByUser(String username);    

}
