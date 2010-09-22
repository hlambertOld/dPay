package swp.service;

import java.net.URI;
import java.net.URL;

import swp.model.AuctionPaymentRequest;

public interface AuctionPaymentRequestService {
    
    public AuctionPaymentRequest load(URL host, URI id);
    
}
