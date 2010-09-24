package swp.model;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class AuctionPaymentRequest implements Serializable {

    
    private BasicAuctionDetails details; 
    private Date endDate;
    private String buyer;
    
    public AuctionPaymentRequest(BasicAuctionDetails details, String buyer, Date endDate) {
        this.buyer = buyer;
        this.endDate = endDate;
        this.details = details;
    }

    public AuctionPaymentRequest(PaymentKey id, String buyer, String itemName, Date endDate, int price) {
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
