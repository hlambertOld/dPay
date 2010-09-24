package swp.service;

import swp.model.AuctionPaymentRequest;
import swp.model.PaymentKey;
import swp.web.exception.ItemURLReferenceException;
import swp.web.exception.AuctionPaymentSyntaxException;

public interface AuctionPaymentRequestService {
    
    public AuctionPaymentRequest load(PaymentKey key) throws AuctionPaymentSyntaxException, ItemURLReferenceException;
    
}
