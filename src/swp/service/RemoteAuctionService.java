package swp.service;

import swp.model.AuctionPaymentRequest;
import swp.model.BasicAuctionDetails;
import swp.model.PaymentKey;
import swp.web.exception.AuctionNotExpiredException;
import swp.web.exception.AuctionPaymentSyntaxException;
import swp.web.exception.ItemURLReferenceException;

/**
 * The interface for accessing remote auction details.
 */

public interface RemoteAuctionService {

    AuctionPaymentRequest loadPaymentRequest(PaymentKey key) throws AuctionPaymentSyntaxException, ItemURLReferenceException, AuctionNotExpiredException;

    BasicAuctionDetails loadBasicDetails(PaymentKey key) throws AuctionPaymentSyntaxException, ItemURLReferenceException;

}
