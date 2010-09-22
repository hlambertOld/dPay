package swp.service;

import java.net.URI;
import java.net.URL;

import swp.model.AuctionPaymentRequest;
import swp.web.exception.AuctionDoesNotExistException;
import swp.web.exception.AuctionPaymentSyntaxException;

public interface AuctionPaymentRequestService {
    
    public AuctionPaymentRequest load(URL host, URI id) throws AuctionPaymentSyntaxException, AuctionDoesNotExistException;
    
}
