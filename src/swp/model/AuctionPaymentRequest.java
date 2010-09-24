package swp.model;

import java.io.Serializable;
import java.util.Date;

import swp.web.exception.AuctionNotExpiredException;

/**
 * Class representing a payment request.
 */

public class AuctionPaymentRequest implements Serializable {

    
    private BasicAuctionDetails details; 
    private Date endDate;
    private String buyer;
    
    /**
     * 
     * @param details the auction details
     * @param buyer the winner of the auction
     * @param endDate the date the auction expires. Need to be in the future
     * @throws AuctionNotExpiredException if endDate is not in the future
     */
    
    public AuctionPaymentRequest(BasicAuctionDetails details, String buyer, Date endDate) throws AuctionNotExpiredException {
        this.buyer = buyer;
        if (endDate.after(new Date())) {
            throw new AuctionNotExpiredException("The auction is not expired");
        }
        this.endDate = endDate;
        this.details = details;
    }

    public AuctionPaymentRequest(PaymentKey id, String buyer, String itemName, Date endDate, int price) throws AuctionNotExpiredException {
        this(new BasicAuctionDetails(id, itemName, price), buyer, endDate); 
    }

    public String getBuyer() {
        return buyer;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PaymentKey getId() {
        return details.getId();
    }

    public String getItemName() {
        return details.getItemName();
    }

    public int getPrice() {
        return details.getPrice();
    }
}
